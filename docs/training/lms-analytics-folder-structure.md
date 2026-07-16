# LMS Analytics — Folder Structure (Sprint 26 · Part 9)

```
frontend/web-app/src/admin/training-workspace/analytics/
├── data/
│   ├── analyticsMockData.ts      # deterministic mock datasets + KPI computation
│   └── analyticsOptions.ts       # filter options, scopes, defaults, types
├── state/
│   └── useAnalyticsState.ts      # filter/search state + derived KPIs (memoized)
├── components/
│   ├── ResponsiveChart.tsx       # ResizeObserver-based responsive chart wrapper
│   ├── WidgetGrid.tsx            # CSS grid auto-fill layout
│   ├── WidgetCard.tsx            # titled panel with skeleton + a11y region
│   ├── KpiCard.tsx               # wraps design-system MetricTile
│   ├── AnalyticsTable.tsx        # generic accessible table
│   ├── AnalyticsToolbar.tsx      # search + filters + export menu (role=search)
│   └── ExportMenu.tsx            # export/print/share placeholders (role=menu)
└── pages/
    ├── AnalyticsWorkspaceRoute.tsx  # sub-layout: header + section tabs + <Outlet/>
    ├── ExecutiveDashboardPage.tsx
    ├── CourseAnalyticsPage.tsx
    ├── EnrollmentAnalyticsPage.tsx
    ├── CurriculumAnalyticsPage.tsx
    ├── ResourceAnalyticsPage.tsx
    └── SavedDashboardsPage.tsx
```

## Conventions
- **Feature-first**: everything analytics-related is co-located under `analytics/`.
- **Layered**: `data` → `state` → `components` → `pages`.
- **Barrel-free imports** for chart primitives (imported by explicit file path,
  because the standard chart index re-exports only utilities, not the SVG charts).
- **Default exports** for pages/components to match `React.lazy` usage in `App.tsx`.

## Integration Points
| File                              | Change                                   |
|-----------------------------------|------------------------------------------|
| `src/App.tsx`                     | 7 lazy imports + nested route block      |
| `courses/data/courseMockData.ts`  | **read-only** import (unchanged)         |
| `design-system/components/charts` | **read-only** import (unchanged)         |
