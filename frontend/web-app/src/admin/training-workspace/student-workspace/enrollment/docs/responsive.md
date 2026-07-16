# Enrollment Platform — Responsive Design

## Breakpoints

| Breakpoint | Width   | Layout Behavior |
|------------|---------|-----------------|
| Desktop    | ≥1024px | Multi-column grids, full table |
| Tablet     | 768–1023px | Reduced columns, card view promoted |
| Mobile     | 320–767px | Single column, stacked panels |

## Component Behavior

### Dashboard Page
- Desktop: 4×2 stat widget grid, 2-column lower section (recent + batches)
- Tablet: 2×4 stat widget grid, stacked lower section
- Mobile: 1×8 stat widget grid, single column

### EnrollmentTable
- Desktop: full 7-column table
- Tablet/Mobile: horizontal scroll with `overflow-x: auto`
- Card view is preferred for mobile

### EnrollmentSearchFilter
- Desktop: inline search + dropdown + count
- Mobile: stacked full-width search, dropdown below

### EnrollmentPagination
- Desktop: inline left/right layout
- Mobile: centered stacked buttons, page size selector above

### Batch Management
- Desktop: 3-column grid (320px min)
- Tablet: 2-column grid
- Mobile: single column full-width

### Capacity Dashboard
- Desktop: 4-column widget grid, 3-column gauge grid
- Tablet: 2-column gauge grid
- Mobile: single column gauges

### Approval Queue
- Desktop: 2-column when detail selected
- Mobile: stacked single column

### Enrollment Timeline
- Desktop: 2-column (student list + timeline)
- Mobile: stacked (select student first, then timeline)

## Implementation
- Uses CSS custom properties for spacing
- No hardcoded pixel widths
- `min-width: 320px` supported
- Grid layouts use `auto-fill` and `minmax` for fluid sizing
- Horizontal scroll only on data tables — never on page layout
- Touch targets ≥44px on mobile
