# Bug Register — QA Sprint 3

**Date:** 2026-07-18  
**Tester:** Enterprise Principal QA Engineer  
**Branch:** current workspace  
**Server:** `http://localhost:4173` (vite preview — production build)  
**Browser:** Chromium (Desktop Chrome)  
**Tool:** Playwright 1.61.1

---

## Classification Key

| Severity | Meaning |
|----------|---------|
| **S1 - Critical** | Complete feature/flow blocked; no workaround |
| **S2 - High** | Major functionality broken; workaround possible |
| **S3 - Medium** | Partial functionality broken; non-blocking |
| **S4 - Low** | Cosmetic, minor, enhancement |

| Priority | Meaning |
|----------|---------|
| **P1** | Must fix before next release |
| **P2** | Should fix; important |
| **P3** | Could fix; minor |
| **P4** | Won't fix this cycle |

---

## Defects Found

### BUG-S3-CRIT-001: Production build crashes on all complex pages — CSS-in-JS / React #62

| Field | Value |
|-------|-------|
| **Severity** | S1 - Critical |
| **Priority** | P1 |
| **Area** | Build pipeline / Design System |
| **Status** | Open |

**Steps to Reproduce:**
1. Run `npx vite preview --port 4173` from `frontend/web-app/`
2. Open `http://localhost:4173/login` in Chromium
3. Observe blank page with "Something went wrong" error boundary

**Expected:** Login page renders with phone/email input, terms checkbox, and sign-in button.
**Actual:** React ErrorBoundary shows "Something went wrong — An unexpected error occurred while rendering this screen." Console shows 14 errors:
  - Minified React error #62 (style property value is not a number or string)
  - `TypeError: Failed to set an indexed property [0] on 'CSSStyleDeclaration': Indexed property setter is not supported.`

**Evidence:**
- Screenshot: `Evidence/Screenshots/__login__17-security-no-console-errors.png`
- Trace: `test-results/*login*/trace.zip`
- Console log: 14 errors recorded

**Root Cause Hypothesis:**
CSS-in-JS library (likely Emotion or styled-components) attempts to set CSS custom properties via numeric index (`style[0]`), which is no longer supported in Chromium's CSSStyleDeclaration. React error #62 occurs because a style prop receives a non-numeric/non-string value (possibly `undefined`/`null`). This only manifests in the production (minified) build — the dev server may include fallback/polyfill behavior.

**Impact:**
- Login page: 0 forms, 0 inputs rendered
- ALL authentication flows blocked
- ALL protected routes inaccessible (they redirect through the broken login page)
- Admin dashboard also crashes (14 errors, ErrorBoundary shown)
- All customer dashboard sub-pages (profile, wishlist, training, etc.) inaccessible

**Suggested Fix:**
1. Run production build with `NODE_ENV=development` temporarily to identify the exact minified error
2. Update the CSS-in-JS library to a version compatible with Chromium's CSSStyleDeclaration changes
3. Alternatively, replace indexed property access with `.setProperty()` API calls
4. Verify fix by running `vite preview` and testing `/login` page

---

### BUG-S3-CRIT-002: All authenticated/admin routes show error boundary — zero functional pages

| Field | Value |
|-------|-------|
| **Severity** | S1 - Critical |
| **Priority** | P1 |
| **Area** | Route Protection / Auth |
| **Status** | Open |

**Steps to Reproduce:**
1. Navigate to any of: `/dashboard`, `/admin/dashboard`, `/dashboard/profile`, `/dashboard/wishlist`, `/dashboard/training`, `/admin/products`
2. Observe "Something went wrong" ErrorBoundary on all pages

