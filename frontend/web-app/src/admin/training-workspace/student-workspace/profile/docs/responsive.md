# Student Profile — Responsive Design

## Breakpoints

| Breakpoint | Width   | Layout Behavior |
|------------|---------|-----------------|
| Desktop    | ≥1024px | Two-column grid (sidebar + main) |
| Tablet     | 768–1023px | Single column, reduced padding |
| Mobile     | 320–767px | Single column, full-width panels |

## Component Behavior

### ProfileHeader
- Desktop: avatar + info side by side
- Tablet/Mobile: stacked vertically, centered
- Stats row wraps to 2×2 grid on mobile

### ProfileCompletenessCard
- Full width at all breakpoints
- Section bars scale to container width

### ProfileMainPage grid
- Desktop: `sidebar (320px) + main (1fr)` using CSS Grid
- Tablet/Mobile: single column, `sidebar` above `main`

### ProfileNavigation (sub-nav)
- Desktop: horizontal scrollable row of buttons
- Mobile: wraps to multiple rows, full-width buttons

### ProfileEditPage form
- Desktop: 2-column field grid per section
- Mobile: single column, full-width fields

### ProfileInfoCard
- Desktop: label/value on same row (flex row)
- Mobile: label above value (flex column)

## Implementation Notes
- Uses existing CSS custom properties (`--space-*`, `--radius-*`, `--text-*`)
- No hardcoded pixel widths for layout containers
- `min-width: 320px` supported without overflow
- Touch targets ≥44px on mobile for all interactive elements
