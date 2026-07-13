# Responsive Foundation

## Breakpoint Tokens

| Token | Min-Width | Usage |
|-------|-----------|-------|
| `--breakpoint-xs` | 0 | Default (mobile-first) |
| `--breakpoint-sm` | 480px | Small tablet / large phone |
| `--breakpoint-md` | 768px | Tablet portrait |
| `--breakpoint-lg` | 1024px | Desktop / tablet landscape |
| `--breakpoint-xl` | 1280px | Wide desktop |
| `--breakpoint-2xl` | 1536px | Ultra-wide |

Breakpoints are **min-width (mobile-first)**. Max-width variants exist as `sm-down`, `md-down`, etc. for exceptional cases.

```tsx
import { useBreakpoint } from '@/design-system';

function Component() {
  const { isAboveMd, isBelowLg } = useBreakpoint();
  return isAboveMd ? <DesktopView /> : <MobileView />;
}
```

## Container Queries

Components declare `container-type: inline-size` to enable querying their own width rather than the viewport.

```css
.card {
  container-type: inline-size;
}

@container (min-width: 400px) {
  .card { display: flex; }
}
```

## Clamp Functions for Fluid Typography

All heading and display sizes use `clamp()` for fluid scaling:

```css
--font-size-h1: clamp(2rem, 4vw, 3rem);
--font-size-h2: clamp(1.5rem, 3vw, 2.25rem);
```

Formula used to generate clamp values: `clamp(minSize, preferredSize, maxSize)` where `preferredSize` is typically a viewport-relative unit.

## Responsive Grid Utilities

### `grid-auto-fit`

Creates a grid that auto-fills columns with a minimum item width, expanding to fit the container:

```css
.grid-auto-fit {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(var(--grid-min, 250px), 1fr));
  gap: var(--grid-gap, var(--space-4));
}
```

### `grid-auto-fill`

Similar to `auto-fit` but preserves track space even when empty:

```css
.grid-auto-fill {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(var(--grid-min, 250px), 1fr));
  gap: var(--grid-gap, var(--space-4));
}
```

## Usage Guidelines

- Always design **mobile-first** — start with single-column, add breakpoints for wider layouts.
- Use `useBreakpoint()` sparingly; prefer CSS `@media` queries for layout.
- Use container queries for reusable components that appear in varying-width contexts.
- Use `clamp()` for fluid typography — never set fixed `font-size` on headings.
- Maintain the `--breakpoint-*` tokens in `tokens/breakpoints.ts` — do not hardcode values.
