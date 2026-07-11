# Enterprise Prompt Management Architecture

**Version:** 1.0.0
**Last Updated:** 2026-07-11
**Owner:** Enterprise AI Platform Engineering Team

---

## Overview

The Enterprise Prompt Management Platform is the centralized system for managing all AI prompts in the SporeKart platform. Prompts are never hardcoded inside business services. Every AI interaction loads prompts from this platform, ensuring consistency, auditability, and governance.

---

## Architecture Diagram

```
Business Modules (Catalog, Orders, Support, Training, Marketplace, ERP, etc.)
        │
        ▼
┌──────────────────────────────────────────────────────────────────────┐
│                    Prompt Management Platform                         │
│                                                                      │
│  ┌──────────────────────────────────────────────────────────────┐   │
│  │                    REST API Layer                              │   │
│  │  POST /api/v1/ai/prompts    GET /api/v1/ai/prompts           │   │
│  │  PUT /api/v1/ai/prompts/{id} DELETE /api/v1/ai/prompts/{id}  │   │
│  │  POST .../publish  POST .../rollback  POST .../render        │   │
│  │  GET .../categories  GET .../history  POST .../import        │   │
│  └──────────────────────────────────────────────────────────────┘   │
│                                                                      │
│  ┌──────────────────────────────────────────────────────────────┐   │
│  │                  Application Services                          │   │
│  │  ┌────────────────┐ ┌──────────────┐ ┌──────────────────┐   │   │
│  │  │ PromptRegistry  │ │ PromptRender │ │ PromptVersion    │   │   │
│  │  │ Category        │ │ Safe Render  │ │ Create           │   │   │
│  │  │ Create/Update   │ │ Substitution │ │ Publish          │   │   │
│  │  │ Delete/Search   │ │ Validation   │ │ Rollback         │   │   │
│  │  └────────────────┘ └──────────────┘ └──────────────────┘   │   │
│  │  ┌────────────────┐ ┌──────────────┐ ┌──────────────────┐   │   │
│  │  │ PromptLifecycle│ │ PromptAudit  │ │ PromptImport     │   │   │
│  │  │ Submit/Approve │ │ Trail Query  │ │ Export/Import    │   │   │
│  │  │ Publish/Deprec │ │ Record Event │ │ JSON Format      │   │   │
│  │  └────────────────┘ └──────────────┘ └──────────────────┘   │   │
│  └──────────────────────────────────────────────────────────────┘   │
│                                                                      │
│  ┌──────────────────────────────────────────────────────────────┐   │
│  │                    Infrastructure Layer                         │   │
│  │  ┌────────────────────┐  ┌──────────────────────────────┐    │   │
│  │  │  JPA Repositories   │  │  PromptRedisCacheService    │    │   │
│  │  │  (6 repositories)   │  │  (5 cache namespaces)       │    │   │
│  │  └────────────────────┘  └──────────────────────────────┘    │   │
│  │  ┌────────────────────┐  ┌──────────────────────────────┐    │   │
│  │  │  PromptKafkaEvent  │  │  Flyway V13 Migration        │    │   │
│  │  │  Publisher (7 evts) │  │  (6 tables)                  │    │   │
│  │  └────────────────────┘  └──────────────────────────────┘    │   │
│  └──────────────────────────────────────────────────────────────┘   │
└──────────────────────────────────────────────────────────────────────┘
        │
        ▼
┌──────────────────────────────────────────────────────────────────────┐
│                    External Systems                                   │
│  PostgreSQL (Flyway V13)    Redis (Cache)    Kafka (Events)         │
└──────────────────────────────────────────────────────────────────────┘
```

---

## Module Structure

