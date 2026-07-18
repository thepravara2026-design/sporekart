# SporeKart QA Sprint 2 — Coupon / Promotion Report

**Date:** 2026-07-17  
**Scope:** Discount display, coupon code entry, promotion application  

---

## Test Results

| Area | Test | Status | Notes |
|------|------|--------|-------|
| Discount Display | Order shows discount/coupon text | PASS | Order details mention "Discount" or "Coupon" |
| Coupon Code Entry | N/A | NOT APPLICABLE | No coupon input field in checkout |
| Promotion Application | N/A | NOT APPLICABLE | No promotion engine |
| Discount Calculation | N/A | NOT APPLICABLE | No real pricing engine |

---

## Detailed Findings

### Discount Display
- **Status:** PASS
- Order detail pages contain references to discounts/coupons in the order breakdown
- This is passive display only — mock data includes discount line items

### Coupon Functionality
- **Status:** NOT APPLICABLE / IMPLEMENTATION GAP
- No checkout flow exists where coupons could be applied
- No coupon code input field exists in the application
- No promotion/discount engine implemented

---

## Recommendations

1. Implement coupon input field in checkout flow (when cart exists)
2. Wire promotion engine with mock validation
3. Display discount breakdown in order summary
