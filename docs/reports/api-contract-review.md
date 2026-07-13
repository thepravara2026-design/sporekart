# SporeKart Enterprise AI Platform — API Contract Review

**Certification Gate:** Step 2 — API Contract Review
**Reviewing Body:** Enterprise Architecture Review Board (EARB)
**Service Under Review:** `ai-service` (Spring Boot, `services/ai-service`)
**Scope:** REST surface under `com.sporekart.ai.**.interfaces.rest`
**Date:** 2026-07-12
**Status:** CERTIFIED WITH NON-BLOCKING FINDINGS

---

## 1. Executive Summary

The `ai-service` exposes a substantial, well-organized REST surface: **37 controllers and 380 HTTP endpoints** in total. Of these, **285 endpoints across 25 module base paths are correctly namespaced under `/api/v1/<module>`**, aligning with the enterprise versioning policy. The remaining 95 endpoints live on legacy/non-platform root paths (`/ai`, `/erp`, `/finance`, `/gst`, `/mobile`, `/operations`, `/suppliers`, `/vendors`, `/warehouses`, `/`) and represent earlier commerce/ERP/mobile domains that predate the `/api/v1` convention.

The platform demonstrates strong discipline in URI structure, HTTP semantics, DTO isolation (245 dedicated DTO classes), and OpenAPI coverage (springdoc 2.6.0, 1176 annotation usages). A global RFC 9457-style exception handler exists, but its adoption is **inconsistent** across controllers (see Findings F-03). No blocking defects were identified for this gate.

**Freeze Statement (Binding):** The `/api/v1` API contracts are hereby declared **STABLE AND FROZEN**. No breaking changes (URI renames, method signature changes, request/response schema mutations, status-code changes, or removal of fields) may be introduced within the `v1` namespace without a formal version bump to `/api/v2` (or higher) and EARB approval. Additive, backward-compatible changes (new optional fields, new endpoints, new query parameters) are permitted under the freeze.

---

## 2. Module Base Paths and Endpoint Counts

### 2.1 Platform modules under `/api/v1/` (certified namespace)

| # | Module Base Path | Controller | Endpoints | Dominant Verbs |
|---|------------------|------------|-----------|----------------|
| 1 | `/api/v1/admin` | AdminController | 11 | GET, PUT, POST |
| 2 | `/api/v1/ai` | GatewayController | 5 | GET, POST |
| 3 | `/api/v1/ai/prompts` | PromptController | 18 | GET, POST, PUT, DELETE |
| 4 | `/api/v1/ai/providers` | ProviderController | 6 | GET, POST |
| 5 | `/api/v1/api-registry` | ApiRegistryController | 13 | GET, POST, PUT |
| 6 | `/api/v1/approvals` | ApprovalController | 12 | GET, POST |
| 7 | `/api/v1/assistants` | AssistantController | 9 | GET, POST |
| 8 | `/api/v1/automation` (and `/api/v1/governance/lifecycle`) | AutomationController | 10 | GET, POST |
| 9 | `/api/v1/capability-discovery` | CapabilityDiscoveryController | 13 | GET, POST, PUT |
| 10 | `/api/v1/compliance` | ComplianceController | 9 | GET, POST |
| 11 | `/api/v1/config-registry` | ConfigRegistryController | 11 | GET, POST, PUT, DELETE |
| 12 | `/api/v1/content` | ContentController | 10 | GET, POST |
| 13 | `/api/v1/conversation` | ConversationController | 18 | GET, POST, PUT, DELETE |
| 14 | `/api/v1/decisions` | DecisionController | 8 | GET, POST |
| 15 | `/api/v1/event-catalog` | EventCatalogController | 13 | GET, POST, PUT |
| 16 | `/api/v1/governance` | AnalyticsController + GovernanceController | 16 | GET, POST |
| 17 | `/api/v1/knowledge` | KnowledgeController | 12 | GET, POST, PUT, DELETE |
| 18 | `/api/v1/knowledge-registry` | KnowledgeRegistryController | 12 | GET, POST, PUT |
| 19 | `/api/v1/policies` | PolicyController | 10 | GET, POST, PUT, DELETE |
| 20 | `/api/v1/prompt-registry` | PromptRegistryController | 9 | GET, POST, PUT |
| 21 | `/api/v1/provider-registry` | ProviderRegistryController | 11 | GET, POST, PUT |
| 22 | `/api/v1/risk` | RiskController | 10 | GET, POST |
| 23 | `/api/v1/semantic` | SemanticController | 7 | GET, POST |
| 24 | `/api/v1/usage-tracking` | UsageTrackingController | 11 | GET, POST |
| 25 | `/api/v1/workflows` | WorkflowController | 21 | GET, POST, PUT, DELETE |

