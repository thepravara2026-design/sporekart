# Page Template — Page Shell

Every module page shares one shell: a header (`h1` + description), permission/feature gating, the main content (usually `DataGrid`), and operational states. The canonical reference is `src/admin/modules/ModulePage.tsx`.

## Canonical reference implementation

```tsx
// src/admin/modules/ModulePage.tsx
import { memo } from 'react';
import { DataGrid } from '../components/data-grid/DataGrid';
import { PermissionGate } from '../permissions/PermissionGate';
import { FeatureGate } from '../feature-flags/FeatureGate';
import type { MockModule } from './moduleData';

interface ModulePageProps {
  module: MockModule;
}

export const ModulePage = memo(function ModulePage({ module }: ModulePageProps) {
  return (
    <PermissionGate action="view" resource={module.id}>
      <FeatureGate flag={module.id}>
        <div style={{ display: 'flex', flexDirection: 'column', gap: 16, padding: 24 }}>
          <div>
            <h1 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>
              {module.label}
            </h1>
            <p style={{ margin: '4px 0 0', color: 'var(--color-text-secondary)', fontSize: 'var(--text-body)' }}>
              {module.description}
            </p>
          </div>

          <DataGrid
            columns={module.columns}
            data={module.data}
            sortable
            searchable
            exportable
            stickyHeader
            pageSize={10}
          />
        </div>
      </FeatureGate>
    </PermissionGate>
  );
});
```

## Mandatory page shell rules

- **Header**: one `<h1>` using `var(--text-h2)` and `var(--color-text-primary)`; description in `var(--color-text-secondary)` / `var(--text-body)`.
- **Gating**: `PermissionGate action="view"` outermost, `FeatureGate flag={module.id}` inside.
- **Container**: `display:flex; flexDirection:column; gap:16px; padding:24px` (or use `var(--space-section-gap)` / `var(--space-page-x)` tokens).
- **Content**: `DataGrid` for tabular data; see [`table-template.md`](./table-template.md).
- **Loading**: pass `loading` to `DataGrid`, or use `operational-states` `LoadingSkeleton`.
- **Empty**: pass `emptyMessage` / `emptyDescription` to `DataGrid`.
- **Error**: wrap the route element in `ModuleErrorBoundary` / `PageErrorBoundary`.
- **Responsive**: root container uses token spacing; `DataGrid cardViewBreakpoint` handles mobile.

## Variant — non-grid page (form / dashboard)

For forms or dashboards, keep the same header + gates, but swap `DataGrid` for a `FormLayout` (see [`form-template.md`](./form-template.md)) or `KPIGrid` (see [`widget-template.md`](./widget-template.md)):

```tsx
<PermissionGate action="view" resource="loyalty">
  <FeatureGate flag="loyalty">
    <div style={{ display: 'flex', flexDirection: 'column', gap: 16, padding: 24 }}>
      <div>
        <h1 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>Loyalty</h1>
        <p style={{ margin: '4px 0 0', color: 'var(--color-text-secondary)', fontSize: 'var(--text-body)' }}>
          Manage loyalty tiers
        </p>
      </div>
      <KPIGrid kpis={loyaltyKpis} columns={4} />
    </div>
  </FeatureGate>
</PermissionGate>
```
