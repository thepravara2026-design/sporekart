# Content Intelligence Platform Architecture

**Version:** 1.0.0
**Last Updated:** 2026-07-12
**Module:** ai-service

---

## Overview

The Content Intelligence Platform provides enterprise-grade AI content generation, summarization, translation, classification, moderation, SEO optimization, and recommendation capabilities for the SporeKart AI Platform. It orchestrates content operations through a pipeline architecture, delegating AI execution to the AI Gateway while leveraging Prompt, Knowledge, Semantic, and Conversation platforms for context enrichment.

---

## Architecture Principles

1. **Pipeline Architecture** — All content operations flow through a consistent pipeline (validate → resolve → enrich → execute → validate → format → audit)
2. **Platform Delegation** — Content Platform delegates AI execution to AI Gateway; never calls providers directly
3. **Context Enrichment** — Every generation enriches prompts with knowledge base data, semantic search results, and conversation context
4. **Template-Driven** — All content generation uses managed templates from the Prompt Platform
5. **Caching First** — Templates and generation results are cached in Redis to reduce latency and cost
6. **Event-Driven** — All content lifecycle events published to Kafka for downstream consumers
7. **Multi-Format** — Supports PLAIN_TEXT, MARKDOWN, HTML, JSON, and RICH_TEXT output formats

---

## Layer Diagram

```
┌──────────────────────────────────────────────────────────────────────────┐
│                         Content API Layer                                 │
│        /api/v1/content/* (10 endpoints)                                   │
│   generate │ summarize │ translate │ classify │ moderate │ seo/optimize  │
│   templates │ recommend │ health                                          │
└───────────────────────────────┬──────────────────────────────────────────┘
                                │
┌───────────────────────────────▼──────────────────────────────────────────┐
│                      Content Application Layer                             │
│                                                                           │
│  ┌──────────────────────────────────────────────────────────────────┐    │
│  │                      Content Generation Pipeline                   │    │
│  │  RequestValidator → TemplateResolver → PromptResolver →           │    │
│  │  KnowledgeResolver → SemanticContext → ConversationContext →      │    │
│  │  ContentExecutor → ContentValidator → ContentFormatter →          │    │
│  │  ContentAuditor                                                    │    │
│  └──────────────────────────────────────────────────────────────────┘    │
│                                                                           │
│  ┌────────────┐ ┌──────────────┐ ┌────────────┐ ┌──────────────────┐    │
│  │ Summarize  │ │  Translate   │ │  Classify  │ │  Moderate        │    │
│  │ Service    │ │  Service     │ │  Service   │ │  Service         │    │
│  └────────────┘ └──────────────┘ └────────────┘ └──────────────────┘    │
│  ┌────────────┐ ┌──────────────┐ ┌──────────────┐                        │
│  │ SEO Opt    │ │  Recommend   │ │  Template    │                        │
│  │ Service    │ │  Service     │ │  Service     │                        │
│  └────────────┘ └──────────────┘ └──────────────┘                        │
└───────────────────────────────┬──────────────────────────────────────────┘
                                │
┌───────────────────────────────▼──────────────────────────────────────────┐
│                      Content Domain Layer                                  │
│  ContentType, ContentStatus, ContentFormat, ToneType, ModerationSeverity  │
│  ContentTemplate, ContentGeneration, ContentVersion, ContentClassification│
│  ContentTranslation, ModerationResult, SeoData, ContentRecommendation    │
└───────────────────────────────┬──────────────────────────────────────────┘
                                │
┌───────────────────────────────▼──────────────────────────────────────────┐
│                    Content Infrastructure Layer                            │
│  ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────────┐   │
│  │  JPA Repositories │  │  Redis Cache     │  │  Kafka Publisher    │   │
│  │  (8 repositories) │  │  (5 namespaces)  │  │  (10 event types)   │   │
│  └──────────────────┘  └──────────────────┘  └──────────────────────┘   │
│  ┌──────────────────┐  ┌──────────────────┐                              │
│  │  Flyway V18      │  │  Monitoring      │                              │
│  │  (8 tables)      │  │  (Micrometer)    │                              │
│  └──────────────────┘  └──────────────────┘                              │
└───────────────────────────────┬──────────────────────────────────────────┘
                                │
                                ▼
              Prompt Platform → Knowledge Platform →
              Semantic Platform → Conversation Platform →
              AI Gateway → Provider Framework
```

