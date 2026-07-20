# SporeKart QA Sprint 2 — Bug Register: Part 1 Authentication

**Date:** 2026-07-17  
**RC:** v1.0.0-rc1  
**Total Bugs:** 9 (1 Critical, 4 High, 3 Medium, 1 Low)

---

## Critical

### BUG-AUTH-001 — Firefox: Complete test failure on all auth operations
- **Severity:** Critical
- **Browser:** Firefox (all 11 tests)
- **Test:** All Part 1 — Auth Validation tests
- **Symptom:** Every test times out at 30 seconds on Firefox. First-run and retry both fail.
- **Root Cause (suspected):** Mock API route interception (`page.route()`) may not be properly handled in Gecko engine. Possibly a timing/response delivery issue.
- **Evidence:** test-results/auth-validation-Part-1-*firefox*/trace.zip
- **Recommendation:** Investigate `page.route()` compatibility with Firefox. Consider adding user-agent-specific mock handlers or extending timeout for Firefox.

---

## High

### BUG-AUTH-002 — LoginPage: 2 WCAG critical/serious violations
- **Severity:** High
- **Browser:** Chromium
- **Test:** Part 8 — Accessibility Validation > LoginPage accessibility scan (WCAG 2.1 AA)
- **Symptom:** axe-core scan found 2 violations with impact "critical" or "serious".
- **Expected:** 0 violations
- **Received:** 2 violations
- **Evidence:** test-results/accessibility-Part-8-—-Acc-86ce8-ssibility-scan-WCAG-2-1-AA--chromium/trace.zip
- **Recommendation:** Run detailed axe-core report to identify specific violations (likely form label association and color contrast).

### BUG-AUTH-003 — RegisterPage: 1 WCAG critical/serious violation
- **Severity:** High
- **Browser:** Chromium
- **Test:** Part 8 — Accessibility Validation > RegisterPage accessibility scan (WCAG 2.1 AA)
- **Symptom:** axe-core scan found 1 violation with impact "critical" or "serious".
- **Expected:** 0 violations
- **Received:** 1 violation
- **Evidence:** test-results/accessibility-Part-8-—-Acc-04492-ssibility-scan-WCAG-2-1-AA--chromium/trace.zip
- **Recommendation:** Run detailed axe-core report to identify specific violation.

### BUG-AUTH-004 — Keyboard Tab order: checkbox not focusable
- **Severity:** High
- **Browser:** Chromium
- **Test:** Part 8 — Accessibility Validation > Semantic HTML and keyboard navigation on login inputs
- **Symptom:** After tabbing out of identifier input, the first checkbox is not focused. Expected focus, received "inactive" even though aria-disabled="false".
- **Expected:** Checkbox should receive keyboard focus
- **Received:** Resolved to "inactive"
- **Evidence:** test-results/accessibility-Part-8-—-Acc-6239c--navigation-on-login-inputs-chromium/trace.zip
- **Recommendation:** Check tabindex order and ensure checkbox is reachable via keyboard navigation.

### BUG-AUTH-005 — Unauthorized workspace access prevention test fails
- **Severity:** High
- **Browser:** Chromium
- **Test:** Part 10 — Security Validation > Prevent unauthorized workspace access through state manipulation
- **Symptom:** `page.selectOption('select[aria-label="Switch review role"]', 'guest')` times out — the role switcher select element is not found.
- **Expected:** Role switcher should be present to allow role switching
- **Received:** Element not found (timeout 15s)
- **Evidence:** test-results/security-validation-Part-1-00685--through-state-manipulation-chromium/trace.zip
- **Recommendation:** Verify that the role switcher (`select[aria-label="Switch review role"]`) is rendered in the dev build. May need to be added to App.tsx or context.ts for mock/qa mode.

---

## Medium

### BUG-AUTH-006 — OTP input fill timeout on Mobile Safari
- **Severity:** Medium
- **Browser:** Mobile Safari
- **Test:** OTP verification (both fail and success scenarios)
- **Symptom:** `.fill()` on individual `.sk-otp-input` elements occasionally times out. Resolves on retry.
- **Expected:** OTP inputs should be fillable
- **Received:** Timeout waiting for input to be visible, enabled, and editable
- **Evidence:** test-results/auth-validation-Part-1-*mobile-safari*/trace.zip
- **Recommendation:** Add `{ force: true }` to fill actions or increase wait time. Consider using a single combined OTP input field approach.

### BUG-AUTH-007 — OTP input fill timeout on WebKit
- **Severity:** Medium
- **Browser:** WebKit
- **Test:** OTP verification fail (000000)
- **Symptom:** Same as BUG-AUTH-006 but affects WebKit desktop as well (1 occurrence).
- **Evidence:** test-results/auth-validation-Part-1-*webkit*/trace.zip

### BUG-AUTH-008 — Dashboard networkidle timeout
- **Severity:** Medium
- **Browser:** Chromium
- **Test:** Part 2 — Session Management > Customer dashboard route is accessible
- **Symptom:** `waitForLoadState('networkidle')` times out at 15s on /dashboard.
- **Expected:** Dashboard should load
- **Received:** Timeout
- **Evidence:** test-results/session-management-Part-2--2c524-shboard-route-is-accessible-chromium/trace.zip
- **Recommendation:** Check if dashboard has polling or WebSocket connections. Use `load` or `domcontentloaded` instead of `networkidle`.

---

## Low

### BUG-AUTH-009 — ENOENT artifact cleanup failures
- **Severity:** Low
- **Browser:** Chromium
- **Tests:** 3 instances (Back button, verify-otp redirect, window resize)
- **Symptom:** "ENOENT: no such file or directory, unlink '...zip'" from artifact cleanup.
- **Expected:** Clean artifact cleanup
- **Received:** File not found during cleanup attempt
- **Recommendation:** Likely a race condition in Playwright's artifact collector when video/trace files are closed before cleanup. Can be ignored but may indicate concurrent test worker resource contention.
