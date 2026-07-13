# Semantic Intelligence Platform Architecture

**Version:** 1.0.0
**Last Updated:** 2026-07-12
**Module:** ai-service

---

## Overview

The Semantic Intelligence Platform provides enterprise-grade vector embeddings, semantic search, and hybrid retrieval capabilities for the SporeKart AI Platform. It abstracts embedding providers and vector store implementations behind clean interfaces, enabling provider-agnostic operations and future extensibility.

---

## Architecture Principles

1. **Provider Abstraction** — Embedding providers (OpenAI, Gemini) are interchangeable via a common interface; no provider SDK leaks into business logic
2. **Vector Store Isolation** — pgvector implementation is abstracted behind repository interfaces; future vector databases (Pinecone, Qdrant, Weaviate) can be added without changing application code
3. **Caching First** — Embeddings and search results are cached in Redis to reduce cost and latency
4. **Security at Every Layer** — Embedding access is governed by source document visibility and RBAC
5. **Observability by Default** — Every embedding, search, and index operation emits metrics and structured logs

---

## Architecture Diagram

```
┌──────────────────────────────────────────────────────────────────────────────┐
│                           Business Modules                                    │
│  Catalog │ Orders │ Inventory │ Marketplace │ ERP │ Training │ Support       │
└───────────────────────────────────┬──────────────────────────────────────────┘
                                    │
┌───────────────────────────────────▼──────────────────────────────────────────┐
│                            Knowledge Platform                                 │
│  Document Management │ Chunking │ Metadata │ Retrieval │ Citations           │
└───────────────────────────────────┬──────────────────────────────────────────┘
                                    │
┌───────────────────────────────────▼──────────────────────────────────────────┐
│                        Semantic Intelligence Platform                         │
│                                                                              │
│  ┌────────────────┐  ┌────────────────┐  ┌────────────────┐                 │
│  │  Embedding     │  │  Semantic      │  │  Ranking       │                 │
│  │  Framework     │  │  Search        │  │  Engine        │                 │
│  └───────┬────────┘  └───────┬────────┘  └───────┬────────┘                 │
│  ┌───────▼────────┐  ┌───────▼────────┐  ┌───────▼────────┐                 │
│  │  Vector Index  │  │  Hybrid        │  │  Context       │                 │
│  │  Management    │  │  Search        │  │  Retrieval     │                 │
│  └───────┬────────┘  └───────┬────────┘  └───────┬────────┘                 │
│          │                   │                    │                          │
│  ┌───────▼───────────────────▼────────────────────▼──────────────────────┐  │
│  │                       Embedding Provider Layer                          │  │
│  │  ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐     │  │
│  │  │  OpenAI Adapter  │  │  Gemini Adapter  │  │  Future Adapters │     │  │
│  │  └──────────────────┘  └──────────────────┘  └──────────────────┘     │  │
│  └──────────────────────────────┬──────────────────────────────────────────┘  │
└─────────────────────────────────┬────────────────────────────────────────────┘
                                  │
┌─────────────────────────────────▼────────────────────────────────────────────┐
│                              Vector Store Layer                               │
│  ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐           │
│  │  pgvector        │  │  HNSW Index      │  │  IVFFlat Index   │           │
│  │  (PostgreSQL)    │  │  (High Perf)     │  │  (Balanced)      │           │
│  └──────────────────┘  └──────────────────┘  └──────────────────┘           │
│  ┌────────────────────────────────────────────────────────────────────────┐  │
│  │  Future: Pinecone │ Qdrant │ Weaviate │ Milvus │ ChromaDB             │  │
│  └────────────────────────────────────────────────────────────────────────┘  │
└──────────────────────────────────────────────────────────────────────────────┘
```

---

## Module Descriptions

### semantic-core (`domain/`)
- Domain records: `SemanticEmbedding`, `SemanticSearchQuery`, `SemanticSearchResult`, `SemanticSimilarityScore`
- Enums: `SearchType` (SEMANTIC, KEYWORD, HYBRID, CROSS_ENCODER, MULTI_VECTOR, CONTEXTUAL), `IndexType` (HNSW, IVFFlat), `EmbeddingProvider` (OPENAI, GEMINI)
- Value objects: `VectorEmbedding`, `SimilarityScore`, `IndexConfig`

