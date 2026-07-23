# Copilot API Reference

> **Base URL:** `http://localhost:8080/api/copilot` (via gateway)  
> **Content-Type:** `application/json`  
> **Authentication:** Bearer JWT token (`Authorization: Bearer <token>`)

---

## 1. Authentication and Authorization

### 1.1 Authentication

All endpoints require a JWT bearer token obtained from the Identity Service (`POST /api/auth/login`). The gateway validates the token and propagates claims to downstream services.

```http
Authorization: Bearer eyJhbGciOiJSUzI1NiIs...
```

### 1.2 Role Requirements

| Endpoint | Required Role |
|----------|---------------|
| `GET /health` | Public |
| `POST /chat` | `ROLE_USER` |
| `POST /stream` | `ROLE_USER` |
| `GET /list` | `ROLE_USER` |
| `GET /capabilities` | `ROLE_USER` |
| `GET /personas` | `ROLE_USER` |
| `POST /register` | `ROLE_ADMIN`, `ROLE_COPILOT_MANAGER` |
| `POST /context` | `ROLE_USER` |
| `GET /{id}` | `ROLE_USER` |
| `GET /{id}/health` | `ROLE_USER` |
| `PUT /{id}/enable` | `ROLE_ADMIN` |
| `PUT /{id}/disable` | `ROLE_ADMIN` |
| `POST /session` | `ROLE_USER` |
| `GET /session/{sessionId}` | `ROLE_USER` (own), `ROLE_ADMIN` (all) |
| `DELETE /session/{sessionId}` | `ROLE_USER` (own), `ROLE_ADMIN` (all) |

---

## 2. Endpoints

### 2.1 GET /health

**Description:** Health check for the copilot service.

**Response `200 OK`:**
```json
{
  "status": "UP",
  "timestamp": "2026-07-23T10:30:00Z",
  "version": "0.2.0-SNAPSHOT",
  "components": {
    "ai-service": { "status": "UP" },
    "memory-service": { "status": "UP" },
    "redis": { "status": "UP" }
  }
}
```

| Field | Type | Description |
|-------|------|-------------|
| `status` | string | `UP`, `DOWN`, or `DEGRADED` |
| `timestamp` | string (ISO 8601) | Response timestamp |
| `version` | string | Service version |
| `components` | object | Per-dependency health |

---

### 2.2 POST /chat

**Description:** Send a chat message to a copilot and receive a response.

