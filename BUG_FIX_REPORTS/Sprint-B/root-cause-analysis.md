# Root Cause Analysis — Bug Fix Sprint B (P1)

**Branch:** `bugfix/sprint-b-high-priority` · **Date:** 2026-07-17

---

## BUG-RT-007 — WorkspacePage placeholder instead of redirect
- **Symptom:** When a role lacked view permission, `WorkspacePage` rendered a static "Access restricted" placeholder rather than enforcing the restriction.
- **Root cause:** The component treated the restriction as informational UI; it never transitioned the router state, so an unauthorised role stayed on a protected-looking page.
- **Fix:** Redirect to the dedicated `/access-denied` route via `<Navigate replace>`, matching the `RequireAuth` guard behaviour.

## BUG-RT-008 — No browser history clearing on logout
- **Symptom:** After logout the stored role survived in `sessionStorage` and the browser back button could return to a protected view.
- **Root cause:** Logout was a plain `navigate('/login')` with no session teardown.
- **Fix:** Central `logout()` in `AppContext` removes `sk_session_role`, resets to `guest`, and uses `navigate(..., { replace: true })`.

## BUG-RT-009 — No multi-tab session synchronization
- **Symptom:** Signing in/out in one tab did not update other open tabs.
- **Root cause:** Role state was local React state with no cross-tab signal.
- **Fix:** A `window` `storage` listener in `App` mirrors `sk_session_role` changes into `activeRole`.

## BUG-RT-010 — No global error boundary
- **Symptom:** An unhandled render exception blanked the entire shell.
- **Root cause:** No `ErrorBoundary` wrapped the `<Routes>`.
- **Fix:** `components/ErrorBoundary.tsx` wraps both enterprise and public route subtrees.

## BUG-RT-011 — No session expiry auto-redirect
- **Symptom:** Session expiry only flipped an internal flag; no redirect occurred.
- **Root cause:** `useSession.endSession()` set state `'expired'` but never navigated or cleared the session.
- **Fix:** `endSession()` clears `sessionStorage`, dispatches a `storage` event, and navigates to `/session-expired` (replace).

## BUG-PERF-002 — Timer leak in SessionTimeoutWarning
- **Symptom:** Potential dangling interval / double `onLogout` under StrictMode.
- **Root cause:** `setInterval` was created inside a `setState` updater; cleanup depended on `clearInterval(interval)` inside that updater.
- **Fix:** Interval handle stored in a ref; `onLogout` guarded by a `loggedOutRef` so it fires exactly once; cleanup guaranteed on every open/close/unmount.

## SEC-005 — Role change via UI dropdown
- **Symptom:** Any visitor could select `administrator` from the header dropdown and reach admin areas.
- **Root cause:** The role `<select>` mutated the session role directly, bypassing authorization.
- **Fix:** Switcher is shown only on the public site (`activeRole === 'guest'`) as a design preview; hidden once authenticated.

## SEC-011 — Mock OTP accepts any code
- **Symptom:** `verifyOtp` accepted any 6-char code except `000000`.
- **Root cause:** Stub accepted arbitrary input; only one magic-reject value.
- **Fix:** Requires deterministic demo PIN `123456`; remains an explicit UX stub.

## BUG-COMP-001/MOB-001 — KPI grid collapses poorly
- **Root cause:** Fixed `repeat(columns,1fr)` inline style, no reflow.
- **Fix:** CSS-driven fluid `auto-fit` → 2-col (≤1200px) → 1-col (≤767px).

## BUG-COMP-002/MOB-002, COMP-003/MOB-003, MOB-006 — Tables overflow, profile overlap, sub-44px targets
- **Root cause:** No responsive/scroll container; inline flex without mobile stacking; controls without minimum touch area (WCAG 2.5.5).
- **Fix:** Global responsive stylesheet in `admin/admin.css`.

---

*End of Root Cause Analysis.*
