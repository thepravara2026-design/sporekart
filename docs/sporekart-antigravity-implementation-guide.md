# Sporekart — Antigravity Implementation Guide
**Goal:** build the full microservices architecture in Google Antigravity using agents, in an order that never breaks integration
**Core discipline:** contracts before code, one agent per service, one shared source of truth, a hard integration gate at the end of every tier

---

## 0. How Antigravity's model maps onto this build

Antigravity gives you two surfaces — the **Editor View** (hands-on, single workspace, like a normal IDE) and the **Manager View / Mission Control** (where you spawn, monitor, and review multiple autonomous agents working in parallel across workspaces). Every agent task produces **Artifacts** — a task list, an implementation plan, diffs, screenshots, browser recordings — that you review and comment on before the agent proceeds or merges.

The feature that matters most for a 14-service build is **multi-repo workspaces** (2.0): you can open your contracts repo + several service repos in one session and tell an agent "implement the Order Service per `contracts/order-service.yaml`" and it works only inside that boundary. This is what lets you run one agent per service without them stepping on each other.

**The single biggest cause of "mess" in a multi-agent build is letting agents invent their own contracts.** Two agents building Order Service and Payment Service in parallel, each guessing at what the other's API looks like, will produce two services that don't actually integrate — even if each one individually looks correct and passes its own tests. Everything below is built around preventing that one failure mode.

---

## 1. Workspace structure

Set this up yourself before spawning a single agent — this is the skeleton every agent will build inside, not something to delegate.

```
sporekart/
├── contracts/                  ← frozen in Phase 1, read-only to service agents after
│   ├── openapi/                  one .yaml per service (14 files)
│   ├── events/                   one AsyncAPI schema per event type
│   └── db-schema/                the ERDs from the database design doc, as .sql migration stubs
├── platform/                   ← infra that isn't any one service's job
│   ├── docker-compose.yml        full local stack: Postgres x N, Redis, Kafka/Redpanda, OpenSearch
│   ├── api-gateway/               Kong/Envoy config or an Express BFF stub
│   └── ci-templates/
├── services/
│   ├── identity-service/
│   ├── catalog-service/
│   ├── inventory-service/
│   ├── cart-service/
│   ├── order-service/
│   ├── payment-service/
│   ├── fulfillment-service/
│   ├── training-service/
│   ├── content-service/
│   ├── notification-service/
│   ├── search-service/
│   ├── analytics-service/
│   ├── risk-service/
│   └── support-service/
├── frontend/
│   ├── buyer-app/
│   └── admin-dashboard/
├── docs/                        ← your three existing .md files live here as ground truth
│   ├── sporekart-PRD-audit-and-redesign.md
│   ├── sporekart-microservices-architecture.md
│   ├── sporekart-microservices-flow-diagrams.md
│   └── sporekart-microservices-database-schema.md
└── AGENTS.md                    ← root-level rules, every agent reads this first
```

