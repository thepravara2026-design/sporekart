# Forecasting Guide

## Overview

The Forecasting Engine predicts future business metrics using historical data patterns. It applies time series analysis techniques including moving averages, seasonal decomposition, and trend projection to generate forecasts with confidence intervals. The engine is specifically tuned for the mushroom industry's unique seasonal patterns.

## Methodology

The forecasting pipeline processes data through four stages:

```
Historical Time Series
    |
    v
Stage 1: Preprocessing
    |-- Remove outliers (z-score > 3.0)
    |-- Fill missing values (linear interpolation)
    |-- Normalize for trading day effects
    |-- Apply calendar adjustments (holidays, events)
    |
    v
Stage 2: Decomposition
    |-- Trend component (Hodrick-Prescott filter)
    |-- Seasonal component (STL decomposition)
    |-- Residual component
    |-- Identify dominant seasonal periods (7d, 30d, 365d)
    |
    v
Stage 3: Forecasting
    |-- Apply selected algorithm based on data characteristics
    |-- Generate point forecast
    |-- Calculate prediction intervals (80%, 95%)
    |
    v
Stage 4: Post-processing
    |-- Apply business rules (min/max constraints)
    |-- Adjust for known future events
    |-- Format with metadata
    |-- Cache result
```

### Algorithm Selection

The engine automatically selects the best algorithm based on data characteristics:

| Data Pattern | Algorithm | Use Case |
|---|---|---|
| Stable, no seasonality | Simple Moving Average | Inventory levels for steady products |
| Linear trend, no seasonality | Holt's Linear Trend | Customer base growth |
| Seasonality, no trend | Seasonal Naive | Recurring training enrollments |
| Trend + Seasonality | Holt-Winters (Additive/Multiplicative) | Revenue, order volume |
| High volatility | ARIMA (auto-ARIMA selection) | New product sales |
| Multiple seasonality | Prophet (Facebook) | Platform-wide metrics |

### Moving Average

Used for short-term forecasts (7-day horizon) on stable metrics.

```
SMA(n) = (x_t + x_{t-1} + ... + x_{t-n+1}) / n

forecast = SMA(last 7 days)
```

**When used:** Inventory days of supply, customer service metrics.

### Seasonal Adjustment

The engine detects and compensates for seasonal patterns using STL (Seasonal and Trend decomposition using Loess).

```
y_t = Trend_t + Seasonal_t + Residual_t

seasonal_factor(day_of_week) = average(ratio_of_daily_to_weekly_ma)
seasonal_factor(month) = average(ratio_of_monthly_to_annual_ma)

forecast = (trend_projection) * seasonal_factor
```

### Trend Projection

Used for medium to long-term forecasts where a clear trend exists.

```
linear: y = mx + b
exponential: y = a * e^(bx)
logistic: y = L / (1 + e^(-k(x - x0)))
```

Algorithm selection for trend projection:
- Linear if R-squared > 0.8 and residual pattern is random
- Exponential if growth rate is consistently proportional to size
- Logistic if market saturation is expected

## Available Forecast Metrics

| Metric ID | Description | Default Horizon | Typical Pattern |
|---|---|---|---|
| `revenue` | Daily net revenue | 30 days | 7-day seasonality + growth trend |
| `orders` | Daily order count | 30 days | 7-day seasonality, weekend dip |
| `aov` | Average order value | 30 days | Stable with slight upward trend |
| `sales_by_product` | Revenue per product SKU | 30 days | High variance for new products |
| `sales_by_category` | Revenue per category | 30 days | Clear seasonal patterns |
| `new_customers` | Daily new registrations | 30 days | Weekly pattern, campaign-driven spikes |
| `active_users` | Daily active users | 30 days | Steady growth, weekday bias |
| `churn_rate` | Monthly churn percentage | 90 days | Slow-moving, quarterly cycles |
| `inventory_turnover` | Monthly turnover ratio | 90 days | Seasonal, quarter-end effects |
| `stockout_risk` | SKUs at risk of stockout | 7 days | Highly variable, product-specific |
| `inventory_value` | Total inventory value | 30 days | Gradual changes, order cycle driven |
| `customer_clv` | Average CLV per cohort | 90 days | Cohort-specific, stabilizes after 6 months |

## Horizon Selection

| Horizon | Days | Use Case | Confidence |
|---|---|---|---|
| Short-term | 7 | Operational decisions: restocking, staffing, promotion scheduling | Higher (narrower intervals) |
| Medium-term | 30 | Tactical planning: marketing budget, procurement, hiring | Moderate |
| Long-term | 90 | Strategic planning: quarterly targets, capacity investment, expansion | Lower (wider intervals) |

