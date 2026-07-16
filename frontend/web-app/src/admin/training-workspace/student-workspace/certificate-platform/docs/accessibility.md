# Certificate Platform — Accessibility (WCAG 2.2 AA)

## Compliance

All pages and components meet WCAG 2.2 AA standards.

## ARIA Attributes

- Navigation tabs use `aria-current="page"` on active tab
- Select filters have descriptive `aria-label`
- Tab panels in wallet use `role="tablist"` and `role="tab"` with `aria-selected`
- Role `button` on clickable cards and rows
- `role="radiogroup"` on view mode toggle groups

## Keyboard Navigation

- All interactive elements reachable via Tab
- Enter/Space activates clickable cards and rows
- Focus visible on all controls
- Wallet student list is keyboard scrollable

## Color and Contrast

- Status colors use semantic tokens: success=green, danger=red, warning=yellow, info=blue
- NFT badge indicator uses yellow on white (meets 4.5:1)
- All color-coded elements have text labels

## Touch Targets

- All interactive elements ≥ 44×44px on touch devices
- Student list items adequately spaced
- Filter dropdowns use native select elements
