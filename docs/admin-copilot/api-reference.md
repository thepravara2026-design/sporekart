# API Reference

## Base URL

```
Production:  https://api.sporekart.example/api/v1/copilot/admin
Staging:     https://staging.api.sporekart.example/api/v1/copilot/admin
Local:       http://localhost:8100/api/v1/copilot/admin
```

## Authentication

All endpoints require a Bearer JWT token in the `Authorization` header.

```
Authorization: Bearer <jwt-token>
```

The token must contain roles in the `roles` claim. Admin Copilot endpoints require one of:
- `ADMIN` — full access
- `ANALYST` — read-only access

### Error Response Format

All errors follow RFC 9457 (Problem Details):

```json
{
  "type": "https://api.sporekart.example/errors/authentication-failed",
  "title": "Authentication Failed",
  "status": 401,
  "detail": "The JWT token is expired or invalid.",
  "instance": "/api/v1/copilot/admin/dashboard"
}
```

### Error Codes

| Status | Type | Title | Description |
|---|---|---|---|
| 400 | `invalid-request` | Invalid Request | Malformed payload or missing required fields |
| 401 | `authentication-failed` | Authentication Failed | Missing, expired, or invalid JWT |
| 403 | `insufficient-permissions` | Insufficient Permissions | Valid token but missing required role |
| 404 | `not-found` | Resource Not Found | Report job, forecast, or insight not found |
| 415 | `unsupported-media-type` | Unsupported Media Type | Content-Type must be application/json |
| 429 | `rate-limit-exceeded` | Rate Limit Exceeded | Too many requests |
| 500 | `internal-error` | Internal Server Error | Unexpected server error |
| 502 | `bad-gateway` | Bad Gateway | Upstream service unavailable |
| 504 | `gateway-timeout` | Gateway Timeout | Upstream service timed out |

### Rate Limiting

| Endpoint Group | Limit | Window |
|---|---|---|
| Chat / Stream | 30 requests | per minute |
| Dashboard | 60 requests | per minute |
| Insights | 30 requests | per minute |
| Report generation | 10 requests | per minute |
| Forecast | 20 requests | per minute |
| Alerts | 30 requests | per minute |

Rate limit headers are included in all responses:

```
X-RateLimit-Limit: 30
X-RateLimit-Remaining: 28
X-RateLimit-Reset: 1627034100
```

---

## POST /chat

Send a natural language message to the Admin Copilot and receive a response.

**Request:**

```
POST /api/v1/copilot/admin/chat
Content-Type: application/json
Authorization: Bearer <token>
```

```json
{
  "message": "What were our sales yesterday?",
  "context": {
    "timezone": "America/New_York",
    "dashboardId": "default"
  }
}
```

**Response (200):**

```json
{
  "reply": "Yesterday (July 22, 2026) your total revenue was $38,900 from 128 orders. The average order value was $47.50. Compared to the previous day, revenue increased 9.3% and orders were up 10.9%.",
  "structuredData": {
    "revenue": 38900.00,
    "orders": 128,
    "averageOrderValue": 47.50,
    "revenueChange": 9.3,
    "ordersChange": 10.9,
    "period": "2026-07-22"
  },
  "suggestedFollowUps": [
    "Which products drove the most sales yesterday?",
    "How does this compare to last week?",
    "Show me the sales trend for this month."
  ],
  "correlationId": "corr-chat-001"
}
```

### Schema Definitions

**ChatRequest:**

| Field | Type | Required | Description |
|---|---|---|---|
| `message` | string | yes | Natural language query (3-2000 chars) |
| `context` | object | no | Contextual parameters |
| `context.timezone` | string | no | IANA timezone (default: UTC) |
| `context.dashboardId` | string | no | Dashboard context identifier |
| `context.locale` | string | no | Language locale (default: en-US) |

**ChatResponse:**

| Field | Type | Description |
|---|---|---|
| `reply` | string | Natural language response |
| `structuredData` | object | Parsed data payload (optional) |
| `suggestedFollowUps` | string[] | Suggested follow-up questions |
| `correlationId` | string | Trace identifier for support |

### curl Example

```bash
curl -X POST https://api.sporekart.example/api/v1/copilot/admin/chat \
  -H "Authorization: Bearer eyJhbGciOiJSUzI1NiIs..." \
  -H "Content-Type: application/json" \
  -d '{
    "message": "What were our sales yesterday?",
    "context": {
      "timezone": "America/New_York"
    }
  }'
```

---

## POST /stream

Send a message and receive a streaming (SSE) response for real-time token-by-token output.

**Request:**

```
POST /api/v1/copilot/admin/stream
Content-Type: application/json
Authorization: Bearer <token>
Accept: text/event-stream
```

