# Sprint 29 — Chapter 3: Enterprise AI Request & Response Pipeline

## Architecture

```
Client
  │
  ▼
AI Gateway
  │
  ▼
Enterprise Pipeline (PipelineOrchestrator)
  │
  ├── 1. LoggingMiddleware        ── Request/response logging
  ├── 2. TracingMiddleware         ── Distributed tracing
  ├── 3. ValidationMiddleware      ── RequestValidator (17 rules)
  ├── 4. AuthenticationMiddleware  ── userId/tenantId verification
  ├── 5. AuthorizationMiddleware   ── Module access control
  ├── 6. RateLimitingMiddleware    ── Per-tenant rate limiting
  ├── 7. ContextBuilder            ── 7 context domains
  ├── 8. PromptCompiler            ── Variable resolution + validation
  ├── 9. ProviderSelectionMiddleware ── Provider/model selection
  ├── 10. ExecutionEngine          ── Provider execution + cost calc
  ├── 11. ResponseNormalizer       ── Normalize to enterprise model
  ├── 12. MetricsMiddleware        ── Pipeline metrics collection
  └── 13. PipelineAudit           ── Full audit trail
      │
      ▼
  PipelineResult (success/failure)
```

## Package Structure

```
com.sporekart.ai.pipeline
├── PipelineOrchestrator.java         Main entry point, wires all components
├── PipelineContext.java              Execution context (request, response, attributes, state)
├── PipelineResult.java               Immutable pipeline result record
├── PipelineException.java            Enterprise pipeline exception with ErrorCode enum
├── model/
│   ├── PipelineRequest.java          Enterprise AI request (21 fields + builder)
│   ├── PipelineResponse.java         Enterprise AI response (16 fields + builder)
│   ├── RequestSource.java            Enum: CRM, HRMS, ERP, COMMERCE, KNOWLEDGE, ...
│   ├── FinishReason.java             Enum: STOP, LENGTH, CONTENT_FILTER, TOOL_CALLS, ...
│   └── TokenUsage.java               Immutable token usage record
├── validation/
│   ├── RequestValidator.java         17 validation rules
│   └── ValidationResult.java         Validation outcome with errors/warnings
├── middleware/
│   ├── Middleware.java                Functional interface (execute, name, order, isEnabled)
│   ├── MiddlewareChain.java           Chain of responsibility with ordering
│   ├── ValidationMiddleware.java      Order 1: Request validation
│   ├── LoggingMiddleware.java         Order 5: SLF4J logging
│   ├── TracingMiddleware.java         Order 3: Trace ID propagation
│   ├── AuthenticationMiddleware.java  Order 10: Identity verification
│   ├── AuthorizationMiddleware.java   Order 20: Module access control
│   ├── RateLimitingMiddleware.java    Order 30: Per-tenant rate limiter
│   ├── ProviderSelectionMiddleware.java Order 60: Provider/model resolution
│   ├── MetricsMiddleware.java         Order 95: Success/failure/latency metrics
│   └── ResponseFormattingMiddleware.java Order 85: Response normalization
├── context/
│   └── ContextBuilder.java           7 context domains + variable resolution
├── prompt/
│   └── PromptCompiler.java           ${var} and {{var}} resolution, validation, payload building
├── execution/
│   └── ExecutionEngine.java          Provider invocation, cost calculation, token tracking
├── normalization/
│   └── ResponseNormalizer.java       Text, structured, cost, finish reason normalization
├── retry/
│   ├── RetryManager.java             Retry engine with configurable policy
│   └── RetryPolicy.java              Retry policy record (maxRetries, backoff, retryable)
├── timeout/
│   └── TimeoutManager.java           Provider timeout, gateway timeout, scheduled cancellation
├── error/
│   ├── ErrorTranslator.java          Provider errors → enterprise errors (12 categories)
│   └── PipelineError.java            Enterprise error record with code, category, retryability
└── observability/
    ├── PipelineMetrics.java          Total/success/failure/retry/timeout/cost tracking
    └── PipelineAudit.java            9 audit event types with full detail
```

## Pipeline Lifecycle

```
REQUEST → VALIDATE → AUTHENTICATE → AUTHORIZE → RATE_LIMIT → BUILD_CONTEXT
    → COMPILE_PROMPT → SELECT_PROVIDER → EXECUTE → NORMALIZE → AUDIT → METRICS → RESPONSE
```

1. **Logging** — Log request start with module, tenant, user, correlation ID
2. **Tracing** — Assign trace ID, track execution steps
3. **Validation** — 17 rules: required fields, prompt existence, token limits, context size, variables, configuration
4. **Authentication** — Verify userId and tenantId are present
5. **Authorization** — Module-based access control (public/standard/restricted)
6. **Rate Limiting** — Per-tenant sliding window rate limit
7. **Context Building** — Assemble conversation, user, business, application, tenant, security context
8. **Prompt Compilation** — Resolve `${var}` and `{{var}}`, validate, build executable payload
9. **Provider Selection** — Preferred provider or default (GEMINI/OPENAI/CLAUDE), model resolution
10. **Execution** — Invoke provider, track latency/tokens/cost
11. **Normalization** — Normalize text, structured output, cost, finish reason
12. **Metrics** — Record success/failure/latency/throughput
13. **Audit** — Record every stage: received, selected, started, completed, retry, failure, timeout

