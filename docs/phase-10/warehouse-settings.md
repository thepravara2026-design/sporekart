# Warehouse Settings

The Settings page (`pages/WarehouseSettingsPage.tsx`) is the configuration + governance surface for
the warehouse workspace. It is gated by `can('settings')` via `PermissionPlaceholder`/`can()`.

## Local settings (preview-only)
- **Appearance:** theme (light/dark/system), density (comfortable/compact), language (en/hi/mr).
  These are local UI state for the preview (no persistence in Mock Mode).
- **Current Role:** switches the active `role` to preview permission gating live.
- **Developer Tools:** show-devtools toggle + reset-mock-data button (placeholder actions in Mock Mode).

## Permission matrix
A read-only `Role × Permission` table built from `WAREHOUSE_ROLES` × `WAREHOUSE_PERMISSIONS` and
`WAREHOUSE_ROLE_PERMISSIONS`. It documents exactly which capabilities each role holds.

Permissions: `view, create, edit, archive, restore, settings, reports, analytics, operations_future`.

Roles: `viewer, warehouse_operator, warehouse_manager, inventory_manager, administrator`.

## Settings sections (catalog)
`WAREHOUSE_SETTINGS_SECTIONS` lists future configuration areas (general, warehouse preferences,
storage preferences, capacity rules, units, security, audit, notifications, integrations). Several
are marked `placeholder: true` to signal they are wired for later sprints.

## Note on auth
A global `PermissionProvider` is **not** mounted in the admin route tree. Warehouse access control is
implemented locally via `WarehouseWorkspaceProvider` + `useWarehousePermissions`. This is intentional
and matches the Inventory Foundation approach; a future admin-wide auth refactor would replace these
local providers.
