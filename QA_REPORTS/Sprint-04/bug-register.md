# Bug Register — QA Sprint 4

**Date:** 2026-07-18
**Total Open:** 16
**Total Fixed (this sprint):** 2
**Grand Total:** 18

---

## Sprint 4 — Fixed This Sprint

### BUG-QA4-CRIT-001 — Input/Checkbox Style Prop Crash

| Field | Value |
|-------|-------|
| **Severity** | Critical |
| **Priority** | P0 |
| **Module** | UI — Input.tsx, Checkbox.tsx |
| **Status** | FIXED |
| **Root Cause** | CSS strings passed as `style` prop values instead of `React.CSSProperties` objects. React throws error #62 when a style prop value is not a number or string. |
| **Recommendation** | Already fixed. Add a type lint rule to catch non-object style props at compile time. |

---

### BUG-QA4-CRIT-002 — AuthStore sessionStorage Crash in Node.js

| Field | Value |
|-------|-------|
| **Severity** | Critical |
| **Priority** | P0 |
| **Module** | Auth — AuthStore.ts |
| **Status** | FIXED |
| **Root Cause** | `sessionStorage.getItem()` called at module scope unconditionally. Crashes in SSR, Node.js test runners, or any non-browser context. |
| **Recommendation** | Already fixed. Ensure all browser-only APIs across the codebase use `typeof window !== 'undefined'` guards. |

---

## Sprint 3 — Carried Over (Still Open)

### BUG-S3-HIGH-003 — Role Switcher Component Missing

| Field | Value |
|-------|-------|
| **Severity** | High |
| **Priority** | P0 |
| **Module** | Auth / RBAC |
| **Status** | OPEN |
| **Root Cause** | The `select[aria-label="Switch review role"]` component used by 30+ RBAC test cases does not exist in the codebase. Never implemented or removed during refactoring. |
| **Recommendation** | Build a role switcher component (dropdown) accessible on protected pages for dev/testing. Blocking all admin console and RBAC validation. |

---

### BUG-S3-MED-006 — Guest User Not Redirected from Protected Routes

| Field | Value |
|-------|-------|
| **Severity** | Medium |
| **Priority** | P1 |
| **Module** | Auth / Routing |
| **Status** | OPEN |
| **Root Cause** | No route guard middleware implemented. Protected routes render even when unauthenticated, hitting error boundaries instead of redirecting to `/login`. |
| **Recommendation** | Implement an `AuthGuard` wrapper component that checks auth state and redirects to `/login` with a return URL. |

---

### BUG-AUTH-001 — Firefox Auth Complete Failure

| Field | Value |
|-------|-------|
| **Severity** | High |
| **Priority** | P1 |
| **Module** | Auth — Cross-Browser |
| **Status** | OPEN (Sprint 2 carry-over) |
| **Root Cause** | Not yet diagnosed. All auth flows fail on Firefox while working on Chromium. Likely a sessionStorage, cookie, or CSS compatibility issue. |
| **Recommendation** | Investigate Firefox-specific failure in `vite dev` and `vite preview`. Check polyfill coverage and CSS compatibility. |

---

## Sprint 4 — New Bugs (Open)

### BUG-QA4-HIGH-003 — OTP Flow Not Functional Without Navigation State

| Field | Value |
|-------|-------|
| **Severity** | High |
| **Priority** | P0 |
| **Module** | Auth — OTP |
| **Status** | OPEN |
| **Root Cause** | The OTP verification page reads `location.state` from React Router to get the verification context (email, flow type). No code sets this navigation state before routing to the OTP page, so the page renders with empty/invalid state and all OTP operations fail. |
| **Recommendation** | Ensure the login and registration flows pass the required state (`email`, `flow`, `timestamp`) to the OTP route via `useNavigate` with state or a query parameter fallback. |

---

### BUG-QA4-HIGH-004 — Product Detail Pages Show No Pricing/Images

| Field | Value |
|-------|-------|
| **Severity** | High |
| **Priority** | P1 |
| **Module** | Customer — Product Pages |
| **Status** | OPEN |
| **Root Cause** | Product detail pages render with mock data structures but no actual product images, pricing, or descriptions. The mock data layer was never populated with meaningful content. |
| **Recommendation** | Populate mock product data (images, price, description, specs) in the fixture/seed file. Replace placeholder components with real rendering logic. |

