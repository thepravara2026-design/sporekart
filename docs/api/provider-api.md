# AI Provider API

**Version:** 1.0.0
**Last Updated:** 2026-07-11
**Base Path:** `/api/v1/ai/providers`

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

### GET /api/v1/ai/providers

List all registered providers.

**Success Response (200):**

```json
{
  "success": true,
  "data": [
    {
      "available": true,
      "capabilities": ["TEXT_GENERATION", "CHAT_COMPLETION", "STREAMING", "VISION"]
    }
  ]
}
```

---

### GET /api/v1/ai/providers/{id}

Get provider details by type (e.g., `GEMINI`, `OPENAI`).

**Success Response (200):**

```json
{
  "success": true,
  "data": {
    "available": true,
    "capabilities": ["TEXT_GENERATION", "CHAT_COMPLETION", "STREAMING"]
  }
}
```

**Error Response (404):**

```json
{
  "success": false,
  "data": null,
  "errorCode": "AI-010",
  "errorMessage": "Provider not found: UNKNOWN"
}
```

---

### GET /api/v1/ai/providers/capabilities

List capabilities of all registered providers.

**Success Response (200):**

```json
{
  "success": true,
  "data": {
    "GEMINI": ["TEXT_GENERATION", "CHAT_COMPLETION", "STREAMING", "IMAGE_ANALYSIS"],
    "OPENAI": ["TEXT_GENERATION", "CHAT_COMPLETION", "EMBEDDINGS", "STREAMING", "FUNCTION_CALLING", "CODE_GENERATION"]
  }
}
```

---

### GET /api/v1/ai/providers/health

Health status of all providers.

**Success Response (200):**

```json
{
  "success": true,
  "data": [
    {
      "providerName": "GEMINI",
      "healthy": true,
      "degraded": false,
      "latencyMs": 0,
      "lastChecked": "2026-07-11T12:00:00Z",
      "details": "Operational"
    }
  ]
}
```

---

### POST /api/v1/ai/providers/switch

Switch active provider for a module.

**Request Body:**

```json
{
  "module": "chat",
  "provider": "OPENAI"
}
```

**Success Response (200):**

```json
{
  "success": true,
  "data": {
    "module": "chat",
    "selectedProvider": "OPENAI",
    "status": "SWITCHED"
  }
}
```

---

### POST /api/v1/ai/providers/validate

Validate a provider configuration.

**Request Body:**

```json
{
  "provider": "GEMINI"
}
```

**Success Response (200):**

```json
{
  "success": true,
  "data": {
    "provider": "GEMINI",
    "valid": true,
    "error": null
  }
}
```

**Validation Failure Response (200):**

```json
{
  "success": true,
  "data": {
    "provider": "UNKNOWN",
    "valid": false,
    "error": "Provider not registered: UNKNOWN"
  }
}
```

---

## Error Codes

| Code | HTTP Status | Description |
|------|-------------|-------------|
| AI-006 | 400 | Validation failure (missing module/provider) |
| AI-010 | 200* | Provider not found |

*Provider errors return 200 with `success: false` in the envelope.
