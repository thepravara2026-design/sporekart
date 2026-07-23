# Customer Copilot API Reference

**Version:** 0.2.0
**Base Path:** `/api/v1/copilot/customer`
**Service Port:** 8099
**Content-Type:** `application/json`

---

## Authentication

All endpoints (except health check and OpenAPI docs) require a Bearer JWT token.

```
Authorization: Bearer <jwt-token>
```

JWT tokens are issued by the Identity Service. The token must contain:
- `sub` — user ID
- `roles` — user roles
- `exp` — expiration timestamp

---

## Rate Limiting

| Tier | Limit | Burst |
|---|---|---|
| Standard | 60 requests/minute | 100 |
| Premium | 300 requests/minute | 500 |
| Internal | 1000 requests/minute | 2000 |

Rate limit headers are returned in all responses:
```
X-RateLimit-Limit: 60
X-RateLimit-Remaining: 55
X-RateLimit-Reset: 1689523200
```

---

## Endpoints

### 1. Chat

**`POST /api/v1/copilot/customer/chat`**

Process a user message and return a copilot response synchronously.

#### Request Body

```json
{
  "message": "show me oyster mushrooms",
  "sessionId": "sess_abc123",
  "pageUrl": "https://sporekart.com/products",
  "pageTitle": "Products",
  "section": "mushroom-spawn",
  "entityType": "product",
  "entityId": "prod_456"
}
```

#### Request Schema

| Field | Type | Required | Description |
|---|---|---|---|
| `message` | string | Yes | The user's message |
| `sessionId` | string | No | Existing session ID (omit for new session) |
| `pageUrl` | string | No | Current page URL for context |
| `pageTitle` | string | No | Current page title |
| `section` | string | No | Current section/category |
| `entityType` | string | No | Entity type (product, order, course) |
| `entityId` | string | No | Entity identifier |

#### Response Body

```json
{
  "sessionId": "sess_abc123",
  "message": "I found 12 oyster mushroom products...",
  "suggestions": [
    {
      "label": "Filter by price range",
      "action": "filter",
      "payload": { "field": "price", "type": "range" }
    },
    {
      "label": "Show growing kits",
      "action": "search",
      "payload": { "query": "oyster mushroom grow kit" }
    }
  ],
  "context": {
    "sessionId": "sess_abc123",
    "profile": { "userName": "Rajesh", "journeyStage": "browsing" },
    "cartCount": 0
  },
  "streaming": false
}
```

#### Response Schema

| Field | Type | Description |
|---|---|---|
| `sessionId` | string | Session identifier |
| `message` | string | Copilot response message |
| `suggestions` | array[Suggestion] | Follow-up action suggestions |
| `context` | object | Session context metadata |
| `streaming` | boolean | Whether response was streamed |

---

### 2. Streaming Chat

**`POST /api/v1/copilot/customer/stream`**

Process a user message and return a streaming response via Server-Sent Events (SSE).

#### Request Body

Same as `POST /chat`.

#### Response (SSE)

```
event: context
data: {"sessionId":"sess_abc123","pageContext":"/products","userInfo":{...},"cartInfo":{...}}

event: processing
data: {"status":"analyzing"}

event: token
data: {"token":"I"}

event: token
data: {"token":" found"}

event: token
data: {"token":" 12"}

event: token
data: {"token":" oyster"}

event: token
data: {"token":" mushroom"}

event: token
data: {"token":" products"}

event: suggestions
data: {"suggestions":[{"label":"Filter by price","action":"filter","payload":{...}}]}

event: complete
data: {"sessionId":"sess_abc123"}
```

#### SSE Event Types

| Event | Data | Description |
|---|---|---|
| `context` | ContextResponse | Assembled context for the session |
| `processing` | `{ status: string }` | Processing status updates |
| `token` | `{ token: string }` | Individual response token |
| `suggestions` | `{ suggestions: Suggestion[] }` | Follow-up suggestions |
| `complete` | `{ sessionId: string }` | Response complete |
| `error` | `{ error: string }` | Error occurred |

---

### 3. Conversation History

**`GET /api/v1/copilot/customer/history?sessionId={sessionId}`**

Retrieve the conversation history for a session.

#### Query Parameters

| Parameter | Type | Required | Description |
|---|---|---|---|
| `sessionId` | string | Yes | Session identifier |

#### Response Body

```json
{
  "sessionId": "sess_abc123",
  "messages": [
    {
      "id": "msg_001",
      "role": "user",
      "content": "show me oyster mushrooms",
      "metadata": { "pageUrl": "/products" },
      "timestamp": "2026-07-23T10:30:00+05:30"
    },
    {
      "id": "msg_002",
      "role": "assistant",
      "content": "I found 12 oyster mushroom products...",
      "metadata": { "suggestions": [...] },
      "timestamp": "2026-07-23T10:30:01+05:30"
    }
  ]
}
```

#### Response Schema

| Field | Type | Description |
|---|---|---|
| `sessionId` | string | Session identifier |
| `messages` | array[ConversationMessage] | Ordered list of messages |

---

### 4. Recommendations

