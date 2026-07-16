# Analytics Platform — Responsive Design

## Breakpoints

- **Mobile (320px–639px):** Single column, stacked widgets, hidden chart value labels, compact metric cards
- **Tablet (640px–1023px):** 2-column grid for widgets, inline filters
- **Desktop (1024px+):** Multi-column grids, full data tables, visible chart labels

## Grid Behaviour

- Metric cards: `auto-fill, minmax(140px, 1fr)` — reflows from 1→2→3+ columns
- Dashboard widgets: `auto-fill, minmax(360px, 1fr)` — reflows from 1→2 columns
- KPI tiles: `auto-fill, minmax(160px, 1fr)`
- Chart widths: 100% SVG with `viewBox` for intrinsic scaling
- DataTable: `overflow-x: auto` with horizontal scroll on narrow screens

## Filter Bar

- Search bar: `min-width: 200px, max-width: 320px` with `flex: 1`
- Dropdowns wrap to next line on mobile using `flex-wrap: wrap`

## Touch Targets

- All filter controls ≥ 36px tap target
- Tab navigation buttons: `padding: 6px 14px` with sufficient spacing
