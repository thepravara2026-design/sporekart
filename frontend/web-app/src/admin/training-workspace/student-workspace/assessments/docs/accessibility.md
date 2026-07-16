# Assessment Platform — Accessibility

## Standards

WCAG 2.2 AA compliant.

## Key Patterns

- **AssessmentStatusBadge**: Color + text label dual encoding for 11 statuses
- **AssessmentTable**: Native `<table>` with `<thead>`, `<th>`, keyboard-navigable rows
- **AssessmentCard**: Interactive cards with `role="button"`, `tabIndex`, keyboard handlers
- **AssessmentTimeline**: Visual timeline with text labels, completed/pending indicators
- **AnalyticsPanel**: Progress bars have adjacent count labels, color-coded scores have text
- **DashboardWidget**: Labels are visible `<span>` text, not icon-only
- **EmptyStates**: `<h3>` heading + `<p>` description for screen reader context
- **ResultCard**: Score, grade, percentage all visible text; status color + text label
- **QuestionCategoryCard**: All outcomes, tags, stats are visible text

## Keyboard Navigation

- All interactive elements are native HTML or have keyboard handlers
- Sub-nav uses `<button>` elements with `aria-current`
- View mode toggles use `role="radiogroup"` with `aria-pressed`
- Search and filter controls are native `<input>` and `<select>` elements

## Color Contrast

- All text meets 4.5:1 ratio (AA)
- Status colors supplemented with text labels
- Performance scores use green/amber/red with visible percentage
- Timeline completed/incomplete distinguished by color + text