**`POST /api/v1/copilot/customer/recommend`**

Get product recommendations for a customer.

#### Request Body

```json
{
  "customerId": "cust_789",
  "category": "mushroom-spawn",
  "context": "product_page",
  "limit": 10,
  "excludeProductIds": ["prod_123", "prod_456"]
}
```

#### Request Schema

| Field | Type | Required | Description |
|---|---|---|---|
| `customerId` | string | Yes | Customer identifier |
| `category` | string | No | Category filter |
| `context` | string | No | Recommendation context |
| `limit` | integer | No | Max results (default: 10) |
| `excludeProductIds` | array[string] | No | Products to exclude |

#### Response Body

```json
{
  "recommendations": [
    {
      "product": {
        "id": "prod_789",
        "name": "Oyster Mushroom Grow Kit",
        "description": "Complete kit for beginners",
        "category": "growing-kits",
        "price": 1299.00,
        "currency": "INR",
        "imageUrl": "https://cdn.sporekart.com/kits/oyster-kit.jpg",
        "stockLevel": 45,
        "isAvailable": true,
        "rating": 4.5,
        "tags": ["beginner", "oyster", "kit"]
      },
      "score": 92,
      "reason": "Great for beginners and perfect for the current growing season",
      "recommendationType": "PERSONALIZED"
    }
  ],
  "recommendationType": "PERSONALIZED",
  "explanation": "Recommended based on your profile and seasonal availability"
}
```

#### Response Schema

| Field | Type | Description |
|---|---|---|
| `recommendations` | array[ProductRecommendation] | Scored product recommendations |
| `recommendationType` | string | Strategy type that dominated |
| `explanation` | string | Human-readable explanation |

---

### 5. Context Assembly

**`POST /api/v1/copilot/customer/context`**

Assemble and return the full context for a user session.

#### Request Body

```json
{
  "sessionId": "sess_abc123",
  "customerId": "cust_789",
  "pageUrl": "https://sporekart.com/products/oyster-spawn",
  "pageTitle": "Oyster Mushroom Spawn"
}
```

#### Request Schema

| Field | Type | Required | Description |
|---|---|---|---|
| `sessionId` | string | Yes | Session identifier |
| `customerId` | string | Yes | Customer identifier |
| `pageUrl` | string | No | Current page URL |
| `pageTitle` | string | No | Current page title |

#### Response Body

```json
{
  "sessionId": "sess_abc123",
  "pageContext": "/products/oyster-spawn",
  "userInfo": {
    "userId": "cust_789",
    "userName": "Rajesh Kumar",
    "email": "rajesh@example.com",
    "preferredLanguage": "en",
    "journeyStage": "browsing"
  },
  "cartInfo": {
    "items": [
      {
        "productId": "prod_456",
        "productName": "Oyster Spawn",
        "quantity": 2,
        "unitPrice": 299.0,
        "totalPrice": 598.0
      }
    ],
    "itemCount": 1,
    "totalPrice": 598.0
  },
  "recentActivity": {
    "recentOrders": [],
    "viewedProducts": ["prod_123", "prod_456"],
    "conversationLength": 5
  }
}
```

#### Response Schema

| Field | Type | Description |
|---|---|---|
| `sessionId` | string | Session identifier |
| `pageContext` | string | Current page URL |
| `userInfo` | object | User profile and journey info |
| `cartInfo` | object | Cart contents and totals |
| `recentActivity` | object | Recent orders and viewed products |

---

### 6. Product Search

**`POST /api/v1/copilot/customer/products`**

Search products in the catalog.

#### Request Body

```json
{
  "query": "oyster mushroom",
  "category": "spawn",
  "minPrice": 100,
  "maxPrice": 5000,
  "sortBy": "relevance",
  "page": 0,
  "size": 20
}
```

#### Request Schema

| Field | Type | Required | Description |
|---|---|---|---|
| `query` | string | No | Search query |
| `category` | string | No | Category filter |
| `minPrice` | number | No | Minimum price |
| `maxPrice` | number | No | Maximum price |
| `sortBy` | string | No | Sort field (relevance, price_asc, price_desc) |
| `page` | integer | No | Page number (default: 0) |
| `size` | integer | No | Page size (default: 20) |

#### Response Body

```json
{
  "products": [
    {
      "id": "prod_789",
      "name": "Oyster Mushroom Spawn (100g)",
      "description": "High-quality spawn for oyster mushroom cultivation",
      "category": "mushroom-spawn",
      "price": 299.0,
      "currency": "INR",
      "imageUrl": "https://cdn.sporekart.com/spawn/oyster-100g.jpg",
      "stockLevel": 120,
      "isAvailable": true,
      "rating": 4.7,
      "tags": ["oyster", "spawn", "beginner"]
    }
  ],
  "totalResults": 15,
  "page": 0,
  "size": 20
}
```

#### Response Schema

| Field | Type | Description |
|---|---|---|
| `products` | array[ProductItem] | Product search results |
| `totalResults` | integer | Total matching results |
| `page` | integer | Current page number |
| `size` | integer | Page size |

---

