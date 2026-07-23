# Analytics Engine Guide

## Overview

The Analytics Engine processes structured queries from the CopilotEngine and returns aggregated metrics from platform services. It supports dashboard snapshots, sales analysis, customer analytics, inventory analytics, training analytics, and platform health monitoring.

## Dashboard Metrics Explained

The dashboard endpoint (`GET /api/v1/copilot/admin/dashboard`) returns a real-time snapshot of key business metrics.

```json
{
  "snapshotTime": "2026-07-23T09:35:00Z",
  "period": {
    "start": "2026-07-23T00:00:00Z",
    "end": "2026-07-23T23:59:59Z"
  },
  "sales": {
    "revenueToday": 42500.00,
    "revenueYesterday": 38900.00,
    "revenueChangePercent": 9.3,
    "ordersToday": 142,
    "ordersYesterday": 128,
    "averageOrderValue": 47.50,
    "conversionRate": 3.8,
    "topProducts": [
      { "name": "Oyster Mushroom Kit", "revenue": 8500.00 },
      { "name": "Shiitake Spawn Log", "revenue": 6200.00 }
    ]
  },
  "customers": {
    "newToday": 18,
    "activeToday": 312,
    "totalActive": 8450,
    "repeatRate": 34.2
  },
  "inventory": {
    "totalSkuCount": 342,
    "lowStockItems": 11,
    "outOfStockItems": 2,
    "inventoryValue": 1240000.00,
    "turnoverRate": 2.4
  },
  "training": {
    "activeEnrollments": 89,
    "completionRate": 92.0,
    "revenueMonth": 28500.00
  },
  "platform": {
    "serviceStatus": [
      { "name": "analytics-service", "status": "UP", "latencyMs": 45 },
      { "name": "order-service", "status": "UP", "latencyMs": 32 }
    ],
    "uptimeToday": 99.98,
    "errorRate": 0.02
  }
}
```

| Metric | Source | Description |
|---|---|---|
| `revenueToday` | Order Service | Sum of confirmed order amounts for today |
| `ordersToday` | Order Service | Count of orders placed today |
| `averageOrderValue` | Order Service | Revenue / Orders for today |
| `conversionRate` | Analytics Service | (Orders / Sessions) * 100 |
| `newToday` | Identity Service | Registrations today |
| `activeToday` | Analytics Service | Unique users with activity today |
| `repeatRate` | Analytics Service | % of orders from returning customers |
| `lowStockItems` | Inventory Service | SKUs where stock < reorder threshold |
| `turnoverRate` | Inventory Service | COGS / Average Inventory (trailing 30d) |
| `completionRate` | Training Service | Completed / Enrolled * 100 |

## Sales Analytics Methodology

### Revenue Calculation

Revenue is calculated as the sum of `amount` for orders with status `ORDER_CONFIRMED` or `SHIPPED` within the specified time range. Refunds are subtracted.

```
net_revenue = SUM(order.amount WHERE status IN ('ORDER_CONFIRMED', 'SHIPPED'))
              - SUM(refund.amount)
```

### Period Comparison

All sales metrics support comparison against:
- Previous day
- Previous week (same day-of-week)
- Previous month (same date range)
- Year-over-year (same period last year)

### Top Products

Products are ranked by revenue contribution within the period. The default limit is 10 products. Results include both absolute revenue and percentage of total revenue.

### Sales Query Parameters

| Parameter | Type | Default | Description |
|---|---|---|---|
| `periodStart` | ISO 8601 | Today 00:00 | Start of analysis period |
| `periodEnd` | ISO 8601 | Now | End of analysis period |
| `granularity` | enum | `day` | `hour`, `day`, `week`, `month` |
| `comparison` | enum | `previous_period` | `none`, `previous_period`, `year_over_year` |
| `productIds` | string[] | all | Filter by specific products |
| `categoryIds` | string[] | all | Filter by product categories |
| `region` | string | all | Filter by customer region |

## Customer Analytics

### Cohort Analysis

Cohorts are defined by the month of first purchase. Retention is calculated as the percentage of cohort customers who make a purchase in each subsequent month.

```
retention(month_n) = (customers_from_cohort_who_purchased_in_month_n)
                     / (total_customers_in_cohort) * 100
```

Example cohort table:

| Cohort | Size | M1 | M2 | M3 | M4 | M5 | M6 |
|---|---|---|---|---|---|---|---|
| Jan 2026 | 450 | 100% | 62% | 48% | 41% | 36% | 32% |
| Feb 2026 | 520 | 100% | 58% | 44% | 38% | 33% | - |
| Mar 2026 | 490 | 100% | 64% | 50% | 42% | - | - |
| Apr 2026 | 610 | 100% | 60% | 46% | - | - | - |
| May 2026 | 580 | 100% | 55% | - | - | - | - |
| Jun 2026 | 720 | 100% | - | - | - | - | - |

### Customer Lifetime Value (CLV)

CLV is calculated using the historical method for existing customers and predictive method for new customers.

