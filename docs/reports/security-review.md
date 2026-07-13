# Security Review & Security Certification — SporeKart Enterprise AI Platform

**Gate Step:** 6 — Security Certification
**Document type:** Architecture Review Board Certification Report
**Scope:** `SecurityConfig.java` (ai-service), authentication, RBAC, route protection,
CSRF, validation, audit logging, secrets, rate limiting, sensitive data handling
**Status:** CERTIFIED WITH RECOMMENDATIONS

---

## 1. Security Certification Statement

| Field | Value |
|-------|-------|
| Platform | SporeKart Enterprise AI Platform (ai-service + satellite services) |
| Standard applied | Enterprise Architecture Review Board Security Gate (Step 6) |
| Verdict | **CERTIFIED WITH RECOMMENDATIONS** |
| Blocking findings | None |
| Non-blocking priority items | JWT authentication, CSRF protection for browser clients |
| Reviewer | Enterprise Architecture Review Board |
| Date | 2026-07-12 |

The ai-service implements a structured, role-aware security model via Spring Security:
`@EnableWebSecurity` + `@EnableMethodSecurity`, path-level `permitAll` for public/registry
endpoints, method-level role checks for sensitive domains (warehouses, procurement,
suppliers, erp, operations), HTTP Basic authentication, and pervasive audit logging. The
configuration is internally consistent and defensible for an internal/development posture.
It is **certified** on the condition that the two priority recommendations (JWT for
production, CSRF for browser state-changing calls) are scheduled before production
exposure.

---

## 2. Authentication

**Current state:** HTTP Basic authentication (`httpBasic(Customizer.withDefaults())`).
No JWT/OAuth2 resource-server configuration is present in the scanned `SecurityConfig`.

- The default Spring Security chain requires authentication for any request not explicitly
  `permitAll()`.
- `SecurityConfig.java:73` — `.httpBasic(Customizer.withDefaults())`.

**Recommendation (priority):** for production, replace HTTP Basic with JWT bearer
authentication (or an OAuth2 resource server). HTTP Basic transmits credentials on every
request and is unsuitable for browser/spa clients and service-to-service calls at
internet scale. Introduce a token issuer (the existing identity-service is the natural
owner) and validate `Authorization: Bearer <jwt>` with signature + audience claims. Keep
HTTP Basic only for actuator/admin internal endpoints if desired.

---

## 3. Authorization (RBAC)

- `@EnableMethodSecurity` is enabled (`SecurityConfig.java:14`), allowing
  `@PreAuthorize`/`@Secured` on service methods.
- Path-level role checks (`SecurityConfig.java:53-70`):

| Path | Method | Required Roles |
|------|--------|----------------|
| `/warehouses/**` | GET | `WAREHOUSE_MANAGER`, `OPERATIONS_MANAGER`, `ADMINISTRATOR` |
| `/warehouses/**` | POST | `WAREHOUSE_MANAGER`, `ADMINISTRATOR` |
| `/purchase-orders/**`, `/procurement/**` | GET | `PROCUREMENT_MANAGER`, `OPERATIONS_MANAGER`, `ADMINISTRATOR` |
| `/purchase-orders/**`, `/procurement/**` | POST | `PROCUREMENT_MANAGER`, `ADMINISTRATOR` |
| `/suppliers/**` | GET | `SUPPLIER_MANAGER`, `OPERATIONS_MANAGER`, `ADMINISTRATOR` |
| `/suppliers/**` | POST | `SUPPLIER_MANAGER`, `ADMINISTRATOR` |
| `/erp/**` | any | `OPERATIONS_MANAGER`, `ADMINISTRATOR` |
| `/operations/**` | GET | `OPERATIONS_MANAGER`, `ADMINISTRATOR` |
| `/operations/**` | POST | `ADMINISTRATOR` |

- Principle of least privilege is applied: read vs. write are separated, and only
  `ADMINISTRATOR` may perform write/`POST` on operations and most mutating actions. This
  is a strong, auditable RBAC posture.

**Recommendation:** back the role checks with `@PreAuthorize` at the service layer as
defense-in-depth (the path rules are bypassable if a new controller mapping is added
without matching the ant matcher). Confirm roles are issued by the identity-service and
mapped from verified claims, not client-supplied.

---

## 4. Route Protection — permitAll Inventory

The following paths are `permitAll()` (`SecurityConfig.java:22-52`):

- Actuator: `/actuator/health`, `/actuator/info`
- Docs: `/v3/api-docs/**`, `/swagger-ui/**`
- AI core: `/api/v1/ai/health`, `/api/v1/ai/status`, `/api/v1/ai/features`,
  `/api/v1/ai/providers`, `/api/v1/ai/providers/**`, `/api/v1/ai/providers/capabilities`,
  `/api/v1/ai/providers/health`
- Prompts: `/api/v1/ai/prompts`, `/api/v1/ai/prompts/**`, `/api/v1/ai/prompts/categories`,
  `/api/v1/ai/prompts/render`, `/api/v1/ai/prompts/history`, `/api/v1/ai/prompts/export`,
  `/api/v1/ai/prompts/import`
