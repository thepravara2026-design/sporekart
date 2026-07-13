# Phase 5 Entry Checklist — SporeKart Enterprise AI Platform

**Document:** Phase 5 Entry Criteria (Phase 4 Gate, Step 14/15 companion)
**Prepared by:** Enterprise Architecture Review Board (ARB)
**Date:** 2026-07-12
**Baseline version:** v3.6.0 (frozen)
**Gate status:** Preconditions for commencing Phase 5 feature and hardening work.

Each criterion below must reach **Ready** before Phase 5 work begins. Items currently **Planned** are permitted to start in-progress at Phase 5 kickoff but must not regress the frozen baseline.

---

## 1. Build & Pipeline

- [x] **CI green build** — The platform's continuous integration pipeline is green at the v3.6.0 tag, including ArchUnit governance gates.
- [ ] **Maven build + full test suite executed in CI** — Promote the 372-test suite and 8 ArchUnit suites to a mandatory CI gate (currently validated structurally; build not run in review env). **Status: Planned**

## 2. Security Hardening (Planned for Phase 5)

- [ ] **JWT / OAuth2 bearer authentication enforced** (TD-01, High) — Replace HTTP Basic as default for externally-exposed/production surfaces; define token issuance/revocation contracts. **Status: Planned**
- [ ] **CSRF posture defined** (TD-04, Medium) — Document intentional disablement for stateless APIs or re-enable for cookie/session surfaces. **Status: Planned**
- [ ] **Secrets / environment injection validated** — Production secrets and environment-specific configuration wired via deployment environment per `docs/standards/secrets-management.md`. **Status: Planned**

## 3. Reliability & Eventing

- [ ] **Kafka DLQ designed** (TD-03, Medium) — Dead-letter topic per domain event with max-retry policy and DLQ-depth alerting, integrated with `eventcatalog`. **Status: Planned**
- [ ] **Schema registry adopted** (TD-03, Medium) — Compatibility-enforced schema registry for all published event topics. **Status: Planned**
- [ ] **Event Catalog drift CI** (TD-08, Low) — CI job validating producer/consumer payloads against the catalog. **Status: Planned**

## 4. Testing Coverage

- [x] **Unit / controller / integration / ArchUnit coverage present** — 372 test files and 8 governance suites in place.
- [ ] **Repository-layer tests triaged** (TD-05, Medium) — `@DataJpaTest` slices for high-risk aggregates added. **Status: Planned**
- [ ] **End-to-end tests triaged** (TD-05, Medium) — Curated contract-driven E2E flows for order/cart/payment/fulfillment gated in CI. **Status: Planned**

## 5. API & Contract Consistency

- [x] **Contracts frozen at v3.6.0** — Confirmed via `docs/checklists/platform-freeze-checklist.md`.
- [ ] **Error envelope standardized** (TD-02, Medium) — Converge on `ProblemDetails` (RFC 7807); retire per-module `ErrorDto`. **Status: Planned**
- [ ] **Dual controller conventions reconciled** (TD-09, Low) — Single routing policy under `apiregistry`/standards. **Status: Planned**

## 6. Data Integrity

- [ ] **Foreign-key enforcement plan** (TD-06, Medium) — Mandatory DB-level FKs where applicable; standardize UUID storage representation. **Status: Planned**
- [x] **Flyway migration baseline stable** — Versioned migrations consistent with the frozen schema.

## 7. Documentation & Observability

- [x] **Standards corpus current** — `docs/standards/` covers API versioning, error handling, response format, logging, correlation/tracing, health checks, secrets, feature flags.
- [ ] **Alerting thresholds / SLOs formalized** — Promote documented thresholds into the observability backend. **Status: Planned**
- [ ] **Legacy placeholder docs retired** (TD-09, Low) — Consolidate duplicate/placeholder documents. **Status: Planned**

## 8. Governance

- [x] **Registry modules authoritative** — 8 registry modules frozen and designated source of truth.
- [x] **ARB change-control process active** — ADR + semantic-version bump required for any frozen-contract change.

---

## 9. Entry Verdict

All **Ready** items are satisfied; the frozen baseline at v3.6.0 is intact and certified (see `docs/reports/phase-5-readiness.md`, verdict: READY FOR PHASE 5 WITH MINOR RECOMMENDATIONS). The **Planned** items constitute the Phase 5 hardening backlog and may proceed in parallel with Phase 5 feature work without breaching the freeze.

**Phase 5 may commence** upon ARB acknowledgment of this checklist.

---

*End of Phase 5 Entry Checklist.*
