# Widget Template — Dashboard Widget Standard

Dashboard widgets render statistics via `KPIGrid` and richer widgets via `WidgetConfig`. Existing dashboard components are referenced (linked), not duplicated.

## KPI statistics

```ts
import type { KPIData } from '../dashboard/types';
import { KPIGrid } from '../dashboard/kpi/KPIGrid';
```

`KPIData` = `{ id, title, value, trend: 'up'|'down'|'neutral', percentage, comparison?, icon, color }`.

```tsx
const kpis: KPIData[] = [
  { id: 'products-total', title: 'Total Products', value: '1,284', trend: 'up', percentage: 12.5, comparison: 'vs last month', icon: 'package', color: '#2f6f4f' },
  { id: 'products-active', title: 'Active Products', value: '1,142', trend: 'up', percentage: 8.1, comparison: 'vs last month', icon: 'check-circle', color: '#1d9bf0' },
  { id: 'products-categories', title: 'Categories', value: '24', trend: 'neutral', percentage: 0, comparison: 'vs last month', icon: 'grid', color: '#7c3aed' },
  { id: 'products-avg-price', title: 'Avg Price', value: '$48.20', trend: 'down', percentage: 3.2, comparison: 'vs last month', icon: 'dollar-sign', color: '#d97706' },
];

<KPIGrid kpis={kpis} columns={4} />
```

> The `color` field is the only place hardcoded hex is tolerated — it is the documented KPI palette. All other UI must use design tokens.

## Widget registry

```ts
import type { WidgetConfig } from '../dashboard/types';
```

`WidgetConfig` describes non-KPI widgets (activity, timeline, quick-actions, status, announcements, notifications). Register them in the dashboard widget registry; render via the existing `dashboard/widgets/*` components:

- Activity timeline — `dashboard/widgets/WidgetCard` + `WidgetContent` (reference `docs/phase-8/widget-framework.md`)
- Quick actions — `WidgetGrid` of action buttons
- Status panel — `system-status/SystemStatusPanel`
- Announcements / Notifications — `navigation/notifications/NotificationCenter`

> Do NOT re-implement these; link to [`../phase-8/widget-framework.md`](../phase-8/widget-framework.md) and [`../data-visualization/kpis.md`](../data-visualization/kpis.md).

## Module KPI registration

Add module KPIs to `src/admin/modules/dashboardWidgets.ts` as `ModuleKPIs` (`moduleId`, `moduleLabel`, `kpis: KPIData[]`). Use `getAllModuleKPIs()` to flatten across modules.

## Rules

- KPIs use `KPIGrid` with a responsive `columns` count (typically 4 on desktop).
- `trend` must be `'up' | 'down' | 'neutral'`; `percentage` is a number.
- `icon` is a string icon name; `color` is from the documented KPI palette only.
- Non-KPI widgets reuse existing dashboard components; do not duplicate their internals here.
