# Learning Progress Platform — Accessibility (WCAG 2.2 AA)

## Compliance

All pages and components meet WCAG 2.2 AA standards.

## Implementation Details

### ARIA Attributes
- Navigation tabs use `aria-current="page"` on active tab
- Select filters have descriptive `aria-label` attributes
- Sort/filter controls use `aria-pressed` for toggle state
- Role `button` on clickable rows/cards
- `role="radiogroup"` on view mode toggle groups

### Keyboard Navigation
- All interactive elements reachable via Tab key
- Enter/Space activates clickable rows and cards
- Focus visible on all controls
- No keyboard traps

### Color and Contrast
- Status colors use semantic color tokens (success=green, danger=red, warning=yellow, info=blue)
- All text meets 4.5:1 contrast ratio against backgrounds
- Color is never the sole differentiator (labels accompany all color-coded elements)

### Screen Reader Support
- Descriptive `aria-label` on all icon-only buttons
- Screen reader announcements for filter changes
- Empty states provide context-aware messages
- Progress percentages announced alongside visual bars

### Touch Targets
- All interactive elements ≥ 44×44px on touch devices
- Adequate spacing between clickable elements
