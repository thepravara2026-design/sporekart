# Engineering Priority Matrix — Sprint C

## Priority Quadrant

| | High Value | Medium Value | Low Value |
|---|-----------|-------------|-----------|
| **Low Effort** | **BUG-C-001** (avatar double upload — 4h) | **BUG-C-008** (404 page — 4h) | — |
| | **BUG-C-002** (save button disabled — 3h) | | |
| | **BUG-C-004** (mobile nav — 6h) | | |
| **Medium Effort** | **BUG-C-003** (ARIA landmarks — 8h) | **BUG-C-007** (duplicate toasts — 8h) | **BUG-C-009** (perf budgets — 12h) |
| **High Effort** | **BUG-C-006** (real auth — 24h) | **BUG-C-005** (Service Worker — 20h) | — |

## Priority Ranking

| Rank | ID | Title | Effort (h) | Value | Risk | Score |
|------|----|-------|-----------|-------|------|-------|
| 1 | BUG-C-002 | Save button disabled (notification prefs) | 5 | High | Low | ★★★★★ |
| 2 | BUG-C-001 | Avatar double upload buttons | 6 | High | Low | ★★★★★ |
| 3 | BUG-C-004 | Mobile nav menu empty | 9 | High | Low | ★★★★☆ |
| 4 | BUG-C-008 | No 404 page | 6 | Medium | Low | ★★★★☆ |
| 5 | BUG-C-003 | No ARIA landmarks | 11 | High | Medium | ★★★★☆ |
| 6 | BUG-C-007 | Duplicate toast implementations | 11 | Medium | Medium | ★★★☆☆ |
| 7 | BUG-C-006 | Auth client is stub | 32 | High | High | ★★★☆☆ |
| 8 | BUG-C-005 | No Service Worker | 26 | Medium | Medium | ★★☆☆☆ |
| 9 | BUG-C-009 | No performance tooling | 16 | Low | Low | ★★☆☆☆ |

## Wave Assignment by Priority

| Wave | Items | Total Effort | Rationale |
|------|-------|-------------|-----------|
| **Wave 1** | BUG-C-001, BUG-C-002, BUG-C-003, BUG-C-004, BUG-C-007 | 39h eng + 13h QA = **52h** | Highest value, lowest risk, quick wins |
| **Wave 2** | BUG-C-005, BUG-C-006, BUG-C-008 | 48h eng + 16h QA = **64h** | Higher effort but critical for operational readiness |
| **Wave 3** | BUG-C-009 | 12h eng + 4h QA = **16h** | Nice-to-have; stretch goal |

## Effort Breakdown

| Wave | Engineering | QA | Total |
|------|------------|-----|-------|
| Wave 1 | 39h | 13h | 52h |
| Wave 2 | 48h | 16h | 64h |
| Wave 3 | 12h | 4h | 16h |
| **Total** | **99h** | **33h** | **132h** |
