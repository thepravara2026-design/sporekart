# Cross-Copilot Intelligence

## Overview

The `CrossCopilotIntelligenceEngine` aggregates and compares performance metrics across all SporeKart copilots for unified business intelligence.

## Copilots Tracked

| Copilot | Revenue Share | Avg Active Sessions | Avg Latency | Satisfaction |
|---------|--------------|--------------------|-------------|--------------|
| Customer Copilot | 45% | 850 | 120ms | 4.5/5 |
| Admin Copilot | 20% | 320 | 95ms | 4.2/5 |
| Trainer Copilot | 15% | 180 | 145ms | 4.3/5 |
| Grower Copilot | 20% | 420 | 110ms | 4.6/5 |

## Metrics per Copilot

- **Revenue** — Share of total platform revenue
- **Active Sessions** — Concurrent user sessions
- **Avg Latency** — API response time in milliseconds
- **Satisfaction** — User satisfaction score (1–5)
- **Total Queries** — Aggregate query volume
- **Resolution Rate** — Issue resolution percentage

## Unified Business Health

A composite health score calculated as average of domain health scores:

```
Health = avg(revenueHealth, customerHealth, trainingHealth, cultivationHealth)
```

- **>= 80:** healthy
- **60–79:** warning
- **< 60:** critical

## Correlation Insights

| Insight | Correlation | Action |
|---------|-------------|--------|
| Customer Engagement Drives Revenue | 0.87 | Invest in CX and retention |
| Training Impact on Yield Quality | 0.82 | Expand grower training |
| Seasonal Demand Patterns | 0.76 | Plan Q4 inventory/marketing |
| Wholesale Channel Growth | 0.79 | Allocate resources to wholesale |

## Weekly Digest

The engine produces a weekly digest with cross-copilot summary metrics, copilot performance breakdowns, and top correlation insights.

## API Endpoints

| Endpoint | Description |
|----------|-------------|
| `GET /v1/bi/cross-copilot/metrics` | All cross-copilot metrics |
| `GET /v1/bi/cross-copilot/comparison` | Performance comparison across copilots |
| `GET /v1/bi/cross-copilot/health` | Unified business health score |
| `GET /v1/bi/cross-copilot/insights` | Correlation insights |
| `GET /v1/bi/cross-copilot/revenue-contribution` | Revenue contribution by copilot |
| `GET /v1/bi/cross-copilot/digest` | Weekly digest summary |
