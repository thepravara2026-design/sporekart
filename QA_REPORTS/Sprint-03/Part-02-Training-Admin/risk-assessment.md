# Risk Assessment — QA Sprint 3 Part 2

## Methodology
Risks scored on Likelihood (1–5) × Impact (1–5) = Risk Score (1–25). P0–P5 priority mapping.

## Risk Register

| # | Risk Description                                      | L | I | Score | Priority | Mitigation |
|---|-------------------------------------------------------|---|---|-------|----------|------------|
| R1 | Production build crash not resolved before RC         | 4 | 5 | **20** | **P0** | Assign senior front-end engineer immediately; isolate infinite re-render cause |
| R2 | Role switcher not implemented before RBAC validation   | 3 | 4 | **12** | **P1** | Implement auth mock or role switcher component |
| R3 | Undiscovered bugs hidden beneath the build crash       | 4 | 3 | **12** | **P1** | Prioritize build fix to unblock all functional testing |
| R4 | Component library architecture causes repeated crashes | 3 | 4 | **12** | **P1** | Review component dependency graph; add integration tests |
| R5 | Route guard logic has security gaps (no real testing)  | 2 | 5 | **10** | **P2** | Auth middleware audit after build fix |
| R6 | Performance baseline is inflated (crash overhead)      | 2 | 2 | **4**  | **P3** | Re-baseline after fix; compare with initial sweep |
| R7 | Accessibility issues hidden by ErrorBoundary           | 3 | 3 | **9**  | **P2** | WCAG audit after build fix |
| R8 | Test suite maintenance burden (221 tests already)       | 2 | 2 | **4**  | **P3** | Refactor shared test utilities after MVP |
| R9 | Skipped viewports (Firefox, WebKit) may reveal bugs    | 2 | 3 | **6**  | **P2** | Enable cross-browser testing in RC phase |
| R10 | Missing 404 handling confuses users                   | 2 | 2 | **4**  | **P3** | Implement app-level 404 page |

## Top 3 Risks

| Rank | Risk | Score | Action Owner     |
|------|------|-------|------------------|
| 1    | R1 — Build crash unresolved | 20 | Front-end lead  |
| 2    | R2 — Missing role switcher  | 12 | Auth team       |
| 3    | R3 — Hidden bugs            | 12 | QA + Dev teams  |

## Risk Summary
- **Critical (P0)**: 1 risk (R1)
- **High (P1–P2)**: 4 risks (R2, R3, R4, R5, R7, R9)
- **Medium (P3)**: 3 risks (R6, R8, R10)

The single highest-impact risk is the unresolved production build crash, which blocks ~95% of all QA validation activities.
