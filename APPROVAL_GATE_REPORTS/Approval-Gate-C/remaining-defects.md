# Approval Gate C — Remaining Defects

## Sprint C Backlog — Incomplete Items

| ID | Issue | Type | Priority | Owner |
|----|-------|------|----------|-------|
| **C-001** | `mountedRef` is declared but never used as a guard — no actual fix for double file inputs | Ineffective fix | P2 | Engineering |
| **C-006** | `sendOtp()`, `verifyOtp()`, `register()` remain stub implementations | Partial fix | P2 | Engineering |

## Observations (Non-Blocking)

| ID | Issue | Severity | Recommendation |
|----|-------|----------|----------------|
| O-001 | 404 page uses `role="alert"` instead of `role="status"` | Minor | Change to `role="status"` for persistent content |
| O-002 | No focus trap on mobile sidebar when open | Minor | Consider adding focus trap for WCAG 2.1.2 compliance |
| O-003 | Lighthouse CI `numberOfRuns: 1` reduces reliability | Minor | Increase to 3 runs for consistent results |
| O-004 | AuthStore header comment removed — partial stubs may confuse devs | Minor | Restore comment noting remaining stub methods |

## Previously Documented (Pre-Sprint C)

| ID | Description | Status |
|----|-------------|--------|
| BUG-S3-CRIT-001 | Production build crash (React #62 + CSSStyleDeclaration) | ✅ **RESOLVED** — build now passes |
| BUG-S3-HIGH-004 | No semantic ARIA landmarks | ✅ **RESOLVED** — landmarks existed; test added |
| BUG-SEC-005 | Role switcher exposed to authenticated users | ⏳ Fixed in earlier sprint |
| BUG-SEC-011 | Any OTP code accepted | ⏳ Fixed in earlier sprint |

## Summary
- **P0/P1 defects remaining**: 0
- **P2 defects remaining**: 2 (C-001 ineffective, C-006 partial)
- **Observations**: 4 (non-blocking)
- **Critical build crash**: RESOLVED
