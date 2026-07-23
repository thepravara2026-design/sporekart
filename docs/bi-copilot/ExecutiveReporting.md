# Executive Reporting Engine

## Overview

The Executive Reporting Engine generates structured business summaries across five reporting cadences: daily, weekly, monthly, quarterly, and annually. Each report includes a composite **Company Health Score**, domain analytics, key insights, recommendations, and risk alerts.

## Report Types

### Daily Snapshot
- **Purpose**: Quick operational pulse check
- **Sections**: KPI summary (top 8 metrics), revenue today vs yesterday, low stock alerts, critical insights
- **Delivery**: Push notification, dashboard widget
- **Visualizations**: KPI cards, sparklines, alert badges

### Weekly Summary
- **Purpose**: Week-over-week performance review
- **Sections**: Weekly KPI grid, revenue by channel, top/bottom products, new customers, training batches started
- **Delivery**: Email summary, dashboard section
- **Visualizations**: Bar charts (WoW comparison), trend lines, mini-scorecards

### Monthly Report
- **Purpose**: Comprehensive monthly business review
- **Sections**: Full health score, revenue deep-dive (category/product/region), customer segment analysis, inventory status, training performance, insights & recommendations, risk register
- **Delivery**: PDF/HTML/JSON export, dashboard page
- **Visualizations**: Full chart suite (line, bar, pie, heatmap, scorecard)

### Quarterly Review
- **Purpose**: Strategic quarterly business assessment
- **Sections**: Quarter-over-quarter comparison, trend analysis (12-month rolling), strategic recommendations, goal tracking, competitive analysis, growth initiatives
- **Delivery**: Board-ready PDF, slide deck export
- **Visualizations**: Trend charts with YoY overlay, composite scorecards, heatmap grids

### Annual Overview
- **Purpose**: Annual business performance and strategic planning
- **Sections**: Year-over-year comparison, annual revenue by quarter, customer lifecycle analysis, product portfolio performance, training impact assessment, strategic roadmap, 3-year trend forecasts
- **Delivery**: Executive PDF, interactive dashboard, data export
- **Visualizations**: Full suite + forecast projections

## Company Health Score

The Company Health Score is a composite metric (0-100) that measures overall business health across six dimensions.

### Score Dimensions

| Dimension | Weight (Default) | Factors |
|-----------|-----------------|---------|
| **Revenue Score** | 25% | Revenue growth, AOV trend, refund rate, revenue per category |
| **Customer Score** | 20% | Retention rate, churn rate, CLV, new customer growth, NPS (proxy) |
| **Training Score** | 15% | Enrollment rate, completion rate, certification rate, trainer ratings |
| **Inventory Score** | 15% | Turnover rate, low stock %, dead stock %, restock efficiency |
| **Operations Score** | 15% | Order fulfillment rate, operational efficiency, cost ratios |
| **Growth Score** | 10% | Revenue growth trajectory, customer acquisition trend, market expansion |

### Calculation Methodology

```
HealthScore = 0
For each dimension:
    dimensionScore = calculateDimensionScore(dimension)
    HealthScore += dimensionScore * dimensionWeight

HealthScore = clamp(HealthScore, 0, 100)
```

Each dimension score is calculated from normalized sub-metrics (scaled 0-100):
- Revenue growth: `(currentRevenue - previousRevenue) / previousRevenue * 100`, capped at ±50, mapped to 0-100
- Retention rate: Direct percentage (0-100)
- Churn rate: Inverse (100 - churn%)
- Turnover rate: Normalized to 0-100 with target range
- Completion rate: Direct percentage (0-100)

### Health Score Tiers

| Score Range | Status | Description |
|-------------|--------|-------------|
| 80-100 | Excellent | Business is in excellent health; maintain momentum |
| 60-79 | Good | Business is healthy; minor areas for improvement |
| 40-59 | Fair | Business needs attention in specific dimensions |
| 20-39 | Warning | Significant issues requiring intervention |
| 0-19 | Critical | Urgent action required across multiple dimensions |

### Trend Direction

The `trend` field indicates direction:
- `"improving"`: Score increased 3+ points
- `"stable"`: Score within +/- 3 points
- `"declining"`: Score decreased 3+ points

## Dashboard Definitions

### Executive Dashboard
- Health score scorecard (top center)
- Revenue trend (line, 12-month)
- KPI grid (8 key metrics with sparklines)
- Top insights (critical + important)
- Risk alerts (critical + high)

### Revenue Dashboard
- Gross vs net revenue (line, dual axis)
- Revenue by category (horizontal bar)
- Revenue by region (pie)
- MoM growth trend (bar with markers)
- Top products (sorted bar)

### Customer Dashboard
- Customer growth trend (line)
- Segment distribution (pie or donut)
- Retention vs churn (dual bar)
- Top customers (table)
- CLV by segment (bar)

### Inventory Dashboard
- Stock level by category (stacked bar)
- Low stock alerts (table, urgency color-coded)
- Fast vs slow movers (comparison bar)
- Turnover rate trend (line)
- Restocking priority (sorted table)

### Training Dashboard
- Enrollment by course (bar)
- Completion rate by course (bar with goal line)
- Trainer performance (table with score indicators)
- Revenue by training (pie)
- Batch status (funnel chart)

## Report Generation

Reports are generated on-demand or via scheduled jobs. The `ReportRequest` accepts:

| Field | Description |
|-------|-------------|
| `reportType` | `daily` \| `weekly` \| `monthly` \| `quarterly` \| `annual` |
| `format` | `pdf` \| `html` \| `json` |
| `period` | Reference period |
| `sections` | Specific sections to include (omit for all) |
| `recipients` | Email recipients for delivery |

The response includes a download URL for asynchronous retrieval.
