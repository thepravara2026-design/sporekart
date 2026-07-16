# Analytics Platform — Accessibility

## Compliance

Designed for WCAG 2.2 AA conformance.

## Keyboard Navigation

- All filter controls are native `<select>` and `<input>` elements
- Tab navigation buttons use `<button>` with `aria-current="page"`
- Sortable table headers have `cursor: pointer` and keyboard-triggerable `onClick`

## ARIA Attributes

- Tab nav: `aria-label="Analytics sections"`, `aria-current="page"` on active tab
- SVG charts: accessible as decorative images (role="img" not added — charts are presentational)
- Empty states: semantic heading hierarchy (h3)

## Color Contrast

- KPI tile variants use accessible color combinations with proper contrast ratios
- Risk indicators: high (red #dc2626), medium (yellow #ca8a04), low (green #16a34a)
- Performance labels: mapped to semantic variants (success/info/warning/danger)

## Screen Reader Support

- DataTable uses native `<table>` with `<thead>` and `<th>` elements
- Chart data should be provided as accompanying text for screen readers
- All interactive elements have visible focus states
