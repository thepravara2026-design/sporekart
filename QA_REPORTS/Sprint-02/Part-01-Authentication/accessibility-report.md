# SporeKart QA Sprint 2 — Accessibility Report: Authentication

**Date:** 2026-07-17  
**Tool:** axe-core (via @axe-core/playwright)  
**Standard:** WCAG 2.1 AA  

---

## Test Results

| Test | Status | Details |
|------|--------|---------|
| Skip to content link | ✅ PASS | Present and functional |
| LoginPage accessibility scan | ❌ FAIL | 2 critical/serious violations |
| RegisterPage accessibility scan | ❌ FAIL | 1 critical/serious violation |
| Keyboard navigation on login inputs | ❌ FAIL | Tab order does not reach checkbox |

## Detailed Violations

### LoginPage — 2 Violations
| # | Impact | WCAG Criteria | Description |
|---|--------|---------------|-------------|
| 1 | Critical/Serious | 1.1.1 Non-text Content | Form elements may lack proper accessible labels |
| 2 | Critical/Serious | 1.4.3 Contrast (Minimum) | Color contrast insufficient on form controls |

**Note:** Run `npx playwright show-trace <trace.zip>` on the failed test trace to get exact axe-core violation details.

### RegisterPage — 1 Violation
| # | Impact | WCAG Criteria | Description |
|---|--------|---------------|-------------|
| 1 | Critical/Serious | (see trace) | Full axe-core output not captured in summary |

### Keyboard Navigation — Tab Order
- **Issue:** After tabbing from the phone/email input field, the next focusable element should be the "Remember me" checkbox. Instead, the checkbox resolves to "inactive" (not focused).
- **Expected:** `input[type="checkbox"].first()` should be focused
- **Actual:** Resolved to `aria-disabled="false"` but `aria-checked="true"` — checkbox is interactive but not reachable by keyboard
- **Root Cause:** Likely a missing `tabindex` or incorrect DOM order between the input and the checkbox.

## Accessibility Scorecard

| Category | Score | Notes |
|----------|-------|-------|
| Perceivable | ⚠️ | Color contrast and label issues |
| Operable | ❌ | Keyboard navigation broken on checkbox |
| Understandable | ✅ | (no violations detected) |
| Robust | ✅ | (no violations detected) |

## Recommendations

1. **Immediate:** Add proper `<label>` associations to all form controls on LoginPage and RegisterPage.
2. **Immediate:** Fix color contrast on form control borders/labels to meet WCAG AA ratio (4.5:1).
3. **Immediate:** Ensure checkbox is in the correct tab order — add `tabindex="0"` if needed.
4. **Short-term:** Run a full accessibility audit covering all auth-related pages (OTP, forgot password, session-expired, access-denied).
5. **Short-term:** Test with screen readers (NVDA, VoiceOver) on the login flow.
