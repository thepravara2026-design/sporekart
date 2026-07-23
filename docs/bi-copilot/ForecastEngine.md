# Forecast Engine

## Overview

The Forecast Engine provides time-series forecasting across all business domains using four methods. It supports automatic method selection, accuracy calculation via MAPE, and seasonality detection.

## Forecasting Methods

### 1. Moving Average (MA)
- **Description**: Simple moving average over a configurable window
- **Best for**: Stable, non-seasonal data with low variance
- **Parameters**: `windowSize` (default: 3 periods)
- **Formula**: `MA_t = (1/n) * sum(X_{t-1} ... X_{t-n})`

### 2. Exponential Smoothing (ES)
- **Description**: Weighted average with exponential decay, more weight to recent observations
- **Best for**: Data with trend but no clear seasonality
- **Parameters**: `alpha` (smoothing factor, default: 0.3)
- **Formula**: `S_t = alpha * X_t + (1 - alpha) * S_{t-1}`

### 3. Linear Regression (LR)
- **Description**: Ordinary least squares linear trend projection
- **Best for**: Data with clear linear trend
- **Parameters**: Fitted from historical data
- **Formula**: `y = mx + b` where m and b are least-squares estimates

### 4. Seasonal Decomposition (Seasonal)
- **Description**: STL-inspired decomposition (trend + seasonal + residual) with seasonal component projection
- **Best for**: Data with strong repeating patterns
- **Parameters**: `seasonLength` (auto-detected via autocorrelation)
- **Formula**: `y = trend + seasonal + residual`

## Auto-Method Selection

When `method` is not specified, the engine evaluates historical data and selects the best method:

1. **Seasonality detection**: Autocorrelation function (ACF) at lags 6 and 12; if significant, prefer Seasonal
2. **Trend detection**: Mann-Kendall test on deseasonalized data; if significant trend, prefer LR
3. **Variance analysis**: Coefficient of variation; if low (<0.15), prefer MA
4. **Default**: Exponential Smoothing as fallback

## Accuracy Calculation

MAPE (Mean Absolute Percentage Error) is calculated using walk-forward validation:

```
MAPE = (100/n) * sum(|actual - forecast| / |actual|)
```

The last 3-6 periods are held out for validation depending on data availability.

## Seasonality Detection

- **Method**: Autocorrelation at seasonal lags (6-month and 12-month)
- **Threshold**: Lag autocorrelation > 0.3 indicates seasonality
- **Output**: `seasonality` field returns `"Strong"`, `"Moderate"`, or `"None"`

## Domain-Specific Forecasts

### Revenue Forecast
- **Metric values**: `grossRevenue`, `netRevenue`, `aov`, `orderCount`
- **Data source**: 24 months of historical revenue snapshots
- **Output**: Predicted revenue with confidence bands

### Orders Forecast
- **Metric values**: `orderCount`, `refundCount`
- **Data source**: Monthly order counts from revenue history

### Customer Forecast
- **Metric values**: `newCustomers`, `totalCustomers`, `retentionRate`
- **Data source**: 12 months of customer acquisition data

### Inventory Forecast
- **Metric values**: `stockLevel`, `turnoverRate`, `restockingQuantity`
- **Data source**: Current inventory state + 12 months sales velocity

### Training Enrollment Forecast
- **Metric values**: `enrollmentCount`, `completionRate`
- **Data source**: 12 months of training batch data

## Forecast Response

Each forecast returns a `BusinessForecast` object:

| Field | Description |
|-------|-------------|
| `forecastId` | Unique forecast identifier |
| `metric` | The metric being forecasted |
| `period` | Base period for the forecast |
| `method` | Chosen/requested forecasting method |
| `points` | Array of `{period, value, lowerBound, upperBound}` |
| `confidenceInterval` | Confidence level (e.g., 0.95) |
| `accuracy` | MAPE score (lower is better) |
| `seasonality` | Detected seasonality level |
| `trend` | Trend direction (up, down, stable) |
| `recommendations` | Business recommendations based on forecast |

## Configuration

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `metric` | string | (required) | Metric to forecast |
| `period` | string | `current` | Base period (`current` or `yyyy-MM`) |
| `horizon` | integer | 90 | Number of days to forecast |
| `method` | string | auto | Forecast method: `ma`, `es`, `lr`, `seasonal` |
