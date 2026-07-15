# Inventory Layout

`InventoryWorkspaceLayout` is a self-contained shell that mirrors the admin app shell using design tokens only (no global-shell modification).

## 1. Regions

1. **Header** — icon + title/subtitle (left); role switcher + Settings + Help (right).
2. **Breadcrumbs** — `Admin / Inventory / <section>`.
3. **Search + Banner row** — `SearchComponent` (global inventory search) + `WorkspaceBanner`.
4. **Body** — sidebar (desktop) or top nav (mobile) + `<main id="inventory-main">` content region.
5. **Activity aside** — placeholder "Activity feed coming soon".
6. **Footer** — "SporeKart Inventory · Mock Mode" + Profile control.

## 2. Token Usage

All styling uses CSS custom properties — no hardcoded colours/spacing:

- Surfaces: `var(--color-surface)`, `var(--color-surface-hover)`, `var(--color-bg-surface-default)`
- Borders/text: `var(--color-border)`, `var(--color-text-primary|secondary|tertiary)`
- Brand/state: `var(--color-primary)`, `var(--color-primary-alpha)`, `var(--color-success|warning|danger|info)`
- Radius: `var(--radius-lg|md|sm|badge)`
- Typography: `var(--text-h2|body|body-sm|caption)`
- Gap: `var(--space-component-gap)`

## 3. Responsive Behaviour

- `useInventoryResponsive()` exposes `isMobile` (<768px), `isTablet`, `isDesktop`, and `width`.
- The layout switches `flex-direction` and the sidebar width based on `isMobile`: desktop shows a 240px docked sidebar; mobile shows a full-width top nav.
- The metric/status/summary grids use `grid-template-columns: repeat(auto-fit, minmax(Npx, 1fr))` so they reflow fluidly between 320px and 1920px.

## 4. Dark Mode

The layout does not itself define theme tokens. The preview app injects a `[data-theme="dark"]` block that overrides the same `--color-*` variables; because the layout consumes tokens (not hardcoded values), it adapts automatically.

## 5. Accessibility

- `<main id="inventory-main" tabIndex={-1}>` is the focus target after section switches.
- Sidebar buttons use `aria-current="page"` for the active section.
- The search region, banner, and activity aside have `aria-label`s.
- All interactive controls are real `<button>` / `<select>` elements with visible focus.
