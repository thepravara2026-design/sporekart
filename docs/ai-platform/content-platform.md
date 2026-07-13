# Content Platform

**Module:** ai-service
**Package:** `com.sporekart.ai.content`

---

## Overview

The Content Platform provides enterprise-grade AI-powered content operations including generation, summarization, translation, classification, moderation, SEO optimization, and recommendations. It orchestrates content through a pipeline architecture, delegating AI execution to the AI Gateway while leveraging Prompt, Knowledge, Semantic, and Conversation platforms for context enrichment.

---

## Modules

### Content Generation
Generates AI content from templates and parameters. Supports multiple content types (ARTICLE, BLOG, PRODUCT_DESC, SEO_META, SOCIAL_POST, EMAIL, AD_COPY, LANDING_PAGE, REVIEW) and output formats (PLAIN_TEXT, MARKDOWN, HTML, JSON, RICH_TEXT). Configurable tone, word count, and language.

### Summarization
Extractive and abstractive summarization of existing content. Supports configurable summary length (short/medium/long) and format preservation.

### Translation
Multi-language translation using AI providers. Supports 50+ languages with automatic language detection and confidence scoring.

### Classification
Content categorization, sentiment analysis, topic extraction, and entity recognition. Returns confidence scores and hierarchical category assignments.

### Moderation
Content safety and policy enforcement. Detects offensive, hateful, harmful content and spam. Configurable severity thresholds and action policies (ALLOW, FLAG, BLOCK, REVIEW).

### SEO Optimization
Generates SEO metadata (title, meta description, keywords, slug, canonical URL) and Open Graph tags. Provides SEO score and optimization suggestions.

### Recommendation
Content recommendation engine supporting multiple strategies (POPULARITY, SIMILARITY, PERSONALIZED, TRENDING, MIXED). Returns ranked content recommendations with relevance scores.

### Templates
Centralized content template management. Templates define structure, variables, and formatting for content generation. Templates are resolved via the Prompt Platform for safe rendering.

---

## Configuration Reference

```yaml
sporekart:
  content:
    enabled: true
    caching: true
    audit: true
    generation:
      default-type: ARTICLE
      default-format: MARKDOWN
      max-word-count: 5000
      min-word-count: 50
      default-temperature: 0.7
      timeout-ms: 30000
    summarization:
      max-input-length: 10000
      default-ratio: 0.3
    translation:
      max-input-length: 5000
      default-source-language: auto
    moderation:
      severity-threshold: OFFENSIVE
      default-action: FLAG
      auto-block: false
    seo:
      max-title-length: 70
      max-description-length: 160
      max-keywords: 10
    recommendations:
      default-strategy: MIXED
      max-results: 10
      min-score: 0.3
    rate-limit:
      generation: 50
      other: 100
      window-ms: 60000
    cache-ttl:
      template: 1800
      generation: 3600
      classification: 1800
      seo: 3600
      recommendations: 900
```

---

## Redis Caching Strategy

| Namespace | Key Pattern | TTL | Strategy |
|-----------|-------------|-----|----------|
| Templates | `content:template:{id}` | 30 min | Cache-aside |
| Generations | `content:generation:{hash}` | 60 min | Write-through |
| Classifications | `content:classification:{id}` | 30 min | Cache-aside |
| SEO Data | `content:seo:{contentId}` | 60 min | Cache-aside |
| Recommendations | `content:recommendations:{strategy}:{contentId}` | 15 min | Cache-aside |

---

## Kafka Events Reference

| Event | Key | Payload Summary |
|-------|-----|-----------------|
| ContentGenerated | contentId | type, format, wordCount, status, latencyMs |
| ContentSummarized | contentId | sourceLength, summaryLength, ratio, latencyMs |
| ContentTranslated | contentId | sourceLang, targetLang, confidence, latencyMs |
| ContentClassified | contentId | categories, sentiment, confidence, entities |
| ContentModerated | contentId | severity, action, confidence, categories |
| ContentSeoOptimized | contentId | seoScore, suggestions, latencyMs |
| ContentRecommended | contentId | strategy, resultCount, latencyMs |
| TemplateCreated | templateId | type, format, version, category |
| TemplateUpdated | templateId | type, format, version, category |
| TemplateDeleted | templateId | type, format, version |

All events published to `content-events` topic (3 partitions, 1 replica).

---

## Monitoring Metrics

| Metric | Type | Tags |
|--------|------|------|
| `content.generation.latency` | Timer | type, format, status |
| `content.summarization.latency` | Timer | type, ratio, status |
| `content.translation.latency` | Timer | sourceLang, targetLang, status |
| `content.classification.latency` | Timer | type, status |
| `content.moderation.latency` | Timer | severity, action |
| `content.seo.latency` | Timer | status |
| `content.generation.count` | Counter | type, format, status |
| `content.moderation.flagged` | Counter | severity |
| `content.moderation.blocked` | Counter | severity |
| `content.cache.hit` | Counter | namespace |
| `content.cache.miss` | Counter | namespace |
| `content.pipeline.steps` | Timer | stepName |
