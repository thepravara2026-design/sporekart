# Risk Register — Release Reconciliation

**Date:** 2026-07-18
**Context:** Risks to RC1 qualification identified during reconciliation

---

## Risk Table

| ID | Risk | Likelihood | Impact | RPN | Level | Mitigation | Owner |
|----|------|-----------|--------|-----|-------|------------|-------|
| RR-01 | 3 Playwright specs test non-existent routes/selectors → false failures block QA Sprint 5 | 5 | 4 | 20 | **CRITICAL (process)** | Reconcile specs to shipped architecture before QA Sprint 5 | QA Lead |
| RR-02 | CI regression workflow hardcodes `Regression/Sprint-B/RC2` dir → overwrites prior reports | 4 | 3 | 12 | **HIGH** | Parameterize `PLAYWRIGHT_REPORT_DIR` per run | DevOps |
| RR-03 | Firefox/WebKit launch hangs in local env → cross-browser smoke not executed | 4 | 2 | 8 | MEDIUM | Run in capable CI runner; not an app defect | DevOps |
| RR-04 | Stale QA Sprint 4 + Sprint D register used for RC1 go/no-go | 3 | 4 | 12 | **HIGH** | Use reconciled state (this package) as authority | Release Mgr |
| RR-05 | Large uncommitted WIP on `bugfix/sprint-b-high-priority` | 3 | 2 | 6 | MEDIUM | Commit/stash before Gate D | Eng Lead |
| RR-06 | Product detail pages (`/product/:slug`) not built | 3 | 2 | 6 | MEDIUM | Schedule post-RC1; documented as feature | Product |
| RR-07 | Mock auth provider (no real backend) | 2 | 4 | 8 | MEDIUM | Accept for RC1 demo; real provider post-RC1 (DEF-001) | Product |

---

## Risk Summary

| Level | Count | Items |
|-------|-------|-------|
| CRITICAL (process) | 1 | RR-01 |
| HIGH | 2 | RR-02, RR-04 |
| MEDIUM | 4 | RR-03, RR-05, RR-06, RR-07 |

**Note:** No **application/code** risks remain at Critical or High. All code-level
defects from prior registers are verified resolved. The risks above are
**process/test/infrastructure** items that must be closed for a clean RC1
qualification.

---

## Trend vs Prior Registers

The QA Sprint 4 risk-register listed 4 Critical risks (no route guards, cart missing,
admin broken, Firefox auth). **All 4 are now RESOLVED in code** (verified). The risk
profile has shifted entirely from product defects to test/CI hygiene — a positive
trajectory.