### semantic-embedding (`application/EmbeddingService`)
- Primary API for generating embeddings from text content
- Delegates to provider-specific `EmbeddingGenerator` implementations
- Supports single and batch embedding operations
- Caches embeddings in Redis (24h TTL, write-through)

### semantic-index (`application/VectorIndexService`)
- Manages pgvector index lifecycle (create, rebuild, drop)
- Supports HNSW and IVFFlat index types
- Configurable index parameters (m, ef_construction, lists, probes)
- Scheduled rebuilds via `IndexRebuildScheduler`

### semantic-search (`application/SearchService`)
- Executes six search types: SEMANTIC, KEYWORD, HYBRID, CROSS_ENCODER, MULTI_VECTOR, CONTEXTUAL
- Integrates with `RankingService` for result scoring and re-ranking
- Applies security filters based on source document visibility
- Logs search history and publishes Kafka events

### semantic-ranking (`application/RankingService`)
- Computes similarity scores using configurable algorithms
- Supports cosine similarity, Euclidean distance, and dot product
- Implements reciprocal rank fusion (RRF) for hybrid search
- Optional cross-encoder re-ranking for high-precision results

### semantic-retrieval (`application/SemanticRetrievalService`)
- Combines semantic search with knowledge platform context assembly
- Retrieves relevant chunks and builds citation metadata
- Applies visibility and permission filters from Knowledge Platform

### semantic-cache (`infrastructure/cache/SemanticRedisCacheService`)
- Write-through cache for embeddings (24h TTL)
- Cache-aside for search results (5 min TTL) and similarity scores (1h TTL)
- Refresh-ahead for index metadata and statistics
- Cache invalidation on index rebuild or embedding update

### semantic-monitoring (`infrastructure/monitoring/`)
- Micrometer metrics for latency, throughput, cache hit ratio
- Health indicators for embedding providers and vector store
- Structured JSON logging with correlation IDs
- Prometheus endpoint for metrics collection

### semantic-security (`application/SemanticSecurityService`)
- Embedding-level access control based on source document visibility
- Index-scoped permissions for create/rebuild operations
- Input sanitization for search queries
- Rate limiting per user per search type

### semantic-adapters (`infrastructure/adapters/`)
- `OpenAIEmbeddingAdapter` — text-embedding-3-small (1536d) and text-embedding-3-large (3072d)
- `GeminiEmbeddingAdapter` — embedding-001 (768d)
- Extensible via `EmbeddingGenerator` interface for future providers

---

## Data Flow

### Embed Generation

```
POST /api/v1/semantic/embed
  → SemanticController
    → EmbeddingService.generateEmbedding(content, provider?)
      → Check Redis cache (semantic:embed:{content_hash})
        → Cache HIT: Return cached embedding
        → Cache MISS:
          → EmbeddingGenerator.generate(content)
            → Provider Adapter (OpenAI/Gemini)
            → Raw embedding response
          → EmbeddingNormalizer.normalize(vector)
          → EmbeddingValidator.validate(dimensions, quality)
          → Store in Redis (write-through)
          → Store in semantic_embeddings table
          → Publish Kafka event (SemanticEmbeddingGenerated)
      ← EmbeddingResponse
```

### Semantic Search

```
POST /api/v1/semantic/search
  → SemanticController
    → SearchService.search(query, searchType, filters)
      → Check Redis cache (semantic:search:{query_hash})
        → Cache HIT: Return cached results
        → Cache MISS:
          → For SEMANTIC search:
            → EmbeddingService.generateEmbedding(query)
            → VectorIndexService.similaritySearch(embedding, limit)
            → SimilarityScorer.score(embedding, candidates)
          → For HYBRID search:
            → Execute SEMANTIC and KEYWORD in parallel
            → ReciprocalRankFusion.fuse(semanticResults, keywordResults)
            → Optional: CrossEncoderScorer.rerank(topN)
          → Apply security filters
          → Store in Redis (cache-aside)
          → Log to semantic_search_history
          → Publish Kafka event (SemanticSearchExecuted)
      ← SearchResponse with ranked results
```

### Hybrid Search

```
Query → Embed(OpenAI/Gemini) ──→ Vector Similarity (pgvector)
                                        ↓
Query → Tokenize/Stem        ──→ BM25 Scoring (PostgreSQL)
                                        ↓
                              Reciprocal Rank Fusion (RRF k=60)
                                        ↓
                                Weighted Merge (0.7:0.3)
                                        ↓
                              Cross-Encoder Re-rerank (optional)
                                        ↓
                                Final Ranked Results
```