### Horizon Impact on Accuracy

| Horizon | Revenue MAPE* | Orders MAPE | Inventory MAPE |
|---|---|---|---|
| 7 days | 3-5% | 4-6% | 2-4% |
| 30 days | 8-12% | 10-15% | 5-8% |
| 90 days | 15-22% | 18-25% | 10-15% |

*MAPE = Mean Absolute Percentage Error

## Confidence Intervals

Forecasts include 80% and 95% confidence intervals calculated from historical forecast errors.

```
forecast_error = actual - forecast (on historical data)
mae = mean(|forecast_error|)
rmse = sqrt(mean(forecast_error^2))

80% CI: forecast +/- 1.28 * rmse
95% CI: forecast +/- 1.96 * rmse
```

Intervals widen with forecast horizon:

```
ci_multiplier(horizon) = 1 + (horizon / max_horizon) * 0.5

adjusted_ci = base_ci * ci_multiplier(horizon)
```

## Seasonal Patterns: Mushroom Industry Cycles

The mushroom industry exhibits several distinct seasonal patterns that the forecasting engine accounts for:

### Growing Season Cycles

| Period | Pattern | Impact |
|---|---|---|
| Spring (Mar-May) | Increased spawn and kit sales | +15-25% vs winter baseline |
| Summer (Jun-Aug) | Peak growing season, high demand | +20-30% vs winter baseline |
| Fall (Sep-Nov) | Harvest season, workshop demand | +10-20% vs winter baseline |
| Winter (Dec-Feb) | Off-season, indoor kit demand | Baseline, +5% for indoor kits |

### Weekly Patterns

| Day | Sales Index (avg=1.0) | Explanation |
|---|---|---|
| Monday | 0.85 | Post-weekend slowdown |
| Tuesday | 0.90 | |
| Wednesday | 0.95 | |
| Thursday | 1.05 | Pre-weekend planning |
| Friday | 1.15 | Weekend project purchases |
| Saturday | 1.25 | Peak shopping day |
| Sunday | 0.85 | |

### Holiday Effects

| Holiday | Effect | Duration |
|---|---|---|
| New Year | +5-10% kits (resolution gardeners) | Jan 1-7 |
| Earth Day | +15-20% (sustainability buyers) | Apr 15-25 |
| Mother's Day | +10-15% gift kits | 1 week prior |
| Black Friday | +25-40% (promotional) | Nov 24-30 |
| Christmas | +10-15% (gift giving) | Dec 15-25 |

### Weather Correlation

Mushroom growing supply sales correlate with regional weather patterns:

```
Sales_Index = 1.0 + 0.02 * (65 - |temp - 65|) + 0.01 * humidity_factor
```

Where `temp` is in Fahrenheit and `humidity_factor` is 0-1 based on regional humidity.

## Examples

### Revenue Forecast (7-day)

```json
{
  "metric": "revenue",
  "horizon": 7,
  "generatedAt": "2026-07-23T09:35:00Z",
  "historicalPeriod": {
    "start": "2026-06-23T00:00:00Z",
    "end": "2026-07-22T23:59:59Z"
  },
  "dataPoints": [
    { "date": "2026-07-23", "forecast": 41200.00, "ci80Lower": 39800.00, "ci80Upper": 42600.00, "ci95Lower": 38500.00, "ci95Upper": 43900.00 },
    { "date": "2026-07-24", "forecast": 43800.00, "ci80Lower": 41800.00, "ci80Upper": 45800.00, "ci95Lower": 39800.00, "ci95Upper": 47800.00 },
    { "date": "2026-07-25", "forecast": 46500.00, "ci80Lower": 43700.00, "ci80Upper": 49300.00, "ci95Lower": 41000.00, "ci95Upper": 52000.00 },
    { "date": "2026-07-26", "forecast": 32000.00, "ci80Lower": 28500.00, "ci80Upper": 35500.00, "ci95Lower": 25000.00, "ci95Upper": 39000.00 },
    { "date": "2026-07-27", "forecast": 29000.00, "ci80Lower": 24800.00, "ci80Upper": 33200.00, "ci95Lower": 21000.00, "ci95Upper": 37000.00 },
    { "date": "2026-07-28", "forecast": 39000.00, "ci80Lower": 34000.00, "ci80Upper": 44000.00, "ci95Lower": 29500.00, "ci95Upper": 48500.00 },
    { "date": "2026-07-29", "forecast": 42500.00, "ci80Lower": 36500.00, "ci80Upper": 48500.00, "ci95Lower": 31000.00, "ci95Upper": 54000.00 }
  ],
  "summary": {
    "totalForecast": 274000.00,
    "totalCi80Lower": 239100.00,
    "totalCi80Upper": 308900.00,
    "dailyAverage": 39142.86,
    "weekOverWeekChange": 8.3,
    "algorithm": "Holt-Winters Additive",
    "seasonalityDetected": [7],
    "trendDirection": "UP",
    "mapeHistorical": 4.2
  }
}
```