---

### BUG-QA4-MED-005 — Admin Dashboard Renders Error Boundary for Unauthorized Users

| Field | Value |
|-------|-------|
| **Severity** | Medium |
| **Priority** | P1 |
| **Module** | Admin Console |
| **Status** | OPEN |
| **Root Cause** | Admin routes have no auth guard. When an unauthenticated or non-admin user navigates to `/admin/*`, the page renders and immediately hits a server/data error, triggering the React ErrorBoundary. Should redirect to `/login` or show an access denied page gracefully. |
| **Recommendation** | Add role-based route guards. Check `user.role === 'admin'` before rendering admin routes. Redirect non-admin users to `/` or `/access-denied`. |

---

### BUG-QA4-MED-006 — Search Case-Insensitive Matching Not Implemented

| Field | Value |
|-------|-------|
| **Severity** | Medium |
| **Priority** | P2 |
| **Module** | Customer — Search |
| **Status** | OPEN |
| **Root Cause** | Product/search filtering performs exact case-sensitive string comparison. Searching "GROW" does not match "Grow" or "grow". |
| **Recommendation** | Normalize both search query and target strings to lowercase (or use `String.localeCompare` with sensitivity: 'base') before comparison. |

---

### BUG-QA4-MED-007 — Header Logo Navigation Fails on Trailing Slash Comparison

| Field | Value |
|-------|-------|
| **Severity** | Medium |
| **Priority** | P2 |
| **Module** | Customer — Navigation |
| **Status** | OPEN (test bug — content renders correctly) |
| **Root Cause** | The header logo test compares the current URL to the expected homepage URL. When navigating to `/products` and clicking the logo, the test expects the URL to be `/` but the app navigates to `` (empty trailing slash) which is effectively the same page but the string comparison fails. |
| **Recommendation** | Update the test to normalize trailing slashes before comparison, or change the assertion to check `page.url().endsWith('/')` instead of an exact match. Application behavior is correct. |

---

### BUG-QA4-LOW-008 — Test regression.spec.ts Expects .sk-header on Homepage

| Field | Value |
|-------|-------|
| **Severity** | Low |
| **Priority** | P3 |
| **Module** | QA — Regression Tests |
| **Status** | OPEN (test bug) |
| **Root Cause** | The regression smoke test searches for `.sk-header` CSS class on the public homepage. The header component renders correctly but uses a different CSS module class name (e.g., `._header_abc123`). The test selector is out of date with the component's CSS module output. |
| **Recommendation** | Update the test selector to use a stable data attribute (e.g., `[data-testid="header"]`) instead of a CSS module class name that changes with each build. |

---

### BUG-QA4-LOW-009 — Cross-Browser Test Expects .auth-layout__brand Class

| Field | Value |
|-------|-------|
| **Severity** | Low |
| **Priority** | P3 |
| **Module** | QA — Cross-Browser Tests |
| **Status** | OPEN (test bug) |
| **Root Cause** | The cross-browser test for auth layout uses `.auth-layout__brand` as a selector. The component renders correctly but uses a CSS module-scoped class name that does not match the BEM-style name. |
| **Recommendation** | Replace the BEM-style class selector with `[data-testid="auth-brand"]` or update the component to include the expected class. |

---

## Summary

| Sprint | Critical | High | Medium | Low | Total |
|--------|----------|------|--------|-----|-------|
| Sprint 4 (fixed) | 2 | 0 | 0 | 0 | **2** |
| Sprint 4 (open) | 0 | 2 | 3 | 2 | **7** |
| Sprint 3 (carried) | 0 | 1 | 1 | 0 | **2** |
| Sprint 2 (carried) | 0 | 1 | 0 | 0 | **1** |
| **Grand Total** | **2** | **4** | **4** | **2** | **12** |

> **Note:** 6 additional medium/low bugs from pre-Sprint 3 are tracked but not detailed here. They are non-blocking and address placeholder/deferred functionality (cart, checkout, payment, security headers).

---

*End of Bug Register — QA Sprint 4*
