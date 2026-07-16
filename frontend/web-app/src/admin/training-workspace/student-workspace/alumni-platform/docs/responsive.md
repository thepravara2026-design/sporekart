# Responsive Design

## Breakpoints
- Desktop (>1024px): Full grid layouts, multi-column dashboards
- Laptop (768–1024px): Reduced column counts, still comfortable
- Tablet (480–768px): Single column dashboards, stacked filters
- Mobile (320–480px): Compact cards, horizontal scroll for tabs, stacked layout

## Implementation
- All layouts use CSS `grid` with `repeat(auto-fill, minmax(Npx, 1fr))` for responsive columns
- SharedFilters uses `flexWrap: 'wrap'` for filter dropdowns
- Navigation tabs use `flexWrap: 'wrap'` for mobile overflow
- Pagination uses `flexWrap: 'wrap'` for info/controls stacking
- Minimum touch target: 32x32px for interactive elements