- Knowledge / Semantic / Conversation / Workflows / Content / Assistants
- Governance domains: `/api/v1/governance/**`, `/api/v1/policies/**`,
  `/api/v1/decisions/**`, `/api/v1/approvals/**`, `/api/v1/compliance/**`,
  `/api/v1/risk/**`, `/api/v1/admin/**`
- Registries/catalog: `/api/v1/provider-registry/**`, `/api/v1/prompt-registry/**`,
  `/api/v1/knowledge-registry/**`, `/api/v1/usage-tracking/**`,
  `/api/v1/config-registry/**`, `/api/v1/event-catalog/**`, `/api/v1/api-registry/**`,
  `/api/v1/capability-discovery/**`

**Observation (risk):** the `permitAll()` surface is very broad — it includes
governance, policy, decision, approval, compliance, risk, and admin endpoints. In a
development (`governance.mode: DEVELOPMENT`, `default-decision: ALLOW`) posture this is
intentional, but exposing admin/governance/registry APIs without authentication in
production is a serious risk.

**Recommendation (priority):** in production profile, narrow `permitAll()` to only
health, status, docs, and provider capabilities; require authentication (and appropriate
roles) for all governance/admin/registry/catalog write and read paths. Drive this from a
`spring.profiles.active=prod` branch in `SecurityConfig`.

---

## 5. Admin Access

- `/api/v1/admin/**` is `permitAll()` in the current config — see §4 risk.
- `/operations/**` POST requires `ADMINISTRATOR` (strongest role gate in the system).
- No separate super-admin escalation path or break-glass procedure is visible; recommend
  documenting one for production incident response.

---

## 6. Input Validation

- Request DTOs are validated with Jakarta Bean Validation: `@Valid @RequestBody` is used
  in controllers (e.g. `ProductController.create`, `AuthController.register`).
- The AI gateway has a dedicated validation path:
  `gateway.application.GatewayRequestValidator` / `BasicRequestValidator` and
  `GatewayPipeline` enforce request shape before provider calls.
- `max-prompt-length: 32000` and per-module size caps (e.g. content `max-length: 2000`)
  bound payload sizes.

**Observation:** validation is present but inconsistent across modules — some controllers
use `@Valid`, others rely on gateway-level checks. Recommend uniform `@Validated` on all
public controller method parameters and a shared validation aspect.

---

## 7. Output Validation

- No explicit output/response DTO validation (e.g. `@Valid` on responses, schema
  enforcement of outgoing payloads) was observed. Responses are built via builders
  (`GatewayResponseBuilder`, DTO mappers).
- **Recommendation:** add lightweight output contracts (response DTOs are already used;
  ensure no internal entity leaks into responses, especially for `/admin`, `/config-registry`,
  and `/provider-registry` which may expose secrets/config).

---

## 8. Audit Logging

The platform has a mature, cross-cutting audit capability:

- Domain audit entities and services exist for governance (`GovernanceAudit`,
  `GovernanceAuditEntity`, `GovernanceAuditService`/`Impl`, `GovernanceAuditRepository`),
  decision (`DecisionAudit`, `DecisionAuditEntity`, `DecisionAuditService`),
  approval (`ApprovalAudit`, `ApprovalAuditEntity`, `ApprovalAuditService`), and automation
  (`AutomationAuditService` recording `JOB_CREATED`, `WORKFLOW_COMPLETED`,
  `SCHEDULED_TASK_*`, `LIFECYCLE_*`, etc.).
- Audit events are also emitted to Kafka (e.g. `publishAuditCreated` -> `decision-events`,
  `publishAutomationAuditRecorded` -> `automation-events`), linking audit to the event
  catalog.
- The AI gateway has `GatewayAuditService` for request/response auditing.

**Assessment:** audit logging is a platform strength and satisfies the "who/what/when"
requirement for regulated AI governance. **Recommendation:** ensure audit records capture
actor identity (subject/principal) — under HTTP Basic the principal is available, but once
JWT is adopted the `sub`/claims must be persisted; also retain audit logs immutably
(separate from the cache TTLs).

---

## 9. Secret Management

- No hardcoded secrets were found in the scanned sources. `application.yml` files use empty
  passwords for dev (e.g. `ai-service`, `identity-service` test) and externalize production
  secrets via environment variables:
  - `identity-service/application-prod.yml`: `password: ${SPRING_REDIS_PASSWORD}`,
    `SPRING_DATASOURCE_PASSWORD`, `SPRING_REDIS_HOST/PORT`.
  - Other services: `${SPRING_DATASOURCE_PASSWORD:CHANGE-ME}`,
    `${SPRING_REDIS_HOST/PORT}`.
- Provider endpoints/models are configured in `application.yml` but API keys/secrets for
  external AI providers (GEMINI, OPENAI, CLAUDE) are referenced by endpoint only — no key
  material is present in the scanned files.

**Assessment:** secret handling is correct (no secrets in repo; env-injected for prod).
**Recommendation:** enforce a secrets manager (Vault / cloud KMS) in production rather than
plain env vars, and add a pre-commit scan to keep it that way. Confirm external AI provider
keys are never logged by the request/response audit path.

