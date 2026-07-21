# AI Gateway Health

## Health Architecture

```mermaid
graph TB
    SUB[Health Endpoint] --> IND[GatewayHealthIndicator]
    IND --> AG[HealthAggregator]
    AG --> C1[Pipeline Checker]
    AG --> C2[Router Checker]
    AG --> C3[Provider Checkers]
    AG --> C4[Redis Checker]
    AG --> C5[Database Checker]
    C1 --> RES[Health Result]
    C2 --> RES
    C3 --> RES
    C4 --> RES
    C5 --> RES
```

## Health Check Interface

```mermaid
classDiagram
    class GatewayHealthIndicator {
        <<interface>>
        +HealthStatus check(String component)
        +Map~String, HealthStatus~ checkAll()
        +List~String~ getComponents()
        +boolean isHealthy(String component)
        +boolean isOverallHealthy()
        +void registerComponent(String, HealthChecker)
    }
    class HealthChecker {
        <<interface>>
        +HealthStatus check()
    }
    class HealthAggregator {
        <<interface>>
        +HealthCheckResult aggregate(List~HealthCheckResult~)
        +String determineOverallStatus(List~HealthCheckResult~)
    }
    GatewayHealthIndicator --> HealthChecker
    GatewayHealthIndicator --> HealthAggregator
```

## Health Status Levels

| Status | Description |
|--------|-------------|
| UP | Component is fully operational |
| DEGRADED | Component is operational but degraded |
| DOWN | Component is not operational |

## Health Check Components

| Component | Checker | Description |
|-----------|---------|-------------|
| pipeline | Pipeline Checker | Pipeline executor health |
| router | Router Checker | Provider router health |
| provider:* | Provider Checkers | Each provider's health |
| redis | Redis Checker | Cache connectivity |
| database | Database Checker | Database connectivity |

## Response Format

```json
{
  "status": "UP",
  "details": {
    "pipeline": {
      "status": "UP",
      "lastChecked": "2026-01-01T00:00:00Z",
      "responseTime": "5ms"
    },
    "router": {
      "status": "UP",
      "lastChecked": "2026-01-01T00:00:00Z",
      "responseTime": "2ms"
    },
    "provider:openai": {
      "status": "DEGRADED",
      "lastChecked": "2026-01-01T00:00:00Z",
      "responseTime": "1500ms"
    }
  },
  "timestamp": "2026-01-01T00:00:00Z"
}
```