```json
{
  "message": "Show me the dashboard summary",
  "context": {}
}
```

**Response (200, Server-Sent Events):**

```
id: 1
event: token
data: {"token": "Here", "index": 0}

id: 2
event: token
data: {"token": " is", "index": 1}

id: 3
event: token
data: {"token": " your", "index": 2}

id: 4
event: token
data: {"token": " dashboard", "index": 3}

...

id: 25
event: done
data: {"correlationId": "corr-stream-001", "totalTokens": 25, "structuredData": { ... }}
```

**Events:**

| Event | Description | Data |
|---|---|---|
| `token` | A single token of the response | `{"token": "...", "index": N}` |
| `done` | Streaming complete | `{"correlationId": "...", "totalTokens": N, "structuredData": {...}}` |
| `error` | An error occurred | `{"code": "...", "message": "..."}` |

### curl Example

```bash
curl -N -X POST https://api.sporekart.example/api/v1/copilot/admin/stream \
  -H "Authorization: Bearer eyJhbGciOiJSUzI1NiIs..." \
  -H "Content-Type: application/json" \
  -H "Accept: text/event-stream" \
  -d '{
    "message": "Show me the dashboard summary"
  }'
```

---

## GET /dashboard

Retrieve the current admin dashboard snapshot.

**Request:**

```
GET /api/v1/copilot/admin/dashboard
Authorization: Bearer <token>
```

**Query Parameters:**

| Parameter | Type | Default | Description |
|---|---|---|---|
| `period` | string | `today` | `today`, `yesterday`, `this_week`, `this_month`, or custom `start:end` |
| `includeHistory` | boolean | false | Include 7-day trend data |

**Response (200):**

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
    "ordersChangePercent": 10.9,
    "averageOrderValue": 47.50,
    "aovYesterday": 45.20,
    "conversionRate": 3.8,
    "topProducts": [
      { "rank": 1, "name": "Oyster Mushroom Kit", "revenue": 8500.00, "units": 180 },
      { "rank": 2, "name": "Shiitake Spawn Log", "revenue": 6200.00, "units": 95 }
    ]
  },
  "customers": {
    "newToday": 18,
    "newYesterday": 22,
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
    "revenueMonth": 28500.00,
    "utilizationRate": 78.5
  },
  "platform": {
    "healthStatus": "HEALTHY",
    "services": [
      { "name": "analytics", "status": "UP", "latencyMs": 45 },
      { "name": "order", "status": "UP", "latencyMs": 32 },
      { "name": "inventory", "status": "UP", "latencyMs": 28 },
      { "name": "payment", "status": "UP", "latencyMs": 120 }
    ],
    "uptimeToday": 99.98,
    "errorRate": 0.02
  }
}
```

### Schema: AdminDashboard

| Field | Type | Description |
|---|---|---|
| `snapshotTime` | ISO 8601 | When the snapshot was captured |
| `period` | object | The time period of the snapshot |
| `sales` | object | Sales metrics (revenue, orders, AOV, conversion, topProducts) |
| `customers` | object | Customer metrics (new, active, repeatRate) |
| `inventory` | object | Inventory metrics (stock, value, turnover) |
| `training` | object | Training metrics (enrollments, completion, revenue) |
| `platform` | object | Platform health summary |

### curl Example

```bash
curl https://api.sporekart.example/api/v1/copilot/admin/dashboard \
  -H "Authorization: Bearer eyJhbGciOiJSUzI1NiIs..."
