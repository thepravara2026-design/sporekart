# Assistant API Reference

**Base URL:** `/api/v1/assistants`
**Module:** ai-service

---

## Authentication

All endpoints require authentication via Bearer JWT token. Include `Authorization: Bearer <token>` header.

## Rate Limiting

| Operation | Limit | Window |
|-----------|-------|--------|
| All operations | 30 req/min | 60 seconds |
| Burst | 5 req | 1 second |

---

## Endpoints

### 1. Chat with Assistant

`POST /api/v1/assistants/chat`

Send a message to the assistant for full pipeline processing (intent resolution → task planning → copilot execution → response).

**Request Body:**
```json
{
  "sessionId": "uuid",
  "message": "Show me details for customer CUST-456 and their recent orders",
  "context": {
    "assistantType": "CUSTOMER"
  }
}
```

**Response (200):**
```json
{
  "sessionId": "uuid",
  "intent": {
    "id": "uuid",
    "category": "INQUIRY",
    "confidence": 0.92,
    "entities": {
      "customerId": "CUST-456"
    },
    "resolvedCopilot": "CUSTOMER"
  },
  "taskPlan": {
    "id": "uuid",
    "status": "COMPLETED",
    "steps": [
      {
        "sequence": 1,
        "copilot": "CUSTOMER",
        "action": "LOOKUP_CUSTOMER",
        "status": "COMPLETED"
      },
      {
        "sequence": 2,
        "copilot": "ORDER",
        "action": "LIST_ORDERS",
        "status": "COMPLETED"
      }
    ]
  },
  "response": {
    "message": "Customer CUST-456 (Premium Seeds Pvt Ltd) found. They have 12 recent orders, including 3 pending shipments.",
    "data": {
      "customer": { "id": "CUST-456", "name": "Premium Seeds Pvt Ltd", "segment": "PREMIUM" },
      "recentOrders": [
        { "id": "ORD-789", "status": "SHIPPED", "total": 15000 },
        { "id": "ORD-790", "status": "PENDING", "total": 8500 }
      ]
    },
    "suggestions": [
      "View full order history",
      "Update customer contact details",
      "Check payment status"
    ]
  },
  "metadata": {
    "totalLatencyMs": 320,
    "copilotsUsed": ["CUSTOMER", "ORDER"],
    "intentLatencyMs": 45,
    "taskLatencyMs": 15,
    "executionLatencyMs": 260
  }
}
```

**Status Codes:**

| Code | Description |
|------|-------------|
| 200 | Success — response delivered |
| 400 | Validation error — message too long or empty |
| 401 | Unauthorized — missing or invalid token |
| 429 | Rate limit exceeded |
| 500 | Internal error |

---

### 2. Resolve Intent

`POST /api/v1/assistants/intent/resolve`

Resolve intent from a user utterance without executing a task plan.

**Request Body:**
```json
{
  "sessionId": "uuid",
  "utterance": "Find products in the vegetable seeds category under $30"
}
```

**Response (200):**
```json
{
  "id": "uuid",
  "sessionId": "uuid",
  "category": "SEARCH",
  "confidence": 0.88,
  "entities": {
    "category": "vegetable seeds",
    "maxPrice": 30,
    "currency": "USD"
  },
  "resolvedCopilot": "PRODUCT",
  "status": "RESOLVED",
  "alternatives": [
    { "copilot": "INVENTORY", "confidence": 0.45 }
  ],
  "createdAt": "2026-07-12T10:00:00+05:30"
}
```

**Status Codes:**

| Code | Description |
|------|-------------|
| 200 | Success |
| 400 | Validation error |
| 401 | Unauthorized |

---

### 3. Create Task Plan

`POST /api/v1/assistants/task/plan`

Create a task plan for a resolved intent.

**Request Body:**
```json
{
  "intentId": "uuid",
  "priority": "MEDIUM"
}
```

