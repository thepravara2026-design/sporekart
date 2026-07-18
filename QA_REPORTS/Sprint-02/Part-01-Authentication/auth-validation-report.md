# SporeKart QA Sprint 2 — Part 1: Authentication Validation Report

**Report Date:** 2026-07-17  
**Release Candidate:** v1.0.0-rc1  
**Execution Mode:** Mock (no production database, Razorpay, OTP, email, SMS, or shipping)  
**Test Framework:** Playwright 1.61.1  
**Base URL:** http://localhost:5174

---

## 1. Executive Summary

| Metric | Value |
|--------|-------|
| Total Tests | 131 |
| Passed | 113 (86.3%) |
| Failed (hard) | 7 (5.3%) |
| Flaky (passed on retry) | 11 (8.4%) |
| Browsers Tested | 5 (chromium, firefox, webkit, mobile-chrome, mobile-safari) |
| Screenshots Captured | 40 |
| Videos Captured | 54 |
| Traces Captured | 52 |

## 2. Part-by-Part Breakdown

### Part 1 — Auth Validation (11 tests × 5 projects = 55 runs)
| Browser | Passed | Failed | Flaky | Rate |
|---------|--------|--------|-------|------|
| Chromium | 10 | 0 | 0 | 100% |
| Firefox | 0 | 11 | 0 | 0% |
| WebKit | 10 | 0 | 1 | 90.9% |
| Mobile Chrome | 11 | 0 | 0 | 100% |
| Mobile Safari | 9 | 0 | 2 | 81.8% |

**Key Findings:**
- Firefox: Complete failure — all tests time out at 30s. Likely mock API response issue specific to Gecko engine.
- Mobile Safari: OTP input `.fill()` occasionally times out on individual OTP digit inputs (flaky).

### Part 2 — Session Management (23 tests × 2 projects = 46 runs)
| Browser | Passed | Failed | Flaky | Rate |
|---------|--------|--------|-------|------|
| Chromium | 19 | 0 | 4 | 82.6% |
| WebKit | 21 | 0 | 0 | 100% |

**Key Findings:**
- Chromium: 4 flaky tests — 3 ENOENT artifact file cleanup issues, 1 dashboard `networkidle` timeout.
- Full login flow (Login → OTP → AuthLoading → Home) passes on both browsers.
- localStorage/sessionStorage: No auth secrets stored. No cookies set by the app.
- Session persists across refresh. Hard refresh lands on home.

### Part 3 — RBAC/Authorization (19 tests on Chromium)
| Passed | Failed | Rate |
|--------|--------|------|
| 19 | 0 | 100% |

**Key Findings:**
- All 9 roles render correct workspaces in sidebar.
- Guest → admin dashboard blocked correctly.
- Customer → order management queue blocked correctly.
- Administrator can navigate to all protected routes.
- Role persists across SPA navigation and browser refresh.
- No role/permission data stored in localStorage or sessionStorage.
- Sidebar responsive on mobile viewport.

### Part 8 — Accessibility (3 tests on Chromium)
| Passed | Failed | Rate |
|--------|--------|------|
| 0 | 3 | 0% |

**Violations Found:**
- **LoginPage:** 2 critical/serious WCAG 2.1 AA violations (form labels, color contrast)
- **RegisterPage:** 1 critical/serious WCAG 2.1 AA violation
- **Keyboard navigation:** Tab order does not reach checkbox as expected (checkbox is aria-disabled="false" but remains inactive)

### Part 9 — Performance (2 tests on Chromium)
| Passed | Failed | Rate |
|--------|--------|------|
| 2 | 0 | 100% |

**Findings:**
- Page loads complete within performance threshold.
- No failed network requests or broken assets.

### Part 10 — Security Validation (2 tests on Chromium)
| Passed | Failed | Rate |
|--------|--------|------|
| 1 | 1 | 50% |

**Findings:**
- ✅ Mock environment variables isolation — PASS
- ✅ Client-side session storage does not expose sensitive keys — PASS
- ❌ Prevent unauthorized workspace access through state manipulation — FAIL (role switcher select not found, likely not rendered in dev build)

---

## 3. Cross-Browser Summary

| Feature | Chromium | Firefox | WebKit | Mobile Chrome | Mobile Safari |
|---------|----------|---------|--------|---------------|---------------|
| Channel switching | ✅ | ❌ Timeout | ✅ | ✅ | ✅ |
| Empty validation | ✅ | ❌ Timeout | ✅ | ✅ | ✅ |
| Invalid format validation | ✅ | ❌ Timeout | ✅ | ✅ | ✅ |
| Terms gate | ✅ | ❌ Timeout | ✅ | ✅ | ✅ |
| Happy path login → OTP | ✅ | ❌ Timeout | ✅ | ✅ | ✅ |
| OTP 000000 fail | ✅ | ❌ Timeout | ✅(flaky) | ✅ | ✅(flaky) |
| OTP success → session | ✅ | ❌ Timeout | ✅ | ✅ | ✅(flaky) |
| Resend OTP cooldown | ✅ | ❌ Timeout | ✅ | ✅ | ✅ |
| Register → OTP | ✅ | ❌ Timeout | ✅ | ✅ | ✅ |
| Forgot password | ✅ | ❌ Timeout | ✅ | ✅ | ✅ |
| Social sign-in buttons | ✅ | ❌ Timeout | ✅ | ✅ | ✅ |

## 4. Bug Count by Severity

| Severity | Count | Area |
|----------|-------|------|
| Critical | 1 | Firefox: Complete test failure on all auth operations |
| High | 3 | Accessibility: WS violations (2), Tab order (1) |
| High | 1 | Security: Unauthorized access prevention test fails |
| Medium | 4 | Flaky OTP tests on Mobile Safari, WebKit |
| Low | 4 | Flaky artifact cleanup on Chromium |

## 5. Risks & Recommendations

1. **Firefox Blocking:** Mock API routes may not be intercepted in Firefox. Investigate `page.route()` compatibility with Gecko or add Firefox-specific route handling.
2. **OTP Input Flakiness:** `.fill()` on individual OTP input cells is timing-sensitive on iOS Safari and WebKit. Consider using `page.locator.fill()` on the parent container or `page.type()`.
3. **Accessibility Debt:** 3 WCAG violations on core authentication pages — should be resolved before GA.
4. **Dashboard `networkidle`:** The dashboard page may have polling or WebSocket connections that never resolve `networkidle`. Use `load` or `domcontentloaded` instead.

## 6. Evidence

All screenshots, videos, and traces are stored in `shared-testing/test-results/`.
Total: 40 screenshots, 54 videos, 52 traces.