---

## Generation Pipeline Flow

```
Request → ContentController.generate()
  │
  ▼
┌──────────────────────────────────────────────────────────────────────────┐
│ 1. RequestValidator                                                        │
│    - Validate content type, format, parameters                             │
│    - Check feature flags                                                   │
│    - Rate limit check (50 req/min per user)                                │
└────────────────────────────────┬─────────────────────────────────────────┘
                                 │
┌────────────────────────────────▼─────────────────────────────────────────┐
│ 2. TemplateResolver                                                        │
│    - Resolve template from templateId or default by content type           │
│    - Load template from Redis cache or Prompt Platform                     │
│    - Merge user parameters with template variables                         │
└────────────────────────────────┬─────────────────────────────────────────┘
                                 │
┌────────────────────────────────▼─────────────────────────────────────────┐
│ 3. PromptResolver                                                          │
│    - Build AI prompt using resolved template and parameters                │
│    - Delegate to Prompt Platform for safe template rendering               │
│    - Apply tone and format instructions                                    │
└────────────────────────────────┬─────────────────────────────────────────┘
                                 │
┌────────────────────────────────▼─────────────────────────────────────────┐
│ 4. KnowledgeResolver                                                       │
│    - Query Knowledge Platform for relevant documents                       │
│    - Filter by category and business module                                │
│    - Attach knowledge context to prompt                                    │
└────────────────────────────────┬─────────────────────────────────────────┘
                                 │
┌────────────────────────────────▼─────────────────────────────────────────┐
│ 5. SemanticContext                                                         │
│    - Query Semantic Platform for related content via vector search         │
│    - Add semantic context to prompt                                        │
│    - Boost relevance with top-K results                                    │
└────────────────────────────────┬─────────────────────────────────────────┘
                                 │
┌────────────────────────────────▼─────────────────────────────────────────┐
│ 6. ConversationContext                                                     │
│    - Include conversation history if sessionId provided                    │
│    - Query Conversation Platform for memory and context                    │
│    - Fuse context into generation prompt                                   │
└────────────────────────────────┬─────────────────────────────────────────┘
                                 │
┌────────────────────────────────▼─────────────────────────────────────────┐
│ 7. ContentExecutor                                                         │
│    - Send assembled prompt to AI Gateway                                   │
│    - Configure generation parameters (temperature, maxTokens, etc.)        │
│    - Handle execution timeout and retry                                    │
└────────────────────────────────┬─────────────────────────────────────────┘
                                 │
┌────────────────────────────────▼─────────────────────────────────────────┐
│ 8. ContentValidator                                                        │
│    - Validate output length and format                                     │
│    - Check for empty or nonsensical content                                │
│    - Run moderation check on generated content                             │
└────────────────────────────────┬─────────────────────────────────────────┘
                                 │
┌────────────────────────────────▼─────────────────────────────────────────┐
│ 9. ContentFormatter                                                        │
│    - Format output to requested ContentFormat                              │
│    - Apply markdown/html conversion if needed                              │
│    - Trim whitespace and validate structure                                │
└────────────────────────────────┬─────────────────────────────────────────┘
                                 │
┌────────────────────────────────▼─────────────────────────────────────────┐
│ 10. ContentAuditor                                                         │
│     - Record generation event in content_generations table                 │
│     - Publish Kafka event (ContentGenerated)                               │
│     - Collect Micrometer metrics                                           │
│     - Build response with metadata                                         │
└────────────────────────────────┬─────────────────────────────────────────┘
                                 │
                                 ▼
                        ContentGenerationResponse
```

---

## Integration Architecture

```
┌──────────────────────────────────────────────────────────────────────────┐
│                      Content Intelligence Platform                         │
│                                                                           │
│  Content Generation ↔ Summarization ↔ Translation ↔ Classification      │
│  ↔ Moderation ↔ SEO ↔ Recommendations ↔ Templates                        │
└───────┬──────────────┬──────────────┬──────────────┬────────────────────┘
        │              │              │              │
        ▼              ▼              ▼              ▼
┌──────────────┐ ┌──────────────┐ ┌──────────────┐ ┌──────────────────┐
│   Prompt     │ │  Knowledge   │ │   Semantic   │ │  Conversation    │
│   Platform   │ │  Platform    │ │   Platform   │ │  Platform        │
└──────┬───────┘ └──────┬───────┘ └──────┬───────┘ └───────┬──────────┘
       │                │                │                 │
       └────────────────┴────────────────┴─────────────────┘
                                │
                                ▼
                       ┌────────────────┐
                       │  AI Gateway    │
                       └───────┬────────┘
                               │
                               ▼
                       ┌────────────────┐
                       │   Provider     │
                       │   Framework    │
                       └────────────────┘
```