```
historical_clv = SUM(all_purchase_amounts) - SUM(all_refunds)

predictive_clv = average_order_value * purchase_frequency * average_lifetime_months
```

The analytics engine returns both metrics:

```json
{
  "averageCLV": 320.00,
  "clvByCohort": {
    "2025-Q4": 410.00,
    "2026-Q1": 380.00,
    "2026-Q2": 320.00
  },
  "clvBySegment": {
    "hobbyist": 185.00,
    "commercial": 1250.00,
    "enthusiast": 520.00
  }
}
```

### Churn Analysis

Churn is calculated on a monthly basis:

```
monthly_churn_rate = (customers_who_churned_this_month)
                     / (total_customers_at_start_of_month) * 100

churned = no purchase activity for 90+ days
```

## Inventory Analytics

### Inventory Turns

```
inventory_turnover_ratio = COGS (trailing 12 months) / average_inventory_value
days_of_inventory = 365 / inventory_turnover_ratio
```

### Inventory Valuation

Valuation uses the weighted average cost method:

```
weighted_avg_cost = total_cost_of_available_stock / total_units_available
inventory_value = weighted_avg_cost * current_stock_quantity
```

### ABC Analysis

Products are classified into A, B, and C categories based on their cumulative revenue contribution:

| Class | % of SKUs | % of Revenue | Management Focus |
|---|---|---|---|
| A | 10-20% | 70-80% | Tight control, accurate forecasting |
| B | 20-30% | 15-25% | Moderate monitoring |
| C | 50-70% | 5-10% | Simplified controls, bulk ordering |

Calculation:

```
1. Sort products by revenue descending
2. Calculate cumulative revenue percentage
3. Classify:
   A: cumulative <= 80%
   B: cumulative > 80% and <= 95%
   C: cumulative > 95%
```

### Inventory Query Parameters

| Parameter | Type | Default | Description |
|---|---|---|---|
| `categoryId` | string | all | Filter by product category |
| `classification` | enum | all | `A`, `B`, `C` |
| `stockStatus` | enum | all | `in_stock`, `low_stock`, `out_of_stock` |
| `sortBy` | enum | `name` | `name`, `stock`, `turnover`, `value` |
| `includeZeroStock` | boolean | false | Include SKUs with zero stock |

## Training Analytics

### Completion Rates

```
completion_rate = (enrollments_with_status_COMPLETED)
                  / (total_enrollments) * 100
```

Completions are tracked per training program, category, and difficulty level.

### Training Revenue

Training revenue includes both direct program fees and associated material purchases attributed to training enrollments.

### Utilization Rate

```
utilization = (total_seats_filled / total_seats_available) * 100
```

### Training Query Parameters

| Parameter | Type | Default | Description |
|---|---|---|---|
| `periodStart` | ISO 8601 | Month start | Start of analysis period |
| `periodEnd` | ISO 8601 | Now | End of analysis period |
| `category` | string | all | `beginner`, `intermediate`, `advanced` |
| `programId` | string | all | Filter by specific program |
| `language` | string | all | `en`, `es`, `fr` |

## Platform Health Monitoring

The analytics engine aggregates health check results from all registered services.

### Health Check Flow

```
Admin Copilot
    |
    v
GET /actuator/health -> analytics-service (8001)
GET /actuator/health -> order-service (8002)
GET /actuator/health -> inventory-service (8003)
GET /actuator/health -> training-service (8004)
GET /actuator/health -> payment-service (8005)
GET /actuator/health -> identity-service (8006)
GET /actuator/health -> notification-service (8007)
GET /actuator/health -> risk-service (8008)
    |
    v
Aggregate results with latency metrics
Return platform health summary
```

### Health Status Matrix

| Status | Meaning | SLA Impact |
|---|---|---|
| UP | Service responding normally | None |
| DEGRADED | Service responding but with latency > 500ms or errors < 5% | Minor |
| DOWN | Service not responding or error rate > 5% | Major |
| UNKNOWN | Service not registered or unreachable | Investigate |

## Query Parameters and Filters

All analytics endpoints accept the following common parameters:

| Parameter | Type | Location | Description |
|---|---|---|---|
| `periodStart` | ISO 8601 | Query | Inclusive start of time range |
| `periodEnd` | ISO 8601 | Query | Exclusive end of time range |
| `granularity` | string | Query | `hour`, `day`, `week`, `month`, `quarter` |
| `timezone` | string | Query | IANA timezone (default: UTC) |
| `currency` | string | Query | ISO 4217 currency code (default: USD) |
| `compareWith` | string | Query | `previous_period`, `year_over_year`, `none` |
| `limit` | integer | Query | Maximum records to return (default: 100, max: 10000) |
| `offset` | integer | Query | Pagination offset |

### Granularity Mapping

| Granularity | Period Start Alignment | Grouping |
|---|---|---|
| `hour` | Start of hour | SUM per hour |
| `day` | 00:00 UTC | SUM per day |
| `week` | Monday 00:00 UTC | SUM per ISO week |
| `month` | 1st 00:00 UTC | SUM per calendar month |
| `quarter` | Quarter start | SUM per calendar quarter |