**Subtotal:** 25 base paths, **285 endpoints** under `/api/v1`.

### 2.2 Legacy / non-`/api/v1` controllers (pre-existing domains)

| Controller | Base Path | Endpoints |
|------------|-----------|-----------|
| AiController | `/ai` | 19 |
| B2bController | `/` | 16 |
| ProcurementController | `/` | 7 |
| ERPIntegrationController | `/erp` | 8 |
| FinanceController | `/finance` | 8 |
| GSTController | `/gst` | 4 |
| MobileController | `/mobile` | 8 |
| OperationsController | `/operations` | 1 |
| SupplierController | `/suppliers` | 6 |
| MarketplaceController | `/vendors` | 13 |
| WarehouseController | `/warehouses` | 5 |

**Subtotal:** 95 endpoints outside the versioned namespace. These are NOT part of the frozen `v1` contract but must be tracked for future consolidation (see F-01).

---

## 3. URI Consistency

- **Positive:** All 25 platform modules consistently prefix their routes with `/api/v1/<module>`. Resource-oriented nouns are used as base paths (`/api/v1/workflows`, `/api/v1/approvals`, `/api/v1/knowledge-registry`).
- **Inconsistency (F-01):** 12 controllers (95 endpoints) reside outside `/api/v1`, mixed into the catch-all `com.sporekart.ai.interfaces.rest` package with root or domain paths (`/`, `/ai`, `/erp`, `/finance`, `/gst`, `/mobile`, `/operations`, `/suppliers`, `/vendors`, `/warehouses`). These predate the convention and should be migrated or explicitly documented as a separate (legacy) API tier.
- **Overlap (F-02):** The `/api/v1/governance` base path is shared by **two** controllers — `AnalyticsController` (dashboard/metrics/kpis/reports) and `GovernanceController` (governance lifecycle). While Spring resolves distinct sub-paths, co-locating analytics under the `governance` noun is a semantic mismatch and creates routing fragility. `AutomationController` further injects `/api/v1/governance/lifecycle` and `/api/v1/governance/workflows` paths inline (no `@RequestMapping` base), compounding the overlap.
- **Kebab-case convention:** Module names use kebab-case (`knowledge-registry`, `capability-discovery`, `usage-tracking`, `config-registry`), which is consistent and URL-safe.

---

## 4. Naming Conventions

- Module/resource names use **kebab-case** in URIs — good for URL safety.
- Sub-resource and action paths use kebab-case (`/{workflowId}/steps`, `/{id}/approve`, `/register`, `/sync-status/{status}`).
- **Action-oriented (RPC-style) sub-paths** are used for state transitions: e.g. `POST /api/v1/workflows/{workflowId}/publish`, `POST /api/v1/approvals/{id}/approve`, `POST /api/v1/approvals/{id}/reject`, `POST /api/v1/usage-tracking/record`, `POST /api/v1/knowledge-registry/{id}/sync`. These are acceptable for non-CRUD operations but should be standardized (verb-on-noun vs noun-only) across modules.
- DTOs follow a consistent `XRequest` / `XResponse` naming pattern with records (e.g., `WorkflowDefinitionRequest`, `WorkflowDefinitionResponse`).

---

## 5. HTTP Semantics

| Verb | Usage Observed | Assessment |
|------|----------------|------------|
| GET | Reads, list, by-id, sub-resources, `/health` | Correct. List endpoints often accept query params for filtering. |
| POST | Create, action invocations, execute, record, export, search | Correct for creation; action endpoints (`/approve`, `/publish`, `/execute`) are RPC-style but acceptable. |
| PUT | Full updates by id, configuration, feature-flags, schedules pause/resume | Mostly correct. Note `PUT /api/v1/automation/schedules/{scheduleId}/pause` uses PUT for an action — should be POST. |
| DELETE | Remove by id, steps, configuration rollback | Correct. |
| PATCH | None observed | No partial-update support; clients must send full representations on PUT. Acceptable but noted. |

