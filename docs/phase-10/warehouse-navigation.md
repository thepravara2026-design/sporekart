# Warehouse Navigation

## Admin sidebar
`admin/config/adminNavigation.tsx` gained a **Warehouse** entry (icon `home`) routed to
`/admin/warehouse`, visible to `administrator` and `business_owner` (mirrors Inventory's gating).

## Admin route
`App.tsx`:
```tsx
<Route path="warehouse" element={<AdminWarehousePage />} />
```
`AdminWarehousePage` = `WarehouseWorkspaceProvider` + `WarehouseWorkspaceLayout`, which renders the
active section from `useWarehouseWorkspace().activeSection`.

## Workspace sections (14)
Defined in `WAREHOUSE_SECTIONS` (overview, warehouses, locations, zones, storage, capacity,
cold_storage, virtual_warehouses, operations, analytics, reports, settings, audit, help). Only the
six built-out sections render real UI; the rest render an extensible `SectionPlaceholder`.

## Preview app
`/preview/warehouse/*` → `WarehousePreviewApp` (lazy). It is a self-contained preview with:
- a role switcher (`useWarehousePermissions`) so reviewers can exercise gating,
- six tabs: **Dashboard, Directory, Profile, Storage, Zones, Settings**,
- an "Open" link to `/preview/warehouse/dashboard`.

Each tab renders its corresponding page inside the same `WarehouseWorkspaceProvider`.

## Keyboard / a11y
- `main` regions carry `id` + `tabIndex={-1}` for focus management.
- Tables use semantic `<table>` with `<th scope="col">`.
- Toggles/chips are real `<button>` with `aria-pressed`; modals use `role="dialog"` + `aria-modal`.
