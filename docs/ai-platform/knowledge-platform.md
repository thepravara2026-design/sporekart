# Knowledge Platform

**Version:** 1.0.0
**Last Updated:** 2026-07-11

---

## Overview

The Knowledge Platform is the centralized document management and retrieval system for the SporeKart AI Platform. It stores, chunks, and retrieves enterprise knowledge documents for use in AI-powered features.

---

## Key Concepts

### Documents
Knowledge documents are the primary entities. Each document has a title, content, category, visibility level, and lifecycle status. Documents support versioning and soft deletion.

### Chunks
Documents are split into chunks for efficient retrieval. Chunking respects sentence boundaries and supports configurable size and overlap.

### Categories
Documents are organized into categories (e.g., Training, FAQ, Product Documentation). Categories are seeded at startup and can be managed via the API.

### Visibility
Documents have four visibility levels: PUBLIC, INTERNAL, RESTRICTED, and CONFIDENTIAL. Access is enforced by the KnowledgeSecurityService based on the user's role.

---

## API Endpoints

See [Knowledge API Reference](../api/knowledge-api.md) for full documentation.

### Quick Start

```bash
# Create a document
curl -X POST http://localhost:8088/api/v1/knowledge/documents \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Tomato Growing Guide",
    "content": "Tomatoes need full sun and well-drained soil...",
    "categoryId": "<category-uuid>",
    "visibility": "INTERNAL"
  }'

# Search documents
curl http://localhost:8088/api/v1/knowledge/search?q=tomato

# Retrieve context for AI
curl -X POST http://localhost:8088/api/v1/knowledge/retrieve \
  -H "Content-Type: application/json" \
  -d '{
    "query": "how to grow tomatoes",
    "maxChunks": 10
  }'
```

---

## Configuration

Knowledge platform features are configurable via `application.yml`:

```yaml
sporekart:
  ai:
    features:
      knowledge-enabled: true
      knowledge-caching: true
      knowledge-audit: true
    modules:
      knowledge:
        enabled: true
```
