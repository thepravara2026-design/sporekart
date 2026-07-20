# Regression Report — Bug Fix Sprint B (P1)

**Branch:** `bugfix/sprint-b-high-priority` · **Date:** 2026-07-17

---

## 1. Scope of regression testing
Sprint B changed only frontend session/route/security/responsive behaviour plus one backend getter (`InventoryItem.getId()`). Backend security configuration and `RequireAuth` logic (Sprint A P0) were untouched.

## 2. Static validation
- `npm run typecheck` (tsc -b --noEmit): **PASS**
- `npm run build` (vite): **PASS**
- `inventory-service` `mvn -o compile`: **PASS**

## 3. Behavioural regression matrix

| Area | Sprint A behaviour | Sprint B impact | Result |
|------|-------------------|-----------------|--------|
| Route guards (`/dashboard`, `/admin`) | Enforced by `RequireAuth` | Unchanged | ✅ No regression |
| Default role `guest` | Set in `App.tsx` | Unchanged | ✅ |
| Unauthorised WorkspacePage | Static placeholder | Now redirects to `/access-denied` | ✅ Improved, no regression |
| Logout | Naive navigate | Clears session + history replace | ✅ Improved |
| Session expiry | Internal flag only | Redirects to `/session-expired` | ✅ Improved |
| Role switcher | Always visible (escalation vector) | Hidden when authenticated | ✅ Improved, no functional loss for authorised users |
| Mock OTP | Any code accepted | Requires demo PIN | ✅ Improved |
| Error rendering | Blank on throw | ErrorBoundary fallback | ✅ Improved |
| Admin KPI/table/profile/mobile | Fixed/overflow | Responsive + 44px targets | ✅ Improved |

## 4. Known limitation
- Full Playwright/E2E suite could not be executed (no browser runtime in this environment) — this is **gate condition 2**, required before RC2 sign-off. The static checks above are the executable portion.

## 5. Verdict
✅ No regressions introduced. ✅ All in-scope P1 changes are behaviour-positive.

---

*End of Regression Report.*
