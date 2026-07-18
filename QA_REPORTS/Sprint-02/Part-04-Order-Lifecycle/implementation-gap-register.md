# SporeKart QA Sprint 2 — Implementation Gap Register (Order Lifecycle)

**Date:** 2026-07-17  
**Scope:** Gaps identified during Part 4 execution  

---

## New Order Lifecycle Gaps

### GAP-ORD-001 — Order Creation (Cart + Checkout + Payment)
| Field | Value |
|-------|-------|
| **Priority** | P0 — Critical |
| **Module** | Order Lifecycle — Creation |
| **Current State** | No cart, no checkout, no payment — order creation not possible |
| **Expected State** | Full order creation flow: add to cart → checkout → payment → order confirmation |
| **Business Impact** | Revenue Impact — Users cannot place orders |
| **Suggested Sprint** | Sprint 3 |
| **References** | GAP-CHK-001, GAP-CHK-002, GAP-CHK-006 |

### GAP-ORD-002 — Order Status State Machine
| Field | Value |
|-------|-------|
| **Priority** | P1 — High |
| **Module** | Order Lifecycle — Status |
| **Current State** | Statuses are static mock data (Processing, In Transit, Delivered, Refunded). No UI for status transitions. |
| **Expected State** | State machine: Pending → Confirmed → Processing → Packed → Shipped → Delivered → Cancelled/Failed/Returned/Refunded |
| **Business Impact** | Operational Impact — Cannot manage order fulfillment |
| **Suggested Sprint** | Sprint 4 |

### GAP-ORD-003 — Invoice Generation
| Field | Value |
|-------|-------|
| **Priority** | P1 — High |
| **Module** | Order Lifecycle — Documents |
| **Current State** | Invoice button exists but no download action |
| **Expected State** | Clicking invoice button generates and downloads PDF invoice |
| **Business Impact** | Customer Impact — Customers cannot download invoices for orders |
| **Suggested Sprint** | Sprint 3 |

### GAP-ORD-004 — Cancel Order
| Field | Value |
|-------|-------|
| **Priority** | P2 — Medium |
| **Module** | Order Lifecycle — Customer Actions |
| **Current State** | No cancel order button or flow |
| **Expected State** | Cancel button on Processing/Pending orders with confirmation dialog |
| **Business Impact** | Customer Impact — Customers cannot cancel orders |
| **Suggested Sprint** | Sprint 4 |

### GAP-ORD-005 — Reorder Functionality
| Field | Value |
|-------|-------|
| **Priority** | P2 — Medium |
| **Module** | Order Lifecycle — Customer Actions |
| **Current State** | "Reorder Items" button exists on order detail page but is non-functional |
| **Expected State** | Clicking reorder adds items to cart and navigates to checkout |
| **Business Impact** | Customer Experience Impact — Cannot easily reorder |
| **Suggested Sprint** | Sprint 4 |

### GAP-ORD-006 — Admin Order Management
| Field | Value |
|-------|-------|
| **Priority** | P1 — High |
| **Module** | Admin — Orders |
| **Current State** | `/admin/orders` returns empty page. No admin order management available. |
| **Expected State** | Admin can view, search, filter, and update order statuses |
| **Business Impact** | Operational Impact — Admin cannot manage orders |
| **Suggested Sprint** | Sprint 3 |

### GAP-ORD-007 — Audit Trail
| Field | Value |
|-------|-------|
| **Priority** | P2 — Medium |
| **Module** | Order Lifecycle — Auditing |
| **Current State** | No audit trail or change history visible for orders |
| **Expected State** | All order status changes logged with timestamp and actor |
| **Business Impact** | Compliance Impact — Cannot audit order changes |
| **Suggested Sprint** | Sprint 5 |

---

## Previously Registered Gaps (Relevant)

| Gap ID | Title | Priority |
|--------|-------|----------|
| GAP-001 | Product Catalog | P0 |
| GAP-002 | Product Details | P0 |
| GAP-003 | Shopping Cart | P0 |
| GAP-CHK-001 | Shopping Cart (Functional) | P0 |
| GAP-CHK-002 | Checkout Flow | P0 |
| GAP-CHK-003 | Address Management | P1 |
| GAP-CHK-004 | Guest Checkout | P1 |
| GAP-CHK-005 | Coupon Engine | P2 |
| GAP-CHK-006 | Payment Gateway | P0 |

---

## Summary

| Priority | New Gaps | Count |
|----------|----------|-------|
| P0 — Critical | GAP-ORD-001 | 1 |
| P1 — High | GAP-ORD-002, GAP-ORD-003, GAP-ORD-006 | 3 |
| P2 — Medium | GAP-ORD-004, GAP-ORD-005, GAP-ORD-007 | 3 |
| **Total New** | | **7** |
| **Legacy Gaps** | | **9** |
| **Grand Total** | | **16** |
