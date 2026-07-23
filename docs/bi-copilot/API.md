# BI Copilot API Reference

Base URL: `http://localhost:8104`

All endpoints require JWT bearer authentication (except `GET /actuator/health`).

## Endpoints

### 1. Forecast

**POST** `/v1/bi/forecast`

Generate business forecasts using any of the four methods (MA, ES, LR, Seasonal).

Request:
```json
{
  "metric": "grossRevenue",
  "period": "current",
  "horizon": 90,
  "method": "es"
}
```

Response:
```json
{
  "forecast": {
    "forecastId": "f-c09f8e7a",
    "metric": "grossRevenue",
    "period": "2026-07",
    "method": "exponential_smoothing",
    "points": [
      {"period": "2026-08", "value": 485000, "lowerBound": 425000, "upperBound": 545000},
      {"period": "2026-09", "value": 472000, "lowerBound": 408000, "upperBound": 536000}
    ],
    "confidenceInterval": 0.95,
    "accuracy": 8.3,
    "seasonality": "Moderate",
    "trend": "up",
    "recommendations": "Revenue trending upward. Consider increasing inventory for peak season."
  },
  "visualizations": [...]
}
```

---

### 2. Health Score

**GET** `/v1/bi/health-score?period=current`

Get the company health score composite.

Response:
```json
{
  "healthScore": {
    "overall": 74.5,
    "revenueScore": 82.0,
    "customerScore": 68.0,
    "trainingScore": 79.0,
    "inventoryScore": 71.0,
    "operationsScore": 76.0,
    "growthScore": 65.0,
    "period": "2026-07",
    "trend": "improving",
    "factors": [
      {"name": "Revenue Growth", "score": 82.0, "status": "good", "description": "Revenue increased 12.5% MoM"}
    ],
    "calculatedAt": "2026-07-23T12:00:00Z"
  },
  "scoreVisualizations": [...]
}
```

---

### 3. Insights

**GET** `/v1/bi/insights?category=revenue&period=current&limit=10&actionableOnly=true`

Get business insights.

Response:
```json
{
  "insights": [
    {
      "insightId": "ins-a1b2c3",
      "title": "Revenue Decline in Mushroom Products",
      "description": "Mushroom Products category declined 8.2% MoM driven by Fresh Oyster Mushroom",
      "category": "revenue",
      "severity": "important",
      "confidenceScore": 0.87,
      "businessImpact": "Potential revenue loss of INR 34,500 if trend continues",
      "actionItems": ["Review pricing", "Launch promotion"],
      "supportingData": {"category": "Mushroom Products", "moMChange": -8.2},
      "generatedAt": "2026-07-23T12:00:00Z"
    }
  ],
  "total": 15,
  "critical": 2,
  "important": 5,
  "info": 8
}
```

---

### 4. Decision Support

**GET** `/v1/bi/decision-support?focus=revenue&period=current`

Get prioritized business recommendations.

Response:
```json
{
  "recommendations": [
    {
      "recommendationId": "rec-d4e5f6",
      "title": "Revenue Recovery Opportunity",
      "description": "Target Home Growers segment with promotional pricing",
      "category": "revenue",
      "priority": "High",
      "expectedImpact": "Potential recovery of INR 85,000 monthly",
      "confidenceScore": 0.82,
      "rationale": "Home Growers segment revenue dropped 15% while customer count remained stable",
      "supportingData": {"segment": "Home Growers", "moMChange": -15.0},
      "actionItems": ["Create targeted promotion", "Adjust pricing"],
      "implemented": false,
      "createdAt": "2026-07-23T12:00:00Z"
    }
  ],
  "focus": "revenue",
  "period": "2026-07"
}
```

---

### 5. KPI

**GET** `/v1/bi/kpi?period=current`

Get key performance indicators.

Response:
```json
{
  "kpis": [
    {
      "id": "kpi-revenue",
      "name": "Gross Revenue",
      "value": 855000.0,
      "previousValue": 760000.0,
      "changePercent": 12.5,
      "trend": "up",
      "unit": "INR",
      "status": "good"
    }
  ],
  "period": "2026-07",
  "timestamp": "2026-07-23T12:00:00Z"
}
```

---

### 6. Dashboard

**GET** `/v1/bi/dashboard?type=executive&period=current`

Get a curated executive dashboard with summary and visualizations.

