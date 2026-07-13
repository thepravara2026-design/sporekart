# Conversation API Reference

**Base URL:** `/api/v1/conversation`
**Module:** ai-service

---

## Sessions

### Create Session

`POST /api/v1/conversation/sessions`

**Request Body:**
```json
{
  "userId": "user-1",
  "title": "Customer Support Inquiry"
}
```

**Response (201):**
```json
{
  "id": "uuid",
  "userId": "user-1",
  "title": "Customer Support Inquiry",
  "status": "ACTIVE",
  "createdAt": "2026-07-12T10:00:00+05:30",
  "updatedAt": "2026-07-12T10:00:00+05:30"
}
```

### Get Session

`GET /api/v1/conversation/sessions/{sessionId}`

**Response (200):** Single session object.

### List User Sessions

`GET /api/v1/conversation/sessions?userId={userId}`

**Response (200):** Array of session objects.

### Suspend Session

`PUT /api/v1/conversation/sessions/{sessionId}/suspend`

**Response (200):** Session with `ARCHIVED` status.

### Resume Session

`PUT /api/v1/conversation/sessions/{sessionId}/resume`

**Response (200):** Session with `ACTIVE` status.

### Close Session

`PUT /api/v1/conversation/sessions/{sessionId}/close`

**Response (200):** Session with `CLOSED` status.

### Delete Session

`DELETE /api/v1/conversation/sessions/{sessionId}`

**Response (204):** No content.

---

## Messages

### Send Message

`POST /api/v1/conversation/sessions/{sessionId}/messages`

**Request Body:**
```json
{
  "role": "USER",
  "content": "What is the status of my order?"
}
```

**Response (201):**
```json
{
  "id": "uuid",
  "sessionId": "uuid",
  "role": "USER",
  "content": "What is the status of my order?",
  "status": "SENT",
  "createdAt": "2026-07-12T10:00:00+05:30"
}
```

### Get Session Messages

`GET /api/v1/conversation/sessions/{sessionId}/messages?offset=0&limit=100`

**Response (200):** Array of message objects ordered by creation date.

### Get Message

`GET /api/v1/conversation/messages/{messageId}`

**Response (200):** Single message object.

### Delete Message

`DELETE /api/v1/conversation/messages/{messageId}`

**Response (204):** No content.

---

## Memories

### Store Memory

`POST /api/v1/conversation/sessions/{sessionId}/memories`

**Request Body:**
```json
{
  "memoryType": "SHORT_TERM",
  "summary": "User asked about order status",
  "keywords": "order,status,delivery",
  "relevanceScore": 0.85
}
```

**Response (201):** Memory object.

### Get Session Memories

`GET /api/v1/conversation/sessions/{sessionId}/memories`

**Response (200):** Array of memory objects.

### Delete Memory

`DELETE /api/v1/conversation/memories/{memoryId}`

**Response (204):** No content.

---

## Context

### Get Session Context

`GET /api/v1/conversation/sessions/{sessionId}/context`

**Response (200):**
```json
[
  {
    "source": "conversation_history",
    "content": "USER: What is the status of my order?",
    "weight": 0.8
  }
]
```

### Get Context Sources

`GET /api/v1/conversation/sessions/{sessionId}/sources`

**Response (200):**
```json
{
  "knowledge_base": 1.0,
  "conversation_history": 0.8
}
```

### Refresh Context

`POST /api/v1/conversation/sessions/{sessionId}/context/refresh`

**Response (200):** OK.

---

## Health

### Health Check

`GET /api/v1/conversation/health`

**Response (200):**
```json
{
  "status": "UP",
  "sessionCount": 0,
  "cacheAvailable": true
}
```

---

## Error Responses

| Status | Description |
|--------|-------------|
| 400 | Invalid input (validation) |
| 404 | Session/Message not found |
| 429 | Rate limit exceeded |
| 500 | Internal server error |