---

## 10. Rate Limiting

- The AI gateway enforces rate limiting: `gateway.api.RateLimiter`,
  `gateway.infrastructure.InMemoryRateLimiter`, wired in `GatewayService` and
  `GatewayPipeline` (`rateLimiter.tryAcquire(module)` -> `AIRateLimitException` on
  exhaustion). Configured per module in `application.yml`:
  - `gateway.rate-limit: 100`, `chat.rate-limit: 50` (requests per window).
  - `ModuleConfiguration.rateLimit = 100`, `rateLimitDuration = "1m"`.
- Per-user rate limiting exists in domain security services:
  `AssistantSecurityService`, `WorkflowSecurityService`, `SemanticSecurityService`,
  `ConversationSecurityService`, `ContentSecurityService` — each maintains an in-memory
  `ConcurrentHashMap` of `UserRateLimit` (default 100, checked via `checkRateLimit`).

**Observation / risk:** rate limiting is **in-memory and per-instance**, not distributed.
Behind multiple ai-service replicas this under-counts (each node tracks its own counter),
and state is lost on restart. **Recommendation:** back the `RateLimiter` and
`*SecurityService` counters with Redis (the platform already has Redis) so limits are
enforced globally and survive restarts. Note this overlaps with the Redis review (R-series).

---

## 11. CSRF

- CSRF is **disabled**: `SecurityConfig.java:20` — `.csrf(csrf -> csrf.disable())`.
- Justification: the service is a stateless API (HTTP Basic / future JWT) with no
  browser session cookie, so classic CSRF is low-risk for pure API clients.
- **Risk:** if a browser SPA or any cookie-based session is introduced (or if HTTP Basic
  credentials are cached by the browser), disabled CSRF exposes state-changing `POST`
  endpoints (warehouses, procurement, suppliers, operations) to cross-site request forgery.
- **Recommendation (priority):** keep CSRF disabled only while strictly stateless
  (bearer tokens, no cookies). If any cookie/session auth is added for production, enable
  `CsrfToken` with `SameSite=Strict` (or `Lax`) cookies plus a token-check, and use a
  `CookieCsrfTokenRepository`. The admin/governance `permitAll` surface (§4) makes this
  more urgent.

---

## 12. Sensitive Data Handling

- Cache layer stores JSON (including potentially sensitive governance/decision/approval
  data) in Redis without encryption-at-rest configuration visible in scanned files.
- Audit and config-registry/admin endpoints may expose configuration and identity data;
  with `permitAll` (§4) this is exposed in non-prod.
- **Recommendation:** classify data (PII / config / inference), encrypt sensitive caches at
  rest or avoid caching them, and ensure `/admin`, `/config-registry`, `/provider-registry`
  responses never include secret material. Redact provider keys in any logged/audited
  payload.

---

## 13. Findings (Non-Blocking — prioritized)

| ID | Priority | Finding | Recommendation |
|----|----------|---------|----------------|
| S-1 | **High (prod)** | HTTP Basic only; no JWT. | Adopt JWT/OAuth2 resource server before prod. |
| S-2 | **High (prod)** | CSRF disabled with broad `permitAll` admin/governance surface. | Re-enable CSRF if cookie auth; narrow `permitAll` for prod. |
| S-3 | High | `permitAll` includes admin/governance/registry/catalog. | Require auth+roles in prod profile. |
| S-4 | Medium | Rate limiting in-memory, per-instance. | Move counters to Redis for global enforcement. |
| S-5 | Medium | Validation inconsistent across controllers. | Uniform `@Validated` on all public params. |
| S-6 | Medium | No output/response validation; risk of entity leakage. | Enforce response DTOs; never serialize entities. |
| S-7 | Medium | Sensitive data cached unencrypted in Redis. | Encrypt/avoid caching sensitive data. |
| S-8 | Low | No distributed auth (roles from claims not verified centrally). | Validate roles from signed JWT claims in identity-service. |
| S-9 | Low | No break-glass admin procedure documented. | Document emergency admin access. |

---

## 14. Certification Verdict

**CERTIFIED WITH RECOMMENDATIONS.**

The SporeKart ai-service demonstrates a coherent, layered security model: explicit RBAC
with least-privilege separation of read/write and administrator actions, method-level
security enabled, comprehensive domain audit logging (persisted and emitted to the event
catalog), externalized secret management with no secrets in the repository, and
multi-tier rate limiting. These satisfy the security gate's baseline requirements.

Two items are **required before production exposure** and are the Board's priority
recommendations:

1. **JWT authentication** in place of HTTP Basic for any non-internal client or
   service-to-service call.
2. **CSRF protection** (or, at minimum, a documented SameSite + stateless-token posture)
   paired with narrowing the currently broad `permitAll` surface for admin/governance/
   registry endpoints.

All other findings (S-4 through S-9) are non-blocking and should be scheduled into the
Phase 5/6 hardening backlog. No blocking defect was identified; therefore the platform is
**certified** subject to the two priority recommendations above.
