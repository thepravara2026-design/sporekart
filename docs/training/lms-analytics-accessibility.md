# LMS Analytics — Accessibility Report (Sprint 26 · Part 9)

## Standard
Targets WCAG 2.1 AA, consistent with the Enterprise Design System.

## Implemented Measures
| Area                | Measure                                                                 |
|---------------------|------------------------------------------------------------------------|
| Charts              | Reused DS charts expose `role="img"` + `aria-label` and `<title>/<desc>`|
| Chart wrappers      | `ResponsiveChart` forwards an `ariaLabel` to a `role="img"` container   |
| Widget panels       | `WidgetCard` renders a `<section aria-label>` landmark per widget       |
| Tables              | `AnalyticsTable` uses `<caption>`, `<th scope="col">`, semantic rows    |
| Search + filters    | `AnalyticsToolbar` wrapped in `role="search"`; every control has a `<label>` |
| Search input        | Explicit `htmlFor`/`id` association + `type="search"`                   |
| Export menu         | `aria-haspopup="menu"`, `aria-expanded`, `role="menu"`/`menuitem`       |
| Section tabs        | `<nav aria-label="Analytics sections">` with `aria-current` via `NavLink`|
| Future items        | Disabled tabs use non-interactive `<span>` + `title="Coming soon"`      |
| Color               | Series colors use design-system data-viz tokens (contrast-managed)     |

## Keyboard
- All controls are native `<button>`, `<select>`, `<input>`, `<a>` — fully tab-navigable.
- Export menu toggles with the button; menu items are focusable buttons.
- No custom key traps introduced.

## Screen Reader Notes
- Each widget is announced by its `aria-label` (widget title).
- Charts announce their purpose via `ariaLabel` passed to every `ResponsiveChart`.
- Tables announce caption + column headers.

## Known Limitations (Mock Mode)
- Chart data points are not individually keyboard-focusable (inherited DS behavior).
- Live-region announcements for filter changes are deferred to a later phase.