---

## Content Pipeline Components

| Component | Service | Purpose |
|-----------|---------|---------|
| ContentGenerationService | application | Primary content generation orchestrator |
| SummarizationService | application | Content summarization (extractive/abstractive) |
| TranslationService | application | Multi-language translation |
| ClassificationService | application | Content categorization and tagging |
| ModerationService | application | Content safety and policy enforcement |
| SeoOptimizationService | application | SEO metadata generation and optimization |
| RecommendationService | application | Content recommendation engine |
| ContentTemplateService | application | Template CRUD and resolution |
| ContentPipeline | application | Pipeline orchestration |
| ContentRequestValidator | application | Input validation |
| ContentTemplateResolver | application | Template resolution |
| ContentPromptResolver | application | AI prompt assembly |
| ContentKnowledgeResolver | application | Knowledge enrichment |
| ContentSemanticContext | application | Semantic enrichment |
| ContentConversationContext | application | Conversation enrichment |
| ContentExecutor | application | AI Gateway execution delegation |
| ContentValidator | application | Output validation |
| ContentFormatter | application | Output format conversion |
| ContentAuditor | application | Audit, metrics, events |

---

## Security Architecture

| Concern | Mechanism |
|---------|-----------|
| Input Validation | Content type, format, length validation at pipeline entry |
| Rate Limiting | 50 requests/min per user for generation; 100 req/min for other operations |
| Content Moderation | Pre-generation and post-generation moderation checks |
| Output Sanitization | Strip HTML/script injection patterns from generated content |
| Template Security | Templates resolved via Prompt Platform with injection detection |
| Access Control | Content type and category based on user role and business module |
| Audit Trail | Every generation, moderation, and classification event recorded |

---

## Caching Strategy

| Cache Key Pattern | TTL | Strategy | Purpose |
|---|---|---|---|
| `content:template:{id}` | 30 min | Cache-aside | Content template cache |
| `content:generation:{hash}` | 60 min | Write-through | Generation result cache |
| `content:classification:{id}` | 30 min | Cache-aside | Classification results |
| `content:seo:{contentId}` | 60 min | Cache-aside | SEO optimization data |
| `content:recommendations:{strategy}:{contentId}` | 15 min | Cache-aside | Recommendation results |

---

## Event Catalog

| Event | Topic | Description |
|---|---|---|
| `ContentGenerated` | content-events | Content generation completed |
| `ContentSummarized` | content-events | Content summarized |
| `ContentTranslated` | content-events | Content translated |
| `ContentClassified` | content-events | Content classified |
| `ContentModerated` | content-events | Content moderation completed |
| `ContentSeoOptimized` | content-events | SEO optimization completed |
| `ContentRecommended` | content-events | Recommendations generated |
| `TemplateCreated` | content-events | Content template created |
| `TemplateUpdated` | content-events | Content template updated |
| `TemplateDeleted` | content-events | Content template deleted |

---

## Monitoring Metrics

| Metric | Type | Description |
|--------|------|-------------|
| `content.generation.latency` | Timer | Generation pipeline execution time |
| `content.summarization.latency` | Timer | Summarization execution time |
| `content.translation.latency` | Timer | Translation execution time |
| `content.classification.latency` | Timer | Classification execution time |
| `content.moderation.latency` | Timer | Moderation execution time |
| `content.seo.latency` | Timer | SEO optimization execution time |
| `content.generation.count` | Counter | Total generations by type |
| `content.moderation.flagged` | Counter | Moderated content flagged |
| `content.moderation.blocked` | Counter | Content blocked by moderation |
| `content.cache.hit` | Counter | Cache hits by namespace |
| `content.cache.miss` | Counter | Cache misses by namespace |
| `content.pipeline.steps` | Timer | Per-step pipeline timing |