```

---

## POST /report

Generate a report asynchronously.

**Request:**

```
POST /api/v1/copilot/admin/report
Content-Type: application/json
Authorization: Bearer <token>
```

```json
{
  "type": "sales",
  "format": "csv",
  "periodStart": "2026-07-01T00:00:00Z",
  "periodEnd": "2026-07-23T23:59:59Z",
  "granularity": "day",
  "metrics": ["revenue", "orders", "aov", "conversion"],
  "dimensions": ["product", "category"],
  "filters": {
    "categoryIds": ["kits", "logs"],
    "region": "us-west"
  },
  "compareWith": "previous_period"
}
```

**Response (202):**

```json
{
  "jobId": "report-job-abc123",
  "status": "pending",
  "estimatedCompletion": "2026-07-23T09:35:05Z",
  "downloadUrl": null
}
```

### Polling

```
GET /api/v1/copilot/admin/report/{jobId}
Authorization: Bearer <token>
```

**Response (200, completed):**

```json
{
  "jobId": "report-job-abc123",
  "status": "completed",
  "estimatedCompletion": "2026-07-23T09:35:05Z",
  "completedAt": "2026-07-23T09:35:04Z",
  "downloadUrl": "/api/v1/copilot/admin/report/report-job-abc123/download",
  "expiresAt": "2026-07-24T09:35:04Z",
  "fileSize": 245000,
  "recordCount": 3450,
  "type": "sales",
  "format": "csv",
  "periodStart": "2026-07-01T00:00:00Z",
  "periodEnd": "2026-07-23T23:59:59Z"
}
```

### Schema: ReportRequest

| Field | Type | Required | Default | Description |
|---|---|---|---|---|
| `type` | string | yes | — | `dashboard_snapshot`, `sales`, `inventory`, `customer`, `training` |
| `format` | string | yes | — | `csv` |
| `periodStart` | ISO 8601 | no | 30 days ago | Start of report period |
| `periodEnd` | ISO 8601 | no | Now | End of report period |
| `granularity` | string | no | `day` | `hour`, `day`, `week`, `month` |
| `metrics` | string[] | no | All | Metrics to include (see reporting guide) |
| `dimensions` | string[] | no | Product-level | Dimensions to break down by |
| `filters` | object | no | None | Dimension filters |
| `compareWith` | string | no | `none` | `none`, `previous_period`, `year_over_year` |

### curl Example

```bash
curl -X POST https://api.sporekart.example/api/v1/copilot/admin/report \
  -H "Authorization: Bearer eyJhbGciOiJSUzI1NiIs..." \
  -H "Content-Type: application/json" \
  -d '{
    "type": "sales",
    "format": "csv",
    "periodStart": "2026-07-01T00:00:00Z",
    "periodEnd": "2026-07-23T23:59:59Z",
    "granularity": "day",
    "metrics": ["revenue", "orders", "aov"]
  }'
```

---

## GET /insights

Retrieve generated business insights.

**Request:**

```
GET /api/v1/copilot/admin/insights
Authorization: Bearer <token>
```

**Query Parameters:**

| Parameter | Type | Default | Description |
|---|---|---|---|
| `category` | string | all | Filter by category: `sales`, `customer`, `inventory`, `operational`, `risk` |
| `severity` | string | all | Filter by severity: `info`, `warning`, `critical` |
| `since` | ISO 8601 | 24 hours ago | Only return insights generated after this time |
| `limit` | integer | 20 | Maximum insights to return |
| `includeResolved` | boolean | false | Include insights that have been resolved/dismissed |

**Response (200):**

```json
{
  "insights": [
    {
      "id": "insight-sales-001",
      "category": "SALES",
      "severity": "WARNING",
      "title": "Week-over-Week Revenue Decline",
      "summary": "Revenue for the current week is $84,500, which is 12.3% lower than the previous week ($96,300). The decline is primarily driven by a 15% drop in mushroom kit sales.",
      "currentValue": 84500.00,
      "previousValue": 96300.00,
      "deltaPercentage": -12.3,
      "trend": "DECLINING",
      "dimensions": { "productCategory": "Mushroom Kits" },
      "recommendedActions": [
        "Review pricing and promotion calendar for mushroom kits.",
        "Check inventory availability for top-selling kit variants."
      ],
      "generatedAt": "2026-07-23T08:00:00Z",
      "expiresAt": "2026-07-24T08:00:00Z",
      "resolved": false
    }
  ],
  "total": 15,
  "unresolved": 12,
  "critical": 2,
  "warning": 5,
  "info": 8
}
```

### Schema: BusinessInsight

| Field | Type | Description |
|---|---|---|
| `id` | string | Unique insight identifier |
| `category` | string | `SALES`, `CUSTOMER`, `INVENTORY`, `OPERATIONAL`, `RISK` |
| `severity` | string | `INFO`, `WARNING`, `CRITICAL` |
| `title` | string | Short title |
| `summary` | string | Detailed description |
| `currentValue` | number | Current period value |
| `previousValue` | number | Comparison period value |
| `deltaPercentage` | number | Percentage change |
| `trend` | string | `UP`, `DOWN`, `STABLE`, `ACCELERATING`, `DECLINING` |
| `dimensions` | object | Relevant dimension filters |
| `recommendedActions` | string[] | Suggested actions |
| `generatedAt` | ISO 8601 | Generation timestamp |
| `expiresAt` | ISO 8601 | Expiration timestamp |
| `resolved` | boolean | Whether the insight has been resolved |

### curl Example

```bash
curl "https://api.sporekart.example/api/v1/copilot/admin/insights?severity=critical&limit=5" \
  -H "Authorization: Bearer eyJhbGciOiJSUzI1NiIs..."
