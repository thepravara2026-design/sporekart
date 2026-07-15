# Intelligence Component Library

## Widget Components

### MetricCard / MetricCardGrid
Displays a single KPI metric with value, trend arrow (↑↓→), and subtitle. Grid variant auto-fills rows at 220px min-width.

### TrendCard / TrendCardGrid
Displays a metric with emphasis on trend direction: value, percentage change, and a 7-bar visual sparkline. Available in up (green), down (red), neutral (gray) variants.

### HealthCard / HealthCardGrid
Displays health status with score, max score, status badge (healthy/warning/critical), description, horizontal progress bar, and trend indicator. Supports 3 status colors.

### KpiCard / KpiCardGrid
Displays a single KPI metric with icon, trend icon, value, and subtitle. Compact layout for dense KPI grids.

### HealthScore / HealthScoreGrid
Displays a circular progress ring health indicator with SVG ring, percentage badge, label, and score fraction. Supports 3 threshold colors (≥80 success, ≥60 warning, <60 danger).

### InsightCard
Displays an actionable intelligence recommendation with type icon (alert/warning/info/success), title, description, timestamp, and optional action button.

### AlertCard
Displays a system alert with severity-colored left border, severity icon (critical/high/medium/low), category badge, title, message, and timestamp. Dimmed when acknowledged.

### ChartContainer
Reusable wrapper providing title, optional subtitle, and a fixed-height content area for chart visualizations.

## Visualization Components

### BarChart
SVG-based bar chart accepting `{ label, value, color }[]` data. Auto-scales to max value. Renders bars with labels below.

### LineChart
SVG-based line chart accepting `{ label, value }[]` data. Renders a polyline across x-axis categories. Shows "Not enough data" for <2 points.

### PieChart
SVG-based pie chart accepting `{ label, value, color }[]` data. Renders filled circle segments with color-coded legend showing percentages.

### DonutChart
SVG-based donut chart accepting `{ label, value, color }[]` data. Same as PieChart but with a center cutout (22px radius hole) for a cleaner look.

### ProgressRing
SVG circular progress indicator accepting value, max, size, strokeWidth, and color. Displays percentage text centered in the ring.
