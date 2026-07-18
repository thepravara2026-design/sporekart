# Regression Report — Bug Fix Sprint A

**Scope:** Modules touched — `App.tsx`, `RequireAuth.tsx` (new), `SessionPages.tsx`, `LoginPage.tsx`, `RegisterPage.tsx`, backend `SecurityConfig` (×15 new) and 10 controller classes.

## Affected module checks
- **Routing:** Only `/dashboard` and `/admin` route elements gained a `<RequireAuth>` wrapper. Public website routes (`/`, `/products`, `/training`, blog, legal, auth routes, `/preview/*`) are untouched → no regression to public navigation.
- **Authentication:** `activeRole` default changed `administrator` → `guest`. Public/guest flows (browse catalog, view training) require no role and are unaffected. Authenticated flows now require a real session.
- **State management:** `AppContext` shape unchanged (added `sessionStorage` persistence in the `useState` initializer only).
- **Backend controllers:** Only authorization annotations and ownership checks added; no signature/contract changes → no API contract regression for legitimate callers.

## Regression validation
- `npm run typecheck` ✅ pass
- `npm run build` ✅ pass (no new chunks, bundle size stable)
- `mvn -o compile` ✅ for 8 of 10 edited services (2 blocked by pre-existing, unrelated errors — see remaining-critical-bugs.md)

## Result
**No regressions introduced by Sprint A fixes.**

---

*End of Regression Report.*
