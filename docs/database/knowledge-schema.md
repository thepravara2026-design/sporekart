# Knowledge Platform Database Schema

**Version:** 1.0.0
**Migration:** V14__sprint17_knowledge_management.sql
**Engine:** PostgreSQL (Supabase)

---

## Tables

### knowledge_categories

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, DEFAULT gen_random_uuid() | Primary key |
| name | VARCHAR(255) | NOT NULL, UNIQUE | Category name |
| description | TEXT | | Category description |
| display_order | INT | DEFAULT 0 | Sort order |
| is_active | BOOLEAN | DEFAULT TRUE | Active flag |
| created_by | UUID | | Creator user ID |
| created_at | TIMESTAMPTZ | NOT NULL, DEFAULT NOW() | Creation timestamp |
| updated_by | UUID | | Last updater user ID |
| updated_at | TIMESTAMPTZ | | Last update timestamp |
| is_deleted | BOOLEAN | DEFAULT FALSE | Soft delete flag |
| deleted_at | TIMESTAMPTZ | | Deletion timestamp |

### knowledge_documents

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, DEFAULT gen_random_uuid() | Primary key |
| category_id | UUID | FK → knowledge_categories(id) | Document category |
| title | VARCHAR(500) | NOT NULL | Document title |
| description | TEXT | | Document description |
| content | TEXT | | Document content |
| language | VARCHAR(10) | DEFAULT 'en' | Language code |
| author | VARCHAR(255) | | Document author |
| source | VARCHAR(255) | | Document source |
| source_type | VARCHAR(100) | | Source type identifier |
| status | VARCHAR(50) | NOT NULL, DEFAULT 'DRAFT' | Document status |
| visibility | VARCHAR(50) | NOT NULL, DEFAULT 'INTERNAL' | Visibility level |
| business_module | VARCHAR(100) | | Business module |
| region | VARCHAR(100) | | Geographic region |
| current_version | INT | NOT NULL, DEFAULT 1 | Current version |
| file_path | VARCHAR(1000) | | File path |
| file_size | BIGINT | | File size in bytes |
| mime_type | VARCHAR(100) | | MIME type |
| is_active | BOOLEAN | DEFAULT TRUE | Active flag |
| created_by | UUID | | Creator user ID |
| created_at | TIMESTAMPTZ | NOT NULL, DEFAULT NOW() | Creation timestamp |
| updated_by | UUID | | Last updater user ID |
| updated_at | TIMESTAMPTZ | | Last update timestamp |
| is_deleted | BOOLEAN | DEFAULT FALSE | Soft delete flag |
| deleted_at | TIMESTAMPTZ | | Deletion timestamp |

### knowledge_document_versions

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, DEFAULT gen_random_uuid() | Primary key |
| document_id | UUID | FK, NOT NULL | Parent document |
| version_number | INT | NOT NULL | Version number |
| content | TEXT | | Version content |
| change_notes | TEXT | | Change description |
| status | VARCHAR(50) | NOT NULL, DEFAULT 'DRAFT' | Version status |
| created_by | UUID | | Creator user ID |
| created_at | TIMESTAMPTZ | NOT NULL, DEFAULT NOW() | Creation timestamp |
| approved_by | UUID | | Approver user ID |
| approved_at | TIMESTAMPTZ | | Approval timestamp |
| is_deleted | BOOLEAN | DEFAULT FALSE | Soft delete flag |
| deleted_at | TIMESTAMPTZ | | Deletion timestamp |

**Unique Constraint:** (document_id, version_number)

### knowledge_chunks

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, DEFAULT gen_random_uuid() | Primary key |
| document_id | UUID | FK, NOT NULL | Parent document |
| version_id | UUID | FK | Document version |
| chunk_index | INT | NOT NULL | Chunk sequence |
| content | TEXT | NOT NULL | Chunk content |
| token_count | INT | DEFAULT 0 | Estimated token count |
| char_count | INT | DEFAULT 0 | Character count |
| chunk_size_strategy | VARCHAR(50) | DEFAULT 'FIXED' | Strategy used |
| parent_chunk_id | UUID | FK → self | Parent chunk (hierarchy) |
| is_active | BOOLEAN | DEFAULT TRUE | Active flag |
| created_at | TIMESTAMPTZ | NOT NULL, DEFAULT NOW() | Creation timestamp |
| is_deleted | BOOLEAN | DEFAULT FALSE | Soft delete flag |
| deleted_at | TIMESTAMPTZ | | Deletion timestamp |

