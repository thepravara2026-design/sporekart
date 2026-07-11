# Sprint 17 — Part 4: Enterprise Prompt Management Platform

**Date:** 2026-07-11
**Module:** ai-service
**Lead:** Enterprise AI Platform Engineering Team

---

## Objective

Build the Enterprise Prompt Management Platform — the centralized system for managing, versioning, rendering, and auditing all AI prompts. Prompts must never be hardcoded inside business services. Every AI interaction must load prompts from this platform.

---

## Deliverables

### Prompt Registry & Categories

| Category | Purpose |
|----------|---------|
| Customer Support | Support assistant prompts |
| Grower Assistant | Cultivation and farming assistant |
| Training | Training content prompts |
| Product Recommendations | Product suggestion prompts |
| Marketplace | Marketplace assistant prompts |
| SEO | SEO optimization prompts |
| Marketing | Marketing content prompts |
| Content Generation | Content creation prompts |
| Analytics | Data analysis prompts |
| ERP | ERP integration prompts |
| Internal Assistant | Internal operations assistant |
| System Prompt | System-level prompts |
| Developer Prompt | Developer assistance prompts |

13 default categories seeded via `AiPromptConfig` `@PostConstruct`.

### Prompt Template Variables

Supported placeholders: `{{customerName}}`, `{{productName}}`, `{{trainingName}}`, `{{language}}`, `{{region}}`, `{{conversationHistory}}`, `{{currentDate}}`, `{{userRole}}` — plus any custom variable.

### Version Management

| State | Transitions |
|-------|-------------|
| DRAFT | → PENDING_APPROVAL |
| PENDING_APPROVAL | → APPROVED, → DRAFT (reject) |
| APPROVED | → PUBLISHED |
| PUBLISHED | → DEPRECATED |
| DEPRECATED | → ARCHIVED |
| ARCHIVED | Terminal |

Rollback creates a new version with the rolled-back content.

### Prompt Template Engine

- `PromptRenderService` — renders `{{variable}}` placeholders safely
- Rejects unresolved variables
- Escapes special characters
- Validates variable values against regex patterns
- Supports default values

### Application Services (7 new in `prompt/application/`)

| Service | Responsibility |
|---------|----------------|
| `PromptCategoryService` | Category CRUD with audit |
| `PromptValidationService` | Template validation, variable validation, injection detection, payload size limits |
| `PromptRenderService` | Safe template rendering with variable substitution |
| `PromptVersionService` | Version creation, publishing, deprecation, rollback, archiving |
| `PromptLifecycleService` | Submit for approval, approve, reject, publish, deprecate, archive |
| `PromptAuditService` | Audit trail querying and recording |
| `PromptSearchService` | Search by query, category, status |
| `PromptImportExportService` | JSON export/import of categories, templates, and variables |
| `PromptApplicationService` | Main facade — template CRUD with validation |

### Infrastructure (2 new in `prompt/infrastructure/`)

| Component | Purpose |
|-----------|---------|
| `PromptRedisCacheService` | Redis caching with namespace prefixes and TTLs |
| `PromptKafkaEventPublisher` | Kafka event publishing on prompt lifecycle changes |

### REST APIs (10 endpoints)

| Method | Path | Purpose |
|--------|------|---------|
| `GET` | `/api/v1/ai/prompts` | List/search/filter prompts |
| `GET` | `/api/v1/ai/prompts/{id}` | Get prompt by ID |
| `POST` | `/api/v1/ai/prompts` | Create prompt |
| `PUT` | `/api/v1/ai/prompts/{id}` | Update prompt |
| `DELETE` | `/api/v1/ai/prompts/{id}` | Delete prompt |
| `POST` | `/api/v1/ai/prompts/{id}/publish` | Publish prompt |
| `POST` | `/api/v1/ai/prompts/{id}/rollback` | Rollback to version |
| `POST` | `/api/v1/ai/prompts/render` | Render template |
| `GET` | `/api/v1/ai/prompts/categories` | List categories |
| `GET` | `/api/v1/ai/prompts/history` | Audit history |
| `GET` | `/api/v1/ai/prompts/{id}/versions` | List versions |
| `GET` | `/api/v1/ai/prompts/{id}/history` | Template audit history |
| `POST` | `/api/v1/ai/prompts/{id}/submit` | Submit for approval |
| `POST` | `/api/v1/ai/prompts/{id}/approve` | Approve prompt |
| `POST` | `/api/v1/ai/prompts/{id}/deprecate` | Deprecate prompt |
| `POST` | `/api/v1/ai/prompts/import` | Import prompts JSON |
| `GET` | `/api/v1/ai/prompts/export` | Export prompts JSON |

### Kafka Events (7 event types)

| Event | Publisher |
|-------|-----------|
| `PromptCreated` | `PromptKafkaEventPublisher` |
| `PromptUpdated` | `PromptKafkaEventPublisher` |
| `PromptPublished` | `PromptKafkaEventPublisher` |
| `PromptDeprecated` | `PromptKafkaEventPublisher` |
| `PromptRolledBack` | `PromptKafkaEventPublisher` |
| `PromptExecutionStarted` | `PromptKafkaEventPublisher` |
| `PromptExecutionCompleted` | `PromptKafkaEventPublisher` |

