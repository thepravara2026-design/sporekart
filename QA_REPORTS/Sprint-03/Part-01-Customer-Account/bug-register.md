# Bug Register — QA Sprint 3 Part 1: Customer Account & Dashboard

**Date:** 2026-07-18
**Tester:** Staff SDET / Principal QA Engineer
**Environment:** http://localhost:4173 (vite preview — production build)
**Browser:** Chromium Desktop Chrome
**Tool:** Playwright 1.61.1
**Test Suite:** 88 tests across 10 validation areas

## Classification Key

| Severity | Meaning |
|----------|---------|
| S1 Critical | Complete feature/flow blocked; no workaround |
| S2 High | Major functionality broken; workaround possible |
| S3 Medium | Partial functionality broken; non-blocking |
| S4 Low | Cosmetic, minor, enhancement |

| Priority | Meaning |
|----------|---------|
| P1 | Must fix before next release |
| P2 | Should fix; important |
| P3 | Could fix; minor |
| P4 | Won't fix this cycle |

---

## Defects Found (Part 1)

### BUG-S3-P1-001: Production build crash — all customer account pages show ErrorBoundary

**Severity:** S1 Critical | **Priority:** P1
**Component:** Build pipeline / Design System (CSS-in-JS)
**Status:** Open (inherited from initial Sprint 3 sweep — BUG-S3-CRIT-001)

**Reproduction Steps:**
1. Run `npx vite preview --port 4173` from `frontend/web-app`
2. Navigate to any customer account page: `/dashboard`, `/dashboard/profile`, `/dashboard/orders`, etc.
3. Observe ErrorBoundary fallback on every page

**Expected:** Each page renders its respective content (dashboard metrics, profile form, order list, etc.)
**Actual:** Every page shows "Something went wrong — An unexpected error occurred while rendering this screen"

**Console Errors (per page):** 14
- Minified React error #62 (style property value type)
- `TypeError: Failed to set an indexed property [0] on 'CSSStyleDeclaration'`

**Evidence:**
- Screenshots in `Evidence/Screenshots/` for all 16 customer account routes
- Traces in `html-report/data/*.zip`
- Videos in `html-report/data/*.webm`

**Root Cause Hypothesis:** CSS-in-JS library (Emotion/styled-components) uses indexed property access on CSSStyleDeclaration which is no longer supported in Chromium. Only manifests in minified production build; dev server is unaffected.

**Impact:** 0% of customer account functionality accessible. Login, dashboard, profile, orders, addresses all blocked.

**Risk:** Extreme — blocks all user-facing functionality beyond the homepage.

**Recommendation:** Fix before any other Sprint C work. Update CSS-in-JS library or replace indexed property access with `.setProperty()`.

---

### BUG-S3-P1-002: No role switcher — RBAC validation impossible for customer account

**Severity:** S2 High | **Priority:** P2
**Component:** RBAC / Auth Testing Infrastructure
**Status:** Open (inherited — BUG-S3-HIGH-003)

**Description:** The `select[aria-label="Switch review role"]` component used to test role-based access is absent from the source code. All RBAC test cases cannot execute.

**Impact:** Cannot validate customer-specific vs. grower-specific vs. admin-specific account views. 30+ existing RBAC tests permanently broken.

---

### BUG-S3-P1-003: No semantic ARIA landmarks on customer account pages

**Severity:** S2 High | **Priority:** P2
**Component:** Accessibility / CustomerLayout
**Status:** Open

**Description:** Customer account pages (dashboard, profile, orders, etc.) have zero `<main>`, `<nav>`, or `<header>` semantic landmark elements. Combined with the ErrorBoundary, pages are invisible to screen reader region navigation.

**Impact:** WCAG 2.1 SC 1.3.1, SC 2.4.1 violations. Screen reader users cannot navigate.

---

### BUG-S3-P1-004: Admin routes accessible without authentication

**Severity:** S2 High | **Priority:** P1
**Component:** Auth / Route Protection
**Status:** Open (known gap — BUG-ADM-001)

**Description:** `/admin/dashboard`, `/admin/products`, `/admin/orders` are not wrapped in `RequireAuth`. Any user can access admin routes without authentication.

**Evidence:** Test 7.2 confirmed admin pages load without redirect or access-denied.

---

### BUG-S3-P1-005: Guest access to dashboard shows ErrorBoundary, not login redirect

**Severity:** S3 Medium | **Priority:** P3
**Component:** Auth / ErrorBoundary
**Status:** Open

**Description:** When an unauthenticated guest navigates to `/dashboard`, the `RequireAuth` component attempts to redirect to `/login`, but the login page crashes (BUG-S3-P1-001). The ErrorBoundary catches the crash and shows a generic error. User never sees a login prompt or access-denied message.

**Impact:** Users don't know they need to authenticate. Poor UX — app appears broken.

---

### BUG-S3-P1-006: Console errors on every customer account page (14 per page)

**Severity:** S3 Medium | **Priority:** P3
**Component:** Error Handling
**Status:** Open

**Description:** Every customer account page logs 14 console errors (React #62 + CSSStyleDeclaration). The cross-cutting audit confirmed this across all 16 tested routes.

**Impact:** Console pollution, false alerts in error monitoring tools, hides legitimate errors.

---

### BUG-S3-P1-007: Notification bell may not have unread badge functionality

**Severity:** S4 Low | **Priority:** P4
**Component:** Notifications
**Status:** Open

**Description:** Test 5.4 confirmed the notification bell is present on the homepage, but unread badge count functionality could not be verified because the notification page crashes with ErrorBoundary.

**Impact:** Low — cosmetic/functionality gap. Users cannot see unread notification count.

---

## Defect Summary

| ID | Severity | Priority | Area | Root Cause |
|----|----------|----------|------|------------|
| BUG-S3-P1-001 | S1 Critical | P1 | Build / Design System | CSS-in-JS indexed property access broken in minified build |
| BUG-S3-P1-002 | S2 High | P2 | RBAC / Testing | Role switcher component absent from codebase |
| BUG-S3-P1-003 | S2 High | P2 | Accessibility | No semantic landmarks in customer layout |
| BUG-S3-P1-004 | S2 High | P1 | Auth / Route Protection | Admin routes missing RequireAuth |
| BUG-S3-P1-005 | S3 Medium | P3 | Auth / ErrorBoundary | Login crash hides auth redirect |
| BUG-S3-P1-006 | S3 Medium | P3 | Error Handling | 14 console errors per page load |
| BUG-S3-P1-007 | S4 Low | P4 | Notifications | Unread badge not verifiable |

**Total: 7 defects (1 Critical, 3 High, 2 Medium, 1 Low)**

---

*End of Bug Register — QA Sprint 3 Part 1*
