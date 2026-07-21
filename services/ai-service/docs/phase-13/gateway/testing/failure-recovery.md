# AI Gateway — Failure Recovery Guide

## Failure Scenarios & Recovery

### 1. Provider Unavailable

```
Trigger: Provider returns 503 or connection refused
Detection: StandardGatewayError.providerUnavailable()
Recovery: → fallback chain → next available provider
Retryable: Yes (exponential backoff, max 3 retries)
Circuit: Increments failure count → OPEN at threshold
```

### 2. Provider Timeout

```
Trigger: Request exceeds timeout window (default 30s)
Detection: StandardGatewayError.providerTimeout()
Recovery: → retry (up to 3 times) → fallback chain
Retryable: Yes
Circuit: Increments failure count
```

### 3. Rate Limited (429)

```
Trigger: Provider returns 429 with retry-after
Detection: StandardGatewayError.rateLimited()
Recovery: Retry after retry-after seconds → fallback if exhausted
Retryable: Yes (with backoff matching retry-after)
Circuit: Not counted (transient)
```

### 4. Invalid Authentication (401)

```
Trigger: API key invalid or expired
Detection: StandardGatewayError.invalidAuth()
Recovery: → fallback chain immediately (no retry)
Retryable: No
Circuit: Not counted (misconfiguration)
```

### 5. Quota Exceeded (429)

```
Trigger: Provider token/money quota exhausted
Detection: StandardGatewayError.quotaExceeded()
Recovery: → fallback chain → retry after quota reset
Retryable: Yes (after quota window)
Circuit: Not counted (transient/planning)
```

### 6. Malformed Response (502)

```
Trigger: Provider returns unparseable JSON or unexpected schema
Detection: StandardGatewayError.malformedResponse()
Recovery: → fallback chain immediately (no retry)
Retryable: No
Circuit: Increments failure count
```

### 7. Partial Response

```
Trigger: Provider returns incomplete data (missing fields)
Detection: StandardGatewayError.partialResponse()
Recovery: → retry → fallback chain
Retryable: Yes
Circuit: Increments failure count
```

### 8. Connection Refused (503)

```
Trigger: Provider host actively refuses TCP connection
Detection: StandardGatewayError.connectionRefused()
Recovery: → retry → fallback chain
Retryable: Yes
Circuit: Increments failure count → OPEN at threshold
```

### 9. TLS Failure (502)

```
Trigger: TLS handshake fails (certificate expired, mismatch)
Detection: StandardGatewayError.tlsFailure()
Recovery: → fallback chain immediately
Retryable: No (cert issue won't resolve with retry)
Circuit: Not counted (infrastructure issue)
```

### 10. DNS Failure (503)

```
Trigger: Provider hostname cannot be resolved
Detection: StandardGatewayError.dnsFailure()
Recovery: → retry → fallback chain
Retryable: Yes (DNS may recover)
Circuit: Increments failure count
```

### 11. Circuit Breaker Open

```
Trigger: Failure threshold exceeded
Detection: StandardGatewayError.circuitOpen()
Recovery: Wait for cooldown period → HALF_OPEN → test request → CLOSED
Retryable: Yes (after cooldown)
Circuit: Manages itself (OPEN → HALF_OPEN → CLOSED)
```

## Circuit Breaker State Machine

```
        ┌─────────────────────────────────────────┐
        │                                         │
        ▼                                         │
    ┌────────┐    failure >= threshold    ┌────────┐
    │ CLOSED │───────────────────────────→│  OPEN  │
    └────────┘                            └────────┘
        ↑                                     │
        │           cooldown expires          │
        │    ┌────────────────────────────────┘
        │    ▼
        │ ┌──────────┐    test succeeds    ┌──────────┐
        │ │ HALF_OPEN │───────────────────→│  CLOSED  │
        │ └──────────┘                     └──────────┘
        │      │
        │      │ test fails
        │      ▼
        │ ┌────────┐
        └─│  OPEN  │
          └────────┘
```

## Retry Strategy

```
DefaultRetryStrategy:
  maxRetries: 3
  baseDelay: 1000ms
  backoff: exponential (attempt ^ 2)

Attempt 1: 1000ms delay
Attempt 2: 2000ms delay
Attempt 3: 4000ms delay

Total max wait: 7000ms (before fallback)
```

## Fallback Chain

```
Priority order (configurable via sporekart.ai.gateway.router.fallback-providers):

1. Primary provider (e.g., GEMINI)
2. Fallback 1 (e.g., OPENAI)
3. Fallback 2 (e.g., CLAUDE)
4. Final fallback (MOCK — always succeeds)
```

## Quick Reference

| Failure Mode | Status Code | Retryable | Circuit Counted | Fallback |
|-------------|-------------|-----------|-----------------|----------|
| Unavailable | 503 | Yes | Yes | Yes |
| Timeout | 504 | Yes | Yes | Yes |
| Rate Limit | 429 | Yes | No | Yes |
| Invalid Auth | 401 | No | No | Yes |
| Quota | 429 | Yes | No | Yes |
| Malformed | 502 | No | Yes | Yes |
| Partial | 502 | Yes | Yes | Yes |
| Connection | 503 | Yes | Yes | Yes |
| TLS | 502 | No | No | Yes |
| DNS | 503 | Yes | Yes | Yes |
| Circuit Open | 503 | Yes | Self-managed | Yes |
