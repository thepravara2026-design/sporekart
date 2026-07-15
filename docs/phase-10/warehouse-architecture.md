# Warehouse Architecture

The Warehouse module mirrors the **Inventory Foundation** architecture (Phase 10 / Part 1) so it can
be maintained and extended with the same patterns. It reuses Inventory's certified components and
hooks wherever possible and adds only warehouse-specific, type-correct variants.

## Layering

```
warehouse/
├── types.ts                      Domain + UI types (Warehouse, Zone, StorageNode, …)
├── constants.ts                  Sections, options, mock metrics, empty states, permissions
├── utils.ts                      formatNumber, utilization, filter/sort/search, labels
├── contexts/
│   └── WarehouseWorkspaceContext.tsx
├── hooks/
│   ├── useWarehousePermissions.ts
│   ├── useWarehouseFilters.ts
│   ├── useWarehouseSearch.ts
│   ├── useWarehouseResponsive.ts
│   └── useWarehouseData.ts        (reuses inventory useInventoryMockData)
├── services/warehouseMockService.ts
├── components/                    Warehouse-typed + reused inventory components
├── layouts/WarehouseWorkspaceLayout.tsx
├── workspace/WarehouseWorkspace.tsx
├── dashboard/WarehouseDashboard.tsx
├── pages/                         Overview, Directory, Profile, Storage, Zones, Settings
├── preview/WarehousePreviewApp.tsx (+ pages/)
└── WarehousePage.tsx
```

## State: WarehouseWorkspaceProvider
`WarehouseWorkspaceProvider` (in `contexts/WarehouseWorkspaceContext.tsx`) is the single source of
truth and exposes:

```ts
{ role, setRole, activeSection, setActiveSection, searchQuery, setSearchQuery, can }
```

- `role` + `can(action)` implement the local permission model
  (`WAREHOUSE_ROLE_PERMISSIONS`). This is **local** because a global `PermissionProvider` is not
  mounted in the admin route tree (same constraint as Inventory).
- `WarehousePage` wraps `WarehouseWorkspaceLayout` in the provider for the admin route.
- The Preview app wraps everything in the same provider (`initialRole="administrator"`) so the
  role switcher in the preview header drives `can()` gating live.

## Data flow
```
warehouseMockService  →  useInventoryMockData(fetcher)  →  useWarehouses / useWarehouseDashboard
                                                              / useWarehouseZones / useWarehouseStorageNodes
                                                              / useWarehouseActivities / useWarehouseMutations
```
`useInventoryMockData` is **reused directly** from the Inventory module; only the fetcher
(`warehouseMockService`) is warehouse-specific. Mutations (`createWarehouse`, `updateWarehouse`,
`archiveWarehouse`) mutate the in-memory arrays after a `delay(500)` to simulate latency.

## Permission model
| Role | Capabilities |
|------|--------------|
| viewer | view, analytics |
| warehouse_operator | + create, edit, operations |
| warehouse_manager | + archive, restore, settings, reports |
| inventory_manager | + archive, restore, settings, reports (no operations_future) |
| administrator | full |

`PermissionActionBar` and `PermissionPlaceholder` consume `can()` to gate UI.

## Reuse contracts
- **Reused (no rewrite):** `SectionHeader`, `SummaryCard`, `WorkspaceBanner`, `RecentActivityCard`,
  `Toolbar`, skeletons (`SkeletonDashboard/MetricCards/Table`), `useInventoryMockData`,
  `StatusBadge`, `Icon`, design-system tokens.
- **Warehouse-specific (typed to warehouse shapes):** `MetricCard`, `StatisticsGrid`, `EmptyState`,
  `SearchComponent`, `FilterPanel`, `QuickActionCard`, `WarehouseTable`, `StorageHierarchy`,
  `PermissionActionBar`, `PermissionPlaceholder`.

## Constraints honoured
- Mock Mode only; no network/DB.
- Only token CSS variables (`--color-*`, `--radius-*`, `--text-*`, `--space-component-gap`); responsive 320–1920px.
- WCAG 2.2 AA; no console errors; typecheck 0 errors.