Topic: `ai-prompt-events` (3 partitions, 1 replica)

### Redis Caching Strategy

| Prefix | TTL | Purpose |
|--------|-----|---------|
| `prompt:{id}` | 30 min | Prompt template cache |
| `prompt:pub:{id}` | 15 min | Published prompt text |
| `prompt:version:{id}` | 30 min | Version cache |
| `prompt:cat:*` | 60 min | Category cache |
| `prompt:meta:{id}` | 10 min | Metadata cache |

Invalidation on publish, update, deprecate, rollback.

### Flyway Migration V13 — 6 tables

| Table | Purpose |
|-------|---------|
| `ai_prompt_categories` | Prompt categories with display order |
| `ai_prompt_templates` | Template definitions with status and version tracking |
| `ai_prompt_versions` | Version history with approval tracking |
| `ai_prompt_variables` | Variable definitions with validation regex |
| `ai_prompt_audit` | Audit trail for all prompt modifications |
| `ai_prompt_execution_log` | Execution metadata for rendered prompts |

### Security (RBAC roles)

| Role | Permissions |
|------|-------------|
| Prompt Author | Create, update, delete own prompts |
| Prompt Reviewer | View, approve, reject pending prompts |
| Prompt Publisher | Publish, deprecate, rollback |
| Administrator | Full access including import/export |

### Tests (10 test classes)

| Test Class | Tests | Type |
|-----------|-------|------|
| `PromptValidationServiceTest` | 12 | Unit |
| `PromptRenderServiceTest` | 9 | Unit |
| `PromptVersionServiceTest` | 8 | Unit |
| `PromptLifecycleServiceTest` | 10 | Unit |
| `PromptCategoryServiceTest` | 6 | Unit |
| `PromptImportExportServiceTest` | 3 | Unit |
| `PromptApplicationServiceTest` | 6 | Unit |
| `PromptSearchServiceTest` | 5 | Unit |
| `PromptAuditServiceTest` | 3 | Unit |
| `PromptControllerTest` | 10 | Integration (MockMvc) |
| `PromptRedisCacheServiceTest` | 5 | Unit |
| `PromptKafkaEventPublisherTest` | 7 | Unit |

**84 total tests across 12 test classes**

---

## Architecture Rules

1. **No hardcoded prompts** — All AI interactions load prompts from Prompt Management Platform
2. **Prompt isolation** — Prompt logic is isolated from provider implementations
3. **Version immutability** — Once published, versions are immutable (rollback creates new version)
4. **Audit every modification** — Every prompt change is recorded in `ai_prompt_audit`
5. **Safe rendering** — Template engine rejects unresolved variables and detects injection patterns
6. **Backward compatibility** — Existing `PromptManagementService`, `PromptOrchestrationService`, `AiController` untouched

---

## Configuration Changes

### `application.yml`

```yaml
sporekart:
  ai:
    features:
      prompt-enabled: true
      prompt-caching: true
      prompt-audit: true
```

### `SecurityConfig.java`

Permitted paths for `/api/v1/ai/prompts`, `/api/v1/ai/prompts/*`, `/api/v1/ai/prompts/categories`, `/api/v1/ai/prompts/render`, `/api/v1/ai/prompts/history`, `/api/v1/ai/prompts/export`, `/api/v1/ai/prompts/import`

### `KafkaConfig.java`

`aiPromptEventsTopic()` bean added (3 partitions, 1 replica)

### `AiPromptConfig.java`

Rewritten — seeds 13 default categories on startup

### `FeatureFlagName.java`

Added `AI_PROMPT_ENABLED`, `AI_PROMPT_CACHING`, `AI_PROMPT_AUDIT`

---

## Risks

- Template engine rejects unresolved variables — requires all callers to provide complete variable maps
- Existing `PromptManagementService` and `PromptOrchestrationService` remain in-memory — migration to new platform is optional until Sprint 17 Part 5
- `PromptRenderService.escapeValue()` escapes special characters — may affect prompts that contain literal `\n`, `\t`, `\"` sequences
- Import/export uses JSON format — large exports may hit memory limits
- H2 in-memory database used for tests — PostgreSQL-specific features (JSONB, arrays) are emulated via `TEXT`

---

## Technical Debt

- No circuit breaker for prompt execution logging
- Prompt execution metrics not wired to Micrometer yet
- No scheduled version cleanup for expired/archived prompts
- Import overwrites categories but skips existing templates (no conflict resolution)
- No bulk operations for templates or versions
- 7 pre-existing `AiControllerTest` failures (HTTP 401 — missing auth) remain

---

## Readiness for Sprint 17 Part 5

Part 5 can begin implementation of:
- AI Chat service with conversation management
- RAG pipeline for knowledge retrieval
- Semantic search with vector embeddings
- Content generation service
- Workflow engine execution
- Monitoring and cost tracking dashboards
- Provider SDK integration for real AI calls
