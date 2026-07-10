# Sporekart — Microservices Architecture & Flow Diagrams
**Consolidated reference:** architecture audit summary + microservices design + every flow diagram, in one file
**Diagram format:** Mermaid (renders natively on GitHub, GitLab, Notion, Obsidian, VS Code preview, and most markdown viewers)

This file merges and supersedes the two prior documents (`sporekart-PRD-audit-and-redesign.md` and `sporekart-microservices-architecture.md`) into a single reference, with every flow rendered as an actual diagram rather than ASCII art.

---

## Table of contents
1. [Audit summary](#1-audit-summary)
2. [Service catalog & data ownership](#2-service-catalog--data-ownership)
3. [System topology diagram](#3-system-topology-diagram)
4. [Checkout saga — full sequence diagram](#4-checkout-saga--full-sequence-diagram)
5. [Return & refund saga — full sequence diagram](#5-return--refund-saga--full-sequence-diagram)
6. [Training enrollment saga — full sequence diagram](#6-training-enrollment-saga--full-sequence-diagram)
7. [Event bus fan-out diagram](#7-event-bus-fan-out-diagram)
8. [Consistency model](#8-consistency-model)
9. [Migration roadmap (strangler fig)](#9-migration-roadmap-strangler-fig)

---

## 1. Audit summary

### Strongest links (kept and built on)
| # | Strength |
|---|---|
| 1 | V3 order state machine with 3-dimension model (`status` / `delivery_status` / `fulfillment_status`) |
| 2 | Row-Level Security as the primary authorization layer — defense-in-depth at the database itself |
| 3 | Idempotency keys + compensating transactions already designed into refunds |
| 4 | Feature flags as the rollout mechanism |
| 5 | Audit logging on refunds and admin actions |
| 6 | Dual-sided business model (B2C + B2B training) on one identity layer |
| 7 | Real-time sync via SSE + BroadcastChannel |
| 8 | Explicit, server-enforced cancellation/return windows, not just UI countdowns |

### Weakest links (the reason this redesign exists)
| # | Weak link | Fixed by |
|---|---|---|
| 1 | No caching layer | Redis cache-aside on Catalog + sessions (§2) |
| 2 | State transitions run on cron polling, not events | Event bus replaces polling (§3, §7) |
| 3 | Single payment gateway, no failover | Payment Service with PSP orchestration (§2) |
| 4 | Admin impersonation with no guardrails | Time-boxed, audited, step-up auth required |
| 5 | No idempotency on order *creation* | `Idempotency-Key` header, checked in saga step 1 (§4) |
| 6 | Monolithic files (110KB routes, 970-line core service) | Domain-bounded services, each independently deployable (§2) |
| 7 | No fraud/risk engine | Risk Service, async-scored on the event bus (§7) |
| 8 | Single-warehouse assumption | Inventory Service models `warehouse_id` as first-class (§2) |
| 9 | No dedicated search engine | Search Service, rebuilt from events (§7) |
| 10 | Dual state machines (v3 + legacy) running concurrently | Legacy frozen, single source of truth in Order Service |

---

## 2. Service catalog & data ownership

**The one rule that makes this maintainable:** each service owns its own database. No service ever queries another service's tables directly — only through its API or its published events.

| # | Service | Owns (its own DB) | Scaling profile |
|---|---|---|---|
| 1 | Identity Service | users, roles, sessions, credentials | Read-heavy, cache aggressively |
| 2 | Catalog Service | products, categories, pricing, variants | Read-heavy, high cache hit rate |
| 3 | Inventory Service | stock, reservations, warehouse allocation | Write-heavy, strong consistency |
| 4 | Cart Service | active carts, reservation TTLs | Ephemeral, Redis-backed |
| 5 | Order Service | orders, state machine, order history | Write-heavy, saga orchestrator |
| 6 | Payment Service | payment records, refunds, PSP orchestration | Low volume, highest correctness bar |
| 7 | Fulfillment Service | shipments, tracking events, carrier abstraction | Bursty, webhook-driven |
| 8 | Training Service | courses, batches, enrollments, progress, certificates | Moderate, mostly read |
| 9 | Content Service | blogs, stories, SEO metadata | Read-heavy, CDN-friendly |
| 10 | Notification Service | templates, delivery logs, preferences | Async, queue-driven |
| 11 | Search Service | denormalized search index | Rebuildable from events |
| 12 | Analytics Service | event store, dashboards | Append-only |
| 13 | Risk Service | fraud rules, scores, abuse history | Low latency on hot path |
| 14 | Support Service | tickets, escalations | Low volume |

---

## 3. System topology diagram

Every client request takes this path before it ever reaches business logic. The API Gateway handles synchronous client→service calls; the event bus handles everything service→service that isn't a real-time query.

```mermaid
flowchart TD
    CDN["CDN<br/><small>static assets, images, cached GET</small>"]
    Client["Client<br/><small>web, mobile, admin apps</small>"]
    Gateway["API Gateway<br/><small>JWT validation, rate limiting, BFF routing</small>"]
    Mesh["Service Mesh<br/><small>mTLS, retries, circuit breaking, tracing</small>"]

    subgraph Core["Core services"]
        direction LR
        Identity["Identity"]
        Catalog["Catalog"]
        Inventory["Inventory"]
        Order["Order<br/>(saga orchestrator)"]
        Payment["Payment"]
        Fulfillment["Fulfillment"]
        Training["Training"]
    end

    Bus["Event Bus<br/><small>Kafka / SQS+SNS / EventBridge</small>"]

    subgraph Consumers["Async consumers"]
        direction LR
        Notification["Notification"]
        Search["Search Index"]
        Analytics["Analytics"]
        Risk["Risk Scoring"]
    end

    Client --> CDN
    Client --> Gateway
    Gateway --> Mesh
    Mesh --> Core
    Core -->|publish events| Bus
    Bus --> Consumers

    classDef infra fill:#F1EFE8,stroke:#888780,color:#2C2C2A
    classDef core fill:#E1F5EE,stroke:#0F6E56,color:#04342C
    classDef bus fill:#EEEDFE,stroke:#534AB7,color:#26215C
    classDef consumer fill:#E1F5EE,stroke:#0F6E56,color:#04342C

    class CDN,Client,Gateway,Mesh infra
    class Identity,Catalog,Inventory,Order,Payment,Fulfillment,Training core
    class Bus bus
    class Notification,Search,Analytics,Risk consumer
```

---

## 4. Checkout saga — full sequence diagram

Order Service is the saga orchestrator. Every step has a defined compensating action if a later step fails — this is what replaces the implicit "one big database transaction" the monolith used to rely on.

```mermaid
sequenceDiagram
    participant C as Client
    participant O as Order Service
    participant I as Inventory Service
    participant P as Payment Service
    participant B as Event Bus

    C->>O: POST /checkout (Idempotency-Key)
    O->>I: Reserve stock
    alt out of stock
        I-->>O: Reservation denied
        O-->>C: 409 — item unavailable
    else stock available
        I-->>O: Reservation confirmed
        O->>O: Create order (ORDER_CREATED)
        O->>P: Create PSP order
        alt PSP order creation fails
            P-->>O: Error
            O->>I: Compensate — release reservation
            O-->>C: 502 — payment unavailable
        else PSP order created
            P-->>C: Razorpay checkout UI
            C->>P: Complete payment
            P->>B: publish payment.verified
            B->>O: consume payment.verified
            O->>O: status -> PAYMENT_VERIFIED
            O->>B: publish order.payment_verified
            B->>I: consume order.payment_verified
            I->>I: Commit reservation, deduct stock
            alt stock commit fails
                I->>B: publish inventory.commit_failed
                B->>O: consume event
                O->>P: Trigger auto-refund
                P->>C: Refund initiated notification
            else commit succeeds
                I->>B: publish inventory.committed
                B->>O: consume event
                O->>O: status -> APPROVED
            end
        end
    end
```

---

## 5. Return & refund saga — full sequence diagram

This reuses the existing PRD's refund phase design (restock → refund record → gateway refund → update → notify) — the redesign just makes each phase an event instead of a sequential in-process call, so any single service can be briefly down without failing the whole return.

```mermaid
sequenceDiagram
    participant Cu as Customer
    participant O as Order Service
    participant F as Fulfillment Service
    participant A as Admin (Quality Check)
    participant P as Payment Service
    participant I as Inventory Service
    participant N as Notification Service
    participant B as Event Bus

    Cu->>O: Request return (within 7-day window)
    O->>O: status -> RETURN_REQUESTED
    O->>B: publish return.requested
    B->>A: notify for review
    A->>O: Approve return
    O->>B: publish return.approved
    B->>F: consume return.approved
    F->>F: Schedule pickup with carrier
    F->>B: publish pickup.completed
    B->>O: consume event -> status: RETURN_RECEIVED
    O->>B: publish quality_check.requested
    B->>A: notify for inspection
    alt quality check fails
        A->>O: Reject return
        O->>B: publish return.rejected
        B->>N: notify customer — return declined
    else quality check passes
        A->>O: Pass quality check
        O->>B: publish quality_check.passed
        B->>P: consume event
        P->>P: Initiate gateway refund
        P->>B: publish refund.completed
        B->>I: consume refund.completed -> restock
        B->>O: consume refund.completed -> status: COMPLETED
        B->>N: consume refund.completed -> notify customer
    end
```

---

## 6. Training enrollment saga — full sequence diagram

The last two steps are the strategic core of the dual-sided business — a completed certification is what should route a trainee back into the commerce funnel as a high-intent, credibility-verified buyer.

```mermaid
sequenceDiagram
    participant Tr as Trainee
    participant T as Training Service
    participant P as Payment Service
    participant N as Notification Service
    participant Ca as Catalog Service
    participant B as Event Bus

    Tr->>T: Register for batch
    alt batch full
        T->>T: Add to waitlist
        T-->>Tr: Waitlisted — notified on opening
    else seat available
        T->>T: Reserve seat (optimistic lock)
        T->>P: Create PSP order
        alt payment fails
            P-->>T: Error
            T->>T: Release seat
            T-->>Tr: Payment failed, seat released
        else payment succeeds
            P->>B: publish payment.verified
            B->>T: consume event
            T->>T: status -> ENROLLED
            T->>B: publish enrollment.confirmed
            B->>N: consume event -> send welcome sequence
            Note over Tr,T: Trainee progresses through modules
            Tr->>T: Complete final module + assessment
            T->>T: status -> COMPLETED
            T->>T: Generate certificate (unique cert ID)
            T->>B: publish certificate.issued
            B->>Ca: consume certificate.issued
            Ca->>Ca: Flag profile as grower-tier eligible
            B->>N: consume event -> notify trainee, share verify link
        end
    end
```

---

## 7. Event bus fan-out diagram

Every domain event published anywhere in the system fans out to these four consumers independently — none of them block the transaction that produced the event, and none of them can slow down checkout by being slow themselves.

```mermaid
flowchart TD
    Bus["Event Bus<br/><small>order, payment, refund, training events</small>"]

    Bus --> Notification["Notification Service<br/><small>email, SMS, WhatsApp, retry queue</small>"]
    Bus --> Search["Search Service<br/><small>rebuilds index from events</small>"]
    Bus --> Analytics["Analytics Service<br/><small>dashboards, funnels, cohort tracking</small>"]
    Bus --> Risk["Risk Service<br/><small>async fraud + abuse scoring</small>"]

    classDef bus fill:#EEEDFE,stroke:#534AB7,color:#26215C
    classDef consumer fill:#E1F5EE,stroke:#0F6E56,color:#04342C
    classDef risk fill:#FAEEDA,stroke:#854F0B,color:#412402

    class Bus bus
    class Notification,Search,Analytics consumer
    class Risk risk
```

---

## 8. Consistency model

| Data | Consistency requirement | Mechanism |
|---|---|---|
| Inventory reservation at checkout | Strong (can't oversell) | Synchronous call to Inventory Service, row-level lock |
| Order state transitions | Strong within Order Service | Local ACID transaction + optimistic locking (version column) |
| Payment status → Order status | Eventually consistent (seconds) | Event-driven; acceptable because the state machine already has intermediate states built for this |
| Search index | Eventually consistent (seconds–minutes) | Rebuilt from Catalog/Training/Content events |
| Analytics dashboards | Eventually consistent (minutes) | Stream processing, never needs to be real-time |

Only the checkout hot-path (inventory reservation) needs synchronous consistency. Everything else was already effectively async in the original PRD (notifications, analytics) — this just makes it formal.

---

## 9. Migration roadmap (strangler fig)

Extract in order of lowest coupling + highest independent value first. The monolith keeps running throughout — the API Gateway routes each endpoint to either the monolith or the new service, one at a time.

```mermaid
flowchart LR
    S1["1. Notification<br/><small>stateless, very low risk</small>"]
    S2["2. Search<br/><small>read-only, rebuildable</small>"]
    S3["3. Analytics<br/><small>append-only consumer</small>"]
    S4["4. Content<br/><small>no money flow</small>"]
    S5["5. Training<br/><small>first real saga, lower stakes</small>"]
    S6["6. Fulfillment<br/><small>abstraction already exists</small>"]
    S7["7. Inventory<br/><small>saga pattern now proven</small>"]
    S8["8. Payment<br/><small>highest correctness bar</small>"]
    S9["9. Order<br/><small>becomes saga orchestrator</small>"]
    S10["10. Identity<br/><small>dual-write cutover, last</small>"]

    S1 --> S2 --> S3 --> S4 --> S5 --> S6 --> S7 --> S8 --> S9 --> S10

    classDef low fill:#E1F5EE,stroke:#0F6E56,color:#04342C
    classDef med fill:#FAEEDA,stroke:#854F0B,color:#412402
    classDef high fill:#FAECE7,stroke:#993C1D,color:#4A1B0C

    class S1,S2,S3,S4 low
    class S5,S6,S7 med
    class S8,S9,S10 high
```

**Honest trade-off, worth repeating here:** this buys independent deployability and fault isolation at the cost of needing a real platform/DevOps function, harder local development, and debugging that trades "read one file" for "read one trace across five services." Do this when the monolith's coupling is visibly costing you — not because microservices are correct in the abstract.

---

*This file consolidates the audit, the microservices architecture, and every flow diagram into one reference. All diagrams use Mermaid — open this file in GitHub, GitLab, Obsidian, Notion, or VS Code's markdown preview to render them.*