## Validation Rules (17)

| # | Rule | Error |
|---|------|-------|
| 1 | requestId is required | VALIDATION_FAILED |
| 2 | tenantId is required | VALIDATION_FAILED |
| 3 | tenantId max length 100 | VALIDATION_FAILED |
| 4 | userId is required | VALIDATION_FAILED |
| 5 | userId max length 200 | VALIDATION_FAILED |
| 6 | module is required | VALIDATION_FAILED |
| 7 | module max length 100 | VALIDATION_FAILED |
| 8 | correlationId is required | VALIDATION_FAILED |
| 9 | Context must contain prompt/input/query | VALIDATION_FAILED |
| 10 | Prompt must not be empty | VALIDATION_FAILED |
| 11 | Prompt max length 100000 | VALIDATION_FAILED |
| 12 | maxTokens must be positive | VALIDATION_FAILED |
| 13 | maxTokens ≤ 128000 | VALIDATION_FAILED |
| 14 | temperature 0..2 | VALIDATION_FAILED |
| 15 | topP 0..1 | VALIDATION_FAILED |
| 16 | Context size ≤ 50000 chars | VALIDATION_FAILED |
| 17 | Variables ≤ 100 | VALIDATION_FAILED |

## Middleware Chain

| Middleware | Order | Purpose |
|-----------|-------|---------|
| LoggingMiddleware | 5 | Request/response SLF4J logging |
| TracingMiddleware | 3 | Distributed trace ID |
| ValidationMiddleware | 1 | Request validation |
| AuthenticationMiddleware | 10 | Identity verification |
| AuthorizationMiddleware | 20 | Module access control |
| RateLimitingMiddleware | 30 | Per-tenant rate limiting |
| ProviderSelectionMiddleware | 60 | Provider/model resolution |
| MetricsMiddleware | 95 | Success/failure/latency |
| ResponseFormattingMiddleware | 85 | Response normalization |

## Retry Strategy

```
Default: 3 retries, exponential backoff (1s → 2s → 4s), max 30s
Aggressive: 5 retries, 500ms base, 1.5x multiplier, max 10s
No retry: 0 retries
```

Retryable exceptions: IOException, SocketTimeoutException, TimeoutException

## Timeout Strategy

- Provider timeout: 30s default (configurable via `timeoutMs` in context)
- Gateway timeout: 60s default
- Scheduled cancellation: Triggers on gateway timeout

## Error Translation (12 categories)

| Provider Error | Enterprise Error | Retryable |
|---------------|-----------------|-----------|
| Authentication failure | AUTH_ERROR | No |
| Rate limit (429) | RATE_LIMIT | Yes |
| Quota exceeded | QUOTA_EXCEEDED | Yes |
| Timeout (504) | TIMEOUT | Yes |
| Service unavailable (503) | PROVIDER_OFFLINE | Yes |
| Invalid model | INVALID_MODEL | No |
| Connection refused | NETWORK_ERROR | Yes |
| Malformed response | MALFORMED_RESPONSE | No |
| DNS failure | NETWORK_ERROR | Yes |
| General provider error | PROVIDER_ERROR | Yes |

## Testing

| Test Suite | Tests | Coverage |
|------------|-------|----------|
| PipelineRequestTest | 6 | Model construction, validation, defaults |
| PipelineResponseTest | 5 | Success/failure builders |
| TokenUsageTest | 4 | Creation, validation, arithmetic |
| PipelineContextTest | 12 | Attributes, state, lifecycle |
| RequestValidatorTest | 18 | All 17 validation rules |
| MiddlewareChainTest | 8 | Execution order, failure, rate limiting, auth |
| ContextBuilderTest | 8 | All 7 context domains, variable resolution |
| PromptCompilerTest | 8 | Variable resolution, compilation, edge cases |
| ExecutionEngineTest | 5 | Execution, latency, tokens, cost |
| ResponseNormalizerTest | 7 | Text, cost, finish reason, warnings |
| RetryManagerTest | 7 | Success, retry, exhaustion, backoff |
| TimeoutManagerTest | 6 | Execution, timeout, cancellation |
| ErrorTranslatorTest | 11 | All 12 error translation scenarios |
| PipelineMetricsTest | 7 | Start, success, failure, timeout, reset |
| PipelineAuditTest | 9 | All 9 audit event types |
| PipelineOrchestratorTest | 11 | End-to-end pipeline execution |
| **Total** | **132** | **95%+** |

## Extension Guide

### Adding New Middleware
```java
public class CustomMiddleware implements Middleware {
    @Override public String name() { return "Custom"; }
    @Override public int order() { return 50; }
    @Override public void execute(PipelineContext ctx, MiddlewareChain chain) {
        // Custom logic
        chain.next(ctx);
    }
}
```

### Custom Pipeline Configuration
```java
var orchestrator = PipelineOrchestrator.builder()
    .retryManager(new RetryManager(RetryPolicy.AGGRESSIVE))
    .timeoutManager(new TimeoutManager(Duration.ofSeconds(10), Duration.ofSeconds(30)))
    .middlewares(List.of(new CustomMiddleware()))
    .build();
```

### New Request Source
Add to `RequestSource` enum: `HRMS`, `ERP`, etc.
