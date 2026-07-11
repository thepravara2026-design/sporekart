# Enterprise AI Platform Architecture

**Version:** 1.3.0  
**Last Updated:** 2026-07-11  
**Owner:** Enterprise AI Platform Engineering Team  

---

## Overview

The Enterprise AI Platform is the centralized AI capability layer for SporeKart. Every business module communicates exclusively through this platform — never directly with external AI providers.

---

## Architecture Diagram

```
Business Modules (Catalog, Orders, Inventory, Marketplace, ERP, Training, Support, Analytics)
        │
        ▼
┌───────────────────────────────────────────────────────────────────────────┐
│                          AI Gateway (Implemented Part 2)                   │
│                                                                           │
│  ┌─────────────────────────────────────────────────────────────────────┐  │
│  │                    GatewayPipeline                                    │  │
│  │  Receive Request → Validate → Feature Check → Rate Limit →          │  │
│  │  Resolve Context → Resolve Provider → Execute → Audit →            │  │
│  │  Collect Metrics → Build Response                                    │  │
│  └─────────────────────────────────────────────────────────────────────┘  │
│                                                                           │
│  Kafka Events │ Redis Cache │ Micrometer Metrics │ Audit Log              │
└───────────────────────────────────────────────────────────────────────────┘
        │
        ▼
┌───────────────────────────────────────────────────────────────────────────┐
│                    AI Provider Abstraction Layer (Implemented Part 3)      │
│                                                                           │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐  │
│  │  Gemini  │  │  OpenAI  │  │  Claude  │  │  Azure   │  │  Bedrock │  │
│  │  Adapter │  │  Adapter │  │  Adapter │  │  OpenAI  │  │  Adapter │  │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘  └──────────┘  │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐                               │
│  │  Ollama  │  │  Mistral │  │  Local   │                               │
│  │  Adapter │  │  Adapter │  │ LLM Adap │                               │
│  └──────────┘  └──────────┘  └──────────┘                               │
│                                                                           │
│  Registry │ Factory │ Selector │ Health │ Config │ Validator │ Failover   │
└───────────────────────────────────────────────────────────────────────────┘
        │
        ▼
┌───────────────────────────────────────────────────────────────┐
│                    Provider Abstraction Layer                   │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────────┐  │
│  │  Gemini  │  │  OpenAI  │  │  Claude  │  │   Future     │  │
│  │ Adapter  │  │ Adapter  │  │ Adapter  │  │  Providers   │  │
│  └──────────┘  └──────────┘  └──────────┘  └──────────────┘  │
└───────────────────────────────────────────────────────────────┘
        │
        ▼
External AI Providers (Google, OpenAI, Anthropic)
```

---

## Layered Architecture

Each module follows Hexagonal Architecture:

```
┌──────────────────────────────────────────────────┐
│                   API Layer                       │
│  GatewayController (/api/v1/ai/*)                 │
│  OpenAPI Swagger documentation                    │
├──────────────────────────────────────────────────┤
│                Application Layer                  │
│  GatewayPipeline, GatewayDomainService,           │
│  GatewayApplicationService, GatewayRequestValidator│
│  GatewayResponseBuilder, GatewayContextResolver,  │
│  GatewayAuditService, GatewayMetricsCollector,    │
│  GatewayExceptionTranslator, GatewayFeatureManager│
├──────────────────────────────────────────────────┤
│                 Domain Layer                      │
│  (Models, value objects, domain logic)            │
├──────────────────────────────────────────────────┤
│              Infrastructure Layer                 │
│  (Persistence, providers, external integrations)  │
├──────────────────────────────────────────────────┤
│              Configuration Layer                  │
│  (Spring @Configuration, properties)              │
└──────────────────────────────────────────────────┘
```

---

## Module Boundaries

| Module | Dependency | Responsibility |
|--------|-----------|----------------|
| `core` | None | Shared types, DTOs, exceptions, feature flags |
| `gateway` | core | Single entry point, pipeline, validation, rate limiting, audit, metrics, Kafka events, Redis cache |
| `provider` | core | Provider abstraction (13 interfaces), registry, factory, 8 adapters, selector, failover, health, config, validator, Kafka events, Redis cache |
| `prompt` | core | Template management, versioning, rendering, approval workflow, audit, import/export, Redis cache, Kafka events |
| `rag` | core, search | Knowledge retrieval, context assembly |
| `search` | core | Semantic search, vector store |
| `chat` | core, prompt | Conversations, sessions, history |
| `content` | core, prompt | Content generation, SEO, translation |
| `workflow` | core, prompt, provider | AI pipeline orchestration |
| `monitoring` | core | Metrics, audit, cost tracking |

---

## Key Principles

1. **No provider SDK leakage** — Business modules never see Gemini/OpenAI/Claude SDKs
2. **Feature flags gate all operations** — Every module checks its flag before processing
3. **Event-driven** — AI operations publish events for async processing
4. **Correlation IDs** — Every request carries a traceable correlation ID
5. **Structured logging** — 100% structured, machine-parseable logging
6. **Rate limiting** — Per-module, per-user rate limits enforced at the gateway

---

## Technology Stack

| Component | Technology |
|-----------|-----------|
| Runtime | Java 21 LTS |
| Framework | Spring Boot 3.3.3 |
| Architecture | Spring Modulith |
| Database | PostgreSQL (via Flyway) |
| Cache | Redis |
| Messaging | Apache Kafka |
| API Docs | OpenAPI 3.1 (SpringDoc) |
| Monitoring | Micrometer + Prometheus |
| Security | Spring Security |
