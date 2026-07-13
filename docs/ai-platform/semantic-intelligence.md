# Semantic Intelligence Platform

**Version:** 1.0.0
**Last Updated:** 2026-07-12

---

## Overview

The Semantic Intelligence Platform provides enterprise-grade semantic search, vector embeddings, and hybrid retrieval capabilities for the SporeKart AI Platform. It enables business modules to perform concept-aware searches, find similar content, and retrieve contextually relevant information using state-of-the-art embedding models and vector search technology.

---

## Architecture

The platform follows a layered architecture with clean separation of concerns:

```
┌────────────────────────────────────────────────────────────┐
│                     REST API Layer                           │
│  Embed │ Search │ Similarity │ Index │ Statistics │ Health  │
└──────────────────────────┬─────────────────────────────────┘
                           │
┌──────────────────────────▼─────────────────────────────────┐
│                   Application Services                       │
│  EmbeddingService │ SearchService │ RankingService          │
│  VectorIndexService │ SemanticRetrievalService              │
│  SemanticCacheService │ SemanticSecurityService             │
└──────────────────────────┬─────────────────────────────────┘
                           │
┌──────────────────────────▼─────────────────────────────────┐
│                   Infrastructure Layer                       │
│  Redis Cache │ Kafka Publisher │ JPA Repositories           │
│  Embedding Adapters (OpenAI, Gemini)                        │
│  Vector Store (pgvector)                                    │
└─────────────────────────────────────────────────────────────┘
```

---

## Embedding Framework

### EmbeddingService

The primary API for generating vector embeddings from text content. Supports both single and batch operations with automatic provider selection, caching, and fallback.

**Single Embedding:**
```java
EmbeddingResponse embed(String text, String provider);
```

**Batch Embedding:**
```java
EmbeddingBatchResponse embedBatch(List<String> texts, String provider);
```

### Supported Providers

| Provider | Models | Dimensions |
|----------|--------|------------|
| OpenAI | text-embedding-3-small | 1536 |
| OpenAI | text-embedding-3-large | 3072 |
| Gemini | embedding-001 | 768 |

### Embedding Pipeline

```
Input Text
  ↓
Pre-processing (trim, sanitize, truncate to max tokens)
  ↓
Cache Check (Redis: semantic:embed:{hash})
  ├── HIT → Return cached embedding
  └── MISS
       ↓
  Provider Selection (explicit / default / fallback)
       ↓
  EmbeddingGenerator.generate(text)
       ↓
  Normalize (unit-length normalization)
       ↓
  Validate (dimension check, quality check)
       ↓
  Persist (semantic_embeddings table)
       ↓
  Cache (Redis, write-through)
       ↓
  Publish Event (SemanticEmbeddingGenerated)
```

---

## Vector Indexing

### Index Management

Vector indexes are managed through the `VectorIndexService`, which supports creating, rebuilding, and monitoring pgvector indexes.

**Supported Index Types:**
- **HNSW** — High-performance approximate nearest neighbor search
- **IVFFlat** — Balanced performance for development and moderate workloads

### Index Operations

```bash
# List all indexes
GET /api/v1/semantic/index

# Rebuild an index (e.g., after significant data growth)
POST /api/v1/semantic/index/rebuild
{
  "indexName": "idx_embeddings_cosine",
  "indexType": "HNSW",
  "config": {
    "m": 16,
    "efConstruction": 64
  }
}
```

---

## Semantic Search

Six search types provide flexible retrieval options:

### 1. SEMANTIC
Pure vector similarity search. Converts query to embedding, finds nearest neighbors via cosine similarity.

```bash
curl -X POST http://localhost:8088/api/v1/semantic/search \
  -H "Content-Type: application/json" \
  -d '{
    "query": "tomato growing techniques",
    "searchType": "SEMANTIC",
    "limit": 10
  }'
```

### 2. KEYWORD
BM25-based text search using PostgreSQL full-text search (tsvector/tsquery).

### 3. HYBRID
Weighted combination of SEMANTIC and KEYWORD using Reciprocal Rank Fusion (RRF).

```bash
curl -X POST http://localhost:8088/api/v1/semantic/search \
  -H "Content-Type: application/json" \
  -d '{
    "query": "tomato growing techniques",
    "searchType": "HYBRID",
    "limit": 10,
    "hybridConfig": {
      "semanticWeight": 0.7,
      "keywordWeight": 0.3
    }
  }'
```

### 4. CROSS_ENCODER
Semantic search followed by cross-encoder re-ranking for high precision.

### 5. MULTI_VECTOR
Multiple query vectors for complex multi-concept queries.

### 6. CONTEXTUAL
Search with surrounding document context for improved relevance.

