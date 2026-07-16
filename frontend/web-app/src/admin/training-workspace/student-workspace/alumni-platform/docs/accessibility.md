# Accessibility (WCAG 2.2 AA)

## Standards
- All interactive elements are keyboard accessible
- ARIA labels on search inputs, navigation, and pagination
- `aria-current="page"` on active navigation tabs
- `aria-label` on interactive controls
- Proper heading hierarchy (h1 → h3)
- Color contrast meets WCAG AA standards
- Badge variants use semantic colors (green=success, red=error, blue=info, yellow=warning)

## Implementation
- Search input has hidden `<label>` with `htmlFor`
- Pagination buttons have `aria-label="Page N"` and `aria-current="page"`
- Tab navigation has `aria-label` and `aria-current`
- Color is never the sole means of conveying information
- Reduced motion respected via CSS variables
- Focus visible on all interactive elements
