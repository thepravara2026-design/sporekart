# Sprint 17 — Part 2: AI Gateway & Core AI Service Layer

**Date:** 2026-07-11
**Module:** ai-service
**Lead:** Enterprise AI Platform Engineering Team

---

## Objective

Build the Enterprise AI Gateway — the single entry point for every AI request in the application. Every business module (Catalog, Training, Marketplace, Orders, Support, ERP, Analytics) must communicate only with the AI Gateway. No module may directly communicate with Gemini, OpenAI, Claude, or any future provider.

---

## Deliverables

### Gateway Components

| Component | Package | Purpose |
|-----------|---------|---------|
| `GatewayController` | `interfaces.rest` | REST API — execute, validate, health, status, features |
| `GatewayApplicationService` | `gateway.application` | Facade implementing `AiGateway` interface |
| `GatewayDomainService` | `gateway.application` | Domain logic, request counting, status aggregation |
| `GatewayPipeline` | `gateway.application` | Execution pipeline — validate, rate limit, resolve, execute, audit, metrics |
| `GatewayRequestValidator` | `gateway.application` | Input validation (null, blank, max length) |
| `GatewayResponseBuilder` | `gateway.application` | Standard `ResponseEnvelope<T>` builder |
| `GatewayContextResolver` | `gateway.application` | Resolves user ID, module, provider from request |
| `GatewayAuditService` | `gateway.application` | Structured audit logging |
| `GatewayMetricsCollector` | `gateway.application` | Micrometer counters, timers, error tracking |
| `GatewayExceptionTranslator` | `gateway.application` | Maps exceptions to RFC 9457 Problem Details |
| `GatewayFeatureManager` | `gateway.application` | Feature flag validation facade |

### Core Interfaces (4 new in `core.api`)

| Interface | Methods |
|-----------|---------|
| `AIRateLimiter` | `tryAcquire`, `getRemainingTokens`, `getResetTimeSeconds` |
| `AIRequestValidator` | `validate`, `supports` |
| `AIAuditService` | `recordRequestReceived`, `recordRequestCompleted`, `recordRequestRejected`, `recordEvent` |
| `AIGateway` | `execute`, `isAvailable`, `getGatewayName` |

### Gateway Infrastructure (6 components)

| Component | Purpose |
|-----------|---------|
| `GatewayHealthIndicator` | Actuator health + `AIHealthService` integration |
| `GatewayKafkaEventPublisher` | Kafka event publishing (7 event types) |
| `GatewayRedisCacheService` | Redis caching (config, features, health, execution metadata) |
| `DefaultProviderResolver` | Feature-flag-aware provider resolution |
| `DefaultRetryStrategy` | Exponential backoff, max 3 retries |
| `DefaultTimeoutStrategy` | Per-module timeout configuration |
| `InMemoryRateLimiter` | In-memory rate limiting (placeholder) |
| `BasicRequestValidator` | Simple null/blank validation (placeholder) |

### Gateway DTOs (9 in `gateway.domain`)

| DTO | Type |
|-----|------|
| `AIExecutionRequest` | Record — prompt, role, module, preferred provider, userId, parameters, metadata |
| `AIExecutionResponse` | Record — requestId, correlationId, content, role, provider, model, timestamp, executionTimeMs, status, warnings, metadata, error |
| `AIErrorDetail` | Record — code, message |
| `AIRequestMetadata` | Record — source, sessionId, conversationId, apiVersion, clientVersion |
| `AIExecutionResult` | Record — executionId, status, provider, durationMs, success, response |
| `AIHealthResponse` | Record — status, healthy, degraded, moduleStatus, message |
| `CorrelationMetadata` | Record — correlationId, requestId, sessionId, userId, module |
| `GatewayStatus` | Record — gatewayEnabled, totalRequests, successfulRequests, failedRequests, averageLatencyMs, moduleStatus, featureFlags |
| `GatewayExecutionContext` | Record — executionId, correlationId, userId, module, provider, timestamp, context |

### Gateway Exceptions (6 in `core.application.exception`)

| Exception | Error Code |
|-----------|------------|
| `AIGatewayException` | Base gateway exception |
| `AIValidationException` | AI-006 (validation failure) |
| `AIRateLimitException` | AI-007 (rate limit exceeded) |
| `AIExecutionException` | Variable (execution failure) |
| `GatewayUnavailableException` | AI-005 (gateway down) |
| `FeatureDisabledException` | AI-001 (feature disabled) |

### Feature Flags (4 new, 14 total)

| Flag | Purpose |
|------|---------|
| `AI_GATEWAY_ENABLED` | Enable/disable gateway module |
| `AI_REQUEST_LOGGING` | Enable/disable structured request logging |
| `AI_RATE_LIMITING` | Enable/disable rate limiting |
| `AI_METRICS` | Enable/disable metrics collection |

### REST APIs

