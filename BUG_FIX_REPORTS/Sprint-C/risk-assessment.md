# Risk Assessment — Sprint C

## Risk Scoring Methodology
- **L** = Likelihood (1-5), **I** = Impact (1-5), **Score** = L × I (1-25)
- **Implementation Risk**: Risk that the fix is complex or introduces new issues
- **Regression Risk**: Risk that the fix breaks existing functionality
- **Security Risk**: Security implications of the fix or delay

## Item-Level Risk

| ID | Title | Impl Risk | Reg Risk | Sec Risk | Overall | Mitigation |
|----|-------|-----------|----------|----------|---------|------------|
| BUG-C-001 | Avatar double upload | 2 | 2 | 1 | **5** | Simple DOM fix; remove duplicate input |
| BUG-C-002 | Save button disabled | 2 | 3 | 1 | **6** | Fix loading state toggle; verify with Playwright |
| BUG-C-003 | ARIA landmarks | 1 | 2 | 1 | **4** | Semantic HTML change; verify with aXe |
| BUG-C-004 | Mobile nav empty | 3 | 3 | 1 | **7** | Ensure nav items mount on toggle; test all viewports |
| BUG-C-005 | Service Worker | 4 | 3 | 2 | **9** | Use Workbox; test in incognito; verify cache invalidation |
| BUG-C-006 | Auth client stub | 5 | 5 | 5 | **15** | High risk — touches auth; requires extensive testing |
| BUG-C-007 | Duplicate toasts | 3 | 4 | 1 | **8** | Consolidate to one provider; verify all toast consumers |
| BUG-C-008 | No 404 page | 1 | 1 | 1 | **3** | Router catch-all; lowest risk item |
| BUG-C-009 | Perf budgets | 1 | 1 | 1 | **3** | CI config only; zero production risk |

## Risk Categorization

| Risk Level | Score Range | Items | Action |
|------------|-------------|-------|--------|
| **Low** | 1-5 | BUG-C-001, BUG-C-003, BUG-C-008, BUG-C-009 | Proceed; standard review |
| **Medium** | 6-10 | BUG-C-002, BUG-C-004, BUG-C-005, BUG-C-007 | Proceed with caution; add QA verification |
| **High** | 11-15 | BUG-C-006 | Requires architecture review; pair programming |

## Risk by Wave

| Wave | Low | Medium | High | Notes |
|------|-----|--------|------|-------|
| Wave 1 | 2 (C-001, C-003) | 3 (C-002, C-004, C-007) | 0 | Safe; majority low-medium |
| Wave 2 | 1 (C-008) | 1 (C-005) | 1 (C-006) | Auth is the highest-risk item |
| Wave 3 | 1 (C-009) | 0 | 0 | Stretch goal; zero risk |

## Top 3 Risks

| Rank | Risk | Score | Mitigation |
|------|------|-------|------------|
| 1 | BUG-C-006 (Auth stub → real auth) | 15 | Implement in Wave 2; pair with senior backend engineer; extensive Playwright test coverage |
| 2 | BUG-C-005 (Service Worker) | 9 | Use Workbox recipe; test cache-first vs network-first strategies; verify offline navigation |
| 3 | BUG-C-007 (Duplicate toasts) | 8 | Deep audit of all toast consumers; verify no breaking changes |

## Overall Sprint C Risk

| Dimension | Rating | Notes |
|-----------|--------|-------|
| **Implementation Risk** | **Medium** | Majority of items are low-medium risk; auth is the outlier |
| **Regression Risk** | **Medium-Low** | Most items are additive or isolated |
| **Security Risk** | **Low** (except C-006) | Auth refactor is medium-high security risk |
| **Business Risk** | **Low** | No revenue-critical features affected |
| **Schedule Risk** | **Medium** | Auth and SW may slip; Wave 3 is a stretch goal |
