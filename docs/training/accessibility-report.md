# Accessibility Report — Sprint 26 Part 8 (WCAG 2.2 AA)

## Verified
| Requirement | Implementation |
|-------------|----------------|
| Keyboard navigation | All interactive controls are native `<button>`/`<a>`/`<input>`/`<select>` |
| Accessible cards | `Card as="article"` with `aria-label`; title wrapped in link |
| Accessible carousels | `role="region"` + `aria-label` + focusable scroll container |
| Accessible filters | Labeled `<select aria-label>` + toggle `aria-pressed` |
| Accessible search | `role="searchbox"`, `aria-label` |
| Screen reader support | Landmarks (`nav`/`section`/`aside`/`table`), `aria-current` on active sort/page |
| ARIA labels | Present on icons, toggles, compare, bookmark, wishlist |
| Reduced motion | Token-driven transitions; respects `prefers-reduced-motion` via design tokens |
| Focus management | Visible focus rings via `--color-border-focus` |

## Notes
- Comparison table uses real `<table>` semantics for SR users.
- Non-color state: badges combine icon + text.
- Skip-link already present at app root (`sk-skip`).

## Verdict
Accessibility passed (AA-aligned). No `tabindex > 0`, no inaccessible custom widgets.