---

## Ranking Engine

The ranking engine computes similarity scores and fuses results from multiple search strategies.

### Similarity Algorithms

| Algorithm | Operator | Range | Description |
|-----------|----------|-------|-------------|
| Cosine | `<=>` | [0, 1] | Normalized vector angle similarity |
| Euclidean | `<->` | [0, ∞) | L2 distance (lower = more similar) |
| Dot Product | `<#>` | (-∞, ∞) | Inner product (higher = more similar) |

### Reciprocal Rank Fusion (RRF)

```
RRFscore(d) = Σ(1 / (k + rank_s(d)))  for each search method s
```

Default k = 60, configurable per request.

---

## Hybrid Search

Hybrid search combines semantic understanding with keyword precision:

1. **Semantic leg**: Query → Embedding → pgvector cosine similarity → ranked results
2. **Keyword leg**: Query → tsquery → PostgreSQL full-text search → ranked results
3. **Fusion**: RRF merge with configurable weights
4. **Re-ranking** (optional): Cross-encoder scoring on top-N results

---

## Context Retrieval

The `SemanticRetrievalService` combines semantic search with the Knowledge Platform's document management:

1. Execute semantic/hybrid search against embeddings
2. Map embedding results back to Knowledge Platform documents
3. Apply document visibility and permission filters
4. Retrieve relevant chunks with citation metadata
5. Build structured context response

---

## Caching

| Cache | Scope | TTL | Strategy |
|-------|-------|-----|----------|
| Embeddings | Per (content, provider, model) | 24h | Write-through |
| Search Results | Per (query, type, filters) | 5 min | Cache-aside |
| Similarity Scores | Per (source, target, algorithm) | 1h | Cache-aside |
| Index Metadata | Per index | 10 min | Refresh-ahead |
| Statistics | Global | 5 min | Refresh-ahead |

---

## Security

- **Embedding-Level RBAC** — Embedding access inherits from source document visibility
- **Rate Limiting** — 100 req/min for semantic search, 200 req/min for keyword
- **Input Sanitization** — HTML stripping, length limits (1000 chars), injection prevention
- **Audit Logging** — All search queries logged with user, timestamp, search type
- **Provider Credentials** — Managed via Vault, never exposed in API responses

---

## Observability

### Metrics (Micrometer)

| Metric | Type | Description |
|--------|------|-------------|
| `semantic.embed.latency` | Timer | Embedding generation latency |
| `semantic.embed.count` | Counter | Total embedding requests |
| `semantic.embed.batch.size` | DistributionSummary | Batch sizes |
| `semantic.search.latency` | Timer | Search execution latency |
| `semantic.search.count` | Counter | Total searches by type |
| `semantic.search.result.count` | DistributionSummary | Results per search |
| `semantic.index.size` | Gauge | Vector count per index |
| `semantic.cache.hit.ratio` | Gauge | Redis cache hit ratio |
| `semantic.provider.health` | Gauge | Embedding provider health (0/1) |

### Health Checks

- Embedding provider connectivity (OpenAI, Gemini)
- Vector store (pgvector) connectivity
- Redis cache connectivity
- Kafka connectivity
- Index health (last rebuild time, vector count)

---

## Integration with Knowledge Platform

```
Knowledge Platform (Part 5)
  │
  │  Documents │ Chunks │ Metadata │ Visibility
  │
  └──────────→ Semantic Intelligence Platform
                  │
                  │  Embeddings → Index → Search → Rank
                  │
                  └──────────→ Knowledge Platform
                                  │
                              Context Assembly
                                  │
                              Citation Building
```

The relationship between Knowledge Platform and Semantic Intelligence Platform:

1. **Knowledge Platform** manages documents, chunks, categories, and visibility
2. **Semantic Platform** generates embeddings for Knowledge Platform chunks
3. **Search** queries the semantic index and maps results back to Knowledge Platform documents
4. **Retrieval** combines semantic relevance with visibility filters from Knowledge Platform
5. **Citations** are built by Knowledge Platform using semantic search results

### Configuration

```yaml
sporekart:
  ai:
    features:
      semantic-enabled: true
      semantic-caching: true
      semantic-audit: true
    modules:
      semantic:
        enabled: true
        embedding:
          default-provider: openai
          default-model: text-embedding-3-small
          batch-size: 20
          cache-ttl: 24h
        search:
          default-type: HYBRID
          default-limit: 10
          rrf-constant: 60
          semantic-weight: 0.7
          keyword-weight: 0.3
        index:
          default-type: HNSW
          auto-rebuild: true
          rebuild-cron: "0 3 * * *"
          growth-threshold: 0.2
```
