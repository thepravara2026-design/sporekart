# Semantic Intelligence Platform Database Schema

**Version:** 1.0.0
**Migration:** V15__sprint17_semantic_intelligence.sql
**Engine:** PostgreSQL (Supabase) with pgvector extension

---

## Extension

```sql
CREATE EXTENSION IF NOT EXISTS vector;
```

---

## Tables

### semantic_embeddings

Stores all generated vector embeddings with provider-agnostic metadata.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, DEFAULT gen_random_uuid() | Primary key |
| content | TEXT | NOT NULL | Original text content |
| content_hash | VARCHAR(64) | NOT NULL | SHA-256 hash of content |
| embedding | vector | NOT NULL | Vector embedding (variable dimensions) |
| provider | VARCHAR(50) | NOT NULL | Embedding provider (openai, gemini) |
| model | VARCHAR(100) | NOT NULL | Model name (text-embedding-3-small, etc.) |
| dimensions | INT | NOT NULL | Embedding dimensions (768, 1536, 3072) |
| token_count | INT | DEFAULT 0 | Estimated token count |
| metadata | JSONB | DEFAULT '{}' | Source document metadata (sourceId, sourceType, category, language, visibility) |
| status | VARCHAR(50) | NOT NULL, DEFAULT 'ACTIVE' | Embedding status (ACTIVE, STALE, INVALID) |
| version | INT | NOT NULL, DEFAULT 1 | Version number for updates |
| source_id | UUID | | FK → knowledge_chunks.id or other source |
| source_type | VARCHAR(100) | | Source entity type (knowledge_chunk, document, etc.) |
| provider_ref | VARCHAR(255) | | External provider reference ID |
| created_by | UUID | | Creator user ID |
| created_at | TIMESTAMPTZ | NOT NULL, DEFAULT NOW() | Creation timestamp |
| updated_by | UUID | | Last updater user ID |
| updated_at | TIMESTAMPTZ | | Last update timestamp |
| is_deleted | BOOLEAN | DEFAULT FALSE | Soft delete flag |
| deleted_at | TIMESTAMPTZ | | Deletion timestamp |

**Unique Constraints:**
- (content_hash, provider, model, version)

**Notes:**
- Embedding vector dimensions vary by provider/model (768 for Gemini, 1536 for OpenAI small, 3072 for OpenAI large)
- The vector type supports any dimension up to 16000
- Provider-independence is achieved by storing provider, model, and dimensions alongside the vector

---

### semantic_vector_index

Registers vector index metadata for management and monitoring.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, DEFAULT gen_random_uuid() | Primary key |
| name | VARCHAR(255) | NOT NULL, UNIQUE | Index name (e.g., idx_embeddings_cosine) |
| description | TEXT | | Index description |
| status | VARCHAR(50) | NOT NULL, DEFAULT 'CREATING' | Index status (CREATING, ACTIVE, REBUILDING, FAILED, INACTIVE) |
| index_type | VARCHAR(50) | NOT NULL | Index algorithm (HNSW, IVFFlat) |
| vector_count | INT | DEFAULT 0 | Number of vectors indexed |
| dimensions | INT | NOT NULL | Embedding dimensions |
| index_config | JSONB | DEFAULT '{}' | Index parameters (m, ef_construction, lists, probes) |
| provider | VARCHAR(50) | | Provider this index applies to (null = all) |
| model | VARCHAR(100) | | Model this index applies to (null = all) |
| last_rebuilt_at | TIMESTAMPTZ | | Last successful rebuild timestamp |
| last_rebuild_duration_ms | BIGINT | | Duration of last rebuild |
| error_message | TEXT | | Last error message |
| created_at | TIMESTAMPTZ | NOT NULL, DEFAULT NOW() | Creation timestamp |
| updated_at | TIMESTAMPTZ | | Last update timestamp |
| is_deleted | BOOLEAN | DEFAULT FALSE | Soft delete flag |
| deleted_at | TIMESTAMPTZ | | Deletion timestamp |

