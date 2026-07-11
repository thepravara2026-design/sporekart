# Changelog

## [1.4.0] — 2026-07-11 — Sprint 17 Part 5

### Added
- Enterprise Knowledge Platform — centralized knowledge document management and retrieval
- Knowledge Document Lifecycle (DRAFT → PENDING_REVIEW → APPROVED → PUBLISHED → DEPRECATED → ARCHIVED)
- Document Chunking Service with configurable chunk size (default 1000 chars) and overlap (default 100 chars), sentence-boundary aware
- Knowledge Metadata Service (key-value metadata, tags, categories)
- Knowledge Retrieval Pipeline (keyword-based with category/language/visibility/business module filtering, chunk selection, citation building)
- Knowledge Security Service (RBAC with 6 roles, 4 visibility levels — PUBLIC, INTERNAL, RESTRICTED, CONFIDENTIAL)
- Knowledge Redis Cache (6 namespaces with TTL strategy: doc 30min, categories 60min, metadata 10min, search 5min, citations 15min, retrieval 10min)
- Knowledge Kafka Publisher (8 event types on `knowledge-events` topic)
- Knowledge REST APIs (12 endpoints under `/api/v1/knowledge/*`)
- Knowledge RBAC (ADMINISTRATOR, KNOWLEDGE_MANAGER, CONTENT_EDITOR, USER roles)
- Feature flags (3 — AI_KNOWLEDGE_ENABLED, AI_KNOWLEDGE_CACHING, AI_KNOWLEDGE_AUDIT)
- Kafka topic `knowledge-events` (8 event types: DocumentCreated/Updated/Deleted, ChunkCreated/Updated, Indexed, Retrieved, SearchExecuted)
- Flyway migration V14 (9 tables — knowledge_categories, knowledge_documents, knowledge_document_versions, knowledge_chunks, knowledge_metadata, knowledge_tags, knowledge_sources, knowledge_access_log, knowledge_citations)
- Knowledge tests (7 test classes, 56 tests — document service, chunking, metadata, retrieval, security, Redis, Kafka, controller)
- 15 default knowledge categories seeded via `@PostConstruct`

### Modified
- `FeatureFlagName.java` — Added 3 knowledge flags (AI_KNOWLEDGE_ENABLED, AI_KNOWLEDGE_CACHING, AI_KNOWLEDGE_AUDIT)
- `AiFeatureFlagProperties.java` — Added 3 knowledge-boolean properties (knowledgeEnabled, knowledgeCaching, knowledgeAudit) with getters/setters
- `KafkaConfig.java` — Added `knowledgeEventsTopic()` bean
- `SecurityConfig.java` — Permitted `/api/v1/knowledge/**` endpoints
- `application.yml` — Added knowledge-enabled, knowledge-caching, knowledge-audit feature flags and knowledge module config

### Documentation
- `docs/sprints/phase-3/sprint-17-part-05.md` — Sprint spec
- `docs/architecture/knowledge-platform.md` — Architecture
- `docs/architecture/rag-architecture.md` — RAG pipeline
- `docs/ai-platform/knowledge-platform.md` — Knowledge platform overview
- `docs/api/knowledge-api.md` — API reference (12 endpoints)
- `docs/database/knowledge-schema.md` — 9 tables with indexes
- `docs/implementation-log.md` — Part 5 entry
- `docs/changelog.md` — This entry

## [1.3.0] — 2026-07-11 — Sprint 17 Part 4

