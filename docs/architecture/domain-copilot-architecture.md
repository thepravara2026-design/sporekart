# Domain Copilot Architecture

**Version:** 1.0.0
**Last Updated:** 2026-07-12
**Module:** ai-service
**Package:** `com.sporekart.ai.assistant`

---

## Overview

The Domain Copilot Architecture defines a pattern for 12 specialized AI assistants (copilots), each responsible for a specific business domain. Each copilot follows a consistent contract: validate input, delegate to the corresponding business module, transform the response, and return a standardized result. Copilots contain **no business logic** — they are orchestration stubs that route requests to existing business services.

---

## Architecture Principles

1. **Domain Isolation** — Each copilot operates within a single business domain boundary
2. **Zero Business Logic** — Copilots validate, delegate, transform, and respond; they never implement domain rules
3. **Consistent Contract** — All 12 copilots implement the same `Copilot` interface
4. **Delegation Pattern** — Every copilot delegates to the corresponding business module via service clients
5. **Stateless Design** — Copilots maintain no state; all state lives in the assistant session
6. **Graceful Degradation** — If a business module is unavailable, copilot returns descriptive error
7. **Observable** — Every copilot execution emits metrics and audit events

---

## Copilot Interface

```java
public interface Copilot {
    CopilotType getType();
    CompletableFuture<CopilotResponse> execute(CopilotRequest request);
    boolean supports(IntentCategory category);
}
```

| Method | Returns | Description |
|--------|---------|-------------|
| `getType()` | `CopilotType` | Returns the copilot's domain type enum |
| `execute()` | `CopilotResponse` | Executes the delegated action |
| `supports()` | `boolean` | Returns true if copilot handles the given intent category |

---

## Copilot Execution Pattern

```
CopilotRequest ──► validate()
                     │
                     ▼
                  delegateToBusinessModule()
                     │
                     ▼
                  transformResponse()
                     │
                     ▼
                  CopilotResponse
```

### Step Details

| Step | Responsibility | Error Handling |
|------|---------------|----------------|
| `validate()` | Check request completeness, field types, required params | Return validation error |
| `delegateToBusinessModule()` | Call business service via client interface | Catch timeout, circuit breaker, 4xx/5xx |
| `transformResponse()` | Map business response to CopilotResponse | Handle null/empty responses |
| `respond()` | Build final response with metadata | Never throw; wrap in error response |

---

## 12 Domain Copilots

### 1. Customer Copilot

| Property | Value |
|----------|-------|
| **Type** | `CUSTOMER` |
| **Domain** | Customer management |
| **Delegates To** | Customer Service (`CustomerServiceClient`) |
| **Supported Intents** | INQUIRY, SEARCH, TASK |
| **Capabilities** | Customer lookup, profile management, segmentation, status inquiries |

### 2. Product Copilot

| Property | Value |
|----------|-------|
| **Type** | `PRODUCT` |
| **Domain** | Product catalog & search |
| **Delegates To** | Product Service (`ProductServiceClient`) |
| **Supported Intents** | INQUIRY, SEARCH, NAVIGATION |
| **Capabilities** | Product search, category browsing, product details, pricing |

### 3. Training Copilot

| Property | Value |
|----------|-------|
| **Type** | `TRAINING` |
| **Domain** | Training content & progress |
| **Delegates To** | Training Service (`TrainingServiceClient`) |
| **Supported Intents** | INQUIRY, SEARCH, REPORT |
| **Capabilities** | Course lookup, progress tracking, certification status |

### 4. Grower Copilot

| Property | Value |
|----------|-------|
| **Type** | `GROWER` |
| **Domain** | Grower network & operations |
| **Delegates To** | Grower Service (`GrowerServiceClient`) |
| **Supported Intents** | INQUIRY, SEARCH, TASK |
| **Capabilities** | Grower lookup, network status, compliance checks |

### 5. Marketplace Copilot

| Property | Value |
|----------|-------|
| **Type** | `MARKETPLACE` |
| **Domain** | Marketplace listings & orders |
| **Delegates To** | Marketplace Service (`MarketplaceServiceClient`) |
| **Supported Intents** | INQUIRY, SEARCH, COMMAND |
| **Capabilities** | Listing search, marketplace stats, listing management |

### 6. ERP Copilot

| Property | Value |
|----------|-------|
| **Type** | `ERP` |
| **Domain** | Enterprise resource planning |
| **Delegates To** | ERP Service (`ErpServiceClient`) |
| **Supported Intents** | INQUIRY, REPORT, CONFIGURATION |
| **Capabilities** | Resource lookup, planning data, configuration management |

