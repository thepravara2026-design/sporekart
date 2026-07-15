# Permission Framework

## Architecture

The permission framework uses React Context to provide role-based access control at the component level. Permissions are evaluated client-side using mock role definitions — no backend calls are made.

```
PermissionProvider (provides role + can())
  └─ usePermissions() hook
       └─ PermissionGate (single action)
       └─ PermissionGateAll (all actions required)
       └─ PermissionGateAny (any action required)
```

## Usage

### Provider

Wrap your admin layout or page root:

```tsx
<PermissionProvider initialRole="super_admin">
  <App />
</PermissionProvider>
```

### Hook

```tsx
const { can, canAny, canAll, role, setRole } = usePermissions();
can('create', 'orders')       // true/false
canAny(['create', 'delete'], 'orders')  // true if either is allowed
canAll(['view', 'export'], 'orders')    // true only if both are allowed
setRole('manager')            // dynamically switch role
```

### Component Gates

```tsx
<PermissionGate action="create" resource="orders" fallback={<Forbidden />}>
  <CreateOrderButton />
</PermissionGate>

<PermissionGateAll actions={['view', 'export']} resource="reports" fallback={<AccessDenied />}>
  <ExportReportButton />
</PermissionGateAll>

<PermissionGateAny actions={['view', 'update']} resource="customers">
  <CustomerProfile />
</PermissionGateAny>
```

## Configuration

Roles and permissions are defined in `permissionConfig.ts`. Each role maps to a set of resources and their allowed actions:

```ts
{
  role: 'manager',
  grants: {
    orders: ['view', 'create', 'update', 'export', 'approve'],
    products: ['view', 'create', 'update', 'export'],
    customers: ['view', 'create', 'update', 'export'],
  },
}
```

## Extension Points

- **Backend integration**: Replace `DEFAULT_PERMISSION_CONFIG` with fetched role definitions
- **Dynamic roles**: `setRole()` allows runtime role switching from auth context
- **Resource-level granularity**: Resources are arbitrary strings — add any module name
