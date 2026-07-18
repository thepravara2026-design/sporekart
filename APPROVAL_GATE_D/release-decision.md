# Approval Gate D — Release Decision

**Date:** 2026-07-18
**Authority:** Independent Enterprise Release Governance Board
**Decision:** **APPROVED WITH RELEASE CONDITIONS**

---

## 1. Decision Options Considered

- **APPROVED** — Rejected: cross-browser not executed locally and test specs not yet
  reconciled; cannot assert unconditional green without CI evidence.
- **APPROVED WITH RELEASE CONDITIONS** — **Selected**: all hard gate criteria pass;
  residual items are non-production, non-functional qualification conditions.
- **REJECTED** — Rejected: zero production defects; no basis to reject.

---

## 2. Decision Matrix Compliance

"APPROVED WITH RELEASE CONDITIONS" permits only non-production, non-functional,
non-customer-facing items. Verified:

| Attached Condition | Non-prod? | Non-functional? | Non-customer-facing? |
|--------------------|-----------|-----------------|----------------------|
| C1 Reconcile 3 Playwright specs | ✅ | ✅ | ✅ |
| C2 Parameterize CI report dir | ✅ | ✅ | ✅ |
| C3 Execute cross-browser in CI | ✅ | ✅ | ✅ |
| C4 Commit/stash WIP | ✅ | ✅ | ✅ |

All four satisfy the matrix. No production defect condition exists.

---

## 3. Conditions of Approval

1. **C1** — Reconcile `rbac-authorization.spec.ts`, `protected-routes.spec.ts`,
   `customer-journey-product-details.spec.ts` to the shipped route/selector architecture.
2. **C2** — Parameterize `PLAYWRIGHT_REPORT_DIR` in `playwright-regression.yml`
   (per-run input / matrix) to prevent Sprint-B/RC2 overwrite.
3. **C3** — Execute full Playwright matrix (chromium/firefox/webkit/mobile-chrome/
   mobile-safari/tablet) in a capable CI runner; attach results to `QA_REPORTS`.
4. **C4** — Commit or stash outstanding WIP on `bugfix/sprint-b-high-priority`.

---

## 4. Authorization

- **Authorized:** Proceed to RC1 qualification; begin Regression Sprint D upon
  acknowledgement of conditions.
- **Not Authorized:** Any production code change outside the conditions above; any
  scope expansion.

---

## 5. Sign-off

Board certifies, on verified evidence, that SporeKart meets production-readiness
bar with the four release conditions. Final RC1 sign-off completes when C1–C4 are
closed and CI evidence attached.
