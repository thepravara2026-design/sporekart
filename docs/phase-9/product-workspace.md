# Product Workspace

> Foundation doc for [Sprint 24 Part 1](./sprint-24-part-1.md). Code: `src/admin/modules/products/workspace`. Mock Mode.

## Purpose

`ProductWorkspace` is the in-module navigation + content surface for the Product domain. It owns its own
tabbed navigation so it can grow across the 14 sub-domains without ever modifying the **global admin sidebar**.

## Structure

```tsx
// src/admin/modules/products/workspace/ProductWorkspace.tsx (foundation)
import { Tabs } from 'src/admin/components/navigation/Tabs';
import { Card } from 'src/design-system/components/composite/Card';

export type ProductWorkspaceSection =
  | 'overview' | 'products' | 'categories' | 'collections' | 'brands'
  | 'pricing' | 'media' | 'seo' | 'publishing' | 'activity' | 'settings' | 'help';

const SECTIONS: { id: ProductWorkspaceSection; label: string }[] = [
  { id: 'overview', label: 'Overview' },
  { id: 'products', label: 'Products' },
  { id: 'categories', label: 'Categories' },
  { id: 'collections', label: 'Collections' },
  { id: 'brands', label: 'Brands' },
  { id: 'pricing', label: 'Pricing' },
  { id: 'media', label: 'Media' },
  { id: 'seo', label: 'SEO' },
  { id: 'publishing', label: 'Publishing' },
  { id: 'activity', label: 'Activity' },
  { id: 'settings', label: 'Settings' },
  { id: 'help', label: 'Help' },
];
```

Sections implemented as shells: **Overview, Products, Categories, Collections, Brands, Pricing, Media, SEO,
Publishing, Activity, Settings, Help** (12 tabs). Most are placeholders rendering a `Card` + `Skeleton`.

## Module-local navigation

- Uses the existing admin `Tabs` component — `src/admin/components/navigation/Tabs.tsx`.
- `activeId` is driven by `ProductState.currentView` (see [product-state-management.md](./product-state-management.md)).
- No global sidebar mutation; the workspace is rendered **inside** `ProductLayout` content area.

## Integration with the existing admin sidebar

> The global admin sidebar is an **integration point, not a modification target**.

| Concern | Approach |
|---------|----------|
| Entry point | A single "Products" item in the sidebar opens `/preview/products/workspace` (and later `/products`) |
| Section nav | Owned entirely by `ProductWorkspace` tabs |
| Breadcrumbs | `ProductLayout` renders `Breadcrumb` (design-system) above the workspace |
| Theme | Inherits global dark/light via CSS variables — no override |

See [product-layout.md](./product-layout.md) for how the shell is mirrored.

## Responsive behaviour

- **≥1024px:** tabs render as a horizontal `underline` variant; content is multi-column.
- **768–1023px:** `pills` variant; content collapses to 2 columns.
- **≤767px:** tabs become a horizontally scrollable strip; content stacks to 1 column; sidebar becomes overlay (global).

## Real components used

- `Tabs` (admin nav) — `src/admin/components/navigation/Tabs.tsx`
- `Card` (design-system composite) — `src/design-system/components/composite/Card.tsx`
- `Breadcrumb` (design-system nav) — `src/design-system/components/navigation/Breadcrumb.tsx`
- `Skeleton` (design-system display) — `src/design-system/components/display/Skeleton.tsx`
