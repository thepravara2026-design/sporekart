# Admin Copilot Architecture

## Overview

Admin Copilot is an AI-powered operations and analytics assistant for the SporeKart platform. It enables administrators and analysts to interact with platform data through natural language queries, generate reports, forecast business metrics, and receive actionable business insights. It serves as the administrative intelligence layer, sitting between the user-facing AI copilot framework and the backend platform services.

## Component Diagram

```
+-----------------------------------------------------------------------------------+
|                              CLIENT LAYER                                         |
|  +------------------+  +-------------------+  +-----------------------------+     |
|  |  Admin Web UI    |  |  Mobile Admin     |  |  API Clients / curl / Postman|    |
|  +--------+---------+  +---------+---------+  +--------------+--------------+     |
|           |                      |                            |                    |
+-----------+----------------------+----------------------------+--------------------+
            |                      |                            |
            v                      v                            v
+-----------------------------------------------------------------------------------+
|                            GATEWAY LAYER                                          |
|  +----------------------------------------------------------------------------+   |
|  |  Spring Cloud Gateway (port 8080)                                         |   |
|  |  Routes: /api/copilot/admin/* -> admin-copilot-service:8100               |   |
|  |  Auth: JWT validation + RBAC (ADMIN, ANALYST roles)                       |   |
|  |  Rate limiting, correlation ID injection, audit logging                   |   |
|  +----------------------------------------------------------------------------+   |
+-----------------------------------------------------------------------------------+
            |
            v
+-----------------------------------------------------------------------------------+
|                        ADMIN COPILOT SERVICE (port 8100)                          |
|                                                                                   |
|  +----------------------------------+                                             |
|  |     CopilotEngine               |  NL query parsing, intent resolution,       |
|  |     (AI orchestration)          |  context management, response generation   |
|  +----------------------------------+                                             |
|            |                          |                        |                  |
|            v                          v                        v                  |
|  +------------------+  +----------------------------+  +-----------------+        |
|  | AnalyticsEngine  |  | BusinessInsightsEngine    |  | ForecastingEngine|        |
|  | Query processing  |  | Trend detection            |  | Predictive models|        |
|  | Metric aggregation|  | Insight generation         |  | Seasonal adj.    |        |
|  | Report generation |  | Action recommendations     |  | Confidence calc  |        |
|  +------------------+  +----------------------------+  +-----------------+        |
|            |                          |                        |                  |
|            v                          v                        v                  |
|  +----------------------------------------------------------------------------+   |
|  |  Service Integrations (REST/HTTP clients)                                   |   |
|  |  Analytics Service  |  Order Service  |  Inventory Service  |  Training    |   |
|  |  Identity Service   |  Payment Service|  Notification Svc   |  Risk Service|   |
|  +----------------------------------------------------------------------------+   |
+-----------------------------------------------------------------------------------+
            |                          |                        |                  |
            v                          v                        v                  |
+-----------------------------------------------------------------------------------+
|                         PLATFORM SERVICES                                         |
|  +------------+  +---------+  +------------+  +----------+  +----------+          |
|  | Analytics  |  | Order   |  | Inventory  |  | Training |  | Payment  |          |
|  | Service    |  | Service |  | Service    |  | Service  |  | Service  |          |
|  | :8001      |  | :8002   |  | :8003      |  | :8004    |  | :8005    |          |
|  +------------+  +---------+  +------------+  +----------+  +----------+          |
|  +----------+  +----------+  +----------+  +----------+                          |
|  | Identity |  | Risk     |  | Notific. |  | Gateway  |                          |
|  | Service  |  | Service  |  | Service  |  | Service  |                          |
|  +----------+  +----------+  +----------+  +----------+                          |
+-----------------------------------------------------------------------------------+
```

## Data Flow

### Synchronous Query Flow (Chat)

```
Admin Query (natural language)
    |
    v
Gateway (/api/copilot/admin/*)
    |-- JWT validation
    |-- Role check (ADMIN or ANALYST)
    |-- Inject correlation ID
    |-- Route to admin-copilot-service:8100
    |
    v
CopilotEngine.receiveMessage(query, context)
    |-- Intent resolution (AnalyticsEngine, ForecastingEngine, InsightsEngine)
    |
    +--> AnalyticsEngine path:
    |    |-- Parse query for metrics, dimensions, filters
    |    |-- Call platform services (analytics, order, inventory, etc.)
    |    |-- Aggregate and transform data
    |    |-- Return structured result
    |
    +--> BusinessInsightsEngine path:
    |    |-- Fetch current and historical period data
    |    |-- Compare periods, detect trends
    |    |-- Generate insights with severity levels
    |    |-- Return insight object
    |
    +--> ForecastingEngine path:
    |    |-- Retrieve historical time series
    |    |-- Apply forecasting algorithm
    |    |-- Calculate confidence intervals
    |    |-- Return forecast result
    |
    v
CopilotEngine.formatResponse(result)
    |-- Generate natural language response
    |-- Attach structured data payload
    |-- Fire audit event
    |
    v
Response to client
```