| Method | Path | Purpose |
|--------|------|---------|
| `POST` | `/api/v1/ai/execute` | Execute an AI request through the gateway pipeline |
| `POST` | `/api/v1/ai/validate` | Validate an AI request without execution |
| `GET` | `/api/v1/ai/health` | Gateway health check |
| `GET` | `/api/v1/ai/status` | Detailed gateway status with metrics and feature flags |
| `GET` | `/api/v1/ai/features` | Current state of all AI feature flags |

### Kafka Events (7 event types)

| Event | Publisher |
|-------|-----------|
| `AIRequestReceived` | `GatewayKafkaEventPublisher` |
| `AIRequestValidated` | `GatewayKafkaEventPublisher` |
| `AIRequestRejected` | `GatewayKafkaEventPublisher` |
| `AIExecutionStarted` | `GatewayKafkaEventPublisher` |
| `AIExecutionCompleted` | `GatewayKafkaEventPublisher` |
| `AIExecutionFailed` | `GatewayKafkaEventPublisher` |
| `GatewayHealthChanged` | `GatewayKafkaEventPublisher` |

Topic: `ai-gateway-events` (3 partitions, 1 replica)

### Redis Caching Strategy

| Cache Prefix | TTL | Purpose |
|-------------|-----|---------|
| `gw:config:*` | 10 minutes | Gateway configuration values |
| `gw:features:*` | 5 minutes | Feature flag state |
| `gw:health:*` | 1 minute | Health metadata |
| `gw:exec:*` | 1 hour | Execution metadata (hash) |

### Flyway Migrations

`V11__sprint17_ai_gateway.sql` — 3 tables:

| Table | Purpose | Key Columns |
|-------|---------|-------------|
| `ai_gateway_configuration` | Gateway config KV store | id (UUID), config_key (unique), config_value, config_type, module, audit columns, soft delete |
| `ai_request_audit` | Request audit log | id (UUID), request_id, correlation_id, execution_id, module, provider, user_id, action, status, prompt_length, duration_ms, error, payload, source_ip, user_agent |
| `ai_execution_history` | Execution history | id (UUID), request_id, correlation_id, execution_id (unique), module, provider, model, status, prompt_hash, prompt_length, response_length, input_tokens, output_tokens, duration_ms, success, error, retry_count, pipeline_stage, audit columns |

### Tests (3 new test classes)

| Test Class | Type | Coverage |
|-----------|------|----------|
| `GatewayControllerTest` | Integration (MockMvc) | 7 tests — health, status, features, execute, validate, reject |
| `GatewayPipelineTest` | Unit (Mockito) | 8 tests — execute, disabled states, validation, rate limiting, status |
| `GatewayRequestValidatorTest` | Unit | 5 tests — valid, null, blank, long prompt, detailed errors |

### Architecture Rules Added

- Core must not depend on gateway
- Gateway must only depend on `core.api`, `core.application`, `core.domain`
- Gateway must not depend on other feature modules (provider, prompt, rag, search, chat, content, workflow, monitoring)
- No cyclic dependencies across gateway

---

## Configuration Changes

### `application.yml`

```yaml
sporekart:
  ai:
    features:
      gateway-enabled: true
      request-logging: true
      rate-limiting: true
      metrics: true
```

### `SecurityConfig.java`

Permitted paths added for gateway health/status/features endpoints.

### `KafkaConfig.java`

`aiGatewayEventsTopic()` bean added (3 partitions, 1 replica).

---

## Architecture Rules

1. **Single entry point** — All AI requests flow through Gateway → Pipeline → Provider Interface
2. **No bypass** — Business modules must never call providers directly
3. **Feature gating** — Gateway refuses execution when `AI_PLATFORM_ENABLED` or `AI_GATEWAY_ENABLED` is false
4. **RFC 9457 compliance** — All errors return Problem Details via `ResponseEnvelope<T>`
5. **Backward compatibility** — All existing modules untouched

---

## Dependencies Added

None (all dependencies existed in Part 1).

---

## Risks

- `InMemoryRateLimiter` is not production-ready — must be replaced with Redis-based implementation
- `BasicRequestValidator` is a minimal placeholder — `GatewayRequestValidator` handles validation
- No provider adapters yet — all requests return mock responses from `GatewayDomainService`
- Kafka and Redis must be running for full integration

---

## Technical Debt

- Rate limiting window is not resetting automatically (no time-based eviction)
- Gateway audit writes to structured log only — no database persistence yet
- Pipeline status always returns `COMPLETED` — no real execution tracking
- No health degradation detection logic in `GatewayHealthIndicator`

---

## Readiness for Sprint 17 Part 3

Part 3 can begin implementation of:
- Provider abstraction layer (Gemini, OpenAI, Claude adapters)
- Provider SDK integration with retry and timeout
- Provider-specific error mapping
- Provider health checks and circuit breaking
- Prompt execution engine
