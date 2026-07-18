# Bug Register — QA Sprint 3 Part 2

## Active Defects (Carried Forward)

| ID               | Severity | Priority | Module     | Description                                                                 | Component       | Status |
|------------------|----------|----------|------------|-----------------------------------------------------------------------------|-----------------|--------|
| BUG-S3-CRIT-001  | Critical | P0       | Build      | Production build crash: React #62 — "Too many re-renders" + CSSStyleDeclaration TypeError on all routes using shared component library | shared/layout   | Open   |
| BUG-S3-HIGH-003  | High     | P1       | RBAC       | Role switcher component (`select[aria-label="Switch review role"]`) not present in codebase — cannot test role-based views | auth/roles      | Open   |
| BUG-S3-P1-001    | Critical | P0       | Build      | Same as BUG-S3-CRIT-001 — Part 1 duplicate tracking                         | shared/layout   | Open   |
| BUG-S3-P1-002    | High     | P1       | RBAC       | Same as BUG-S3-HIGH-003 — Part 1 duplicate tracking                         | auth/roles      | Open   |
| BUG-S3-P1-003    | Medium   | P2       | Account    | Account profile — Avatar section shows double-upload buttons (two identical `<input type="file">` elements) | account/profile | Open   |
| BUG-S3-P1-004    | Medium   | P2       | Account    | Notification preferences — "Save" button persists as disabled after preferences loaded | account/notifications | Open   |
| BUG-S3-HIGH-005  | High     | P1       | Auth       | Auth context: `role` defaults to empty string, causing infinite re-render loop in route guards | auth/context    | Open   |
| BUG-S3-MED-006   | Medium   | P2       | Navigation | Mobile nav menu — `<nav>` element empty when toggled on small viewport      | layout/nav      | Open   |
| BUG-S3-LOW-007   | Low      | P3       | UI         | Brand asset broken: `<title>` reads `LitVue App` instead of `SporeKart`     | index.html      | Open   |

## New Findings from Part 2 Execution

| ID               | Severity | Priority | Module     | Description                                                                 | Evidence        | Status |
|------------------|----------|----------|------------|-----------------------------------------------------------------------------|-----------------|--------|
| (none)           | —        | —        | —          | No new defects discovered — all routes are blocked by BUG-S3-CRIT-001       | 220/221 tests pass at HTTP level | Open   |

## Cumulative Defect Summary

| Severity | Count | Action Required                          |
|----------|-------|------------------------------------------|
| Critical | 1     | Must fix before any further QA validation|
| High     | 2     | Must fix before RC                       |
| Medium   | 2     | Should fix before RC                     |
| Low      | 1     | Nice-to-have before RC                   |
| **Total**| **6** | (2 are duplicates tracked across parts)  |

**Unique defects**: 6 (BUG-S3-CRIT-001, BUG-S3-HIGH-003, BUG-S3-P1-003, BUG-S3-P1-004, BUG-S3-MED-006, BUG-S3-LOW-007)
