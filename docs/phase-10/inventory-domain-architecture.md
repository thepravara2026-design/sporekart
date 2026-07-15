# Inventory Domain Architecture

The Inventory & Warehouse Management System (IMS) foundation is a generic, extensible vertical inside `src/admin/modules/inventory/`. It is built entirely in Mock Mode — there is no backend, API, database, or transaction logic.

## 1. Section Map (15 sections)

`INVENTORY_SECTIONS` defines the in-module navigation. `overview` and `dashboard` are fully implemented shells; the rest are extensible placeholders that render a `SectionPlaceholder` with description and an "Extensible placeholder · Sprint 25 Part 2 ready" badge.

| id | label | icon | implemented? |
|----|-------|------|--------------|
| overview | Overview | layout | ✓ |
| dashboard | Inventory Dashboard | bar-chart | ✓ |
| warehouses | Warehouses | home | placeholder |
| stock | Stock | box | placeholder |
| items | Inventory Items | package | placeholder |
| movements | Movements | move | placeholder |
| receiving | Receiving | download | placeholder |
| transfers | Transfers | git-branch | placeholder |
| batch | Batch Management | layers | placeholder |
| adjustments | Adjustments | sliders | placeholder |
| analytics | Analytics | trending-up | placeholder |
| validation | Validation | check-circle | placeholder |
| reports | Reports | file-text | placeholder |
| settings | Settings | settings | partial |
| help | Help | help-circle | placeholder |

## 2. Core Types (`types.ts`)

- `InventoryItem` — id, inventoryId, sku, name, warehouse, location, category, brand, status, stockStatus, stockLevel, reorderPoint, unit, batch?, createdAt, updatedAt.
- `Warehouse` — id, name, location, status, capacity, used.
- `InventoryMetric` — id, title, value (string), trend, percentage, comparison, icon, color.
- `HealthMetric` — id, label, score (0–100).
- `StatusCount` — id, label, count, variant.
- `QuickAction` — id, label, icon, href, permission?.
- `RecentActivity` — id, icon, text, timestamp.
- `InventoryFilterState` — warehouse, stockStatus, inventoryStatus, category, brand, supplier, location, batchStatus, savedFilters (string[]), plus optional date ranges.
- `InventoryFilterOption`, `InventorySearchField`, `SettingsSection`, `EmptyStateConfig`.

Enums: `InventoryStatus`, `StockStatus`, `InventoryUnit`, `InventoryRole`, `InventoryPermission`.

## 3. Mock Data (`services/inventoryMockService.ts`, `constants.ts`)

- `INVENTORY_MOCK_METRICS` — 11 dashboard metrics.
- `INVENTORY_HEALTH` — 4 health metrics (availability, accuracy, turnover, expiry control).
- `INVENTORY_STATUS_COUNTS` — low / out / expired / damaged counts.
- `INVENTORY_RECENT_ACTIVITIES` — 5 sample activities.
- `INVENTORY_QUICK_ACTIONS` — 6 quick actions.
- `generateInventoryItems(count=60)` — deterministic seeded sample rows.
- `generateWarehouses()` — 4 sample warehouses.
- `inventoryMockService` wraps each in a `delay(ms=500)` to mimic network latency.

## 4. Extension Points

- New sections: add to `INVENTORY_SECTIONS` and a `case` in `InventoryWorkspaceLayout.renderContent()` (or replace the placeholder with a real page).
- New metrics: append to `INVENTORY_MOCK_METRICS` (rendered automatically by `StatisticsGrid`).
- New filters/search fields: extend `INVENTORY_FILTER_OPTIONS` / `INVENTORY_SEARCH_FIELDS`.
- Real data: replace `inventoryMockService` methods with API calls; `useInventoryMockData` already returns `{ data, loading, error, reload }`.
- Permissions: extend `INVENTORY_ROLE_PERMISSIONS` and the `InventoryPermission` union.

## 5. Constraints

- No backend, no API, no DB, no transactions, no business logic.
- Reuse existing design-system components (`StatusBadge`, `SearchBar`, `Icon`) and admin primitives.
- Token-only styling (`--color-*`, `--radius-*`, `--text-*`, `--space-component-gap`).
- WCAG 2.2 AA; responsive 320–1920px.