Response:
```json
{
  "summary": {
    "summaryId": "sum-abc123",
    "period": "2026-07",
    "type": "executive",
    "healthScore": {...},
    "revenue": {...},
    "customers": {...},
    "products": {...},
    "inventory": {...},
    "training": {...},
    "insights": [...],
    "recommendations": [...],
    "risks": [...],
    "generatedAt": "2026-07-23T12:00:00Z"
  },
  "visualizations": [...],
  "generatedAt": "2026-07-23T12:00:00Z"
}
```

---

### 7. Report Generation

**POST** `/v1/bi/reports`

Generate an executive report.

Request:
```json
{
  "reportType": "monthly",
  "format": "pdf",
  "period": "2026-07",
  "sections": ["revenue", "customers", "inventory"],
  "recipients": ["exec@sporekart.example"]
}
```

Response:
```json
{
  "reportId": "rpt-xyz789",
  "title": "Monthly Executive Report - July 2026",
  "format": "pdf",
  "status": "generated",
  "downloadUrl": "/v1/bi/reports/rpt-xyz789/download",
  "pageCount": 12,
  "generatedAt": "2026-07-23T12:00:00Z"
}
```

---

### 8. Risk Alerts

**GET** `/v1/bi/risks?severity=all&period=current`

Get risk alerts with severity classification.

Response:
```json
{
  "risks": [
    {
      "riskId": "risk-a1b2",
      "riskType": "inventory",
      "severity": "high",
      "title": "Multiple Products Below Safety Stock",
      "description": "8 products across Equipment and Substrates categories are below safety stock levels",
      "probability": 0.75,
      "impact": 0.6,
      "affectedArea": "Inventory",
      "recommendedAction": "Place urgent reorder for critical items",
      "status": "open",
      "detectedAt": "2026-07-23T12:00:00Z"
    }
  ],
  "total": 5,
  "critical": 1,
  "high": 2,
  "open": 5,
  "period": "2026-07"
}
```

---

### 9. Natural Language Query

**POST** `/v1/bi/query`

Query business data using natural language.

Request:
```json
{
  "query": "Show me revenue by region for last month",
  "generateVisualization": true,
  "includeExplanation": true
}
```

Response:
```json
{
  "intent": "revenue_by_region",
  "explanation": "Showing gross revenue distribution across regions for June 2026",
  "data": {
    "Maharashtra": 256500.0,
    "Karnataka": 188100.0,
    "Tamil Nadu": 153900.0,
    "Punjab": 145350.0,
    "Himachal": 111150.0
  },
  "visualization": {...},
  "insights": [...],
  "suggestedFollowUp": "Would you like to see the trend for Maharashtra?"
}
```

---

### 10. Chat (Conversational)

**POST** `/v1/bi/chat`

Continue a conversational analytics session.

Request:
```json
{
  "message": "What were our top 5 products last month?",
  "sessionId": "sess-abc123",
  "pageUrl": "/bi/dashboard",
  "pageTitle": "Executive Dashboard",
  "section": "products"
}
```

Response:
```json
{
  "sessionId": "sess-abc123",
  "message": "Here are your top 5 products by revenue for June 2026...",
  "suggestions": [
    {"label": "Show revenue trend", "value": "Show me the revenue trend for these products"},
    {"label": "Compare with previous month", "value": "Compare top 5 with previous month"}
  ],
  "context": {"dashboardType": "executive", "period": "2026-06"},
  "streaming": false
}
```

---

### 11. Revenue Analytics

**GET** `/v1/bi/revenue?period=current`

Get comprehensive revenue analytics.

---

### 12. Customer Analytics

**GET** `/v1/bi/customers?period=current`

Get customer analytics.

---

### 13. Product Analytics

**GET** `/v1/bi/products?period=current`

Get product analytics.

---

### 14. Inventory Analytics

**GET** `/v1/bi/inventory?period=current`

Get inventory analytics.

---

### 15. Training Analytics

**GET** `/v1/bi/training?period=current`

Get training analytics.

---

## Common Query Parameters

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `period` | string | `current` | `current` or `yyyy-MM` format |
| `limit` | integer | 10 | Max results (where applicable) |
| `category` | string | - | Filter by category |
| `severity` | string | `all` | Filter by severity level |

## Error Response Format

All errors return RFC 9457 Problem Details:

```json
{
  "type": "https://api.sporekart.example/errors/bad-request",
  "title": "Bad Request",
  "status": 400,
  "detail": "Invalid period format. Expected 'current' or 'yyyy-MM'",
  "instance": "/v1/bi/forecast"
}
```
