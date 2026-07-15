# Sprint 25 Part 1 — Enterprise Inventory & Warehouse Management (Foundation)

- **Date:** 2026-07-15
- **Status:** COMPLETE (foundation)
- **Quality Gate:** SATISFIED
- **Mode:** Mock Mode (no backend, no API, no CRUD, no transactions)
- **Scope:** Inventory module FOUNDATION only — architecture, workspace layout, routing config, dashboard/workspace/settings shells, local state, role/permission model, mock data, component library, and live previews.
- **Code location:** `src/admin/modules/inventory/` (within `frontend/web-app`)
- **Previews:** `/preview/inventory/{dashboard,workspace,components,navigation,settings}`
- **Admin mount:** `/admin/inventory` → `InventoryPage` (already wired in `src/App.tsx`)

---

## 1. Inventory Domain Architecture Summary

The Enterprise Inventory & Warehouse Management System (IMS) is modelled as a **15-section** workspace vertical. The foundation is intentionally generic (not mushroom-specific) so it can span the full SporeKart catalog in later parts.

| # | Section | Status | Notes |
|---|---------|--------|-------|
| 1 | Overview | Implemented (shell) | Snapshot + quick actions + sample table + activity |
| 2 | Inventory Dashboard | Implemented (shell) | Executive metrics, health, status, gated analytics, activity |
| 3 | Warehouses | Placeholder | Storage/capacity scaffold |
| 4 | Stock | Placeholder | Available / reserved / incoming stock scaffold |
| 5 | Inventory Items | Placeholder | Item/SKU management scaffold |
| 6 | Movements | Placeholder | Stock ledger scaffold |
| 7 | Receiving | Placeholder | Goods receiving / put-away scaffold |
| 8 | Transfers | Placeholder | Inter-warehouse transfer scaffold |
| 9 | Batch Management | Placeholder | Lot / expiry tracking scaffold |
| 10 | Adjustments | Placeholder | Adjustments / cycle counts scaffold |
| 11 | Analytics | Placeholder | Trend / turnover / ABC scaffold |
| 12 | Validation | Placeholder | Data QA scaffold |
| 13 | Reports | Placeholder | Operational / audit reports scaffold |
| 14 | Settings | Partially implemented | General + units_configurable; remaining areas placeholders |
| 15 | Help | Placeholder | Docs / support scaffold |

See [inventory-domain-architecture.md](./inventory-domain-architecture.md) for the full breakdown and extension points.

## 2. Workspace Architecture

`InventoryWorkspaceLayout` provides a module-local shell that mirrors the admin app shell without touching the global sidebar: header (icon + title + role switcher + settings/help), breadcrumbs, global search + notification banner, a collapsible sidebar on desktop (top nav on mobile), a content region that renders by `activeSection`, an activity-feed placeholder, and a footer.

Key principle: the global admin sidebar is **documented as an integration point but not modified** — the workspace owns its own in-module nav. The sidebar lists all 15 sections; selecting one updates `activeSection` in `InventoryWorkspaceContext` and the content region swaps. See [inventory-workspace.md](./inventory-workspace.md) and [inventory-navigation-framework.md](./inventory-navigation-framework.md).

## 3. Routing Summary

- `/admin/inventory` — already mounted to `InventoryPage` in `src/App.tsx` (renders `InventoryWorkspaceProvider` + `InventoryWorkspaceLayout`).
- `/preview/inventory/*` — live previews served now via `InventoryPreviewApp` (dashboard, workspace, components, navigation, settings tabs).

The preview app is wrapped in both `PermissionProvider` (for preview-only permission demos) and `InventoryWorkspaceProvider` (module state). See [inventory-routing.md](./inventory-routing.md).

## 4. Layout Summary

`InventoryWorkspaceLayout` is a self-contained shell mirroring the admin app shell: header, breadcrumbs, toolbar/search, content, status banner, and footer. It inherits design tokens (light/dark via CSS variables) without modifying the global shell. See [inventory-layout.md](./inventory-layout.md).

## 5. Inventory Entity Model (foundation types)

The `InventoryItem` entity captures the enterprise attribute set used by the mock table and filters:

- `id`, `inventoryId`, `sku`, `name`
- `warehouse`, `location`
- `category`, `brand`
- `status` (InventoryStatus), `stockStatus` (StockStatus)
- `stockLevel`, `reorderPoint`, `unit` (InventoryUnit)
- `batch?`
- `createdAt`, `updatedAt`

Supporting types: `Warehouse`, `InventoryMetric`, `HealthMetric`, `StatusCount`, `QuickAction`, `RecentActivity`, `InventoryFilterState`, `InventoryFilterOption`, `InventorySearchField`, `SettingsSection`, `EmptyStateConfig`. See [inventory-domain-architecture.md](./inventory-domain-architecture.md).

## 6. Permission Architecture

Roles: **Viewer, Inventory Operator, Inventory Manager, Warehouse Manager, Administrator**.

Actions: `view, create, edit, archive, restore, settings, reports, analytics, transactions_future`.

A `INVENTORY_ROLE_PERMISSIONS` map (role → actions) drives a **module-local** permission model via `useInventoryPermissions()` / `InventoryWorkspaceContext`. This is deliberate: the global admin route tree does **not** mount a `PermissionProvider`, so inventory pages must not use the admin `PermissionGate` (it would throw). The `InventoryPreviewApp` mounts `PermissionProvider` only to demonstrate permission-gated components (e.g. `PermissionActionBar`, `PermissionPlaceholder`) in the preview surface. See [inventory-permissions.md](./inventory-permissions.md).