### 7. Inventory Copilot

| Property | Value |
|----------|-------|
| **Type** | `INVENTORY` |
| **Domain** | Stock & warehouse management |
| **Delegates To** | Inventory Service (`InventoryServiceClient`) |
| **Supported Intents** | INQUIRY, SEARCH, ALERT |
| **Capabilities** | Stock levels, warehouse status, low-stock alerts |

### 8. Order Copilot

| Property | Value |
|----------|-------|
| **Type** | `ORDER` |
| **Domain** | Order processing & tracking |
| **Delegates To** | Order Service (`OrderServiceClient`) |
| **Supported Intents** | INQUIRY, SEARCH, TASK |
| **Capabilities** | Order lookup, tracking, status updates, history |

### 9. Analytics Copilot

| Property | Value |
|----------|-------|
| **Type** | `ANALYTICS` |
| **Domain** | Business intelligence & reports |
| **Delegates To** | Analytics Service (`AnalyticsServiceClient`) |
| **Supported Intents** | REPORT, INQUIRY, SEARCH |
| **Capabilities** | Report generation, KPI queries, trend analysis |

### 10. Support Copilot

| Property | Value |
|----------|-------|
| **Type** | `SUPPORT` |
| **Domain** | Customer support & tickets |
| **Delegates To** | Support Service (`SupportServiceClient`) |
| **Supported Intents** | INQUIRY, TASK, SEARCH |
| **Capabilities** | Ticket lookup, FAQ resolution, escalation |

### 11. Administration Copilot

| Property | Value |
|----------|-------|
| **Type** | `ADMINISTRATION` |
| **Domain** | System administration |
| **Delegates To** | Administration Service (`AdministrationServiceClient`) |
| **Supported Intents** | CONFIGURATION, TASK, COMMAND |
| **Capabilities** | System status, user management, configuration |

### 12. Notification Copilot

| Property | Value |
|----------|-------|
| **Type** | `NOTIFICATION` |
| **Domain** | Notifications & alerts |
| **Delegates To** | Notification Service (`NotificationServiceClient`) |
| **Supported Intents** | ALERT, COMMAND, TASK |
| **Capabilities** | Send notification, alert history, preference management |

---

## Integration Architecture

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                        Assistant Orchestrator                                 │
└────────────────────────────────┬────────────────────────────────────────────┘
                                 │
                    ┌────────────┴────────────┐
                    │  CopilotOrchestrator     │
                    │  routes by AssistantType │
                    └────────────┬────────────┘
                                 │
         ┌───────────────────────┼───────────────────────┐
         │                       │                       │
         ▼                       ▼                       ▼
┌─────────────────┐   ┌─────────────────┐   ┌─────────────────┐
│ CustomerCopilot │   │  ProductCopilot │   │  TrainingCopilot│   ...  12 total
│                 │   │                 │   │                 │
│ CustomerClient  │   │  ProductClient  │   │  TrainingClient │
└────────┬────────┘   └────────┬────────┘   └────────┬────────┘
         │                     │                     │
         ▼                     ▼                     ▼
┌─────────────────┐   ┌─────────────────┐   ┌─────────────────┐
│  Customer       │   │   Product       │   │   Training      │
│  Service        │   │   Service       │   │   Service       │
└─────────────────┘   └─────────────────┘   └─────────────────┘
```

---

## Copilot Response Format

All copilots return the same `CopilotResponse` structure:

```json
{
  "copilotType": "CUSTOMER",
  "status": "SUCCESS",
  "data": { },
  "message": "Customer profile retrieved successfully",
  "suggestions": ["View order history", "Update contact details"],
  "metadata": {
    "executionTimeMs": 145,
    "delegatedService": "CustomerService",
    "confidence": 0.94
  }
}
```

### Status Values

| Status | Description |
|--------|-------------|
| `SUCCESS` | Execution completed successfully |
| `PARTIAL` | Partial results with warnings |
| `ERROR` | Execution failed with error detail |
| `TIMEOUT` | Business module timed out |
| `UNAVAILABLE` | Business module is unavailable |

---

## No Business Logic Enforcement

- Copilots **must not** contain domain-specific business rules
- Copilots **must not** perform data transformations beyond format conversion
- Copilots **must not** implement fallback logic that overrides business module responses
- Copilots **must not** cache business data (only session context and profiles are cached)
- All business logic lives in the corresponding business module (Customer Service, Product Service, etc.)
