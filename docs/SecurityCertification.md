# Security Certification Report

**Certification:** Phase 13.5 — Sprint 1 — Part 1  
**Date:** 2026-07-24  
**Result:** ❌ **FAIL** (2 CRITICAL, 3 HIGH, 3 MEDIUM)

---

## Certification Criteria

| # | Criterion | Result | Evidence |
|---|-----------|--------|----------|
| 1 | No hardcoded secrets in repository | **❌ FAIL** | Real SMTP credentials in `frontend/web-app/.env:58-62` |
| 2 | JWT uses environment-variable-managed secrets | ✅ PASS | `${APP_JWT_SECRET}` pattern, JWK Set URI in gateway |
| 3 | Security headers enforced at gateway | ✅ PASS | HSTS, CSP, X-Frame-Options, X-Content-Type-Options |
| 4 | Parameterized queries only (no SQL injection) | ✅ PASS | All queries use `@Query` with `:param` binding |
| 5 | CSRF protection (disabled by design for REST APIs) | ✅ PASS | Consistent `.csrf(AbstractHttpConfigurer::disable)` |
| 6 | Prompt injection detection active | ✅ PASS | 12 patterns in AssistantSecurityService, feature-flag enabled |
| 7 | RBAC enforced at gateway + service layers | ⚠️ PASS | Gateway level: ✅; Service `@PreAuthorize`: **MISSING** |
| 8 | Workspace isolation enforced | **❌ FAIL** | Hardcoded `workspaceId("default")` in CustomerCopilotController |
| 9 | No .env files committed to version control | **❌ FAIL** | 6 .env files tracked in git (frontend + 5 mobile apps) |
| 10 | JWT secret has no weak default | ⚠️ MEDIUM | `change-me-in-production` fallback in application.yml |
| 11 | Output sanitization for PII | ✅ PASS | Email, phone, SSN, credit card, Aadhaar, PAN redacted |
| 12 | Rate limiting configured | ✅ PASS | 100 req/s default, 200 burst in gateway |
| 13 | Consistent dependency versions | ✅ PASS | Spring Boot 3.3.3, Java 21 across all services |
| 14 | No insecure defaults | ⚠️ MEDIUM | Empty/H2 passwords acceptable; CHANGE-ME placeholders acceptable |

---

## CRITICAL Findings

### C-1: Real Gmail SMTP Credentials in Version Control
- **File:** `frontend/web-app/.env` (lines 58-62)
- **Value:** `SMTP_USER=[REDACTED]`, `SMTP_PASS=[REDACTED]`
- **Risk:** Any contributor with repo access can send email as this account. Credentials are indexed by GitHub.
- **Remediation:** 
  1. Rotate Gmail app password immediately
  2. Add `*.env` to `.gitignore`
  3. Remove all `.env` files from git history (`git filter-branch` or `BFG Repo-Cleaner`)
  4. Use GitHub Actions secrets or vault for SMTP credentials

### C-2: .env Files Tracked in Git
- **Files:** `frontend/web-app/.env`, `mobile/customer-app/.env`, `mobile/grower-app/.env`, `mobile/dealer-app/.env`, `mobile/admin-companion/.env`
- **Risk:** All environment secrets are exposed in version control. Supabase anon keys + project URL enable unauthenticated API access if RLS is misconfigured.
- **Remediation:** Add `*.env` (except `.env.example`) to `.gitignore`. Purge from git history.

---

## HIGH Findings

### H-1: Supabase Keys Exposed
- **Files:** All 5 mobile/frontend `.env` files
- **Value:** `SUPABASE_ANON_KEY=eyJ...`, `https://irwiiyowpppdynbmwwuz.supabase.co`
- **Risk:** Supabase anon keys are technically public-by-design, but combined with the project URL, they enable API access if Row Level Security is not enforced.
- **Remediation:** Verify RLS policies on all Supabase tables. Anon key should only allow public-read on explicitly public data.

### H-2: No Method-Level Security (`@PreAuthorize`)
- **Finding:** Zero `@PreAuthorize`, `@Secured`, or `@RolesAllowed` annotations across all services
- **Risk:** If a service is accessed directly (bypassing the gateway), there is zero authorization enforcement at the service layer.
- **Remediation:** Add `@EnableMethodSecurity` and `@PreAuthorize` annotations to critical service methods.

### H-3: No CSRF Protection for State-Changing Endpoints
- Note: CSRF is disabled across all services. This is standard for JWT-based REST APIs but creates risk if cookies or session-based auth is used.
- **Status:** Acceptable per REST API best practices with Bearer token auth.

---

## MEDIUM Findings

### M-1: Weak JWT Secret Default
- **File:** `services/identity-service/src/main/resources/application.yml:65`
- **Value:** `secret: ${APP_JWT_SECRET:change-me-in-production}`
- **Risk:** If `APP_JWT_SECRET` env var is not set in production, JWT tokens will be signed with the trivial string `change-me-in-production`.

### M-2: Workspace Isolation Gap
- **File:** `customer-copilot-service/.../CustomerCopilotController.java:66,101,187`
- **Finding:** `workspaceId("default")` hardcoded for all requests
- **Risk:** Multi-tenant workspace isolation is completely bypassed for customer copilot access.

### M-3: Training Data Not Workspace-Scoped
- **File:** `services/training-service/.../InMemoryTrainingRepository.java:28-31`
- **Finding:** `findAll()` returns ALL programs with no workspace filtering

---

## Remediation Priority

| Priority | Finding | Effort | Owner |
|----------|---------|--------|-------|
| P0 | Rotate SMTP credentials, purge .env from git | 1h | DevOps |
| P0 | Add `*.env` to `.gitignore` | 5min | DevOps |
| P1 | Add `@PreAuthorize` to service-layer methods | 8h | Backend |
| P1 | Fix workspace isolation in customer-copilot | 4h | Backend |
| P2 | Remove `change-me-in-production` JWT default | 1h | Backend |
| P2 | Add workspace scoping to training-service | 2h | Backend |