**Unique Constraint:** (document_id, chunk_index)

### knowledge_metadata

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, DEFAULT gen_random_uuid() | Primary key |
| document_id | UUID | FK, NOT NULL | Parent document |
| meta_key | VARCHAR(255) | NOT NULL | Metadata key |
| meta_value | TEXT | | Metadata value |
| created_at | TIMESTAMPTZ | NOT NULL, DEFAULT NOW() | Creation timestamp |
| updated_at | TIMESTAMPTZ | | Last update timestamp |

**Unique Constraint:** (document_id, meta_key)

### knowledge_tags

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, DEFAULT gen_random_uuid() | Primary key |
| document_id | UUID | FK, NOT NULL | Parent document |
| tag | VARCHAR(100) | NOT NULL | Tag value |
| created_at | TIMESTAMPTZ | NOT NULL, DEFAULT NOW() | Creation timestamp |

**Unique Constraint:** (document_id, tag)

### knowledge_sources

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, DEFAULT gen_random_uuid() | Primary key |
| name | VARCHAR(255) | NOT NULL, UNIQUE | Source name |
| description | TEXT | | Source description |
| source_type | VARCHAR(100) | NOT NULL | Source type |
| is_active | BOOLEAN | DEFAULT TRUE | Active flag |
| created_at | TIMESTAMPTZ | NOT NULL, DEFAULT NOW() | Creation timestamp |
| updated_at | TIMESTAMPTZ | | Last update timestamp |
| is_deleted | BOOLEAN | DEFAULT FALSE | Soft delete flag |
| deleted_at | TIMESTAMPTZ | | Deletion timestamp |

### knowledge_access_log

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, DEFAULT gen_random_uuid() | Primary key |
| document_id | UUID | FK | Document accessed |
| action | VARCHAR(100) | NOT NULL | Access action |
| user_id | UUID | | User who acted |
| user_role | VARCHAR(50) | | User's role |
| ip_address | VARCHAR(45) | | Client IP |
| timestamp | TIMESTAMPTZ | NOT NULL, DEFAULT NOW() | Access timestamp |
| duration_ms | BIGINT | | Duration in ms |
| details | TEXT | | Additional details |

### knowledge_citations

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, DEFAULT gen_random_uuid() | Primary key |
| document_id | UUID | FK, NOT NULL | Cited document |
| retrieval_request_id | UUID | | Retrieval request ID |
| chunk_ids | TEXT[] | | Array of chunk UUIDs |
| excerpts | TEXT[] | | Array of text excerpts |
| relevance_score | DOUBLE PRECISION | | Relevance score |
| retrieval_context | TEXT | | Retrieval context |
| created_at | TIMESTAMPTZ | NOT NULL, DEFAULT NOW() | Creation timestamp |
| created_by | UUID | | Creator user ID |

---

## Indexes

```sql
-- Document queries
idx_knowledge_documents_status (status, is_deleted)
idx_knowledge_documents_category (category_id, is_deleted)
idx_knowledge_documents_visibility (visibility, is_active)
idx_knowledge_documents_business_module (business_module, is_deleted)

-- Full-text search
idx_knowledge_documents_title_search USING gin(to_tsvector('english', title))

-- Time-based queries
idx_knowledge_documents_created_at (created_at DESC)

-- Version queries
idx_knowledge_versions_document (document_id, version_number DESC)

-- Chunk queries
idx_knowledge_chunks_document (document_id, chunk_index)

-- Metadata and tags
idx_knowledge_metadata_document (document_id)
idx_knowledge_tags_document (document_id)

-- Access log queries
idx_knowledge_access_log_timestamp (timestamp DESC)
idx_knowledge_access_log_action (action, timestamp DESC)

-- Citation queries
idx_knowledge_citations_document (document_id)
idx_knowledge_citations_request (retrieval_request_id)
```
