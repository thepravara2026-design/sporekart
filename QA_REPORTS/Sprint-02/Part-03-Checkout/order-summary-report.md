# SporeKart QA Sprint 2 — Order Summary Report

**Date:** 2026-07-17  
**Scope:** Order pricing, subtotal/tax, items/qty, discount, payment info, shipping address, data integrity  

---

## Test Results

| Test | Chromium | WebKit | Mobile Chrome | Mobile Safari | Status |
|------|----------|--------|---------------|---------------|--------|
| Order Shows Pricing (INR) | PASS | PASS | PASS | PASS | PASS |
| Order Shows Subtotal/Tax | PASS | PASS | PASS | PASS | PASS |
| Order Shows Items/Qty | PASS | PASS | PASS | PASS | PASS |
| Order Shows Discount | PASS | PASS | PASS | PASS | PASS |
| Order Shows Payment Info | PASS | PASS | PASS | PASS | PASS |
| Order Shows Shipping Address | PASS | PASS | PASS | PASS | PASS |
| Data Persists on Refresh | PASS | PASS | PASS | PASS | PASS |
| Different Order Loads | PASS | PASS | PASS | PASS | PASS |

**Total: 32 executions, 32 pass, 0 fail**

---

## Detailed Findings

### Order Pricing Display
- Order details at `/dashboard/orders/ORD-2026-8842` show INR pricing
- All orders display currency formatting with ₹ symbol

### Subtotal & Tax Breakdown
- Subtotal and tax information visible in order details
- Full pricing breakdown present

### Items & Quantities
- Each order line item shows product name, quantity, unit price
- SKU numbers displayed

### Discount/Coupon Display
- Discount or coupon information visible in order breakdown
- Indicates coupon/promotion awareness in the UI

### Payment Information
- Payment method displayed per order: UPI (Razorpay), Credit Card (Visa), UPI (GPay), Netbanking
- Payment status shown: Paid, Refunded

### Shipping Address
- Customer name and shipping address visible per order
- Ship-to information displayed in order cards

### Data Integrity
- **Refresh:** Order data persists after page reload
- **Different Orders:** Multiple order IDs load correctly (ORD-2026-8842, ORD-2026-5541)
- **Cross-browser:** Consistent behavior across all 4 browsers

---

## Recommendations

1. Order summary functionality is complete for display purposes (mock data)
2. Wire to real order data when backend API available
3. Add order placement/submission flow after cart implementation
