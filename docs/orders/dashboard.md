# Orders Dashboard Design

The Orders Dashboard is designed to be the central workspace for customer orders.

## Visual Design Details

- **Header Metrics Grid**: Uses the design-system `Card` and `Icon` components to display:
  - Total Spend (Paid orders aggregated).
  - Active Orders count (In Transit, Dispatched, etc.).
  - Delivered count.
  - Returned & Refunded counts.
- **AI Assistant Context Banner**: Designed with a soft green gradient (`linear-gradient(135deg, var(--color-bg-primary-weak) 0%, #f7f9f3 100%)`) and sparkling icon, giving contextual growth prep tips matching active order contents.
- **Search & Filters Control Row**:
  - Filter Tabs: Placed on a light background container, supporting smooth transition transitions between active states.
  - Search Input: Absolute-positioned icon, styled borders, clear captioning, matches design system standards.
- **List States**:
  - Renders detailed skeletons (`ShimmerLoader` + `Card` geometry) to minimize visual layout shift (CLS).
  - Clean descriptive empty state when no queries match.
