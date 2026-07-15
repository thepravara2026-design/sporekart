# Inventory Component Library

All components live in `src/admin/modules/inventory/components/` and are token-driven (no hardcoded colours). They are re-exported from `components/index.ts`. None contain business logic — they render props and mock state only.

## 1. Metric & Health

- **MetricCard** `metric?: InventoryMetric; loading?` — KPI card with trend icon, percentage, comparison. Loading shows a shimmer skeleton.
- **HealthCard** `health: HealthMetric; loading?` — circular SVG score ring + `StatusBadge` (Healthy/Monitor/Risk) via `getHealthColor`/`getHealthVariant`.
- **StatusCard** `status: StatusCount` — count + label + `StatusBadge`.
- **StatisticsGrid** `metrics: InventoryMetric[]; health?: HealthMetric[]; statusCounts?: StatusCount[]; loading?; metricCount?` — composes MetricCard / HealthCard / StatusCard in responsive grids.

## 2. Cards & Empty

- **SummaryCard** `title; description?; icon?; actions?: SummaryAction[]; children?; footer?; loading?` — section card with optional action buttons and a loading skeleton.
- **EmptyState** `stateKey` (+ optional title/message/actionLabel/icon/onAction) — reads `INVENTORY_EMPTY_STATES`; roles: `status`.

## 3. Data Display

- **InventoryTable** `data: InventoryItem[]; loading?; onRowClick?; emptyKey?; onEmptyAction?` — semantic `<table>` with `StatusBadge` stock status; skeleton while loading; empty state when `data.length === 0`.
- **RecentActivityCard** `activities: RecentActivity[]; loading?; title?` — activity list with shimmer skeleton.

## 4. Toolbars & Actions

- **Toolbar** `title?; search?: ReactNode; filters?: ReactNode; actions?: ReactNode` — flexible bar.
- **ActionBar** `actions: ActionButton[]` — plain action buttons (`{ id, label, icon?, onClick?, variant? }`).
- **PermissionActionBar** `actions: PermissionActionButton[]` — like `ActionBar` but each action requires `permission` and is filtered via `can()`.

## 5. Search & Filter

- **SearchComponent** `query; onQueryChange; activeFields: string[]; onToggleField; placeholder?` — wraps `SearchBar` + field toggle chips from `INVENTORY_SEARCH_FIELDS`.
- **FilterPanel** `state: InventoryFilterState; onToggle; onClear; activeCount` — fieldset group of `INVENTORY_FILTER_OPTIONS` with an active-count badge and a "Clear all" button; shows a "Smart Filters (coming soon)" lock chip.

## 6. Layout & Misc

- **WorkspaceBanner** `variant?: 'info'|'success'|'warning'|'offline'|'maintenance'; icon?; title; message?; action?` — `role="status"` (or `alert` for offline/maintenance).
- **SectionHeader** `title; description?; sublabel?; icon?; actions?: ReactNode` — page header with optional icon and action slot.
- **QuickActionCard** `action: QuickAction; onClick?` — icon + label tile.
- **PermissionPlaceholder** `permission: InventoryPermission; children` — gates children by `can(permission)`.
- **Skeletons** `SkeletonDashboard`, `SkeletonMetricCards`, `SkeletonTable`, `SkeletonSearch`, `SkeletonFilters` — `role="status"` shimmer placeholders.

## 7. Performance

All presentational components are `React.memo`-ised. `StatisticsGrid` and `InventoryTable` memoize rows; skeletons avoid layout shift during loading.
