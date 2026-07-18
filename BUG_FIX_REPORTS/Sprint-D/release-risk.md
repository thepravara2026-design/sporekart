# Release Risk Assessment — Bug Fix Sprint D → RC1

**Date:** 2026-07-18

---

## Risk Assessment Framework

Each risk is scored on Likelihood (1–5) and Impact (1–5). RPN = L × I. Thresholds: Critical ≥ 20, High 12–19, Medium 6–11, Low 1–5.

---

## Risk Register for RC1 Release

| ID | Risk | L | I | RPN | Level | Mitigation | Owner |
|----|------|---|---|-----|-------|------------|-------|
| R-D01 | Sprint D scope expansion not approved — RC1 ships without Critical/High fixes | 4 | 5 | 20 | **CRITICAL** | Escalate to Product Owner; document risk acceptance | Eng Lead |
| R-D02 | Cart/checkout implementation takes longer than estimated (15–20 days) | 4 | 4 | 16 | **HIGH** | Cut scope: ship cart + checkout only, defer payment; parallelize with other work | Dev Lead |
| R-D03 | Route guards break existing navigation on edge-case routes | 3 | 4 | 12 | **HIGH** | Comprehensive authZ test suite; gradual rollout (admin first, then dashboard, then training) | QA Lead |
| R-D04 | Admin console requirements unclear — scope creep | 4 | 3 | 12 | **HIGH** | Define minimum viable admin (dashboard + user list); defer settings/permissions | PM |
| R-D05 | Flaky tests increase beyond 5% after Sprint D changes | 3 | 3 | 9 | **MEDIUM** | Address flaky tests (MED-005) during Sprint D; add test stability gates in CI | QA Lead |
| R-D06 | Team capacity insufficient for expanded scope | 3 | 4 | 12 | **HIGH** | Ensure 2+ developers assigned; consider contractor or extended timeline | Eng Lead |
| R-D07 | CSS changes for Firefox fix regress Chromium rendering | 2 | 3 | 6 | **MEDIUM** | Cross-browser test suite runs automatically; visual regression tests | Frontend Dev |
| R-D08 | Production build time increases with new code | 2 | 2 | 4 | **LOW** | Monitor build time; lazy-load admin and cart routes | Dev Lead |
| R-D09 | Real auth provider integration postponed — mock auth in RC1 | 5 | 5 | 25 | **CRITICAL** | Cannot ship to production without real auth; if RC1 is internal/demo only, this is acceptable | PM |

---

## Risk Summary

| Level | Count | Key Items |
|-------|-------|-----------|
| CRITICAL | 2 | Scope approval (R-D01), Real auth provider (R-D09) |
| HIGH | 4 | Cart timeline (R-D02), Route guard regressions (R-D03), Admin scope (R-D04), Team capacity (R-D06) |
| MEDIUM | 2 | Flaky tests (R-D05), Firefox CSS regression (R-D07) |
| LOW | 1 | Build time (R-D08) |

---

## Go/No-Go Criteria for RC1

### Must Pass (Gate 1 — Security)
- [ ] All 256 protected routes require authentication
- [ ] Admin routes restricted to admin role only
- [ ] No sensitive data exposed without auth

### Must Pass (Gate 2 — Core Functionality)
- [ ] Cart page renders with add/remove/quantity
- [ ] Checkout flow functional (mock payment OK)
- [ ] Login works in Chromium, Firefox, Edge
- [ ] OTP flow works with direct navigation

### Must Pass (Gate 3 — Quality)
- [ ] QA Sprint 5 regression: 0 Critical/High
- [ ] Stability score ≥ 85/100
- [ ] Flaky test rate ≤ 3%
- [ ] TypeScript 0 errors
- [ ] Production build passes

---

## Recommendation

**Do not ship RC1** until:
1. Sprint D expanded scope is approved (or formal risk acceptance documented)
2. All 3 Critical bugs are fixed
3. At minimum, Firefox auth + OTP navigation are fixed (2 of 4 High bugs)
4. QA Sprint 5 passes with 0 Critical/High issues

The current 74/100 stability score and 55/100 release readiness score are insufficient for RC1.
