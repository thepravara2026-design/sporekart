# Inventory State Management

Module-local state is provided by `InventoryWorkspaceContext` and consumed through hooks. There is **no global store** and **no server state** — Part 1 is Mock Mode.

## 1. Context (`contexts/InventoryWorkspaceContext.tsx`)

```ts
interface InventoryWorkspaceValue {
  role: InventoryRole;
  setRole: (role: InventoryRole) => void;
  activeSection: string;
  setActiveSection: (id: string) => void;
  searchQuery: string;
  setSearchQuery: (q: string) => void;
  can: (action: InventoryPermission) => boolean;
}
```

`InventoryWorkspaceProvider` defaults `initialRole="administrator"` and seeds `activeSection="overview"`, `searchQuery=""`. `can(action)` is derived from `INVENTORY_ROLE_PERMISSIONS[role]`.

`useInventoryWorkspace()` throws if used outside the provider — every page/layout is mounted under `InventoryWorkspaceProvider`.

## 2. Data Hooks (`hooks/`)

All data hooks wrap `useInventoryMockData`:

```ts
function useInventoryMockData<T>(fetcher: () => Promise<T>): {
  data: T | null; loading: boolean; error: string | null; reload: () => void;
}
```

Key detail: `fetcher` is stored in a `useRef` so the fetch effect runs once on mount (and on `reload()`), **not on every render** — this avoids an infinite re-fetch loop.

| Hook | Returns | Source |
|------|---------|--------|
| `useInventoryItems()` | `InventoryItem[]` | `inventoryMockService.fetchInventoryItems()` |
| `useInventoryWarehouses()` | `Warehouse[]` | `fetchWarehouses()` |
| `useInventoryDashboard()` | `{ metrics, health }` | `fetchDashboardMetrics()` |
| `useInventoryActivities()` | `RecentActivity[]` | `fetchRecentActivities()` |
| `useInventorySearch(items)` | filtered items + query state | `searchInventoryItems()` |
| `useInventoryFilters(items)` | `{ state, toggle, setSingle, clear, results, activeCount }` | `filterInventoryItems()` |
| `useInventoryPermissions()` | `{ can, canAny, canAll, role, setRole }` | context |
| `useInventoryResponsive()` | `{ isMobile, isTablet, isDesktop, width }` | resize listener |

## 3. Mock Service (`services/inventoryMockService.ts`)

Wraps generated data in `delay(ms=500)` to simulate network latency without real I/O. Methods: `fetchInventoryItems`, `fetchWarehouses`, `fetchDashboardMetrics`, `fetchRecentActivities`. Generators `generateInventoryItems(count=60)` and `generateWarehouses()` are deterministic (seeded) so previews are stable.

## 4. Operational States

Every consumer handles the three states the data hooks expose:

- **loading** → skeletons (`SkeletonDashboard`, `SkeletonMetricCards`, `SkeletonTable`, etc.) or `aria-busy` regions.
- **error** (`mock_data_missing`) → `EmptyState` with a Reload action (`reload()`).
- **empty** → `EmptyState` (`no_inventory`, `no_results`, `configuration_required`, `permission_denied`).

## 5. Future API Slot

To move to real data, replace the `inventoryMockService` method bodies with API calls. The hook contract (`{ data, loading, error, reload }`) is already shaped for React Query / SWR-style adoption in a later part.
