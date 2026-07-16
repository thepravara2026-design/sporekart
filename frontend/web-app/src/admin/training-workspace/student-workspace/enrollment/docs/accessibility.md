# Enrollment Platform — Accessibility

## Standards

WCAG 2.2 AA compliant across all enrollment components and pages.

## Key Patterns

### EnrollmentStatusBadge
- Color + text label dual encoding — never color alone
- Semantic `<span>` with inline styles, no hidden text

### EnrollmentTable
- Native `<table>` with `<thead>`, `<tbody>`, `<th>` elements
- Keyboard navigable rows: `tabIndex={0}`, Enter/Space keys trigger selection
- `role="row"` and `aria-label` on each row with enrollment ID
- Sort headers will use `aria-sort` when sorting is added

### EnrollmentSearchFilter
- `<input type="search">` with `aria-label="Search enrollments"`
- `<select>` with `aria-label="Filter by status"`
- Result count announced via visible text

### EnrollmentPagination
- `<nav>` with `aria-label="Pagination"`
- Page buttons have `aria-label="Page {n}"` and `aria-current="page"`
- Previous/Next have `aria-label` and `disabled` state

### ApprovalActionsPanel
- Buttons have `aria-label` for approve/reject/request-info
- Disabled state applied when action is not available
- Color is not the only indicator (text labels: "Approve", "Reject", "Request Info")

### CapacityGauge
- SVG with text label showing percentage
- Color is supplemented by adjacent numeric values

### EmptyStates
- Icon, heading `<h3>`, and descriptive `<p>` for screen reader context
- Clear Filters button when applicable

### Skeletons
- Purely visual; no interactive elements during loading
- Respect `prefers-reduced-motion` via CSS transitions

## Keyboard Navigation
- All interactive elements are native HTML (buttons, inputs, selects)
- Tab order follows visual order
- No custom focus trapping or tabindex manipulation beyond row navigation
- Sub-navigation tabs use native `<button>` elements

## Color Contrast
- All text meets WCAG AA 4.5:1 ratio
- Status badge colors use tinted backgrounds (18% opacity) with full-color text
- Gauges use distinct colors (green/yellow/red) with percentage text overlay
- No information conveyed by color alone
