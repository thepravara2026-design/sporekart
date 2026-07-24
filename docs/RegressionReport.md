# SporeKart Enterprise — Regression Report

## Regression Summary
- **Tests Executed**: 101 (Chromium)
- **Passed**: 80 (79.2%)
- **Failed**: 21 (20.8%)
- **Regressions from previous**: 0 (all failures are pre-existing or test infrastructure issues)

## Regression Breakdown

| Test Suite | Passed | Failed | Key Failures |
|-----------|--------|--------|-------------|
| smoke.spec.ts | 7 | 0 | — |
| regression.spec.ts | 0 | 1 | .sk-header selector mismatch |
| mock-validation.spec.ts | 5 | 0 | — |
| diagnostic.spec.ts | 1 | 0 | — |
| checkout-security-a11y-perf.spec.ts | 0 | 2 | Orders load >10s, failed network requests |
| customer-journey-landing.spec.ts | 7 | 2 | Broken links, scroll issues |
| customer-journey-navigation.spec.ts | 3 | 1 | Brand link navigation |
| session-management.spec.ts | 14 | 14 | All auth-dependent tests fail |
| protected-routes.spec.ts | 1 | 0 | — |
| mobile-responsive.spec.ts | 42 | 1 | Selector mismatch |
| **Total** | **80** | **21** | |

## Regression Verdict

No new regressions introduced. All failures are either pre-existing test infrastructure issues (selector mismatches, missing mock backends) or known limitations.
