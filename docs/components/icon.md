# Icon

> **Status:** v1.0 — Component spec for Sprint 20 Part 2

## Overview

The Icon component is an SVG icon wrapper with a registry of 55+ named icons. Supports size, color, and accessibility toggles (decorative vs. semantic).

## Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `name` | `IconName` | — | Registered icon name |
| `size` | `'xs' \| 'sm' \| 'md' \| 'lg' \| 'xl' \| '2xl' \| '3xl'` | `'md'` | Icon dimension |
| `color` | `'default' \| 'primary' \| 'danger' \| 'success' \| 'warning' \| 'info'` | `'default'` | Icon color |
| `className` | `string` | — | Additional CSS classes |
| `aria-label` | `string` | — | Label for non-decorative icons |

## Size Tokens

| Token | px |
|-------|-----|
| `xs` | 16 |
| `sm` | 20 |
| `md` | 24 |
| `lg` | 28 |
| `xl` | 32 |
| `2xl` | 40 |
| `3xl` | 48 |

## Color Tokens

| Color | Token |
|-------|-------|
| `default` | `currentColor` |
| `primary` | `var(--color-icon-primary)` |
| `danger` | `var(--color-icon-danger)` |
| `success` | `var(--color-icon-success)` |
| `warning` | `var(--color-icon-warning)` |
| `info` | `var(--color-icon-info)` |

## Registry System

Icons are registered globally via `registerIcon(name, svgElement)`:

```ts
import { registerIcon } from '@sporekart/icons';

registerIcon('plus', plusSVG);
registerIcon('search', searchSVG);
// ... 55+ icons
```

## Usage Examples

```tsx
// Decorative icon (hidden from screen readers)
<Icon name="search" />

// Semantic icon
<Icon name="alert-circle" aria-label="Error" color="danger" />

// Custom size
<Icon name="check" size="2xl" color="success" />

// Inline with text
<span>
  <Icon name="user" size="sm" /> Profile
</span>
```

## Accessibility

- **Decorative** — No `aria-label`: component renders `aria-hidden="true"` and `focusable="false"`
- **Semantic** — With `aria-label`: component renders `role="img"` and `aria-label="..."`
- Always use `aria-label` for icons that convey meaning alone
- Decorative icons (accompanying text) omit `aria-label`
- Minimum touch target for interactive icons: 44×44px

## Design Tokens Used

| Token | Example |
|-------|---------|
| `sizing.icon.{size}` | `--sizing-icon-md` |
| `color.icon.{color}` | `--color-icon-primary` |
| `color.icon.disabled` | `--color-icon-disabled` |
