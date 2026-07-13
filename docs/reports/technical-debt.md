# Technical Debt Register — SporeKart Enterprise AI Platform

**Document:** Technical Debt Assessment (Phase 4 Gate, Step 13)
**Prepared by:** Enterprise Architecture Review Board (ARB)
**Date:** 2026-07-12
**Platform version under review:** v3.6.0 (Phase 4 Final Hardening)
**Gate status:** All items below are assessed as **non-blocking for the v3.6.0 gate**. None of these findings should prevent advancement to Phase 5; they are prioritized for remediation within the indicated phases.

---

## 1. Executive Summary

The SporeKart Enterprise AI Platform (Spring Boot 3.x, Java 21, modular monolith under `services/ai-service` and supporting domain services) presents a structurally strong, governance-enforced foundation. The debt captured here is representative of a platform that has been intentionally frozen at the contract level for Phase 5, with selected production-hardening and consistency items deferred.

Debt is rated **Critical / High / Medium / Low**. The aggregate debt posture for this gate is **Low-to-Medium**, with one High item (JWT enforcement) that is explicitly scoped as a Phase 5 production-readiness action rather than a blocker for the current freeze.

| Severity | Count | Phase-targeted |
|---|---:|---|
| Critical | 0 | — |
| High | 1 | Phase 5 |
| Medium | 5 | Phase 5 / Phase 6 |
| Low | 3 | Phase 6 / backlog |

---

## 2. Critical Debt

None identified at this gate. The platform's layered architecture, ArchUnit governance gates, and frozen contracts establish a safe baseline for Phase 5.

---

## 3. High Debt

### TD-01 — JWT Authentication Not Yet Enforced (HTTP Basic Only)

- **Description:** Across the services reviewed, HTTP Basic authentication is the configured mechanism. `SecurityConfig` in `services/ai-service` (`com.sporekart.ai.infrastructure.security.SecurityConfig`) and `services/identity-service` (`com.sporekart.identity.config.SecurityConfig`) both wire `.httpBasic(Customizer.withDefaults())` with no JWT bearer-token validation, token issuer, or OAuth2 resource-server configuration present.
- **Impact:** Basic-auth-only transport is unsuitable for production multi-tenant exposure. Credentials are retransmitted per request, there is no short-lived token revocation, no federated identity, and no fine-grained claims-based authorization beyond the in-process user store. This is the single largest security gap before a production cutover.
- **Recommendation:** Introduce a JWT resource-server filter (or OAuth2/OIDC resource server) in `services/identity-service` and propagate the verified `Authentication` across the gateway and downstream modules. Define token issuance, rotation, and revocation contracts and document them as part of the security standard.
- **Suggested phase:** Phase 5 (production security hardening track).
- **Priority:** High — required before any production/externally-exposed deployment, but not blocking the current contract freeze.

---

## 4. Medium Debt

### TD-02 — Error-Envelope Inconsistency (ProblemDetails vs ErrorDto vs ResponseEnvelope)

- **Description:** Three distinct error/response shapes coexist. `com.sporekart.ai.common.exception.ProblemDetails` is emitted by `GlobalExceptionHandler` for platform exceptions. Module controllers (e.g., `AdminController`, `ComplianceController`, `AnalyticsController`, `ApprovalController`) return bespoke `ErrorDto` records (`interfaces/rest/dto/ErrorDto.java`) from local `@ExceptionHandler` methods. Separately, several controllers (e.g., `ApiRegistryController`, `PromptController`) return `ResponseEnvelope<T>` for success and error paths. The `shared-errors` module exists but is not uniformly adopted.
- **Impact:** Clients must parse three different failure shapes, complicating the API gateway, frontend error handling, and contract tests. Inconsistent `Content-Type` (RFC 7807 `application/problem+json` vs `application/json`) undermines tooling and observability of failures.
- **Recommendation:** Standardize on a single error contract. Recommended path: adopt `ProblemDetails` (RFC 7807) as the canonical error envelope, wrap success payloads in `ResponseEnvelope<T>` only where a uniform wrapper is desired, and retire per-module `ErrorDto` in favor of the shared handler. Document the decision in `docs/standards/response-format.md` and `docs/standards/error-handling.md`.
- **Suggested phase:** Phase 5 (API consistency track).
- **Priority:** Medium.

### TD-03 — Missing Kafka Dead-Letter Queue (DLQ) and Schema Registry

- **Description:** Kafka is the event backbone, but no dead-letter topic strategy, retry/backoff policy, or schema registry (e.g., Confluent Schema Registry / Apicurio) is wired. The only DLQ reference is an enum value `DLQ` in `eventcatalog/domain/EventDeliveryStatus.java`; no consumer-side routing or producer-side serialization governance exists.
- **Impact:** Poison messages can stall consumer groups or be silently dropped; schema evolution on shared topics risks breaking consumers without a controlled compatibility contract. This weakens event-driven reliability and the Event Catalog's drift guarantees.
- **Recommendation:** Introduce a DLQ topic per domain event with a max-retry policy and an alert on DLQ depth; integrate a schema registry with backward-compatible evolution enforcement. Wire both into the `eventcatalog` module as the authoritative schema source.
- **Suggested phase:** Phase 5 (reliability track) / Phase 6 (schema governance maturity).
- **Priority:** Medium.

### TD-04 — CSRF Protection Disabled

