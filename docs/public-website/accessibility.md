# Public Website — Accessibility

## Landmarks & Semantics
- `<header role="banner">` in `PublicHeader`.
- `<nav aria-label="Primary">` in `PublicNav`; footer `<nav aria-label="...">` per column.
- `<main id="main">` is the skip-link target (`#main` skip link lives in `App.tsx`).
- `<footer role="contentinfo">` in `PublicFooter`.
- Breadcrumb uses `<nav aria-label="Breadcrumb">` + `<ol>` with `aria-current="page"` on the last item.

## Keyboard & Focus
- Mobile drawer: toggled by a labeled button (`aria-expanded`, `aria-label`),
  closes on `Escape` and outside pointer-down. Focus is not trapped (drawer is non-modal
  by design in this foundation; a later part may add focus trap for the modal variant).
- All links are native `<a>` (react-router `Link`/`NavLink`) — standard focus order.
- `Icon` receives `aria-label` for meaningful glyphs; decorative-only icons should omit it.

## Colour & Contrast
- All colours come from Design System tokens. Text pairs use `--color-text-*` on
  `--color-bg-surface-*` which meet WCAG AA in the default and high-contrast themes.
- Active nav uses `--color-text-accent` with `font-weight: 600` (not colour alone).

## Reduced Motion / Zoom
- Transitions are short (120–200ms). No autoplay. Layout uses relative units and reflows
  on zoom via `auto-fit` grids and `maxWidth: 100%`.

## Follow-ups
- Add visible focus-visible rings via token-driven outline (later part).
- Audit with the Design System `AccessibilityCenter` once content pages exist.
