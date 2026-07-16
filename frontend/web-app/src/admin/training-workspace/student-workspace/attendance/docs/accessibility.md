# Attendance Platform — Accessibility

## Standards

WCAG 2.2 AA compliant.

## Key Patterns

- **AttendanceStatusBadge**: Color + text label dual encoding
- **AttendanceTable**: Native `<table>` with `<thead>`, `<th>`, `aria-label` on rows, keyboard-navigable
- **CalendarView**: Color-coded cells have visible percentage text; holidays marked with 'H'
- **AnalyticsPanel**: Progress bars have adjacent percentage text labels
- **DashboardWidget**: Labels are `<span>` text, not icon-only
- **EmptyStates**: `<h3>` heading + `<p>` description for screen reader context
- **PolicyCard**: All threshold values are visible text

## Keyboard Navigation

- All interactive elements are native HTML
- Sub-nav uses `<button>` elements with `aria-current`
- Calendar navigation buttons have `aria-label`
- View mode toggles use `role="radiogroup"` with `aria-pressed`

## Color Contrast

- All text meets 4.5:1 ratio (AA)
- Status colors are supplemented with text labels
- Calendar heatmap uses green/yellow/red with percentage text
