# Sprint 17 — Part 5: Enterprise Knowledge Platform & RAG Foundation

**Date:** 2026-07-11
**Module:** ai-service
**Lead:** Enterprise AI Platform Engineering Team

---

## Objective

Build the Enterprise Knowledge Platform — the centralized system for managing, storing, chunking, retrieving, and securing all knowledge documents used for AI retrieval-augmented generation (RAG). All business modules MUST use the Knowledge Platform for document retrieval. No direct access to document storage or embeddings.

---

## Deliverables

### Knowledge Platform Modules

| Module | Package | Purpose |
|--------|---------|---------|
| knowledge-core | `com.sporekart.ai.knowledge.domain` | Domain records, enums |
| knowledge-storage | `com.sporekart.ai.knowledge.infrastructure.persistence` | JPA entities, repositories |
| knowledge-index | `com.sporekart.ai.knowledge.application` | Document chunking service |
| knowledge-repository | `com.sporekart.ai.knowledge.infrastructure.persistence` | 9 repositories |
| knowledge-search | `com.sporekart.ai.knowledge.application` | Keyword search |
| knowledge-metadata | `com.sporekart.ai.knowledge.application` | Metadata & tag management |
| knowledge-retrieval | `com.sporekart.ai.knowledge.application` | Retrieval pipeline |
| knowledge-context | `com.sporekart.ai.knowledge.application` | Context building |
| knowledge-citations | `com.sporekart.ai.knowledge.application` | Citation tracking |
| knowledge-security | `com.sporekart.ai.knowledge.application` | RBAC, permissions |

### Document States

| State | Description |
|-------|-------------|
| DRAFT | Initial state, editable |
| PENDING_REVIEW | Submitted for approval |
| APPROVED | Approved, ready to publish |
| PUBLISHED | Active, available for retrieval |
| DEPRECATED | No longer recommended |
| ARCHIVED | Terminal state, not retrievable |

### Supported Knowledge Sources

- Training PDFs
- Training Videos Metadata
- Product Catalog
- Product Documentation
- FAQ
- Policies
- Help Articles
- Grower Manuals
- Supplier Documents
- Internal SOPs

### Document Visibility

| Level | Description |
|-------|-------------|
| PUBLIC | Accessible to anyone |
| INTERNAL | Accessible to authenticated users |
| RESTRICTED | Editors and above only |
| CONFIDENTIAL | Administrators only |

### Document Chunking

- `KnowledgeChunkingService` — splits document content into chunks
- Configurable chunk size (default: 1000 chars) and overlap (default: 100 chars)
- Sentence-boundary aware splitting
- Token count estimation (1 token ≈ 4 chars)
- Chunk ordering, versioning, and hierarchy support

### Retrieval Pipeline

- `KnowledgeRetrievalService` — keyword-based document retrieval
- Metadata filtering (category, language)
- Visibility/permission filtering
- Chunk selection and citation building
- Result ranking interface (keyword match)
- No vector search yet

### REST APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/knowledge/documents` | Create document |
| GET | `/api/v1/knowledge/documents` | List documents |
| GET | `/api/v1/knowledge/documents/{id}` | Get document |
| PUT | `/api/v1/knowledge/documents/{id}` | Update document |
| DELETE | `/api/v1/knowledge/documents/{id}` | Delete document |
| GET | `/api/v1/knowledge/categories` | List categories |
| GET | `/api/v1/knowledge/search` | Search documents |
| POST | `/api/v1/knowledge/retrieve` | Retrieve context |
| GET | `/api/v1/knowledge/citations` | Get citations |
| POST | `/api/v1/knowledge/documents/{id}/chunk` | Chunk document |
| GET | `/api/v1/knowledge/documents/{id}/chunks` | Get chunks |
| POST | `/api/v1/knowledge/documents/{id}/publish` | Publish document |

### Database Tables (Flyway V14)

- `knowledge_categories` — document categories
- `knowledge_documents` — document registry
- `knowledge_document_versions` — version history
- `knowledge_chunks` — document chunks
- `knowledge_metadata` — key-value metadata
- `knowledge_tags` — document tags
- `knowledge_sources` — source registry
- `knowledge_access_log` — access audit
- `knowledge_citations` — retrieval citations

### Redis Caching

| Cache | Prefix | TTL |
|-------|--------|-----|
| Document | `knowledge:doc:` | 30 min |
| Categories | `knowledge:cat:` | 60 min |
| Metadata | `knowledge:meta:` | 10 min |
| Search Results | `knowledge:search:` | 5 min |
| Citations | `knowledge:cite:` | 15 min |
| Retrieval Results | `knowledge:retrieve:` | 10 min |

### Kafka Events (topic: `knowledge-events`)

- `KnowledgeDocumentCreated`
- `KnowledgeDocumentUpdated`
- `KnowledgeDocumentDeleted`
- `KnowledgeChunkCreated`
- `KnowledgeChunkUpdated`
- `KnowledgeIndexed`
- `KnowledgeRetrieved`
- `KnowledgeSearchExecuted`

### Default Categories (15)

Training, Product Catalog, Product Documentation, FAQ, Policies, Help Articles, Grower Manuals, Supplier Documents, Internal SOPs, Marketing, Support, Technical Documentation, Compliance, Research, Operations

---

## Architecture

```
Client
  ↓
KnowledgeController (REST API)
  ↓
KnowledgeDocumentService / KnowledgeRetrievalService / KnowledgeChunkingService
  ↓
KnowledgeRedisCacheService ← → Redis
KnowledgeKafkaEventPublisher → Kafka
  ↓
JPA Repositories
  ↓
PostgreSQL (Supabase)
```

### Package Structure

```
com.sporekart.ai.knowledge/
  api/                    — Port interfaces
  application/            — Service layer
  domain/                 — Domain records, enums
  config/                 — @PostConstruct seed
  infrastructure/
    persistence/           — Entities, Repositories
  interfaces/
    rest/                 — Controller
      dto/                — Request/response records
```

---

## Test Coverage

| Test Class | Tests |
|------------|-------|
| KnowledgeDocumentServiceTest | 9 |
| KnowledgeChunkingServiceTest | 6 |
| KnowledgeMetadataServiceTest | 6 |
| KnowledgeRetrievalServiceTest | 5 |
| KnowledgeSecurityServiceTest | 9 |
| KnowledgeRedisCacheServiceTest | 6 |
| KnowledgeKafkaEventPublisherTest | 8 |
| KnowledgeControllerTest | 9 |
| **Total** | **58** |

---

## DO NOT IMPLEMENT (Future)

- Embeddings
- Vector Database
- Semantic Search
- LLM Calls
- AI Chat
- Prompt Execution
- Workflow Engine
- Content Generation
- Frontend Knowledge UI
- Mobile Applications

---

## Acceptance Criteria

✓ Knowledge Platform implemented
✓ Document ingestion implemented
✓ Chunking implemented
✓ Metadata management implemented
✓ Retrieval pipeline implemented
✓ REST APIs implemented
✓ Flyway V14 migration executed
✓ Redis caching configured
✓ Kafka events published
✓ Documentation updated
✓ Changelog updated
✓ Implementation log updated
✓ 58 tests passing
✓ Existing application unaffected
```

<｜DSML｜tool_calls>
<｜DSML｜invoke name="write">
<｜DSML｜parameter name="filePath" string="true">F:\The Pravara\clients\sporekart\sporekart\docs\sprints\phase-3\sprint-17-part-05.md