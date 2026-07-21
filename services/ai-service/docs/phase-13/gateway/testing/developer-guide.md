# AI Gateway — Developer Guide

## Architecture Overview

The AI Gateway follows a **staged pipeline architecture** with 11 ordered stages:

```
Request → [Validation → Authentication → Authorization → Quota Check → Rate Limiter → 
           Provider Selection → Routing → Execution → Post-Processing → Audit → Metrics] → Response
```

### Core Components

| Component | Responsibility | Location |
|-----------|----------------|----------|
| GatewayController | REST API entry (`/api/v1/ai/*`) | `interfaces/rest/` |
| GatewayApplicationService | Orchestrator facade | `gateway/application/` |
| GatewayPipeline | 11-stage execution engine | `gateway/application/` |
| AiGateway | Core routing interface | `gateway/api/` |
| GatewayDomainService | Domain logic / mock responses | `gateway/application/` |
| GatewayExceptionTranslator | Exception → AIResponse mapping | `gateway/application/` |
| GatewayMetricsCollector | Micrometer metrics collection | `gateway/application/` |
| GatewayResponseBuilder | Response envelope construction | `gateway/application/` |
| GatewayAuditService | Audit event recording | `gateway/application/` |
| InMemoryRateLimiter | Per-module rate limiting | `gateway/infrastructure/` |
| DefaultRetryStrategy | Exponential backoff retry | `gateway/infrastructure/` |
| DefaultTimeoutStrategy | Configurable timeouts | `gateway/infrastructure/` |
| StandardGatewayError | Unified error record | `gateway/error/` |

### Provider Architecture

```
providers/
  AIProvider.java          — Core provider interface
  ProviderType.java        — Provider type enum
  adapter/                 — ProviderAdapter for request/response adaptation
  circuit/                 — CircuitBreakerManager, CircuitState (CLOSED/OPEN/HALF_OPEN)
  circuit/breaker/         — Per-provider CircuitBreaker + Config
  circuit/policy/          — CircuitBreakerPolicy
  circuit/recovery/        — CircuitRecovery
  health/                  — HealthStatus, ProviderHealthManager, Readiness/Liveness checkers
  registry/                — ProviderRegistry, ProviderCatalog, Discovery, Activation, Versioning
  lifecycle/               — LifecycleState (15 states), LifecycleTransition (state machine)
  selector/                — ProviderSelector with 6 strategies
  failover/                — FailoverManager with 6 strategies
  recovery/                — RecoveryManager with 5 strategies
  metrics/                 — MetricsCollector, MetricsModel (15 fields)
  heartbeat/               — HeartbeatManager, HeartbeatMonitor
  availability/            — AvailabilityManager
  maintenance/             — MaintenanceManager
  audit/                   — AuditManager, AuditEventType (13 types)
```

### Gateway Pipeline Stages

| Order | Stage | What Happens | Exceptions |
|-------|-------|-------------|------------|
| 1 | VALIDATION | Request validation | ValidationException |
| 2 | AUTHENTICATION | API key / bearer token check | AuthenticationException |
| 3 | AUTHORIZATION | RBAC enforcement | AuthorizationException |
| 4 | QUOTA_CHECK | Token quota verification | QuotaExceededException |
| 5 | RATE_LIMITER | Per-module rate limiting | RateLimitException |
| 6 | PROVIDER_SELECTION | Resolve target provider | ConfigurationException |
| 7 | ROUTING | Route to provider | ServiceUnavailableException |
| 8 | EXECUTION | Execute with retry + timeout | ProviderException, TimeoutException |
| 9 | POST_PROCESSING | Response normalization | GatewayException |
| 10 | AUDIT | Record audit event | — |
| 11 | METRICS | Collect metrics | — |

## Adding a New Provider

1. Add provider type to `AiProviderType` enum
2. Implement `AIProvider` interface
3. Implement `ProviderAdapter` with `adaptRequest()` and `adaptResponse()`
4. Register in `ProviderFactory` or via `@Component`
5. Add to discovery in `ProviderDiscoveryService`
6. Configure in `application.yml` under `sporekart.ai.providers`

## Adding a New Pipeline Stage

1. Add enum constant in `PipelineStage`
2. Create stage handler in `pipeline/stages/`
3. Wire into `PipelineExecutor` as a `StageHandler`
4. Add to `GatewayPipeline` execution flow

## Error Handling

All provider failures must be converted to `StandardGatewayError`:

```java
// Instead of raw provider exceptions:
throw StandardGatewayError.providerUnavailable("OPENAI");
throw StandardGatewayError.rateLimited("GEMINI", 30L);
throw StandardGatewayError.circuitOpen("CLAUDE");
```

The `StandardGatewayError` record provides:
- `errorCode` — Machine-readable error code
- `message` — Human-readable description
- `statusCode` — HTTP status code
- `providerId` — Failing provider identifier
- `failureType` — Failure category (UNAVAILABLE, TIMEOUT, RATE_LIMIT, etc.)
- `retryable` — Whether operation can be retried
- `retryAfterSeconds` — Suggested retry delay (for rate limits)

## Testing

```bash
# Run all AI Gateway tests
mvn test -Dtest="com.sporekart.ai.gateway.**"

# Run specific suite
mvn test -Dtest="GatewayIntegrationTest"
mvn test -Dtest="GatewayConcurrencyTest"
mvn test -Dtest="GatewayPerformanceBenchmarkTest"
mvn test -Dtest="GatewaySecurityTest"
mvn test -Dtest="GatewayRegressionTest"
```

### Test File Locations

```
src/test/java/com/sporekart/ai/gateway/
    GatewayDomainTest.java                        20 tests
    GatewayArchitectureTest.java                  1 test
    application/GatewayPipelineTest.java          8 tests
    application/GatewayRequestValidatorTest.java  5 tests
    integration/GatewayIntegrationTest.java       77 tests
    integration/GatewayConcurrencyTest.java       8 tests
    performance/GatewayPerformanceBenchmarkTest.java 5 tests
    security/GatewaySecurityTest.java             24 tests
    regression/GatewayRegressionTest.java         20 tests
```

Total: **167 tests** across **7 suites**.

## Configuration

All gateway configuration in `application.yml`:

```yaml
sporekart:
  ai:
    gateway:
      pipeline:
        enabled: true
        stage-timeout: 30s
        max-retries: 3
      rate-limit:
        enabled: true
        default-limit: 100
        window-seconds: 60
      quota:
        enabled: true
        default-token-limit: 10000
      execution:
        default-timeout: 60s
        streaming-supported: true
      observability:
        metrics-enabled: true
        tracing-enabled: true
        audit-enabled: true
      router:
        default-strategy: first-available
        fallback-enabled: true
        fallback-providers: [mock]
```

## Provider Configuration

```yaml
sporekart:
  ai:
    providers:
      openai:
        enabled: true
        api-key: ${OPENAI_API_KEY}
        models: [gpt-4, gpt-3.5-turbo]
        timeout: 30s
      gemini:
        enabled: false
      claude:
        enabled: false
      mock:
        enabled: true
```

## Key Design Decisions

1. **Immutable records** — All domain objects are Java records for thread safety
2. **Constructor injection** — Services use constructor injection for testability
3. **No static state** — No singletons or static mutable state
4. **No circular dependencies** — Strict layered architecture
5. **StandardGatewayError** — Unified error boundary, never leak provider exceptions
6. **11-stage pipeline** — Extensible stage-based execution with ordered enum
7. **Mock provider** — Always available as the last fallback in the chain
8. **Per-provider circuit breakers** — Isolated failure domains
