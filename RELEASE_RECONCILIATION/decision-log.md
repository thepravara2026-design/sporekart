# Decision Log — Release Reconciliation

**Date:** 2026-07-18
**Purpose:** Record the reconciliation decisions made for Approval Gate D

---

## D-01 — Authoritative source of truth

**Decision:** The verified **source code** (Sprint 27+ baseline) + Approval-Gate-C
dashboard + Sprint D `fixed-bugs.md` are authoritative. QA Sprint 4 and the original
Sprint D bug-register/dashboard are **superseded** for RC1 decisions.

**Rationale:** The QA Sprint 4 report and Sprint D register were generated against a
stale (Sprint 21/26) codebase. Direct code inspection confirms all register items
resolved. The same-day Approval-Gate-C dashboard (score 87.9, 0 P0/P1) independently
agrees with the code.

**Impact:** Resolves the 3 Critical / 4 High contradiction; verified open-defect
count = 0.

---

## D-02 — QA Sprint 4 retention

**Decision:** Retain `QA_REPORTS/Sprint-04/*` as a **historical QA artifact**; flag as
superseded. Do not delete (traceability), but exclude from RC1 readiness math.

---

## D-03 — Sprint D register/dashboard reconciliation

**Decision:** Edit `BUG_FIX_REPORTS/Sprint-D/bug-register.md` (add reconciliation
header) and `dashboard.json` (set 0 open, READY_PENDING_TEST_RECONCILIATION). No new
bug status invented; reflects verified code.

---

## D-04 — Test-suite / architecture mismatch is a test defect, not app defect

**Decision:** The 25 Playwright failures in `rbac-authorization`, `protected-routes`,
`customer-journey-product-details` are classified as **test defects** (specs assert
removed/renamed routes + selectors). They do **not** indicate application regression.
Block RC1 qualification until reconciled (RR-01).

---

## D-05 — Release recommendation

**Decision:** **READY FOR APPROVAL GATE D, conditionally** — application is
release-ready (score 90.2, 0 open defects). Two non-code conditions: (1) reconcile 3
mismatched specs; (2) re-run cross-browser/mobile in capable CI.

---

## D-06 — CI report-dir overwrite risk

**Decision:** Flag `playwright-regression.yml` hardcoded
`PLAYWRIGHT_REPORT_DIR: Regression/Sprint-B/RC2` as a HIGH process risk (RR-02).
Recommend parameterization before next regression run. No code change required.

---

## D-07 — No production code changes in reconciliation

**Decision:** This sprint is validation-only. The working tree source is unchanged
except documentation reconciliation edits. Confirms Sprint D "no regression" mandate.