**Request Body:**
```json
{
  "message": "What is the status of my order?",
  "sessionId": "550e8400-e29b-41d4-a716-446655440000",
  "copilotId": "660e8400-e29b-41d4-a716-446655440001",
  "persona": "support-agent",
  "context": {
    "includeOrderHistory": true,
    "includeUserProfile": true
  }
}
```

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `message` | string | Yes | User's message |
| `sessionId` | string (uuid) | No | Session for conversation continuity |
| `copilotId` | string (uuid) | No | Target copilot (defaults to user's assigned copilot) |
| `persona` | string | No | Persona override |
| `context` | object | No | Context assembly overrides |
| `metadata` | object | No | Custom metadata |

**Response `200 OK`:**
```json
{
  "id": "770e8400-e29b-41d4-a716-446655440002",
  "message": "Your order #12345 is currently being shipped.",
  "sessionId": "550e8400-e29b-41d4-a716-446655440000",
  "copilotId": "660e8400-e29b-41d4-a716-446655440001",
  "persona": "support-agent",
  "finishReason": "stop",
  "usage": {
    "promptTokens": 245,
    "completionTokens": 18,
    "totalTokens": 263
  },
  "timestamp": "2026-07-23T10:30:05Z"
}
```

| Field | Type | Description |
|-------|------|-------------|
| `id` | string (uuid) | Response message ID |
| `message` | string | Copilot's response text |
| `sessionId` | string (uuid) | Session ID |
| `copilotId` | string (uuid) | Copilot that handled the request |
| `persona` | string | Persona used |
| `finishReason` | string | `stop`, `length`, `content_filter`, `tool_calls`, `error` |
| `usage` | object | Token usage statistics |
| `timestamp` | string (ISO 8601) | Response timestamp |

**Error Responses:**

| Status | Error Code | Description |
|--------|------------|-------------|
| 400 | `INVALID_REQUEST` | Missing or malformed fields |
| 401 | `UNAUTHORIZED` | Missing or invalid token |
| 403 | `FORBIDDEN` | Insufficient permissions |
| 429 | `RATE_LIMITED` | Too many requests |
| 500 | `INTERNAL_ERROR` | Unexpected server error |

---

### 2.3 POST /stream

**Description:** Send a message and receive a streaming response via Server-Sent Events.

**Request Body:**
```json
{
  "message": "Explain the return policy in detail",
  "sessionId": "550e8400-e29b-41d4-a716-446655440000",
  "copilotId": "660e8400-e29b-41d4-a716-446655440001",
  "persona": "support-agent",
  "temperature": 0.5,
  "maxTokens": 2048
}
```

| Field | Type | Required | Default | Description |
|-------|------|----------|---------|-------------|
| `message` | string | Yes | — | User's message |
| `sessionId` | string (uuid) | No | — | Session ID |
| `copilotId` | string (uuid) | No | — | Target copilot |
| `persona` | string | No | — | Persona override |
| `context` | object | No | — | Context overrides |
| `temperature` | number | No | 0.7 | Response creativity (0.0–2.0) |
| `maxTokens` | integer | No | 2048 | Max response tokens |
| `metadata` | object | No | — | Custom metadata |

**Response `200 OK` (SSE stream):**

```text
event: token
data: {"token":"Our","index":0,"sessionId":"550e8400-..."}

event: token
data: {"token":" return","index":1,"sessionId":"550e8400-..."}

event: token
data: {"token":" policy","index":2,"sessionId":"550e8400-..."}

event: token
data: {"token":" allows","index":3,"sessionId":"550e8400-..."}

event: done
data: {"id":"770e8400-...","finishReason":"stop","usage":{"promptTokens":120,"completionTokens":85,"totalTokens":205}}
```

| SSE Event | Description | Data Fields |
|-----------|-------------|-------------|
| `token` | A single response token | `token`, `index`, `sessionId` |
| `error` | An error occurred | `error`, `message`, `sessionId` |
| `done` | Stream completed | `id`, `finishReason`, `usage` |

---

### 2.4 GET /list

**Description:** List all registered copilots.

**Query Parameters:**

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `page` | integer | 0 | Page number (0-indexed) |
| `size` | integer | 20 | Page size (max 100) |

**Response `200 OK`:**
```json
{
  "content": [
    {
      "id": "660e8400-e29b-41d4-a716-446655440001",
      "name": "support-copilot",
      "description": "Customer support assistant",
      "status": "ACTIVE",
      "persona": "support-agent",
      "capabilities": ["order.lookup", "ticket.create"],
      "version": "1.0.0",
      "healthStatus": { "status": "UP" },
      "createdAt": "2026-07-01T00:00:00Z",
      "updatedAt": "2026-07-23T00:00:00Z"
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 1,
  "totalPages": 1
}
```

---

### 2.5 GET /capabilities

**Description:** List all registered capabilities.

**Response `200 OK`:**
```json
{
  "capabilities": [
    {
      "id": "880e8400-e29b-41d4-a716-446655440003",
      "name": "order.lookup",
      "description": "Look up order details by order ID",
      "category": "query",
      "inputSchema": {
        "orderId": { "type": "string", "description": "The order ID" }
      },
      "outputSchema": {
        "orderId": { "type": "string" },
        "status": { "type": "string" },
        "total": { "type": "number" }
      },
      "cost": 1,
      "timeout": "5s",
      "enabled": true
    }
  ]
}
```

---

### 2.6 GET /personas

**Description:** List available personas.

**Query Parameters:**

| Parameter | Type | Description |
|-----------|------|-------------|
| `category` | string | Filter by category: `support`, `sales`, `admin`, `technical`, `custom` |

**Response `200 OK`:**
```json
{
  "personas": [
    {
      "id": "990e8400-e29b-41d4-a716-446655440004",
      "name": "support-agent",
      "description": "Customer support agent persona",
      "category": "support",
      "systemPrompt": "You are a helpful customer support agent...",
      "temperature": 0.5,
      "maxTokens": 1024,
      "allowedCapabilities": ["order.lookup", "ticket.create"],
      "tools": ["create_support_ticket"],
      "createdAt": "2026-07-01T00:00:00Z",
      "updatedAt": "2026-07-23T00:00:00Z"
    }
  ]
}
```

---

### 2.7 POST /register

**Description:** Register a new copilot.

**Request Body:**
```json
{
  "name": "support-copilot",
  "description": "Customer support assistant",
  "persona": "support-agent",
  "capabilities": ["order.lookup", "order.status"],
  "tools": ["create_support_ticket"],
  "configuration": {
    "model": "gpt-4o",
    "allowEscalation": true
  },
  "maxConcurrency": 20,
  "rateLimitPerMinute": 100
}
```

**Response `201 Created`:**
```json
{
  "id": "660e8400-e29b-41d4-a716-446655440001",
  "name": "support-copilot",
  "apiKey": "sk-cop-abc123def456",
  "status": "ACTIVE",
  "createdAt": "2026-07-23T10:30:00Z"
}
```

---

### 2.8 POST /context

**Description:** Assemble context for a copilot interaction.

**Request Body:**
```json
{
  "sessionId": "550e8400-e29b-41d4-a716-446655440000",
  "copilotId": "660e8400-e29b-41d4-a716-446655440001",
  "userId": "user-123",
  "includeSources": ["orders", "profile", "support"],
  "maxTokens": 4096
}
```

**Response `200 OK`:**
```json
{
  "sessionId": "550e8400-e29b-41d4-a716-446655440000",
  "context": {
    "user": {
      "included": true,
      "summary": "User: John Doe, Role: CUSTOMER",
      "tokenCount": 42,
      "sourceService": "identity",
      "data": { "name": "John Doe", "email": "john@example.com" }
    },
    "orders": {
      "included": true,
      "summary": "3 recent orders, 1 in transit",
      "tokenCount": 128,
      "sourceService": "order",
      "data": { "recentOrders": ["#12345", "#12346", "#12347"] }
    },
    "support": {
      "included": true,
      "summary": "1 open ticket: TKT-789",
      "tokenCount": 64,
      "sourceService": "support",
      "data": { "tickets": [{ "id": "TKT-789", "status": "open" }] }
    }
  },
  "assembledAt": "2026-07-23T10:30:00Z",
  "tokenCount": 234,
  "sourcesUsed": ["user", "orders", "support"]
}
```

---

### 2.9 GET /{id}

**Description:** Get detailed information about a registered copilot.

**Path Parameters:**

| Parameter | Type | Description |
|-----------|------|-------------|
| `id` | string (uuid) | Copilot ID |

**Response `200 OK`:** See `CopilotDetails` schema.

---

### 2.10 GET /{id}/health

**Description:** Get health status of a specific copilot.

**Response `200 OK`:**
```json
{
  "copilotId": "660e8400-e29b-41d4-a716-446655440001",
  "status": "UP",
  "uptime": "7d 3h 12m",
  "latency": "245ms",
  "memoryUsage": "256 MB",
  "activeSessions": 5,
  "lastError": null,
  "timestamp": "2026-07-23T10:30:00Z"
}
```

---

### 2.11 PUT /{id}/enable

**Description:** Enable a disabled copilot.

**Response `200 OK`:**
```json
{
  "id": "660e8400-e29b-41d4-a716-446655440001",
  "status": "ACTIVE",
  "message": "Copilot enabled successfully",
  "timestamp": "2026-07-23T10:30:00Z"
}
```

---

### 2.12 PUT /{id}/disable

**Description:** Disable an active copilot.

**Response `200 OK`:**
```json
{
  "id": "660e8400-e29b-41d4-a716-446655440001",
  "status": "INACTIVE",
  "message": "Copilot disabled successfully",
  "timestamp": "2026-07-23T10:30:00Z"
}
```

---

### 2.13 POST /session

**Description:** Create a new copilot session.

**Request Body:**
```json
{
  "copilotId": "660e8400-e29b-41d4-a716-446655440001",
  "persona": "support-agent",
  "userId": "user-123",
  "metadata": { "channel": "web", "referrer": "help-page" },
  "ttlMinutes": 120
}
```

**Response `201 Created`:**
```json
{
  "sessionId": "550e8400-e29b-41d4-a716-446655440000",
  "copilotId": "660e8400-e29b-41d4-a716-446655440001",
  "persona": "support-agent",
  "status": "ACTIVE",
  "createdAt": "2026-07-23T10:30:00Z",
  "expiresAt": "2026-07-23T12:30:00Z",
  "metadata": { "channel": "web" }
}
```

---

### 2.14 GET /session/{sessionId}

**Description:** Get session details and optionally the message history.

**Path Parameters:**

| Parameter | Type | Description |
|-----------|------|-------------|
| `sessionId` | string (uuid) | Session ID |

**Query Parameters:**

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `includeHistory` | boolean | false | Include full message history |

**Response `200 OK`:**
```json
{
  "sessionId": "550e8400-e29b-41d4-a716-446655440000",
  "copilotId": "660e8400-e29b-41d4-a716-446655440001",
  "persona": "support-agent",
  "status": "ACTIVE",
  "messageCount": 5,
  "history": [
    {
      "id": "aa0e8400-e29b-41d4-a716-446655440005",
      "role": "user",
      "content": "What is the status of my order?",
      "timestamp": "2026-07-23T10:29:00Z",
      "tokens": 8
    },
    {
      "id": "bb0e8400-e29b-41d4-a716-446655440006",
      "role": "assistant",
      "content": "Your order #12345 is being shipped.",
      "timestamp": "2026-07-23T10:29:05Z",
      "tokens": 12
    }
  ],
  "contextTokenCount": 234,
  "totalTokenUsage": 489,
  "createdAt": "2026-07-23T10:25:00Z",
  "lastActivityAt": "2026-07-23T10:29:05Z",
  "expiresAt": "2026-07-23T11:25:00Z"
}
```

---

### 2.15 DELETE /session/{sessionId}

**Description:** Delete a session and its history.

**Response `204 No Content`:** No body.

---

## 3. Error Codes

| HTTP Status | Error Code | Description |
|-------------|------------|-------------|
| 400 | `INVALID_REQUEST` | Request body validation failed |
| 400 | `INVALID_SESSION` | Session ID is invalid or malformed |
| 400 | `UNSUPPORTED_PERSONA` | The specified persona does not exist |
| 400 | `UNSUPPORTED_CAPABILITY` | The specified capability does not exist |
| 401 | `UNAUTHORIZED` | Missing or expired JWT token |
| 403 | `FORBIDDEN` | Token valid but insufficient roles |
| 404 | `COPILOT_NOT_FOUND` | No copilot with the given ID |
| 404 | `SESSION_NOT_FOUND` | No session with the given ID |
| 409 | `COPILOT_ALREADY_EXISTS` | A copilot with this name is already registered |
| 429 | `RATE_LIMITED` | Request rate exceeded the allowed limit |
| 500 | `INTERNAL_ERROR` | Unexpected server error |
| 502 | `UPSTREAM_ERROR` | Upstream service (AI, memory) returned an error |
| 504 | `GATEWAY_TIMEOUT` | Upstream service timed out |

### Error Response Format

```json
{
  "error": "RATE_LIMITED",
  "message": "Too many requests. Please try again in 30 seconds.",
  "status": 429,
  "timestamp": "2026-07-23T10:30:00Z",
  "path": "/api/copilot/chat",
  "traceId": "abc123def456"
}
```

---

## 4. Rate Limiting

| Endpoint | Default Limit | Burst | Window |
|----------|---------------|-------|--------|
| `/chat` | 60 req/min | 100 | 1 minute |
| `/stream` | 30 req/min | 50 | 1 minute |
| `/list` | 120 req/min | 200 | 1 minute |
| `/register` | 10 req/min | 20 | 1 minute |
| `/session` (all) | 60 req/min | 100 | 1 minute |

Rate limit headers are returned on all responses:

```http
X-RateLimit-Limit: 60
X-RateLimit-Remaining: 42
X-RateLimit-Reset: 1627039800
```

---

## 5. Streaming Protocol

The streaming endpoint uses [Server-Sent Events (SSE)](https://html.spec.whatwg.org/multipage/server-sent-events.html).

### Connection

```bash
curl -X POST http://localhost:8080/api/copilot/stream \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -H "Accept: text/event-stream" \
  -d '{"message": "Hello", "copilotId": "<id>"}'
```

### Event Types

#### token
```text
event: token
data: {"token":"Hello","index":0,"sessionId":"..."}
```

#### tool_call
```text
event: tool_call
data: {"id":"call_xxx","function":{"name":"create_ticket","arguments":"{\"subject\":\"...\"}"}}
```

#### tool_result
```text
event: tool_result
data: {"id":"call_xxx","result":{"ticketId":"TKT-123"}}
```

#### error
```text
event: error
data: {"error":"UPSTREAM_ERROR","message":"AI service unavailable","sessionId":"..."}
```

#### done
```text
event: done
data: {"id":"...","finishReason":"stop","usage":{"promptTokens":100,"completionTokens":50,"totalTokens":150}}
```

### Client Handling

```javascript
const eventSource = new EventSource('/api/copilot/stream');

eventSource.addEventListener('token', (event) => {
  const data = JSON.parse(event.data);
  processToken(data.token);
});

eventSource.addEventListener('done', (event) => {
  const data = JSON.parse(event.data);
  finishStream(data);
});

eventSource.addEventListener('error', (event) => {
  const data = JSON.parse(event.data);
  handleError(data);
});
```

---

## 6. Examples

### 6.1 Basic Chat

```bash
curl -X POST http://localhost:8080/api/copilot/chat \
  -H "Authorization: Bearer eyJhbGciOiJSUzI1NiIs..." \
  -H "Content-Type: application/json" \
  -d '{
    "message": "What is my order status?",
    "sessionId": "550e8400-e29b-41d4-a716-446655440000"
  }'
```

### 6.2 Streaming Chat

```bash
curl -X POST http://localhost:8080/api/copilot/stream \
  -H "Authorization: Bearer eyJhbGciOiJSUzI1NiIs..." \
  -H "Content-Type: application/json" \
  -H "Accept: text/event-stream" \
  -d '{
    "message": "Tell me about your return policy",
    "copilotId": "660e8400-e29b-41d4-a716-446655440001",
    "temperature": 0.3
  }'
```

### 6.3 Register a Copilot

```bash
curl -X POST http://localhost:8080/api/copilot/register \
  -H "Authorization: Bearer eyJhbGciOiJSUzI1NiIs..." \
  -H "Content-Type: application/json" \
  -d '{
    "name": "my-support-copilot",
    "description": "Custom support copilot",
    "persona": "support-agent",
    "capabilities": ["order.lookup", "order.status"],
    "tools": ["create_support_ticket"],
    "rateLimitPerMinute": 60
  }'
```

### 6.4 List All Copilots

```bash
curl -X GET "http://localhost:8080/api/copilot/list?page=0&size=20" \
  -H "Authorization: Bearer eyJhbGciOiJSUzI1NiIs..."
```

### 6.5 Get Copilot Details

```bash
curl -X GET http://localhost:8080/api/copilot/660e8400-e29b-41d4-a716-446655440001 \
  -H "Authorization: Bearer eyJhbGciOiJSUzI1NiIs..."
```

### 6.6 Create a Session

```bash
curl -X POST http://localhost:8080/api/copilot/session \
  -H "Authorization: Bearer eyJhbGciOiJSUzI1NiIs..." \
  -H "Content-Type: application/json" \
  -d '{
    "copilotId": "660e8400-e29b-41d4-a716-446655440001",
    "persona": "support-agent",
    "ttlMinutes": 120
  }'
```

### 6.7 Get Session with History

```bash
curl -X GET "http://localhost:8080/api/copilot/session/550e8400-e29b-41d4-a716-446655440000?includeHistory=true" \
  -H "Authorization: Bearer eyJhbGciOiJSUzI1NiIs..."
```

### 6.8 Delete a Session

```bash
curl -X DELETE http://localhost:8080/api/copilot/session/550e8400-e29b-41d4-a716-446655440000 \
  -H "Authorization: Bearer eyJhbGciOiJSUzI1NiIs..."
```

### 6.9 Disable a Copilot

```bash
curl -X PUT http://localhost:8080/api/copilot/660e8400-e29b-41d4-a716-446655440001/disable \
  -H "Authorization: Bearer eyJhbGciOiJSUzI1NiIs..."
```

### 6.10 Assemble Context

```bash
curl -X POST http://localhost:8080/api/copilot/context \
  -H "Authorization: Bearer eyJhbGciOiJSUzI1NiIs..." \
  -H "Content-Type: application/json" \
  -d '{
    "sessionId": "550e8400-e29b-41d4-a716-446655440000",
    "includeSources": ["orders", "profile", "support"]
  }'
```