- **Description:** `csrf(AbstractHttpConfigurer::disable)` (or `csrf -> csrf.disable()`) is present in the security configurations of `ai-service`, `identity-service`, `analytics-service`, `notification-service`, and `admin-service`. CSRF is disabled across the board, presumably for stateless API testing.
- **Impact:** With HTTP Basic and no anti-CSRF token, browser-based session-forgery risk is low today, but once JWT/session cookies or browser-facing surfaces are enabled, the absence of a CSRF posture becomes a real exposure and an audit finding.
- **Recommendation:** For purely stateless bearer-token APIs, document that CSRF is intentionally out of scope; for any cookie/session-based surface, re-enable CSRF or adopt double-submit token patterns. Record the explicit decision in the security standard.
- **Suggested phase:** Phase 5 (security hardening track, alongside TD-01).
- **Priority:** Medium.

### TD-05 — Missing Repository and End-to-End Test Coverage

- **Description:** While the platform has 372 test files (unit, controller, integration, ArchUnit governance), dedicated repository-layer (Spring Data JPA) tests and cross-service end-to-end (`testing/e2e`) flows are sparse relative to the domain surface (286 JPA entities, 31 modules). The `testing/e2e` and `testing/integration` directories exist but are under-populated versus the API/contract surface.
- **Impact:** Refactors to persistence mapping, Flyway migrations, and cross-module event flows can regress without detection. Repository-level drift between entity and schema is under-tested.
- **Recommendation:** Add Spring Data repository slices (`@DataJpaTest`) for high-risk aggregates and a curated set of contract-driven E2E scenarios covering the primary order/cart/payment/fulfillment flows. Gate them in CI.
- **Suggested phase:** Phase 5 (test coverage track).
- **Priority:** Medium.

### TD-06 — Sparse Foreign Keys and Mixed UUID Storage Conventions

- **Description:** The schema employs UUID primary keys, but several relationships rely on application-level references or nullable/optional foreign keys rather than enforced DB constraints. Mixed storage of UUID-as-string vs UUID-as-binary (and occasional legacy numeric identifiers in integration boundaries) appears across modules.
- **Impact:** Referential integrity is partially delegated to application logic, increasing the risk of orphaned records and complicating analytics joins. Mixed UUID representation inflates storage and complicates indexing/comparison.
- **Recommendation:** Enforce foreign-key constraints at the database layer where the relationship is mandatory; standardize UUID storage (recommended: `uuid` binary or `varchar(36)` consistently) and add Flyway checks/integration tests for orphan detection.
- **Suggested phase:** Phase 6 (data integrity hardening).
- **Priority:** Medium (borderline Low).

---

## 5. Low Debt

### TD-07 — Registry vs Business-Module Metadata Overlap

- **Description:** The 8 registry modules (`providerregistry`, `promptregistry`, `knowledgeregistry`, `usagetracking`, `configregistry`, `eventcatalog`, `apiregistry`, `capabilitydiscovery`) duplicate some metadata that also lives in their corresponding business modules (e.g., provider configuration vs provider registry; API metadata vs business module routing).
- **Impact:** Risk of drift between registry metadata and live business-module state; consumers may read stale registry entries. Minor duplication of storage and sync logic.
- **Recommendation:** Define a clear ownership boundary: registries are the authoritative discovery/catalog source; business modules emit events to keep registries synchronized. Document the sync contract in the `eventcatalog` and `apiregistry` standards.
- **Suggested phase:** Phase 6 (governance refinement).
- **Priority:** Low.

### TD-08 — Event Catalog Schema-Drift CI Absent

- **Description:** The `eventcatalog` module is declared the authoritative source of event schemas, but no CI job validates producer/consumer payloads against the catalog (no schema-drift gate). Drift is detectable only at runtime.
- **Impact:** Contract violations surface late; the catalog can fall out of sync with actual published events.
- **Recommendation:** Add a CI step that extracts event schemas from producers and asserts compatibility against the catalog (leverages the TD-03 schema registry). Fail the build on incompatible drift.
- **Suggested phase:** Phase 6 (CI governance maturity).
- **Priority:** Low.

### TD-09 — Legacy Placeholder and Duplicate Documentation

- **Description:** As noted in the platform certification, a number of top-level placeholder/legacy documents and dual controller conventions (`interfaces.rest` aggregates) coexist with the canonical standards. Some docs still carry placeholder scaffolding.
- **Impact:** Documentation sprawl increases onboarding cost and risks contradictory guidance; low runtime impact.
- **Recommendation:** Retire or consolidate legacy placeholder documents and reconcile controller routing policy under a single convention prior to Phase 5 feature expansion.
- **Suggested phase:** Phase 6 (documentation hygiene) / quick-win in Phase 5.
- **Priority:** Low.

---

## 6. Remediation Roadmap (Summary)

| ID | Item | Severity | Target Phase | Status |
|---|---|---|---|---|
| TD-01 | JWT not enforced (Basic only) | High | Phase 5 | Open |
| TD-02 | Error-envelope inconsistency | Medium | Phase 5 | Open |
| TD-03 | Kafka DLQ + schema registry | Medium | Phase 5/6 | Open |
| TD-04 | CSRF disabled | Medium | Phase 5 | Open |
| TD-05 | Repository / E2E test gaps | Medium | Phase 5 | Open |
| TD-06 | Sparse FKs / mixed UUID | Medium | Phase 6 | Open |
| TD-07 | Registry vs module overlap | Low | Phase 6 | Open |
| TD-08 | Event Catalog drift CI | Low | Phase 6 | Open |
| TD-09 | Legacy placeholder docs | Low | Phase 5/6 | Open |

**Gate conclusion:** No Critical or blocking debt. The platform is certified for Phase 5 advancement subject to the High/Medium items being scheduled.

---

*End of Technical Debt Register.*
