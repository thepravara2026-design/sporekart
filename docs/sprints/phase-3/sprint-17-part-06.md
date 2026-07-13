# Sprint 17 — Part 6: Enterprise Semantic Intelligence Platform

**Date:** 2026-07-12
**Module:** ai-service
**Lead:** Enterprise AI Platform Engineering Team

---

## Objective

Build the Enterprise Semantic Intelligence Platform — the centralized system for generating embeddings, managing vector indexes, performing semantic and hybrid search, ranking results, and caching semantic queries. All business modules MUST use the Semantic Platform for vector-based semantic retrieval. No direct access to embedding providers or vector stores.

---

## Deliverables

### Semantic Platform Modules

| Module | Package | Purpose |
|--------|---------|---------|
| semantic-core | `com.sporekart.ai.semantic.domain` | Domain records, enums, value objects |
| semantic-embedding | `com.sporekart.ai.semantic.application` | Embedding generation & abstraction |
| semantic-index | `com.sporekart.ai.semantic.application` | Vector index management |
| semantic-search | `com.sporekart.ai.semantic.application` | Semantic search engine |
| semantic-ranking | `com.sporekart.ai.semantic.application` | Result ranking & re-ranking |
| semantic-retrieval | `com.sporekart.ai.semantic.application` | Context retrieval pipeline |
| semantic-cache | `com.sporekart.ai.semantic.infrastructure.cache` | Redis caching layer |
| semantic-monitoring | `com.sporekart.ai.semantic.infrastructure.monitoring` | Metrics & observability |
| semantic-security | `com.sporekart.ai.semantic.application` | Embedding-level security |
| semantic-adapters | `com.sporekart.ai.semantic.infrastructure.adapters` | Embedding provider adapters |

---

### Embedding Framework

The embedding framework provides a provider-agnostic abstraction for generating vector embeddings.

| Component | Responsibility |
|-----------|---------------|
| `EmbeddingService` | Primary API for embedding operations |
| `EmbeddingGenerator` | Interface for provider-specific generation |
| `EmbeddingProviderAdapter` | Abstract base for provider adapters |
| `OpenAIEmbeddingAdapter` | OpenAI text-embedding-3-small/large |
| `GeminiEmbeddingAdapter` | Google Gemini embedding-001 |
| `EmbeddingBatchProcessor` | Batch embedding with progress tracking |
| `EmbeddingValidator` | Validates embedding dimensions & quality |
| `EmbeddingNormalizer` | Normalizes vectors to unit length |
| `EmbeddingCacheService` | Caches embeddings in Redis |

**Supported Models:**

| Provider | Model | Dimensions | Max Input |
|----------|-------|------------|-----------|
| OpenAI | text-embedding-3-small | 1536 | 8192 tokens |
| OpenAI | text-embedding-3-large | 3072 | 8192 tokens |
| Gemini | embedding-001 | 768 | 3072 tokens |

### Vector Index

The vector index manages pgvector-based indexes for efficient similarity search.

| Component | Responsibility |
|-----------|---------------|
| `VectorIndexService` | Create, rebuild, drop indexes |
| `VectorIndexManager` | Index lifecycle management |
| `IndexConfigValidator` | Validates index configuration |
| `HnswIndexBuilder` | Builds HNSW indexes |
| `IvfFlatIndexBuilder` | Builds IVFFlat indexes |
| `IndexRebuildScheduler` | Scheduled index rebuilds |

**Index Types:**

| Type | Algorithm | Use Case | Build Time |
|------|-----------|----------|------------|
| HNSW | Hierarchical Navigable Small Worlds | High-performance search | Slow build, fast query |
| IVFFlat | Inverted File with Flat | Balanced performance | Fast build, moderate query |

### Semantic Search

Six distinct search types supported:

