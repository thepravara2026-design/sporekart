# Executive Copilot API Reference

**Base Path**: `/api/v1/copilot/executive`  
**Port**: 8109  
**Auth**: Authenticated (JWT via Gateway)

All responses wrapped in `ExecutiveCopilotResponse<T>`:
```json
{ "success": true, "message": "Success", "data": { ... }, "errorCode": null }
```

## Endpoints

### Chat & Streaming

| Method | Path | Description | Request Body |
|--------|------|-------------|-------------|
| POST | `/chat` | Natural language executive query | `{ "query": "...", "intent": "...", "period": "?","department": "?","forecastType": "?" }` |
| POST | `/stream` | NL query (placeholder for SSE streaming) | Same as `/chat` |

**Intents**: `company_performance`, `biggest_risk`, `weekly_focus`, `revenue_drop`, `expansion`, `training`, `profitability`

### Dashboard

| Method | Path | Description |
|--------|------|-------------|
| GET | `/dashboard` | Full executive dashboard (health, financial highlights, recommendations, risks, metrics, alerts) |
| GET | `/dashboard/today` | Today's summary (orders, revenue, new customers, alerts) |
| GET | `/dashboard/weekly` | Weekly executive report |
| GET | `/dashboard/monthly` | Monthly executive report |

### Reports

| Method | Path | Description | Request Body |
|--------|------|-------------|-------------|
| POST | `/report` | Generate board/quarterly report | `{ "reportType": "quarterly|annual", "period": "?" }` |

### Forecasting

| Method | Path | Description | Params/Body |
|--------|------|-------------|-------------|
| POST | `/forecast` | Business forecasting | `{ "forecastType": "revenue|orders|customers|inventory", "horizonMonths": 12 }` |
| GET | `/forecast/revenue` | Revenue forecast | `?months=12` |
| GET | `/forecast/orders` | Order forecast | `?months=12` |

### Risk Intelligence

| Method | Path | Description |
|--------|------|-------------|
| POST | `/risk` | Assess business risks |
| GET | `/risk/matrix` | Risk matrix (critical/high/medium/low) |
| GET | `/risk/top` | Top N risks | `?count=5` |
| GET | `/risk/revenue-decline` | Revenue decline risk detection |

### Company Health

| Method | Path | Description |
|--------|------|-------------|
| GET | `/health` | Overall company health report |
| GET | `/health/dimension/{dimension}` | Health for specific dimension |
| GET | `/health/sustainability` | Business sustainability assessment |
| GET | `/health/cashflow` | Cash flow indicators |

### Financial Intelligence

| Method | Path | Description |
|--------|------|-------------|
| POST | `/financial` | Financial analysis | `{ "metric": "?", "period": "?" }` |
| GET | `/financial/revenue-breakdown` | Revenue breakdown by product/region/channel |
| GET | `/financial/profit-analysis` | Profit analysis (gross/net/EBITDA margins) |
| GET | `/financial/trends` | Financial trends | `?period=quarterly` |

### Strategic Decisions

| Method | Path | Description |
|--------|------|-------------|
| POST | `/decision` | Strategic recommendation | `{ "intent": "expansion|market_entry|pricing|investment" }` |
| GET | `/decision/simulate` | Simulate decision impact | `?decision=...&investment=...` |

### Performance Analytics

| Method | Path | Description |
|--------|------|-------------|
| POST | `/performance` | Cross-departmental performance | `{ "department": "?" }` |
| GET | `/performance/department/{dept}` | Department detail |
| GET | `/performance/cross-departmental` | Cross-departmental metrics |

### Market Intelligence

| Method | Path | Description |
|--------|------|-------------|
| POST | `/market` | Market analysis | `{ "segment": "?", "region": "?" }` |
| GET | `/market/benchmark` | Competitor benchmarking | `?metric=overall` |
| GET | `/market/trends` | Industry trends | `?sector=mushroom_cultivation` |

## Domain Models (17)

CompanyHealth, HealthDimension, FinancialMetric, RevenueBreakdown, ProfitAnalysis, BusinessForecast, StrategicRecommendation, BusinessRisk, RiskMatrix, ExecutiveDashboard, PerformanceMetric, BoardReport, MarketIntelligence, NaturalLanguageQuery, DepartmentPerformance, CashFlowIndicators, BusinessSustainability

## Test Coverage

184 tests across 14 test classes — 0 failures. Every engine, orchestrator, controller, and metrics service tested with mocks and real instances.
