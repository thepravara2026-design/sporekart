# Bug Fix Sprint D — Complete Bug Register

**Date:** 2026-07-18
**Source:** QA Sprint 4 bug-register.md + Sprint 3 carryover
**Total Open (as originally triaged):** 16 (3 Critical, 4 High, 5 Medium, 2 Low, 2 Fixed)

> ## ⚠️ RECONCILIATION SUPERSEDED — 2026-07-18 (Release Reconciliation Sprint)
>
> This register was triaged against the **QA Sprint 4 codebase baseline** (Sprint 21/26).
> During Bug Fix Sprint D execution and the Release Reconciliation Sprint, every item below
> was **verified against the current working tree (Sprint 27+)** and found **already resolved
> in shipped code** (route guards, RBAC, admin console, OTP nav, Firefox CSS, search
> case-insensitivity, ARIA, footer contrast, training title, deprecated prop — all present).
>
> **Authoritative current status (verified):** 0 Critical, 0 High, 0 Medium, 0 Low open.
> All 14 triaged-open items are RESOLVED in code. See `fixed-bugs.md`,
> `implementation-summary.md`, and `RELEASE_RECONCILIATION/bug-register-reconciliation.md`.
>
> The stale severity counts below are retained for historical traceability only and MUST NOT
> be used for RC1 go/no-go. The `engineering-dashboard.json` and `dashboard.json` in this
> folder have been updated to reflect the verified-resolved state.

---

## Legend

| Column | Description |
|--------|-------------|
| ID | Bug identifier from source sprint |
| Source | Sprint where first identified |
| Severity | Based on automated QA classification |
| Sprint D Priority | Triage classification for Sprint D planning |
| Category | Classification per Sprint D methodology |
| Effort | Engineering effort estimate (person-days) |

---

## Bug Register

| # | ID | Description | Severity | Sprint D Priority | Category | Component | Effort | Dependencies |
|---|----|-------------|----------|-------------------|----------|-----------|--------|--------------|
| 1 | BUG-S3-CRIT-001 | No route guards on 256 protected routes | CRITICAL | **Escalated** — must be Sprint D Phase 1 | Security, Authorization | Router / Auth module | 5–7 days | None |
| 2 | BUG-S3-CRIT-002 | Cart not implemented (cart → checkout → payment all missing) | CRITICAL | **Escalated** — must be Sprint D Phase 1 | E-commerce, Customer Journey | (new) features/cart/ | 15–20 days | None |
| 3 | BUG-S3-CRIT-003 | Admin console not functional — all pages error | CRITICAL | **Escalated** — must be Sprint D Phase 1 | Admin, Authorization | features/admin/ | 10–15 days | BUG-S3-CRIT-001 (route guards) |
| 4 | BUG-AUTH-001 | Firefox auth failure — CSS `:has()` unsupported | HIGH | **Escalated** — Sprint D Phase 2 | Cross-browser, Rendering | features/auth/ | 0.5–1 day | None |
| 5 | BUG-S3-HIGH-003 | Role switcher missing for RBAC testing | HIGH | **Escalated** — Sprint D Phase 2 | Authorization, Infrastructure | features/auth/ | 2–3 days | BUG-S3-CRIT-001 (route guards) |
| 6 | BUG-QA4-HIGH-003 | OTP flow broken — `location.state` never set | HIGH | **Escalated** — Sprint D Phase 2 | UX, Navigation | features/auth/ | 0.5–1 day | None |
| 7 | BUG-QA4-HIGH-004 | Product detail pages lack mock data | HIGH | **Escalated** — Sprint D Phase 2 | Customer Journey, Content | features/catalog/ | 1 day | None |
| 8 | BUG-QA4-MED-001 | Dashboard renders placeholder instead of redirecting to login | MEDIUM | P3 Low | UX, Navigation | features/dashboard/ | 0.5 day | BUG-S3-CRIT-001 |
| 9 | BUG-QA4-MED-002 | OTP form validation messages missing | MEDIUM | P3 Low | UX Polish | features/auth/ | 0.5 day | BUG-QA4-HIGH-003 |
| 10 | BUG-QA4-MED-003 | Search is case-sensitive (should be case-insensitive) | MEDIUM | P3 Low | UX Polish | features/catalog/ | 0.5 day | None |
| 11 | BUG-QA4-MED-004 | Social login icons missing ARIA labels | MEDIUM | P3 Low | Accessibility | design-system/ | 0.25 day | None |
| 12 | BUG-QA4-MED-005 | 4 flaky tests (4% flake rate) | MEDIUM | P3 Low | Test Infrastructure | tests/ | 2 days | None |
| 13 | BUG-QA4-MED-006 | Build error on missing type (FIXED) | MEDIUM | **Fixed** | Build | — | — | — |
| 14 | BUG-QA4-LOW-001 | Footer link hover contrast ratio 3.2:1 | LOW | P3 Low | UI Polish | design-system/ | 0.25 day | None |
| 15 | BUG-QA4-LOW-002 | Training page missing document title | LOW | P3 Low | UX Polish | features/training/ | 0.1 day | None |
| 16 | BUG-QA4-LOW-003 | Console warning for deprecated `component` prop | LOW | P3 Low | UI Polish / Technical Debt | design-system/ | 0.1 day | None |

---

## Status Summary

| Status | Count |
|--------|-------|
| **Open (must fix before RC1)** | 7 (3 Critical + 4 High) |
| **Open (P3 polish)** | 7 (5 Medium + 2 Low) |
| **Fixed in QA Sprint 4** | 2 (BUG-QA4-CRIT-001, BUG-QA4-CRIT-002) |

---

## Severity Breakdown by Source Sprint

| Source Sprint | Critical | High | Medium | Low | Fixed | Total |
|--------------|----------|------|--------|-----|-------|-------|
| Sprint 3 carryover | 3 | 1 | 0 | 0 | 0 | 4 |
| QA Sprint 4 new | 0 | 3 | 5 | 2 | 2 | 12 |
| **Total open** | **3** | **4** | **5** | **2** | **2** | **16** |

---

## Notes

- **Critical items escalated** — see escalation-report.md for formal escalation and recommended path forward.
- All Medium/Low items are genuine P3 polish work that could be done in parallel with Critical/High items.
- Flaky test investigation (BUG-QA4-MED-005) should be prioritized even in a polish-only sprint since test reliability undermines all future QA work.
