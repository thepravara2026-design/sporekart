# Reporting and Dashboards

## Report Types

| Type | Description | Data Sources |
|------|-------------|--------------|
| Revenue | Revenue, orders, AOV, product/region/channel breakdowns | RevenueAnalyticsEngine |
| Customer | Customer counts, churn, retention, CLV, segments | CustomerAnalyticsEngine, SegmentationEngine |
| Training | Students, batches, scores, certifications, profitability | TrainingAnalyticsEngine |
| Cultivation | Yield, contamination, cycle time, grower metrics | CultivationAnalyticsEngine |
| Executive Summary | Key metrics, highlights, risks, recommendations | All engines |
| Comprehensive | All domains in a single report | All engines |

## Report Formats

PDF, Excel, CSV, HTML (configurable via `sporekart.bi.copilot.report-formats`).

## Report Scheduling

Reports can be scheduled via the `POST /v1/bi/reports` API with a cron-like `schedule` field. Scheduled reports are tracked in memory by the `ReportingEngine`.

## Dashboards

### Executive Dashboard (`DASH_EXEC`)
6 widgets: Revenue KPI, Active Customers KPI, Revenue Trend (line), Training Completion (gauge), Yield by Species (bar), Customer Satisfaction (KPI).

### Operations Dashboard (`DASH_OPS`)
8 widgets: Active Batches, Contamination Rate, Top Products (table), Pending Orders, Cycle Time Trend, Inventory Levels, Recent Alerts, Batch Yield Distribution.

### Marketing Dashboard (`DASH_MKTG`)
5 widgets: New Customers, Customer Growth Trend, Revenue by Channel (pie), Customer Segments (donut), Marketing Campaign ROI (table).

## Widget Configuration

Each widget defines:
- **Type:** kpi_card, line_chart, bar_chart, pie_chart, donut_chart, gauge, table, list, scatter_plot
- **Position:** x, y coordinates in grid
- **Size:** width, height in grid units
- **Refresh:** configurable interval
- **Configuration:** type-specific options (thresholds, segments, columns, colors, etc.)

## API Summary

| Endpoint | Description |
|----------|-------------|
| `GET /v1/bi/dashboards/{dashboardId}` | Get dashboard with all widgets |
| `GET /v1/bi/dashboards/default` | Get default (Executive) dashboard |
| `GET /v1/bi/dashboards/executive` | Get Executive dashboard |
| `GET /v1/bi/dashboards/operations` | Get Operations dashboard |
| `GET /v1/bi/dashboards/marketing` | Get Marketing dashboard |
| `POST /v1/bi/dashboards/widgets` | Create a new widget |
| `PUT /v1/bi/dashboards/widgets/{widgetId}` | Update an existing widget |
| `DELETE /v1/bi/dashboards/widgets/{widgetId}` | Remove a widget |
| `GET /v1/bi/dashboards/widgets/{widgetId}/data` | Get live data for a widget |
| `POST /v1/bi/dashboards/{dashboardId}/refresh` | Refresh dashboard data |
| `POST /v1/bi/reports` | Generate or schedule a report |
| `GET /v1/bi/reports/scheduled` | List scheduled reports |