| Search Type | Description | Use Case |
|-------------|-------------|----------|
| `SEMANTIC` | Pure embedding similarity | Conceptual matching |
| `KEYWORD` | BM25 keyword scoring | Exact term matching |
| `HYBRID` | Weighted combination of semantic + keyword | Best overall results |
| `CROSS_ENCODER` | Neural cross-encoder re-ranking | High-precision matching |
| `MULTI_VECTOR` | Multiple query vectors per document | Complex multi-concept queries |
| `CONTEXTUAL` | Search with surrounding context awareness | Document-level understanding |

### Ranking Engine

| Component | Responsibility |
|-----------|---------------|
| `RankingService` | Primary ranking API |
| `SimilarityScorer` | Cosine/Euclidean/Dot product scoring |
| `HybridScorer` | Weighted combination scoring |
| `CrossEncoderScorer` | Cross-encoder re-ranking |
| `ReciprocalRankFusion` | RRF fusion for hybrid results |
| `ScoreNormalizer` | Normalizes scores to 0-1 range |

### Hybrid Search Strategy

```
Query
  ├──→ Embedding Generation ──→ Vector Similarity ──→
  └──→ Keyword Extraction  ──→ BM25 Scoring     ──→
       ↓
  Reciprocal Rank Fusion (RRF)
       ↓
  Cross-Encoder Re-ranking (optional)
       ↓
  Final ranked results
```

- Default RRF constant k = 60
- Configurable semantic/keyword weight ratio (default: 0.7 / 0.3)
- Minimum threshold filtering before fusion
- Optional cross-encoder re-ranking for top-N results

### Database Tables (Flyway V15)

| Table | Purpose |
|-------|---------|
| `semantic_embeddings` | Stores all generated embeddings |
| `semantic_vector_index` | Vector index metadata registry |
| `semantic_search_history` | Search query & result log |
| `semantic_similarity_scores` | Pre-computed similarity pairs |
| `semantic_embedding_jobs` | Batch embedding job tracking |
| `semantic_index_statistics` | Index performance statistics |

### REST APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/semantic/embed` | Generate embeddings |
| POST | `/api/v1/semantic/search` | Execute semantic/hybrid search |
| POST | `/api/v1/semantic/similarity` | Compute similarity between texts |
| GET | `/api/v1/semantic/index` | List vector indexes |
| POST | `/api/v1/semantic/index/rebuild` | Rebuild a vector index |
| GET | `/api/v1/semantic/statistics` | Get index/search statistics |
| GET | `/api/v1/semantic/health` | Health check |

### Redis Caching

| Cache | Prefix | TTL | Strategy |
|-------|--------|-----|----------|
| Embedding | `semantic:embed:{hash}` | 24h | Write-through |
| Search Results | `semantic:search:{hash}` | 5 min | Cache-aside |
| Similarity | `semantic:sim:{hash}` | 1h | Cache-aside |
| Index Metadata | `semantic:idx:{name}` | 10 min | Refresh-ahead |
| Statistics | `semantic:stats` | 5 min | Refresh-ahead |

### Kafka Events (topic: `semantic-events`)

- `SemanticEmbeddingGenerated`
- `SemanticEmbeddingBatchCompleted`
- `SemanticSearchExecuted`
- `SemanticHybridSearchExecuted`
- `SemanticIndexCreated`
- `SemanticIndexRebuildStarted`
- `SemanticIndexRebuildCompleted`
- `SemanticSimilarityComputed`
- `SemanticCacheInvalidated`

### Security

| Concern | Implementation |
|---------|---------------|
| Embedding Access | Embedding-level RBAC based on source document visibility |
| Index Security | Index-scoped access control |
| Query Auditing | All search queries logged with user context |
| Rate Limiting | Per-user, per-search-type rate limits |
| Input Sanitization | Query content sanitized before embedding |
| Provider Credentials | Managed via Spring Cloud Config / Vault |

### Observability

| Aspect | Implementation |
|--------|---------------|
| Metrics (Micrometer) | `semantic.embed.latency`, `semantic.search.latency`, `semantic.index.size`, `semantic.cache.hit.ratio` |
| Tracing | Distributed tracing via Micrometer Tracing + correlation IDs |
| Logging | Structured JSON logging with search/embed context |
| Health Checks | Provider connectivity, vector store connectivity, index health |
| Alerts | Index rebuild failures, embedding provider degradation, high latency |