---

### semantic_search_history

Logs all search queries for audit, analytics, and performance monitoring.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, DEFAULT gen_random_uuid() | Primary key |
| query | TEXT | NOT NULL | Search query text |
| query_hash | VARCHAR(64) | | SHA-256 hash of normalized query |
| search_type | VARCHAR(50) | NOT NULL | Search type (SEMANTIC, KEYWORD, HYBRID, etc.) |
| filters | JSONB | DEFAULT '{}' | Applied filters (category, language, visibility) |
| result_count | INT | DEFAULT 0 | Number of results returned |
| latency_ms | BIGINT | NOT NULL | Query execution time in milliseconds |
| cached | BOOLEAN | DEFAULT FALSE | Whether result was served from cache |
| provider | VARCHAR(50) | | Embedding provider used |
| model | VARCHAR(100) | | Embedding model used |
| correlation_id | VARCHAR(64) | | Request correlation ID |
| created_by | UUID | | User who executed the search |
| created_at | TIMESTAMPTZ | NOT NULL, DEFAULT NOW() | Creation timestamp |
| is_deleted | BOOLEAN | DEFAULT FALSE | Soft delete flag |
| deleted_at | TIMESTAMPTZ | | Deletion timestamp |

---

### semantic_similarity_scores

Pre-computed or on-demand similarity scores between embedding pairs.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, DEFAULT gen_random_uuid() | Primary key |
| source_id | UUID | NOT NULL, FK → semantic_embeddings(id) | Source embedding |
| target_id | UUID | NOT NULL, FK → semantic_embeddings(id) | Target embedding |
| similarity | DOUBLE PRECISION | NOT NULL | Similarity score [0, 1] |
| algorithm | VARCHAR(50) | NOT NULL | Algorithm used (COSINE, EUCLIDEAN, DOT_PRODUCT) |
| metadata | JSONB | DEFAULT '{}' | Additional scoring metadata |
| created_at | TIMESTAMPTZ | NOT NULL, DEFAULT NOW() | Creation timestamp |
| is_deleted | BOOLEAN | DEFAULT FALSE | Soft delete flag |
| deleted_at | TIMESTAMPTZ | | Deletion timestamp |

**Unique Constraints:**
- (source_id, target_id, algorithm)

---

### semantic_embedding_jobs

Tracks batch embedding jobs for progress monitoring and error handling.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, DEFAULT gen_random_uuid() | Primary key |
| type | VARCHAR(50) | NOT NULL | Job type (GENERATE, REGENERATE, DELETE) |
| status | VARCHAR(50) | NOT NULL, DEFAULT 'PENDING' | Job status (PENDING, PROCESSING, COMPLETED, FAILED, CANCELLED) |
| total_items | INT | DEFAULT 0 | Total items to process |
| processed_items | INT | DEFAULT 0 | Successfully processed items |
| failed_items | INT | DEFAULT 0 | Failed items |
| provider | VARCHAR(50) | | Embedding provider |
| model | VARCHAR(100) | | Embedding model |
| config | JSONB | DEFAULT '{}' | Job configuration (batchSize, dimension, etc.) |
| started_at | TIMESTAMPTZ | | Job start time |
| completed_at | TIMESTAMPTZ | | Job completion time |
| duration_ms | BIGINT | | Total duration in milliseconds |
| error_message | TEXT | | Error details if failed |
| created_by | UUID | | User who created the job |
| created_at | TIMESTAMPTZ | NOT NULL, DEFAULT NOW() | Creation timestamp |
| updated_at | TIMESTAMPTZ | | Last update timestamp |
| is_deleted | BOOLEAN | DEFAULT FALSE | Soft delete flag |
| deleted_at | TIMESTAMPTZ | | Deletion timestamp |

---

### semantic_index_statistics