- **`/health` endpoints:** Most controllers expose `GET /health` (e.g., WorkflowController, AnalyticsController, ApprovalController). These should be consolidated under the actuator `/actuator/health` (already exposed in `application.yml`: `health,info,metrics,prometheus`) rather than per-controller custom health routes to avoid duplication.

---

## 6. DTO Design

- **Isolation:** 245 dedicated DTO classes live under per-module `interfaces/rest/dto` packages. Controllers do not leak domain entities — strong separation of concerns (hexagonal style).
- **Immutability:** Request/Response DTOs are implemented as Java `record` types (e.g., `WorkflowDefinitionRequest`), promoting immutability.
- **Validation:** `@Valid` is applied to 49 request bodies (`jakarta.validation.Valid`). `@Validated` (class-level / group validation) is **not used anywhere** (0 occurrences) — see F-04.
- **Field-level constraints:** Present on many DTOs (e.g., `@NotNull`, `@Size`, `@Pattern` implied by `@Valid` usage). Recommended to expand Bean Validation annotations across all request DTOs uniformly.

---

## 7. Request/Response Validation

- `spring-boot-starter-validation` is a declared dependency and `@Valid` is used on inbound `@RequestBody` parameters.
- **Gap (F-04):** `@Validated` is never applied at the controller class level, so path/query parameter validation (e.g., `@PathVariable @Min`, `@RequestParam` constraints) is not enforced consistently. Method-level `@Valid` only covers request bodies.
- Recommendation: adopt `@Validated` on controllers and validate `@PathVariable`/`@RequestParam` inputs to guarantee fail-fast 400 responses.

---

## 8. RFC 9457 Error Responses

A global `@RestControllerAdvice` (`com.sporekart.ai.common.exception.GlobalExceptionHandler`) exists and produces an RFC 9457-shaped `ProblemDetails` record (`common/exception/ProblemDetails.java`) with fields: `type`, `title`, `status`, `detail`, `instance`, `timestamp`, and `extensions`. This is the **correct, standards-compliant** error envelope.

**Critical inconsistency (F-03):** Numerous controllers declare **local** `@ExceptionHandler` methods that override the global advice and return **different, non-uniform** envelopes:
- `ErrorDto` — Admin, Analytics, Approval, Automation, Compliance, Decision, Risk
- `GovernanceErrorDto` — Governance
- `PolicyErrorDto` — Policy
- `ResponseEnvelope<Void>` — Knowledge, Prompt, PromptRegistry, ApiRegistry, Semantic

Because local handlers take precedence, clients receive **three or four different error shapes** depending on the module. This breaks the contract promise of a single, predictable RFC 9457 error format.

**Recommendation:** Deprecate all per-controller `ErrorDto`/`ResponseEnvelope` error handlers and route everything through the global `ProblemDetails` advice so every `/api/v1` endpoint emits a uniform RFC 9457 body with `Content-Type: application/problem+json`.

---

## 9. Pagination, Filtering, Sorting

- Pagination primitives (`Pageable`, `Page<T>`, `Sort`) are referenced across the codebase, and list endpoints (`GET /list`, `GET`, `GET /{id}`) exist in most modules.
- **Inconsistency (F-05):** There is no single standardized pagination contract. Some modules expose generic `GET /list` (UsageTracking, KnowledgeRegistry) while others use plain `GET` (Workflow, Approval). Filtering is largely implemented as bespoke sub-paths (`/type/{type}`, `/owner/{owner}`, `/sync-status/{status}`, `/provider/{providerId}`) rather than standardized query parameters (`?filter=`, `?sort=`, `?page=`, `?size=`).
- **Recommendation:** Adopt a uniform query-parameter pagination/filter/sort contract (e.g., `page`, `size`, `sort=field,asc`, `filter=key:value`) and a standard `PageResponse<T>` envelope (with `content`, `page`, `size`, `totalElements`, `totalPages`) across all list endpoints.

---

## 10. Versioning Strategy

- The platform uses **URI path versioning** (`/api/v1/...`), which is explicit, cache-friendly, and mobile-client friendly.
- No `v2` exists yet; `v1` is the active contract.
- The freeze (Section 1) mandates that all breaking changes require `/api/v2`.
- **Recommendation:** Publish the OpenAPI document for `v1` as a versioned, immutable artifact (e.g., `openapi-v1.json`) and gate CI on contract diffs to detect accidental breaking changes.

