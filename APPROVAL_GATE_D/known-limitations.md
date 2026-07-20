# Approval Gate D — Known Limitations

**Date:** 2026-07-18

## Classification

### A. Limitations that DO NOT block RC1 (ship as-is)
| Limitation | Type | Rationale |
|------------|------|-----------|
| Playwright spec/architecture mismatch (25 specs) | Test defect | App correct; specs stale. Reconcile post-gate. |
| Firefox/WebKit local launch hang | Environment | Not an app defect; CI runs them. |
| Mock auth provider (no real IdP) | Known limitation | Per DEF-001; demo-grade acceptable for RC1. |
| `dist/` + `target/` untracked | Hygiene | Build regenerates; gitignore to confirm. |
| Uncommitted WIP on branch | Process | Pre-existing; commit/stash before merge. |

### B. Limitations that CAN SHIP but track to v1.1
| Limitation | Type |
|------------|------|
| Product Detail Pages (`/product/:slug`) | Feature gap |
| Real authentication provider integration | Feature |
| Performance enhancements beyond budget | Stretch |
| Additional cross-browser hardening (Safari specifics) | Stretch |

### C. Limitations that MUST BLOCK RC1
**None.** No production defect, no Critical/High/Medium bug, no security gap blocks
RC1. (If any of the §A items were production-facing they would block — they are not.)

## Explicit Determination (per Gate brief §10)
- Playwright spec reconciliation → ships with condition (reconcile, non-blocking for RC1 build).
- CI browser execution → runs in capable CI; results attach post-gate.
- Future Product Detail Pages → v1.1.
- Performance enhancements / stretch goals → v1.1.
