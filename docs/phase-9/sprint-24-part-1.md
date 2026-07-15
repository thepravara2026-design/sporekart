# Sprint 24 Part 1 — Enterprise Product Domain Architecture & Module Foundation

- **Date:** 2026-07-14
- **Status:** COMPLETE (foundation)
- **Quality Gate:** SATISFIED
- **Mode:** Mock Mode (no backend, no API, no CRUD)
- **Scope:** Product module FOUNDATION only — architecture, workspace, routing config, dashboard shell, state, lifecycle, permissions, mock data, previews.
- **Code location:** `src/admin/modules/products/` (within `frontend/web-app`)
- **Previews:** `/preview/products/{layout,dashboard,routes,workspace,lifecycle}`

---

## 1. Product Domain Architecture Summary

The Enterprise Product domain is modelled as a **14 sub-domain** vertical, designed to be generic (not mushroom-specific) so it can span the full SporeKart catalog in later parts.

| # | Sub-domain | Status | Notes |
|---|------------|--------|-------|
| 1 | Products | Implemented (shell) | Core entity + dashboard shell |
| 2 | Categories | Placeholder | Taxonomy scaffold only |
| 3 | Collections | Placeholder | Curated grouping scaffold |
| 4 | Brands | Placeholder | Brand registry scaffold |
| 5 | Tags | Placeholder | Free-form tagging scaffold |
| 6 | Variants | Placeholder | SKU-variant matrix (extensible) |
| 7 | Packaging | Placeholder | Pack/unit config scaffold |
| 8 | Pricing | Placeholder | MRP/selling/wholesale scaffold |
| 9 | Media | Placeholder | Images/videos/thumbnail scaffold |
| 10 | SEO | Placeholder | Title/slug/keywords scaffold |
| 11 | Publishing | Placeholder | Lifecycle wiring scaffold |
| 12 | Product Activity | Placeholder | Audit/activity feed scaffold |
| 13 | Product Settings | Placeholder | Module settings scaffold |
| 14 | Product Analytics | Placeholder | KPI/metrics scaffold |

See [product-domain-architecture.md](./product-domain-architecture.md) for the full breakdown and extension points.

## 2. Workspace Architecture

`ProductWorkspace` provides a module-local, tabbed navigation surface that mirrors the admin shell without touching the global sidebar.

Sections: **Overview, Products, Categories, Collections, Brands, Pricing, Media, SEO, Publishing, Activity, Settings, Help**.

Key principle: the global admin sidebar is **documented as an integration point but not modified** — the workspace owns its own in-module nav. See [product-workspace.md](./product-workspace.md).

## 3. Routing Summary

- `/products/*` — defined as a **route manifest** for future mounting (status: planned).
- `/preview/products/*` — live previews served now: `layout`, `dashboard`, `routes`, `workspace`, `lifecycle`.

The route manifest lets a future part mount the module into the app router without changing the framework. See [product-routing.md](./product-routing.md).

## 4. Layout Summary

`ProductLayout` is a self-contained shell mirroring the admin app shell: header, breadcrumbs, toolbar, content, status. It inherits the admin shell semantics (dark/light via CSS variables) without modifying the global shell. See [product-layout.md](./product-layout.md).

## 5. Product Lifecycle Definition

Nine states: **draft, under_review, approved, published, scheduled, active, inactive, archived, deleted**.

Transitions: `draft → under_review → approved → published → {active | inactive}`, plus `published → scheduled`, `→ archived`, `→ deleted`. See [product-lifecycle.md](./product-lifecycle.md).

## 6. Product Entity Model

The `Product` entity captures the full enterprise attribute set (all fields are part of the foundation type, none are business-logic-bound):

- `id`, `sku`, `barcode`
- `name`, `shortDescription`, `longDescription`
- `category`, `collection`, `brand`, `tags[]`
- `productType`, `variantType`
- `packaging`, `unit`, `weight`, `dimensions`, `shelfLife`, `storageConditions`
- `countryOfOrigin`, `manufacturer`
- `gst`, `hsnCode`
- `pricing`: `{ mrp, sellingPrice, wholesalePrice, discount }`
- `media`: `{ images[], videos[], thumbnail }`
- `seo`: `{ title, description, keywords[], slug }`
- `publishingStatus` (one of the 9 lifecycle states)
- `createdBy`, `updatedBy`, `createdAt`, `updatedAt`
- `inventoryRef`, `shippingRef`, `analyticsRef` (cross-module reference stubs)

## 7. Permission Architecture

Roles: **Viewer, Editor, Manager, Administrator**.
Actions: `view, create, update, delete, publish, archive, export, import, bulk_actions`.

A `PRODUCT_PERMISSION_MATRIX` maps role × action. Integration is via the existing `PermissionGate` / `PermissionGateAll` / `PermissionGateAny` and `FeatureGate` components — **mock-only**, no backend validation. See [product-permissions.md](./product-permissions.md).

