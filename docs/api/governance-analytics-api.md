# Governance Analytics API Reference

Base path: `/api/v1/governance/analytics`

---

## POST /api/v1/governance/analytics/metrics

Collect a metric data point.

### Request Body

```json
{
  "metricType": "GOVERNANCE",
  "name": "policy.evaluation.count",
  "value": 150,
  "unit": "count",
  "source": "PolicyEngine",
  "timestamp": "2026-07-12T10:30:00Z",
  "dimensions": {
    "module": "policy",
    "status": "ALLOWED",
    "environment": "production"
  }
}
```

### Response (201)

```json
{
  "metricId": "mtr-abc-123",
  "metricType": "GOVERNANCE",
  "name": "policy.evaluation.count",
  "value": 150,
  "unit": "count",
  "source": "PolicyEngine",
  "timestamp": "2026-07-12T10:30:00Z",
  "createdAt": "2026-07-12T10:30:00Z"
}
```

---

## GET /api/v1/governance/analytics/metrics

Query collected metrics.

### Query Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| metricType | string | No | Filter by metric type |
| name | string | No | Filter by metric name (supports wildcard) |
| source | string | No | Filter by source module |
| from | string (ISO date) | No | Start time |
| to | string (ISO date) | No | End time |
| page | integer | No | Page number (default: 0) |
| size | integer | No | Page size (default: 20) |

### Response (200)

```json
{
  "metrics": [
    {
      "metricId": "mtr-abc-123",
      "metricType": "GOVERNANCE",
      "name": "policy.evaluation.count",
      "value": 150,
      "unit": "count",
      "source": "PolicyEngine",
      "timestamp": "2026-07-12T10:30:00Z",
      "dimensions": {
        "module": "policy",
        "status": "ALLOWED"
      }
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 1
}
```

---

## POST /api/v1/governance/analytics/aggregate

Aggregate metrics by criteria.

### Request Body

```json
{
  "metricNames": ["policy.evaluation.count", "decision.count"],
  "aggregation": "AVG",
  "windowMinutes": 60,
  "from": "2026-07-12T00:00:00Z",
  "to": "2026-07-12T23:59:59Z",
  "groupBy": ["metricType", "source"]
}
```

### Response (200)

```json
{
  "aggregations": [
    {
      "metricName": "policy.evaluation.count",
      "aggregation": "AVG",
      "value": 145.3,
      "windowMinutes": 60,
      "dataPoints": [
        {
          "timestamp": "2026-07-12T10:00:00Z",
          "value": 150,
          "count": 1
        },
        {
          "timestamp": "2026-07-12T11:00:00Z",
          "value": 140,
          "count": 1
        }
      ]
    }
  ],
  "period": {
    "from": "2026-07-12T00:00:00Z",
    "to": "2026-07-12T23:59:59Z"
  }
}
```

---

## GET /api/v1/governance/analytics/kpis

List KPI definitions and values.

### Query Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| status | string | No | Filter by KPI status (ON_TRACK, AT_RISK, BELOW_TARGET, CRITICAL) |
| name | string | No | Filter by KPI name |

### Response (200)

```json
{
  "kpis": [
    {
      "kpiId": "kpi-001",
      "name": "Governance Success Rate",
      "description": "Percentage of successful governance actions",
      "currentValue": 97.5,
      "targetValue": 95.0,
      "unit": "%",
      "status": "ON_TRACK",
      "trend": "UP",
      "lastCalculated": "2026-07-12T10:30:00Z"
    },
    {
      "kpiId": "kpi-002",
      "name": "Approval SLA Compliance",
      "description": "Percentage of approvals meeting SLA",
      "currentValue": 88.2,
      "targetValue": 90.0,
      "unit": "%",
      "status": "AT_RISK",
      "trend": "DOWN",
      "lastCalculated": "2026-07-12T10:30:00Z"
    }
  ]
}
```

---

## POST /api/v1/governance/analytics/kpis/calculate

Calculate KPIs.

### Request Body

```json
{
  "kpiNames": ["Governance Success Rate", "Policy Evaluation Rate"],
  "period": {
    "from": "2026-07-11T00:00:00Z",
    "to": "2026-07-12T00:00:00Z"
  }
}
```

### Response (200)

```json
{
  "calculated": [
    {
      "kpiId": "kpi-001",
      "name": "Governance Success Rate",
      "currentValue": 97.5,
      "targetValue": 95.0,
      "status": "ON_TRACK",
      "trend": "UP",
      "calculatedAt": "2026-07-12T10:30:00Z"
    }
  ]
}
```

---

## GET /api/v1/governance/analytics/trends

Analyze trends for metrics.

### Query Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| metricName | string | Yes | Metric name to analyze |
| period | string | No | Comparison period (DAILY, WEEKLY, MONTHLY) |
| from | string (ISO date) | No | Start time |
| to | string (ISO date) | No | End time |

### Response (200)

```json
{
  "metricName": "policy.evaluation.count",
  "direction": "UP",
  "percentageChange": 12.5,
  "currentPeriod": {
    "from": "2026-07-12T00:00:00Z",
    "to": "2026-07-12T23:59:59Z",
    "averageValue": 145.3
  },
  "previousPeriod": {
    "from": "2026-07-11T00:00:00Z",
    "to": "2026-07-11T23:59:59Z",
    "averageValue": 129.2
  },
  "dataPoints": [
    {"timestamp": "2026-07-11T00:00:00Z", "value": 129.2},
    {"timestamp": "2026-07-12T00:00:00Z", "value": 145.3}
  ]
}
```

