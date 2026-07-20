# Artifact Classification — Regression Sprint B Repository Hygiene

**Date:** 2026-07-18
**Branch:** `bugfix/sprint-b-high-priority`

Every item present in the working tree before hygiene was classified into one of five categories.
Decision rules:
- **Production Source** — code that belongs to the product/test suite deliverables of Sprint A/B.
- **QA Evidence** — test results, reports, dashboards, and release-governance documents.
- **Generated Artifact** — machine-produced output (node_modules, reports dirs) excluded by `.gitignore`.
- **Temporary File** — scratch/throwaway runtime output (logs, caches) not needed for the record.
- **Debug File** — process stdout/stderr capture from a dev run.

## 1. Production Source (committed → `4ee6fc5`)
`shared-testing/` Playwright test foundation — 49 files:
- `global-setup.ts`, `global-teardown.ts`
- `helpers/` (api-mock, assertions, auth, navigation)
- `mock-data/` (addresses, notifications, orders, personas, products, training)
- `page-objects/LoginPage.ts`
- `tests/` — 37 spec files (accessibility, admin-console, auth-apis, auth-validation, checkout-*, cross-browser, customer-journey-*, mobile-responsive, notification-platform, order-lifecycle, payment-validation, performance*, protected-routes, rbac-authorization, regression, security-validation, session-management, smoke, training-platform, ux-accessibility-validation, **debug-login**)
- `utils/env.ts`

> Note: `debug-login.spec.ts` is named "debug" but is legitimate, committed test source — retained.

## 2. QA Evidence / Release Documentation (committed → `8a572ac`)
- `BUG_FIX_REPORTS/Approval-Gate-Sprint-A/` — 11 files (approval-gate, scorecards, audits)
- `BUG_FIX_REPORTS/Approval-Gate-Sprint-B/` — 14 files (approval-gate, hygiene, audits, decision)
- `BUG_FIX_REPORTS/Sprint-A/` — 10 files (bug-fix reports)
- `QA_REPORTS/Infrastructure/` — 11 files
- `QA_REPORTS/Sprint-01/Playwright/junit.xml`, `results.json` — 2 files
- `QA_REPORTS/Sprint-02/` — 9,410 files (Playwright results, part reports, artifacts, dashboards, traces, videos, screenshots)

All treated as **release evidence** and preserved, not deleted.

## 3. Generated Artifact (excluded by `.gitignore`, never committed — correct)
- `shared-testing/node_modules/` (4,689 .js) — `node_modules/` ignored.
- `shared-testing/playwright-report/` — explicitly ignored in `.gitignore`.
- `shared-testing/test-results/` — explicitly ignored in `.gitignore`.
- `*.tsbuildinfo`, `*.log` — ignored.

These do not appear in `git status` and were correctly left out of the repository.

## 4. Temporary / Debug File (removed)
- `shared-testing/webapp-dev.err` — runtime stderr capture → **removed**
- `shared-testing/webapp-dev.out` — runtime stdout capture → **removed**

Neither contained source or evidence; safe to discard.

## 5. Tracked QA Evidence Restored (no longer modified)
- `QA_REPORTS/Sprint-01/Playwright/html-report/` — 48 deleted + 1 modified file were locally altered and restored via `git checkout` so the tracked tree is clean. These are prior-sprint committed evidence.

## 6. Classification Totals
| Category | Count | Disposition |
|----------|-------|-------------|
| Production Source | 49 | Committed (`4ee6fc5`) |
| QA Evidence / Release Doc | 9,458 | Committed (`8a572ac`) |
| Generated Artifact | n/a (gitignored) | Excluded (correct) |
| Temporary / Debug | 2 | Removed |
| Restored tracked evidence | 49 | Restored, clean |

---
*End of Artifact Classification.*
