# Sprint 17 — Part 10: Enterprise AI Business Assistants & Domain Copilots

**Date:** 2026-07-12
**Module:** ai-service
**Lead:** Enterprise AI Platform Engineering Team

---

## Objective

Build the orchestration layer for 12 domain copilots — intent resolution, task planning, copilot orchestration, assistant API with 9 REST endpoints, Flyway V19 migration (8 tables), Redis caching, Kafka events, prompt injection protection, rate limiting, Micrometer monitoring, and comprehensive tests. No business logic enforcement — copilots remain domain-agnostic stubs that delegate to existing business modules. No UI beyond the Web UI dashboard shell.

Future Android/iOS must consume same APIs without backend redesign.

---

## Deliverables

### Assistant Modules

| Module | Package | Purpose |
|--------|---------|---------|
| assistant-core | `com.sporekart.ai.assistant.domain` | Domain records, enums |
| assistant-api | `com.sporekart.ai.assistant.api` | Port interfaces (IntentResolver, TaskPlanner, CopilotOrchestrator, AssistantOrchestrator) |
| assistant-application | `com.sporekart.ai.assistant.application` | Application services |
| assistant-infrastructure | `com.sporekart.ai.assistant.infrastructure` | JPA, Redis, Kafka, monitoring |
| assistant-interfaces | `com.sporekart.ai.assistant.interfaces.rest` | REST controller + DTOs |

---

### Domain Model

| Record/Enum | Fields | Purpose |
|-------------|--------|---------|
| `AssistantType` | CUSTOMER, PRODUCT, TRAINING, GROWER, MARKETPLACE, ERP, INVENTORY, ORDER, ANALYTICS, SUPPORT, ADMINISTRATION, NOTIFICATION | 12 domain copilot types |
| `IntentCategory` | INQUIRY, COMMAND, SEARCH, NAVIGATION, REPORT, ALERT, TASK, CONFIGURATION | Intent classification categories |
| `IntentStatus` | PENDING, RESOLVED, AMBIGUOUS, UNKNOWN | Intent resolution status |
| `TaskStatus` | PENDING, IN_PROGRESS, COMPLETED, FAILED, ROLLED_BACK | Task execution status |
| `TaskPriority` | LOW, MEDIUM, HIGH, CRITICAL | Task execution priority |
| `SessionStatus` | ACTIVE, IDLE, CLOSED, EXPIRED | Assistant session status |
| `CopilotProfile` | id, assistantType, name, description, enabled, capabilities, config, createdBy, createdAt, updatedAt | Copilot definition profile |
| `IntentRecord` | id, sessionId, userId, utterance, category, confidence, entities, status, resolvedType, createdAt | User intent record |
| `TaskPlan` | id, intentId, sessionId, steps, status, priority, assignedCopilot, createdAt, completedAt | Task execution plan |
| `TaskStep` | id, planId, sequence, copilotType, action, input, output, status, startedAt, completedAt | Individual task step |
| `AssistantSession` | id, userId, assistantType, status, context, metadata, createdAt, updatedAt, expiredAt | Session tracking record |
| `AssistantFeedback` | id, sessionId, intentId, rating, comment, categories, createdAt | User feedback record |
| `AssistantAuditLog` | id, sessionId, userId, action, resource, detail, ipAddress, userAgent, createdAt | Audit trail entry |
| `AssistantContext` | id, sessionId, key, value, type, ttl, createdAt, expiresAt | Session context storage |
| `ConversationMessage` | id, sessionId, role, content, messageType, metadata, createdAt | Message within assistant session |

---

### 12 Domain Copilots

| # | Copilot | Domain | Delegates To |
|---|---------|--------|-------------|
| 1 | Customer Copilot | Customer management | Customer Service |
| 2 | Product Copilot | Product catalog & search | Product Service |
| 3 | Training Copilot | Training content & progress | Training Service |
| 4 | Grower Copilot | Grower network & operations | Grower Service |
| 5 | Marketplace Copilot | Marketplace listings & orders | Marketplace Service |
| 6 | ERP Copilot | Enterprise resource planning | ERP Service |
| 7 | Inventory Copilot | Stock & warehouse management | Inventory Service |
| 8 | Order Copilot | Order processing & tracking | Order Service |
| 9 | Analytics Copilot | Business intelligence & reports | Analytics Service |
| 10 | Support Copilot | Customer support & tickets | Support Service |
| 11 | Administration Copilot | System administration | Admin Service |
| 12 | Notification Copilot | Notifications & alerts | Notification Service |

---

### Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                              Client Applications                              │
│          Web UI │ Android (future) │ iOS (future) │ API Clients               │
└───────────────────────────────┬─────────────────────────────────────────────┘
                                │ REST / SSE