Each `services/*` folder is its own git repo (or its own workspace root if you keep a monorepo — either works with Antigravity's multi-repo support; separate repos make the "one agent, one boundary" rule easier to enforce because an agent literally cannot open files outside its assigned repo).

---

## 2. Root `AGENTS.md` — paste this in before anything else

This is the file every agent reads before touching code. It encodes the rules from your architecture doc so you don't have to repeat them in every prompt.

```markdown
# Sporekart build rules

## Non-negotiable architecture rules
1. One database per service. Never write a query, ORM model, or migration that touches
   another service's tables. Cross-service data needs go through the contracts/openapi/
   spec for that service, or through an event defined in contracts/events/.
2. Every service that changes state and publishes an event MUST implement the
   outbox + processed_events pattern exactly as specified in
   docs/sporekart-microservices-database-schema.md section 18. No exceptions,
   no "simpler version for now."
3. Do not modify anything under /contracts unless the task explicitly says so.
   Contracts are frozen after Phase 1 approval. If you believe a contract is wrong,
   stop and flag it — do not silently work around it.
4. Every service exposes a GET /health endpoint returning { status, version, dependencies }.
5. Every service ships with a Dockerfile and a docker-compose entry that plugs into
   platform/docker-compose.yml without modification to other services' entries.
6. Idempotency: every event consumer checks processed_events before acting.
   Every state-changing POST/PUT that could be retried needs an idempotency key.
7. Reference source of truth: docs/sporekart-microservices-flow-diagrams.md for saga
   sequences, docs/sporekart-microservices-database-schema.md for schema.
   If code and docs disagree, docs win — flag the discrepancy, don't guess.

## Definition of done (every service, no exceptions)
- [ ] Matches its contracts/openapi/{service}.yaml exactly — no undocumented endpoints
- [ ] Outbox + processed_events implemented for every publish/consume
- [ ] Unit tests covering the state machine / core business logic
- [ ] Health check endpoint
- [ ] Dockerfile builds and runs standalone
- [ ] README documenting env vars and how to run it alone
- [ ] Structured logging with a correlation_id field on every log line
```

---

## 3. Priority build order

Two different orderings matter here and they're not the same thing:

- **Contract order** — what needs to be *specified* first, so nothing downstream guesses.
- **Build order (tiers)** — what needs to be *runnable* first, so integration testing is possible incrementally instead of only at the very end.

Because contracts are frozen in Phase 1, services in the same tier below can genuinely be built by **parallel agents in Manager View** — each one codes against the contract, not against another agent's in-progress work.

| Phase | What | Parallelizable? | Depends on |
|---|---|---|---|
| **0** | Workspace + `AGENTS.md` + `docker-compose.yml` skeleton | You do this manually | — |
| **1** | **Contracts** — OpenAPI + AsyncAPI for all 14 services, generated from `docs/` | One agent, sequential, human-reviewed before freezing | Phase 0 |
| **2 — Tier 1** | Identity, Catalog, Inventory | 3 parallel agents | Phase 1 contracts only |
| **3 — Tier 2** | Cart, Order (saga orchestrator), Payment | Order agent can start immediately (codes against Identity/Catalog/Inventory *contracts*, not their real implementations); Cart and Payment parallel to it | Phase 1 contracts |
| **4 — Tier 3** | Fulfillment, Notification | 2 parallel agents | Phase 1 contracts |
| **5 — Tier 4** | Content, Training | 2 parallel agents — Content has zero dependencies and is a good "warm-up" agent to run even during Tier 1 if you want a spare lane busy | Phase 1 contracts |
| **6 — Tier 5** | Search, Analytics, Risk, Support | 4 parallel agents — all are consumers of events other tiers produce, so build them against contract event schemas, verify against real events later | Phase 1 contracts |
| **7** | API Gateway route wiring | 1 agent, after endpoint contracts are stable | All services' OpenAPI specs |
| **8** | Frontend (buyer app + admin dashboard) | 2 parallel agents | Gateway routes |
| **9** | **Full integration pass** | Sequential, one agent orchestrating `docker-compose up` across everything | Everything above |
| **10** | Observability, CI/CD, load testing | 1–2 agents | Phase 9 passing |

**Why this order and not the strangler-fig order from the migration doc:** that earlier roadmap was for extracting services out of a *live* monolith, sequenced by risk-of-breaking-production. This is a greenfield build with no live system to protect — so the ordering logic changes to *dependency readiness for integration testing*, not risk reduction. Identity/Catalog/Inventory go first here because Order Service (the most complex service) needs their contracts finalized to be built meaningfully, not because they're low-risk.

---

## 4. Step-by-step

### Phase 0 — You, not an agent (30–60 min)
Create the folder skeleton in §1, write `AGENTS.md`, write a minimal `docker-compose.yml` with just the infra (Postgres, Redis, Kafka/Redpanda, OpenSearch) and no services yet. Commit. This is your anchor commit — every agent branches from here.

### Phase 1 — Contracts agent (single agent, Editor View is fine, this is a focused task)
Open Antigravity, point it at `contracts/` and `docs/`. Prompt:

> "Read every file in docs/. Generate an OpenAPI 3.1 spec for each of the 14 services listed in the database schema doc's table of contents, matching the field names and types in that doc exactly. Generate an AsyncAPI schema for every event named in the flow-diagrams doc (order.created, payment.verified, etc.). Do not write any service implementation code. Output only into contracts/."

Review the Artifact (it'll show you the generated spec files as a diff/plan). **This is the one review you should be the most careful about in the entire build** — every downstream agent trusts this without re-verifying it. Check especially: field names match the ERDs exactly, event names match the sequence diagrams exactly, every saga step from the flow-diagrams doc has a corresponding event schema.

Once approved: commit, tag as `contracts-v1`, and treat `contracts/` as read-only from here on (enforce this with the `AGENTS.md` rule already in place).

### Phase 2 — Tier 1 (3 parallel agents, Manager View)
Spawn three separate agent tasks, each scoped to one repo:

- Agent A → `services/identity-service/`: "Implement this service per `contracts/openapi/identity-service.yaml` and the schema in `docs/sporekart-microservices-database-schema.md` §3. Follow AGENTS.md."
- Agent B → `services/catalog-service/`: same pattern, §4.
- Agent C → `services/inventory-service/`: same pattern, §5.

Let them run asynchronously — this is exactly the "Backend Agent working independently while you do something else" pattern Antigravity is built for. Review each one's Artifact (diffs + test results) as it completes. Don't merge to main until each one's Definition of Done checklist is fully green.

**Integration checkpoint after Tier 1:** spin up just these three services + infra in docker-compose, hit each `/health` endpoint, run each service's own test suite against a live Postgres. Don't proceed to Tier 2 until this is clean — catching a contract mismatch here costs you one service's rework, catching it after Tier 3 costs you three.

### Phase 3 — Tier 2 (Cart, Order, Payment)
Same pattern. The Order Service agent is the most complex — it's implementing the saga orchestrator, `saga_state` table, and idempotency keys. Give it explicit pointers:

> "Implement Order Service per contracts/openapi/order-service.yaml. The checkout saga sequence is defined in docs/sporekart-microservices-flow-diagrams.md §4 — implement it exactly, including the compensation branches (release reservation on payment failure, trigger refund on inventory commit failure). Schema is in docs/sporekart-microservices-database-schema.md §7."

Because it's coding against the *contracts* for Identity/Catalog/Inventory/Payment rather than waiting for their real running instances, this can start the moment Phase 1 is approved — you don't strictly have to wait for Tier 1 to finish, though finishing Tier 1 first gives you real services to integration-test Order against sooner.

**Integration checkpoint after Tier 2:** run the actual checkout saga end-to-end across five real services in docker-compose. This is your first real proof the architecture works, not just that each service works alone.

### Phase 4–6 — Tiers 3, 4, 5
Same repeating pattern each time: spawn parallel agents scoped to their repos and contracts, review Artifacts, run each service's own tests, then an integration checkpoint for that tier's sagas (return/refund after Fulfillment+Notification exist, training enrollment after Training exists).

Use this tier's spare capacity well — Content Service has no dependencies at all, so it's a good agent to run in a spare Manager View lane even during Tier 1 if you want to keep more lanes busy rather than idle.

### Phase 7 — API Gateway
One agent, after all OpenAPI specs are stable (which they've been since Phase 1 — this step is really just "wire the routes now that real services exist to route to"). Point it at every `contracts/openapi/*.yaml` and have it generate the gateway's route table, JWT validation middleware, and rate-limit config.

### Phase 8 — Frontend
Two parallel agents (buyer app, admin dashboard), each pointed at the gateway's route table, not at individual services directly.

### Phase 9 — Full integration pass (the "does it actually integrate perfectly" gate)
This is the step that answers your core question directly. One agent, given the full `docker-compose.yml` (now with every service wired in) and this instruction:

> "Bring up the full stack. Run the checkout saga, the return/refund saga, and the training enrollment saga end to end, using real HTTP calls through the API gateway, not direct service calls. Use the browser subagent to actually click through the buyer app checkout flow and the admin dashboard's order approval flow. Report every failure with the specific step and service involved."

This is where Antigravity's **browser subagent** earns its keep — it doesn't just check that APIs respond correctly, it actually drives the frontend like a user would, which is the real test of "premium e-commerce flow" you asked for earlier: does checkout *feel* right end to end, not just does each endpoint return 200.

Fix failures service by service, re-run this same integration prompt after each fix, until it's clean.

### Phase 10 — Observability, CI/CD, load testing
Last, because it needs a working system to instrument. One or two agents: OpenTelemetry tracing wired across all services (correlation IDs already exist from `AGENTS.md` rule 6, so this is largely "propagate the header and export spans"), CI pipeline per service, and a load test against the checkout saga specifically.

---

## 5. The four habits that actually prevent the mess

1. **Never let a service agent touch `contracts/`.** If an agent hits something ambiguous in a contract, it should stop and surface the question as a comment on its Artifact, not silently resolve it its own way. Two agents "silently resolving" the same ambiguity differently is exactly how integration breaks.
2. **Run an integration checkpoint after every tier, not just at the end.** Catching a mismatch after 3 services costs an afternoon. Catching it after 14 costs a week of untangling which service actually has the bug.
3. **Commit incrementally and review every Artifact before merging to your integration branch.** Don't let an agent's work sit unreviewed for more than one service at a time — if Tier 1's three agents all finish and you haven't reviewed any of them yet, you've lost the ability to catch a Tier 1 problem before Tier 2 agents start building on top of it.
4. **One service = one agent = one repo boundary, always.** The moment you have one agent editing across two service folders "to save time," you've reintroduced the coupling this whole architecture exists to remove.

Follow the tier order, freeze contracts before any service code exists, and integration-test at the end of every tier rather than only at the end of the build — that's what makes 14 independently agent-built services actually snap together instead of needing a rescue integration sprint at the end.
