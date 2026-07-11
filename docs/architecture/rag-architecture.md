# RAG Architecture

**Version:** 1.0.0
**Last Updated:** 2026-07-11
**Module:** ai-service

---

## Overview

The Retrieval-Augmented Generation (RAG) architecture provides the foundation for grounding AI responses in enterprise knowledge. Part 5 implements the retrieval pipeline (knowledge retrieval, chunking, citation building). Vector search and LLM integration will follow in subsequent sprints.

---

## Current RAG Pipeline (Phase 3)

```
User Query
  ↓
Knowledge Retrieval Service
  ├── Category Filter
  ├── Language Filter
  ├── Visibility/Permission Filter
  ├── Keyword Matching
  ├── Chunk Selection
  └── Citation Building
  ↓
Retrieved Context (documents + chunks + citations)
  ↓
[Future: AI Gateway → LLM → Grounded Response]
```

---

## Components

### Knowledge Request
- Input: query string, category/language/permission filters
- Output: retrieved documents, chunks, and citations

### Knowledge Resolver
- Determines which knowledge sources to query
- Applies metadata, category, language, and permission filters

### Chunk Selector
- Selects relevant chunks from matched documents
- Limits: max 3 chunks per document, max 20 documents

### Context Builder
- Constructs retrieval context from selected chunks and excerpts
- Attaches citation metadata for provenance

### Citation Builder
- Creates KnowledgeCitationEntity with document reference, excerpts, relevance score
- Stores retrieval request ID for traceability

---

## Future RAG Pipeline (Phase 4+)

```
User Query
  ↓
Query Embedding (Vector DB)
  ↓
Hybrid Search (Keyword + Semantic)
  ↓
Knowledge Retrieval Service
  ├── Re-ranking
  ├── Context Assembly
  └── Citation Building
  ↓
AI Gateway
  ├── Prompt Assembly
  ├── LLM Call
  └── Grounded Response
```

---

## Data Model

### Retrieval Request
```json
{
  "query": "how to grow tomatoes",
  "categories": ["Grower Manuals"],
  "language": "en",
  "visibility": "INTERNAL",
  "maxChunks": 10
}
```

### Retrieval Response
```json
{
  "requestId": "uuid",
  "documents": [DocumentResponse],
  "chunks": [ChunkResponse],
  "citations": [CitationResponse]
}
```
