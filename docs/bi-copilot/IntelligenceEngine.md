# Intelligence Engine

## Business Insights Engine

**Class:** `BusinessInsightsEngine`

Generates templated and dynamic business insights across all domains.

### Insight Categories

| Category | Count | Examples |
|----------|-------|----------|
| revenue | 8 | Growth acceleration, regional declines, AOV trends, channel shifts |
| customer | 4 | Churn, satisfaction, retention, high-value segment growth |
| training | 4 | Completion rates, curriculum issues, profitability |
| cultivation | 5 | Yield best practices, contamination, cycle time, disease |
| cross-domain | 4 | Training-LTV correlation, spawn-yield correlation, upsell pipeline |

### Methods

| Method | Description |
|--------|-------------|
| `generateInsights(category, period)` | Filtered insights for a domain |
| `generateRevenueInsights(metrics)` | Dynamic revenue insights from metric data |
| `generateCustomerInsights(analytics)` | Dynamic customer insights from analytics |
| `generateTrainingInsights(analytics)` | Dynamic training insights |
| `generateCultivationInsights(analytics)` | Dynamic cultivation insights |
| `generateCrossDomainInsights()` | Cross-domain correlation insights |
| `getTopInsights(limit)` | Highest confidence insights |
| `getActionableInsights()` | Filtered to actionable insights only |
| `generateDailyBriefing()` | Priority-ranked briefing with recommended actions |

### Confidence Scoring

Insights are scored 0.0–1.0 based on data quality, recency, and cross-domain correlation strength.

## Trend Detection Engine

**Class:** `TrendDetectionEngine`

### Methodology

1. **Moving Average** — Simple n-period trailing average (window=3)
2. **Seasonal Factors** — Period-over-period ratio computation
3. **Linear Regression** — Ordinary least squares line fitting
4. **Deviation Analysis** — Observed vs trend line
5. **Growth Acceleration** — Compares recent 3-period avg vs prior 3-period avg

### Methods

| Method | Description |
|--------|-------------|
| `detectTrends(metric, dataPoints)` | Full trend analysis pipeline |
| `calculateMovingAverage(data, window)` | Simple moving average |
| `calculateSeasonalFactors(data, seasonLength)` | Seasonal factor computation |
| `detectGrowthAcceleration(trends)` | Acceleration/deceleration detection |
| `comparePeriods(current, previous, metric)` | Period comparison |
| `getTopTrends(limit)` | Top trends sorted by deviation magnitude |

## Anomaly Detection Engine

**Class:** `AnomalyDetectionEngine`

### Methodology

Z-score based statistical anomaly detection:
- **Anomaly threshold:** z-score > 2.5
- **Critical threshold:** z-score > 3.5
- Maintains metric history (up to 50 points per metric)
- Auto-resolution when recent values return below threshold

### Active Anomalies (Seeded)

| Metric | Period | Observed | Expected | Severity |
|--------|--------|----------|----------|----------|
| revenue | 2025-10 | Rs.680,000 | Rs.520,000 | critical |
| orders | 2025-11 | 3200 | 2400 | anomaly |

### Methods

| Method | Description |
|--------|-------------|
| `detectAnomalies(metric, dataPoints)` | Z-score anomaly detection on data points |
| `detectAnomaliesAcrossMetrics()` | Run detection across 5 default metrics |
| `getActiveAnomalies()` | List all active unresolved anomalies |
| `resolveAnomaly(anomalyId)` | Manually resolve an anomaly |
| `getAnomalySummary()` | Aggregated summary by severity and metric |
| `autoResolveAnomalies(metric)` | Auto-resolve if recent data within threshold |

## Forecasting Engine

**Class:** `ForecastingEngine`

### Methods

| Method | Description |
|--------|-------------|
| `forecast(metric, method, horizon, data)` | Generic forecast with method selection |
| `movingAverageForecast(data, window, horizon)` | Moving average extrapolation |
| `exponentialSmoothingForecast(data, alpha, horizon)` | Simple exponential smoothing |
| `linearRegressionForecast(data, horizon)` | OLS trend projection |
| `seasonalForecast(data, seasonLength, horizon)` | Trend + seasonal decomposition |
| `recommendMethod(metric, data)` | Auto-select best method based on data characteristics |
| `calculateAccuracy(actual, predicted)` | MAPE-based accuracy (100% = perfect) |
| `forecastRevenue(months)` | Revenue forecast convenience method |
| `forecastCustomerGrowth(months)` | Customer growth forecast |
| `forecastYield(species, months)` | Yield forecast per species |

### Method Selection Logic

Checks seasonality (autocorrelation at lag 4 vs lag 1), trend strength (R-squared), and volatility (CV) to recommend the optimal method.
