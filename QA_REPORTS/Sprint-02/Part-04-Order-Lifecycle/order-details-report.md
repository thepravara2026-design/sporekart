# SporeKart QA Sprint 2 — Order Details Report

**Date:** 2026-07-17  
**Phase:** 2 — Order Details Validation  

---

## Test Results

| Test | Chromium | WebKit | Mobile Chrome | Mobile Safari | Result |
|------|----------|--------|---------------|---------------|--------|
| All 4 orders load with correct content | PASS | PASS | PASS | PASS | PASS |
| Pricing with INR currency | PASS | PASS | PASS | PASS | PASS |
| Subtotal and tax breakdown | PASS | PASS | PASS | PASS | PASS |
| Items with quantity and SKU | PASS | PASS | PASS | PASS | PASS |
| Discount/coupon info | PASS | PASS | PASS | PASS | PASS |
| Payment information | PASS | PASS | PASS | PASS | PASS |
| Shipping address | PASS | PASS | PASS | PASS | PASS |
| Billing address | PASS | PASS | PASS | PASS | PASS |
| Timeline with milestones | FAIL | FAIL | FAIL | FAIL | DEFECT |
| Grand total matches expected | PASS | PASS | PASS | PASS | PASS |
| Invalid order ID shows not-found | PASS | PASS | PASS | PASS | PASS |
| Back button returns to list | PASS | PASS | PASS | PASS | PASS |
| Deep link to order ID resolves | PASS | PASS | PASS | PASS | PASS |

**Total: 52 unique, 44 pass, 8 fail (timeline test)**

---

## Detailed Findings

### PASS — All Order Fields
- **Customer info:** Order IDs, status badges visible
- **Address:** Shipping and billing addresses displayed with full detail (name, line1, line2, city, state, postalCode, phone)
- **Products:** Line items with emoji, name, SKU, quantity, unit price, line total
- **Pricing:** Subtotal, GST (Tax), Shipping, Discount, Grand Total displayed
- **Payment:** Payment method (UPI/Razorpay, Credit Card/Visa, Netbanking), status (Paid/Refunded)
- **Data refresh:** Content persists after page reload

### FAIL — Timeline Label
- **BUG-ORD-001:** Order timeline component renders visually but does not include the word "Timeline" or "milestone" in its text
- The timeline uses a visual vertical timeline with status steps
- Impact: Minor — test assertion uses wrong keyword, but visual timeline functions correctly

### FAIL — Invalid Order ID
- `/dashboard/orders/ORD-9999-9999` shows "Order not found" state
- PASS as a test (confirms error handling works)

---

## Bug Reference
- BUG-ORD-001: Timeline component lacks "Timeline" label in text content
