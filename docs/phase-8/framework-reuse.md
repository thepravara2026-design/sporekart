# Part 8 — Framework Reuse Pattern

The core proof of Part 8: a future module needs **no new framework code**, only data.

## The Contract

A module is fully defined by two artifacts:

```ts
// moduleData.ts
interface MockModule {
  id: string;
  label: string;
  description: string;
  columns: DataGridColumn[];   // not ColumnConfig
  data: Record<string, any>[]; // mock rows
  permissionAction: string;     // for PermissionGate
  featureKey?: string;          // for FeatureGate
}
```

## The Template

`ModulePage` composes three certified primitives:

```
ModulePage
 ├─ PermissionGate (action="view")   → blocks unauthorized roles
 ├─ FeatureGate (featureKey)         → blocks disabled features
 └─ DataGrid                          → sortable, searchable, exportable, paginated
```

It is `memo`'d; the `DataGrid` is internally `memo`'d; columns/data are stable references from `moduleData.ts` so re-renders are avoided.

## Why `DataGridColumn` not `ColumnConfig`

`DataGrid` (composite) requires `DataGridColumn[]`. `ColumnConfig` belongs to `EnterpriseTable`, which reads from `DataGridProvider` context and cannot be used standalone with prop data. Using the wrong type surfaced a TS error caught during Part 8 and corrected — the column helper `mockCol()` returns `DataGridColumn` with `width` as `"140px"` strings.

## Real Module Onboarding Checklist

1. Add module entry to `moduleData.ts` (or split into its own `data.ts`).
2. Create `<Module>Page.tsx` returning `<ModulePage module={...} />`.
3. Register route in `App.tsx` (alias if public collision exists).
4. Add sidebar item to `adminNavigation.tsx`.
5. Add role nav item to `roleNavigation.ts` (if role-gated).
6. (Optional) Add KPIs to `dashboardWidgets.ts`.

No changes to `DataGrid`, `PermissionGate`, `FeatureGate`, `ModulePage`, or the layout are required.
