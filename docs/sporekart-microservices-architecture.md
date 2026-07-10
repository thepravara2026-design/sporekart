# Sporekart — Microservices Architecture Redesign
**Goal:** Long-term maintainability, independent deployability, fault isolation, team scalability
**Approach:** Strangler-fig migration from the existing monolith — not a big-bang rewrite

One honest note up front, as your architect: microservices trade *code* complexity for *operational* complexity. You're not eliminating the 970-line `OrderStateService.js` problem — you're relocating it to its own service where it can be owned, tested, and deployed in isolation, at the cost of now needing service discovery, distributed tracing, and eventual consistency. That trade is worth it here because the domain (money movement + inventory + shipping + training) genuinely has independent scaling and failure characteristics. Below is the target state and how to get there without a rewrite freeze.

---

## PART 1 — SERVICE CATALOG (data ownership is the core design decision)

The rule that makes this maintainable: **each service owns its own database. No service ever queries another service's tables directly — only through its API or its published events.** This single rule prevents 80% of the coupling that kills microservices projects.

| # | Service | Owns (its own DB) | Talks to | Scaling profile |
|---|---|---|---|---|
| 1 | **Identity Service** | users, roles, sessions, credentials | Everyone (auth check) | Read-heavy, cache aggressively |
| 2 | **Catalog Service** | products, categories, pricing, variants | Search, Inventory (via events) | Read-heavy, high cache hit rate |
| 3 | **Inventory Service** | stock, reservations, warehouse allocation | Order, Catalog (events) | Write-heavy, needs strong consistency |
| 4 | **Cart Service** | active carts, reservation TTLs | Inventory, Catalog | Ephemeral, Redis-backed |
| 5 | **Order Service** | orders, order state machine, order history | Payment, Inventory, Fulfillment (saga orchestrator) | Write-heavy, core consistency domain |
| 6 | **Payment Service** | payment records, refunds, PSP orchestration | Razorpay + backup PSP, Order (events) | Low volume, extremely high correctness bar |
| 7 | **Fulfillment Service** | shipments, tracking events, carrier abstraction | Shiprocket + backup carrier, Order (events) | Bursty, webhook-driven |
| 8 | **Training Service** | courses, batches, enrollments, progress, certificates | Payment, Identity, Notification | Moderate, mostly read |
| 9 | **Content Service** | blogs, stories, SEO metadata | Search (events) | Read-heavy, cacheable, CDN-friendly |
| 10 | **Notification Service** | templates, delivery logs, preferences | Email/SMS/WhatsApp providers | Async, queue-driven, easiest to extract first |
| 11 | **Search Service** | search index (denormalized, eventually consistent copy) | Consumes events from Catalog/Training/Content | Read-heavy, rebuildable from events |
| 12 | **Analytics Service** | event store, dashboards | Consumes events from everyone | Append-only, decoupled by design |
| 13 | **Risk Service** | fraud rules, scores, abuse history | Order, Payment (sync call at checkout) | Low latency requirement on the hot path |
| 14 | **Support Service** *(new)* | tickets, escalations | Reads order/shipment context via API calls | Low volume |

**Deliberately NOT split further:** Order + its state machine stays as one service, not per-state-microservices. Splitting a state machine across services is a classic anti-pattern — it turns an atomic state transition into a distributed transaction for no benefit. Keep the state machine's *logic* in one place; only extract when a genuinely separate bounded context exists (which Payment, Inventory, Fulfillment are).

---

## PART 2 — SYSTEM TOPOLOGY

```
                            ┌───────────────────┐
                            │   CDN (static,      │
                            │   images, cached     │
                            │   GET responses)     │
                            └─────────┬───────────┘
                                      │
                            ┌─────────▼───────────┐
                            │   API Gateway         │  Kong / AWS API GW / Envoy
                            │   - JWT validation     │
                            │   - rate limiting       │
                            │   - request routing     │
                            │   - BFF aggregation      │  (web BFF, mobile BFF, admin BFF)
                            └─────────┬───────────┘
                                      │
              ┌───────────────────────┼────────────────────────┐
              │           SERVICE MESH (Istio/Linkerd)           │
              │   mTLS between services · retries · circuit      │
              │   breaking · load balancing · distributed trace  │
              │   context propagation                             │
              └───────────────────────┬────────────────────────┘
                                      │
   ┌─────────┬─────────┬─────────────┼─────────────┬─────────┬─────────┐
   ▼         ▼         ▼             ▼             ▼         ▼         ▼
Identity  Catalog   Inventory     Order (saga    Payment  Fulfillment Training
Service   Service   Service       orchestrator)  Service  Service    Service
   │         │         │             │             │         │         │
   └────┬────┴────┬────┴──────┬──────┴──────┬──────┴────┬────┴────┬────┘
        │         │           │             │            │         │
        ▼         ▼           ▼             ▼            ▼         ▼
   ┌────────────────────────────────────────────────────────────────┐
   │              EVENT BUS (Kafka / SQS+SNS / EventBridge)           │
   │   order.created · payment.verified · inventory.reserved ·       │
   │   shipment.delivered · refund.completed · enrollment.completed  │
   └───────┬──────────────┬──────────────┬──────────────┬────────────┘
           ▼              ▼              ▼              ▼
     Notification    Search Index    Analytics       Risk Scoring
     Service          Consumer        Pipeline        (async, post-hoc)
```