### Asynchronous Report Flow

```
POST /api/v1/copilot/admin/report { type, format, metrics, filters }
    |
    v
CopilotEngine.requestReport(params)
    |-- Validate parameters
    |-- Submit report generation job
    |-- Return job ID (202 Accepted)
    |
    v
AnalyticsEngine.generateReport(job)
    |-- Query relevant services
    |-- Transform to requested format
    |-- Store in cache / object storage
    |-- Send notification on completion
    |
    v
Client polls GET /api/v1/copilot/admin/report/:jobId
    |-- Returns status: pending | completed | failed
    |-- On completed: returns download URL
```

## Integration with Existing Platform

| Platform Service | Integration Type | Data Accessed |
|---|---|---|
| Analytics Service | REST /api/analytics/* | Dashboard metrics, sales data, customer analytics |
| Order Service | REST /api/orders/* | Order volume, revenue, status distribution |
| Inventory Service | REST /api/inventory/* | Stock levels, turnover, ABC data |
| Training Service | REST /api/training/* | Enrollment, completion rates, revenue |
| Payment Service | REST /api/payments/* | Transaction success rates, refund data |
| Identity Service | REST /api/auth/* | User role verification, profile data |
| Risk Service | REST /api/risk/* | Risk indicators, fraud metrics |
| Notification Service | REST /api/notifications/* | Alert delivery, notification status |

## Security

### RBAC Integration

The Admin Copilot enforces role-based access control at two levels:

1. **Gateway Level**: The Spring Cloud Gateway AuthorizationFilter checks `X-User-Roles` header against path-based role requirements:
   - `/api/copilot/admin/*` requires `ADMIN` or `ANALYST` role
   - Operations that modify state (report generation, alert configuration) require `ADMIN`

2. **Service Level**: Each endpoint in the Admin Copilot controller is annotated with `@PreAuthorize`:
   - `hasRole('ADMIN')` for sensitive operations (forecast, alerts, report generation)
   - `hasRole('ADMIN') or hasRole('ANALYST')` for read-only operations (dashboard, insights, chat)

### Role Definitions

| Role | Permissions |
|---|---|
| `ADMIN` | Full access: dashboard, insights, reports, forecasts, alerts, chat |
| `ANALYST` | Read access: dashboard, insights, chat; Report generation; No alert configuration |

### Audit Logging

All admin copilot interactions are logged with the following structure:

```json
{
  "eventId": "uuid",
  "timestamp": "2026-07-23T09:35:00Z",
  "userId": "user-123",
  "roles": ["ADMIN"],
  "action": "CHAT_QUERY | DASHBOARD_VIEW | REPORT_GENERATE | FORECAST_REQUEST | INSIGHT_VIEW | ALERT_CONFIG",
  "resource": "/api/v1/copilot/admin/chat",
  "requestBody": "<redacted>",
  "statusCode": 200,
  "correlationId": "corr-abc-123",
  "sourceIp": "10.0.1.50"
}
```

Audit events are published to Kafka topic `audit.admin-copilot` and consumed by the shared audit service for compliance and forensics.

## Deployment

| Property | Value |
|---|---|
| Service Name | `admin-copilot-service` |
| Port | 8100 |
| Gateway Route | `/api/copilot/admin/*` |
| Docker Image | `sporekart/admin-copilot-service:latest` |
| Replicas | 2 (min), 10 (max) |
| Memory | 1GB (min), 2GB (max) |
| CPU | 500m (min), 2000m (max) |
| Health Check | `GET /actuator/health` |
| Readiness Check | `GET /actuator/health/readiness` |

## Configuration

Key application properties (see `application.yml`):

```yaml
server:
  port: 8100

sporekart:
  copilot:
    admin:
      analytics-service-url: http://analytics-service:8001
      order-service-url: http://order-service:8002
      inventory-service-url: http://inventory-service:8003
      training-service-url: http://training-service:8004
      payment-service-url: http://payment-service:8005
      risk-service-url: http://risk-service:8006
      cache-ttl-seconds: 300
      insight-lookback-days: 30
      forecast-default-horizon: 30
      max-report-records: 10000
```
