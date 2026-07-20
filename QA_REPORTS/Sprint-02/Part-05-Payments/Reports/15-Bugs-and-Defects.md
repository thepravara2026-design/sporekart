# Bugs & Defects — Part 5 Payment Gateway

## New Bugs Found in Part 5

### BUG-PAY-001: `/payment/success` and `/payment/failed` routes render empty SPA shell
- **Severity:** P2
- **Description:** Both `/payment/success` and `/payment/failed` routes exist as valid SPA routes but contain no payment-specific content — they render only the navigation sidebar layout.
- **Impact:** If these routes were intended for post-payment redirect, they would show blank/confusing pages to users.
- **Evidence:** Body text contains only navigation links, no payment result messaging.

### BUG-PAY-002: All `/api/payments/*` and `/api/webhooks/*` endpoints return SPA HTML instead of JSON
- **Severity:** P2
- **Description:** Every payment-related API endpoint returns the Vite SPA index.html (200 OK) rather than JSON or proper error responses. This means the frontend is serving catch-all for these routes.
- **Impact:** Confusing for development/debugging — no clear indication of missing API implementation.
- **Evidence:** All checked endpoints return 200 with `Content-Type: text/html`.

## Carryover Bugs (Pre-existing)

| Bug ID | Description | Severity | Status |
|--------|-------------|----------|--------|
| BUG-001 | Firefox: Complete mock API interception failure (all tests timeout at 30s) | P0 | Accepted |
| BUG-CHK-001 | Dashboard routes (orders) accessible without authentication | P0 | Open |
| BUG-CHK-002 | ARIA navigation hidden on mobile viewports | P2 | Open |
| BUG-ORD-003 | Admin orders page returns empty content | P0 | Open |
| BUG-ORD-004 | Guest role not restricted on dashboard orders | P1 | Open |

## New Implementation Gaps in Part 5

| Gap ID | Description | Severity | Affected Phases |
|--------|-------------|----------|-----------------|
| GAP-PAY-001 | No cart/checkout UI exists for payment initiation | P0 | Phase 1 |
| GAP-PAY-002 | No Razorpay checkout integration or gateway session | P0 | Phase 1, 3 |
| GAP-PAY-003 | No payment method selection UI (UPI, Card, Wallet, EMI, COD) | P0 | Phase 2 |
| GAP-PAY-004 | No payment success/callback handling | P0 | Phase 3 |
| GAP-PAY-005 | No payment failure handling or retry mechanism | P1 | Phase 4 |
| GAP-PAY-006 | No idempotency or duplicate payment prevention | P1 | Phase 5 |
| GAP-PAY-007 | No webhook endpoints or signature verification | P1 | Phase 6 |
| GAP-PAY-008 | No real-time payment-to-order synchronization | P1 | Phase 7 |
| GAP-PAY-009 | No transaction tampering or parameter validation | P2 | Phase 8 |
