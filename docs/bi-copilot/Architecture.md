# BI Copilot — Architecture

## Service Coordinates

- **Port:** 8104
- **Application:** `bi-copilot-service`
- **Package:** `com.sporekart.bi.copilot`
- **Spring Boot:** Java 21 + Spring Boot
- **API Spec:** `contracts/openapi/bi-copilot-service.yaml`

## Component Layers

```
┌─────────────────────────────────────────────────┐
│                  Controllers                     │
│  (REST endpoints — 13 endpoints)                │
├─────────────────────────────────────────────────┤
│                    Services                      │
│  (orchestrates engine calls, aggregates data)    │
├─────────────────────────────────────────────────┤
│              Analytics Engines (12)              │
├──────┬──────┬──────┬──────┬──────┬──────┬───────┤
│ Rev  │ Cust │Train │Cult  │Insight│Anom  │Forecast│
│Analyt│Analyt│Analyt│Analyt│Engine │Detect│Engine  │
│Engine│Engine│Engine│Engine│       │Engine│        │
├──────┴──────┴──────┴──────┼──────┼──────┼───────┤
│  CrossCopilotIntelligence │Trend│CustSeg│Dash  │
│  Engine                   │Detect│Engine │Engine│
├───────────────────────────┴──────┴──────┴───────┤
│            Infrastructure Layer                  │
│  DataAggregationClient  │  BiMetricsService      │
├─────────────────────────────────────────────────┤
│              Domain Records                      │
│  12 domain records + 14 DTOs                     │
└─────────────────────────────────────────────────┘
```

## 12 Analytics Engines

| # | Engine | Responsibility |
|---|--------|----------------|
| 1 | RevenueAnalyticsEngine | Revenue summaries, breakdowns, trends, AOV, growth |
| 2 | CustomerAnalyticsEngine | Customer metrics, churn, CLV, retention, satisfaction |
| 3 | TrainingAnalyticsEngine | Training summaries, performance, certifications |
| 4 | CultivationAnalyticsEngine | Yield, contamination, cycle time, grower metrics |
| 5 | BusinessInsightsEngine | Insight generation, daily briefing, cross-domain insights |
| 6 | TrendDetectionEngine | Moving average, seasonal factors, linear regression, growth detection |
| 7 | AnomalyDetectionEngine | Z-score anomaly detection, auto-resolution |
| 8 | ForecastingEngine | 4 methods: moving avg, exponential smoothing, linear regression, seasonal |
| 9 | CustomerSegmentationEngine | 6 segments: High-Value, New Grower, Hobbyist, Commercial, Distributor, At-Risk |
| 10 | ReportingEngine | Report generation, scheduling, executive/comprehensive reports |
| 11 | DashboardEngine | 3 dashboards, CRUD widgets, widget data generation |
| 12 | CrossCopilotIntelligenceEngine | Cross-copilot metrics, performance comparison, business health |

## Data Aggregation

The `DataAggregationClient` in the infrastructure layer handles external data source integration. Currently operates with seeded demonstration data for all engines.

## Cross-Copilot Intelligence

The `CrossCopilotIntelligenceEngine` aggregates metrics from Customer Copilot, Admin Copilot, Trainer Copilot, and Grower Copilot into unified dashboards and health scores.

## Monitoring

The `BiMetricsService` in `infrastructure/monitoring` provides Micrometer/Prometheus metrics for service observability via Actuator endpoints at `/actuator/health`, `/actuator/info`, `/actuator/metrics`, `/actuator/prometheus`.
