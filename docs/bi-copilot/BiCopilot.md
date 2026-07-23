# BI Copilot — Overview

The **Business Intelligence (BI) Copilot** is SporeKart's central analytics and insights service. It provides cross-domain analytics, interactive dashboards, scheduled reporting, forecasting, and intelligence across Revenue, Customer, Training, and Cultivation domains.

## Purpose

- Unify analytics from all SporeKart business domains into a single service
- Deliver real-time and historical business intelligence via REST APIs
- Generate actionable insights, detect anomalies, and forecast trends
- Provide cross-copilot performance comparison and unified business health scoring

## Domains

| Domain | Description |
|--------|-------------|
| Revenue | Revenue summaries, product/category/region breakdowns, AOV, growth rates |
| Customer | Customer counts, churn/retention, segments, CLV, satisfaction |
| Training | Student performance, batch metrics, certification rates, profitability |
| Cultivation | Yield tracking, contamination rates, cycle times, grower metrics |

## Key Features

- 12 analytics engines for domain-specific computation
- Natural-language BI chat interface
- Pre-seeded demonstration data for all domains
- Configurable dashboard refresh intervals and data retention
- Scheduled report generation in PDF, Excel, CSV, HTML
- Cross-copilot intelligence aggregation
- Z-score based anomaly detection
- Multiple forecasting methods (moving average, exponential smoothing, linear regression, seasonal)

## Configuration

The service is configured via `sporekart.bi.copilot.*` properties in `application.yml`:

| Property | Default | Description |
|----------|---------|-------------|
| enabled | true | Master toggle |
| data-retention-days | 90 | Data retention period |
| report-formats | PDF,EXCEL,CSV,HTML | Supported export formats |
| dashboard-refresh-interval | 300s | Auto-refresh interval |
| forecast-horizon-days | 365 | Max forecast horizon |
| max-query-result-size | 10000 | Max rows per query |
| cache-ttl | 5m | Cache time-to-live |
| audit-enabled | true | Audit logging toggle |

## Service Port

The BI Copilot runs on port **8104**.
