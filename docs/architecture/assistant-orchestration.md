# Assistant Orchestration Architecture

**Version:** 1.0.0
**Last Updated:** 2026-07-12
**Module:** ai-service
**Package:** `com.sporekart.ai.assistant`

---

## Overview

The Assistant Orchestrator is the central coordinator for all user-to-copilot interactions. It implements a four-stage pipeline: **User → Intent → Task Plan → Copilot → Response**. Each stage is handled by a dedicated component with clear responsibilities, error handling, and observability.

---

## Orchestration Flow

```
┌──────────┐     ┌──────────────┐     ┌──────────────┐     ┌─────────────────┐     ┌──────────┐
│  User    │────►│  Intent      │────►│  Task         │────►│  Copilot        │────►│ Response │
│  Message │     │  Engine      │     │  Planner      │     │  Orchestrator   │     │          │
└──────────┘     └──────────────┘     └──────────────┘     └─────────────────┘     └──────────┘
     │                 │                    │                       │                    │
     ▼                 ▼                    ▼                       ▼                    ▼
  Validate        Classify             Break into               Route to             Build &
  Sanitize        Resolve              steps                   copilots              format
```

---

## Component Details

### 1. Input Parser

| Property | Value |
|----------|-------|
| **Class** | `InputParser` |
| **Responsibility** | Validate, sanitize, and normalize user input |
| **Input** | Raw user message string |
| **Output** | `ParsedInput` record with sanitized text, session context, metadata |

**Flow:**
```
Raw Message → Length Check (1-4000 chars) → Prompt Injection Scan →
Unicode Normalization → Trim → ParsedInput
```

### 2. Intent Engine

| Property | Value |
|----------|-------|
| **Class** | `IntentResolverImpl` |
| **Responsibility** | Classify user intent, extract entities, resolve target copilot |
| **Input** | `ParsedInput` |
| **Output** | `IntentRecord` with category, confidence, entities, resolved copilot |
| **Strategy** | Keyword-based classification with confidence scoring |

**Flow:**
```
ParsedInput → Tokenize → Keyword Match → Category Classification →
Entity Extraction → Confidence Scoring → IntentRecord
```

**Intent Categories:**

| Category | Example Utterance | Typical Copilot |
|----------|------------------|-----------------|
| INQUIRY | "Show me customer details for ID 123" | Customer, Product |
| COMMAND | "Update the order status to shipped" | Order, Support |
| SEARCH | "Find products in the vegetable category" | Product, Inventory |
| NAVIGATION | "Take me to the training dashboard" | Training, Analytics |
| REPORT | "Generate sales report for last month" | Analytics |
| ALERT | "Notify me when stock is low" | Notification, Inventory |
| TASK | "Create a support ticket for order 456" | Support, Administration |
| CONFIGURATION | "Update notification preferences" | Administration, Notification |

**Entity Extraction:**

| Entity Type | Pattern | Example |
|-------------|---------|---------|
| `PRODUCT_ID` | UUID or alphanumeric | `prod-abc-123` |
| `ORDER_ID` | UUID or alphanumeric | `ORD-789` |
| `CUSTOMER_ID` | UUID or alphanumeric | `CUST-456` |
| `DATE_RANGE` | Date pattern | `last month`, `2026-07-01 to 2026-07-12` |
| `AMOUNT` | Numeric with currency | `$500`, `1000 USD` |
| `STATUS` | Status keyword | `shipped`, `pending`, `completed` |

### 3. Task Planner

| Property | Value |
|----------|-------|
| **Class** | `TaskPlannerImpl` |
| **Responsibility** | Decompose resolved intent into executable task steps |
| **Input** | `IntentRecord` |
| **Output** | `TaskPlan` with ordered `TaskStep` list |
| **Strategy** | Template-based decomposition per intent-copilot combination |

**Flow:**
```
IntentRecord → Lookup Plan Template → Assign Copilot per Step →
Set Dependencies → Prioritize Steps → TaskPlan
```

**Task Plan Structure:**

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
      "copilotType": "CUSTOMER",
      "action": "LOOKUP_CUSTOMER",
      "input": {"customerId": "CUST-456"},
      "status": "COMPLETED"
    },
    {
      "sequence": 2,
      "copilotType": "ORDER",
      "action": "LIST_ORDERS",
      "input": {"customerId": "CUST-456", "limit": 5},
      "status": "PENDING",
      "dependsOn": [1]
    }
  ]
}
```

**Step Actions:**

| Copilot | Actions |
|---------|---------|
| Customer | LOOKUP_CUSTOMER, LIST_CUSTOMERS, UPDATE_PROFILE, GET_SEGMENT |
| Product | SEARCH_PRODUCTS, GET_PRODUCT, LIST_CATEGORIES, GET_PRICING |
| Training | LIST_COURSES, GET_PROGRESS, CHECK_CERTIFICATION |
| Grower | LOOKUP_GROWER, GET_NETWORK_STATUS, CHECK_COMPLIANCE |
| Marketplace | SEARCH_LISTINGS, GET_MARKET_STATS, MANAGE_LISTING |
| ERP | GET_RESOURCES, GET_PLANNING_DATA, UPDATE_CONFIG |
| Inventory | CHECK_STOCK, GET_WAREHOUSE_STATUS, GET_LOW_STOCK |
| Order | LOOKUP_ORDER, TRACK_ORDER, UPDATE_STATUS, GET_HISTORY |
| Analytics | GENERATE_REPORT, GET_KPI, GET_TRENDS |
| Support | LOOKUP_TICKET, GET_FAQ, ESCALATE, RESOLVE |
| Administration | GET_SYSTEM_STATUS, MANAGE_USERS, UPDATE_CONFIG |
| Notification | SEND_NOTIFICATION, GET_HISTORY, UPDATE_PREFERENCES |

### 4. Copilot Orchestrator

| Property | Value |
|----------|-------|
| **Class** | `CopilotOrchestratorImpl` |
| **Responsibility** | Execute task plan by routing steps to appropriate copilots |
| **Input** | `TaskPlan` |
| **Output** | `ExecutionResult` with step results |
| **Execution** | Sequential with parallel support for non-dependent steps |

**Flow:**
```
TaskPlan → For each step (ordered by dependency):
  1. Resolve Copilot by type
  2. Execute Copilot.execute(request)
  3. Collect result
  4. Handle errors (skip / retry / fail)
