# Visualization Engine

## Overview

The Visualization Engine automatically selects and configures chart types based on data shape and analytical context. It produces a `VisualizationConfig` object that the frontend renders without additional chart configuration.

## Chart Types

### Line Chart
- **Use**: Time-series trends, revenue over time, customer growth, forecast projections
- **Data**: Single series of `{label, value}` pairs with temporal ordering
- **Auto-selection**: When data has > 3 time periods

### Bar Chart
- **Use**: Category comparisons, period-over-period, top N products, regional breakdowns
- **Data**: Categorical with numeric values, optionally grouped
- **Variants**: Vertical bar, horizontal bar, stacked bar, grouped bar
- **Auto-selection**: When data has < 10 categories with numeric values

### Pie Chart
- **Use**: Distribution or composition, revenue mix, segment breakdown, channel mix
- **Data**: Proportions that sum to a meaningful total
- **Auto-selection**: When data has 2-7 categories representing parts of a whole

### Heatmap
- **Use**: Cross-tabulation analysis, category-by-region performance, time-of-day patterns
- **Data**: 2D matrix of values
- **Auto-selection**: When data has two categorical dimensions

### Trend Chart
- **Use**: Multi-metric trend comparison, actual vs. forecast, scenario analysis
- **Data**: Multiple time series with shared time axis
- **Auto-selection**: When comparing 2+ metrics over time

### Forecast Chart
- **Use**: Historical + projected values with confidence bands
- **Data**: Actual + forecast points with upper/lower bounds
- **Auto-selection**: When response includes `BusinessForecast` data

### KPI Card
- **Use**: Single metric display with change indicator, target comparison, sparkline
- **Data**: Current value, previous value, change %, trend direction
- **Auto-selection**: When rendering `KpiResponse` entries

### Scorecard
- **Use**: Company health score or composite metric display
- **Data**: Overall score + dimension scores with status indicators
- **Auto-selection**: When rendering `CompanyHealthScore`

### Data Table
- **Use**: Raw data drill-down, detailed comparisons, exportable datasets
- **Data**: Tabular with sortable columns
- **Auto-selection**: Fallback when no visual chart type fits

## Auto-Visualization Logic

The engine analyzes response data using these criteria:

1. **Data Shape Detection**
   - Temporal data (YearMonth keys): use Line or Trend
   - Categorical data (< 10 items): use Bar or Pie
   - Single numeric + change: use KPI
   - Composite score: use Scorecard
   - 2D matrix: use Heatmap

2. **Context from Endpoint**
   - `/forecast` → Forecast chart
   - `/health-score` → Scorecard + KPI grid
   - `/kpi` → KPI cards
   - `/dashboard` → Mixed layout
   - `/insights` → Insight cards with mini-charts

3. **Fallback Chain**
   - Primary chart → Secondary chart → Data Table

## VisualizationConfig Schema

```json
{
  "vizId": "viz-123",
  "type": "bar",
  "title": "Revenue by Category",
  "dataSource": "RevenueAnalyticsEngine",
  "options": {
    "orientation": "horizontal",
    "showLegend": true,
    "stacked": false,
    "xAxisLabel": "Category",
    "yAxisLabel": "Revenue (INR)"
  },
  "labels": ["Mushroom Products", "Training", "Equipment", "Substrates", "Services"],
  "values": [425000, 185000, 142000, 68000, 35000],
  "colors": ["#4CAF50", "#2196F3", "#FF9800", "#9C27B0", "#607D8B"],
  "metadata": {
    "total": 855000,
    "unit": "INR",
    "period": "2026-07",
    "changePercent": 12.5
  }
}
```

## Color Schemes

| Scheme | Use Case | Colors |
|--------|----------|--------|
| `primary` | Default charts | Green, Blue, Orange, Purple, Grey |
| `sequential` | Heatmaps, gradients | Light green to dark green |
| `diverging` | Growth/decline indicators | Red (negative) to Green (positive) |
| `status` | Health score, severity | Green (good), Yellow (warning), Red (critical) |
| `categorical` | Category breakdowns | 12-color palette |

## Configuration Options

| Option | Type | Description |
|--------|------|-------------|
| `orientation` | `vertical`\|`horizontal` | Bar/column direction |
| `showLegend` | boolean | Toggle legend |
| `stacked` | boolean | Stacked bar variant |
| `showDataLabels` | boolean | Display values on chart |
| `xAxisLabel` | string | X-axis label |
| `yAxisLabel` | string | Y-axis label |
| `confidenceBand` | boolean | Show confidence interval shading |
| `goalLine` | number | Target/goal line overlay |
| `comparisonSeries` | object | Overlay another series |
| `sparklinePreview` | boolean | Mini sparkline in KPI cards |
