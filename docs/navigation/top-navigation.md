# Top Navigation

## Overview

TopNav is a horizontal navigation bar typically used in public pages, marketing sites, or as a secondary navigation within authenticated pages. It supports dropdown menus, mega menus, and responsive overflow.

## Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `items` | `NavItem[]` | — | Navigation items |
| `variant` | `'primary' \| 'secondary'` | `'primary'` | Visual variant |
| `activePath` | `string` | — | Current active route path |
| `className` | `string` | — | Additional CSS classes |
| `onNavigate` | `(href: string) => void` | — | Navigation callback |

## NavItem

```ts
interface NavItem {
  id: string;
  label: string;
  href?: string;
  icon?: ReactNode;
  badge?: string | number;
  disabled?: boolean;
  children?: NavItem[];
  megaMenu?: MegaMenuColumn[];
}
```

## NavGroup

A group of related navigation items with an optional group label.

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `label` | `string` | — | Group heading |
| `items` | `NavItem[]` | — | Items in group |
| `className` | `string` | — | Additional CSS classes |

## MegaMenu

A multi-column dropdown for large navigation structures.

### MegaMenuColumn

```ts
interface MegaMenuColumn {
  title: string;
  items: NavItem[];
  description?: string;
}
```

### Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `columns` | `MegaMenuColumn[]` | — | Column definitions |
| `footer` | `ReactNode` | — | Optional footer content |
| `className` | `string` | — | Additional CSS classes |

## Active State

The active nav item is determined by matching `activePath` against each item's `href`. Active items receive:
- Accent color on text/icon
- Underline or indicator bar (configurable via variant)
- `aria-current="page"` attribute

## Dropdown Behavior

- Hover (desktop): Opens on mouse enter, closes on mouse leave with 200ms delay
- Click (mobile/touch): Toggles on tap
- Click outside: Closes open dropdown
- Escape key: Closes open dropdown

## Responsive Overflow

| Breakpoint | Behavior |
|------------|----------|
| `xs`, `sm` | Collapses into hamburger drawer |
| `md` | Horizontal scroll on overflow |
| `lg+` | Full horizontal layout, dropdown overflow |

When items exceed available width, overflow items are grouped into a "More" dropdown.

## Design Tokens Used

| Token | Usage |
|-------|-------|
| `--color-nav-bg` | Background |
| `--color-nav-text` | Text color |
| `--color-nav-active` | Active indicator color |
| `--color-nav-hover` | Hover background |
| `--spacing-nav-item` | Item padding |
| `--font-size-nav` | Font size |
| `--font-weight-nav-active` | Active font weight |
| `--radius-nav-dropdown` | Dropdown border radius |
| `--elevation-nav-dropdown` | Dropdown shadow |