---

## Embedding Abstraction Layer

The embedding abstraction ensures provider independence through a clean interface hierarchy:

```
EmbeddingService (application facade)
  └── EmbeddingGenerator (port/interface)
        ├── OpenAIEmbeddingAdapter (infrastructure)
        ├── GeminiEmbeddingAdapter (infrastructure)
        └── FutureProviderAdapter (extensible)
```

**Interface:**
```java
public interface EmbeddingGenerator {
    String providerName();
    int dimensions();
    int maxTokens();
    VectorEmbedding generate(String text);
    List<VectorEmbedding> generateBatch(List<String> texts);
    boolean isAvailable();
}
```

**Provider Selection Strategy:**
- Explicit provider in request overrides default
- Default provider configured via `sporekart.semantic.embedding.default-provider`
- Automatic fallback on provider unavailability
- Round-robin load balancing across available providers

---

## Vector Index Abstraction

```
VectorIndexService (application facade)
  └── VectorIndexManager (port/interface)
        ├── HnswIndexBuilder (infrastructure)
        ├── IvfFlatIndexBuilder (infrastructure)
        └── FutureIndexBuilder (extensible)
```

**Index Config:**
| Parameter | HNSW | IVFFlat |
|-----------|------|---------|
| m | 16 (default) | — |
| ef_construction | 64 (default) | — |
| ef_search | 40 (default) | — |
| lists | — | 100 (default) |
| probes | — | 10 (default) |

**Rebuild Strategy:**
- Scheduled daily at low-traffic hours
- Triggered manually via API
- On index configuration change
- When vector count grows by 20% since last rebuild

---

## Security Model

| Concern | Mechanism |
|---------|-----------|
| Embedding Access | Check source document visibility (PUBLIC/INTERNAL/RESTRICTED/CONFIDENTIAL) |
| Index Management | ADMINISTRATOR and KNOWLEDGE_MANAGER roles only |
| Search Queries | Rate limited per user (100 req/min SEMANTIC, 200 req/min KEYWORD) |
| Query Sanitization | Strip HTML, limit query length (1000 chars), prevent injection |
| Provider Credentials | Stored in Vault, never exposed in logs or responses |

---

## Caching Strategy

| Cache Key Pattern | TTL | Strategy | Purpose |
|---|---|---|---|
| `semantic:embed:{md5(content+provider+model)}` | 24h | Write-through | Avoid redundant embedding API calls |
| `semantic:search:{md5(query+type+filters)}` | 5 min | Cache-aside | Fast repeated query responses |
| `semantic:sim:{md5(src+tgt+alg)}` | 1h | Cache-aside | Pre-computed similarity scores |
| `semantic:idx:{name}` | 10 min | Refresh-ahead | Index metadata without DB calls |
| `semantic:stats` | 5 min | Refresh-ahead | Real-time statistics display |

---

## Event Catalog

| Event | Producer | Consumers | Payload |
|---|---|---|---|
| `SemanticEmbeddingGenerated` | EmbeddingService | Knowledge Platform, Monitoring | embeddingId, contentHash, provider, dimensions |
| `SemanticEmbeddingBatchCompleted` | EmbeddingBatchProcessor | Monitoring, Notification | batchId, totalItems, failedItems, durationMs |
| `SemanticSearchExecuted` | SearchService | Analytics, Monitoring | query, searchType, resultCount, latencyMs |
| `SemanticHybridSearchExecuted` | SearchService | Analytics, Monitoring | query, semanticWeight, keywordWeight, resultCount |
| `SemanticIndexCreated` | VectorIndexService | Monitoring | indexName, indexType, dimensions, vectorCount |
| `SemanticIndexRebuildStarted` | VectorIndexService | Notification | indexName, indexType, estimatedDuration |
| `SemanticIndexRebuildCompleted` | VectorIndexService | Knowledge Platform, Monitoring | indexName, vectorCount, durationMs |
| `SemanticSimilarityComputed` | SimilarityScorer | Analytics | sourceId, targetId, algorithm, score |
| `SemanticCacheInvalidated` | SemanticRedisCacheService | Monitoring | cachePrefix, reason |
