# Communication Platform — Accessibility

## Compliance

Designed for WCAG 2.2 AA conformance.

## Keyboard Navigation

- All filter controls: native `<select>`, `<input>`, and `<button>` elements
- Section tabs: `<button>` with `aria-current="page"`
- Read/star toggles: `<button>` with `aria-label`
- Preference toggles: `<button>` with `aria-pressed`

## ARIA Attributes

- Section nav: `aria-label="Communication sections"`, `aria-current="page"` on active tab
- Read toggle: `aria-label="Mark as read/unread"`
- Star toggle: `aria-label="Star/Unstar"`
- Search input: `<label>` with screen reader only text

## Color Contrast

- Status badges: accessible color combinations (green/red/yellow/blue)
- Priority badges: critical=red, high=yellow, medium=blue, informational=green
- Read indicator: blue border on unread items (#2563eb)

## Screen Reader Support

- Inbox items: semantic structure with h4 for titles
- Announcement cards: clear heading hierarchy
- Timeline: sequential DOM order matches visual order
- Focus states: visible on all interactive elements
