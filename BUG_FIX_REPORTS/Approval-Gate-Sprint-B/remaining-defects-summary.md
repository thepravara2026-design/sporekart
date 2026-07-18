# Remaining Defects Summary — Approval Gate Sprint B

**Branch:** `bugfix/sprint-b-high-priority` · **Date:** 2026-07-17

## 1. Assigned P1 — Disposition
- **18 assigned items (16 P1 + 1 blocker + SEC-011 counted in P1):** ✅ 18/18 resolved.

## 2. Deferred P1 (tracked, out of P1 stabilization scope)

| ID | Title | Classification | Reason |
|----|-------|----------------|--------|
| BUG-API-013 | No JWT filter implementation | Implementation Gap | Backend IdP integration |
| BUG-API-014 | No error-boundary standardization (backend) | Implementation Gap | Platform-wide |
| BUG-API-015 | Cart service empty placeholder | Implementation Gap | Phase 0 foundation |
| BUG-API-016 | Content service empty placeholder | Implementation Gap | Phase 0 foundation |
| BUG-API-017 | Search service empty placeholder | Implementation Gap | Phase 0 foundation |
| BUG-API-018 | Support service empty placeholder | Implementation Gap | Phase 0 foundation |
| BUG-API-019 | GET /users/me placeholder string | Implementation Gap | Backend build-out |
| BUG-API-020 | No pagination on list endpoints | Implementation Gap | Backend foundation |
| SEC-006 | No CSP header | Deferred | Gateway/platform config |
| SEC-007 | No X-Frame-Options | Deferred | Gateway/platform config |
| SEC-008 | No security headers (all 8) | Deferred | Gateway/platform config |
| SEC-009 | IDOR on controllers (dup API-003) | Partially Fixed | Orders fixed Sprint A; other services deferred |
| SEC-010 | No session management (dup RT-008/009) | Fixed (frontend) | Resolved in Sprint B |
| SEC-012 | No rate limiting | Deferred | API gateway work |
| ai-service compile | Deeper pre-existing defects | Blocked | Requires dedicated foundation sprint |

## 3. Classification counts
- **P1 Deferred:** 12 (API-013..020, SEC-006..008, SEC-012)
- **Implementation Gap:** 8 (cart/content/search/support/users-me/pagination/jwt/error-boundary)
- **Accepted Risk:** 0
- **Deferred (platform):** 4 (SEC-006..008, SEC-012)
- **Blocked:** 1 (ai-service foundation)

## 4. Repository artifacts (not defects, but cleanliness risk)
- Untracked `ai-service/.../config/SecurityConfig.java` (pre-existing, unrelated to B).
- Uncommitted Sprint A working-tree changes (backend SecurityConfigs/controllers; LoginPage/RegisterPage/SessionPages/RequireAuth).
- Untracked `QA_REPORTS/`, `shared-testing/`, `run.bat`, `debug-login.png`.

---

*End of Remaining Defects Summary.*
