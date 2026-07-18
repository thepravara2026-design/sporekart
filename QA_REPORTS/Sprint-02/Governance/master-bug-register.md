# SporeKart QA Sprint 2 — Master Bug Register

**Date:** 2026-07-17  
**Scope:** Defects ONLY in implemented functionality. Implementation gaps are in `feature-implementation-register.md`.  
**Total Real Bugs:** 7  

---

## Bug Register

### BUG-001 — Firefox: Complete test failure (mock API interception)

| Field | Value |
|-------|-------|
| **Priority** | P0 — Critical |
| **Classification** | Browser Compatibility |
| **Module** | Authentication, Cross-Browser |
| **Business Impact** | Business Critical — Firefox users cannot authenticate or use the application |
| **Feature Status** | Implemented with Defects |
| **Environment** | Mock (all browsers), Firefox (all versions) |
| **Browser** | Firefox (all tests) |
| **Preconditions** | Any QA Sprint 2 test on Firefox |
| **Steps** | Run any auth-validation, session-management, or cross-browser test on Firefox project |
| **Expected** | Tests should pass or fail with meaningful validation errors |
| **Actual** | All tests time out at 30 seconds. `page.route()` mock interception appears incompatible with Gecko engine. |
| **Evidence** | 11+ Firefox trace files across Parts 1 and 2 |
| **Root Cause** | Mock API route interception (`page.route()`) not properly handled in Gecko. Likely a response streaming or CORS compatibility issue. |
| **Suggested Fix** | Investigate alternative mock approaches for Firefox: HAR files (`page.routeFromHAR()`), WebSocket mock server, or Firefox-specific route handlers |

---

### BUG-009 — LoginPage: WCAG 2.1 AA violations

| Field | Value |
|-------|-------|
| **Priority** | P1 — High |
| **Classification** | Accessibility |
| **Module** | Authentication — Login Page |
| **Business Impact** | Compliance Impact — WCAG non-compliance creates legal exposure |
| **Feature Status** | Implemented with Defects |
| **Browser** | Chromium |
| **Preconditions** | Navigate to /login |
| **Steps** | Run axe-core accessibility scan |
| **Expected** | 0 violations with impact critical or serious |
| **Actual** | 2 violations found (likely form label association and color contrast) |
| **Evidence** | accessibility-Part-8-LoginPage trace |
| **Suggested Fix** | Add proper `<label>` associations and fix color contrast ratio to meet WCAG AA (4.5:1) |

---

### BUG-010 — RegisterPage: WCAG 2.1 AA violation

| Field | Value |
|-------|-------|
| **Priority** | P1 — High |
| **Classification** | Accessibility |
| **Module** | Authentication — Register Page |
| **Business Impact** | Compliance Impact — WCAG non-compliance |
| **Feature Status** | Implemented with Defects |
| **Browser** | Chromium |
| **Preconditions** | Navigate to /register |
| **Steps** | Run axe-core accessibility scan |
| **Expected** | 0 violations with impact critical or serious |
| **Actual** | 1 violation found |
| **Evidence** | accessibility-Part-8-RegisterPage trace |
| **Suggested Fix** | Run detailed axe-core report and fix identified violation |

---

### BUG-011 — Keyboard Tab order: checkbox not focusable

| Field | Value |
|-------|-------|
| **Priority** | P1 — High |
| **Classification** | Accessibility |
| **Module** | Authentication — Login Form |
| **Business Impact** | Usability Impact — Keyboard-only users cannot navigate forms |
| **Feature Status** | Implemented with Defects |
| **Browser** | Chromium |
| **Preconditions** | Navigate to /login |
| **Steps** | Tab from identifier input field |
| **Expected** | Checkbox should receive keyboard focus |
| **Actual** | Checkbox resolves to "inactive" — not focusable despite `aria-disabled="false"` |
| **Evidence** | accessibility-Part-8-tab-order trace |
| **Suggested Fix** | Check tabindex order and ensure checkbox is in the correct tab sequence |

---

### BUG-012 — Interactive elements lack accessible labels

| Field | Value |
|-------|-------|
| **Priority** | P1 — High |
| **Classification** | UX / Accessibility |
| **Module** | Landing Page |
| **Business Impact** | Usability Impact — Screen reader users cannot identify interactive elements |
| **Feature Status** | Implemented with Defects |
| **Browser** | Chromium |
| **Preconditions** | Navigate to homepage |
| **Steps** | Scan all buttons and links for aria-label or visible text |
| **Expected** | All interactive elements have accessible names |
| **Actual** | Some icon-only buttons lack aria-label and visible text |
| **Evidence** | customer-journey-a11y-perf trace |
| **Suggested Fix** | Add aria-label to all icon-only buttons and links |

---

### BUG-015 — Page scroll may be prevented by CSS

| Field | Value |
|-------|-------|
| **Priority** | P2 — Medium |
| **Classification** | UX |
| **Module** | Landing Page |
| **Business Impact** | Usability Impact — Users may not be able to scroll through content |
| **Feature Status** | Implemented with Defects |
| **Browser** | Chromium, WebKit |
| **Preconditions** | Navigate to homepage |
| **Steps** | Execute `window.scrollTo(0, document.body.scrollHeight)` |
| **Expected** | Page scrolls to bottom |
| **Actual** | scrollY remains 0, suggesting CSS `overflow: hidden` on body or html element |
| **Evidence** | customer-journey-landing trace |
| **Suggested Fix** | Check for unintended `overflow: hidden` on body/html in homepage CSS |

---

### BUG-016 — Logo link trailing slash URL inconsistency

| Field | Value |
|-------|-------|
| **Priority** | P2 — Medium |
| **Classification** | UX |
| **Module** | Navigation |
| **Business Impact** | Usability Impact — Minor URL normalization issue |
| **Feature Status** | Implemented with Defects |
| **Browser** | Chromium, WebKit |
| **Preconditions** | Navigate to any page |
| **Steps** | Click header logo/brand link |
| **Expected** | URL navigates to `http://localhost:5174` |
| **Actual** | URL navigates to `http://localhost:5174/` (trailing slash) |
| **Evidence** | customer-journey-navigation trace |
| **Suggested Fix** | Normalize URL comparison. Trailing slash is functionally harmless. |

---

## Classification Summary (Master Bug Register)

| Priority | Count | Bug IDs |
|----------|-------|---------|
| P0 — Critical | 1 | BUG-001 |
| P1 — High | 3 | BUG-009, BUG-010, BUG-011, BUG-012 |
| P2 — Medium | 2 | BUG-015, BUG-016 |
| P3 — Low | 0 | — |
| **Total Real Bugs** | **7** | |

## What This Register Excludes

The following have been moved to `feature-implementation-register.md`:

- BUG-002: No product catalog (Implementation Gap)
- BUG-003: No product details (Implementation Gap)
- BUG-004: No shopping cart (Implementation Gap)
- BUG-005: No role switcher (Implementation Gap)
- BUG-006: No product search (Implementation Gap)
- BUG-007: No filters (Implementation Gap)
- BUG-008: No sorting (Implementation Gap)
- BUG-017: No loading skeletons (Implementation Gap)

The following have been reclassified as Testing Limitations — tracked in updated-dashboard.json:

- BUG-013: WebKit viewport timeout (Testing Limitation)
- BUG-014: Broken links test timeout (Testing Limitation)
- BUG-018: ENOENT cleanup (Testing Limitation)
- BUG-019: networkidle timeout (Testing Limitation)
- BUG-020: OTP flaky on iOS/WebKit (Testing Limitation)
