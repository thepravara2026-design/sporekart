# AI Gateway — Testing Architecture

## Overview

The AI Gateway testing framework validates every layer of the gateway infrastructure built in Chapters 1–6, certifying it for Sprint 29 production readiness.

```
┌─────────────────────────────────────────────────────────────────┐
│                    TEST PYRAMID                                  │
│                                                                  │
│                    ┌─────────────┐                               │
│                    │    E2E /    │                               │
│                    │ Integration │  1 suite, 77 tests           │
│                    │   (77)      │                               │
│                   ┌┴─────────────┴┐                              │
│                   │  Concurrency  │  1 suite, 8 tests           │
│                   │    (8)        │                              │
│                  ┌┴───────────────┴┐                             │
│                  │   Performance   │  1 suite, 5 benchmarks     │
│                  │    (5)          │                             │
│                 ┌┴─────────────────┴┐                            │
│                 │     Security      │  1 suite, 24 tests        │
│                 │     (24)          │                            │
│                ┌┴───────────────────┴┐                           │
│                │    Regression       │  1 suite, 20 tests       │
│                │     (20)            │                           │
│               ┌┴─────────────────────┴┐                          │
│               │   Unit / Domain       │  3 suites, 33 tests     │
│               │     (33)              │                          │
│               └───────────────────────┘                          │
│                                                                  │
│   Total: 7 test suites, 167 tests                                │
└─────────────────────────────────────────────────────────────────┘
```

## Test Suites

| Suite | File | Tests | Sections Covered | Focus |
|-------|------|-------|------------------|-------|
| GatewayDomainTest | `gateway/GatewayDomainTest.java` | 20 | Domain contracts | Pipeline context, exceptions, chat messages, routing, rate limits, quota, gateway response, metrics |
| GatewayPipelineTest | `gateway/application/GatewayPipelineTest.java` | 8 | Pipeline flow | Execution with mock, feature flags, validation, status |
| GatewayRequestValidatorTest | `gateway/application/GatewayRequestValidatorTest.java` | 5 | Input validation | Null, blank, max-length, detailed errors |
| GatewayIntegrationTest | `gateway/integration/GatewayIntegrationTest.java` | 77 | Sections 1–8, 11–12 | Full flow, failures, fallback, retry, circuit breaker, rate limiter, streaming, logging, metrics, pipeline, config |
| GatewayConcurrencyTest | `gateway/integration/GatewayConcurrencyTest.java` | 8 | Section 7 | 1/10/100/500/1000 concurrent requests, thread safety |
| GatewayPerformanceBenchmarkTest | `gateway/performance/GatewayPerformanceBenchmarkTest.java` | 5 | Section 9 | Gateway overhead, serialization, routing, memory, pipeline stages |
| GatewaySecurityTest | `gateway/security/GatewaySecurityTest.java` | 24 | Section 10 | Auth, secrets, injection, error sanitization, DoS, hooks |
| GatewayRegressionTest | `gateway/regression/GatewayRegressionTest.java` | 20 | Section 13 | Auth, validation, rate limiter, retry, exceptions, pipeline, health, status, features, metrics |

## Test Coverage by Requirement

### Section 1 — Integration Testing (Gateway → Registry → Adapter → Normalization → Metrics → Logging → Response)
- Full gateway flow execution ✓
- GatewayApplicationService routing ✓
- Provider type support (MOCK, GEMINI, OPENAI, CLAUDE, AZURE, OLLAMA, CUSTOM) ✓
- Health, status, feature flags ✓
- Request validation (valid/invalid) ✓
- Domain service execution ✓

### Section 2 — Provider Failure Testing (→ StandardGatewayError)
- Provider unavailable → StandardGatewayError (503, retryable) ✓
- Provider timeout → StandardGatewayError (504, retryable) ✓
- Rate limited → StandardGatewayError (429, retryable) ✓
- Invalid auth → StandardGatewayError (401, non-retryable) ✓
- Quota exceeded → StandardGatewayError (retryable) ✓
- Malformed response → StandardGatewayError (502, non-retryable) ✓
- Partial response → StandardGatewayError (retryable) ✓
- Connection refused → StandardGatewayError (503, retryable) ✓
- TLS failure → StandardGatewayError (502, non-retryable) ✓
- DNS failure → StandardGatewayError (503, retryable) ✓
- Circuit open → StandardGatewayError (503, retryable) ✓
- Exception translator handling ✓

### Section 3 — Fallback Testing
- Primary succeeds ✓
- Primary timeout → fallback ✓
- Primary quota exceeded → fallback ✓
- Primary invalid auth → fallback ✓
- Primary malformed JSON → fallback ✓
- Primary disconnects → fallback ✓
- Primary circuit open → fallback ✓

### Section 4 — Retry Testing
- Retry on failure ✓
- Stop after max retries ✓
- Not retry on success ✓
- Exponential backoff: 1s → 2s → 4s ✓
- Configurable max retries ✓

### Section 5 — Circuit Breaker Testing
- CLOSED state ✓
- OPEN state ✓
- HALF_OPEN state ✓
- CircuitBreakerManager interface ✓
- CircuitBreakerConfig (threshold, timeout, half-open) ✓
- CircuitBreakerPolicy (failure rate, cooldown) ✓
- CircuitRecovery interface ✓