### Added
- Enterprise Prompt Management Platform — centralized prompt template management
- Prompt Registry with 13 default categories (Customer Support, Grower Assistant, Training, Product Recommendations, Marketplace, SEO, Marketing, Content Generation, Analytics, ERP, Internal Assistant, System Prompt, Developer Prompt)
- Prompt Template Engine with safe `{{variable}}` rendering (escape, validation, unresolved variable rejection)
- Prompt Versioning (DRAFT → PENDING_APPROVAL → APPROVED → PUBLISHED → DEPRECATED → ARCHIVED)
- Prompt Approval Workflow (submit, approve, reject)
- Prompt Rollback (creates new version with rolled-back content)
- Prompt Validation (injection detection, regex validation, payload size limits, template length limits)
- Prompt Import/Export (JSON format with categories, templates, and variables)
- Prompt Audit Trail (18 audit action types tracked in `ai_prompt_audit`)
- Prompt Execution Log (`ai_prompt_execution_log` for render tracking)
- Application services (9 — PromptApplicationService, PromptCategoryService, PromptValidationService, PromptRenderService, PromptVersionService, PromptLifecycleService, PromptAuditService, PromptSearchService, PromptImportExportService)
- JPA entities (6 — category, template, version, variable, audit, execution log)
- JPA repositories (6 with search, filter, and pagination)
- Prompt controller with 17 REST endpoints (`/api/v1/ai/prompts/*`)
- Prompt infrastructure (Redis cache with 5 namespaces, Kafka publisher with 7 event types)
- Feature flags (3 — AI_PROMPT_ENABLED, AI_PROMPT_CACHING, AI_PROMPT_AUDIT)
- Kafka topic `ai-prompt-events` (7 event types: PromptCreated, PromptUpdated, PromptPublished, PromptDeprecated, PromptRolledBack, PromptExecutionStarted, PromptExecutionCompleted)
- Flyway migration V13 (6 tables — ai_prompt_categories, ai_prompt_templates, ai_prompt_versions, ai_prompt_variables, ai_prompt_audit, ai_prompt_execution_log)
- Prompt tests (12 test classes, 84 tests — validation, render, version, lifecycle, category, import/export, application, search, audit, controller, Redis, Kafka)

### Modified
- `FeatureFlagName.java` — Added 3 new prompt flags
- `AiFeatureFlagProperties.java` — Added 3 new prompt boolean properties (promptEnabled, promptCaching, promptAudit) with getters/setters
- `KafkaConfig.java` — Added `aiPromptEventsTopic()` bean
- `AiPromptConfig.java` — Rewritten to seed 13 default prompt categories on startup
- `SecurityConfig.java` — Permitted `/api/v1/ai/prompts` endpoints
- `application.yml` — Added prompt feature flags (prompt-enabled, prompt-caching, prompt-audit)

## [1.2.0] — 2026-07-11 — Sprint 17 Part 3

### Added
- Enterprise AI Provider Abstraction Layer (8 provider adapters)
- Core provider interfaces (13 — AIProvider, ChatProvider, EmbeddingProvider, GenerationProvider, VisionProvider, ModerationProvider, ProviderCapabilities, ProviderHealth, ProviderConfiguration, ProviderHealthService, ProviderSelector, ProviderFailoverStrategy, ProviderValidator)
- Provider domain models (4 — ProviderModel, ProviderHealthRecord, ProviderConfigurationRecord, ProviderCapabilityInfo)
- Provider application services (7 — registry, factory, health, configuration, selector, validator, failover)
- Provider adapters (8 — Gemini, OpenAI, Claude, Azure OpenAI, Bedrock, Ollama, Mistral, Local LLM)
- Provider controller with 6 REST endpoints (`/api/v1/ai/providers/*`)
- Provider infrastructure (Redis cache, Kafka publisher)
- Provider adapter registration via `AiProviderConfig`
- Feature flags (9 provider flags — AI_PROVIDER_ENABLED, GEMINI, OPENAI, CLAUDE, AZURE_OPENAI, BEDROCK, OLLAMA, MISTRAL, LOCAL_LLM)
- Kafka topic `ai-provider-events` (6 event types)
- Flyway migration V12 (5 tables — ai_provider_registry, ai_provider_models, ai_provider_configuration, ai_provider_capabilities, ai_provider_health)
- Provider tests (8 test classes, 49 tests)

### Modified
- `FeatureFlagName.java` — Added 9 new provider flags
- `FeatureFlagService.java` — `isProviderEnabled` handles 8 provider types
- `AiFeatureFlagProperties.java` — Added 8 new provider boolean properties
- `KafkaConfig.java` — Added `aiProviderEventsTopic()` bean
- `SecurityConfig.java` — Permitted `/api/v1/ai/providers` endpoints
- `application.yml` — Added provider feature flags (azure-openai, bedrock, ollama, mistral, local-llm)
- `AiProviderConfig.java` — Rewritten to register all 8 adapters on startup