| Action | Viewer | Operator | Manager | WH Mgr | Admin |
|--------|:------:|:--------:|:-------:|:------:|:-----:|
| view | ✓ | ✓ | ✓ | ✓ | ✓ |
| analytics | ✓ | ✓ | ✓ | | ✓ |
| create | | ✓ | ✓ | ✓ | ✓ |
| edit | | ✓ | ✓ | ✓ | ✓ |
| reports | | ✓ | ✓ | ✓ | ✓ |
| archive | | | ✓ | ✓ | ✓ |
| restore | | | ✓ | | ✓ |
| settings | | | ✓ | | ✓ |
| transactions_future | | | | | ✓ |

## 7. State Management Summary

`InventoryWorkspaceContext` manages module-local state:

- `role` / `setRole` — active role (drives permission gating)
- `activeSection` / `setActiveSection` — which workspace section is shown
- `searchQuery` / `setSearchQuery` — global inventory search
- `can(action)` — permission check bound to current role

Data-fetching hooks wrap `useInventoryMockData` (≈400–700ms artificial latency, loading/error/reload states): `useInventoryDashboard`, `useInventoryItems`, `useInventoryWarehouses`, `useInventoryActivities`, `useInventorySearch`, `useInventoryFilters`, `useInventoryPermissions`, `useInventoryResponsive`. See [inventory-state-management.md](./inventory-state-management.md).

## 8. Component Library

A reusable, token-driven component library was built for the module (no new business logic): `MetricCard`, `HealthCard`, `StatusCard`, `SummaryCard`, `EmptyState`, skeleton set, `InventoryTable`, `Toolbar`, `ActionBar`, `PermissionActionBar`, `FilterPanel`, `SearchComponent`, `StatisticsGrid`, `WorkspaceBanner`, `SectionHeader`, `QuickActionCard`, `RecentActivityCard`, `PermissionPlaceholder`. See [inventory-component-library.md](./inventory-component-library.md).

## 9. Responsive Validation Report

| Viewport | Sidebar | Dashboard | Cards | Toolbar |
|----------|:------:|:---------:|:-----:|:-------:|
| Desktop (≥1280) | docked | 4-col KPI | multi | inline |
| Laptop (1024–1279) | docked | 3-col KPI | multi | inline/wrap |
| Tablet landscape (768–1023) | collapse-to-icon | 2-col KPI | 2-col | wrap |
| Tablet portrait (481–767) | overlay | 2-col KPI | 1-col | stacked |
| Mobile landscape (≤480) | overlay | 2-col KPI | 1-col | stacked |
| Mobile portrait (≤480) | overlay | 1-col KPI | 1-col | stacked |

Responsive behaviour uses CSS-variable-driven `grid auto-fit` and `useInventoryResponsive` (resize listener). The layout switches the sidebar to a top nav under 1024px.

## 10. Accessibility Report (WCAG 2.2 AA)

- **Keyboard nav:** sidebar buttons, quick-action cards, filter inputs, and table rows are all keyboard operable.
- **Screen readers:** `WorkspaceBanner` uses `role="status"`/`role="alert"`; `EmptyState` and skeletons expose `role="status"` + `aria-busy`; the table is a semantic `<table>` with `<th scope="col">`.
- **ARIA:** section nav uses `aria-current="page"`; the activity region is labelled; toolbar uses `role="toolbar"`.
- **Focus management:** visible focus rings via `--color-focus`; all interactive controls are labelled.
- **Reduced motion:** skeleton/`shimmer` animations are CSS-driven and inherit the design system's reduced-motion handling.
- **Contrast:** all colours are taken from design tokens (`--color-*`), validated for AA contrast.

## 11. Performance Report

- **Routing:** `/preview/inventory/*` is lazy-loaded; `/admin/inventory` mounts `InventoryPage` (React.lazy) like other admin modules.
- **Render:** all presentational components are `React.memo`-ised; derived data via `useMemo`; callbacks via `useCallback`.
- **Mock latency:** `inventoryMockService` adds ~400–700ms latency to mimic network without real I/O; loading states show skeletons.
- **Minimal re-renders:** module-local context isolates state; `useInventoryMockData` uses a `fetcherRef` so the fetch effect runs once (not on every render), avoiding render loops.
- **Future scalability:** entity model and 15 sections are extensible; the API-state slot is ready for a later part.

## 12. Documentation Generated

1. `sprint-25-part-1.md` (this report)
2. `inventory-domain-architecture.md`
3. `inventory-workspace.md`
4. `inventory-routing.md`
5. `inventory-layout.md`
6. `inventory-state-management.md`
7. `inventory-permissions.md`
8. `inventory-component-library.md`
9. `inventory-navigation-framework.md`

(9 files total under `docs/phase-10/`)

## 13. Risks

- **Mock-only permissions:** the `INVENTORY_ROLE_PERMISSIONS` map is module-local; no server-side RBAC enforcement yet — must be wired to the backend in a later part.
- **Preview-only mounting:** previews are illustrative; `/admin/inventory` is the production mount and already wired.
- **Placeholder drift:** 13 of 15 sections are placeholders; naming/contracts must stay stable to avoid rework.
- **No business logic:** filtering/search mock utilities exist, but no persistence/transactions — by design for Part 1.

## 14. Recommendations for Sprint 25 Part 2

1. Implement real `InventoryTable` CRUD (add/edit/adjust) reusing the existing column model.
2. Wire `INVENTORY_ROLE_PERMISSIONS` to the backend RBAC service (replace the local `can()`).
3. Build out the highest-value placeholders first: **Warehouses**, **Inventory Items**, **Stock**, **Receiving**.
4. Add server-backed state (query/mutation/cache) reusing `InventoryWorkspaceContext` as the slot.
5. Extend the activity feed with real events and an audit trail.
6. Implement analytics (turnover, ABC classification) in the gated Analytics region.
