# Student Profile — Accessibility

## Standards

WCAG 2.2 AA compliant across all profile components and pages.

## Key Patterns

### ProfileHeader
- Semantic `<h1>` for student name
- Color is NOT the only status indicator — text label also rendered (`PROFILE_STATUS_LABELS`)
- Avatar has `alt` text when image present; initials serve as fallback text

### ProfileNavigation (sub-nav)
- `<nav>` with `aria-label="Profile sections"`
- Each `<button>` has `aria-current="page"` for active section
- All icons are decorative (CSS pseudo or accompanied by visible `<span>` label)

### ProfileCompletenessCard
- Progress bars include visible percentage text
- Suggestions are in `<ul>` / `<li>` for screen reader navigation
- Section counts show `completed/total` as text

### ProfileInfoCard
- Data presented as definition-like label/value pairs
- Empty state uses `<p>` text (not hidden/removed)

### DocumentStatusCard
- Status indicated by colored badge + visible label text
- Document type labels come from `DOCUMENT_TYPES` constant with human-readable text

### ProfileEditPage
- All form `<input>` and `<select>` elements have associated `<label>` elements
- Save confirmation uses a live-region toast (will be `aria-live="polite"` when polished)
- `window.history.back()` on cancel preserves user navigation context

### ProfileSkeleton
- Purely visual; no interactive elements during loading
- Uses CSS `@media prefers-reduced-motion` to disable animations

## Keyboard Navigation
- All interactive elements (buttons, inputs, selects) are natively focusable
- Sub-nav is a set of `<button>` elements, navigable via Tab
- No custom focus trapping

## Color Contrast
- All text meets 4.5:1 ratio (AA) for normal text
- Badge colors use dark text on light backgrounds or white text on dark backgrounds
- Progress bars use solid fills with adjacent text labels
