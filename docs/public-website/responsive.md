# Public Website — Responsive Strategy

## Breakpoints (Design System v1.0.0 tokens)
| Viewport | Width | Notes |
| --- | --- | --- |
| Desktop | 1280px | Default canvas |
| Laptop | 1024px | Collapsed paddings |
| Tablet | 768px | Stacked grids begin |
| Mobile | 375px | Single column, mobile drawer nav |

## Approach
- Layout uses CSS Grid with `repeat(auto-fit, minmax(...))` so columns reflow without
  media queries where possible.
- `PublicHeader` switches to a mobile drawer below the primary-nav breakpoint: the
  hamburger button reveals a vertical `PublicNav` + quick links. The drawer closes on
  `Escape` and outside-click.
- `PublicContentContainer` caps content width via `--container-*` tokens and centers it.
- `PublicFooter` columns use `auto-fit` grids and wrap naturally.

## Preview Environment
The design-system `ResponsivePreview` component provides a viewport switcher
(desktop / laptop / tablet / mobile) that renders children inside a fixed-width frame.
All five public preview routes use it:

- `/preview/public-layout` — full shell at desktop width.
- `/preview/public-header` — header at laptop width (resize to trigger drawer).
- `/preview/public-footer` — footer at desktop width.
- `/preview/public-navigation` — vertical nav at mobile width.
- `/preview/public-seo` — SEO component (no frame needed).

## Acceptance
- [x] Public layout renders correctly across all four viewports.
- [x] Header collapses to a usable mobile drawer.
- [x] Footer and sections reflow without horizontal overflow.
- [x] Preview routes allow design review at each breakpoint.
