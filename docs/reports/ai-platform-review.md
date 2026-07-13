# AI Platform Review — Enterprise Architecture Review Board

**Report:** AI Platform Certification (Step 7 of the gate)
**Platform:** SporeKart Enterprise AI Platform
**Date:** 2026-07-12
**Status:** Submitted for certification
**Sources reviewed:** `docs/ai-platform/*`, `docs/architecture/ai-gateway-architecture.md`, `docs/architecture/provider-registry.md`, `docs/architecture/prompt-registry.md`, `docs/architecture/knowledge-registry.md`, `docs/architecture/usage-tracking.md`, `docs/architecture/capability-discovery.md`, `docs/architecture/workflow-automation.md`, `docs/architecture/governance-platform.md`, `docs/sprints/phase-4/final-hardening.md`, `docs/phase-4-final-report.md`

---

## 1. Executive Summary

The Enterprise AI Platform is implemented as a Spring Modulith with hexagonal, DDD-per-module boundaries. It provides centralized AI capabilities to all business modules and abstracts provider complexity behind a single gateway. The platform is composed of runtime/exectution modules (gateway, provider, prompt, knowledge, semantic, conversation, content, workflow, monitoring, assistant) and a layer of read-optimized **hardening registries** introduced in the Phase 4 architecture hardening (provider registry, prompt registry, knowledge registry, usage tracking, capability discovery).

All execution modules are documented as implemented or planned per the AI Platform Overview (v1.3.0). The gateway, provider abstraction, and prompt platform are reported as fully implemented; content, knowledge, semantic, conversation, workflow, monitoring are documented with interfaces/contracts (some still marked "Planned" in the capability matrix but with full design docs). The five reviewed hardening registries are documented as delivered in the Phase 4 final hardening and final report.

Integration is coherent: a business request enters through the conversation platform, is orchestrated by the workflow engine, passes through the governance pipeline, then reaches the AI gateway which resolves the provider via the registry/capability-discovery layer and returns a response. The registries act purely as discovery/metadata layers and do not participate in the request hot path except as consulted catalogs.

**Certification recommendation:** Proceed. No blocking gaps identified. Non-blocking findings are listed in Section 6.

---

## 2. Module Responsibilities (confirmed)

### 2.1 AI Gateway — Central Abstraction
**Doc:** `docs/architecture/ai-gateway-architecture.md`, `docs/ai-platform/ai-platform-overview.md`
**Package:** `com.sporekart.ai.gateway`
**REST:** `/api/v1/ai/*` (5 endpoints: execute, validate, health, status, features)

The AI Gateway is the single entry point for every AI request. Business modules never call external providers directly. It runs a 9-stage `GatewayPipeline`:
`Receive → Validate → Feature Check → Rate Limit → Resolve Context → Resolve Provider → Execute → Audit → Collect Metrics → Build Response`.

Confirmed responsibilities: request validation (max prompt 32000 chars), feature-flag gating at entry, rate limiting (interface + InMemory), provider resolution, retry with exponential backoff (max 3), per-module timeout, Micrometer metrics, structured audit logging, Kafka publishing (7 event types to `ai-gateway-events`), Redis caching (4 namespaces), RFC 9457 error envelope, and ArchUnit module-isolation rules.

### 2.2 Provider Abstraction (provider module)
**Doc:** `docs/ai-platform/provider-abstraction.md`
**Package:** `com.sporekart.ai.provider`

Defines the provider interface hierarchy (`AIProvider`, `ChatProvider`, `EmbeddingProvider`, `GenerationProvider`, `VisionProvider`, `ModerationProvider`) and 8 stub adapters (Gemini, OpenAI, Claude, Azure OpenAI, Bedrock, Ollama, Mistral, Local LLM). Provides capability matrix, health monitoring (`ProviderHealthServiceImpl`), selector + failover, configuration, and validation. All adapters currently return stub responses; no real SDK calls are made. This is the execution contract the gateway dispatches to.

### 2.3 Prompt Platform (existing prompt module)
**Doc:** `docs/ai-platform/prompt-management.md`
**Package:** `com.sporekart.ai.prompt`

Fully implemented prompt management: 17 REST endpoints, 9 application services, 6 JPA repositories, 13 seeded categories. Includes a safe template engine (variable resolution, injection detection), full version lifecycle (`DRAFT → PENDING_APPROVAL → APPROVED → PUBLISHED → DEPRECATED → ARCHIVED`), approval workflow, 18-type audit trail, import/export, Redis (5 namespaces), and Kafka events (7 types). Every AI interaction loads prompts from this platform; no hardcoding is permitted.

### 2.4 Prompt Registry (new versioning registry)
**Doc:** `docs/architecture/prompt-registry.md`
**Module:** `prompt-registry` (`com.sporekart.ai.promptregistry`)

