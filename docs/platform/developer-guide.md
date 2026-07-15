# Developer Guide — Build a New Admin Module

This guide walks through building a complete admin module end-to-end using the verified platform primitives. It assumes you have read the [`README.md`](./README.md) and the [`module-blueprint.md`](./module-blueprint.md).

Cross-cutting rules (error handling, commit/branch naming, response formats) are **not** duplicated here — follow [`../developer-guide/README.md`](../developer-guide/README.md) and [`../standards/`](../standards/).

## Overview of the 7 steps

1. Create the module folder.
2. Define data + `DataGridColumn[]`.
3. Create the Page using the `ModulePage` pattern (`PermissionGate` + `FeatureGate` + `DataGrid`).
4. Register the route in `App.tsx` (alias `Admin*` if public-website collides).
5. Add a sidebar item to `src/admin/config/adminNavigation.tsx` (`RAW_SIDEBAR_ITEMS` + `LABELS`).
6. Add a role-nav item to `src/admin/navigation/config/roleNavigation.ts`.
7. (Optional) Add KPIs to `src/admin/modules/dashboardWidgets.ts`.

---

## Step 1 — Create the module folder

```
src/admin/modules/loyalty/
  LoyaltyPage.tsx
  types.ts
  data.ts
  index.ts
```

## Step 2 — Define data + columns

```tsx
// src/admin/modules/loyalty/types.ts
import type { DataGridColumn } from '../components/data-grid/types';

export interface LoyaltyTier {
  id: string;
  name: string;
  status: 'Active' | 'Draft' | 'Archived';
  members: number;
  points: number;
  createdAt: string;
}

export const loyaltyColumns: DataGridColumn<LoyaltyTier>[] = [
  { key: 'id', header: 'ID', width: '80px', sortable: true },
  { key: 'name', header: 'Name', width: '200px', sortable: true, filterable: true, filterType: 'text' },
  {
    key: 'status', header: 'Status', width: '120px', sortable: true, filterable: true,
    filterType: 'select',
    filterOptions: [
      { label: 'Active', value: 'Active' },
      { label: 'Draft', value: 'Draft' },
      { label: 'Archived', value: 'Archived' },
    ],
  },
  { key: 'members', header: 'Members', width: '100px', sortable: true, align: 'right' },
  { key: 'points', header: 'Points', width: '100px', sortable: true, align: 'right' },
];
```

## Step 3 — Create the Page (`ModulePage` pattern)

Wrap `DataGrid` inside `PermissionGate` (`action="view"`, `resource={module.id}`) and `FeatureGate` (`flag={module.id}`). Use `useQueryState` to lift table state to the page.

```tsx
// src/admin/modules/loyalty/LoyaltyPage.tsx
import { memo } from 'react';
import { DataGrid } from '../components/data-grid/DataGrid';
import { PermissionGate } from '../permissions/PermissionGate';
import { FeatureGate } from '../feature-flags/FeatureGate';
import { useQueryState } from '../hooks/useQueryState';
import { loyaltyColumns, type LoyaltyTier } from './types';
import { loyaltyData } from './data';

export const LoyaltyPage = memo(function LoyaltyPage() {
  const query = useQueryState(); // lift sort/filter/page/search to the page
  return (
    <PermissionGate action="view" resource="loyalty">
      <FeatureGate flag="loyalty">
        <div style={{ display: 'flex', flexDirection: 'column', gap: 16, padding: 24 }}>
          <div>
            <h1 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>
              Loyalty
            </h1>
            <p style={{ margin: '4px 0 0', color: 'var(--color-text-secondary)', fontSize: 'var(--text-body)' }}>
              Manage loyalty tiers and member rewards
            </p>
          </div>
          <DataGrid<LoyaltyTier>
            columns={loyaltyColumns}
            data={loyaltyData}
            total={loyaltyData.length}
            sortable
            searchable
            exportable
            filterable
            stickyHeader
            pageSize={10}
            pageSizeOptions={[10, 25, 50]}
            rowKey="id"
            loading={false}
            emptyMessage="No loyalty tiers yet"
            emptyDescription="Create your first tier to get started"
            cardViewBreakpoint={768}
            renderCard={(row) => (
              <div style={{ padding: 12 }}>
                <strong style={{ color: 'var(--color-text-primary)' }}>{row.name}</strong>
                <div style={{ color: 'var(--color-text-secondary)' }}>{row.status}</div>
              </div>
            )}
          />
        </div>
      </FeatureGate>
    </PermissionGate>
  );
});
```

## Step 4 — Register the route in `App.tsx`

```tsx
import { LoyaltyPage } from './modules/loyalty/LoyaltyPage';
// If the public-website also has a "Loyalty" page, alias to avoid collision:
// import { LoyaltyPage as AdminLoyaltyPage } from './modules/loyalty/LoyaltyPage';

<Route path="/admin/loyalty" element={<LoyaltyPage />} />
```

## Step 5 — Add a sidebar item (`adminNavigation.tsx`)

Add to `RAW_SIDEBAR_ITEMS` and `LABELS`:

```tsx
// src/admin/config/adminNavigation.tsx
{ id: 'loyalty', label: 'Loyalty', href: '/admin/loyalty',
  icon: <Icon name="star" size={18} color="currentColor" />,
  roles: ['administrator', 'business_owner'] },
```

## Step 6 — Add a role-nav item (`roleNavigation.ts`)

```ts
// src/admin/navigation/config/roleNavigation.ts
const MODULE_ITEMS: NavItem[] = [
  // ...existing
  { id: 'loyalty', label: 'Loyalty', icon: 'star', href: '/admin/loyalty',
    roles: ['super_admin', 'administrator', 'manager', 'marketing_manager'] },
];
```

## Step 7 — (Optional) Add KPIs (`dashboardWidgets.ts`)

```ts
// src/admin/modules/dashboardWidgets.ts
import type { KPIData } from '../dashboard/types';

// add to MODULE_KPIS:
{
  moduleId: 'loyalty',
  moduleLabel: 'Loyalty',
  kpis: [
    { id: 'loyalty-tiers', title: 'Tiers', value: '8', trend: 'up', percentage: 6.0, comparison: 'vs last month', icon: 'star', color: '#2f6f4f' },
    { id: 'loyalty-members', title: 'Members', value: '4,210', trend: 'up', percentage: 9.4, comparison: 'vs last month', icon: 'users', color: '#1d9bf0' },
    { id: 'loyalty-points', title: 'Points Issued', value: '1.2M', trend: 'up', percentage: 12.1, comparison: 'vs last month', icon: 'trending-up', color: '#7c3aed' },
    { id: 'loyalty-redemptions', title: 'Redemptions', value: '312', trend: 'down', percentage: 2.0, comparison: 'vs last week', icon: 'dollar-sign', color: '#d97706' },
  ],
}
```

Render with `<KPIGrid kpis={...} columns={4} />` (see [`widget-template.md`](./widget-template.md)).

## Before you merge

Run the [`quality-checklist.md`](./quality-checklist.md) and confirm the [`coding-standards.md`](./coding-standards.md) rules. No framework changes should be required.
