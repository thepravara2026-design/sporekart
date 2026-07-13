# Vector Search Architecture

**Version:** 1.0.0
**Last Updated:** 2026-07-12
**Module:** ai-service

---

## Overview

The Vector Search Architecture provides the foundation for similarity-based retrieval using pgvector on PostgreSQL (Supabase). It abstracts vector index management, similarity search algorithms, and hybrid search strategies behind clean interfaces, ensuring provider independence and future extensibility to other vector databases.

---

## Vector Search Pipeline

```
Query Text
    ↓
EmbeddingService.generate(query)
    ↓
VectorEmbedding (float[])
    ↓
VectorStore.search(embedding, limit, filters)
    ├── pgvector: Cosine/Euclidean/Dot product
    ├── Index scan (HNSW/IVFFlat)
    └── Metadata filter pushdown
    ↓
Candidate Results (id, score, metadata)
    ↓
RankingService.rerank(candidates)
    ↓
Final Results
```

---

## pgvector Abstraction

The pgvector implementation is isolated behind a `VectorStoreRepository` interface:

```java
public interface VectorStoreRepository {
    List<VectorSearchResult> similaritySearch(
        float[] embedding, int limit, SearchType type, Map<String, Object> filters);
    void upsertEmbedding(UUID id, float[] embedding, Map<String, Object> metadata);
    void deleteEmbedding(UUID id);
    void createIndex(String indexName, IndexType type, IndexConfig config);
    void dropIndex(String indexName);
    boolean indexExists(String indexName);
    long vectorCount();
}
```

This interface allows future vector databases (Pinecone, Qdrant, Weaviate) to be added as alternative implementations without changing application-level code.

---

## Vector Index Management

### Index Types

| Index Type | Algorithm | Build Speed | Query Speed | Memory | Use Case |
|------------|-----------|-------------|-------------|--------|----------|
| HNSW | Hierarchical Navigable Small Worlds | Slow (O(n log n)) | Fast (log n) | High | Production, read-heavy |
| IVFFlat | Inverted File with Flat | Fast (O(n)) | Moderate | Low | Development, write-heavy |

### HNSW Parameters

| Parameter | Default | Range | Description |
|-----------|---------|-------|-------------|
| m | 16 | 2–100 | Maximum number of connections per layer |
| ef_construction | 64 | 4–1000 | Dynamic candidate list during construction |
| ef_search | 40 | 1–1000 | Dynamic candidate list during search |

### IVFFlat Parameters

| Parameter | Default | Range | Description |
|-----------|---------|-------|-------------|
| lists | 100 | 1–10000 | Number of inverted lists (clusters) |
| probes | 10 | 1–lists | Number of lists probed during search |

### Index Lifecycle

```
Create → Build → Activate → Query → Rebuild/Recreate → Drop
```

---

## Similarity Search Algorithms

### Cosine Similarity

Preferred for semantic search where vector magnitude is irrelevant (normalized embeddings).

```sql
SELECT id, content, 1 - (embedding <=> :query_embedding) AS similarity
FROM semantic_embeddings
WHERE is_deleted = FALSE
ORDER BY embedding <=> :query_embedding
LIMIT :limit;
```

- Range: [-1, 1] → mapped to [0, 1]
- Use with: OpenAI and Gemini normalized embeddings
- Index support: HNSW with vector_cosine_ops, IVFFlat with vector_cosine_ops

### Euclidean Distance (L2)

Used when magnitude matters, e.g., for certain embedding models that encode magnitude.

```sql
SELECT id, content, (embedding <-> :query_embedding) AS distance
FROM semantic_embeddings
WHERE is_deleted = FALSE
ORDER BY embedding <-> :query_embedding
LIMIT :limit;
```

- Range: [0, ∞) → lower is more similar
- Index support: HNSW with vector_l2_ops, IVFFlat with vector_l2_ops

### Dot Product

Suitable for models specifically trained to optimize dot-product similarity.

```sql
SELECT id, content, (embedding <#> :query_embedding) AS inner_product
FROM semantic_embeddings
WHERE is_deleted = FALSE
ORDER BY embedding <#> :query_embedding
LIMIT :limit;
```

- Range: (-∞, ∞) → higher is more similar
- Index support: HNSW with vector_ip_ops, IVFFlat with vector_ip_ops

### Algorithm Selection

| Model | Recommended Algorithm | Rationale |
|-------|----------------------|-----------|
| OpenAI text-embedding-3-small | Cosine | Model produces normalized vectors |
| OpenAI text-embedding-3-large | Cosine | Model produces normalized vectors |
| Gemini embedding-001 | Cosine | Model produces normalized vectors |
| Custom/future models | Configurable | Per-model configuration |

---

## Hybrid Search Strategy

Hybrid search combines semantic (vector) and keyword (BM25) scoring for optimal retrieval quality.

### Architecture

```
┌──────────────────────────────────────────────────────┐
│                   SearchService                        │
│                                                       │
│  ┌────────────────┐  ┌────────────────┐              │
│  │ Semantic Query │  │ Keyword Query  │              │
│  │ (pgvector)     │  │ (BM25/TSVect)  │              │
│  └────────┬───────┘  └────────┬───────┘              │
│           │                   │                       │
│  ┌────────▼───────────────────▼───────┐              │
│  │    Reciprocal Rank Fusion (RRF)    │              │
│  │    k = 60 (default)                │              │
│  └────────┬───────────────────┬───────┘              │
│           │                   │                       │
│  ┌────────▼───────┐  ┌───────▼────────┐             │
│  │ Merge Results  │  │ De-duplicate   │             │
│  └────────┬───────┘  └────────────────┘             │
│           │                                           │
│  ┌────────▼──────────────────────────────────────┐   │
│  │  Cross-Encoder Re-ranking (optional)          │   │
│  └────────┬──────────────────────────────────────┘   │
│           │                                           │
│  ┌────────▼───────┐                                   │
│  │ Final Ranking  │                                   │
│  └────────────────┘                                   │
└──────────────────────────────────────────────────────┘
```