Extends the Prompt Platform with registry-level discovery, comparison, and lifecycle governance. Adds immutable versioning (`templateId` + `versionNumber` + `versionId`), owner/tags metadata, status lifecycle (`DRAFT → REVIEW → APPROVED → PUBLISHED → DEPRECATED → ARCHIVED`), safe rollback (new version, no in-place mutation), append-only history, injection/length/regex validation, version diff/comparison API, paginated search, and a future A/B-testing extension point. Integration: consumed by Gateway/Content/Conversation/Assistants; emits to Event Catalog and Usage Tracking; 17 endpoints auto-registered to the API Registry.

### 2.5 Workflow Platform (workflow module)
**Doc:** `docs/architecture/workflow-automation.md`

Provides configurable workflow execution for governance and AI operations. Supports event-driven, scheduled, and manual initiation with sequential, parallel, retry (exponential backoff), and compensation execution modes. Lifecycle: `PENDING → RUNNING → COMPLETED / FAILED → COMPENSATED / TIMEOUT / CANCELLED`. Integrates with `WorkflowOrchestrator`, `AutomationEngine`, `SchedulerService`, `RetryManager`, `EscalationManager`. Acts as the orchestration layer between conversation intake and the governance/AI execution pipeline.

### 2.6 Knowledge Platform (knowledge module)
**Doc:** `docs/ai-platform/knowledge-platform.md`
**Package:** `com.sporekart.ai.knowledge`

Centralized document management and retrieval: documents (versioned, soft-deleted), sentence-boundary chunking, categories, and four visibility levels (`PUBLIC`, `INTERNAL`, `RESTRICTED`, `CONFIDENTIAL`) enforced by `KnowledgeSecurityService`. Provides create/search/retrieve context APIs. Feeds the Semantic Platform with chunks for embedding.

### 2.7 Knowledge Registry (new source registry)
**Doc:** `docs/architecture/knowledge-registry.md`
**Module:** `knowledge-registry` (`com.sporekart.ai.knowledgeregistry`)

Authoritative catalog of every knowledge source behind a uniform source-type contract. Supports source types `DATABASE`, `PDF`, `MARKDOWN`, `WEBSITE`, `FAQ`, `TRAINING`, `CMS` (supported) and reserves `SHAREPOINT`, `GOOGLE_DRIVE`, `CONFLUENCE`, `NOTION` (future, enum `UNSUPPORTED`). Carries owner/version/refresh-policy (MANUAL/SCHEDULED/EVENT_DRIVEN/WEBHOOK), health status, and sync status (`PENDING/IN_PROGRESS/COMPLETED/FAILED/STALE`). Integration: ingestion/chunking/retrieval for Knowledge Platform; embedding + vector indexing for Semantic Platform; consumes embedding providers from Provider Registry/Capability Discovery; logs retrieval counts to Usage Tracking; emits to Event Catalog.

### 2.8 Semantic Search (semantic module)
**Doc:** `docs/ai-platform/semantic-intelligence.md`
**Package:** `com.sporekart.ai.semantic`

Enterprise semantic search, vector embeddings, and hybrid retrieval. Layered REST → application services (`EmbeddingService`, `SearchService`, `RankingService`, `VectorIndexService`, `SemanticRetrievalService`, `SemanticCacheService`, `SemanticSecurityService`) → infrastructure (Redis, Kafka, JPA, embedding adapters, pgvector). Six search types: `SEMANTIC`, `KEYWORD` (BM25), `HYBRID` (RRF fusion), `CROSS_ENCODER`, `MULTI_VECTOR`, `CONTEXTUAL`. Cosine/Euclidean/dot-product similarity. Integrates tightly with the Knowledge Platform (maps embeddings back to documents, applies visibility filters, builds citations). Embedding providers selected via Provider Registry; embeddings cached 24h write-through.

### 2.9 Conversation Platform (conversation module)
**Doc:** `docs/ai-platform/conversation-platform.md`
**Package:** `com.sporekart.ai.conversation`

Manages the full lifecycle of user-AI conversations: session management, message history, short/long-term memory with relevance scoring, and context assembly. Serves as the entry point for all user interactions. Architecture: `User → Conversation API → Security → Session Manager → Message Manager → Context Builder → Memory Manager → Kafka Event → Monitoring → Response`. 17 endpoints. Events: `SessionCreated`, `SessionClosed`, `MessageSent`, `MemoryStored`, `ContextRefreshed`. Redis caching (Session 30m, Messages 15m, Context 10m).

### 2.10 Assistant Platform (assistant module)
**Doc:** `docs/ai-platform/business-assistants.md`
**Package:** `com.sporekart.ai.assistant`

