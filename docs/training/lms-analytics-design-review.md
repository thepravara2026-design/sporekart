# LMS Analytics — Design System Reuse & Compliance (Sprint 26 · Part 9)

## Objective
Demonstrate that the Analytics Foundation is built **entirely by composition**
of existing Enterprise Design System primitives, with zero duplication.

## Charts & KPIs (reused as-is)
| Primitive     | Usage in Analytics                                              |
|---------------|----------------------------------------------------------------|
| `LineChart`   | Enrollment/completion trends, revenue trend, daily enrollments |
| `BarChart`    | Popularity, funnels, capacity, curriculum volume, languages    |
| `PieChart`    | Category / difficulty / delivery / resource distributions      |
| `CircularKPI` | Completion, utilization, approval-rate gauges                  |
| `MetricTile`  | All KPI summary tiles (via `KpiCard`)                          |
| `ChartSkeleton`| Loading state inside `WidgetCard`                             |
| `Icon`        | Tab + KPI + toolbar iconography                               |

## Design Tokens
Only existing tokens are used (verified against the token CSS):
- Surfaces: `--color-bg-surface-default`, `--color-bg-surface-raised`
- Borders: `--color-border-default`
- Text: `--color-text-primary`, `--color-text-secondary`, `--color-text-muted`, `--color-text-disabled`
- Brand: `--color-primary`, `--color-bg-primary-weak`
- Spacing: `--space-1..6`
- Radius: `--radius-sm/md/lg`
- Shadow: `--shadow-1`, `--shadow-2`
- Typography: `--text-h4/h5`, `--text-body`, `--text-body-sm`, `--text-caption`
- Data-viz: `--color-data-viz-1..8`

No hard-coded hex colors, no ad-hoc pixel typography.

## Icon Registry Compliance
Only icons present in the registry are used:
`sliders`, `book`, `user-check`, `file`, `database`, `star`, `tag`, `search`, `x`.
Unavailable domain icons (e.g. `graduation-cap`, `award`, `flask`) were
deliberately swapped for valid registry entries.

## Atomic Design Mapping
- **Atoms**: reused DS charts, `Icon`, native form controls.
- **Molecules**: `KpiCard`, `WidgetCard`, `ExportMenu`, `AnalyticsTable`.
- **Organisms**: `AnalyticsToolbar`, section pages.
- **Templates**: `AnalyticsWorkspaceRoute` (layout + tabs + outlet).

## Compliance Checklist
- [x] No duplicate chart/KPI implementations.
- [x] No duplicate course mock data.
- [x] No modifications to protected modules.
- [x] Strict typing (no `any`), `tsc -b` clean.
- [x] Tokens-only styling.
- [x] Registry-only icons.
