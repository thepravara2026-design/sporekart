# Approval Gate D — Risk Register

**Date:** 2026-07-18
**Authority:** Independent Release Governance Board

## Risk Table
| ID | Risk | Likelihood | Impact | Level | Mitigation | Owner |
|----|------|-----------|--------|-------|------------|-------|
| RR-01 | 3 Playwright specs test non-existent routes/selectors → false failures block QA Sprint 5 | 5 | 4 | **HIGH (process)** | Reconcile specs to shipped architecture | QA Lead |
| RR-02 | CI regression workflow hardcodes `Regression/Sprint-B/RC2` → overwrites prior reports | 4 | 3 | **HIGH** | Parameterize `PLAYWRIGHT_REPORT_DIR` per run | DevOps |
| RR-03 | Firefox/WebKit launch hangs in local env → cross-browser not executed locally | 4 | 2 | MEDIUM | Run in capable CI runner | DevOps |
| RR-04 | Stale QA Sprint 4 / Sprint D register used for RC1 go/no-go | 3 | 4 | HIGH | Use reconciled state (this gate) as authority | Release Mgr |
| RR-05 | Large uncommitted WIP on `bugfix/sprint-b-high-priority` | 3 | 2 | MEDIUM | Commit/stash before merge | Eng Lead |
| RR-06 | Product detail pages (`/product/:slug`) not built | 3 | 2 | MEDIUM | v1.1 backlog (feature) | Product |
| RR-07 | Mock auth provider (no real backend) | 2 | 4 | MEDIUM | v1.1 (DEF-001) | Product |
| RR-08 | `dist/` + `target/` build outputs untracked in repo | 2 | 1 | LOW | Confirm gitignore | DevOps |

## Risk Summary
| Level | Count |
|-------|-------|
| Critical (code) | 0 |
| High | 3 (all process/CI) |
| Medium | 4 |
| Low | 1 |

**No application/code-level Critical or High risks.** All residual risk is in the
test, CI, and process layers — consistent with "APPROVED WITH RELEASE CONDITIONS".

## Trend
Prior QA Sprint 4 register listed 4 Critical product risks (no route guards, cart
missing, admin broken, Firefox auth). **All 4 verified RESOLVED in code.** Risk
profile has shifted entirely from product defects to test/CI hygiene.
