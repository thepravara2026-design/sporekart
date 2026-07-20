# QA Sprint 2 — Part 1: Authentication Validation Report

## Executive Summary

| Metric | Value |
|---|---|
| **Total Tests** | 11 |
| **Passed** | 11 |
| **Failed** | 0 |
| **Pass Rate** | **100%** |
| **Blocked** | 0 |
| **Skipped** | 0 |
| **Duration** | 12.1s |
| **Browser** | Chromium (Desktop) |
| **Viewport** | 1280x720 |
| **Date** | 2026-07-17 |
| **Branch** | qa/qa-sprint-2 |
| **Mode** | Mock (localhost:5173) |

## Test Results

| # | Test Case | Status | Duration | Defects |
|---|-----------|--------|----------|---------|
| 1 | Switching channels updates form input labels | ✅ PASS | 2.6s | — |
| 2 | Validation alerts displayed for empty identifiers | ✅ PASS | 2.4s | — |
| 3 | Validation alerts displayed for invalid formats | ✅ PASS | 2.8s | — |
| 4 | Terms agreement gate blocks form submission | ✅ PASS | 2.2s | BUG-001 |
| 5 | Happy path login redirects to OTP verification page | ✅ PASS | 3.4s | — |
| 6 | OTP verification fails deterministic code 000000 | ✅ PASS | 6.0s | — |
| 7 | OTP verification success redirects and establishes session | ✅ PASS | 4.8s | — |
| 8 | Resend OTP cooldown timer and resend action | ✅ PASS | 3.0s | — |
| 9 | RegisterPage submits and transitions to OTP | ✅ PASS | 3.5s | — |
| 10 | ForgotPasswordPage submits and shows success state | ✅ PASS | 3.4s | — |
| 11 | Social sign in buttons return unenabled warning | ✅ PASS | 2.8s | — |

## Defects Found & Fixed

### BUG-001 (CRITICAL): React `style` prop receives CSS string instead of object

**Status**: FIXED

**Severity**: Critical
**Priority**: P0

**Description**: Four design-system components (`Input.tsx`, `Checkbox.tsx`, `OtpInput.tsx`, `RadioGroup.tsx`) defined inline CSS styles as template literal strings and cast them with `as React.CSSProperties`. React 18.3+ throws an error for non-object `style` values, crashing the entire render tree.

**Impact**: All auth pages (Login, Register, Verify OTP, Forgot Password, etc.) failed to render — body was empty with 0 children in `#root`. Authentication workflows were completely blocked.

**Root Cause**: CSS string literals like:
```typescript
const wrapperStyles = `display: inline-flex; ...` as React.CSSProperties;
```
used in:
```tsx
<div style={wrapperStyles as React.CSSProperties}>
```
The `as React.CSSProperties` cast is a TypeScript-only assertion — at runtime the value remains a plain string, which React 18.3 rejects.

**Fix**: Converted all CSS string template literals to proper `React.CSSProperties` objects:
```typescript
const wrapperStyles: React.CSSProperties = {
  display: 'inline-flex',
  flexDirection: 'column',
  ...
};
```

**Files Modified**:
- `frontend/web-app/src/design-system/components/core/Input.tsx`
- `frontend/web-app/src/design-system/components/core/Checkbox.tsx`
- `frontend/web-app/src/design-system/components/core/OtpInput.tsx`
- `frontend/web-app/src/design-system/components/core/RadioGroup.tsx`

### BUG-002 (HIGH): Terms agreement error message not rendered

**Status**: FIXED

**Severity**: High
**Priority**: P1

**Description**: In `LoginPage.tsx`, the `termsError` state variable was set when the user submitted without accepting terms, but was never rendered as visible text. The Checkbox received `error={!!termsError}` which only displayed a red border — no error message.

**Fix**: Added `<AuthAlert type="error">{termsError}</AuthAlert>` to render the error message when present.

**File Modified**:
- `frontend/web-app/src/features/auth/pages/LoginPage.tsx`

### BUG-003 (MEDIUM): Test selectors mismatched with component implementation

**Status**: FIXED

**Description**: Three Playwright test selectors did not match actual application behavior/rendering:
1. OTP success test expected manual button click, but OTP auto-submits on complete via `onComplete` callback
2. Register page used `input[name="fullName"]` but `<Input>` component does not receive a `name` prop
3. Terms error not rendered (covered by BUG-002)

**File Modified**:
- `shared-testing/tests/auth-validation.spec.ts`

## Coverage Analysis

| Auth Feature | Coverage | Status |
|---|---|---|
| Login page render | 100% | ✅ |
| Channel switching (Phone/Email) | 100% | ✅ |
| Input validation (empty/invalid) | 100% | ✅ |
| Terms & Privacy gate | 100% | ✅ |
| Happy path login → OTP | 100% | ✅ |
| OTP failure (000000) | 100% | ✅ |
| OTP success → session | 100% | ✅ |
| Resend OTP cooldown | 100% | ✅ |
| Registration flow | 100% | ✅ |
| Forgot password flow | 100% | ✅ |
| Social login disabled state | 100% | ✅ |

## Evidence

| Type | Location |
|---|---|
| Screenshots | `QA_REPORTS/Sprint-02/Screenshots/` |
| Videos | `QA_REPORTS/Sprint-02/Videos/` |
| Traces | `QA_REPORTS/Sprint-02/Traces/` |
| Logs | `QA_REPORTS/Sprint-02/Logs/` |
| HTML Report | `QA_REPORTS/Sprint-02/Playwright/html-report/` |

## Recommendations

1. **Design System Audit**: Audit all remaining design-system components (`Password.tsx`, `Search.tsx`, `ToggleSwitch.tsx`, `Avatar.tsx`, `FloatingActionButton.tsx`, `Button.tsx`) for the same `style` prop string pattern. These may cause crashes when their respective pages are rendered.
2. **Add `name` prop support**: Consider adding `name` prop to `<Input>` component for better form accessibility and testability.
3. **Error boundary**: Add a React error boundary at the app root to prevent a single component crash from taking down the entire page.

## Sign-off

**Part 1 — Authentication Validation**: ✅ COMPLETE

**Pass Rate**: 100% (11/11)

**Next**: Proceed to Part 2 — Session Management
