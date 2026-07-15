# Warehouse Component Library

All components read from design-system tokens only. Warehouse-typed components are listed with their
public contract; reused Inventory components are noted as "reused".

## Warehouse-specific components

### MetricCard
```ts
interface WarehouseMetricDisplay {
  id: string; title?: string; label?: string; value: string;
  trend: 'up' | 'down' | 'flat' | 'neutral';
  percentage?: number; comparison?: string; icon: string; color?: string; loading?: boolean;
}
```
Renders a metric tile with trend arrow + delta. Used in dashboards, profile and storage counts.

### StatisticsGrid
```ts
{ metrics: WarehouseMetric[]; health?: HealthMetric[]; statusCounts?: StatusCount[]; loading?; metricCount? }
```
Grid of `MetricCard`s plus inline health/status cards.

### EmptyState
```ts
{ stateKey: keyof typeof WAREHOUSE_EMPTY_STATES; onAction?; icon?; title?; message?; actionLabel? }
```
Warehouse empty/error states (no_warehouses, no_storage, no_zones, no_permission, mock_data_missing, …).

### SearchComponent
```ts
{ query; onQueryChange; activeFields: string[]; onToggleField: (f: string) => void; placeholder? }
```
Reuses design-system `SearchBar`; chips reflect `WAREHOUSE_SEARCH_FIELDS`.

### FilterPanel
```ts
{ options: WarehouseFilterOption[]; state: WarehouseFilterState; onToggle; onClear; activeCount }
```
Checkbox filter groups from `WAREHOUSE_FILTER_OPTIONS` (type, status, location, temperature, capacity).

### QuickActionCard
```ts
{ action: QuickAction; onClick?: (action: QuickAction) => void }
```
Tile per `WAREHOUSE_QUICK_ACTIONS`.

### PermissionActionBar
```ts
{ permissions: { canEdit?, canDelete?, canExport?, canArchive? }; selectedCount; onAction; onClear }
```
Selection toolbar shown when rows are selected in the Directory.

### PermissionPlaceholder
```ts
{ permission: WarehousePermission; children }
```
Renders children only when `can(permission)`; otherwise an `EmptyState` (permission_denied).

### WarehouseTable
```ts
{ data: Warehouse[]; loading?; selectedIds?; onToggleSelect?; onToggleSelectAll?;
  sortKey?; sortDir?; onSort?; onRowClick? }
```
Sortable, selectable warehouse grid (code, name, type, location, capacity/utilisation, status, updated).

### StorageHierarchy
See `storage-hierarchy.md`.

## Reused from Inventory (certified, not rewritten)
`SectionHeader`, `SummaryCard`, `WorkspaceBanner`, `RecentActivityCard`, `Toolbar`,
`SkeletonDashboard`, `SkeletonMetricCards`, `SkeletonTable`. Also reused: `StatusBadge`, `Icon`,
design-system tokens, and `useInventoryMockData` (data hook).

## Page map
| Page | Key UI |
|------|--------|
| Overview (`WarehouseWorkspace`) | metrics + quick actions + top warehouses + activity |
| Dashboard (`WarehouseDashboard`) | statistics grid + gated analytics + activity |
| Directory | toolbar + search + filters + sortable table + create modal + selection bar |
| Profile | warehouse summary + utilisation + storage hierarchy |
| Storage | level counts + full hierarchy tree |
| Zones | zone table + per-type counts |
| Settings | appearance, role switch, devtools, permission matrix |
