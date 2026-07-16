# Assignment Platform — Accessibility

## Standards

WCAG 2.2 AA compliant.

## Key Patterns

- **AssignmentStatusBadge**: Color + text label dual encoding for 11 statuses
- **AssignmentTable**: Native `<table>` with `<thead>`, `<th>`, keyboard-navigable rows
- **AssignmentCard**: Interactive cards with `role="button"`, `tabIndex`, keyboard handlers
- **AssignmentTimeline**: Visual timeline with text labels, completed/pending indicators
- **AnalyticsPanel**: Progress bars have adjacent count labels
- **DashboardWidget**: Labels are visible `<span>` text, not icon-only
- **EmptyStates**: `<h3>` heading + `<p>` description for screen reader context
- **EvaluationCard**: All scores, grades, and feedback are visible text
- **SubmissionCard**: Status, plagiarism score color-coded + text label

## Keyboard Navigation

- All interactive elements are native HTML
- Sub-nav uses `<button>` elements with `aria-current`
- View mode toggles use `role="radiogroup"` with `aria-pressed`
- Search and filter controls are native `<input>` and `<select>` elements
- Pagination buttons have `aria-label` (First, Previous, Next, Last)

## Color Contrast

- All text meets 4.5:1 ratio (AA)
- Status colors are supplemented with text labels
- Progress bars in analytics have numeric count labels
- Deadline dates use red color + text for overdue indicator