**Expected:** Each page renders its respective content (dashboard metrics, admin KPIs, wishlist items, etc.)
**Actual:** Every route crashes with 14 console errors (same React #62 + CSSStyleDeclaration errors)

**Evidence:**
- Dashboard: `Evidence/Screenshots/__dashboard_01-dashboard-load.png`
- Admin: `Evidence/Screenshots/__admin_dashboard_08-admin-dashboard.png`
- Console logs confirm identical error pattern on all pages

**Root Cause Hypothesis:**
Same as BUG-S3-CRIT-001. The ErrorBoundary wraps the entire app's content area. Any page that imports the shared component library (Input, Checkbox, Button, etc.) triggers the crash because CSS-in-JS's style injection fails.

**Impact:**
- 0% of protected/admin pages functional in production build
- Application is effectively unusable beyond the homepage
- All Sprint B bug fixes invisible in production

**Suggested Fix:**
Root cause must be addressed first (BUG-S3-CRIT-001). No page-specific fix is possible.

---

### BUG-S3-HIGH-003: Role switcher component absent from codebase — RBAC testing infrastructure invalid

| Field | Value |
|-------|-------|
| **Severity** | S2 - High |
| **Priority** | P2 |
| **Area** | RBAC / Auth Testing |
| **Status** | Open |

**Steps to Reproduce:**
1. Search codebase for `select[aria-label="Switch review role"]`, `roleSwitcher`, `RoleSwitcher`, `Switch review role`
2. Observe zero matches in any source file under `frontend/web-app/src/`
3. Navigate to homepage in production build; no role switcher present

**Expected:** Role switcher component exists for manual/integration RBAC testing
**Actual:** Component completely absent. All existing RBAC test specs (protected-routes.spec.ts: 3 tests, rbac-authorization.spec.ts: 27 tests) rely on a non-existent component

**Evidence:**
- Grep results show zero matches for role-switcher related selectors
- Screenshot: homepage shows no role control
- Existing test `"Homepage role switcher exists with expected roles"` fails with `locator('select[aria-label="Switch review role"]') not found`

**Root Cause Hypothesis:**
The role switcher was either:
1. Removed during a refactor/Bug Fix Sprint B and not restored
2. Never implemented in the web-app (might exist in a different frontend app)
3. Injected by a Vite plugin or dev-only tool that's absent in the production build

**Impact:**
- 30 existing test cases are permanently broken for production/preview
- Manual RBAC verification impossible without the switcher
- Cannot test role-based access control without modifying the underlying auth system

**Suggested Fix:**
1. Determine if role switcher should be a dev-only tool or a production feature
2. If production: implement a profile-based role selector
3. If dev-only: restore the component and ensure it's available via environment flag
4. Update test specs to handle presence/absence of the switcher

---

### BUG-S3-HIGH-004: No semantic ARIA landmarks on any pages

| Field | Value |
|-------|-------|
| **Severity** | S2 - High |
| **Priority** | P2 |
| **Area** | Accessibility |
| **Status** | Open |

**Steps to Reproduce:**
1. Navigate to any page
2. Inspect for `<main>`, `<nav>`, `<header>`, `[role="main"]`, `[role="navigation"]`, `[role="banner"]`

**Expected:** Every page should have at minimum `<main>` and `<nav>` landmarks
**Actual:** Combined count of `<main>`, `<nav>`, `<header>` elements is 0

**Evidence:**
- Test `"ARIA landmarks present on dashboard pages"` failed: `Expected: >= 2, Received: 0`
- Screenshots confirm no landmark elements rendered

**Root Cause Hypothesis:**
The app uses custom components (e.g., `Header`, `Sidebar`) that don't render semantic landmark elements. The ErrorBoundary also doesn't render landmarks.

**Impact:**
- Screen reader users cannot navigate between page regions
- WCAG 2.1 SC 1.3.1 (Info and Relationships) and SC 2.4.1 (Bypass Blocks) violations
- Accessibility audit score will be severely impacted

**Suggested Fix:**
1. Add `<main>` wrapper around page content areas
2. Add `role="navigation"` or `<nav>` to sidebar/header components
3. Update ErrorBoundary to include `role="alert"` on the error container

---

### BUG-S3-MED-005: Console errors on login page — 14 errors per page load

| Field | Value |
|-------|-------|
| **Severity** | S3 - Medium |
| **Priority** | P3 |
| **Area** | Error Handling |
| **Status** | Open |

**Steps to Reproduce:**
1. Navigate to `/login`
2. Open browser DevTools Console

**Expected:** 0 console errors on login page
**Actual:** 14 console errors (React #62 + CSSStyleDeclaration TypeError) per page load

**Evidence:**
- Test `"No console errors on login page"` failed: `Expected: 0, Received: 14`
- Console errors captured in test traces

**Root Cause Hypothesis:**
Same as BUG-S3-CRIT-001. The errors are logged before the ErrorBoundary catches them, causing console pollution even though the page "handles" the error.

**Impact:**
- DevTools console polluted with errors
- Error monitoring tools (Sentry, Datadog) would fire 14+ errors per page view
- Makes debugging legitimate issues harder

**Suggested Fix:**
Root cause fix per BUG-S3-CRIT-001 will resolve this as a side effect.

---

### BUG-S3-MED-006: Guest user not redirected from protected routes — auth guard invisible

| Field | Value |
|-------|-------|
| **Severity** | S3 - Medium |
| **Priority** | P3 |
| **Area** | Auth / Route Protection |
| **Status** | Open |

**Steps to Reproduce:**
1. Navigate to `/dashboard` without authentication
2. Observe "Something went wrong" ErrorBoundary

**Expected:** Unauthenticated guest is redirected to `/login` 
**Actual:** ErrorBoundary shown instead of redirect. The `RequireAuth` component's `<Navigate to="/login">` never executes because the component tree crashes before it can process.

**Evidence:**
- Test `"Unauthenticated guest redirected from /dashboard"` failed
- ErrorBoundary snapshot shows error fallback, not redirect or access-denied

**Root Cause Hypothesis:**
The ErrorBoundary wraps the route content, including RequireAuth. When the login page (the redirect target) crashes, React re-throws the error into the ErrorBoundary. The Navigate component never completes.

**Impact:**
- Guest users see "Something went wrong" instead of login page
- Unclear error state — user doesn't know they need to authenticate
- Poor UX for unauthenticated access

**Suggested Fix:**
1. Fix BUG-S3-CRIT-001 (login page crash)
2. Consider moving ErrorBoundary below RequireAuth so auth redirects can bypass it

---

### BUG-S3-LOW-007: Missing dashboard navigation links

| Field | Value |
|-------|-------|
| **Severity** | S4 - Low |
| **Priority** | P4 |
| **Area** | Customer Dashboard |
| **Status** | Open |

**Steps to Reproduce:**
1. Navigate to `/dashboard`

**Expected:** The CustomerLayout sidebar renders navigation links (Dashboard, Orders, Wishlist, Training, Products, Addresses, Support, Notifications)
**Actual:** Page shows ErrorBoundary — no navigation links rendered

**Evidence:**
- Test `"Dashboard navigation links are present"` failed: `Expected: >= 2, Received: 0`
- Screenshot shows error boundary only

**Root Cause Hypothesis:**
Root cause is BUG-S3-CRIT-001. Once the build crash is fixed, navigation links should appear (CustomerLayout defines them at `CustomerLayout.tsx:18-29`).

**Impact:**
- Navigation within the customer area impossible
- Users cannot reach any dashboard sub-page

**Suggested Fix:**
Fix BUG-S3-CRIT-001.

---

## Defect Summary

| ID | Severity | Priority | Area | Root Cause |
|----|----------|----------|------|------------|
| BUG-S3-CRIT-001 | S1 Critical | P1 | Build / Design System | CSS-in-JS style injection broken in production build |
| BUG-S3-CRIT-002 | S1 Critical | P1 | Route Protection | All pages crash due to shared component library failure |
| BUG-S3-HIGH-003 | S2 High | P2 | RBAC / Testing | Role switcher component absent from codebase |
| BUG-S3-HIGH-004 | S2 High | P2 | Accessibility | No semantic ARIA landmarks on any page |
| BUG-S3-MED-005 | S3 Medium | P3 | Error Handling | 14 console errors per page load |
| BUG-S3-MED-006 | S3 Medium | P3 | Auth / Route Protection | Guest redirect invisible behind error boundary |
| BUG-S3-LOW-007 | S4 Low | P4 | Customer Dashboard | Missing navigation links (secondary to CRIT-001) |

**Total: 7 defects (2 Critical, 2 High, 2 Medium, 1 Low)**

---

## Cross-Reference to Existing Sprint B Defects

| This Sprint | Related Sprint B Issue | Relationship |
|-------------|----------------------|--------------|
| BUG-S3-CRIT-001 | (new) | Regression — build was not tested in production mode |
| BUG-S3-CRIT-002 | BUG-RT-001..006 (resolved) | Auth guards implemented but invisible behind crash |
| BUG-S3-HIGH-003 | BUG-RBAC-* (existing tests) | Role switcher assumed present — missing entirely |
| BUG-S3-HIGH-004 | (new) | Accessibility gap not previously detected |
| BUG-S3-MED-005 | (new) | Console errors masked by ErrorBoundary in production |
| BUG-S3-MED-006 | BUG-RT-001..006 (resolved) | Auth guard exists but unreachable |
| BUG-S3-LOW-007 | (new) | Secondary effect of CRIT-001 |

---

*End of Bug Register — QA Sprint 3*
