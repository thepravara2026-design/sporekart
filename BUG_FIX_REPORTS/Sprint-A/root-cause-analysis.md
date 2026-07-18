# Root Cause Analysis — Bug Fix Sprint A

**Release Candidate:** v1.0.0-rc1 | **Date:** 2026-07-17

---

## RC-1 — No Route Guards Anywhere (BUG-RT-001..006, SEC-001/002)

- **Root Cause:** `App.tsx` declared all `<Route>` elements (including `/dashboard` and `/admin` subtrees) without an authentication/authorization wrapper. `activeRole` was initialised to `'administrator'`.
- **Symptoms:** Guests could deep-link to any customer or admin page; no redirect to `/login`; privilege-escalation-by-default.
- **Affected modules:** `frontend/web-app/src/App.tsx`, `CustomerLayout`, `AdminLayout`.
- **Dependencies:** `AppContext.activeRole`.
- **Regression risk:** Low — additive guard; public/non-enterprise routes untouched.
- **Business risk:** Critical (full access-control bypass).
- **Chosen fix:** Introduce `RequireAuth` component (redirects `guest` → `/login`; enforces `allowedRoles` for admin). Wrap `/dashboard` and `/admin` route elements. Default role → `'guest'`, initialised from `sessionStorage` so refresh preserves session.
- **Rejected alternates:** Per-route manual checks (duplication); middleware at nginx only (not in repo scope).

## RC-2 — Mock Authentication (SEC-003)

- **Root Cause:** Auth flow completed at `VerifyOtpPage` → `AuthLoadingPage`, but `AuthLoadingPage` only navigated to `/` without ever calling `setActiveRole`. No session was ever established; `authClient` is a documented UI stub.
- **Symptoms:** "Login" succeeded visually but no authenticated state existed; `RequireAuth` would treat everyone as `guest`.
- **Affected modules:** `features/auth/pages/SessionPages.tsx`, `features/auth/authClient.ts`.
- **Fix:** `AuthLoadingPage` now sets `activeRole` (persisted to `sessionStorage`) and navigates to the intended destination (or `from` return URL).

## RC-3 — Missing Backend Security Configuration (BUG-API-001..012, SEC-004)

- **Root Cause:** 15 of 16 Spring Boot services had `spring-boot-starter-security` on the classpath but **no `SecurityConfig` bean**, and controllers carried no `@PreAuthorize`. The AI `SemanticController` accepted a client-supplied `X-User-Id` header (mass assignment).
- **Symptoms:** Endpoints effectively unauthenticated; anyone could create orders/shipments/payments/products/notifications; order endpoints exposed other users' data (IDOR); analytics exposed customer PII and payment metrics.
- **Affected modules:** `services/*/interfaces/rest/*Controller.java`, `services/*/config/` (new `SecurityConfig`).
- **Fix:** Add `SecurityConfig` (stateless, `authenticated()` on all, permit health/swagger/error) to all 15 services; add `@PreAuthorize` role/ownership checks on mutating and PII/payment endpoints; add IDOR ownership enforcement in `OrderController`; remove `X-User-Id` mass-assignment in `SemanticController`.
- **Rejected alternates:** Disabling security (insecure); full JWT IdP build-out (out of stabilization scope).

---

*End of Root Cause Analysis.*
