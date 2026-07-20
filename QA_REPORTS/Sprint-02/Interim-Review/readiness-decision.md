# SporeKart QA Sprint 2 — Readiness Decision (Interim Review)

**Date:** 2026-07-17  
**Decision Body:** Enterprise QA Governance Board  

---

## Readiness Score: 35/100 ⛔

| Phase | Score | Status |
|-------|-------|--------|
| Part 1 — Authentication | 43/100 | Needs Attention |
| Part 2 — Customer Journey | 42/100 | Needs Attention |
| **Consolidated Interim** | **35/100** | **Critical** |

---

## Decision: HOLD

### ⛔ Can QA continue to Part 3 (Checkout)?

**Decision: HOLD — DO NOT proceed to Part 3 without Product Management scope decision.**

| Vote | Count |
|------|-------|
| GO | 0 |
| GO WITH RISKS | 1 (conditional on scope reduction) |
| HOLD | 6 |
| NO GO | 0 |

### Gating Conditions (Must be resolved before Part 3)

| # | Condition | Owner | Deadline |
|---|-----------|-------|----------|
| 1 | Product Management decides: implement cart MVP or descope from v1.0 | PM | Before Sprint 2 Part 3 |
| 2 | Firefox mock API interception resolved (BUG-001) | QA Engineering | Before Sprint 2 Part 3 |
| 3 | Flaky tests stabilized (OTP, networkidle, ENOENT) | SDET | During Sprint 2 break |
| 4 | Role switcher component added for QA mode (BUG-005) | Engineering | During Sprint 2 break |

### If HOLD conditions are met, Part 3 scope should be:

1. Validate address management (placeholder or implemented)
2. Validate customer dashboard order flow (existing)
3. Validate shipping configuration (if implemented)
4. Gap analysis for payment integration (no real payments)
5. Cross-browser testing with Firefox fix
6. DO NOT test full checkout flow without cart implementation

---

## Defect Burndown Target

| Priority | Current | Target Before Part 3 | Target Before GA |
|----------|---------|---------------------|-------------------|
| P0 | 4 | 2 (fix Firefox + decide on catalog scope) | 0 |
| P1 | 9 | 5 (accessibility + search/filter/sort scope decisions) | 0 |
| P2 | 4 | 4 (accept as known issues) | 0 |
| P3 | 3 | 3 (accept as known issues) | 2 |

---

## Board Recommendation

**"The application has solid foundations but is not yet an e-commerce platform. Before continuing QA Sprint 2, the Product Management must decide what v1.0 actually ships. QA cannot validate checkout without a cart, and cannot validate product discovery without a catalog."**

— SporeKart QA Governance Board, 2026-07-17

---

## Appendix: Decision Matrix

| Scenario | Decision | Risk |
|----------|----------|------|
| Proceed to Part 3 without changes | ❌ HOLD | Part 3 will find zero working features. Wasted effort. |
| Descope cart/catalog from v1.0, proceed with Part 3 as dashboard validation | ⚠️ GO WITH RISKS | Features promised to stakeholders not delivered. |
| Implement cart MVP + fix Firefox, then proceed to Part 3 | ✅ RECOMMENDED | Best outcome. Validates full journey. |
| Fix all P0/P1 before Part 3 | ❌ HOLD | Unrealistic timeline. Prioritize P0 only. |
