# BI Copilot API Reference

## Endpoints

### 1. BI Query
```
POST /v1/bi/query
```
Execute a natural-language or structured BI query against any data source.

**Request:**
```json
{
  "query": "total revenue by region for last quarter",
  "dataSource": "revenue",
  "filters": {"region": "North"},
  "groupBy": ["region"],
  "metrics": ["totalRevenue"],
  "sortBy": "totalRevenue",
  "sortOrder": "desc",
  "page": 1,
  "size": 10
}
```

**Response:**
```json
{
  "results": [{"region": "North", "totalRevenue": 980000.0}],
  "total": 5,
  "page": 1,
  "size": 10,
  "queryTimeMs": 45,
  "queryExplanation": "Revenue grouped by region"
}
```

### 2. Chat
```
POST /v1/bi/chat
```
Natural-language conversation interface.

**Request:**
```json
{
  "message": "show me last month's revenue",
  "sessionId": "sess-123",
  "section": "revenue",
  "dashboardId": "DASH_EXEC"
}
```

**Response:**
```json
{
  "sessionId": "sess-123",
  "message": "Last month's total revenue was Rs.5,80,000",
  "suggestions": [{"label": "View breakdown", "action": "revenue_breakdown"}],
  "context": {"period": "2025-12"},
  "streaming": false
}
```

### 3. Revenue Summary
```
GET /v1/bi/revenue/summary?period=2025-12
```
Revenue metrics for a given period (defaults to latest month).

### 4. Customer Summary
```
GET /v1/bi/customer/summary?period=2025-12
```
Customer analytics for a given period (defaults to latest month).

### 5. Training Summary
```
GET /v1/bi/training/summary?period=2025-12
```
Training analytics for a given period (defaults to latest month).

### 6. Cultivation Summary
```
GET /v1/bi/cultivation/summary?period=2025-12
```
Cultivation analytics for a given period (defaults to latest month).

### 7. Business Insights
```
GET /v1/bi/insights?category=revenue&period=2025-12
```
Insights filtered by category and period.

### 8. Anomalies
```
GET /v1/bi/anomalies?period=2025-12
```
Active anomaly alerts for the given period.

### 9. Trends
```
GET /v1/bi/trends?metric=revenue&months=12
```
Trend data points for a specified metric.

### 10. Forecast
```
POST /v1/bi/forecast
```
Generate a forecast for a metric.

**Request:**
```json
{
  "metric": "revenue",
  "method": "seasonal",
  "horizon": 6,
  "parameters": {}
}
```

**Response:**
```json
{
  "forecastId": "uuid",
  "metric": "revenue",
  "method": "seasonal",
  "points": [
    {"period": "2026-01", "predictedValue": 620000.0, "lowerBound": 580000.0, "upperBound": 660000.0}
  ],
  "confidenceInterval": 0.95,
  "recommendations": "Strong upward trend - prepare for increased demand"
}
```

### 11. Customer Segments
```
GET /v1/bi/customer/segments
```
Return all customer segments with metrics.

### 12. Reports
```
POST /v1/bi/reports
```
Generate or schedule a report.

**Request:**
```json
{
  "reportType": "comprehensive",
  "format": "PDF",
  "schedule": "0 0 8 * * MON",
  "metrics": ["revenue", "customers"],
  "dimensions": ["region", "segment"],
  "filters": {"period": "2025"},
  "recipients": ["exec@sporekart.example"]
}
```

**Response:**
```json
{
  "reportId": "uuid",
  "name": "Comprehensive Report",
  "status": "completed",
  "format": "PDF",
  "downloadUrl": "/v1/bi/reports/uuid/download",
  "summary": {"totalRevenue": 2450000.0},
  "generatedAt": "2025-12-01T08:00:00Z"
}
```

### 13. Cross-Copilot Metrics
```
GET /v1/bi/cross-copilot/metrics
```
Aggregated metrics across all copilots.

## Error Responses

All errors return `application/problem+json`:
```json
{
  "type": "https://api.sporekart.example/errors/bad-request",
  "title": "Bad Request",
  "status": 400,
  "detail": "Invalid period format. Expected yyyy-MM.",
  "instance": "/v1/bi/revenue/summary"
}
```

## Common Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| period | string | yyyy-MM format, defaults to latest |
| metric | string | Metric identifier |
| category | string | revenue, customer, training, cultivation, cross-domain |
| format | string | PDF, EXCEL, CSV, HTML |
| method | string | moving_average, exponential_smoothing, linear_regression, seasonal |