### 7. Health Check

**`GET /api/v1/copilot/customer/health`**

#### Response Body

```json
{
  "status": "UP",
  "service": "customer-copilot-service",
  "version": "0.2.0",
  "uptime": 3600,
  "dependencies": {
    "catalog-service": { "status": "UP", "latencyMs": 45 },
    "order-service": { "status": "UP", "latencyMs": 32 },
    "knowledge-platform": { "status": "DEGRADED", "latencyMs": 0 },
    "training-service": { "status": "UP", "latencyMs": 28 }
  }
}
```

---

### 8. Feedback

**`POST /api/v1/copilot/customer/feedback`**

Submit feedback on a copilot response.

#### Request Body

```json
{
  "sessionId": "sess_abc123",
  "messageId": "msg_002",
  "rating": "helpful",
  "feedback": "Great recommendations, very accurate",
  "metadata": {
    "intent": "product_search",
    "capability": "Product Search & Discovery"
  }
}
```

#### Request Schema

| Field | Type | Required | Description |
|---|---|---|---|
| `sessionId` | string | Yes | Session identifier |
| `messageId` | string | No | Message identifier |
| `rating` | string | Yes | Rating (helpful, neutral, unhelpful) |
| `feedback` | string | No | Free-text feedback |
| `metadata` | object | No | Additional context |

---

## Error Codes

| HTTP Status | Code | Description |
|---|---|---|
| 400 | `BAD_REQUEST` | Invalid request payload |
| 400 | `VALIDATION_ERROR` | Missing required fields |
| 401 | `UNAUTHORIZED` | Missing or invalid JWT |
| 401 | `TOKEN_EXPIRED` | JWT has expired |
| 403 | `FORBIDDEN` | Insufficient permissions |
| 404 | `SESSION_NOT_FOUND` | Session ID does not exist |
| 404 | `PRODUCT_NOT_FOUND` | Product not found in catalog |
| 404 | `ORDER_NOT_FOUND` | Order not found |
| 429 | `RATE_LIMIT_EXCEEDED` | Too many requests |
| 500 | `INTERNAL_ERROR` | Unexpected server error |
| 502 | `DOWNSTREAM_TIMEOUT` | Backend service timeout |
| 503 | `SERVICE_UNAVAILABLE` | Service temporarily unavailable |

### Error Response Format

```json
{
  "type": "https://api.sporekart.com/errors/bad-request",
  "title": "Bad Request",
  "status": 400,
  "detail": "message must not be blank",
  "instance": "/api/v1/copilot/customer/chat",
  "timestamp": "2026-07-23T10:30:00+05:30"
}
```

---

## cURL Examples

### Chat
```bash
curl -X POST https://api.sporekart.com/api/v1/copilot/customer/chat \
  -H "Authorization: Bearer <jwt>" \
  -H "Content-Type: application/json" \
  -d '{
    "message": "show me oyster mushrooms",
    "sessionId": "sess_abc123",
    "pageUrl": "https://sporekart.com/products"
  }'
```

### Streaming Chat
```bash
curl -X POST https://api.sporekart.com/api/v1/copilot/customer/stream \
  -H "Authorization: Bearer <jwt>" \
  -H "Content-Type: application/json" \
  -H "Accept: text/event-stream" \
  -d '{
    "message": "what should I grow?",
    "sessionId": "sess_abc123"
  }'
```

### Conversation History
```bash
curl -X GET "https://api.sporekart.com/api/v1/copilot/customer/history?sessionId=sess_abc123" \
  -H "Authorization: Bearer <jwt>"
```

### Recommendations
```bash
curl -X POST https://api.sporekart.com/api/v1/copilot/customer/recommend \
  -H "Authorization: Bearer <jwt>" \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "cust_789",
    "category": "mushroom-spawn",
    "limit": 5
  }'
```

### Context Assembly
```bash
curl -X POST https://api.sporekart.com/api/v1/copilot/customer/context \
  -H "Authorization: Bearer <jwt>" \
  -H "Content-Type: application/json" \
  -d '{
    "sessionId": "sess_abc123",
    "customerId": "cust_789",
    "pageUrl": "https://sporekart.com/products/oyster-spawn"
  }'
```

### Product Search
```bash
curl -X POST https://api.sporekart.com/api/v1/copilot/customer/products \
  -H "Authorization: Bearer <jwt>" \
  -H "Content-Type: application/json" \
  -d '{
    "query": "oyster mushroom",
    "category": "spawn",
    "page": 0,
    "size": 10
  }'
```

### Health Check
```bash
curl -X GET https://api.sporekart.com/api/v1/copilot/customer/health
```

### Feedback
```bash
curl -X POST https://api.sporekart.com/api/v1/copilot/customer/feedback \
  -H "Authorization: Bearer <jwt>" \
  -H "Content-Type: application/json" \
  -d '{
    "sessionId": "sess_abc123",
    "messageId": "msg_002",
    "rating": "helpful",
    "feedback": "Accurate recommendations"
  }'
```

### Actuator Health
```bash
curl -X GET http://localhost:8099/actuator/health
```
