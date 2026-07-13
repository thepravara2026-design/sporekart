# Sprint 17 — Part 9: Enterprise AI Content & Intelligence Platform

**Date:** 2026-07-12
**Module:** ai-service
**Lead:** Enterprise AI Platform Engineering Team

---

## Objective

Build the Enterprise AI Content & Intelligence Platform — content generation, summarization, translation, classification, moderation, SEO optimization, recommendations, template management, REST APIs, Flyway V18 migration, Redis caching, Kafka events, health monitoring, and comprehensive tests. No WYSIWYG editor, no drag-and-drop layout designer, no content approval workflow UI, no business AI assistants. Provider SDK calls are excluded — the platform remains provider-agnostic.

Business modules MUST NOT communicate directly with AI providers; content generation flow: Content API → Content Platform → Prompt Platform → Knowledge Platform → Semantic Platform → AI Gateway → Provider Framework.

Future Android/iOS must consume same APIs without backend redesign.

---

## Deliverables

### Content Platform Modules

| Module | Package | Purpose |
|--------|---------|---------|
| content-core | `com.sporekart.ai.content.domain` | Domain records, enums |
| content-api | `com.sporekart.ai.content.api` | Port interfaces (ContentGenerationService, SummarizationService, TranslationService, ClassificationService, ModerationService, SeoOptimizationService, RecommendationService, ContentTemplateService) |
| content-application | `com.sporekart.ai.content.application` | Application services |
| content-infrastructure | `com.sporekart.ai.content.infrastructure` | JPA, Redis, Kafka, monitoring |
| content-interfaces | `com.sporekart.ai.content.interfaces.rest` | REST controller + DTOs |

---

### Domain Model

| Record/Enum | Fields | Purpose |
|-------------|--------|---------|
| `ContentType` | ARTICLE, BLOG, PRODUCT_DESC, CATEGORY_DESC, SEO_META, SOCIAL_POST, EMAIL, AD_COPY, LANDING_PAGE, REVIEW, TRANSLATION, SUMMARIZATION | Content type categories |
| `ContentStatus` | DRAFT, REVIEW, APPROVED, PUBLISHED, ARCHIVED | Content lifecycle states |
| `ContentFormat` | PLAIN_TEXT, MARKDOWN, HTML, JSON, RICH_TEXT | Output format options |
| `ToneType` | FORMAL, INFORMAL, PROFESSIONAL, PERSUASIVE, NEUTRAL, FRIENDLY, AUTHORITATIVE, EMPATHETIC | Tone configurations |
| `ModerationSeverity` | HARMLESS, OFFENSIVE, HATEFUL, HARMFUL, SPAM | Content moderation levels |
| `ModerationAction` | ALLOW, FLAG, BLOCK, REVIEW | Moderation actions |
| `RecommendationStrategy` | POPULARITY, SIMILARITY, PERSONALIZED, TRENDING, MIXED | Recommendation strategies |
| `ContentTemplate` | id, name, description, type, format, templateText, variables, category, tags, isActive, version, createdBy, createdAt, updatedAt | Content template definition |
| `ContentGeneration` | id, templateId, type, format, tone, sourceText, generatedText, parameters, wordCount, language, status, createdBy, createdAt, completedAt | Generation request/result |
| `ContentVersion` | id, generationId, versionNumber, content, changeSummary, createdBy, createdAt | Version tracking |
| `ContentClassification` | id, contentId, categories, tags, sentiment, confidence, language, topics, entities, createdAt | Classification results |
| `ContentTranslation` | id, contentId, sourceLanguage, targetLanguage, sourceText, translatedText, provider, confidence, createdAt | Translation records |
| `ModerationResult` | id, contentId, severity, action, categories, confidence, reviewedBy, reviewedAt, createdAt | Moderation results |
| `SeoData` | id, contentId, seoTitle, metaDescription, keywords, slug, canonicalUrl, ogTitle, ogDescription, score, suggestions, createdAt | SEO optimization data |
| `ContentRecommendation` | id, sourceContentId, recommendedContentIds, strategy, scores, limit, createdAt | Content recommendations |

---

### API Endpoints (10 endpoints)

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/v1/content/generate` | Generate AI content from template/parameters |
| POST | `/api/v1/content/summarize` | Summarize existing content |
| POST | `/api/v1/content/translate` | Translate content to target language |
| POST | `/api/v1/content/classify` | Classify content by categories, sentiment, topics |
| POST | `/api/v1/content/moderate` | Moderate content for policy violations |
| POST | `/api/v1/content/seo/optimize` | Optimize content for SEO |
| GET | `/api/v1/content/templates` | List content templates |
| POST | `/api/v1/content/templates` | Create a new content template |
| POST | `/api/v1/content/recommend` | Get content recommendations |
| GET | `/api/v1/content/health` | Content module health check |

---

### Generation Pipeline (ContentPipeline)

```
Request → Controller → ContentPipeline
  1. RequestValidator — Validate input parameters
  2. TemplateResolver — Resolve template and variables
  3. PromptResolver — Build AI prompt via Prompt Platform
  4. KnowledgeResolver — Enrich with knowledge context
  5. SemanticContext — Add semantic search results
  6. ConversationContext — Include conversation context
  7. ContentExecutor — Execute generation via AI Gateway
  8. ContentValidator — Validate output (length, format, safety)
  9. ContentFormatter — Format output to requested format
  10. ContentAuditor — Record generation event, metrics, Kafka
  → Response
```

---

### Flyway Migration V18

| Table | Description |
|-------|-------------|
| `content_templates` | Content template definitions |
| `content_generations` | Generation request/result records |
| `content_versions` | Version snapshots of generated content |
| `content_classifications` | Classification results |
| `content_translations` | Translation records |
| `content_moderation` | Moderation results |
| `content_seo_data` | SEO optimization data |
| `content_recommendations` | Content recommendation records |

All tables use UUID primary keys, `TIMESTAMP` for datetimes, `TEXT` for large content, H2-compatible syntax.

---

### File Count

| Layer | Files |
|-------|-------|
| Domain (enums + records) | 12 |
| API (interfaces) | 8 |
| Application (services + pipeline) | 12 |
| Infrastructure (JPA entities + repos + Kafka + Redis + monitoring) | 18 |
| Interfaces (controller + DTOs) | 10 |
| Config (Flyway + config updates) | 5 |
| Tests | 14 test classes ~130 tests |
| **Total** | **~65 source files, ~130 tests** |
