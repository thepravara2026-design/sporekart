# Decision Engine API Reference

Base URL: `/api/v1/decisions`

## Endpoints

### 1. POST /api/v1/decisions/evaluate

Evaluate a decision request.

**Request Body:**
```json
{
  "module": "content",
  "action": "GENERATE",
  "payload": {
    "contentType": "product_description",
    "wordCount": 500
  },
  "context": {
    "channel": "web",
    "locale": "en-US"
  },
  "userId": "user-123",
  "roles": ["content_editor"]
}
```

**Response (200):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "requestId": "660e8400-e29b-41d4-a716-446655440001",
  "action": "ALLOW",
  "status": "ALLOWED",
  "confidence": "HIGH",
  "summary": "Decision: ALLOW | Status: ALLOWED | Confidence: HIGH | Time: 12ms",
  "reasons": [
    {
      "code": "DECISION_ALLOW",
      "message": "Decision resolved to: ALLOW",
      "category": "outcome",
      "confidence": "HIGH",
      "details": {}
    }
  ],
  "processingTimeMs": 12,
  "requiresApproval": false
}
```

### 2. GET /api/v1/decisions

List decisions (stub — returns empty array).

**Response (200):**
```json
[]
```

### 3. GET /api/v1/decisions/{id}

Get a specific decision by UUID.

**Response (200):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "requestId": "660e8400-e29b-41d4-a716-446655440001",
  "action": "ALLOW",
  "status": "ALLOWED",
  "confidence": "HIGH",
  "summary": "Decision: ALLOW | Status: ALLOWED | Confidence: HIGH | Time: 12ms",
  "reasons": [],
  "processingTimeMs": 12,
  "requiresApproval": false
}
```

**Response (404):**
```json
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "Decision not found",
  "extensions": {}
}
```

### 4. GET /api/v1/decisions/history

Get decision history (stub — returns empty).

**Response (200):**
```json
{
  "entries": [],
  "total": 0
}
```

### 5. GET /api/v1/decisions/explanations

Get explanations (stub — returns empty array).

**Query Parameters:**
- `decisionId` (optional) — filter by decision UUID

**Response (200):**
```json
[]
```

### 6. GET /api/v1/decisions/statistics

Get decision statistics.

**Response (200):**
```json
{
  "totalDecisions": 42,
  "allowed": 30,
  "denied": 8,
  "escalated": 2,
  "approvals": 2,
  "detailed": {
    "totalDecisions": 42,
    "allowed": 30,
    "denied": 8,
    "escalated": 2,
    "approvals": 2,
    "conflicts": 1,
    "replays": 0,
    "avgConfidence": 2.5,
    "avgLatencyMs": 15.3
  }
}
```

### 7. GET /api/v1/decisions/health

Health check endpoint.

**Response (200):**
```json
{
  "status": "UP",
  "service": "decision-engine",
  "timestamp": 1720800000000,
  "details": {
    "metrics": {
      "totalDecisions": 42,
      "allowed": 30,
      "denied": 8,
      "escalated": 2,
      "approvals": 2,
      "conflicts": 1,
      "replays": 0,
      "avgConfidence": 2.5,
      "avgLatencyMs": 15.3
    }
  }
}
```

### 8. POST /api/v1/decisions/replay

Replay a decision (stub — logs request, records metrics, publishes events).

**Request Body:**
```json
{
  "requestId": "660e8400-e29b-41d4-a716-446655440001"
}
```

**Response (200):**
```json
{
  "success": true,
  "message": "Replay completed",
  "decisionId": "660e8400-e29b-41d4-a716-446655440001",
  "timestamp": 1720800000000
}
```

## Error Handling (RFC 9457)

Errors use `application/problem+json` format:

| Field | Type | Description |
|-------|------|-------------|
| `type` | string | URI identifying problem type (default: `about:blank`) |
| `title` | string | Short human-readable summary |
| `status` | int | HTTP status code |
| `detail` | string | Detailed error message |
| `extensions` | object | Additional metadata |

### Error Codes

| Method | Status | Code | Description |
|--------|--------|------|-------------|
| All endpoints | 500 | DEC_500 | Internal evaluation failure |
| GET /{id} | 404 | DEC_404 | Decision not found |
| All endpoints | 400 | DEC_400 | Bad request / validation error |
| All endpoints | 500 | - | Unhandled exception |

### Example Error Response (500)
```json
{
  "type": "about:blank",
  "title": "Internal Server Error",
  "status": 500,
  "detail": "Evaluation failed: null pointer",
  "extensions": {}
}
```

## DTO Summary

| DTO | Fields |
|-----|--------|
| `DecisionRequestDto` | module, action, payload, context, userId, roles |
| `DecisionResponseDto` | id, requestId, action, status, confidence, summary, reasons, processingTimeMs, requiresApproval |
| `ReasonDto` | code, message, category, confidence, details |
| `HealthDto` | status, service, timestamp, details |
| `StatisticsDto` | totalDecisions, allowed, denied, escalated, approvals, detailed |
| `HistoryDto` | entries, total |
| `HistoryEntryDto` | id, fromStatus, toStatus, triggeredBy, reason, timestamp |
| `ExplanationDto` | summary, matchedPolicies, triggeredRules, confidence, recommendedAction, explanationText |
| `ReplayDto` | success, message, decisionId, timestamp |
| `ErrorDto` | type, title, status, detail, extensions |
