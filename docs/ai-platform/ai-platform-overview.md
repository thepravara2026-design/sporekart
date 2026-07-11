# AI Platform Overview

**Version:** 1.3.0  
**Last Updated:** 2026-07-11  
**Status:** Prompt Management (Sprint 17 Part 4)

---

## Purpose

The Enterprise AI Platform provides centralized AI capabilities to all SporeKart business modules. It abstracts provider complexity, enforces governance, tracks usage, and ensures consistent AI behavior across the organization.

---

## Capabilities

| Capability | Module | Status |
|-----------|--------|--------|
| Provider Routing | gateway | Implemented (Part 2) |
| Request Validation | gateway | Implemented (Part 2) |
| Rate Limiting | gateway | Interface & InMemory (Part 2) |
| Audit & Metrics | gateway | Implemented (Part 2) |
| Kafka Events | gateway | 7 event types (Part 2) |
| Redis Caching | gateway | 4 cache namespaces (Part 2) |
| Gateway Health | gateway | HealthIndicator (Part 2) |
| Provider Abstraction | provider | Implemented (Part 3) |
| Provider Adapters | provider | 8 stub adapters (Part 3) |
| Provider Registry | provider | Registry + Factory (Part 3) |
| Provider Health | provider | Health monitoring (Part 3) |
| Provider Selection | provider | Selector + Failover (Part 3) |
| Provider Events | provider | 6 Kafka event types (Part 3) |
| Prompt Management | prompt | Implemented (Part 4) |
| Chat / Conversations | chat | Planned (Part 4) |
| RAG / Knowledge Retrieval | rag | Planned (Part 4) |
| Semantic Search | search | Planned (Part 4) |
| Content Generation | content | Planned (Part 4) |
| Workflow Automation | workflow | Planned (Part 4) |
| Usage Monitoring | monitoring | Planned (Part 4) |
| Cost Management | monitoring | Planned (Part 4) |

---

## Current State (Part 4)

The AI Gateway (Part 2), Provider Abstraction Layer (Part 3), and Prompt Management Platform (Part 4) are fully implemented:

- 5 REST endpoints under `/api/v1/ai/*`
- Gateway execution pipeline with 9 sequential stages
- Feature flag enforcement at pipeline entry
- Request validation with max prompt length (32000 chars)
- Provider resolution via feature flags
- Retry strategy with exponential backoff (max 3)
- Per-module timeout configuration
- Micrometer metrics (counters, timers, error tracking)
- Structured audit logging
- Kafka event publishing (7 event types, topic `ai-gateway-events`)
- Redis caching (configuration, features, health, execution metadata)
- Flyway V11 migration (3 operational tables)
- Standard RFC 9457 response envelope
- 6 gateway-specific exceptions
- 9 gateway DTOs
- 20 tests across controller, pipeline, and validator
- Provider abstraction layer with 13 core interfaces, 7 application services, 8 adapters
- Provider registry, factory, selector, failover, health service, configuration, validator
- 8 provider adapters (Gemini, OpenAI, Claude, Azure OpenAI, Bedrock, Ollama, Mistral, Local LLM)
- 6 provider REST endpoints under `/api/v1/ai/providers/*`
- 49 provider tests across 8 test classes
- ArchUnit rules enforcing gateway module isolation
- Prompt Management Platform with 17 REST endpoints, 9 application services, 6 JPA repositories
- Prompt Template Engine with safe rendering, variable validation, injection detection
- Prompt Versioning with full lifecycle (DRAFT → PENDING_APPROVAL → APPROVED → PUBLISHED → DEPRECATED → ARCHIVED)
- Prompt Approval Workflow (submit, approve, reject)
- Prompt Audit Trail with 18 action types
- Prompt Import/Export in JSON format
- Redis caching (5 namespaces) and Kafka events (7 event types)
- Flyway V13 migration (6 tables)
- 84 prompt tests across 12 test classes

---

## Module Map

```
com.sporekart.ai
├── core/          28 interfaces + 16 domain records/enums + 5 configs
├── gateway/        3 API interfaces + 9 DTOs + 10 application services + 6 infrastructure + 1 config
├── provider/       4 API interfaces + 6 domain records + 7 application services + 10 infrastructure + 1 config
├── prompt/         9 application services + 6 JPA entities + 6 repositories + 2 infrastructure + 8 DTOs + 1 controller + 1 config + 5 domain models/enums
├── rag/            4 interfaces + 3 domain records + 1 config
├── search/         3 interfaces + 2 domain records + 1 config
├── chat/           3 interfaces + 3 domain records + 1 config
├── content/        2 interfaces + 2 domain records + 1 config
├── workflow/       3 interfaces + 2 domain records + 1 config
└── monitoring/     3 interfaces + 1 config
```

---

## Feature Flags

All feature flags are configured in `application.yml` under `sporekart.ai.features.*`. Flags can be toggled at runtime via `CoreFeatureFlagService`.

---

## Configuration

Platform configuration is managed through Spring `@ConfigurationProperties`:

- `sporekart.ai.*` — Global platform settings
- `sporekart.ai.features.*` — Feature flags
- `sporekart.ai.modules.*` — Per-module settings
- `sporekart.ai.provider.providers.*` — Per-provider settings

---

## Security

Security is managed via:
- `AiAuthorization` — Module-level access control
- `RateLimitContract` — Per-module rate limiting
- `PromptValidationContract` — Input validation and content filtering
- `AuditContract` — Audit trail for all AI operations

---

## Observability

Every AI operation is observable via:
- `ObservabilityContract` — Metrics and counters
- `StructuredLogger` — Structured log output
- `CorrelationIdPropagator` — Distributed tracing
- Micrometer + Prometheus integration
