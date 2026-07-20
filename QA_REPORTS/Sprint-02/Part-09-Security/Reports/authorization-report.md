# Authorization — Report

## Tests: 14
- Enterprise routes accessible with admin role (10 routes)
- Guest role shows "Access restricted" on /account
- Customer role shows "Access restricted" on /orders
- Direct URL to /admin accessible without auth
- Guest sidebar shows limited workspaces
- Role switcher exists with all 9 roles
- Unauthorized (401) and Forbidden (403) pages render
- Access denied page works with back navigation

## Findings
| # | Observation | Status | Severity |
|---|-------------|--------|----------|
| 1 | All enterprise routes accessible with admin default role | PASS | — |
| 2 | Role switcher shows 9 distinct roles | PASS | — |
| 3 | Guest role correctly blocked from /account, /orders | PASS | — |
| 4 | Direct URL /admin accessible without auth (BUG-ADM-001) | DEFECT | P0 |
| 5 | Role resets to admin on page reload | DEFECT | P2 |
| 6 | No auth guard on non-enterprise routes | DEFECT | P2 |
| 7 | WorkspacePage shows "Access restricted" for unauthorized roles | PASS | — |
| 8 | No redirect for unauthorized access (still shows page) | DEFECT | P3 |

## Score: **5/10**
