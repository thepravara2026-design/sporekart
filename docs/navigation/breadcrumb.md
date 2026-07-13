# Breadcrumb

## Overview

Breadcrumb provides navigational context by displaying the current page's location within the application hierarchy. It supports dynamic resolution, responsive collapse, icons, and full ARIA compliance.

## Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `crumbs` | `Crumb[]` | — | Breadcrumb trail |
| `maxItems` | `number` | `4` | Max items before collapse |
| `separator` | `ReactNode` | `'/'` | Separator icon/text |
| `expandText` | `string` | `'...'` | Collapse indicator |
| `className` | `string` | — | Additional CSS classes |
| `onNavigate` | `(href: string) => void` | — | Navigation callback |

## Crumb Interface

```ts
interface Crumb {
  label: string;
  href?: string;
  icon?: ReactNode;
  current?: boolean;
}
```

## BreadcrumbItem

Individual breadcrumb segment.

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `crumb` | `Crumb` | — | Crumb data |
| `isLast` | `boolean` | — | Last item (no link) |
| `className` | `string` | — | Additional CSS classes |

## Responsive Collapse

When `crumbs.length > maxItems`, intermediate items are collapsed into a dropdown:

```
Home  /  ...  /  Current Page
              ↓
         [Dropdown list of hidden crumbs]
```

- Clicking the ellipsis opens a popover with hidden crumbs
- First and last items are always visible
- `maxItems` includes the first and last items (interior items are collapsed)

## Icons Support

Each crumb can include a leading icon via the `icon` property.

```tsx
const crumbs = [
  { label: 'Home', href: '/', icon: <HomeIcon /> },
  { label: 'Products', href: '/products', icon: <PackageIcon /> },
  { label: 'Details', current: true, icon: <FileIcon /> },
];
```

## ARIA Attributes

| Attribute | Usage |
|-----------|-------|
| `nav` | Wrapper element with `aria-label="Breadcrumb"` |
| `aria-current="page"` | Applied to the last breadcrumb item |
| `ol` / `li` | Structured list for screen readers |
| `aria-hidden="true"` | On separator elements |

## Design Tokens Used

| Token | Usage |
|-------|-------|
| `--color-breadcrumb-text` | Text color |
| `--color-breadcrumb-active` | Current page color |
| `--color-breadcrumb-separator` | Separator color |
| `--color-breadcrumb-hover` | Hover state |
| `--font-size-breadcrumb` | Font size |
| `--spacing-breadcrumb-gap` | Gap between items |