### Orders Forecast (30-day)

```json
{
  "metric": "orders",
  "horizon": 30,
  "generatedAt": "2026-07-23T09:35:00Z",
  "historicalPeriod": {
    "start": "2026-05-24T00:00:00Z",
    "end": "2026-07-22T23:59:59Z"
  },
  "dataPoints": [
    { "date": "2026-07-23", "forecast": 145, "ci80Lower": 135, "ci80Upper": 155 },
    { "date": "2026-07-24", "forecast": 158, "ci80Lower": 145, "ci80Upper": 171 },
    ...
  ],
  "summary": {
    "totalForecast": 4650,
    "dailyAverage": 155,
    "monthOverMonthChange": 5.2,
    "algorithm": "Prophet",
    "seasonalityDetected": [7, 30],
    "trendDirection": "UP",
    "mapeHistorical": 6.8,
    "peakDayProjected": "2026-08-15",
    "lowDayProjected": "2026-08-03"
  }
}
```

### Inventory Turnover Forecast (90-day)

```json
{
  "metric": "inventory_turnover",
  "horizon": 90,
  "generatedAt": "2026-07-23T09:35:00Z",
  "dataPoints": [
    { "date": "2026-08-01", "forecast": 2.5, "ci80Lower": 2.3, "ci80Upper": 2.7 },
    { "date": "2026-09-01", "forecast": 2.8, "ci80Lower": 2.5, "ci80Upper": 3.1 },
    { "date": "2026-10-01", "forecast": 3.2, "ci80Lower": 2.8, "ci80Upper": 3.6 }
  ],
  "summary": {
    "currentTurnover": 2.4,
    "projectedTurnoverQ4": 3.2,
    "changePercent": 33.3,
    "algorithm": "Holt's Linear Trend",
    "seasonalityDetected": [],
    "trendDirection": "UP",
    "mapeHistorical": 3.1
  }
}
```

### New Customers Forecast (30-day)

```json
{
  "metric": "new_customers",
  "horizon": 30,
  "generatedAt": "2026-07-23T09:35:00Z",
  "dataPoints": [
    { "date": "2026-07-23", "forecast": 22, "ci80Lower": 18, "ci80Upper": 26 },
    ...
  ],
  "summary": {
    "totalForecast": 680,
    "dailyAverage": 22.7,
    "monthOverMonthChange": 12.5,
    "algorithm": "Holt-Winters Additive",
    "seasonalityDetected": [7],
    "trendDirection": "UP",
    "mapeHistorical": 8.2,
    "note": "Forecast does not account for upcoming promotion campaigns. Adjust after campaign schedule is finalized."
  }
}
```

## Requesting a Forecast

```json
POST /api/v1/copilot/admin/forecast
{
  "metric": "revenue",
  "horizon": 30,
  "periodStart": "2026-06-23T00:00:00Z",
  "periodEnd": "2026-07-22T23:59:59Z",
  "granularity": "day",
  "filters": {
    "region": "us-west",
    "categoryIds": ["kits", "logs"]
  },
  "confidenceLevels": [80, 95],
  "algorithm": "auto",
  "adjustForEvents": true
}
```

| Parameter | Type | Default | Description |
|---|---|---|---|
| `metric` | string | required | Metric ID to forecast |
| `horizon` | integer | 30 | Forecast horizon in days (7, 30, or 90) |
| `periodStart` | ISO 8601 | 30 days ago | Start of historical data |
| `periodEnd` | ISO 8601 | Now | End of historical data |
| `granularity` | string | metric default | `day`, `week`, `month` |
| `filters` | object | none | Dimension filters |
| `confidenceLevels` | int[] | [80, 95] | Confidence interval percentages |
| `algorithm` | string | `auto` | `auto`, `sma`, `holt`, `holtwinters`, `arima`, `prophet` |
| `adjustForEvents` | boolean | true | Apply known event adjustments |

## Limitations

1. **New Products**: Products with less than 30 days of history fall back to category-level averages.
2. **Promotional Events**: Forecasts do not account for unplanned promotions. Use `adjustForEvents` with known promotion schedules.
3. **External Factors**: Weather, supply chain disruptions, and macroeconomic changes are not modeled.
4. **Data Gaps**: Historical periods with more than 20% missing data produce wider confidence intervals.
