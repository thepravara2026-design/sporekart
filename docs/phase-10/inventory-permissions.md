# Inventory Permissions

## 1. Model

Roles and actions are defined as unions in `types.ts`:

```ts
type InventoryRole =
  | 'viewer' | 'inventory_operator' | 'inventory_manager'
  | 'warehouse_manager' | 'administrator';

type InventoryPermission =
  | 'view' | 'create' | 'edit' | 'archive' | 'restore'
  | 'settings' | 'reports' | 'analytics' | 'transactions_future';
```

The role→action mapping lives in `INVENTORY_ROLE_PERMISSIONS` (constants.ts) and is consumed by `useInventoryPermissions()`.

## 2. Why a Module-Local Model

The global admin route tree does **not** mount a `PermissionProvider`. The admin `PermissionGate` / `usePermissions` therefore **cannot** be used inside inventory pages — doing so would throw at runtime.

For Part 1 (Mock Mode), inventory implements its own permission check via `InventoryWorkspaceContext.can(action)`, exposed through `useInventoryPermissions()`. This keeps gating fully functional in the preview and admin mounts without requiring the global provider.

The `InventoryPreviewApp` intentionally wraps its content in the admin `PermissionProvider` **only** so the preview can demonstrate the admin-style permission components (`PermissionActionBar`, `PermissionPlaceholder`) side-by-side with the module-local ones.

## 3. Matrix

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

## 4. Permission Components

- `useInventoryPermissions().can(action)` — boolean check used to gate analytics/settings regions.
- `PermissionPlaceholder permission="settings"` — renders children only when permitted, else an `EmptyState` (`permission_denied`).
- `PermissionActionBar actions={[{ permission, ... }]}[]` — filters actions by permission and renders only the allowed ones (returns `null` when none).

## 5. Future Wiring

In Part 2, `INVENTORY_ROLE_PERMISSIONS` (and the `can()` implementation) should be replaced by a call to the backend RBAC service. The component API (`can`, `PermissionPlaceholder`, `PermissionActionBar`) stays the same, so no UI rework is needed.
