# Remaining Recommendations

**Pre-QA Sprint 2** | **Date:** 2026-07-17

---

## Completed Items

- [x] Playwright verified installed
- [x] WebKit browser installed
- [x] Global setup/teardown created
- [x] Project structure created (fixtures, helpers, page-objects, mock-data)
- [x] Mock data files created (personas, products, orders, addresses, training, notifications)
- [x] Helper modules created (auth, navigation, assertions, API mock)
- [x] LoginPage page object created
- [x] Environment utility created
- [x] Playwright config updated (5 browser projects)
- [x] Smoke test created and passed (6/6)
- [x] All infrastructure reports generated

## Recommended (Not Blocking)

### P2 — Before full QA Sprint 2 execution

| # | Recommendation | Effort | Reason |
|---|---------------|--------|--------|
| 1 | Create CI workflow (GitHub Actions) | 2 days | Automated regression on push |
| 2 | Add axe-core integration to a11y tests | 1 day | Automated WCAG scanning |
| 3 | Set up Lighthouse CI thresholds | 1 day | Performance budget enforcement |
| 4 | Migrate existing test specs to use shared helpers | 3 days | Eliminate hardcoded values, use fixtures |
| 5 | Add test retry categorization (flake vs failure) | 1 day | Improve CI reliability |
| 6 | Review .env.production.example for realistic keys | 30 min | Security best practice |

### P3 — Future sprints

| # | Recommendation | Effort | Reason |
|---|---------------|--------|--------|
| 7 | Configure mobile app node_modules | 30 min | Enable native mobile tests |
| 8 | Set up visual regression testing | 2 days | Catch UI drift |
| 9 | Create API contract tests | 3 days | Validate backend schemas |
| 10 | Add test data factories (faker.js) | 2 days | Dynamic test data generation |

## Recommendation

QA Sprint 2 can proceed immediately. The above items are enhancements, not blockers.
