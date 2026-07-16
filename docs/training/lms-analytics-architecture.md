# LMS Analytics Foundation — Architecture (Sprint 26 · Part 9)

## Purpose
The Analytics Foundation delivers executive and operational dashboards for the
SporeKart Learning Management System. It surfaces course, enrollment, curriculum
and resource insights on top of the existing Training Workspace, running entirely
in **Mock Mode** (no backend, API, database, or real analytics pipeline).

## Guiding Principles
- **Documentation-first / structure-first / placeholder-only** (Phase 0 rules).
- **SOLID, DRY, KISS**, feature-first folders, Atomic Design, strict typing.
- **Zero duplicate components** — every chart/KPI is reused from the Enterprise
  Design System.
- **Single source of truth for data** — course data is imported from
  `courses/data/courseMockData.ts`; no duplicate course mock data is defined.
- **No modification of protected modules** — Auth, RBAC, Orders, Products,
  Inventory, Warehouse, Customer Website, Enterprise Dashboard, Enterprise Design
  System, Navigation, Search, Pagination, Filters, and Shared Components are left
  untouched.

## Mount Point
The foundation is mounted **inside the Training Workspace** so it inherits the
workspace chrome and stays within the LMS domain, while deliberately avoiding the
reserved `/admin/analytics` route owned by the Enterprise Dashboard.

```
/admin/training/lms-analytics                -> redirect to /executive
/admin/training/lms-analytics/executive      -> Executive Dashboard
/admin/training/lms-analytics/course         -> Course Analytics
/admin/training/lms-analytics/enrollment     -> Enrollment Analytics
/admin/training/lms-analytics/curriculum     -> Curriculum Analytics
/admin/training/lms-analytics/resource       -> Resource Analytics
/admin/training/lms-analytics/saved          -> Saved Dashboards
/admin/training/lms-analytics/student        -> (future placeholder)
/admin/training/lms-analytics/trainer        -> (future placeholder)
/admin/training/lms-analytics/financial      -> (future placeholder)
```

All routes are registered as **lazy** chunks in `App.tsx` under the existing
`TrainingWorkspaceRoute`.

## Folder Structure
```
src/admin/training-workspace/analytics/
├── data/
│   ├── analyticsMockData.ts     # KPIs, time series, distributions, saved dashboards
│   └── analyticsOptions.ts      # filter option sets, scopes, defaults
├── state/
│   └── useAnalyticsState.ts     # filter/search state + derived KPIs
├── components/
│   ├── ResponsiveChart.tsx      # responsive wrapper for fixed-size SVG charts
│   ├── WidgetGrid.tsx           # auto-fill responsive grid
│   ├── WidgetCard.tsx           # titled card (skeleton + a11y region)
│   ├── KpiCard.tsx              # wraps design-system MetricTile
│   ├── AnalyticsTable.tsx       # accessible generic table
│   ├── AnalyticsToolbar.tsx     # search + filters (role="search")
│   └── ExportMenu.tsx           # export placeholders (menu)
└── pages/
    ├── AnalyticsWorkspaceRoute.tsx  # sub-layout + section tabs + <Outlet/>
    ├── ExecutiveDashboardPage.tsx
    ├── CourseAnalyticsPage.tsx
    ├── EnrollmentAnalyticsPage.tsx
    ├── CurriculumAnalyticsPage.tsx
    ├── ResourceAnalyticsPage.tsx
    └── SavedDashboardsPage.tsx
```

## Data Flow
```
courseMockData.ts (single source of truth)
        │
        ▼
analyticsMockData.ts  ──(deterministic seeded generators)──►  time series / distributions
        │
        ▼
useAnalyticsState()  ──(filters + search)──►  filteredCourses + computed KPIs
        │
        ▼
Pages  ──►  WidgetGrid / WidgetCard  ──►  ResponsiveChart  ──►  Design-System charts
```

## Reuse Map (Design System)
| Concern            | Reused component                              |
|--------------------|-----------------------------------------------|
| Trend lines        | `LineChart`                                   |
| Bar / funnel       | `BarChart`                                    |
| Distributions      | `PieChart`                                    |
| KPI tiles          | `MetricTile` (via `KpiCard`)                  |
| Circular KPIs      | `CircularKPI`                                 |
| Loading state      | `ChartSkeleton`                               |
| Icons              | Design-system `Icon` registry                 |

No new chart primitives were created; only thin, composition-only wrappers.

## Non-Goals (Mock Mode)
- No real data fetching, aggregation, or persistence.
- Export, share, email, and saved-dashboard actions are UI placeholders.
- Student, Trainer and Financial analytics are route + tab placeholders only.
