# Inventory Navigation Framework

The inventory module owns a module-local navigation surface so the global admin sidebar is not modified.

## 1. Section Registry

`INVENTORY_SECTIONS` (`constants.ts`) is the single source of truth for navigation:

```ts
{ id, label, icon, href, description? }
```

The sidebar maps over it; each button calls `setActiveSection(s.id)`. `activeSection` lives in `InventoryWorkspaceContext`, so navigation state is shared across the layout, header, and quick-action handlers.

## 2. Sidebar (desktop) vs Top Nav (mobile)

`InventoryWorkspaceLayout` uses `useInventoryResponsive().isMobile`:

- **≥1024px:** a 240px docked left `<nav>` (vertical buttons).
- **<1024px:** the same `<nav>` becomes a full-width top region (flex-direction column on the body, horizontal scroll on the nav).

The active section gets `aria-current="page"` and primary styling.

## 3. Breadcrumbs

A static `Admin / Inventory / <active label>` trail is rendered above the search row. It is token-styled and self-contained (does not import the global `Breadcrumb`).

## 4. Quick Actions

`INVENTORY_QUICK_ACTIONS` drives the Quick Actions grid on the Overview and Workspace pages. Each `QuickAction` may carry an optional `permission`; actions are filtered by `can(permission)` so lower roles see fewer tiles. Clicking a quick action maps to a section via a small id→section map and calls `setActiveSection(...)`.

## 5. Role Switcher

The header `select` is bound to `setRole`. Changing the role immediately re-derives `can()` and re-gates permission-dependent UI (analytics region, `PermissionActionBar`, `PermissionPlaceholder`) — a live demo of the permission model without a backend.

## 6. Settings Navigation

`InventorySettings` has its own sub-nav over `INVENTORY_SETTINGS_SECTIONS` (general, inventory preferences, warehouse preferences, stock preferences, units, measurement, + placeholder areas). Only `general`/`inventory_preferences`/`warehouse_preferences`/`stock_preferences` render toggle groups; `units` renders the unit list; the rest render an `EmptyState` ("coming soon").

## 7. Extension

To add a real route for a placeholder section, update `INVENTORY_SECTIONS[].href` to the real path and add a `case` in `renderContent()` (or a nested `<Route>`). The navigation framework requires no other changes.
