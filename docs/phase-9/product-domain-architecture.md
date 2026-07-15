# Product Domain Architecture

> Foundation doc for [Sprint 24 Part 1](./sprint-24-part-1.md). Code: `src/admin/modules/products/`. Mock Mode.

## Overview

The Product domain is modelled as a **14 sub-domain vertical** inside `src/admin/modules/products/`.
It is intentionally **generic** — it describes a catalog entity, not a mushroom-specific one — so it
can later span food, supplement, and marketplace SKUs without rework.

```tsx
// src/admin/modules/products/types/product.types.ts (foundation)
export interface Product {
  id: string;
  sku: string;
  barcode?: string;
  name: string;
  shortDescription?: string;
  longDescription?: string;
  category: string;
  collection?: string;
  brand?: string;
  tags: string[];
  productType: string;
  variantType?: string;
  packaging?: string;
  unit?: string;
  weight?: number;
  dimensions?: { length: number; width: number; height: number };
  shelfLife?: string;
  storageConditions?: string;
  countryOfOrigin?: string;
  manufacturer?: string;
  gst?: string;
  hsnCode?: string;
  pricing: { mrp: number; sellingPrice: number; wholesalePrice?: number; discount?: number };
  media: { images: string[]; videos: string[]; thumbnail?: string };
  seo: { title?: string; description?: string; keywords: string[]; slug?: string };
  publishingStatus: ProductLifecycleState;
  createdBy: string;
  updatedBy: string;
  createdAt: string;
  updatedAt: string;
  inventoryRef?: string;
  shippingRef?: string;
  analyticsRef?: string;
}
```

## Sub-domain breakdown

| Sub-domain | Scope in foundation | Placeholder? | Extension point |
|------------|---------------------|:---:|-----------------|
| **Products** | Entity model + dashboard shell + grid wiring | No (shell) | CRUD in Part 2 |
| **Categories** | Taxonomy field + nav stub | Yes | Category service / tree |
| **Collections** | Curated grouping field + nav stub | Yes | Collection service |
| **Brands** | Brand field + nav stub | Yes | Brand registry |
| **Tags** | `tags[]` on entity + nav stub | Yes | Tag indexing |
| **Variants** | `variantType` field | Yes | Variant matrix generator |
| **Packaging** | `packaging`/`unit` fields | Yes | Pack config service |
| **Pricing** | `pricing{}` block | Yes | Pricing engine / tiers |
| **Media** | `media{}` block | Yes | DAM integration |
| **SEO** | `seo{}` block | Yes | Slug/SEO service |
| **Publishing** | `publishingStatus` lifecycle | Yes | Workflow engine |
| **Product Activity** | audit stub | Yes | Activity feed service |
| **Product Settings** | settings nav stub | Yes | Module config service |
| **Product Analytics** | `analyticsRef` stub + KPI shell | Yes | Metrics service |

## Extensibility model

- **Entity-first:** new sub-domains attach via optional reference fields (`inventoryRef`, `shippingRef`, `analyticsRef`)
  so cross-module wiring (orders, shipping, warehouse, analytics) plugs in without breaking the core type.
- **Nav-driven:** each placeholder gets a `ProductWorkspace` tab (see [product-workspace.md](./product-workspace.md));
  adding a tab does not modify the global sidebar.
- **Lifecycle-aware:** `publishingStatus` is the single source of truth for the [product-lifecycle.md](./product-lifecycle.md) state machine.

## How future modules plug in

- **Variants / Pricing:** extend `Product` with `variants[]` and a richer `pricing` object; reuse `DataGridColumn` config.
- **Marketplace:** consume `seo{}` + `media{}`; new `MarketplacePublish` lifecycle transition.
- **SEO:** consume `seo{}`; new service populates `slug`/`keywords`.
- **Analytics:** consume `analyticsRef`; `KPIGrid` + `KPIData` render real metrics.
- **Orders / Shipping / Warehouse:** consume `inventoryRef` / `shippingRef` for fulfilment linkage.

## Real components used

- `DataGrid` + `DataGridColumn` — `src/admin/components/data-grid/DataGrid.tsx`
- `Card` (design-system composite) — `src/design-system/components/composite/Card.tsx`
- `StatusBadge` (admin) — `src/admin/components/status/StatusBadge.tsx`
- `KPIGrid` + `KPIData` — `src/admin/dashboard/kpi/KPIGrid.tsx`, `src/admin/dashboard/types.ts`
- Design tokens via CSS variables (`--color-*`, `--text-*`, `--radius-*`, `--space-*`).