---

## 11. OpenAPI / springdoc Completeness

- `springdoc-openapi-starter-webmvc-ui` 2.6.0 is declared; **1176** OpenAPI annotation usages (`@Operation`, `@Tag`, `@Schema`, `@ApiResponse`) are present — strong coverage.
- Controllers are annotated with `@Tag` (e.g., WorkflowController: `@Tag(name = "Workflow API", ...)`) and operations with `@Operation(summary = ...)`.
- **Gap (F-06):** `@ApiResponse` (error response documentation) is not consistently applied; the RFC 9457 `ProblemDetails` responses are not uniformly documented per operation. DTO `@Schema` descriptions could be expanded for generated client SDKs.
- **Recommendation:** Generate and publish the aggregated `v1` OpenAPI spec; add `@ApiResponse` for 400/404/500 RFC 9457 cases on every operation to support mobile (Android/iOS) codegen.

---

## 12. Future Mobile (Android / iOS) API Compatibility

- **JSON over HTTP, stateless, resource-oriented** design is inherently compatible with native mobile clients (Retrofit / OkHttp on Android; URLSession / Alamofire on iOS).
- UUID identifiers (VARCHAR(36) / `GenerationType.UUID`) are string-safe for both platforms — no binary key leakage.
- **Concerns for mobile:**
  - Inconsistent error envelopes (F-03) force mobile clients to implement multiple parsers; unifying on RFC 9457 removes this burden.
  - Non-standardized pagination (F-05) makes generic list/adapters harder to build; a uniform `PageResponse<T>` simplifies paging UI.
  - Offline-first mobile scenarios would benefit from the existing `/mobile` domain endpoints and conflict-resolution semantics — these should be explicitly brought under the `/api/v1` contract or a dedicated `/api/v1/mobile` module.
  - `OffsetDateTime`/timestamps are already used in error envelopes; ensure all response DTOs serialize temporal fields in ISO-8601 UTC for cross-platform safety.

**Conclusion:** The API is fundamentally mobile-ready; closing F-03 and F-05 is the highest-leverage work for clean native SDK generation.

---

## 13. Findings (Non-Blocking)

| ID | Severity | Finding | Recommended Action |
|----|----------|---------|--------------------|
| F-01 | Medium | 95 endpoints outside `/api/v1` (legacy root/domain paths). | Migrate to `/api/v1` or formally designate a legacy tier; exclude from freeze scope with documentation. |
| F-02 | Medium | `/api/v1/governance` shared by Analytics + Governance controllers; AutomationController injects `/api/v1/governance/*` inline. | Rehome analytics under `/api/v1/analytics`; give AutomationController a single `@RequestMapping("/api/v1/automation")`. |
| F-03 | High | Multiple error envelopes (`ErrorDto`, `ResponseEnvelope`, `GovernanceErrorDto`, `PolicyErrorDto`) override the global RFC 9457 `ProblemDetails`. | Consolidate all error handling into the global `ProblemDetails` advice. |
| F-04 | Medium | `@Validated` never used; path/query param validation not enforced. | Apply `@Validated` at controller level; validate path/query params. |
| F-05 | Medium | No uniform pagination/filter/sort contract; bespoke sub-paths for filtering. | Standardize `page`/`size`/`sort`/`filter` query params and `PageResponse<T>` envelope. |
| F-06 | Low | `@ApiResponse` (error docs) and `@Schema` descriptions under-applied. | Document 400/404/500 RFC 9457 on every operation; enrich DTO schemas. |
| F-07 | Low | Per-controller `GET /health` duplicates actuator health. | Remove custom health routes; rely on `/actuator/health`. |
| F-08 | Low | RPC-style action paths (`/approve`, `/publish`, `/pause` via PUT) inconsistent verb usage. | Standardize action semantics (prefer POST for state transitions). |

---

## 14. Certification Decision

**Result: PASS (with non-blocking findings).** The `/api/v1` API contracts meet the structural, semantic, and versioning requirements of the enterprise gate. Per the freeze statement in Section 1, the `v1` contracts are locked; F-03 and F-05 are recommended as fast-follow improvements prior to native mobile SDK publication but do not block certification.
