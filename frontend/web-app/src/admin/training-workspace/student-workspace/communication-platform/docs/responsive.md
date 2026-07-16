# Communication Platform — Responsive Design

## Breakpoints

- **Mobile (320px–639px):** Single column, stacked widgets, compact badges, tab filters wrap
- **Tablet (640px–1023px):** 2-column grid for widgets, inline filters
- **Desktop (1024px+):** Multi-column grids, full data display

## Grid Behaviour

- Metric cards: `auto-fill, minmax(140px, 1fr)` — reflows from 1→2→3+ columns
- Dashboard widgets: `auto-fill, minmax(360px, 1fr)` — reflows from 1→2 columns
- Message/Announcement cards: `auto-fill, minmax(320px, 1fr)`
- Filter bar: `flex-wrap: wrap` with search `min-width: 180px, max-width: 280px`

## Tab Navigation

- Section tabs: `flex-wrap: wrap` for overflow
- Inbox tabs: `flex-wrap: wrap` with live counts
- Timeline stage filter: `flex-wrap: wrap`

## Touch Targets

- All interactive controls ≥ 36px
- Read/star toggle buttons: 32px touch target
- Section tab buttons: `padding: 6px 14px`