**Response (201):**
```json
{
  "id": "uuid",
  "intentId": "uuid",
  "sessionId": "uuid",
  "status": "PENDING",
  "priority": "MEDIUM",
  "steps": [
    {
      "sequence": 1,
      "copilotType": "PRODUCT",
      "action": "SEARCH_PRODUCTS",
      "input": {
        "category": "vegetable seeds",
        "maxPrice": 30
      },
      "status": "PENDING"
    }
  ],
  "createdAt": "2026-07-12T10:00:00+05:30"
}
```

**Status Codes:**

| Code | Description |
|------|-------------|
| 201 | Created |
| 400 | Validation error — invalid intent ID |
| 404 | Intent not found |
| 401 | Unauthorized |

---

### 4. Get Task Plan

`GET /api/v1/assistants/task/{taskId}`

Get the status and details of a task plan.

**Response (200):**
```json
{
  "id": "uuid",
  "intentId": "uuid",
  "sessionId": "uuid",
  "status": "IN_PROGRESS",
  "priority": "MEDIUM",
  "steps": [
    {
      "sequence": 1,
      "copilotType": "PRODUCT",
      "action": "SEARCH_PRODUCTS",
      "input": { "category": "vegetable seeds", "maxPrice": 30 },
      "output": { "results": 24, "products": [] },
      "status": "COMPLETED",
      "startedAt": "2026-07-12T10:00:00+05:30",
      "completedAt": "2026-07-12T10:00:01+05:30",
      "latencyMs": 120
    }
  ],
  "createdAt": "2026-07-12T10:00:00+05:30",
  "completedAt": null
}
```

**Status Codes:**

| Code | Description |
|------|-------------|
| 200 | Success |
| 404 | Task plan not found |
| 401 | Unauthorized |

---

### 5. List Copilots

`GET /api/v1/assistants/copilots?enabled=true`

List available copilots with optional filter.

**Response (200):**
```json
{
  "copilots": [
    {
      "type": "CUSTOMER",
      "name": "Customer Copilot",
      "description": "AI assistant for customer management",
      "enabled": true,
      "capabilities": ["LOOKUP_CUSTOMER", "LIST_CUSTOMERS", "UPDATE_PROFILE", "GET_SEGMENT"],
      "supportedIntents": ["INQUIRY", "SEARCH", "TASK"]
    },
    {
      "type": "PRODUCT",
      "name": "Product Copilot",
      "description": "AI assistant for product catalog and search",
      "enabled": true,
      "capabilities": ["SEARCH_PRODUCTS", "GET_PRODUCT", "LIST_CATEGORIES", "GET_PRICING"],
      "supportedIntents": ["INQUIRY", "SEARCH", "NAVIGATION"]
    }
  ],
  "total": 12,
  "enabled": 10
}
```

**Status Codes:**

| Code | Description |
|------|-------------|
| 200 | Success |
| 401 | Unauthorized |

---

### 6. Get Copilot Details

`GET /api/v1/assistants/copilots/{type}`

Get details for a specific copilot by type.

**Response (200):**
```json
{
  "type": "CUSTOMER",
  "name": "Customer Copilot",
  "description": "AI assistant for customer management",
  "enabled": true,
  "capabilities": ["LOOKUP_CUSTOMER", "LIST_CUSTOMERS", "UPDATE_PROFILE", "GET_SEGMENT"],
  "supportedIntents": ["INQUIRY", "SEARCH", "TASK"],
  "configuration": {
    "timeoutMs": 5000,
    "retryCount": 1,
    "cacheEnabled": true
  },
  "metrics": {
    "totalExecutions": 1450,
    "successRate": 0.97,
    "avgLatencyMs": 185
  }
}
```

**Status Codes:**

| Code | Description |
|------|-------------|
| 200 | Success |
| 404 | Copilot type not found |
| 401 | Unauthorized |

---

### 7. Create Session

`POST /api/v1/assistants/session`

Create or resume an assistant session.

**Request Body:**
```json
{
  "assistantType": "CUSTOMER",
  "metadata": {
    "source": "web-ui",
    "referrer": "/dashboard"
  }
}
```

