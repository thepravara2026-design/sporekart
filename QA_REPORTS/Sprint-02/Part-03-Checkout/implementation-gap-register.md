# SporeKart QA Sprint 2 — Implementation Gap Register (Checkout)

**Date:** 2026-07-17  
**Scope:** Checkout-specific gaps identified during Part 3 execution  

---

## New Checkout Gaps

### GAP-CHK-001 — Shopping Cart (Functional)
| Field | Value |
|-------|-------|
| **Priority** | P0 — Critical |
| **Module** | Checkout — Cart |
| **Current State** | `/cart` renders a Navigation Prototype placeholder page with "Cart — empty panel". No cart state, no add-to-cart, no quantity management, no cart persistence. |
| **Expected State** | Functional cart with product line items, quantities, pricing, and proceed-to-checkout |
| **Blocked By** | GAP-001 (Product Catalog), GAP-002 (Product Details) |
| **Business Impact** | Revenue Impact — Users cannot add products to cart or proceed to checkout |
| **Suggested Sprint** | Sprint 3 |

### GAP-CHK-002 — Checkout Flow
| Field | Value |
|-------|-------|
| **Priority** | P0 — Critical |
| **Module** | Checkout — Flow |
| **Current State** | `/checkout` renders a Navigation Prototype placeholder page with "Checkout — empty panel". No checkout steps, no address selection, no shipping selection, no payment form. |
| **Expected State** | Multi-step checkout: cart review → address → shipping → payment → confirmation |
| **Blocked By** | GAP-CHK-001 (Cart), GAP-003 (Cart in Master Register) |
| **Business Impact** | Revenue Impact — Core purchase flow absent |
| **Suggested Sprint** | Sprint 3 |

### GAP-CHK-003 — Address Management (Functional)
| Field | Value |
|-------|-------|
| **Priority** | P1 — High |
| **Module** | Checkout — Address |
| **Current State** | `/dashboard/addresses` and `/dashboard/addresses/new` render placeholder pages. No CRUD operations for addresses. |
| **Expected State** | Full address management: create, read, update, delete addresses with form validation |
| **Blocked By** | None (design system has address form patterns) |
| **Business Impact** | Customer Blocking — Cannot save or manage shipping addresses |
| **Suggested Sprint** | Sprint 3 |

### GAP-CHK-004 — Guest Checkout
| Field | Value |
|-------|-------|
| **Priority** | P1 — High |
| **Module** | Checkout — Guest |
| **Current State** | No guest checkout flow exists |
| **Expected State** | Guest users can complete a purchase without registration |
| **Blocked By** | GAP-CHK-002 (Checkout Flow) |
| **Business Impact** | Revenue Impact — Guest users cannot purchase |
| **Suggested Sprint** | Sprint 4 |

### GAP-CHK-005 — Coupon / Promotion Engine
| Field | Value |
|-------|-------|
| **Priority** | P2 — Medium |
| **Module** | Checkout — Promotions |
| **Current State** | No coupon input field or promotion engine. Discount display in order details is mock data only. |
| **Expected State** | Coupon code entry, validation, and discount calculation |
| **Blocked By** | GAP-CHK-002 (Checkout Flow) |
| **Business Impact** | Marketing Impact — Cannot run promotional campaigns |
| **Suggested Sprint** | Sprint 4 |

### GAP-CHK-006 — Payment Gateway Integration
| Field | Value |
|-------|-------|
| **Priority** | P0 — Critical |
| **Module** | Checkout — Payment |
| **Current State** | No payment form. Razorpay referenced in mock order data but not integrated. |
| **Expected State** | Payment form with gateway integration (Razorpay), success/failure handling |
| **Blocked By** | GAP-CHK-002 (Checkout Flow) |
| **Business Impact** | Revenue Impact — Cannot accept payments |
| **Suggested Sprint** | Sprint 3 |

---

## Previously Registered Gaps (Relevant to Checkout)

From Master Feature Implementation Register (`QA_REPORTS/Sprint-02/Governance/feature-implementation-register.md`):

| Gap ID | Title | Priority | Status |
|--------|-------|----------|--------|
| GAP-001 | Product Catalog / Listing Page | P0 — Critical | Unchanged |
| GAP-002 | Product Detail Page | P0 — Critical | Unchanged |
| GAP-003 | Shopping Cart | P0 — Critical | **Updated** — Route now exists as placeholder (was 404) |
| GAP-004 | Product Search | P1 — High | Unchanged |
| GAP-005 | Product Filters | P1 — High | Unchanged |
| GAP-006 | Product Sorting | P1 — High | Unchanged |
| GAP-007 | Role Switcher UI | P1 — High | Unchanged |
| GAP-008 | Loading Skeletons | P2 — Medium | Unchanged |

---

## Summary

| Priority | New Gaps | Existing Gaps | Total |
|----------|----------|---------------|-------|
| P0 — Critical | 2 (Cart, Checkout, Payment) | 3 (Catalog, Details, Cart) | 5 |
| P1 — High | 2 (Address, Guest) | 4 (Search, Filters, Sorting, Role Switcher) | 6 |
| P2 — Medium | 1 (Coupon) | 1 (Loading Skeletons) | 2 |
| **Total** | **5** | **8** | **13** |