Time-series statistics for index performance monitoring.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, DEFAULT gen_random_uuid() | Primary key |
| index_id | UUID | FK → semantic_vector_index(id) | Related index |
| index_name | VARCHAR(255) | NOT NULL | Index name |
| stat_key | VARCHAR(100) | NOT NULL | Statistic name (vector_count, avg_latency_ms, cache_hit_ratio, query_volume_1h) |
| stat_value | DOUBLE PRECISION | NOT NULL | Statistic value |
| recorded_at | TIMESTAMPTZ | NOT NULL | When the statistic was recorded |
| metadata | JSONB | DEFAULT '{}' | Additional context (provider, model, dimensions) |
| created_at | TIMESTAMPTZ | NOT NULL, DEFAULT NOW() | Creation timestamp |
| is_deleted | BOOLEAN | DEFAULT FALSE | Soft delete flag |
| deleted_at | TIMESTAMPTZ | | Deletion timestamp |

---

## Indexes

```sql
-- Embedding lookup by content hash (deduplication)
CREATE INDEX idx_semantic_embeddings_content_hash
  ON semantic_embeddings (content_hash)
  WHERE is_deleted = FALSE;

-- Embedding lookup by source
CREATE INDEX idx_semantic_embeddings_source
  ON semantic_embeddings (source_id, source_type)
  WHERE is_deleted = FALSE;

-- Embedding status queries
CREATE INDEX idx_semantic_embeddings_status
  ON semantic_embeddings (status)
  WHERE is_deleted = FALSE;

-- Embedding time-based queries
CREATE INDEX idx_semantic_embeddings_created_at
  ON semantic_embeddings (created_at DESC)
  WHERE is_deleted = FALSE;

-- Embedding by provider/model
CREATE INDEX idx_semantic_embeddings_provider_model
  ON semantic_embeddings (provider, model)
  WHERE is_deleted = FALSE;

-- Metadata filter queries (GIN for JSONB)
CREATE INDEX idx_semantic_embeddings_metadata
  ON semantic_embeddings USING GIN (metadata jsonb_path_ops)
  WHERE is_deleted = FALSE;

-- Vector index (pgvector HNSW - created manually via API)
-- CREATE INDEX idx_embeddings_cosine
--   ON semantic_embeddings USING hnsw (embedding vector_cosine_ops)
--   WHERE is_deleted = FALSE;

-- Search history queries
CREATE INDEX idx_semantic_search_history_query
  ON semantic_search_history (query_hash, search_type)
  WHERE is_deleted = FALSE;

CREATE INDEX idx_semantic_search_history_created
  ON semantic_search_history (created_at DESC)
  WHERE is_deleted = FALSE;

CREATE INDEX idx_semantic_search_history_user
  ON semantic_search_history (created_by, created_at DESC)
  WHERE is_deleted = FALSE;

-- Similarity score queries
CREATE INDEX idx_semantic_similarity_source
  ON semantic_similarity_scores (source_id, algorithm)
  WHERE is_deleted = FALSE;

CREATE INDEX idx_semantic_similarity_target
  ON semantic_similarity_scores (target_id, algorithm)
  WHERE is_deleted = FALSE;

-- Job queries
CREATE INDEX idx_semantic_embedding_jobs_status
  ON semantic_embedding_jobs (status, created_at DESC)
  WHERE is_deleted = FALSE;

CREATE INDEX idx_semantic_embedding_jobs_type
  ON semantic_embedding_jobs (type, status)
  WHERE is_deleted = FALSE;

-- Statistics queries
CREATE INDEX idx_semantic_index_stats_index
  ON semantic_index_statistics (index_name, stat_key, recorded_at DESC)
  WHERE is_deleted = FALSE;

CREATE INDEX idx_semantic_vector_index_status
  ON semantic_vector_index (status)
  WHERE is_deleted = FALSE;
```

---

## Constraints