Twelve domain copilots (Customer, Product, Training, Grower, Marketplace, ERP, Inventory, Order, Analytics, Support, Administration, Notification) that route/orchestrate to business modules. Four-stage pipeline: `Intent Resolution → Task Planning → Copilot Execution → Response`, coordinated by `AssistantOrchestrator`. Copilots contain no business logic (thin delegation). Keyword-based intent classification; stub responses in initial release; rate limited (30 req/min). 10 Kafka events to `assistant-events`.

### 2.11 Provider Registry (new provider catalog)
**Doc:** `docs/architecture/provider-registry.md`
**Module:** `provider-registry` (`com.sporekart.ai.providerregistry`)

Authoritative, runtime-queryable catalog of every integrated provider. Carries metadata (`providerId`, `providerType`, `tags`), priority + status (`ACTIVE/INACTIVE/MAINTENANCE/DEPRECATED/DECOMMISSIONED`), supported models (family, context/max tokens, informational cost rates), capability matrix (streaming, tool calling, embeddings, image, reserved audio), health records, semantic version, declarative fallback chain (`primary → secondary → tertiary`), deprecation/sunset metadata, and lifecycle. The Gateway's `ProviderResolver` consults it for priority, capabilities, health, and fallback. Integration: consumed by Gateway, Capability Discovery, Event Catalog, Usage Tracking (tags every record with `providerId`/`modelId`), API Registry.

### 2.12 Capability Discovery (new discovery service)
**Doc:** `docs/architecture/capability-discovery.md`
**Module:** `capability-discovery` (`com.sporekart.ai.capabilitydiscovery`)

Runtime service answering "what can the platform do right now, on which provider, with what dependencies?" without hardcoding. Aggregates the Provider Registry capability matrix + Global Config feature-flag state into a single discovery surface. Exposes capability records (with `supported` level, category, constraints, `sinceVersion`, `deprecationNotice`), resolve-by-need API, dependency edges, and **health-aware availability**. Reserved capabilities (audio, structured-output) appear as `UNSUPPORTED` and auto-promote when their feature flag enables and a compatible provider registers. Used by the Gateway for capability-aware routing and fallback.

### 2.13 Usage Tracking (new cost foundation)
**Doc:** `docs/architecture/usage-tracking.md`
**Module:** `usage-tracking` (`com.sporekart.ai.usagetracking`)

Platform-wide, **append-only** record of AI consumption: requests/responses, provider/model usage (linked to Provider Registry), prompt/completion token counts, execution latency, failures by type, and daily/monthly rollups. Cost is captured only as a non-financial `estimatedCost` indicator derived from Provider Registry informational rates. **Scope boundary:** no billing/invoicing/quota-enforcement logic; billing is an explicit future extension (`BillingExtension` port reserved). Emits `UsageRecorded` / `UsageRollupComputed`; consumed by Governance Analytics for KPIs.

### 2.14 Monitoring module
**Doc:** `docs/ai-platform/ai-platform-overview.md`
**Package:** `com.sporekart.ai.monitoring`

Planned (Part 4) usage monitoring and cost management module. Provides the 3 interfaces + 1 config contract in the module map. Instrumentation across the platform is realized via Micrometer (counters, timers, error tracking) and the Usage Tracking registry; the monitoring module itself is the planned consolidation layer.

---

## 3. End-to-End Integration Flow

The confirmed request path (from `docs/architecture/governance-platform.md` and `docs/sprints/phase-4/final-hardening.md`):

```
Business Module
   → Conversation Platform      (session, context assembly, memory)
   → Workflow Platform          (orchestration, task planning, step execution)
   → Governance Pipeline        (Foundation → Policy → Decision → Approval →
                                 Compliance → Risk → Analytics → Admin → Automation)
   → Registries (discovery/metadata only):
        Provider Registry, Prompt Registry, Knowledge Registry,
        Usage Tracking, Capability Discovery, Global Config Registry,
        Event Catalog, API Registry
   → Prompt Platform            (resolve versioned template)
   → Knowledge Platform         (retrieve context)
   → Semantic Platform          (embed + hybrid search + rank)
   → AI Gateway                 (validate, feature-check, rate-limit, resolve provider)
   → Provider Abstraction      (selector + failover → adapter)
   → Provider (external)        (Gemini/OpenAI/Claude/...)
   → Response (back up the chain)
```

**Registries as discovery/metadata layers:** The hardening registries are read-optimized, cache-first catalogs. They are *consulted* by the gateway and platforms (provider selection, capability negotiation, prompt/version resolution, source/sync status, usage accounting) but they do not sit in the synchronous execution hot path as stateful intermediaries. They are emitted to and consumed from via Kafka (`*-events` topics) and Redis cache namespaces, keeping them eventually consistent and decoupled.

---

## 4. Integration Map

