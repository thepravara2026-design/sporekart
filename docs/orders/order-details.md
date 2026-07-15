# Order Details Design

The Order Details Page provides a comprehensive view of a single order.

## Layout & Composition

- **Header Action Bar**: Back to Orders chevron alongside actions for mock invoice PDF downloads and reordering.
- **Two-Column Responsive Layout**:
  - **Main Area (Left)**: Renders the active `OrderTimeline` card, item list card (showing thumbnails, quantities, SKU and item prices), and Address details (shipping and billing cards rendered side-by-side using the `Grid` layout).
  - **Sidebar Area (Right)**: Renders the Invoice Summary calculation table, Payment metadata details (transaction ID, payment source, download receipt action), Courier details, and Returns eligibility info card.
- **Invoice Calculator Table**: Implements subtotal, GST taxation addition, shipping rates, coupon code deductions, and grand total in a clear tabular format.