## [1.1.0] — 2026-07-11 — Sprint 17 Part 2

### Added
- AI Gateway — single entry point for all AI requests
- Gateway execution pipeline (validation, feature flags, rate limiting, provider resolution, audit, metrics)
- Gateway controller with 5 endpoints (`/api/v1/ai/execute`, `/validate`, `/health`, `/status`, `/features`)
- Gateway application services (pipeline, domain service, facade, validator, builder, context resolver, audit, metrics, exception translator, feature manager)
- Gateway infrastructure (Redis cache, Kafka publisher, health indicator, default provider/retry/timeout strategies)
- Gateway DTOs (9 records — AIExecutionRequest, AIExecutionResponse, AIHealthResponse, GatewayStatus, etc.)
- Gateway exceptions (6 — AIGatewayException, AIValidationException, AIRateLimitException, AIExecutionException, GatewayUnavailableException, FeatureDisabledException)
- Core API interfaces (4 — AIGateway, AIRequestValidator, AIRateLimiter, AIAuditService)
- Feature flags (4 new — AI_GATEWAY_ENABLED, AI_REQUEST_LOGGING, AI_RATE_LIMITING, AI_METRICS)
- Kafka topic `ai-gateway-events` (7 event types)
- Redis caching strategy (config, features, health, execution metadata)
- Flyway migration V11 — `ai_gateway_configuration`, `ai_request_audit`, `ai_execution_history`
- Gateway tests (3 test classes — controller, pipeline, validator; 20 tests total)
- ArchUnit rules enforcing gateway module boundaries

### Modified
- `KafkaConfig.java` — Added `aiGatewayEventsTopic()` bean
- `AiFeatureFlagProperties.java` — Added gateway, request-logging, rate-limiting, metrics fields
- `FeatureFlagService.java` — `isModuleEnabled(GATEWAY)` gates on both `AI_PLATFORM_ENABLED` and `AI_GATEWAY_ENABLED`; `isProviderEnabled` handles MOCK
- `application.yml` — Added `gateway-enabled`, `request-logging`, `rate-limiting`, `metrics` feature flags
- `SecurityConfig.java` — Permitted `/api/v1/ai/health`, `/status`, `/features`
- `AiGatewayConfig.java` — Registered `ProviderResolver`, `RetryStrategy`, `TimeoutStrategy` beans; removed duplicate `RequestValidator` bean
- `GatewayResponseBuilder.java` — Fixed `buildError` to include `correlationId`
- `BasicRequestValidator.java` — Changed to throw `AIValidationException` for consistency
- `ModuleDependencyTest.java` — Added 3 gateway-specific ArchUnit rules

### Fixed
- Bean conflict between `BasicRequestValidator` (bean) and `GatewayRequestValidator` (@Service)
- `BasicRequestValidator` throwing `ValidationException` instead of `AIValidationException`
- `GatewayResponseBuilder.buildError` not using `correlationId` parameter
- `MOCK` provider not recognized in `FeatureFlagService.isProviderEnabled`

## [1.0.0] — 2026-07-11 — Sprint 17 Part 1

### Added
- Enterprise AI Platform modular architecture (10 bounded contexts)
- Shared AI contracts, DTOs, exceptions, and constants
- Feature flag framework with 10 configurable flags
- AI provider and model enums (Gemini, OpenAI, Claude support)
- Request/response envelopes with correlation ID support
- Security and observability interface contracts
- Flyway placeholder migrations for AI infrastructure tables
- API path contracts for all AI modules
- Architecture validation tests (ArchUnit, Modulith)
- Documentation (sprint, architecture, platform overview, implementation log)
- Spring Modulith dependency for module boundary enforcement

### Modified
- `pom.xml` — Added spring-modulith and archunit dependencies
- `OpenApiConfig.java` — Updated API description scope
- `application.yml` — Added full AI platform configuration

### Fixed
- Bean name conflict between new and existing `FeatureFlagService`
- Record accessor method conflict in `PromptVariable`