**Why the event bus is the actual backbone, not the API Gateway:** the gateway handles synchronous client→service calls. Everything service→service that isn't a real-time query goes through Kafka. This is what replaces your current cron-polling pattern (`runCancelWindowCleanup`, `runAutoRefundSweep`) — a state change publishes an event the instant it happens, and every interested service reacts immediately instead of waiting for the next poll interval.

---

## PART 3 — THE HARD PART: DISTRIBUTED TRANSACTIONS (Saga Pattern)

This is where most microservices redesigns quietly fall apart. Your checkout flow currently touches Inventory, Payment, and Order in what's implicitly one transaction inside a monolith. Split across services, you cannot use a database transaction anymore — you need a **saga**: a sequence of local transactions, each with a defined compensating action if a later step fails.

**Order Service is the saga orchestrator** — it owns the sequence and issues compensations. This keeps the complexity in one place (matches your existing `OrderStateService.js` role) instead of scattering "what do I do if payment fails" logic across every service.

### 3.1 Checkout Saga (orchestrated)

```
Order Service (orchestrator)
  1. Receive checkout request (with Idempotency-Key header)
  2. → Inventory Service: RESERVE stock          [sync call, ~50ms budget]
       ✗ fail → abort, return "out of stock" to client. No compensation needed yet.
  3. → Order Service: create order (status: ORDER_CREATED)
  4. → Payment Service: create PSP order            [sync call]
       ✗ fail → compensate: release inventory reservation
  5. Client completes payment on PSP checkout UI
  6. Payment Service webhook: PAYMENT_VERIFIED → publish event
  7. Order Service consumes event → status: PAYMENT_VERIFIED
       → publish order.payment_verified
  8. Inventory Service consumes event → commits reservation to real stock deduction
       ✗ fail (rare: stock vanished between reserve and commit)
         → publish inventory.commit_failed
         → Order Service consumes → triggers Payment Service refund
           (this is your existing "orphaned payment" auto-refund sweep —
            now event-triggered in seconds, not on a 5-minute cron)
```

### 3.2 Return/Refund Saga (reuses your existing phase design — it already maps well)

Your current `executeRefundProcess` phases (Restock → Create refund record → Gateway refund → Update order → Notify) is *already saga-shaped*. The redesign just moves each phase to be an event published by the owning service instead of a sequential in-process function call:

```
Order Service: RETURN_APPROVED → publish return.approved
  → Fulfillment Service: schedule pickup → publish pickup.completed
  → Order Service: RETURN_RECEIVED → publish quality_check.requested
  → (admin/QC) → publish quality_check.passed
  → Payment Service: consumes → initiates gateway refund → publish refund.completed
  → Inventory Service: consumes refund.completed (if QC passed) → restock
  → Order Service: consumes both → status: COMPLETED
  → Notification Service: consumes refund.completed → sends email/sms/whatsapp
```

Every arrow above is a Kafka event, not a function call — meaning any single service can be down for a few minutes and the saga just resumes when it comes back, instead of the whole refund flow failing.

### 3.3 Training Enrollment Saga

```
Training Service: reserve seat (optimistic lock on seats_taken)
  → Payment Service: create PSP order → verify
       ✗ fail → Training Service: release seat, offer to next waitlist entry
  → publish enrollment.confirmed
  → Notification Service: welcome sequence
  → (on course completion) Training Service: publish enrollment.completed
  → Training Service: generate certificate → publish certificate.issued
  → Identity/Catalog cross-sell: certificate.issued triggers a grower-tier
    pricing flag on the user's Catalog-facing profile — this is the
    training→commerce flywheel from the previous redesign, now just an
    event subscription instead of a manual join
```

---

## PART 4 — CONSISTENCY MODEL (be explicit about what's strong vs. eventual)

