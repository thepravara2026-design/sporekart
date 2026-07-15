# Product Permissions

> Foundation doc for [Sprint 24 Part 1](./sprint-24-part-1.md). Code: `src/admin/modules/products/permissions`. Mock Mode.

## Roles

| Role | Description |
|------|-------------|
| **Viewer** | Read-only access, can export |
| **Editor** | Create/update own drafts |
| **Manager** | Full content + publish/archive + import/bulk |
| **Administrator** | Everything, including delete |

## Permission actions

Defined by the existing `PermissionAction` type — `src/admin/permissions/types.ts`:

```ts
export type PermissionAction =
  | 'view' | 'create' | 'update' | 'delete'
  | 'export' | 'import'
  | 'approve' | 'publish' | 'archive'
  | 'bulk_actions';
```

(The `approve`/`publish`/`archive` actions map onto the lifecycle transitions in
[product-lifecycle.md](./product-lifecycle.md).)

## PRODUCT_PERMISSION_MATRIX (mock-only)

```tsx
// src/admin/modules/products/permissions/product.permissions.ts (foundation)
import type { PermissionAction } from 'src/admin/permissions/types';

export const PRODUCT_RESOURCE = 'products';

export const PRODUCT_PERMISSION_MATRIX: Record<string, PermissionAction[]> = {
  Viewer:        ['view', 'export'],
  Editor:        ['view', 'create', 'update', 'export'],
  Manager:       ['view', 'create', 'update', 'delete', 'publish', 'archive', 'export', 'import', 'bulk_actions'],
  Administrator: ['view', 'create', 'update', 'delete', 'publish', 'archive', 'export', 'import', 'bulk_actions'],
};
```

Matrix:

| Action | Viewer | Editor | Manager | Administrator |
|--------|:------:|:------:|:-------:|:-------------:|
| view | ✓ | ✓ | ✓ | ✓ |
| create | | ✓ | ✓ | ✓ |
| update | | ✓ | ✓ | ✓ |
| delete | | | ✓ | ✓ |
| publish | | | ✓ | ✓ |
| archive | | | ✓ | ✓ |
| export | ✓ | ✓ | ✓ | ✓ |
| import | | | ✓ | ✓ |
| bulk_actions | | | ✓ | ✓ |

## Integration with PermissionGate / FeatureGate

The foundation renders gated actions using the **existing** components — no new gate logic:

```tsx
import { PermissionGate, PermissionGateAll } from 'src/admin/permissions/PermissionGate';
import { FeatureGate } from 'src/admin/feature-flags/FeatureGate';

<PermissionGate action="create" resource={PRODUCT_RESOURCE}>
  <Button variant="primary" leftIcon={<Icon name="plus" />}>New Product</Button>
</PermissionGate>

<PermissionGateAny actions={['publish', 'archive']} resource={PRODUCT_RESOURCE}>
  <Button variant="success">Publish</Button>
</PermissionGateAny>

<FeatureGate flag="product-bulk-actions">
  <Button variant="outline">Bulk Actions</Button>
</FeatureGate>
```

- `PermissionGate` / `PermissionGateAll` / `PermissionGateAny` — `src/admin/permissions/PermissionGate.tsx`
- `FeatureGate` — `src/admin/feature-flags/FeatureGate.tsx`

## Mock-only caveat

The matrix is **enforced in the UI only** via `usePermissions` mock. There is **no backend validation**.
In a later part, `usePermissions` must be replaced with a server-driven RBAC check; the gates and matrix
shape stay identical so no call sites change.

## Future RBAC wiring

1. Replace the mock `usePermissions` provider with a backend role/grant resolver.
2. Keep `PRODUCT_PERMISSION_MATRIX` as the **default/fallback**; server grants override.
3. Gate lifecycle transitions (publish/archive) using the same `action` names.

## Real components used

- `PermissionGate` (+ `All`/`Any`) — `src/admin/permissions/PermissionGate.tsx`
- `FeatureGate` — `src/admin/feature-flags/FeatureGate.tsx`
- `Button` — `src/design-system/components/core/Button.tsx`
- `Icon` — `src/design-system/icons/Icon.tsx`
