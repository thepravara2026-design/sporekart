# Effort Estimation — Sprint C

## Estimation Methodology
- **Story Points**: Fibonacci (1, 2, 3, 5, 8, 13, 21)
- **Complexity**: XS, S, M, L, XL
- **Hours**: Engineering + QA

## Detailed Estimates

| ID | Title | Complexity | SP | Eng Hours | QA Hours | Total Hours |
|----|-------|------------|----|-----------|----------|-------------|
| BUG-C-001 | Avatar double upload buttons | S | 3 | 4 | 2 | 6 |
| BUG-C-002 | Save button disabled | S | 2 | 3 | 2 | 5 |
| BUG-C-003 | ARIA landmarks | M | 5 | 8 | 3 | 11 |
| BUG-C-004 | Mobile nav empty | M | 5 | 6 | 3 | 9 |
| BUG-C-005 | Service Worker | L | 13 | 20 | 6 | 26 |
| BUG-C-006 | Auth client real replacement | L | 13 | 24 | 8 | 32 |
| BUG-C-007 | Duplicate toast consolidation | M | 5 | 8 | 3 | 11 |
| BUG-C-008 | 404 page | S | 2 | 4 | 2 | 6 |
| BUG-C-009 | Performance budgets / CI | M | 5 | 12 | 4 | 16 |
| **Total** | **9 items** | **Mixed** | **53** | **99** | **33** | **132** |

## Effort by Wave

| Wave | Items | SP | Eng Hours | QA Hours | Total Hours | Days (1 dev) | Days (2 devs) |
|------|-------|----|-----------|----------|-------------|-------------|--------------|
| Wave 1 | 5 | 20 | 39 | 13 | 52 | 6.5 | 3.25 |
| Wave 2 | 3 | 28 | 48 | 16 | 64 | 8 | 4 |
| Wave 3 | 1 | 5 | 12 | 4 | 16 | 2 | 1 |
| **Total** | **9** | **53** | **99** | **33** | **132** | **16.5** | **8.25** |

## Velocity Assumptions

| Dev Capacity | Weekly Hours | Wave 1 | Wave 2 | Wave 3 |
|-------------|-------------|--------|--------|--------|
| 1 full-time engineer | 40h/wk | Week 1 | Week 2 | Week 3 |
| 2 full-time engineers | 80h/wk | 3.25 days | 4 days | 1 day |

## Team Sizing Recommendation

| Role | Allocation | Notes |
|------|-----------|-------|
| Front-end engineer | 1 FTE | All Wave 1 items; BUG-C-008 |
| Backend/full-stack engineer | 0.5 FTE | BUG-C-006 (auth); BUG-C-007 (toasts) |
| QA engineer | 0.5 FTE | Test automation for all waves |
| SRE/DevOps | 0.25 FTE | BUG-C-005 (SW); BUG-C-009 (CI) |

**Recommended**: 2 FTE engineering + 0.5 FTE QA over 3 weeks = **Sprint C is feasible in 3 weeks with 2 engineers**.

## Resource-Loaded Schedule

| Week | Dev 1 | Dev 2 | QA |
|------|-------|-------|-----|
| **W1** | C-002 (3h) → C-001 (4h) → C-004 (6h) | C-003 (8h) → C-007 (8h) | Write tests for C-001, C-002, C-003, C-004, C-007 |
| **W2** | C-008 (4h) → C-005 (20h) | C-006 (24h) | Write tests for C-005, C-006, C-008 |
| **W3** | C-005 cont'd + C-009 (12h) | C-006 cont'd | Final validation; regression suite; E2E tests |
