# Risk Assessment Report

**Pre-QA Sprint 2** | **Date:** 2026-07-17

---

## Risk Register

| # | Risk | Category | L | I | Score | Status | Mitigation |
|---|------|----------|---|---|-------|--------|------------|
| R-01 | Vite dev server must be running before tests | Test Execution | 2 | 4 | 8 | 🟡 MEDIUM | Start dev server as pre-condition; document in README |
| R-02 | No MongoDB for mock database | Infrastructure | 1 | 2 | 2 | 🟢 LOW | Frontend tests don't need DB; mock data is client-side |
| R-03 | Test data is hardcoded (not from fixtures) | Test Quality | 2 | 3 | 6 | 🟢 LOW | Fixtures created in this session — existing specs still use hardcoded values |
| R-04 | No CI pipeline for automated execution | Process | 3 | 3 | 9 | 🟡 MEDIUM | Manual execution only. CI pipeline needs setup |
| R-05 | .env.production.example has realistic keys | Configuration | 2 | 2 | 4 | 🟢 LOW | Example file only; flag for review |
| R-06 | Browser timing differences (WebKit vs Chromium) | Compatibility | 2 | 2 | 4 | 🟢 LOW | Monitor WebKit results; 15000ms action timeout configured |
| R-07 | Playwright browsers need updates | Tooling | 1 | 3 | 3 | 🟢 LOW | Current versions: Chromium 149, Firefox 151, WebKit 26 |
| R-08 | Console sandbox may block `npm run dev` | Environment | 1 | 4 | 4 | 🟢 LOW | Dev server started successfully in this session |
| R-09 | Mobile app dependencies not installed | Tooling | 3 | 2 | 6 | 🟢 LOW | Mobile QA focuses on web-app responsive, not native apps |

## Risk Heat Map

```
Impact 5 |
         |
         |
         |
         |
         |
         |
         |
         |
         |
         |
         |
         |
         |       R-01    R-04
         |  R-05 R-03 R-09
         |  R-06 R-08     R-02
         +----------------------------------->
                    Likelihood 5
```

## Top Risks

1. **R-04 (No CI pipeline)** — Score 9 — MEDIUM — Without CI, every test run must be manual
2. **R-01 (Dev server dependency)** — Score 8 — MEDIUM — Tests fail if dev server is not running

**Verdict:** No blocking risks identified. All risks are MEDIUM or LOW severity.