| Source | Consults / Emits | Target | Mechanism |
|--------|------------------|--------|-----------|
| Conversation | routes request; emits session/message events | Workflow, Gateway | REST, Kafka (`conversation-events`) |
| Workflow | orchestrates execution steps | Governance, Gateway | REST, Kafka |
| Gateway | resolves provider, prompt, capabilities | Provider Registry, Prompt Registry, Capability Discovery | REST, Redis |
| Gateway | dispatches execution | Provider Abstraction (adapters) | in-process interface |
| Provider Abstraction | reports health/usage | Provider Registry, Usage Tracking | Kafka, Redis |
| Prompt Platform | resolves template + version | Prompt Registry | in-process / registry |
| Knowledge Platform | provides chunks/context | Semantic Platform | in-process |
| Semantic Platform | selects embedding provider | Provider Registry, Capability Discovery | Redis, REST |
| Semantic Platform | visibility filter | Knowledge Platform | in-process |
| Capability Discovery | aggregates capability matrix | Provider Registry, Global Config Registry | cache-first read |
| Usage Tracking | tags every execution | Provider Registry (`providerId`/`modelId`) | event + lookup |
| Usage Tracking | feeds KPIs | Governance Analytics | Kafka (`analytics-events`) |
| All registries | publish state changes | Event Catalog / API Registry | Kafka (`*-events`) |

---

## 5. Architecture Conformance

- **Modular monolith / Spring Modulith:** confirmed; module packages are isolated with ArchUnit boundary rules (gateway, provider, prompt, semantic, conversation, content, workflow, monitoring, assistant, and the five registries).
- **Hexagonal per module:** each module follows `api / domain / application / infrastructure / interfaces / config` layering.
- **Observability:** Micrometer + Prometheus, structured logging, correlation-ID propagation across all modules.
- **Security:** RBAC (AI_ADMINISTRATOR … AI_VIEWER), RFC 9457 envelopes, input validation, audit trails, secrets masked (no keys in source; `is_secret` column).
- **Eventing:** Kafka topics per module; registries and platforms publish to their respective `*-events` topics.
- **Caching:** Redis namespaces per module with module-specific TTL strategies.

---

## 6. Findings (non-blocking)

1. **Provider adapters are stubs.** All 8 adapters return stub responses; no real provider SDK calls exist. This is by design for the current phase but means live provider behavior (latency, failure modes, token accounting) is unverified end-to-end. *Recommendation: track real-adapter enablement as a follow-up milestone.*
2. **Capability Discovery availability is eventually consistent.** It relies on cached health/feature-flag state. A provider that becomes unhealthy between cache refreshes could still be reported available. *Acceptable per ADR-010; recommend documenting the cache TTL and refresh trigger.*
3. **Monitoring module still marked "Planned."** The capability matrix lists `monitoring` as Planned (Part 4) while Usage Tracking (a hardening registry) already provides usage/cost accounting. Risk of overlapping scope between the planned `monitoring` module and the delivered `usage-tracking` registry. *Recommendation: clarify the boundary — Usage Tracking is append-only records; `monitoring` should be the alerting/dashboard consolidation layer.*
4. **Knowledge/Conversation/Semantic modules show mixed "Planned"/"Implemented" status** across docs. The overview marks them Planned while dedicated architecture docs describe full designs. *Recommendation: reconcile status labels so certification records reflect actual implementation state.*
5. **Assistant copilots are stubs** and keyword-based (no NLU/LLM). Intent resolution accuracy depends on keyword coverage; ambiguous intents return `AMBIGUOUS`. *Acceptable for initial release; note as a known limitation.*
6. **Usage Tracking explicitly excludes billing/quota enforcement.** Metering for future quota or charging must be built on the reserved `BillingExtension` port. *No action required; boundary is intentional (ADR-007).*
7. **No streaming in Assistant / Conversation** initial releases (SSE foundation exists in Conversation; Assistant is synchronous). *Documented limitation; non-blocking.*
8. **Registries are read-optimized and boundary-isolated** — verified via documented architecture tests per registry. No cross-registry write coupling observed.

---

## 7. Certification Statement

The AI Platform demonstrates a coherent, well-bounded architecture with a clear central abstraction (AI Gateway), a complete provider abstraction layer, an existing prompt platform augmented by a versioned prompt registry, knowledge + semantic retrieval, conversation and assistant entry points, workflow orchestration, and five hardening registries serving as discovery/metadata layers. The integration flow (business request → conversation → workflow → gateway → provider abstraction → response) is confirmed, with registries correctly positioned as consulted catalogs rather than hot-path intermediaries.

**Step 7 result: PASS (with non-blocking findings).** The platform is certified for progression to the governance-platform review (Step 8) and subsequent gate stages, subject to tracking the non-blocking findings above.
