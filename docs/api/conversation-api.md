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

## AI Conversation API v2

**Base URL:** `/api/v1/ai`

Endpoints for conversation, memory, and session management.

### Conversations

`POST /api/v1/ai/conversations`
Create a new conversation.

**Request Body:** `CreateConversationRequest`
```json
{
  "title": "Customer Inquiry",
  "workspaceId": "ws-1",
  "userId": "user-1"
}
```

**Response (200):** `ConversationResponse`

---

`GET /api/v1/ai/conversations/{id}`
Get a conversation by ID.

**Response (200):** `ConversationResponse`

---

`GET /api/v1/ai/conversations?status=ACTIVE&workspaceId=ws-1&userId=user-1`
List conversations, optionally filtered by status, workspaceId, and/or userId.

**Response (200):** List of `ConversationResponse`

---

`PUT /api/v1/ai/conversations/{id}/close`
Close a conversation.

**Response (200):** `ConversationResponse`

---

`PUT /api/v1/ai/conversations/{id}/archive`
Archive a conversation.

**Response (200):** `ConversationResponse`

---

`PUT /api/v1/ai/conversations/{id}/restore`
Restore an archived or closed conversation.

**Response (200):** `ConversationResponse`

---

`DELETE /api/v1/ai/conversations/{id}`
Soft-delete a conversation.

**Response (200):** `ConversationResponse`

### Messages

`GET /api/v1/ai/conversations/{id}/messages`
List messages in a conversation.

**Response (200):** List of `MessageResponse`

---

`POST /api/v1/ai/conversations/{id}/messages`
Add a message to a conversation.

**Request Body:** `MessageRequest`
```json
{
  "type": "USER",
  "content": "What is the status of my order?",
  "metadata": {}
}
```

**Response (200):** `MessageResponse`

---

`GET /api/v1/ai/conversations/{id}/messages/{messageId}`
Get a specific message by ID.

**Response (200):** `MessageResponse`

### Memory

`POST /api/v1/ai/memory`
Store a memory entry.

**Request Body:**
```json
{
  "key": "user-preference",
  "value": "dark-mode",
  "layer": "SESSION",
  "ttlSeconds": 604800
}
```

**Response (200):** `MemoryEntryResponse`

---

`GET /api/v1/ai/memory/query?key=user-preference&query=dark&layer=SESSION`
Query memory entries by key, content, and/or layer.

**Response (200):** List of `MemoryEntryResponse`

---

`GET /api/v1/ai/memory/{layer}`
Get all memory entries for a specific layer.

**Response (200):** List of `MemoryEntryResponse`

---

`DELETE /api/v1/ai/memory/{layer}`
Clear all memory entries in a layer.

**Response (204):** No content.

---

`DELETE /api/v1/ai/memory/workspace/{workspaceId}`
Clear all memory entries for a workspace across all layers.

**Response (204):** No content.

---

`POST /api/v1/ai/memory/promote`
Promote a memory entry to a higher layer.

**Request Body:**
```json
{
  "memoryId": "mem-123",
  "targetLayer": "LONG_TERM"
}
```

**Response (200):** `MemoryEntryResponse`

### Sessions

`GET /api/v1/ai/sessions/{id}`
Get a session by ID.

**Response (200):** `SessionResponse`

---

`POST /api/v1/ai/sessions`
Create a new session.

**Request Body:** `CreateSessionRequest`
```json
{
  "userId": "user-1",
  "workspaceId": "ws-1"
}
```

**Response (200):** `SessionResponse`

---

`POST /api/v1/ai/sessions/{id}/summarize`
Summarize a conversation in a session.

**Request Body:** `SummarizeRequest`
```json
{
  "conversationId": "conv-123",
  "summaryType": "CONCISE"
}
```

**Response (200):** `SummarizeResponse`

---

`POST /api/v1/ai/sessions/{id}/restore`
Restore a conversation from a summary.

**Request Body:** `RestoreRequest`
```json
{
  "conversationId": "conv-123",
  "summaryId": "sum-456"
}
```

**Response (200):** `RestoreResponse`

---

## Error Responses

| Status | Description |
|--------|-------------|
| 400 | Invalid input (validation) |
| 404 | Session/Message not found |
| 429 | Rate limit exceeded |
| 500 | Internal server error |
