# Analytics Platform — Component Inventory

## Visualization Components (10)

| Component | Props | Description |
|---|---|---|
| BarChart | data, height, color, showValues | SVG bar chart with value labels |
| LineChart | data, height, color, showArea | SVG line chart with optional area fill |
| PieChart | data, size, innerRadius | SVG pie/donut chart with legend |
| ProgressRing | value, size, strokeWidth, color, label | Circular progress indicator |
| MetricCard | label, value, subtitle, color, icon | Single stat display card |
| KPITile | data (KPIData) | KPI card with variant colors & change indicator |
| TrendIndicator | value, label, inverse | Up/down/neutral trend label |
| ScoreCard | category, score, maxScore | Category score bar |
| RadarChart | data (category, averageLevel, maxLevel) | SVG radar/spider chart |
| AreaChart | data, height, color, showGrid | SVG area chart with dashed grid |
| ComparisonChart | current, previous, height | Side-by-side bar comparison |
| DataTable | columns, data, sortKey, sortDirection, onSort | Sortable data table with custom renderers |

## Support Components (5)

| Component | Props | Description |
|---|---|---|
| DashboardWidget | title, subtitle, children, actions, headerRight | Reusable widget wrapper with header |
| EmptyState | type (EmptyStateType), onClearFilters | 6 typed empty states |
| DashboardSkeleton | - | Full dashboard skeleton loader |
| AnalyticsTableSkeleton | rows | Table row skeleton loader |
| SharedFilters | currentPage | Search bar + course/batch/performance dropdowns |