```
com.sporekart.ai.prompt
├── api/                          (existing, extended)
│   ├── PromptService.java        — Interface: create, update, render, delete
│   └── PromptTemplatePort.java   — Persistence port interface
│
├── application/                  (new)
│   ├── PromptApplicationService.java    — Template CRUD facade
│   ├── PromptCategoryService.java       — Category management
│   ├── PromptValidationService.java     — Validation & injection detection
│   ├── PromptRenderService.java         — Safe template rendering
│   ├── PromptVersionService.java        — Version lifecycle
│   ├── PromptLifecycleService.java      — Approval workflow
│   ├── PromptAuditService.java          — Audit recording & query
│   ├── PromptSearchService.java         — Search & filter
│   ├── PromptImportExportService.java   — JSON import/export
│   ├── PromptNotFoundException.java     — 404 exception
│   ├── PromptValidationException.java   — Validation exception
│   ├── PromptLifecycleException.java    — State transition exception
│   └── PromptRenderException.java       — Render error exception
│
├── config/
│   └── AiPromptConfig.java       — Seeds 13 default categories
│
├── domain/                       (existing, extended)
│   ├── PromptTemplate.java       — Record with render() method
│   ├── PromptVariable.java       — Variable definition record
│   ├── PromptStatus.java         — DRAFT, PENDING_APPROVAL, APPROVED, PUBLISHED, DEPRECATED, ARCHIVED
│   ├── VariableType.java         — STRING, NUMBER, BOOLEAN, DATE, LIST, OBJECT
│   └── AuditAction.java          — 18 audit action types
│
├── infrastructure/
│   ├── persistence/              (new)
│   │   ├── PromptCategoryEntity.java       — JPA entity
│   │   ├── PromptTemplateEntity.java       — JPA entity
│   │   ├── PromptVersionEntity.java        — JPA entity
│   │   ├── PromptVariableEntity.java       — JPA entity
│   │   ├── PromptAuditEntity.java          — JPA entity
│   │   ├── PromptExecutionLogEntity.java   — JPA entity
│   │   ├── PromptCategoryRepository.java   — JPA repository
│   │   ├── PromptTemplateRepository.java   — JPA repository
│   │   ├── PromptVersionRepository.java    — JPA repository
│   │   ├── PromptVariableRepository.java   — JPA repository
│   │   ├── PromptAuditRepository.java      — JPA repository
│   │   └── PromptExecutionLogRepository.java — JPA repository
│   ├── PromptRedisCacheService.java        — Redis caching
│   └── PromptKafkaEventPublisher.java      — Kafka event publishing
│
└── interfaces/
    └── rest/
        ├── PromptController.java           — 17 endpoints
        └── dto/                            (8 DTOs)
            ├── CreatePromptRequest.java
            ├── UpdatePromptRequest.java
            ├── RenderPromptRequest.java
            ├── RenderPromptResponse.java
            ├── PromptResponse.java
            ├── CategoryResponse.java
            ├── VersionResponse.java
            └── AuditResponse.java
```

---

## Design Patterns

| Pattern | Usage |
|---------|-------|
| **Facade** | `PromptApplicationService` — unified template CRUD |
| **Strategy** | `PromptValidationService` — validation rules |
| **Template Method** | `PromptRenderService` — variable resolution algorithm |
| **State** | `PromptStatus` — lifecycle state machine |
| **Observer** | `PromptKafkaEventPublisher` — lifecycle events |
| **Repository** | 6 JPA repositories for persistence |

---

## Template Rendering Flow

```
Request: templateText + variables Map
        │
        ▼
PromptValidationService.validateRenderVariables()
        │
        ├── Check required variables are present
        ├── Validate variable values against regex patterns
        ├── Check for injection patterns
        └── Validate payload size
        │
        ▼
PromptRenderService.render()
        │
        ├── Substitute {{variable}} placeholders
        ├── Escape special characters
        ├── Check for unresolved variables
        └── Return rendered text
```

---

## Version Lifecycle

```
┌─────────┐     submit     ┌──────────────────┐
│  DRAFT  │───────────────▶│ PENDING_APPROVAL  │
└─────────┘                └──────────────────┘
     ▲                              │
     │ reject              approve  │
     │                              ▼
     │                     ┌──────────────────┐
     │                     │    APPROVED       │
     │                     └──────────────────┘
     │                              │
     │                     publish  │
     │                              ▼
     │                     ┌──────────────────┐
     │                     │   PUBLISHED       │
     │                     └──────────────────┘
     │                              │
     │                     deprecate │
     │                              ▼
     │                     ┌──────────────────┐
     │                     │  DEPRECATED       │
     │                     └──────────────────┘
     │                              │
     │                     archive  │
     │                              ▼
     │                     ┌──────────────────┐
     │                     │   ARCHIVED        │
     │                     └──────────────────┘
```

---

## Cross-Cutting Concerns

### Observability
- Prompt usage via execution log table
- Render latency tracked in execution log (duration_ms)
- Version usage via version query patterns
- Publish events in Kafka
- Cache hit/miss in Redis

### Security
- Variable injection detection via `PromptValidationService`
- Payload size limits (100KB total, 10KB per variable)
- Template length limits (50KB)
- RBAC roles: Author, Reviewer, Publisher, Administrator
- Audit trail for every modification

### Caching (Redis)
- Templates cached with 30 min TTL
- Published prompts cached with 15 min TTL
- Categories cached with 60 min TTL
- Full invalidate on publish/update/deprecate/rollback

### Events (Kafka)
- Topic: `ai-prompt-events` (3 partitions)
- 7 event types for prompt lifecycle

---

## Error Handling

| Error Code | Exception | Description |
|-----------|-----------|-------------|
| AI-006 | `PromptValidationException` | Validation failure |
| AI-008 | `PromptRenderException` | Render error (unresolved variables) |
| AI-012 | `PromptNotFoundException` | Template/version/category not found |