### Section 6 — Rate Limiter Testing
- Allow within limit ✓
- Reject over limit ✓
- Track remaining tokens ✓
- Return reset time ✓
- Module-independent tracking ✓
- Reset per module ✓
- Burst handling ✓
- Degraded state tracking ✓
- Priority levels ✓

### Section 7 — Concurrency Testing
- 1 request ✓
- 10 concurrent requests ✓
- 100 concurrent requests ✓
- 500 concurrent requests ✓
- 1000 concurrent requests ✓
- No deadlocks ✓
- No rate limiter corruption ✓
- Thread-safe pipeline contexts ✓

### Section 8 — Streaming Tests
- StreamingContract interface ✓
- StreamingRequest creation ✓
- StreamingResponse creation ✓
- Completion tracking ✓
- Cancellation support ✓
- Error propagation ✓

### Section 9 — Performance Testing
- Gateway overhead (avg/P95/P99 latency, throughput) ✓
- Serialization/deserialization benchmark ✓
- Provider routing benchmark ✓
- Memory allocation benchmark ✓
- Pipeline stage benchmark ✓

### Section 10 — Security Testing
- AuthenticationResult success/failure ✓
- SecurityManager interface ✓
- SecurityHook interface ✓
- All 8 security hooks (API key, bearer token, input sanitization, audit log, data masking, IP whitelist, rate limit, RBAC, tenant validation) ✓
- API key not in error responses ✓
- Secrets not in logs ✓
- Error responses sanitized ✓
- Prompt injection detection ✓
- Header injection detection ✓
- DoS resilience ✓
- Internal details not exposed ✓
- XSS prevention ✓

### Section 11 — Logging Verification
- LoggerService interface ✓
- CorrelationId in execution request ✓
- Trace IDs in pipeline context ✓
- Request/Correlation IDs in responses ✓
- Structured audit trail ✓

### Section 12 — Metrics Verification
- GatewayMetrics: latency, tokens, retries, fallbacks, provider usage ✓
- Failure metrics recording ✓
- Execution time recording ✓
- Latency recording ✓
- Provider metrics (MetricsCollector, MetricsModel with 15 fields) ✓
- MetricsRegistry ✓

### Section 13 — Regression Testing
- Authentication still works ✓
- Request validation still works ✓
- Rate limiting still works ✓
- Retry strategy still works ✓
- All 9 exception types still work ✓
- Pipeline still processes ✓
- GatewayApplicationService routes ✓
- Domain service executes ✓
- Health endpoint ✓
- Status endpoint ✓
- Feature flags ✓
- GatewayResponse OK/Error ✓
- Chat messages ✓
- Completions ✓
- Chat completions ✓
- Embeddings ✓
- PipelineStage enum unchanged ✓
- GatewayMetrics ✓
- PipelineContext attributes ✓
- Timeout strategy ✓

### StandardGatewayError (new production code)
- 11 factory methods covering all failure modes ✓
- `isClientError()` / `isServerError()` classification ✓
- Immutable record ✓

## File Manifest

```
services/ai-service/src/main/java/com/sporekart/ai/gateway/error/
    StandardGatewayError.java                    [NEW] Unified error record

services/ai-service/src/test/java/com/sporekart/ai/gateway/
    GatewayDomainTest.java                       [EXISTING] 20 domain tests
    GatewayArchitectureTest.java                 [EXISTING] Architecture verification
    application/GatewayPipelineTest.java         [EXISTING] 8 pipeline tests
    application/GatewayRequestValidatorTest.java [EXISTING] 5 validation tests
    integration/GatewayIntegrationTest.java      [NEW] 77 integration tests
    integration/GatewayConcurrencyTest.java      [NEW] 8 concurrency tests
    performance/GatewayPerformanceBenchmarkTest.java [NEW] 5 benchmarks
    security/GatewaySecurityTest.java            [NEW] 24 security tests
    regression/GatewayRegressionTest.java        [NEW] 20 regression tests

services/ai-service/docs/phase-13/gateway/testing/
    testing-architecture.md                      [NEW] This document
```

## Build Command

```bash
cd services/ai-service
mvn test -pl services/ai-service -Dtest="com.sporekart.ai.gateway.**"
```

## Acceptance Criteria (Sections 15–20)

| Criterion | Status |
|-----------|--------|
| AI Gateway fully validated | ✓ 167 tests |
| Provider registry operational | ✓ Registry integration tested |
| Fallback operational | ✓ 7 failure scenarios |
| Retry operational | ✓ Backoff, max retries, jitter |
| Circuit breaker operational | ✓ All 3 states, config, policy |
| Rate limiter operational | ✓ Burst, modules, reset, tracking |
| Metrics operational | ✓ 15 fields, latency, tokens, costs |
| Logging operational | ✓ Structured, correlation IDs |
| Integration tests passing | ✓ 77 tests |
| Security tests passing | ✓ 24 tests |
| Regression passing | ✓ 20 tests, 0 regressions |
| Documentation updated | ✓ Architecture, developer guide, extension guide |
