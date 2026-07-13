# Business Assistants Platform

**Module:** ai-service
**Package:** `com.sporekart.ai.assistant`

---

## Overview

The Business Assistants Platform provides 12 domain-specific AI copilots that enable users to interact with SporeKart business modules through natural language. Each copilot is a specialized assistant that delegates to its corresponding business module — copilots contain no business logic, serving as intelligent routing and orchestration layers.

The platform follows a four-stage pipeline: **User Message → Intent Resolution → Task Planning → Copilot Execution → Response**. This pipeline is orchestrated by the `AssistantOrchestrator`, which coordinates the `IntentResolver`, `TaskPlanner`, and `CopilotOrchestrator` components.

---

## Copilot Capabilities Table

| Copilot | Type | Delegates To | Supported Intents | Key Actions |
|---------|------|-------------|-------------------|-------------|
| Customer | `CUSTOMER` | Customer Service | INQUIRY, SEARCH, TASK | Lookup, profile, segmentation |
| Product | `PRODUCT` | Product Service | INQUIRY, SEARCH, NAVIGATION | Search, browse, pricing |
| Training | `TRAINING` | Training Service | INQUIRY, SEARCH, REPORT | Courses, progress, certification |
| Grower | `GROWER` | Grower Service | INQUIRY, SEARCH, TASK | Grower lookup, network, compliance |
| Marketplace | `MARKETPLACE` | Marketplace Service | INQUIRY, SEARCH, COMMAND | Listings, stats, management |
| ERP | `ERP` | ERP Service | INQUIRY, REPORT, CONFIGURATION | Resources, planning, config |
| Inventory | `INVENTORY` | Inventory Service | INQUIRY, SEARCH, ALERT | Stock, warehouse, alerts |
| Order | `ORDER` | Order Service | INQUIRY, SEARCH, TASK | Lookup, tracking, status |
| Analytics | `ANALYTICS` | Analytics Service | REPORT, INQUIRY, SEARCH | Reports, KPIs, trends |
| Support | `SUPPORT` | Support Service | INQUIRY, TASK, SEARCH | Tickets, FAQ, escalation |
| Administration | `ADMINISTRATION` | Admin Service | CONFIGURATION, TASK, COMMAND | System status, users, config |
| Notification | `NOTIFICATION` | Notification Service | ALERT, COMMAND, TASK | Send, history, preferences |

---

## Use Cases Per Copilot

### Customer Copilot
- **Customer Lookup**: "Show me details for customer CUST-456"
- **Search Customers**: "Find customers in the premium segment"
- **Profile Update**: "Update email for customer CUST-456"
- **Segmentation**: "Which segment does customer CUST-456 belong to?"

### Product Copilot
- **Product Search**: "Find organic tomato seeds under $20"
- **Category Browse**: "Show me all products in the vegetable seeds category"
- **Pricing Inquiry**: "What is the price of product PROD-789?"
- **Product Details**: "Tell me about the Cherry Red Tomato variety"

### Training Copilot
- **Course Lookup**: "What training courses are available for growers?"
- **Progress Tracking**: "Show my training progress"
- **Certification Status**: "Am I certified for organic farming?"

### Grower Copilot
- **Grower Lookup**: "Show details for grower GROW-123"
- **Network Status**: "How many growers are active in Maharashtra?"
- **Compliance Check**: "Is grower GROW-123 compliant with organic standards?"

### Marketplace Copilot
- **Listing Search**: "Find listings for organic fertilizers"
- **Market Stats**: "What are the top-selling products this week?"
- **Listing Management**: "Create a new listing for organic tomatoes"

### ERP Copilot
- **Resource Lookup**: "Show current resource allocation"
- **Planning Data**: "What is the production plan for next quarter?"
- **Configuration**: "Update the fiscal year settings"

### Inventory Copilot
- **Stock Check**: "What is the current stock level for product PROD-789?"
- **Warehouse Status**: "Show warehouse capacity in Mumbai"
- **Low Stock Alert**: "Which products are below reorder level?"

### Order Copilot
- **Order Lookup**: "Find order ORD-789"
- **Tracking**: "Where is my order ORD-789?"
- **Status Update**: "Update order ORD-789 to shipped"

### Analytics Copilot
- **Report Generation**: "Generate sales report for Q2 2026"
- **KPI Query**: "What is the monthly recurring revenue?"
- **Trend Analysis**: "Show customer acquisition trends for last 6 months"

### Support Copilot
- **Ticket Lookup**: "Show status of ticket TKT-456"
- **FAQ Resolution**: "How do I reset my password?"
- **Escalation**: "Escalate ticket TKT-456 to senior support"

### Administration Copilot
- **System Status**: "Is the system healthy?"
- **User Management**: "Create a new user with support role"
- **Configuration**: "Update the session timeout to 60 minutes"

