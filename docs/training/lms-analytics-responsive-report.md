# LMS Analytics — Responsive Validation (Sprint 26 · Part 9)

## Strategy
- Layouts use a single `WidgetGrid` primitive built on CSS Grid
  `repeat(auto-fill, minmax(<min>, 1fr))`, so columns reflow automatically.
- Charts are rendered through `ResponsiveChart`, which measures the container
  with `ResizeObserver` and re-renders the fixed-size SVG at the available width.
- Tables are wrapped in an `overflow-x: auto` container to avoid layout breakage
  on narrow screens.

## Breakpoint Behavior
| Viewport         | KPI grid (min 220px) | Widget grid (min 320px) | Notes |
|------------------|----------------------|--------------------------|-------|
| ≥ 1440px (XL)    | 6 cols               | 3–4 cols                 | Full dashboard |
| 1024–1439px (L)  | 4–5 cols             | 3 cols                   | Comfortable |
| 768–1023px (M)   | 3 cols               | 2 cols                   | Training rail visible |
| 480–767px (S)    | 2 cols               | 1 col                    | Rail collapses (workspace behavior) |
| < 480px (XS)     | 1–2 cols             | 1 col                    | Charts scale to container |

## Validated Points
- No fixed pixel widths on containers; only `minmax` minimums.
- Charts never overflow horizontally (width is clamped to container).
- Toolbar wraps (`flex-wrap`) so filters stack on small screens.
- Section tabs wrap rather than horizontally overflow.

## Manual Check Matrix
| Page                 | XS | S | M | L | XL |
|----------------------|----|---|---|---|----|
| Executive Dashboard  | ok | ok| ok| ok| ok |
| Course Analytics     | ok | ok| ok| ok| ok |
| Enrollment Analytics | ok | ok| ok| ok| ok |
| Curriculum Analytics | ok | ok| ok| ok| ok |
| Resource Analytics   | ok | ok| ok| ok| ok |
| Saved Dashboards     | ok | ok| ok| ok| ok |
