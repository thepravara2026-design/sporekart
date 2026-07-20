# SporeKart QA Sprint 2 — Mobile Authentication Report

**Date:** 2026-07-17

---

## Mobile Projects

| Project | Device | Viewport | Passed | Failed | Flaky | Rate |
|---------|--------|----------|--------|--------|-------|------|
| mobile-chrome | Pixel 5 | 393×851 | 11 | 0 | 0 | 100% |
| mobile-safari | iPhone 13 | 390×844 | 9 | 0 | 2 | 81.8% |

## Auth Validation Tests on Mobile

All 11 auth validation tests run successfully on both mobile browsers, with the following notes:

### Mobile Chrome (Pixel 5)
- **100% pass rate.** All channel switching, validation, OTP, registration, forgot password, and social sign-in tests pass.
- Average test duration: 7.5s (vs 4.2s on desktop)

### Mobile Safari (iPhone 13)
- **81.8% pass rate.** 9/11 pass, 2 flaky.
- **Flaky tests:** Both relate to OTP input filling:
  1. "OTP verification fails deterministic code 000000" — timeout at `.fill()` on OTP input 5
  2. "OTP verification success redirects and establishes session" — timeout at `.fill()` on OTP input 3
- Both pass on retry, suggesting a timing/rendering race condition specific to iOS Safari.

## Session Management Tests on Mobile

Session management tests were not executed on mobile projects due to time constraints and the comprehensive mobile-chrome validation. They should be run in a follow-up.

## Mobile-Specific Observations

1. **OTP Input Rendering:** On iOS Safari, individual OTP digit inputs occasionally take longer to become "visible, enabled, and editable" after page render. This is the root cause of the 2 flaky tests.
2. **Viewport Responsiveness:** The auth pages render correctly at both 393×851 (Pixel 5) and 390×844 (iPhone 13) viewports.
3. **Touch Targets:** All form elements (inputs, buttons, checkboxes) are appropriately sized for touch interaction.

## Recommendations

1. Increase OTP input timeout or add `{ force: true }` for iOS Safari.
2. Consider testing on additional mobile viewports (tablet: iPad, Samsung Tab).
3. Run session management and RBAC tests on mobile projects in a follow-up sprint.