### Notification Copilot
- **Send Notification**: "Send a reminder to all growers about the training session"
- **History**: "Show notifications sent this week"
- **Preferences**: "Update my notification preferences"

---

## Modules

### Intent Engine
Classifies user utterances into intent categories (INQUIRY, COMMAND, SEARCH, NAVIGATION, REPORT, ALERT, TASK, CONFIGURATION). Uses keyword-based classification with confidence scoring. Extracts entities such as product IDs, order references, customer IDs, date ranges, and amounts.

### Task Planner
Decomposes resolved intents into executable task plans. Each plan contains ordered steps with assigned copilots, actions, input parameters, and dependencies. Supports single-step and multi-step plans with sequential execution.

### Copilot Orchestrator
Routes task plan steps to the correct copilot implementation. Handles execution, error recovery (skip, retry, fail), and result aggregation across multiple steps.

### 12 Domain Copilots
Each copilot is a thin delegation layer that validates input, calls the corresponding business module, transforms the response, and returns a standardized result. See `docs/architecture/domain-copilot-architecture.md` for detailed definitions.

### REST API
9 endpoints under `/api/v1/assistants/*` for chat, intent resolution, task planning, copilot discovery, session management, and feedback. See `docs/api/assistant-api.md` for full reference.

### Web UI
React + Vite + TypeScript dashboard displaying copilot list, session view, and chat interface.

---

## Limitations and Constraints

1. **No Business Logic** — Copilots do not implement domain-specific business rules. All business logic must be implemented in the corresponding business module. Copilots only validate, delegate, transform, and respond.

2. **Keyword-Based Intent Resolution** — The Intent Engine uses keyword-based classification, not NLU/LLM. Intent resolution accuracy depends on keyword coverage. Ambiguous intents return `AMBIGUOUS` status with alternatives.

3. **Stub Implementation** — The initial release contains copilot stubs that return simulated responses. Real business module integration requires implementing the service client interfaces and enabling the corresponding feature flags.

4. **No Provider SDK Calls** — Copilots do not call AI providers directly. AI capabilities (if needed) must be accessed through the AI Gateway.

5. **Rate Limited** — 30 requests per minute per user. Burst limit of 5 requests. Exceeding limits returns 429 status.

6. **Session Expiry** — Assistant sessions expire after 30 minutes of inactivity. Session context is cleared on expiry.

7. **Copilot Availability** — Not all copilots support all intent categories. Unsupported combinations return a descriptive error with suggestions for alternative copilots.

8. **Multi-Step Plans** — Task plans execute sequentially by default. Parallel execution for non-dependent steps is supported but not guaranteed for the initial release.

9. **No Streaming** — The assistant API does not support SSE streaming in the initial release. All responses are synchronous.

10. **Language Support** — The assistant platform processes English utterances only. Multi-language support is not included in this release.

---

## Configuration Reference

```yaml
sporekart:
  assistant:
    enabled: true
    caching: true
    audit: true
    intent:
      min-confidence: 0.6
      max-alternatives: 3
    task:
      max-steps: 10
      default-priority: MEDIUM
      retry-count: 1
    rate-limit:
      requests-per-min: 30
      burst: 5
      window-ms: 60000
    session:
      ttl-minutes: 30
      max-context-entries: 50
    cache-ttl:
      profile: 3600
      intent: 1800
      session: 1800
      task: 900
      context: 600
```

---

## Redis Caching Strategy

| Namespace | Key Pattern | TTL | Strategy |
|-----------|-------------|-----|----------|
| Profiles | `assistant:profile:{type}` | 60 min | Cache-aside |
| Intents | `assistant:intent:{id}` | 30 min | Write-through |
| Sessions | `assistant:session:{id}` | 30 min | Cache-aside |
| Tasks | `assistant:task:{id}` | 15 min | Write-through |
| Contexts | `assistant:context:{sessionId}:{key}` | 10 min | Cache-aside |

---

## Kafka Events Reference

| Event | Key | Payload Summary |
|-------|-----|-----------------|
| AssistantMessageReceived | sessionId | userId, messageLength, timestamp |
| IntentResolved | intentId | category, confidence, resolvedCopilot |
| IntentAmbiguous | intentId | utterance, alternatives |
| TaskPlanCreated | planId | stepCount, priority |
| CopilotExecuted | stepId | copilotType, action, status, latencyMs |
| CopilotFailed | stepId | copilotType, errorDetail |
| AssistantResponseDelivered | sessionId | responseLength, latencyMs |
| FeedbackSubmitted | sessionId | rating, categories |
| SessionCreated | sessionId | assistantType, userId |
| SessionExpired | sessionId | durationMinutes |

All events published to `assistant-events` topic (3 partitions, 1 replica).

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
