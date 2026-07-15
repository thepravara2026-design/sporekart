# Product Routing

> Foundation doc for [Sprint 24 Part 1](./sprint-24-part-1.md). Code: `src/admin/modules/products/routing`. Mock Mode.

## Route manifest concept

`/products/*` routes are **declared as a manifest** rather than eagerly mounted. This lets a future part
mount the module into the app router **without changing the framework** — the manifest is the single
source of route definitions consumed by the router.

```tsx
// src/admin/modules/products/routing/product.routes.ts (foundation)
export interface ProductRoute {
  path: string;
  section: string;
  purpose: string;
  status: 'planned';
  lazy?: boolean;
}

export const PRODUCT_ROUTES: ProductRoute[] = [
  { path: '/products', section: 'overview', purpose: 'Product domain overview dashboard', status: 'planned' },
  { path: '/products/list', section: 'products', purpose: 'Product catalog grid', status: 'planned' },
  { path: '/products/categories', section: 'categories', purpose: 'Category management', status: 'planned' },
  { path: '/products/collections', section: 'collections', purpose: 'Collection management', status: 'planned' },
  { path: '/products/brands', section: 'brands', purpose: 'Brand registry', status: 'planned' },
  { path: '/products/pricing', section: 'pricing', purpose: 'Pricing management', status: 'planned' },
  { path: '/products/media', section: 'media', purpose: 'Media library', status: 'planned' },
  { path: '/products/seo', section: 'seo', purpose: 'SEO editor', status: 'planned' },
  { path: '/products/publishing', section: 'publishing', purpose: 'Publishing & lifecycle', status: 'planned' },
  { path: '/products/activity', section: 'activity', purpose: 'Product activity feed', status: 'planned' },
  { path: '/products/settings', section: 'settings', purpose: 'Module settings', status: 'planned' },
  { path: '/products/help', section: 'help', purpose: 'Help & docs', status: 'planned' },
];
```

## Live previews

Previews are served **now** and are isolated routes under `/preview/products/*` so the foundation can be
reviewed without the production mount.

| Preview path | Shows |
|--------------|-------|
| `/preview/products/layout` | `ProductLayout` shell |
| `/preview/products/dashboard` | `ProductDashboard` shell + `KPIGrid` |
| `/preview/products/routes` | Rendered `PRODUCT_ROUTES` manifest |
| `/preview/products/workspace` | `ProductWorkspace` tabs |
| `/preview/products/lifecycle` | 9-state lifecycle visualisation |

## /products/* route table

| Path | Section | Purpose | Status |
|------|---------|---------|--------|
| `/products` | Overview | Domain overview dashboard | planned |
| `/products/list` | Products | Catalog grid (DataGrid) | planned |
| `/products/categories` | Categories | Category management | planned |
| `/products/collections` | Collections | Collection management | planned |
| `/products/brands` | Brands | Brand registry | planned |
| `/products/pricing` | Pricing | Pricing management | planned |
| `/products/media` | Media | Media library | planned |
| `/products/seo` | SEO | SEO editor | planned |
| `/products/publishing` | Publishing | Publishing & lifecycle | planned |
| `/products/activity` | Activity | Activity feed | planned |
| `/products/settings` | Settings | Module settings | planned |
| `/products/help` | Help | Help & docs | planned |

## Mounting in a future part (no framework change)

1. Import `PRODUCT_ROUTES` from the manifest.
2. Map each `path` to its lazy component (`React.lazy`).
3. Register the array with the app router's module registry.
4. The framework's route loader reads the manifest — no router code changes.

See [product-workspace.md](./product-workspace.md) for the in-module tabs that these routes back.