| Action | Viewer | Editor | Manager | Administrator |
|--------|:------:|:------:|:-------:|:-------------:|
| view | ✓ | ✓ | ✓ | ✓ |
| create | | ✓ | ✓ | ✓ |
| update | | ✓ | ✓ | ✓ |
| delete | | | ✓ | ✓ |
| publish | | | ✓ | ✓ |
| archive | | | ✓ | ✓ |
| export | ✓ | ✓ | ✓ | ✓ |
| import | | | ✓ | ✓ |
| bulk_actions | | | ✓ | ✓ |

## 8. State Management Summary

`ProductState` context/hooks manage:

- `currentProduct`, `currentWorkspace`, `filters`
- `currentView`, `selectedProducts`
- `lifecycleState`, `search`, `pagination`
- operational: `loading`, `empty`, `error`
- `futureApiState` placeholder for server data

See [product-state-management.md](./product-state-management.md).

## 9. Responsive Validation Report

| Viewport | Sidebar | Dashboard | Cards | Toolbar |
|----------|:------:|:---------:|:-----:|:-------:|
| Desktop (≥1280) | docked | 4-col KPI | multi | inline |
| Laptop (1024–1279) | docked | 3-col KPI | multi | inline/wrap |
| Tablet landscape (768–1023) | collapse-to-icon | 2-col KPI | 2-col | wrap |
| Tablet portrait (481–767) | overlay | 2-col KPI | 1-col | stacked |
| Mobile landscape (≤480 l) | overlay | 2-col KPI | 1-col | stacked |
| Mobile portrait (≤480) | overlay | 1-col KPI | 1-col | stacked |

Responsive behaviour uses CSS-variable-driven grids and the `cardViewBreakpoint` of `DataGrid`.

## 10. Accessibility Report (WCAG 2.2 AA)

- **Keyboard nav:** all tabs, buttons, grid rows and breadcrumbs reachable via Tab/Enter/Arrow.
- **Screen readers:** `Breadcrumb` exposes `aria-label`; `Card` supports `aria-label`; `StatusBadge` renders meaningful text.
- **ARIA:** `Tabs` uses `role="tablist"`/`tab`; `DataGrid` provides labelled controls.
- **Focus management:** visible focus ring via `--color-focus`; focus restored on view change.
- **Reduced motion:** `StatusBadge` pulse and skeleton animations respect `prefers-reduced-motion`.
- **Accessible nav/dashboard:** semantic landmarks, labelled controls, sufficient contrast from design tokens.

## 11. Performance Report

- **Routing:** route manifest avoids eager mounting; previews are isolated routes.
- **Workspace render:** `memo`-ised `KPIGrid`; module-local state prevents global re-renders.
- **Dashboard render:** KPI cards memoised; skeletons shown during loading.
- **Lazy loading / code splitting:** previews lazy-load; module can be `React.lazy` mounted later.
- **Minimal re-renders:** selector-based context; `DataGrid` query state isolated.
- **Future scalability:** entity model and sub-domains are extensible; API-state slot ready.

## 12. Documentation Generated

1. `sprint-24-part-1.md` (this report)
2. `product-domain-architecture.md`
3. `product-workspace.md`
4. `product-routing.md`
5. `product-layout.md`
6. `product-lifecycle.md`
7. `product-state-management.md`
8. `product-permissions.md`

(plus this index — 9 files total under `docs/phase-9/`)

## 13. Risks

- **Generic vs specific:** the foundation must stay generic (catalog-wide), not mushroom-specific, so future categories/variants fit.
- **Extend not rewrite:** future CRUD must extend the existing entity/state/permission scaffold rather than replace it.
- **Mock-only permissions:** the `PRODUCT_PERMISSION_MATRIX` is mock-only; no server-side enforcement yet — must be wired to RBAC in a later part.
- **Preview-only mounting:** `/products/*` routes are manifest-only; previews are illustrative and not the production mount.
- **Placeholder drift:** 13 of 14 sub-domains are placeholders; naming/contracts must remain stable to avoid rework.

## 14. Recommendations for Sprint 24 Part 2 (Enterprise Product Catalog Experience)

1. Implement real `DataGrid` CRUD over `Product` with the existing `DataGridColumn` config.
2. Wire `PRODUCT_PERMISSION_MATRIX` to the backend RBAC service (replace mock `usePermissions`).
3. Build out the highest-value placeholders first: **Categories**, **Brands**, **Pricing**, **Variants**.
4. Mount `/products/*` from the route manifest into the app router (no framework change).
5. Add server-backed `futureApiState` (query, mutation, cache) reusing `DataGrid` query state.
6. Extend lifecycle transitions with approval workflow + audit feed (Product Activity).
7. Add analytics KPIs driven by `analyticsRef` from a real metrics service.
