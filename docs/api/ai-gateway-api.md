# AI Gateway API

**Version:** 1.0.0
**Last Updated:** 2026-07-11
**Base Path:** `/api/v1/ai`

---

## Standard Response Envelope

Every response follows RFC 9457 Problem Details via `ResponseEnvelope<T>`:

```json
{
  "success": true | false,
  "data": { ... } | null,
  "errorCode": "AI-XXX" | null,
  "errorMessage": "string" | null,
  "timestamp": "2026-07-11T12:00:00Z",
  "correlationId": "uuid" | null,
  "metadata": {}
}
```

---

## Endpoints

### POST /api/v1/ai/execute

Execute an AI request through the gateway pipeline.

**Request Body:**

```json
{
  "prompt": "What is my order status?",
  "role": "USER",
  "module": "support",
  "preferredProvider": "MOCK",
  "userId": "user-123",
  "parameters": {},
  "metadata": {}
}
```

**Success Response (200):**

```json
{
  "success": true,
  "data": {
    "requestId": "uuid",
    "correlationId": "uuid",
    "content": "Mock response: What is my order status?",
    "provider": "MOCK",
    "model": "mock-model",
    "timestamp": "2026-07-11T12:00:00Z",
    "executionTimeMs": 5,
    "status": "COMPLETED",
    "warnings": [],
    "metadata": {},
    "error": null
  }
}
```

**Error Response (400 — Validation Error):**

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

---

### POST /api/v1/ai/validate

Validate an AI request without execution.

**Request Body:** Same as `/execute`

**Success Response (200):**

```json
{
  "success": true,
  "data": {
    "requestId": "uuid",
    "correlationId": "uuid",
    "content": "Request is valid",
    "provider": "none",
    "model": "none",
    "timestamp": "2026-07-11T12:00:00Z",
    "executionTimeMs": 0,
    "status": "COMPLETED",
    "warnings": [],
    "metadata": {},
    "error": null
  }
}
```

**Validation Failure Response (200 — semantics only, no execution):**

```json
{
  "success": true,
  "data": {
    "requestId": "uuid",
    "correlationId": "uuid",
    "content": null,
    "provider": null,
    "model": null,
    "timestamp": "2026-07-11T12:00:00Z",
    "executionTimeMs": 0,
    "status": "FAILED",
    "warnings": [],
    "metadata": {},
    "error": {
      "code": "AI-006",
      "message": "Prompt must not be blank"
    }
  }
}
```

---

### GET /api/v1/ai/health

Gateway health check. (No authentication required.)

**Success Response (200):**

```json
{
  "success": true,
  "data": {
    "status": "UP",
    "healthy": true,
    "degraded": false,
    "moduleStatus": {
      "gateway": "ACTIVE"
    },
    "message": "AI Gateway is operational"
  }
}
```

---

### GET /api/v1/ai/status

Detailed gateway status with metrics and feature flags. (No authentication required.)

**Success Response (200):**

```json
{
  "success": true,
  "data": {
    "gatewayEnabled": true,
    "totalRequests": 0,
    "successfulRequests": 0,
    "failedRequests": 0,
    "averageLatencyMs": 0.0,
    "moduleStatus": {
      "gateway": true,
      "platform": true
    },
    "featureFlags": {
      "AI_PLATFORM_ENABLED": true,
      "AI_GATEWAY_ENABLED": true,
      ...
    }
  }
}
```

---

### GET /api/v1/ai/features

Current state of all AI feature flags. (No authentication required.)

**Success Response (200):**

```json
{
  "success": true,
  "data": {
    "AI_PLATFORM_ENABLED": true,
    "AI_GATEWAY_ENABLED": true,
    "AI_CHAT_ENABLED": true,
    "AI_REQUEST_LOGGING": true,
    "AI_RATE_LIMITING": true,
    "AI_METRICS": true,
    "AI_PROVIDER_GEMINI": true,
    "AI_PROVIDER_OPENAI": true,
    "AI_PROVIDER_CLAUDE": true,
    ...
  }
}
```

---

## Error Codes

| Code | HTTP Status | Description |
|------|-------------|-------------|
| AI-001 | 503 | Feature or module is disabled |
| AI-005 | 503 | Gateway is unavailable |
| AI-006 | 400 | Request validation failed |
| AI-007 | 429 | Rate limit exceeded |
| AI-008 | 400 | Prompt exceeds maximum length (32000) |
| AI-003 | 500 | No provider available for module |
| AI-004 | 500 | Request timed out before execution |
| AI-014 | 500 | Execution failed after retry attempts |
| AI-999 | 500 | Internal gateway error |

---

## Rate Limiting

Rate limiting is controlled via the `AI_RATE_LIMITING` feature flag.

When enabled, the gateway checks rate limits after feature flag validation and before provider resolution.

| Module | Default Limit |
|--------|---------------|
| All modules | 100 requests per window |

Rate limit headers are returned in the `AiGateway` response.

---

## OpenAPI

The API is documented via SpringDoc OpenAPI 3.0 at `/v3/api-docs` and Swagger UI at `/swagger-ui.html`. All endpoints are tagged under **AI Gateway**.