---

## POST /api/v1/governance/analytics/reports

Generate a report.

### Request Body

```json
{
  "reportType": "EXECUTIVE_SUMMARY",
  "format": "JSON",
  "period": {
    "from": "2026-07-11T00:00:00Z",
    "to": "2026-07-12T00:00:00Z"
  },
  "filters": {
    "modules": ["policy", "decision", "approval"],
    "includeKpis": true,
    "includeTrends": true
  },
  "title": "Daily Governance Executive Summary"
}
```

### Response (201)

```json
{
  "reportId": "rpt-abc-123",
  "reportType": "EXECUTIVE_SUMMARY",
  "format": "JSON",
  "status": "COMPLETED",
  "title": "Daily Governance Executive Summary",
  "period": {
    "from": "2026-07-11T00:00:00Z",
    "to": "2026-07-12T00:00:00Z"
  },
  "generatedAt": "2026-07-12T10:30:00Z",
  "data": {
    "summary": {
      "totalPolicies": 45,
      "activePolicies": 38,
      "evaluationsToday": 1520,
      "decisionsToday": 890,
      "approvalsToday": 234,
      "slaCompliance": 88.2,
      "overallHealth": "GOOD"
    },
    "kpis": [
      {"name": "Governance Success Rate", "value": 97.5, "status": "ON_TRACK"},
      {"name": "Approval SLA Compliance", "value": 88.2, "status": "AT_RISK"}
    ],
    "trends": [
      {"metric": "policy.evaluation.count", "direction": "UP", "change": 12.5}
    ]
  }
}
```

---

## GET /api/v1/governance/analytics/reports

List generated reports.

### Query Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| reportType | string | No | Filter by report type |
| status | string | No | Filter by status (PENDING, GENERATING, COMPLETED, FAILED) |
| from | string (ISO date) | No | Start generation date |
| to | string (ISO date) | No | End generation date |
| page | integer | No | Page number (default: 0) |
| size | integer | No | Page size (default: 20) |

### Response (200)

```json
{
  "reports": [
    {
      "reportId": "rpt-abc-123",
      "reportType": "EXECUTIVE_SUMMARY",
      "format": "JSON",
      "status": "COMPLETED",
      "title": "Daily Governance Executive Summary",
      "generatedAt": "2026-07-12T10:30:00Z",
      "period": {
        "from": "2026-07-11T00:00:00Z",
        "to": "2026-07-12T00:00:00Z"
      }
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 1
}
```

---

## POST /api/v1/governance/analytics/export

Export data in specified format.

### Request Body

```json
{
  "reportId": "rpt-abc-123",
  "format": "CSV",
  "options": {
    "includeHeaders": true,
    "delimiter": ","
  }
}
```

### Response (201)

```json
{
  "exportId": "exp-abc-123",
  "reportId": "rpt-abc-123",
  "format": "CSV",
  "status": "COMPLETED",
  "fileReference": "exports/2026/07/12/exp-abc-123.csv",
  "fileSize": 45600,
  "exportedAt": "2026-07-12T10:30:05Z"
}
```

---

## GET /api/v1/governance/analytics/dashboard/{id}

Get dashboard data.

### Path Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | string | Yes | Dashboard ID |

### Query Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| refresh | boolean | No | Force refresh from source (default: false) |

### Response (200)

```json
{
  "dashboardId": "dash-001",
  "name": "Governance Overview",
  "widgets": [
    {
      "widgetId": "wgt-001",
      "type": "KPI_GRID",
      "title": "Key Performance Indicators",
      "data": {
        "kpis": [
          {"name": "Governance Success Rate", "value": 97.5, "status": "ON_TRACK"},
          {"name": "Policy Evaluation Rate", "value": 99.2, "status": "ON_TRACK"},
          {"name": "Approval SLA Compliance", "value": 88.2, "status": "AT_RISK"}
        ]
      },
      "lastRefreshed": "2026-07-12T10:30:00Z"
    },
    {
      "widgetId": "wgt-002",
      "type": "TREND_CHART",
      "title": "Policy Evaluations (7 days)",
      "data": {
        "labels": ["07/06", "07/07", "07/08", "07/09", "07/10", "07/11", "07/12"],
        "values": [1200, 1350, 1280, 1420, 1380, 1520, 1450]
      },
      "lastRefreshed": "2026-07-12T10:30:00Z"
    }
  ],
  "lastRefreshed": "2026-07-12T10:30:00Z"
}
```

### Response (404)

```json
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "Dashboard dash-001 not found",
  "instance": "/api/v1/governance/analytics/dashboard/dash-001",
  "errorCode": "ANL_404"
}
```

## Error Response Format (RFC 9457)

All governance analytics API errors use the RFC 9457 Problem Details format:

```json
{
  "type": "about:blank",
  "title": "string",
  "status": 400,
  "detail": "string",
  "instance": "/api/v1/governance/analytics/metrics",
  "errorCode": "ANL_4xx"
}
```

| Error Code | HTTP Status | Description |
|------------|-------------|-------------|
| ANL_400 | 400 | Invalid request parameters |
| ANL_401 | 401 | Missing or invalid authentication |
| ANL_403 | 403 | Insufficient permissions |
| ANL_404 | 404 | Resource not found |
| ANL_409 | 409 | Conflict (invalid state transition) |
| ANL_422 | 422 | Analytics pipeline error |
| ANL_429 | 429 | Rate limit exceeded |
| ANL_500 | 500 | Internal analytics engine error |
