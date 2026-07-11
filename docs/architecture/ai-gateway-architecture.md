# AI Gateway Architecture

**Version:** 1.0.0
**Last Updated:** 2026-07-11
**Owner:** Enterprise AI Platform Engineering Team

---

## Overview

The AI Gateway is the single entry point for every AI request in the SporeKart platform. Every business module communicates exclusively through this gateway — never directly with external AI providers. The gateway enforces governance, collects metrics, audits every request, and routes to the appropriate provider interface.

---

## Architecture Diagram

```
Business Modules (Catalog, Orders, Inventory, Marketplace, ERP, Training, Support, Analytics)
        │
        ▼
┌──────────────────────────────────────────────────────────────────────┐
│                          AI Gateway                                   │
│                                                                      │
│  POST /api/v1/ai/execute                                             │
│  POST /api/v1/ai/validate                                            │
│  GET  /api/v1/ai/health                                              │
│  GET  /api/v1/ai/status                                              │
│  GET  /api/v1/ai/features                                            │
│                                                                      │
│  ┌────────────────────────────────────────────────────────────────┐  │
│  │                    GatewayPipeline                               │  │
│  │                                                                  │  │
│  │  Receive Request → Validate → Feature Check → Rate Limit →      │  │
│  │  Resolve Context → Resolve Provider → Execute → Audit →         │  │
│  │  Collect Metrics → Build Response                                │  │
│  └────────────────────────────────────────────────────────────────┘  │
└──────────────────────────────────────────────────────────────────────┘
        │
        ▼
┌──────────────────────────────────────────────────────────────────────┐
│                    Provider Interface Layer                           │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐              │
│  │   Gemini     │  │   OpenAI     │  │   Claude     │   Future...  │
│  │   Adapter    │  │   Adapter    │  │   Adapter    │              │
│  └──────────────┘  └──────────────┘  └──────────────┘              │
└──────────────────────────────────────────────────────────────────────┘
        │
        ▼
External AI Providers (Google, OpenAI, Anthropic)
```

---

## Pipeline Flow

```
┌──────────┐    ┌──────────┐    ┌──────────┐    ┌──────────┐
│ Receive  │───▶│ Validate │───▶│ Feature  │───▶│  Rate    │
│ Request  │    │ Request  │    │   Flag   │    │  Limit   │
└──────────┘    └──────────┘    └──────────┘    └──────────┘
                                                    │
┌──────────┐    ┌──────────┐    ┌──────────┐        │
│  Build   │◀───│ Collect  │◀───│  Audit   │◀───────┤
│ Response │    │ Metrics  │    │ Request  │        │
└──────────┘    └──────────┘    └──────────┘        │
                                     │              │
                              ┌──────┘              │
                              ▼                     ▼
                        ┌──────────┐         ┌──────────┐
                        │ Execute  │◀────────│ Resolve  │
                        │ Provider │         │ Provider │
                        └──────────┘         └──────────┘
```

## Module Structure

```
com.sporekart.ai
├── core.api                  Reusable interfaces (AIService, AIExecutionPipeline, AIContextResolver,
│                             ProviderResolver, RetryStrategy, TimeoutStrategy, AIGateway,
│                             AIRequestValidator, AIRateLimiter, AIAuditService, AIHealthService,
│                             AIMetricsService, AIResponseMapper, AIRequestHandler, AIResponseHandler)
├── core.domain               Domain records (AiRequest, AiResponse, AiProviderType, AiRole,
│                             CorrelationId, ResponseEnvelope, GatewayExecutionContext, etc.)
├── core.application          Exceptions, feature flags, validation utilities
├── gateway.api               Gateway-specific interfaces (AiGateway, RateLimiter, RequestValidator)
├── gateway.domain            Gateway DTOs (AIExecutionRequest, AIExecutionResponse, AIHealthResponse,
│                             GatewayStatus, CorrelationMetadata, etc.)
├── gateway.application       Pipeline, services, validators, builders, audit, metrics, exception
│                             translation, feature management
├── gateway.infrastructure    Redis cache, Kafka publisher, health indicator, provider/retry/timeout
│                             strategy implementations, in-memory rate limiter, basic validator
├── gateway.config            Bean definitions for gateway components
└── interfaces.rest           GatewayController (5 endpoints)
```

---

## Cross-Cutting Concerns

### Observability
- **Metrics** — Micrometer counters (`ai.execution.count`), timers (`ai.execution`, `ai.latency`), error tracking (`ai.error.count`)
- **Logging** — Structured log entries via `GatewayAuditService` with audit logger
- **Tracing** — Correlation IDs propagated through every request

### Security
- Feature flag gating at pipeline entry
- Request validation (null/blank checks, max prompt length)
- Rate limiting interfaces
- SecurityConfig permits gateway health/status/features endpoints

### Caching (Redis)
- Gateway configuration (10 min TTL)
- Feature flags (5 min TTL)
- Health metadata (1 min TTL)
- Execution metadata (1 hour TTL)

### Events (Kafka)
- Topic: `ai-gateway-events` (3 partitions)
- Event types: AIRequestReceived, AIRequestValidated, AIRequestRejected, AIExecutionStarted, AIExecutionCompleted, AIExecutionFailed, GatewayHealthChanged

---

## Error Handling

All errors follow RFC 9457 Problem Details format via `ResponseEnvelope<T>`:

```json
{
  "success": false,
  "data": null,
  "errorCode": "AI-006",
  "errorMessage": "Prompt must not be blank",
  "timestamp": "2026-07-11T12:00:00Z",
  "correlationId": "uuid",
  "metadata": {}
}
```

### Error Codes

| Code | Exception | Description |
|------|-----------|-------------|
| AI-001 | `FeatureDisabledException` | Feature or module is disabled |
| AI-005 | `GatewayUnavailableException` | Gateway is unavailable |
| AI-006 | `AIValidationException` | Request validation failed |
| AI-007 | `AIRateLimitException` | Rate limit exceeded |
| AI-008 | `AIValidationException` | Prompt exceeds max length |
| AI-003 | `AIExecutionException` | No provider available |
| AI-004 | `AIExecutionException` | Request timed out |
| AI-014 | `AIExecutionException` | Execution failed after retries |
| AI-999 | `AIExecutionException` | Internal error |

---

## Backward Compatibility

- All existing modules (provider, prompt, rag, search, chat, content, workflow, monitoring) remain unchanged
- Existing `application/`, `domain/`, `infrastructure/`, `interfaces/` packages untouched
- No existing API paths modified
- New endpoints added under `/api/v1/ai/*` prefix