```sql
-- Foreign Key Constraints
ALTER TABLE semantic_embeddings
  ADD CONSTRAINT fk_semantic_embeddings_created_by
  FOREIGN KEY (created_by) REFERENCES users(id);

ALTER TABLE semantic_similarity_scores
  ADD CONSTRAINT fk_similarity_source
  FOREIGN KEY (source_id) REFERENCES semantic_embeddings(id);

ALTER TABLE semantic_similarity_scores
  ADD CONSTRAINT fk_similarity_target
  FOREIGN KEY (target_id) REFERENCES semantic_embeddings(id);

ALTER TABLE semantic_index_statistics
  ADD CONSTRAINT fk_index_stats_index
  FOREIGN KEY (index_id) REFERENCES semantic_vector_index(id);

-- Check Constraints
ALTER TABLE semantic_embeddings
  ADD CONSTRAINT chk_embedding_dimensions
  CHECK (dimensions IN (768, 1536, 3072));

ALTER TABLE semantic_embeddings
  ADD CONSTRAINT chk_embedding_status
  CHECK (status IN ('ACTIVE', 'STALE', 'INVALID'));

ALTER TABLE semantic_vector_index
  ADD CONSTRAINT chk_index_type
  CHECK (index_type IN ('HNSW', 'IVFFlat'));

ALTER TABLE semantic_vector_index
  ADD CONSTRAINT chk_index_status
  CHECK (status IN ('CREATING', 'ACTIVE', 'REBUILDING', 'FAILED', 'INACTIVE'));

ALTER TABLE semantic_search_history
  ADD CONSTRAINT chk_search_type
  CHECK (search_type IN ('SEMANTIC', 'KEYWORD', 'HYBRID', 'CROSS_ENCODER', 'MULTI_VECTOR', 'CONTEXTUAL'));

ALTER TABLE semantic_similarity_scores
  ADD CONSTRAINT chk_similarity_algorithm
  CHECK (algorithm IN ('COSINE', 'EUCLIDEAN', 'DOT_PRODUCT'));

ALTER TABLE semantic_embedding_jobs
  ADD CONSTRAINT chk_job_type
  CHECK (type IN ('GENERATE', 'REGENERATE', 'DELETE'));

ALTER TABLE semantic_embedding_jobs
  ADD CONSTRAINT chk_job_status
  CHECK (status IN ('PENDING', 'PROCESSING', 'COMPLETED', 'FAILED', 'CANCELLED'));

ALTER TABLE semantic_similarity_scores
  ADD CONSTRAINT chk_similarity_range
  CHECK (similarity >= 0 AND similarity <= 1);
```

---

## Provider-Independent Approach

The schema is designed to be provider-agnostic, allowing multiple embedding providers and models to coexist:

1. **Provider and Model Columns** — Each embedding record stores its provider and model, enabling mixed-provider deployments
2. **Variable Dimensions** — The `vector` type supports any dimension; the `dimensions` column records the actual size
3. **Per-Provider Indexes** — Vector indexes can be scoped to specific providers/models or apply globally
4. **Metadata JSONB** — Flexible metadata accommodates provider-specific attributes without schema changes
5. **Unique by Content + Provider + Model** — The same content can have embeddings from multiple providers without conflict
6. **No Provider Lock-In** — Switching providers requires only adding new embeddings; existing ones remain accessible

### Migration Example: Adding a New Provider

```sql
-- No schema changes needed
INSERT INTO semantic_embeddings (content, content_hash, embedding, provider, model, dimensions, source_id, source_type)
SELECT content, content_hash, gen_embedding(content, 'cohere'), 'cohere', 'embed-english-v3.0', 1024, source_id, source_type
FROM semantic_embeddings
WHERE provider = 'openai' AND is_deleted = FALSE;

-- Create provider-specific index
-- CREATE INDEX idx_embeddings_cohere_cosine
--   ON semantic_embeddings USING hnsw (embedding vector_cosine_ops)
--   WHERE provider = 'cohere' AND is_deleted = FALSE;
```
