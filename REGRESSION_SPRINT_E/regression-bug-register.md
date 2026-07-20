# Regression Sprint E — Bug Register

**Zero regressions introduced by Sprint E.**  
This register documents **pre-existing gaps** (not caused by Architecture Correction Sprint E) that remain in the codebase.

---

## Pre-existing Gaps (Not Sprint E Regressions)

| ID | Suite | Description | Severity | Status | First Discovered |
|----|-------|-------------|----------|--------|------------------|
| PRE-001 | 3 | Product detail page displays placeholder data via mockData, not real product data | LOW | Known | Sprint 3 |
| PRE-002 | 3 | Product search returns blog-only results; no product catalogue search | MEDIUM | Known | Sprint 3 |
| PRE-003 | 4 | Cart badge count in header does not update reactively on some navigation paths (pre-existing CartContext detection issue) | MEDIUM | Known | Sprint 5 |
| PRE-004 | 5 | Checkout form state lost on back-navigation (no localStorage persistence for form state) | LOW | Known | Sprint 4 |
| PRE-005 | 3 | Product catalogue uses static/mock data — no backend integration | MEDIUM | Known | Sprint 2 |
| PRE-006 | 6 | Payment gateway is self-contained mock — no real payment processing | MEDIUM | Known | Sprint 4 (Phase 0 intentional) |

---

## Sprint 5 Critical Defects — Verifying No Regression

| ID | Description | Sprint 5 Status | Sprint E Status |
|----|-------------|----------------|-----------------|
| SPRINT5-CRITICAL-001 | State mutation via unsafe return (CartContext) | RESOLVED | Still resolved |
| SPRINT5-CRITICAL-002 | PaymentGateway calls non-existent endpoints | RESOLVED | Still resolved |
| SPRINT5-CRITICAL-003 | store.user.role fallback to localStorage | RESOLVED | Still resolved |
| SPRINT5-CRITICAL-004 | App.tsx memory leak + stale closure | RESOLVED | Still resolved |

## Sprint 5 HIGH Defects — No Change

| ID | Description | Sprint 5 Status | Sprint E Status |
|----|-------------|----------------|-----------------|
| SPRINT5-HIGH-001 | No CSRF protection on mutating requests | RESOLVED (csrf.ts + httpClient.ts) | Still resolved |
| SPRINT5-HIGH-002 | No idempotency on checkout submission | RESOLVED (CheckoutPage.tsx) | Still resolved |
| SPRINT5-HIGH-003 | No rate limiting on auth endpoints | UNRESOLVED (outside Sprint E scope) | Still unresolved |
| SPRINT5-HIGH-004 | Form state loss on back-navigation | UNRESOLVED (outside Sprint E scope) | Still unresolved (PRE-004) |

---

**Total regressions: 0**  
**Total pre-existing gaps carried forward: 6**
