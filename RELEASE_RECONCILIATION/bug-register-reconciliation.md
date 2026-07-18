# Bug Register Reconciliation

**Date:** 2026-07-18

---

## 1. Registers Reviewed

| Register | Location | Role |
|----------|----------|------|
| Sprint A fixed register | `BUG_FIX_REPORTS/Sprint-A/fixed-bug-register.md` | Historical |
| Sprint B fixed register | `BUG_FIX_REPORTS/Sprint-B/fixed-bug-register.md` | Historical |
| Sprint C consolidated | `BUG_FIX_REPORTS/Sprint-C/consolidated-bug-register.md` | Historical |
| Sprint D register | `BUG_FIX_REPORTS/Sprint-D/bug-register.md` | **Reconciled this sprint** |
| QA Sprint 2 master | `QA_REPORTS/Sprint-02/.../master-bug-register.md` | Historical |
| QA Sprint 3 register | `QA_REPORTS/Sprint-03/bug-register.md` | Historical |
| QA Sprint 4 register | `QA_REPORTS/Sprint-04/bug-register.md` | Historical (superseded) |

---

## 2. Duplicated Bug ID Audit

Bug IDs were checked for collisions across all registers.

| ID Pattern | Appears In | Collision? |
|------------|-----------|-----------|
| `BUG-S3-CRIT-001..003` | QA Sprint 4 register, Sprint D register | ✅ Same ID, same meaning (consistent) |
| `BUG-S3-HIGH-003` | QA Sprint 4, Sprint D | ✅ Consistent |
| `BUG-AUTH-001` | QA Sprint 4, Sprint D | ✅ Consistent |
| `BUG-QA4-CRIT-001/002` | QA Sprint 4, Sprint D | ✅ Consistent (fixed) |
| `BUG-QA4-HIGH-003/004` | QA Sprint 4, Sprint D | ✅ Consistent |
| `BUG-QA4-MED-001..005` | QA Sprint 4, Sprint D | ✅ Consistent |
| `BUG-QA4-LOW-001..003` | QA Sprint 4, Sprint D | ✅ Consistent |
| `BUG-RT-*` / `SEC-*` / `COMP-*` / `MOB-*` / `PERF-*` | Git log, code comments | ✅ Distinct namespaces |

**No duplicated/conflicting bug IDs found.** IDs are stable across registers; the
only issue is that their **status** (open vs. resolved) is stale in the QA Sprint 4
and original Sprint D artifacts.

---

## 3. Status Reconciliation (verified against code)

| Bug ID | Original Status | Verified Status | Evidence |
|--------|---------------|----------------|----------|
| BUG-S3-CRIT-001 (route guards) | Open | ✅ RESOLVED | `RequireAuth.tsx`, `WorkspacePage.tsx` |
| BUG-S3-CRIT-002 (cart) | Open | ✅ RESOLVED | `features/customer/orders/*` |
| BUG-S3-CRIT-003 (admin) | Open | ✅ RESOLVED | `admin/` module system |
| BUG-AUTH-001 (Firefox `:has()`) | Open | ✅ RESOLVED | 0 `:has(` matches |
| BUG-S3-HIGH-003 (role switcher) | Open | ✅ RESOLVED | Header `Preview role` switcher |
| BUG-QA4-HIGH-003 (OTP nav) | Open | ✅ RESOLVED | `VerifyOtpPage` redirect + demo |
| BUG-QA4-HIGH-004 (product data) | Open | ⏸ DEFERRED (feature) | No `/product/:slug` route |
| BUG-QA4-MED-001 (dashboard redirect) | Open | ✅ RESOLVED | RequireAuth/CanView redirect |
| BUG-QA4-MED-002 (OTP validation) | Open | ✅ RESOLVED | `AuthAlert` on error |
| BUG-QA4-MED-003 (search case) | Open | ✅ RESOLVED | `searchArticles` lowercases |
| BUG-QA4-MED-004 (social ARIA) | Open | ✅ RESOLVED | `SocialLogin` labels |
| BUG-QA4-MED-005 (flaky tests) | Open | ⏸ DEFERRED (test maint.) | Test/code mismatch |
| BUG-QA4-LOW-001 (footer contrast) | Open | ✅ RESOLVED | White-on-green ≈ 8:1 |
| BUG-QA4-LOW-002 (training title) | Open | ✅ RESOLVED | `seo.title` set |
| BUG-QA4-LOW-003 (deprecated prop) | Open | ✅ RESOLVED | Uses `Component` prop |
| BUG-QA4-CRIT-001/002 | Fixed | ✅ FIXED (confirmed) | Input/Checkbox, AuthStore |

**Net:** 14 of 16 triaged items → RESOLVED in code; 2 → DEFERRED (one feature,
one test maintenance). **0 open defects blocking RC1.**

---

## 4. Accepted Risks / Known Limitations

| Item | Type | Disposition |
|------|------|-------------|
| Product detail pages (`/product/:slug`) | Feature gap | Post-RC1 backlog |
| Test-suite/architecture mismatch (25 specs) | Test defect | Reconcile at Gate D |
| Mock auth (no real provider) | Known limitation | Per DEF-001, post-RC1 |
| WebKit/Safari CI coverage | Infra gap | Re-run in capable CI |

---

## 5. Removal of Stale Issues

The Sprint D `bug-register.md` and `dashboard.json` were updated to reflect the
verified 0-open state. The QA Sprint 4 register is retained as a **historical QA
record** (it accurately described the Sprint 4 baseline) but is **superseded** for
any RC1 go/no-go decision.
