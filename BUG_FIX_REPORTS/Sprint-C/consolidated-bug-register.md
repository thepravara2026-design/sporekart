# Consolidated Bug Register — Sprint C (Full Sprint 3 Audit)

## Full Defect Inventory (All 15 Unique Items from QA Sprint 3)

### P0/P1 — Blockers (6 items — NOT in Sprint C)

| ID | Title | Severity | Priority | Evidence | Status |
|----|-------|----------|----------|----------|--------|
| BUG-S3-CRIT-001 | Production build crash — CSS-in-JS / React #62 | Critical | P1 | Console logs, screenshots all routes | Open |
| BUG-S3-HIGH-003 | Role switcher component absent from codebase | High | P1 | grep results (zero matches) | Open |
| BUG-S3-P1-004 | Admin routes accessible without authentication | High | P1 | Test confirmed no redirect | Open |
| BUG-S3-HIGH-005 | Auth context infinite re-render loop (empty role string) | High | P1 | Console errors in auth context | Open |
| BUG-S3-P3-001 | No observability/monitoring/logging infrastructure | High | P1 | Empty directories | Open |
| BUG-S3-P3-002 | No security headers emitted | High | P1 | Header audit | Open |

### P2 Medium — Sprint C Eligible (9 items)

| ID | Original ID | Title | Severity | Priority | Evidence | Status |
|----|-------------|-------|----------|----------|----------|--------|
| BUG-C-001 | BUG-S3-P1-003 | Avatar upload shows double buttons | Medium | P2 | Screenshot, DOM audit | Open |
| BUG-C-002 | BUG-S3-P1-004 | Notification prefs Save button stuck disabled | Medium | P2 | Screenshot, interaction test | Open |
| BUG-C-003 | BUG-S3-HIGH-004 | No semantic ARIA landmarks on any page | Medium* | P2 | aXe scan, DOM audit | Open |
| BUG-C-004 | BUG-S3-MED-006 | Mobile nav menu empty on small viewport | Medium | P2 | Screenshot, viewport test | Open |
| BUG-C-005 | BUG-S3-P3-003 | No Service Worker — zero offline capability | Medium | P2 | Codebase search | Open |
| BUG-C-006 | BUG-S3-P3-004 | Auth client is stub with hardcoded OTP | Medium | P2 | Code review | Open |
| BUG-C-007 | BUG-S3-P3-005 | Duplicate toast implementations | Medium | P2 | Code review | Open |
| BUG-C-008 | BUG-S3-P3-006 | No app-level 404 page | Medium* | P2 | Route test | Open |
| BUG-C-009 | BUG-S3-P3-008 | No performance budgets or CI tooling | Medium* | P2 | Infrastructure audit | Open |

*Reclassified from original severity. See engineering-backlog.md for rationale.

### P3/P4 — Deferred (4 items — future sprints)

| ID | Title | Severity | Priority | Rationale |
|----|-------|----------|----------|-----------|
| BUG-S3-P1-007 | Notification badge unread count not verifiable | Low | P4 | Cosmetic; UX enhancement |
| BUG-S3-P3-007 | JPA entities lack audit timestamps and soft delete | Low | P3 | Tech debt; resolved when backend is wired |
| BUG-S3-LOW-007 | `<title>` reads "LitVue App" instead of "SporeKart" | Low | P3 | Cosmetic; single-line fix |
| BUG-S3-P1-005 | Guest redirect hidden behind ErrorBoundary | Medium | P3 | Resolved by CRIT-001 fix |

## Rejected / Duplicate Items

| Original ID | Reason |
|-------------|--------|
| BUG-S3-CRIT-002 | Duplicate of CRIT-001 (all pages crash due to same root cause) |
| BUG-S3-P1-001 | Duplicate of CRIT-001 (Part 1 tracking) |
| BUG-S3-P1-002 | Duplicate of HIGH-003 (Part 1 tracking) |
| BUG-S3-MED-005 | Duplicate of CRIT-001 (console errors are symptom) |
| BUG-S3-P1-006 | Duplicate of CRIT-001 (console errors are symptom) |
| BUG-S3-LOW-007 | Duplicate of CRIT-001 (nav links missing due to crash) |
| BUG-S3-MED-006 | Duplicate of CRIT-001 (guest redirect hidden due to crash) |
| BUG-S3-P1-005 | Duplicate of CRIT-001 (same root cause) |