┌───────────────────────────────▼─────────────────────────────────────────────┐
│                        Assistant Controller                                    │
│            /api/v1/assistants/* (9 endpoints)                                  │
└───────────────────────────────┬─────────────────────────────────────────────┘
                                │
┌───────────────────────────────▼─────────────────────────────────────────────┐
│                        Assistant Orchestrator                                  │
│                                                                               │
│  ┌──────────────────────────────────────────────────────────────────────┐   │
│  │                         Assistant Pipeline                             │   │
│  │  ParseInput → IntentResolver → TaskPlanner → CopilotOrchestrator →    │   │
│  │  ResponseBuilder → EventPublisher                                     │   │
│  └──────────────────────────────────────────────────────────────────────┘   │
│                                                                               │
│  ┌────────────────────┐  ┌────────────────────┐  ┌────────────────────────┐ │
│  │   Intent Engine    │  │   Task Planner     │  │  Copilot Orchestrator  │ │
│  │  - IntentResolver  │  │  - TaskPlannerImpl │  │  - Route to copilot    │ │
│  │  - EntityExtractor │  │  - StepExecutor    │  │  - Aggregate responses │ │
│  │  - Confidence      │  │  - Pipeline mgmt   │  │  - Error handling      │ │
│  └────────────────────┘  └────────────────────┘  └────────────────────────┘ │
└───────────────────────────────┬─────────────────────────────────────────────┘
                                │
┌───────────────────────────────▼─────────────────────────────────────────────┐
│                      12 Domain Copilot Stubs                                   │
│                                                                               │
│  CustomerCopilot  ProductCopilot  TrainingCopilot  GrowerCopilot             │
│  MarketplaceCopilot  ERPCopilot  InventoryCopilot  OrderCopilot              │
│  AnalyticsCopilot  SupportCopilot  AdminCopilot  NotificationCopilot         │
│                                                                               │
│  Each copilot: validate → delegate → transform → respond                      │
└───────────────────────────────┬─────────────────────────────────────────────┘
                                │
┌───────────────────────────────▼─────────────────────────────────────────────┐
│                     Existing Business Modules                                  │
│  Customer │ Product │ Training │ Grower │ Marketplace │ ERP │ Inventory      │
│  Order │ Analytics │ Support │ Administration │ Notification                  │
└───────────────────────────────┬─────────────────────────────────────────────┘
                                │
┌───────────────────────────────▼─────────────────────────────────────────────┐
│                         Infrastructure Layer                                   │
│  ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────────────┐   │
│  │  JPA Repositories│  │  Redis Cache     │  │  Kafka Publisher         │   │
│  │  (8 repositories)│  │  (5 namespaces)  │  │  (10 event types)        │   │
│  └──────────────────┘  └──────────────────┘  └──────────────────────────┘   │
│  ┌──────────────────┐  ┌──────────────────┐                                 │
│  │  Flyway V19      │  │  Monitoring      │                                 │
│  │  (8 tables)      │  │  (Micrometer)    │                                 │
│  └──────────────────┘  └──────────────────┘                                 │
└───────────────────────────────┬─────────────────────────────────────────────┘
                                │
                                ▼
              Prompt Platform → Knowledge Platform →
              Semantic Platform → Conversation Platform →
              AI Gateway → Provider Framework
```

---

### API Endpoints (9 endpoints)

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/v1/assistants/chat` | Send message to assistant for processing |
| POST | `/api/v1/assistants/intent/resolve` | Resolve intent from user utterance |
| POST | `/api/v1/assistants/task/plan` | Create task plan for resolved intent |
| GET | `/api/v1/assistants/task/{taskId}` | Get task plan status and details |
| GET | `/api/v1/assistants/copilots` | List available copilots |
| GET | `/api/v1/assistants/copilots/{type}` | Get copilot details by type |
| POST | `/api/v1/assistants/session` | Create or resume assistant session |
| GET | `/api/v1/assistants/session/{sessionId}` | Get session status and history |
| POST | `/api/v1/assistants/feedback` | Submit feedback on assistant response |

---

### Assistant Pipeline Flow

```
User Message → AssistantController.chat()
  │
  ▼
┌──────────────────────────────────────────────────────────────────────────────┐
│ 1. ParseInput                                                                  │
│    - Validate message length (1-4000 chars)                                    │
│    - Sanitize input (prompt injection protection)                              │
│    - Extract session context from request                                      │
└─────────────────────────────────┬────────────────────────────────────────────┘
                                  │
┌─────────────────────────────────▼────────────────────────────────────────────┐
│ 2. IntentResolver                                                              │
│    - Classify utterance into IntentCategory                                   │
│    - Extract entities (product, order, customer references)                    │
│    - Resolve target AssistantType with confidence score                        │
│    - Return intent record (status: RESOLVED / AMBIGUOUS / UNKNOWN)             │
└─────────────────────────────────┬────────────────────────────────────────────┘
                                  │
┌─────────────────────────────────▼────────────────────────────────────────────┐
│ 3. TaskPlanner                                                                 │
│    - Break resolved intent into executable steps                              │
│    - Assign copilot type per step                                              │
│    - Define actions, input parameters, dependencies                           │
│    - Return TaskPlan with ordered steps                                       │
└─────────────────────────────────┬────────────────────────────────────────────┘
                                  │
┌─────────────────────────────────▼────────────────────────────────────────────┐
│ 4. CopilotOrchestrator                                                         │
│    - Execute each step via assigned copilot                                   │
│    - Copilot validates input → delegates to business module → transforms      │
│    - Aggregate results across multiple steps                                   │
│    - Handle errors per step (skip, retry, fail)                                │
└─────────────────────────────────┬────────────────────────────────────────────┘
                                  │
┌─────────────────────────────────▼────────────────────────────────────────────┐
│ 5. ResponseBuilder                                                             │
│    - Build user-friendly response from aggregated results                     │
│    - Include intent details, task status, suggested actions                   │
│    - Attach metadata (latency, copilot used, confidence)                      │
└─────────────────────────────────┬────────────────────────────────────────────┘
                                  │
┌─────────────────────────────────▼────────────────────────────────────────────┐
│ 6. EventPublisher                                                              │
│    - Publish AssistantMessageReceived, IntentResolved, TaskPlanCreated,        │
│      CopilotExecuted, AssistantResponseDelivered events to Kafka               │
│    - Record Micrometer metrics for each stage                                  │
└─────────────────────────────────┬────────────────────────────────────────────┘
                                  │
                                  ▼
                         AssistantChatResponse
```

---

### Flyway Migration V19

| Table | Description |
|-------|-------------|
| `assistant_copilot_profiles` | Copilot definition and configuration |
| `assistant_intents` | Resolved user intents |
| `assistant_task_plans` | Task execution plans |
| `assistant_task_steps` | Individual task step records |
| `assistant_sessions` | Assistant session tracking |
| `assistant_feedback` | User feedback on responses |
| `assistant_audit_logs` | Security audit trail |
| `assistant_contexts` | Session context storage |

All tables use UUID primary keys, `TIMESTAMP` for datetimes, `TEXT` for large content, H2-compatible syntax.

---

### File Count

| Layer | Files |
|-------|-------|
| Domain (enums + records) | 6 enums + 10 records |
| API (interfaces) | 4 |
| Application (services + pipeline) | 8 |
| Infrastructure (JPA entities + repos + Kafka + Redis + monitoring) | 16 |
| Interfaces (controller + DTOs) | 9 endpoints + 13 DTOs |
| Config (Flyway + config updates) | 4 |
| Tests | 10 test classes ~80 tests |
| **Total** | **~60 source files, ~80 tests** |

---

### Integration Points

| Business Module | Integration via | Copilot |
|-----------------|----------------|---------|
| Customer Service | `CustomerServiceClient` | Customer Copilot |
| Product Service | `ProductServiceClient` | Product Copilot |
| Training Service | `TrainingServiceClient` | Training Copilot |
| Grower Service | `GrowerServiceClient` | Grower Copilot |
| Marketplace Service | `MarketplaceServiceClient` | Marketplace Copilot |
| ERP Service | `ErpServiceClient` | ERP Copilot |
| Inventory Service | `InventoryServiceClient` | Inventory Copilot |
| Order Service | `OrderServiceClient` | Order Copilot |
| Analytics Service | `AnalyticsServiceClient` | Analytics Copilot |
| Support Service | `SupportServiceClient` | Support Copilot |
| Administration Service | `AdministrationServiceClient` | Administration Copilot |
| Notification Service | `NotificationServiceClient` | Notification Copilot |

---

### Database Schema Reference

See `docs/database/assistant-schema.md` for full table definitions, columns, types, constraints, indexes, and relationships.

---

### Acceptance Criteria

- [x] Assistant domain model created (6 enums, 10 records)
- [ ] Intent Engine resolves user utterance to IntentCategory with confidence >= 0.6
- [ ] Intent Engine extracts entities (product IDs, order refs, customer refs)
- [ ] Task Planner breaks intent into ordered steps with assigned copilots
- [ ] Task Planner handles single-step and multi-step task plans
- [ ] 12 Copilot stubs exist and return valid stub responses
- [ ] CopilotOrchestrator routes to correct copilot by AssistantType
- [ ] CopilotOrchestrator handles step failure (skip, retry, fail)
- [ ] Full pipeline: Parse → Intent → Task Plan → Execute → Respond
- [ ] 9 REST endpoints return expected status codes and response schemas
- [ ] Flyway V19 migration creates 8 tables with correct schema
- [ ] 8 JPA entities map correctly to Flyway tables
- [ ] Redis caching for profiles (60min), intents (30min), sessions (30min), tasks (15min), contexts (10min)
- [ ] Kafka events published for all 10 event types
- [ ] Prompt injection protection sanitizes input before processing
- [ ] Rate limiting: 30 requests/min per user
- [ ] RBAC: ASSISTANT_USER, ASSISTANT_ADMIN roles
- [ ] Micrometer metrics for each pipeline stage
- [ ] 80+ unit tests passing
- [ ] Web UI Dashboard renders copilot list and session view