**Response (201):**
```json
{
  "id": "uuid",
  "userId": "user-123",
  "assistantType": "CUSTOMER",
  "status": "ACTIVE",
  "context": {},
  "createdAt": "2026-07-12T10:00:00+05:30",
  "expiresAt": "2026-07-12T10:30:00+05:30"
}
```

**Status Codes:**

| Code | Description |
|------|-------------|
| 201 | Created |
| 400 | Validation error |
| 401 | Unauthorized |

---

### 8. Get Session

`GET /api/v1/assistants/session/{sessionId}`

Get session status, context, and message history.

**Response (200):**
```json
{
  "id": "uuid",
  "userId": "user-123",
  "assistantType": "CUSTOMER",
  "status": "ACTIVE",
  "context": {
    "recentCustomerId": "CUST-456",
    "lastIntent": "INQUIRY"
  },
  "messages": [
    {
      "role": "USER",
      "content": "Show me details for customer CUST-456",
      "createdAt": "2026-07-12T10:00:00+05:30"
    },
    {
      "role": "ASSISTANT",
      "content": "Customer CUST-456 (Premium Seeds Pvt Ltd) found.",
      "createdAt": "2026-07-12T10:00:01+05:30"
    }
  ],
  "createdAt": "2026-07-12T09:55:00+05:30",
  "expiresAt": "2026-07-12T10:25:00+05:30"
}
```

**Status Codes:**

| Code | Description |
|------|-------------|
| 200 | Success |
| 404 | Session not found |
| 401 | Unauthorized |

---

### 9. Submit Feedback

`POST /api/v1/assistants/feedback`

Submit user feedback on an assistant response.

**Request Body:**
```json
{
  "sessionId": "uuid",
  "intentId": "uuid",
  "rating": 4,
  "comment": "The response was accurate but could include order dates",
  "categories": ["ACCURATE", "PARTIALLY_HELPFUL"]
}
```

**Response (201):**
```json
{
  "id": "uuid",
  "sessionId": "uuid",
  "rating": 4,
  "acknowledged": true,
  "createdAt": "2026-07-12T10:00:00+05:30"
}
```

**Status Codes:**

| Code | Description |
|------|-------------|
| 201 | Created |
| 400 | Validation error — rating must be 1-5 |
| 401 | Unauthorized |
| 404 | Session or intent not found |

---

## Error Responses

All errors follow RFC 9457 Problem Details format:

| Status | Type | Description |
|--------|------|-------------|
| 400 | `validation-error` | Invalid input parameters |
| 401 | `unauthorized` | Missing or invalid authentication |
| 403 | `forbidden` | Insufficient permissions |
| 404 | `not-found` | Resource not found |
| 429 | `rate-limit-exceeded` | Rate limit exceeded |
| 500 | `internal-error` | Internal server error |

**Error Response Example (400):**
```json
{
  "type": "validation-error",
  "title": "Validation Error",
  "status": 400,
  "detail": "Message must be between 1 and 4000 characters",
  "instance": "/api/v1/assistants/chat",
  "correlationId": "uuid",
  "timestamp": "2026-07-12T10:00:00+05:30"
}
```

---

## OpenAPI Annotations Reference

| Endpoint | `@Operation` Summary | `@ApiResponse` Codes |
|----------|---------------------|---------------------|
| `POST /chat` | "Process a chat message through the assistant pipeline" | 200, 400, 401, 429, 500 |
| `POST /intent/resolve` | "Resolve intent from user utterance" | 200, 400, 401 |
| `POST /task/plan` | "Create a task plan from resolved intent" | 201, 400, 404, 401 |
| `GET /task/{taskId}` | "Get task plan status and details" | 200, 404, 401 |
| `GET /copilots` | "List available copilots" | 200, 401 |
| `GET /copilots/{type}` | "Get copilot details by type" | 200, 404, 401 |
| `POST /session` | "Create or resume assistant session" | 201, 400, 401 |
| `GET /session/{sessionId}` | "Get session with history" | 200, 404, 401 |
| `POST /feedback` | "Submit feedback on assistant response" | 201, 400, 401, 404 |

All endpoints use `@Tag(name = "assistant")` for OpenAPI grouping.
