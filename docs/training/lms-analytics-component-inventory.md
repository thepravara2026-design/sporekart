# LMS Analytics — Component Inventory (Sprint 26 · Part 9)

All components below are **new composition wrappers** or **pages**. None duplicate
an existing Design System primitive; charts and KPIs are reused directly.

## Reused (no code added)
| Component      | Source                                                        |
|----------------|---------------------------------------------------------------|
| `LineChart`    | `design-system/components/charts/standard/LineChart`          |
| `BarChart`     | `design-system/components/charts/standard/BarChart`           |
| `PieChart`     | `design-system/components/charts/standard/PieChart`           |
| `CircularKPI`  | `design-system/components/charts/standard/CircularKPI`        |
| `MetricTile`   | `design-system/components/charts/kpi/MetricTile`              |
| `ChartSkeleton`| `design-system/components/charts` (barrel)                    |
| `Icon`         | `design-system/icons/Icon`                                    |

## New — Components
| Component          | Responsibility                                             | Props (key)                                  |
|--------------------|------------------------------------------------------------|----------------------------------------------|
| `ResponsiveChart`  | Measures container width, renders fixed-size SVG charts responsively via `ResizeObserver` | `height`, `minHeight`, `ariaLabel`, `children(dims)` |
| `WidgetGrid`       | Auto-fill responsive grid                                  | `minColWidth`, `children`                     |
| `WidgetCard`       | Titled panel with skeleton + `aria-label` region + actions | `title`, `subtitle`, `actions`, `loading`, `span` |
| `KpiCard`          | Thin wrapper over `MetricTile`                             | `title`, `value`, `subtitle`, `icon`, `trend`, `trendValue`, `onClick` |
| `AnalyticsTable`   | Generic accessible table (`<caption>`, `scope="col"`)      | `caption`, `columns`, `rows`, `rowKey`        |
| `AnalyticsToolbar` | Search + filter surface (`role="search"`) + export menu    | `filters`, `onFilterChange`, `onClearFilters`, `activeFilterCount`, `showCourseFilters`, `showTrainerFilter` |
| `ExportMenu`       | Export/print/share placeholders (`role="menu"`)           | `label`, `onExport`                           |

## New — Pages
| Page                        | Route                                    | Widgets |
|-----------------------------|------------------------------------------|---------|
| `AnalyticsWorkspaceRoute`   | `/admin/training/lms-analytics/*`        | Header + section tabs + `<Outlet/>` |
| `ExecutiveDashboardPage`    | `/executive`                             | 6 KPI tiles, 6 charts, 2 circular KPIs |
| `CourseAnalyticsPage`       | `/course`                                | 4 KPIs, 4 charts, popularity table |
| `EnrollmentAnalyticsPage`   | `/enrollment`                            | 4 KPIs, 4 charts, 2 circular KPIs |
| `CurriculumAnalyticsPage`   | `/curriculum`                            | 5 KPIs, 3 charts, coverage table |
| `ResourceAnalyticsPage`     | `/resource`                              | 4 KPIs, 2 charts, engagement table |
| `SavedDashboardsPage`       | `/saved`                                 | Pinned / Favorites / Recent cards |

## New — Data & State
| Module                | Exports                                                                 |
|-----------------------|------------------------------------------------------------------------|
| `analyticsMockData.ts`| `computeExecutiveKpis`, `buildEnrollmentTrend`, `buildDailyEnrollments`, `categoryDistribution`, `difficultyDistribution`, `languageDistribution`, `deliveryDistribution`, `coursePopularity`, `resourceUsage`, `curriculumStats`, `enrollmentFunnel`, `capacityUtilization`, `SAVED_DASHBOARDS`, `EXPORT_FORMATS`, `formatNumber`, `formatCurrency` |
| `analyticsOptions.ts` | Filter option sets, `AnalyticsFilters`, `AnalyticsScope`, `ANALYTICS_SCOPES`, `DEFAULT_ANALYTICS_FILTERS` |
| `useAnalyticsState.ts`| `useAnalyticsState()` → `{ filters, setFilter, resetFilters, activeFilterCount, filteredCourses, kpis }` |

## Duplication Audit
- Course mock data: **imported**, never redefined.
- Chart primitives: **imported**, never reimplemented.
- Filter UI pattern: mirrors the existing Course Discovery toolbar convention.