→ Aggregate results → ExecutionResult
```

**Error Handling Strategy:**

| Error | Action | Response |
|-------|--------|----------|
| Business module timeout | Retry once, then skip | PARTIAL status with warning |
| Business module 4xx | Fail step | ERROR status with detail |
| Business module 5xx | Skip step | PARTIAL status with error |
| Copilot not found | Fail plan | ERROR status |
| Validation failure | Fail step | ERROR status with validation details |

### 5. Response Builder

| Property | Value |
|----------|-------|
| **Class** | `ResponseBuilder` |
| **Responsibility** | Assemble user-friendly response from execution results |
| **Input** | `ExecutionResult`, `IntentRecord` |
| **Output** | `AssistantChatResponse` |

**Flow:**
```
ExecutionResult → Format primary response →
Attach intent info → Add suggestions →
Include metadata (latency, copilots used) →
AssistantChatResponse
```

---

## Security Layer

| Concern | Mechanism |
|---------|-----------|
| Input Sanitization | Strip HTML tags, script tags, SQL injection patterns |
| Prompt Injection Detection | Block known injection patterns (role override, system prompt, delimiter escape) |
| Rate Limiting | 30 requests/min per user, burst of 5 |
| Authentication | JWT Bearer token required on all endpoints |
| Authorization | RBAC: `ASSISTANT_USER` (chat, query) and `ASSISTANT_ADMIN` (config, admin copilot) |
| Session Validation | Validate session ownership matches authenticated user |

---

## Event Publishing

All orchestration events are published to the `assistant-events` Kafka topic (3 partitions, 1 replica).

| Event | Payload | Trigger |
|-------|---------|---------|
| `AssistantMessageReceived` | sessionId, userId, messageLength, timestamp | Input parsed |
| `IntentResolved` | intentId, category, confidence, resolvedCopilot, entities | Intent resolved |
| `IntentAmbiguous` | intentId, utterance, alternatives | Multiple matches |
| `TaskPlanCreated` | planId, intentId, stepCount, priority | Plan created |
| `CopilotExecuted` | planId, stepId, copilotType, action, status, latencyMs | Step executed |
| `CopilotFailed` | planId, stepId, copilotType, errorDetail | Step failed |
| `AssistantResponseDelivered` | sessionId, responseLength, latencyMs, copilotsUsed | Response built |
| `FeedbackSubmitted` | sessionId, intentId, rating, categories | Feedback received |
| `SessionCreated` | sessionId, assistantType, userId | Session created |
| `SessionExpired` | sessionId, durationMinutes | Session expired |

---

## Monitoring Metrics

| Metric | Type | Tags |
|--------|------|------|
| `assistant.input.latency` | Timer | status |
| `assistant.intent.latency` | Timer | category, status |
| `assistant.taskplan.latency` | Timer | stepCount, status |
| `assistant.copilot.latency` | Timer | copilotType, action, status |
| `assistant.response.latency` | Timer | copilotCount, status |
| `assistant.total.latency` | Timer | copilotCount, status |
| `assistant.intent.count` | Counter | category, status |
| `assistant.copilot.executions` | Counter | copilotType, action, status |
| `assistant.copilot.errors` | Counter | copilotType, errorType |
| `assistant.session.count` | Counter | assistantType, status |
| `assistant.feedback.rating` | Counter | rating |
| `assistant.cache.hit` | Counter | namespace |
| `assistant.cache.miss` | Counter | namespace |

---

## Caching Strategy

| Cache Key Pattern | TTL | Strategy | Purpose |
|---|---|---|---|
| `assistant:profile:{type}` | 60 min | Cache-aside | Copilot profile cache |
| `assistant:intent:{id}` | 30 min | Write-through | Intent resolution cache |
| `assistant:session:{id}` | 30 min | Cache-aside | Session data cache |
| `assistant:task:{id}` | 15 min | Write-through | Task plan cache |
| `assistant:context:{sessionId}:{key}` | 10 min | Cache-aside | Session context cache |
