# Owner Assignment — Sprint C

## Recommended Assignments

| ID | Title | Suggested Owner | Team | Backup |
|----|-------|----------------|------|--------|
| BUG-C-001 | Avatar double upload | Front-end engineer A | FE Team | Front-end engineer B |
| BUG-C-002 | Save button disabled | Front-end engineer A | FE Team | Front-end engineer B |
| BUG-C-003 | ARIA landmarks | Front-end engineer B | FE Team | Front-end engineer A |
| BUG-C-004 | Mobile nav empty | Front-end engineer A | FE Team | Front-end engineer B |
| BUG-C-005 | Service Worker | Front-end engineer B / SRE | FE + SRE | SRE engineer |
| BUG-C-006 | Auth client replacement | Full-stack engineer C | Backend Team | Front-end engineer A |
| BUG-C-007 | Duplicate toast consolidation | Front-end engineer A | FE Team | Front-end engineer B |
| BUG-C-008 | 404 page | Front-end engineer B | FE Team | Front-end engineer A |
| BUG-C-009 | Performance budgets / CI | SRE / DevOps engineer | Platform Team | Front-end engineer B |

## Workload by Engineer

| Engineer | Wave 1 Items | Wave 2 Items | Wave 3 Items | Total Hours |
|----------|-------------|-------------|-------------|-------------|
| **Front-end A** | C-001 (4h), C-002 (3h), C-004 (6h), C-007 (8h) | — | — | **21h eng** |
| **Front-end B** | C-003 (8h) | C-005 (20h), C-008 (4h) | — | **32h eng** |
| **Full-stack C** | — | C-006 (24h) | — | **24h eng** |
| **SRE/DevOps** | — | C-005 (co-owned) | C-009 (12h) | **12h eng + shared** |
| **QA (shared)** | 13h | 16h | 4h | **33h QA** |

## Assignment Notes

1. **Front-end A** carries the heaviest Wave 1 load (21h) but all items are independent and can be parallelized.
2. **Front-end B** handles ARIA landmarks + Service Worker — complementary skillset.
3. **Full-stack C** is dedicated to auth refactor (Wave 2) — this is the highest-risk item and needs focused attention.
4. **SRE/DevOps** provides part-time support for SW and CI tooling.
5. **QA** supports all waves — recommend writing Playwright tests in parallel with development.

## Unassigned (TBD — Needs Management Decision)

| Role | Count | Rationale |
|------|-------|-----------|
| Full-stack C | 1 | Needs to be assigned if auth refactor goes ahead |
| SRE/DevOps | 1 | Needs to be identified for SW + CI work |