---

## Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    REST API Layer                            │
│  POST /api/v1/semantic/embed                                │
│  POST /api/v1/semantic/search                               │
│  POST /api/v1/semantic/similarity                           │
│  GET  /api/v1/semantic/index                                │
│  POST /api/v1/semantic/index/rebuild                        │
│  GET  /api/v1/semantic/statistics                           │
│  GET  /api/v1/semantic/health                               │
└──────────────────────┬──────────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────────┐
│                 Application Service Layer                    │
│  ┌──────────────┐ ┌──────────────┐ ┌──────────────────┐    │
│  │ EmbeddingSvc │ │ SearchSvc    │ │ RankingSvc       │    │
│  └──────┬───────┘ └──────┬───────┘ └───────┬──────────┘    │
│  ┌──────▼───────┐ ┌──────▼───────┐ ┌──────▼──────────┐    │
│  │ IndexSvc     │ │ CacheSvc     │ │ SecuritySvc     │    │
│  └──────────────┘ └──────────────┘ └──────────────────┘    │
└──────────────────────┬──────────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────────┐
│              Infrastructure Layer                            │
│  ┌──────────────────┐ ┌──────────────────┐                  │
│  │ Redis Cache      │ │ Kafka Publisher  │                  │
│  └──────┬───────────┘ └──────┬───────────┘                  │
│  ┌──────▼────────────────────▼───────────┐                  │
│  │         Adapter Layer                  │                 │
│  │  OpenAI │ Gemini │ pgvector │ Future   │                 │
│  └────────────────────────────────────────┘                 │
└──────────────────────┬──────────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────────┐
│              PostgreSQL + pgvector (Supabase)                │
│  semantic_embeddings │ semantic_vector_index │ ...          │
└─────────────────────────────────────────────────────────────┘
```

### Package Structure

```
com.sporekart.ai.semantic/
  api/                    — Port interfaces
  application/            — Service layer (embedding, search, ranking, retrieval)
  domain/                 — Domain records, enums, value objects
  config/                 — Configuration
  infrastructure/
    persistence/           — Entities, Repositories
    cache/                — Redis cache service
    monitoring/           — Metrics, health checks
    adapters/             — Embedding provider adapters
  interfaces/
    rest/                 — Controller
      dto/                — Request/response records
```

---

## Test Coverage

| Test Class | Tests |
|------------|-------|
| EmbeddingServiceTest | 8 |
| EmbeddingGeneratorTest | 6 |
| EmbeddingBatchProcessorTest | 5 |
| EmbeddingCacheServiceTest | 4 |
| VectorIndexServiceTest | 7 |
| VectorIndexManagerTest | 5 |
| SearchServiceTest | 10 |
| HybridSearchServiceTest | 7 |
| RankingServiceTest | 6 |
| SimilarityScorerTest | 5 |
| HybridScorerTest | 4 |
| CrossEncoderScorerTest | 3 |
| ReciprocalRankFusionTest | 3 |
| SemanticControllerTest | 10 |
| SemanticSecurityServiceTest | 6 |
| SemanticMonitoringServiceTest | 4 |
| IndexRebuildSchedulerTest | 3 |
| **Total** | **96** |

---

## Acceptance Criteria

✓ Enterprise Semantic Intelligence Platform implemented
✓ Embedding framework with provider abstraction implemented
✓ OpenAI and Gemini embedding adapters implemented
✓ pgvector vector index management implemented
✓ Semantic search (6 types) implemented
✓ Hybrid search with RRF fusion implemented
✓ Ranking engine with multi-scorer support implemented
✓ REST APIs implemented
✓ Flyway V15 migration executed
✓ Redis caching configured for embeddings and search
✓ Kafka semantic events published
✓ Security controls implemented
✓ Observability metrics and health checks implemented
✓ Documentation updated
✓ Changelog updated
✓ Implementation log updated
✓ 96 tests passing with 90% coverage target
✓ Existing application unaffected
