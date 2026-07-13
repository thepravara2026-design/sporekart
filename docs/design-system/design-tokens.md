# Design Tokens

## Architecture

```
┌──────────────────────────────────────────────────┐
│             Primitive Tokens (raw values)         │
│  color.gray.50  spacing.4  font.size.sm          │
└──────────────┬───────────────────────────────────┘
               │ maps to
┌──────────────▼───────────────────────────────────┐
│             Semantic Tokens (aliases)             │
│  color.bg.primary   color.text.body              │
└──────────────┬───────────────────────────────────┘
               │ overrides per component
┌──────────────▼───────────────────────────────────┐
│           Component Tokens (scoped)               │
│  button.bg.hover   card.shadow                   │
└──────────────────────────────────────────────────┘
```

## Primitive Tokens

| Category | Token Pattern | Example |
|----------|---------------|---------|
| Color | `--color-{family}-{shade}` | `--color-gray-50`, `--color-blue-600` |
| Typography | `--font-size-{scale}` | `--font-size-sm`, `--font-size-lg` |
| Spacing | `--space-{scale}` | `--space-1` (4px), `--space-4` (16px) |
| Radius | `--radius-{scale}` | `--radius-sm`, `--radius-md`, `--radius-full` |
| Elevation | `--elevation-{level}` | `--elevation-1`, `--elevation-3` |
| Breakpoints | `--breakpoint-{name}` | `--breakpoint-sm`, `--breakpoint-lg` |
| Border | `--border-width-{scale}` | `--border-width-1`, `--border-width-2` |
| Opacity | `--opacity-{pct}` | `--opacity-50`, `--opacity-80` |
| Animation | `--duration-{scale}`, `--easing-{name}` | `--duration-200`, `--easing-in-out` |
| Sizing | `--size-{scale}` | `--size-8` (32px), `--size-12` (48px) |
| Z-Index | `--z-{layer}` | `--z-dropdown`, `--z-modal` |

## Semantic Alias Tokens

```css
--color-bg-primary: var(--color-white);
--color-bg-secondary: var(--color-gray-50);
--color-text-body: var(--color-gray-900);
--color-text-muted: var(--color-gray-500);
--color-border-default: var(--color-gray-200);
--color-border-focus: var(--color-blue-500);
```

These change value per theme (light vs dark).

## Component-Specific Tokens

```css
--button-bg-primary: var(--color-blue-600);
--button-bg-hover: var(--color-blue-700);
--card-shadow: var(--elevation-2);
```

## Theme Override Tokens

High-contrast mode swaps semantic tokens to meet WCAG AAA:

```css
[data-theme="high-contrast"] {
  --color-text-body: var(--color-black);
  --color-border-default: var(--color-gray-700);
}
```

## Usage

```css
.my-element {
  background-color: var(--color-bg-primary);
  color: var(--color-text-body);
  padding: var(--space-4);
  border-radius: var(--radius-md);
  box-shadow: var(--elevation-1);
}
```

```tsx
// In JS via theme object
import { tokens } from '@/design-system/tokens';
const style = { padding: tokens.space[4] };
```

## Naming Conventions

- `--{category}-{modifier}-{variant}` for primitives
- `--{category}-{property}-{modifier}` for semantics
- `--{component}-{part}-{property}-{state}` for component tokens
- Use kebab-case for CSS custom properties, camelCase for JS tokens
