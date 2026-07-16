# Learning Progress Platform — Responsive Design

## Breakpoints

| Breakpoint | Target | Behavior |
|-----------|--------|----------|
| ≥ 1024px | Desktop | Full multi-column grid layouts, side-by-side panels |
| 768–1023px | Tablet | 2-column grids, compact tables, stacked sections |
| 320–767px | Mobile | Single column, stacked cards, scrollable tables |

## Key Responsive Patterns

- **Widget Grid**: `repeat(auto-fill, minmax(160px, 1fr))` — collapses from 4+ columns on desktop to 2 on tablet to 1 on mobile
- **Card Grids**: `repeat(auto-fill, minmax(320px, 1fr))` — responsive card layouts for competencies, milestones, certification readiness
- **Analytics Panel**: 2-column grid collapses to single column below 768px
- **Tables**: `overflow-x: auto` with horizontal scroll on narrow viewports
- **Filter Bar**: Flexbox with `flexWrap: wrap` — filters stack vertically on mobile
- **Timeline**: Vertical layout works naturally at all widths
- **Sub-navigation tabs**: `flexWrap: wrap` — tabs wrap to multiple rows on narrow screens

## Testing

- All pages verified at 320px, 480px, 768px, 1024px, 1440px
- No horizontal scroll on any page at any breakpoint (except data tables which are scrollable)
- Touch targets ≥ 44px on mobile
