# Root Cause Analysis — Sprint C

## Root Cause Clusters

### Cluster 1: CSS-in-JS Build Failure (CRIT-001)

**Affected items**: All routes (symptomatic fixes in P2 backlog)

**Root Cause**: CSS-in-JS library (Emotion/styled-components) uses numeric indexed property access on `CSSStyleDeclaration` (`style[0]`, `style[1]`, etc.). Modern Chromium versions have deprecated indexed property setters on `CSSStyleDeclaration`, causing a `TypeError` when the library attempts to inject styles. React error #62 occurs because a style prop receives an `undefined`/`null` value (the CSS-in-JS library returns undefined when style injection fails).

**Fix**: Update CSS-in-JS library or replace `.style[index] = value` with `.style.setProperty()`. Alternatively, replace the CSS-in-JS library with static CSS modules.

**Dependency**: Blocks visual verification of BUG-C-001, BUG-C-002, BUG-C-003 (partial), BUG-C-004.

---

### Cluster 2: Auth Infrastructure Gaps

**Affected items**: BUG-C-006 (auth stub), BUG-S3-P1-004 (admin routes), BUG-S3-HIGH-005 (infinite loop)

**Root Causes**:
1. **BUG-C-006**: The auth client was intentionally implemented as a stub (marked with warning comments). It simulates OTP flow with hardcoded code `123456`. Was meant to be replaced before production.
2. **BUG-S3-P1-004**: Admin route definitions in `App.tsx` lack `RequireAuth` wrapper — likely oversight during route organization.
3. **BUG-S3-HIGH-005**: Auth context initialization sets `role` to empty string `""`. Route guards check `activeRole` against `allowedRoles` array that doesn't include empty string, causing a re-render loop.

**Fix Pattern**: Replace stub with real Supabase Auth or backend JWT auth. Add `RequireAuth` to admin routes. Fix empty role initialization.

---

### Cluster 3: Missing Infrastructure

**Affected items**: BUG-C-005 (Service Worker), BUG-C-009 (perf budgets), BUG-S3-P3-001 (observability)

**Root Causes**: These are not implemented yet — the project is in early development phase. `monitoring/`, `observability/`, `logging/` directories are empty placeholders. No Service Worker was ever created. No performance CI tools configured.

**Fix Pattern**: Greenfield implementation. Document standards first, then implement.

---

### Cluster 4: Frontend Component Bugs

**Affected items**: BUG-C-001 (double upload), BUG-C-002 (save button), BUG-C-003 (ARIA), BUG-C-004 (mobile nav), BUG-C-007 (duplicate toasts), BUG-C-008 (404 page)

**Root Causes**:
1. **BUG-C-001**: Avatar component likely renders both a styled wrapper `<input>` and the native `<input>` — two file inputs visible.
2. **BUG-C-002**: Notification preferences page loads data asynchronously; the Save button's `disabled` state is bound to a loading flag that never resets.
3. **BUG-C-003**: Layout components (`Header`, `Sidebar`, `Main`) use `<div>` elements instead of semantic `<header>`, `<nav>`, `<main>`.
4. **BUG-C-004**: Mobile nav toggle shows/hides the container but nav items are lazy-loaded and never mount.
5. **BUG-C-007**: Two separate implementations evolved — `ToastQueue` (modern, context-based) and `ToastProvider` (legacy, simpler). Both are functional but create inconsistent UX.
6. **BUG-C-008**: Router is configured with wildcard catch-all only for known route prefixes. Unknown routes hit SPA's `index.html` with no 404 page.

**Fix Pattern**: Targeted component fixes. Each is independent.

## Root Cause Distribution

| Cluster | Items | % of P2 Backlog |
|---------|-------|----------------|
| CSS-in-JS Build Failure | 4 (blockers) | — (P1, not in Sprint C) |
| Auth Infrastructure | 3 | 11% (BUG-C-006) |
| Missing Infrastructure | 3 | 22% (BUG-C-005, BUG-C-009) |
| Frontend Component Bugs | 6 | 67% (BUG-C-001, 002, 003, 004, 007, 008) |

## Summary

The Sprint C P2 backlog is dominated by frontend component bugs (67%) that are independent, low-risk, and well-understood. The auth infrastructure gap (11%) and missing infrastructure items (22%) require more effort but are also well-understood. The critical dependency is the build crash (BUG-S3-CRIT-001), which must be resolved to verify 4 of 9 items.