```

---

## POST /forecast

Generate a forecast for a specified metric.

**Request:**

```
POST /api/v1/copilot/admin/forecast
Content-Type: application/json
Authorization: Bearer <token>
```

```json
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
  "algorithm": "auto"
}
```

**Response (200):**

```json
{
  "metric": "revenue",
  "horizon": 30,
  "generatedAt": "2026-07-23T09:35:00Z",
  "historicalPeriod": {
    "start": "2026-06-23T00:00:00Z",
    "end": "2026-07-22T23:59:59Z"
  },
  "dataPoints": [
    {
      "date": "2026-07-23",
      "forecast": 41200.00,
      "ci80Lower": 39800.00,
      "ci80Upper": 42600.00,
      "ci95Lower": 38500.00,
      "ci95Upper": 43900.00
    }
  ],
  "summary": {
    "totalForecast": 1245000.00,
    "dailyAverage": 41500.00,
    "algorithm": "Holt-Winters Additive",
    "seasonalityDetected": [7],
    "trendDirection": "UP",
    "mapeHistorical": 4.2
  }
}
```

### Schema: ForecastRequest

| Field | Type | Required | Default | Description |
|---|---|---|---|---|
| `metric` | string | yes | — | Metric ID to forecast |
| `horizon` | integer | no | 30 | Days to forecast (7, 30, or 90) |
| `periodStart` | ISO 8601 | no | 30 days ago | Historical data start |
| `periodEnd` | ISO 8601 | no | Now | Historical data end |
| `granularity` | string | no | metric default | `day`, `week`, `month` |
| `filters` | object | no | None | Dimension filters |
| `confidenceLevels` | int[] | no | [80, 95] | Confidence interval percentages |
| `algorithm` | string | no | `auto` | `auto`, `sma`, `holt`, `holtwinters`, `arima`, `prophet` |

### curl Example

```bash
curl -X POST https://api.sporekart.example/api/v1/copilot/admin/forecast \
  -H "Authorization: Bearer eyJhbGciOiJSUzI1NiIs..." \
  -H "Content-Type: application/json" \
  -d '{
    "metric": "revenue",
    "horizon": 7,
    "granularity": "day"
  }'
```

---

## GET /alerts

Retrieve operational alerts and active notifications.

**Request:**

```
GET /api/v1/copilot/admin/alerts
Authorization: Bearer <token>
```

**Query Parameters:**

| Parameter | Type | Default | Description |
|---|---|---|---|
| `status` | string | `active` | `active`, `acknowledged`, `resolved`, `all` |
| `severity` | string | all | `info`, `warning`, `critical` |
| `source` | string | all | Alert source service |
| `since` | ISO 8601 | 24 hours ago | Filter by generation time |

**Response (200):**

```json
{
  "alerts": [
    {
      "id": "alert-001",
      "title": "Payment Gateway Degradation",
      "message": "Payment success rate dropped to 88.2% in the last hour (threshold: 95%). 412 failed transactions detected.",
      "severity": "CRITICAL",
      "source": "payment-service",
      "metric": "payment_success_rate",
      "currentValue": 88.2,
      "threshold": 95.0,
      "status": "ACTIVE",
      "acknowledgedBy": null,
      "acknowledgedAt": null,
      "generatedAt": "2026-07-23T09:30:00Z",
      "resolvedAt": null
    },
    {
      "id": "alert-002",
      "title": "Low Stock Warning",
      "message": "11 SKUs are below reorder threshold. Oyster Mushroom Kit has only 3 days of supply remaining.",
      "severity": "WARNING",
      "source": "inventory-service",
      "metric": "low_stock_count",
      "currentValue": 11,
      "threshold": 5,
      "status": "ACTIVE",
      "acknowledgedBy": "jane@sporekart.example",
      "acknowledgedAt": "2026-07-23T08:45:00Z",
      "generatedAt": "2026-07-23T06:00:00Z",
      "resolvedAt": null
    }
  ],
  "total": 8,
  "active": 5,
  "acknowledged": 2,
  "critical": 1
}
```

### Schema: OperationalAlert

| Field | Type | Description |
|---|---|---|
| `id` | string | Unique alert identifier |
| `title` | string | Short alert title |
| `message` | string | Detailed alert message |
| `severity` | string | `INFO`, `WARNING`, `CRITICAL` |
| `source` | string | Service that generated the alert |
| `metric` | string | The metric that triggered the alert |
| `currentValue` | number | Current metric value |
| `threshold` | number | Threshold that was breached |
| `status` | string | `ACTIVE`, `ACKNOWLEDGED`, `RESOLVED` |
| `acknowledgedBy` | string or null | Who acknowledged the alert |
| `acknowledgedAt` | ISO 8601 or null | When acknowledged |
| `generatedAt` | ISO 8601 | When the alert was generated |
| `resolvedAt` | ISO 8601 or null | When resolved |

### curl Example

```bash
curl "https://api.sporekart.example/api/v1/copilot/admin/alerts?status=active&severity=critical" \
  -H "Authorization: Bearer eyJhbGciOiJSUzI1NiIs..."
```