### RRF Formula

```
RRFscore(d) = Σ(1 / (k + rank_s(d)))  for each search method s
```

Where:
- `k` = 60 (default, configurable)
- `rank_s(d)` = rank position of document d in search method s

### Weighted Hybrid (Alternative)

```
HybridScore(d) = α × SemanticScore(d) + (1 − α) × KeywordScore(d)
```

Where:
- `α` = 0.7 (default, configurable)
- SemanticScore normalized to [0, 1]
- KeywordScore normalized to [0, 1]

### Keyword Search Implementation

```sql
SELECT id, content, ts_rank(to_tsvector('english', content), plainto_tsquery('english', :query)) AS rank
FROM semantic_embeddings
WHERE to_tsvector('english', content) @@ plainto_tsquery('english', :query)
  AND is_deleted = FALSE
ORDER BY rank DESC
LIMIT :limit;
```

### Metadata Filter Pushdown

All search types support metadata filtering at the database level:

```sql
-- Both semantic and hybrid: filter before/after similarity
SELECT id, content, 1 - (embedding <=> :query) AS similarity
FROM semantic_embeddings
WHERE is_deleted = FALSE
  AND metadata->>'category' = :category    -- Pre-filter
  AND metadata->>'language' = :language
  AND (metadata->>'visibility')::int <= :userVisibility
ORDER BY embedding <=> :query
LIMIT :limit;
```

---

## Index Rebuild Strategy

### When to Rebuild

| Condition | Action | Priority |
|-----------|--------|----------|
| New index created | Build immediately | High |
| Vector count +20% | Schedule rebuild | Medium |
| Index configuration change | Recreate index | High |
| Scheduled maintenance window | Rebuild all | Low |
| Performance degradation detected | Trigger rebuild | Medium |

### Rebuild Process

```
1. Create new index with temp name: idx_semantic_embeddings_{type}_new
2. Build index (HNSW can be slow for large tables)
3. Swap: DROP old index, RENAME new index
4. Update semantic_vector_index metadata
5. Publish Kafka event (SemanticIndexRebuildCompleted)
6. Invalidate Redis cache
```

### Scheduled Rebuild

```yaml
sporekart:
  semantic:
    index:
      rebuild:
        cron: "0 3 * * *"       # Daily 3 AM
        min-vectors: 10000       # Minimum vectors to rebuild
        growth-threshold: 0.2    # Rebuild if 20% growth
```

---

## Performance Considerations

| Factor | Impact | Mitigation |
|--------|--------|------------|
| Embedding Dimensions | Higher dimensions = slower search, larger indexes | Use smaller models (1536d vs 3072d) where precision acceptable |
| Index Type | HNSW faster query, slower build | Use HNSW for production read-heavy, IVFFlat for development |
| ef_search / probes | Higher = more accurate, slower | Start with defaults, tune based on latency budgets |
| Vector Count | O(n) or O(log n) depending on index | Partition large collections by category/business module |
| Batch Size | Large batches impact embedding API latency | Configurable batch size (default: 20) |
| Redis Cache | Cache hit ratio directly affects latency | Monitor cache hit ratio, adjust TTLs |
| Connection Pool | pgvector queries are database-intensive | Dedicated connection pool for vector queries |

### Recommended Configuration

| Environment | Index Type | ef_search | probes | Cache TTL |
|-------------|------------|-----------|--------|-----------|
| Development | IVFFlat | — | 10 | 5 min |
| Staging | HNSW | 40 | — | 15 min |
| Production | HNSW | 40 | — | 24h (embeddings), 5 min (search) |

---

## Future Provider Support

The VectorStoreRepository interface enables seamless migration to alternative vector databases:

### Interface Contract

```java
public interface VectorStoreRepository {
    // Core operations
    List<VectorSearchResult> similaritySearch(float[] embedding, int limit, SearchType type, Map<String, Object> filters);
    void upsertEmbedding(UUID id, float[] embedding, Map<String, Object> metadata);
    void deleteEmbedding(UUID id);

    // Index management
    void createIndex(String indexName, IndexType type, IndexConfig config);
    void dropIndex(String indexName);

    // Metadata
    long vectorCount();
    boolean indexExists(String indexName);
}
```

### Future Provider Candidates

| Provider | Type | Strengths | Integration Complexity |
|----------|------|-----------|-----------------------|
| Pinecone | Managed | Zero infrastructure, high scalability | Low (REST API) |
| Qdrant | Self-hosted / Cloud | Advanced filtering, hybrid search | Medium |
| Weaviate | Self-hosted / Cloud | Built-in modules, multi-tenancy | Medium |
| Milvus | Self-hosted / Cloud | Billion-scale, GPU acceleration | High |
| ChromaDB | Embedded | Simple, local development | Low |

### Migration Path

```
pgvector (current)
    ↓
Add new VectorStoreRepository implementation for target DB
    ↓
Dual-write embeddings during migration period
    ↓
Dual-read with percentage-based traffic shift
    ↓
Validation and comparison of results
    ↓
Full cutover → decommission pgvector connection
```
