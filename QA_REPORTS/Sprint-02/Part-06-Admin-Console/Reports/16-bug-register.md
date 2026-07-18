# Bug Register — Admin Console

## New Bugs (Part 6)

| Bug ID | Module | Priority | Description | Status |
|--------|--------|----------|-------------|--------|
| BUG-ADM-001 | Auth | P0 | No authentication guard on /admin routes — any user (guest, customer, grower) can access any admin page directly via URL | Open |
| BUG-ADM-002 | Orders | P2 | Admin Orders page renders with empty body content (no DataGrid rendered) when accessed without auth | Open |

## Carryover Bugs

| Bug ID | Description | Severity | Status |
|--------|-------------|----------|--------|
| BUG-001 | Firefox: Complete mock API interception failure | P0 | Accepted |
| BUG-CHK-001 | Dashboard routes accessible without authentication | P0 | Open |
| BUG-CHK-002 | ARIA navigation hidden on mobile viewports | P2 | Open |
| BUG-ORD-003 | Admin orders page empty content (customer dashboard works) | P0 | Open |
| BUG-ORD-004 | Guest role not restricted on dashboard orders | P1 | Open |
| BUG-PAY-001 | /payment/success and /payment/failed routes render empty SPA shell | P2 | Open |
| BUG-PAY-002 | All /api/payments/* and /api/webhooks/* return SPA HTML instead of JSON | P2 | Open |

## Bug Statistics
| Category | Count |
|----------|-------|
| New P0 | 1 |
| New P2 | 1 |
| Carryover P0 | 3 |
| Carryover P1 | 1 |
| Carryover P2 | 3 |
| **Total** | **9** |
