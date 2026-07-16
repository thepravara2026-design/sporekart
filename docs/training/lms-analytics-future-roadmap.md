# LMS Analytics — Future Roadmap (Sprint 26 · Part 9)

The foundation is built to extend without refactoring. Placeholders and seams
are already in place.

## Reserved Scopes (route + disabled tab today)
| Scope     | Planned surface                                              |
|-----------|-------------------------------------------------------------|
| Student   | Per-learner progress, cohorts, retention, at-risk signals   |
| Trainer   | Trainer load, ratings, session outcomes, utilization        |
| Financial | Revenue, refunds, discounts, LTV, payout reconciliation     |

## Backend Integration Seams
- `analyticsMockData.ts` generators are pure functions — swap their bodies for
  API/query results without touching pages or widgets.
- `useAnalyticsState` centralizes filters/search; add async loading + caching here.
- `DashboardMetric` type already carries `format` and `drillRoute` for a
  data-driven KPI grid.

## Deferred UX (placeholders today)
| Feature            | Current state             | Next step                        |
|--------------------|---------------------------|----------------------------------|
| Export (PDF/Excel/CSV) | `ExportMenu` UI only  | Wire to `useChartExport` / server |
| Print / Email / Share  | menu items            | Wire to `useChartPrint` / share API |
| Saved dashboards   | `SAVED_DASHBOARDS` mock   | Persist per-user layouts         |
| Date range         | filter present            | Bind to real time-series queries |
| Live updates       | none                      | Live regions + polling/websocket |

## Enhancements
- Widget-level configuration & drag-to-reorder dashboards.
- Comparison mode (period over period) using `BarChart` `comparison` variant.
- Chart data-point keyboard navigation (DS-level enhancement).
- Scheduled report generation.

## Guardrails to Preserve
- Keep course data sourced from `courseMockData.ts` (single source of truth).
- Keep charts/KPIs reused from the Design System (no duplication).
- Do not migrate onto the reserved `/admin/analytics` (Enterprise Dashboard) path.