| Data | Consistency requirement | Mechanism |
|---|---|---|
| Inventory reservation at add-to-cart | Strong (can't oversell) | Synchronous call to Inventory Service, row-level lock |
| Order state transitions | Strong within Order Service | Local ACID transaction + optimistic locking (version column — you already have this) |
| Payment status → Order status | Eventually consistent (seconds) | Event-driven, acceptable because the state machine already has intermediate states (`PAYMENT_VERIFIED`) built for this |
| Search index | Eventually consistent (seconds–minutes) | Rebuilt from Catalog/Training/Content events, acceptable for search |
| Analytics dashboards | Eventually consistent (minutes) | Stream processing, never needs to be real-time |

**Design principle:** only the checkout hot-path (inventory reservation) needs strong/synchronous consistency. Nearly everything else in your current PRD was already effectively async (notifications, analytics) — the redesign just makes that explicit and formal instead of implicit.

---

## PART 5 — CROSS-CUTTING CONCERNS (what makes many services *maintainable* instead of chaotic)

| Concern | Solution |
|---|---|
| **Service discovery** | Kubernetes native DNS (`order-service.svc.cluster.local`) — no separate tool needed at this scale |
| **Config & secrets** | Centralized (Vault / AWS Secrets Manager), injected per-service, never hardcoded per your current `.env` pattern |
| **Distributed tracing** | OpenTelemetry, trace ID propagated via the service mesh from API Gateway through every hop — this is what lets you debug "why did this checkout take 4 seconds" across 3 services in one trace view |
| **API contracts** | OpenAPI spec per service, versioned, contract-tested in CI so Service A can't silently break Service B's assumptions |
| **Correlation of events** | Every published event carries `correlation_id` = originating request ID, so a saga can be traced end-to-end in the event bus |
| **Idempotency across services** | Every service that consumes an event checks a processed-events table before acting — Kafka delivers at-least-once, so consumers must be idempotent (this generalizes your existing refund idempotency-key pattern to every service) |
| **Independent CI/CD** | One pipeline per service, one repo per service (or monorepo with per-service build graphs) — a Training Service deploy can never break checkout, which is impossible to guarantee in the current single-deploy monolith |
| **Team ownership** | Each service maps to a team boundary (Conway's Law, deliberately) — e.g., a Payments team owns Payment Service end-to-end including its on-call |

---

## PART 6 — MIGRATION PLAN (Strangler Fig — extract in this order, not all at once)

Extracting in the wrong order is the #1 reason microservices migrations stall. Order by **lowest coupling + highest independent value first**, save the riskiest (Order) for when the team has practice.

| Step | Extract | Why this order | Risk |
|---|---|---|---|
| 1 | **Notification Service** | Fully stateless, no one else depends on its data, already queue-shaped in your current design | Very low |
| 2 | **Search Service** | Read-only, rebuildable from events, easy to run in parallel with the old SQL search and cut over | Low |
| 3 | **Analytics Service** | Append-only consumer, zero risk to the transactional path | Low |
| 4 | **Content Service** (blogs/stories) | No money flow, low write volume, good practice run for a "real" service extraction | Low |
| 5 | **Training Service** | Bounded, mostly self-contained, forces you to build the saga pattern for the first time on lower-stakes data before touching checkout | Medium |
| 6 | **Fulfillment Service** | Already provider-abstracted in your current design (`ProviderRegistry`) — the abstraction boundary already exists, just needs a network boundary drawn around it | Medium |
| 7 | **Inventory Service** | Now you extract this *with* the saga pattern already proven from steps 5–6 | Medium-High |
| 8 | **Payment Service** | Highest correctness bar — extract only after the team has run 2–3 sagas successfully in production | High |
| 9 | **Order Service** | Last, and it becomes the saga orchestrator wiring everything above together — by now every service it needs to call already speaks events cleanly | High |
| 10 | **Identity Service** | Often extracted early in other playbooks, but deliberately last here because *everything* calls it — cut over behind a feature flag with dual-write to old and new for a full traffic cycle before decommissioning the monolith's auth code |

**During the entire migration, the monolith stays running** — the API Gateway routes each endpoint to either the monolith or the new service, one route at a time. Nothing is "half-migrated and broken"; it's "this endpoint moved, that one hasn't yet."

---

## PART 7 — HONEST TRADE-OFFS (say this to whoever's approving budget)

- **You will need a platform/DevOps function that didn't exist before** — someone owns the service mesh, the event bus, the shared CI templates. This isn't optional overhead, it's the cost of the maintainability you're buying.
- **Local development gets harder** — a developer working on Order Service now needs Payment, Inventory, and a Kafka broker running (or solid contract-mocking) to test a checkout locally. Invest in a good `docker-compose`-based local stack early, or velocity drops.
- **Debugging trades "read one file" for "read one trace across five services."** This is *better* long-term but only if tracing is genuinely wired everywhere from day one — half-instrumented tracing is worse than no tracing (false confidence).
- **Eventual consistency will surface as real UX questions** — "why does my order still show pending 2 seconds after I paid" is a support ticket you'll get occasionally. Design the UI to show optimistic/pending states honestly rather than hiding the lag.

Do this migration when the monolith's coupling is visibly costing you (deploy risk, team collisions on the same files, the 970-line service becoming unreviewable) — not because microservices are the "correct" architecture in the abstract. Your current PRD is at almost exactly the size where this starts paying off.
