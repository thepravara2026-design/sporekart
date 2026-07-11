# Knowledge Platform Architecture

**Version:** 1.0.0
**Last Updated:** 2026-07-11
**Module:** ai-service

---

## Overview

The Knowledge Platform is the authoritative source for all AI knowledge retrieval in the SporeKart Enterprise AI Platform. It provides centralized document management, chunking, metadata management, and retrieval services.

---

## Architecture Principles

1. **Single Source of Truth** — All knowledge documents are managed through the Knowledge Platform
2. **Service Isolation** — Business modules never access document storage or embeddings directly
3. **Pluggable Sources** — New knowledge source types can be added without modifying core logic
4. **Security First** — Document visibility, RBAC, and access logging at every layer
5. **Observability** — All retrievals, searches, and access events are logged and metricized

---

## Architecture Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                    REST API Layer                            │
│  POST/GET/PUT/DELETE /api/v1/knowledge/documents/*          │
│  GET /api/v1/knowledge/categories                           │
│  GET /api/v1/knowledge/search                               │
│  POST /api/v1/knowledge/retrieve                            │
│  GET /api/v1/knowledge/citations                            │
└──────────────────────┬──────────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────────┐
│                 Application Service Layer                    │
│  ┌──────────────┐ ┌──────────────┐ ┌──────────────────┐    │
│  │ DocumentSvc  │ │ ChunkingSvc  │ │ RetrievalSvc     │    │
│  └──────┬───────┘ └──────┬───────┘ └───────┬──────────┘    │
│  ┌──────▼───────┐ ┌──────▼───────┐ ┌──────▼──────────┐    │
│  │ MetadataSvc  │ │ SecuritySvc  │ │ CitationSvc     │    │
│  └──────────────┘ └──────────────┘ └──────────────────┘    │
└──────────────────────┬──────────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────────┐
│              Infrastructure Layer                            │
│  ┌──────────────────┐ ┌──────────────────┐                  │
│  │ Redis Cache      │ │ Kafka Publisher  │                  │
│  └──────┬───────────┘ └──────┬───────────┘                  │
│  ┌──────▼────────────────────▼───────────┐                  │
│  │         JPA Repositories               │                 │
│  │  Document │ Chunk │ Category │ Tag...  │                 │
│  └────────────────────────────────────────┘                 │
└──────────────────────┬──────────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────────┐
│              PostgreSQL (Supabase)                           │
│  knowledge_documents │ knowledge_chunks │ ...               │
└─────────────────────────────────────────────────────────────┘
```

---

## Module Descriptions

### knowledge-core (`domain/`)
- Domain records: `KnowledgeDocument`, `KnowledgeChunk`, `KnowledgeCitation`
- Enums: `DocumentStatus`, `DocumentVisibility`

### knowledge-storage (`infrastructure/persistence/`)
- 9 JPA entities with UUID PKs, soft deletes, audit columns
- 9 Spring Data JPA repositories with custom queries

### knowledge-index (`application/KnowledgeChunkingService`)
- Configurable chunk size (default: 1000 chars) and overlap (default: 100 chars)
- Sentence-boundary aware splitting
- Token estimation (1 token ≈ 4 chars)
- Chunk quality validation

### knowledge-search (`application/KnowledgeDocumentService.searchDocuments()`)
- Keyword-based search across title, description, and content
- Category and business module filtering
- Redis cache for search results (5 min TTL)

### knowledge-metadata (`application/KnowledgeMetadataService`)
- Key-value metadata storage per document
- Tag management
- Category listing with Redis caching

### knowledge-retrieval (`application/KnowledgeRetrievalService`)
- Multi-filter retrieval pipeline (category, language, visibility, business module)
- Keyword matching on document content
- Chunk selection and citation building
- Access logging for every retrieval

### knowledge-security (`application/KnowledgeSecurityService`)
- Role-based access control (ADMINISTRATOR, KNOWLEDGE_MANAGER, CONTENT_EDITOR, USER)
- Document visibility enforcement (PUBLIC, INTERNAL, RESTRICTED, CONFIDENTIAL)
- Owner-based edit/delete permissions
- Access log recording

---

## Data Flow

### Document Ingestion
```
POST /api/v1/knowledge/documents
  → KnowledgeDocumentService.createDocument()
    → Validate title and category
    → Save KnowledgeDocumentEntity
    → Save tags
    → Create initial version
    → Log access (DOCUMENT_CREATED)
    → Publish Kafka event (KnowledgeDocumentCreated)
    → Invalidate Redis cache
  ← DocumentResponse
```

### Document Retrieval
```
POST /api/v1/knowledge/retrieve
  → KnowledgeRetrievalService.retrieve()
    → Filter by category/language/visibility/module
    → Keyword match on query
    → Select chunks (max 3 per document)
    → Build citations with excerpts
    → Log access (DOCUMENT_RETRIEVED)
    → Publish Kafka event (KnowledgeRetrieved)
  ← RetrieveResponse with documents, chunks, citations
```

---

## Caching Strategy

| Cache Key Pattern | TTL | Invalidation |
|---|---|---|
| `knowledge:doc:{id}` | 30 min | On update/delete/publish |
| `knowledge:cat:all` | 60 min | On category change |
| `knowledge:meta:{id}` | 10 min | On metadata change |
| `knowledge:search:{hash}` | 5 min | None (ephemeral) |
| `knowledge:retrieve:{id}` | 10 min | None (ephemeral) |
| `knowledge:cite:{id}` | 15 min | On document update |

---

## Security Model

| Role | Create | Read | Edit | Delete | Publish |
|------|--------|------|------|--------|---------|
| ADMINISTRATOR | ✓ | ✓ | ✓ | ✓ | ✓ |
| KNOWLEDGE_MANAGER | ✓ | ✓ | ✓ | ✓ | ✓ |
| CONTENT_EDITOR | ✓ | ✓ | ✓ | - | - |
| USER (owner) | ✓ | ✓ | ✓ | ✓ | - |
| USER (non-owner) | ✓ | ✓ | - | - | - |
