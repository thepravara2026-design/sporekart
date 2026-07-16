# Certificate Platform — Responsive Design

## Breakpoints

| Breakpoint | Target | Behavior |
|-----------|--------|----------|
| ≥ 1024px | Desktop | Full multi-column grids, side-by-side wallet/transcript panes |
| 768–1023px | Tablet | 2-column grids, stacked verification layout |
| 320–767px | Mobile | Single column, stacked wallet list/content, scrollable tables |

## Key Responsive Patterns

- **Widget Grid**: `repeat(auto-fill, minmax(160px, 1fr))` — collapses from 5+ columns to 2 to 1
- **Card Grids**: `repeat(auto-fill, minmax(280px, 1fr))` and `minmax(320px, 1fr)`
- **Wallet Page**: `grid-template-columns: 300px 1fr` — collapses to single column below 768px
- **Verification Page**: `grid-template-columns: 1fr 320px` — stacks sidebar below main content on mobile
- **Transcript Page**: `grid-template-columns: 280px 1fr` — student list collapses above content on mobile
- **Analytics**: 2-column grids collapse to single column below 768px
- **Tables**: `overflow-x: auto` with horizontal scroll on narrow viewports
- **Sub-navigation tabs**: `flexWrap: wrap`
