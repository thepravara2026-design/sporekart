# Unified Copilot Workspace — API Reference

**Version:** 1.0.0  
**Base URL:** `http://localhost:8103/api/v1`

All endpoints require authentication unless marked otherwise.

---

## Endpoints

### 1. Chat

Send a message to the workspace's active copilot (or auto-routed copilot).

```
POST /workspace/{workspaceId}/chat
```

**Request Body:**
```json
{
  "message": "string",
  "session_id": "string (optional)"
}
```

**Response** (`200`):
```json
{
  "workspace_id": "string",
  "copilot": "string",
  "reply": "string",
  "confidence": 0.95,
  "routed": true,
  "suggestions": ["string"]
}
```

---

### 2. Collaboration

Send a message that triggers multi-copilot collaboration.

```
POST /workspace/{workspaceId}/collaborate
```

**Request Body:**
```json
{
  "message": "string",
  "copilots": ["marketing", "sales"],
  "timeout": 30
}
```

**Response** (`200`):
```json
{
  "workspace_id": "string",
  "summary": "string",
  "sections": {
    "marketing": {},
    "sales": {}
  },
  "collaboration_status": "done",
  "partial": false
}
```

---

### 3. Get Workspace Status

```
GET /workspace/{workspaceId}
```

**Response** (`200`):
```json
{
  "workspace_id": "string",
  "active_copilot": "string",
  "default_copilot": "string",
  "status": "active",
  "message_count": 0,
  "created_at": "2026-07-23T10:00:00Z",
  "updated_at": "2026-07-23T10:00:00Z"
}
```

---

### 4. Create Workspace

```
POST /workspace
```

**Request Body:**
```json
{
  "default_copilot": "commerce (optional)",
  "user_id": "string (optional)"
}
```

**Response** (`201`):
```json
{
  "workspace_id": "string",
  "active_copilot": "commerce",
  "status": "active",
  "created_at": "2026-07-23T10:00:00Z"
}
```

---

### 5. List Copilots

```
GET /copilots
```

**Response** (`200`):
```json
{
  "copilots": [
    {
      "name": "marketing",
      "display_name": "Marketing Copilot",
      "description": "Campaigns, content, and segmentation",
      "enabled": true
    }
  ],
  "active_count": 4
}
```

---

### 6. Switch Copilot

Switch the active copilot for a workspace.

```
POST /workspace/{workspaceId}/switch
```

**Request Body:**
```json
{
  "target_copilot": "sales"
}
```

**Response** (`200`):
```json
{
  "workspace_id": "string",
  "previous_copilot": "marketing",
  "active_copilot": "sales",
  "handoff_status": "completed",
  "context_preserved": true
}
```

---

### 7. Handoff (with context)

Initiate a handoff with full context transfer to the target copilot.

```
POST /workspace/{workspaceId}/handoff
```

**Request Body:**
```json
{
  "target_copilot": "service",
  "reason": "user request",
  "context": {}
}
```

**Response** (`200`):
```json
{
  "workspace_id": "string",
  "target_copilot": "service",
  "handoff_id": "string",
  "status": "completed",
  "context_snapshot": {}
}
```

---

### 8. Get Workspace Context

```
GET /workspace/{workspaceId}/context
```

**Response** (`200`):
```json
{
  "workspace_id": "string",
  "context": {
    "workspace": {},
    "session": {},
    "user": {},
    "custom": {}
  }
}
```

---

### 9. Get Conversation History

```
GET /workspace/{workspaceId}/history
```

**Query Parameters:**
- `limit` (int, default 50): Max messages to return
- `before` (string, optional): Message ID cursor for pagination

**Response** (`200`):
```json
{
  "workspace_id": "string",
  "messages": [
    {
      "message_id": "string",
      "role": "user",
      "content": "string",
      "copilot": "string (optional)",
      "timestamp": "2026-07-23T10:00:00Z"
    }
  ],
  "total": 10,
  "has_more": false
}
```

---

## Error Responses

All errors follow the RFC 9457 Problem Details format:

```json
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "Workspace ws_999 not found",
  "instance": "/api/v1/workspace/ws_999"
}
```

| Status | Title | Description |
|--------|-------|-------------|
| 400 | Bad Request | Invalid request payload |
| 401 | Unauthorized | Missing or invalid authentication |
| 404 | Not Found | Workspace or copilot not found |
| 409 | Conflict | Workspace state conflict (e.g., already switched) |
| 429 | Too Many Requests | Rate limit exceeded |
| 502 | Bad Gateway | Upstream copilot error or collaboration failure |
