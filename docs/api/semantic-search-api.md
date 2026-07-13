# Semantic Search API Reference

**Version:** 1.0.0
**Last Updated:** 2026-07-12
**Base URL:** `/api/v1/semantic`
**Content-Type:** `application/json`

---

## Overview

The Semantic Search API provides endpoints for generating embeddings, executing semantic and hybrid searches, computing similarity scores, managing vector indexes, and monitoring search system health. All endpoints require authentication.

---

## Authentication

All API requests must include a valid JWT bearer token in the `Authorization` header:

```
Authorization: Bearer <token>
```

Tokens are issued by the SporeKart Identity Provider and must include the user's roles and permissions for access control.

---

## Error Format

All errors follow RFC 9457 (Problem Details for HTTP APIs):

```json
{
  "type": "https://api.sporekart.com/errors/semantic/validation-error",
  "title": "Validation Error",
  "status": 400,
  "detail": "Query must not be empty",
  "instance": "/api/v1/semantic/search",
  "correlationId": "c7f3a2b1-4d5e-6f7a-8b9c-0d1e2f3a4b5c",
  "timestamp": "2026-07-12T10:30:00Z"
}
```

---

## Endpoints

### Generate Embedding

```
POST /api/v1/semantic/embed
```

**Description:** Generates a vector embedding for the provided text content.

**Request Body:**
```json
{
  "content": "Tomatoes need full sun and well-drained soil with a pH of 6.0 to 6.8",
  "provider": "openai",
  "model": "text-embedding-3-small",
  "metadata": {
    "sourceId": "uuid",
    "sourceType": "knowledge_chunk"
  }
}
```

**Response (200):**
```json
{
  "success": true,
  "data": {
    "id": "uuid",
    "embedding": [0.0123, -0.0456, 0.0789, ...],
    "dimensions": 1536,
    "provider": "openai",
    "model": "text-embedding-3-small",
    "tokenCount": 42,
    "contentHash": "a1b2c3d4e5f6...",
    "createdAt": "2026-07-12T10:30:00Z"
  }
}
```

**Status Codes:**
| Code | Description |
|------|-------------|
| 200 | Embedding generated successfully |
| 400 | Validation error (empty content, invalid provider) |
| 401 | Unauthorized (missing/invalid token) |
| 403 | Forbidden (insufficient permissions) |
| 502 | Embedding provider unavailable |

---

### Execute Search

```
POST /api/v1/semantic/search
```

**Description:** Executes a semantic, keyword, or hybrid search against the vector index.

**Request Body:**
```json
{
  "query": "How to grow tomatoes in dry climate",
  "searchType": "HYBRID",
  "limit": 10,
  "offset": 0,
  "filters": {
    "category": "Grower Manuals",
    "language": "en",
    "visibility": "INTERNAL",
    "businessModule": "training"
  },
  "hybridConfig": {
    "semanticWeight": 0.7,
    "keywordWeight": 0.3,
    "rrfConstant": 60
  },
  "minScore": 0.5,
  "includeVectors": false
}
```

**Response (200):**
```json
{
  "success": true,
  "data": {
    "query": "How to grow tomatoes in dry climate",
    "searchType": "HYBRID",
    "totalResults": 42,
    "results": [
      {
        "rank": 1,
        "id": "uuid",
        "content": "Tomatoes in arid regions require drip irrigation...",
        "sourceId": "uuid",
        "sourceType": "knowledge_chunk",
        "score": 0.92,
        "metadata": {
          "category": "Grower Manuals",
          "language": "en",
          "documentTitle": "Dry Climate Tomato Farming"
        }
      }
    ],
    "latencyMs": 145,
    "cached": false
  }
}
```

**Status Codes:**
| Code | Description |
|------|-------------|
| 200 | Search executed successfully |
| 400 | Validation error (empty query, invalid searchType) |
| 401 | Unauthorized |
| 429 | Rate limit exceeded |

**Search Types:**
| searchType | Description |
|------------|-------------|
| `SEMANTIC` | Pure vector similarity search via pgvector |
| `KEYWORD` | BM25 full-text search via PostgreSQL |
| `HYBRID` | RRF-fused semantic + keyword results |
| `CROSS_ENCODER` | Semantic search with cross-encoder re-ranking |
| `MULTI_VECTOR` | Multi-query vector search |
| `CONTEXTUAL` | Context-aware document-level search |

---

### Compute Similarity

```
POST /api/v1/semantic/similarity
```

**Description:** Computes similarity score between two text inputs.

**Request Body:**
```json
{
  "source": "Tomatoes require full sun exposure",
  "target": "Tomatoes need at least 6-8 hours of direct sunlight daily",
  "algorithm": "COSINE"
}
```

**Response (200):**
```json
{
  "success": true,
  "data": {
    "sourceHash": "a1b2c3d4e5...",
    "targetHash": "f6g7h8i9j0...",
    "algorithm": "COSINE",
    "score": 0.87,
    "latencyMs": 35,
    "cached": false
  }
}
```

**Status Codes:**
| Code | Description |
|------|-------------|
| 200 | Similarity computed successfully |
| 400 | Validation error |
| 401 | Unauthorized |

**Algorithms:**
| algorithm | Description | Range |
|-----------|-------------|-------|
| `COSINE` | Cosine similarity | [0, 1] |
| `EUCLIDEAN` | Euclidean distance (inverted) | [0, 1] |
| `DOT_PRODUCT` | Dot product (normalized) | [0, 1] |

---

### List Indexes

```
GET /api/v1/semantic/index
```

**Description:** Lists all registered vector indexes with metadata.

**Response (200):**
```json
{
  "success": true,
  "data": [
    {
      "id": "uuid",
      "name": "idx_embeddings_cosine",
      "indexType": "HNSW",
      "status": "ACTIVE",
      "vectorCount": 15420,
      "dimensions": 1536,
      "config": {
        "m": 16,
        "efConstruction": 64,
        "efSearch": 40
      },
      "lastRebuiltAt": "2026-07-12T03:00:00Z",
      "createdAt": "2026-07-10T00:00:00Z"
    }
  ]
}
```

**Status Codes:**
| Code | Description |
|------|-------------|
| 200 | Indexes retrieved successfully |
| 401 | Unauthorized |
| 403 | Forbidden (admin role required) |

---

### Rebuild Index

```
POST /api/v1/semantic/index/rebuild
```

**Description:** Triggers a rebuild of a vector index. This operation may take several minutes for large collections.

**Request Body:**
```json
{
  "indexName": "idx_embeddings_cosine",
  "indexType": "HNSW",
  "config": {
    "m": 16,
    "efConstruction": 64
  }
}
```

**Response (202):**
```json
{
  "success": true,
  "data": {
    "indexName": "idx_embeddings_cosine",
    "indexType": "HNSW",
    "status": "REBUILDING",
    "estimatedDurationMs": 30000,
    "startedAt": "2026-07-12T10:30:00Z"
  }
}
```

**Status Codes:**
| Code | Description |
|------|-------------|
| 202 | Rebuild accepted and started |
| 400 | Validation error (invalid index name/type) |
| 401 | Unauthorized |
| 403 | Forbidden (admin role required) |
| 409 | Index already rebuilding |

---

### Get Statistics

```
GET /api/v1/semantic/statistics
```

**Description:** Returns search system statistics including index sizes, cache hit ratios, and query volume.

**Response (200):**
```json
{
  "success": true,
  "data": {
    "totalEmbeddings": 15420,
    "totalIndexes": 3,
    "activeIndexes": 3,
    "searchCount24h": 12500,
    "averageLatencyMs": 120,
    "cacheHitRatio": 0.68,
    "embeddingCountByProvider": {
      "openai": 12000,
      "gemini": 3420
    },
    "searchCountByType": {
      "SEMANTIC": 5000,
      "KEYWORD": 3500,
      "HYBRID": 3000,
      "CROSS_ENCODER": 500,
      "MULTI_VECTOR": 300,
      "CONTEXTUAL": 200
    },
    "indexes": [
      {
        "name": "idx_embeddings_cosine",
        "vectorCount": 15420,
        "dimensions": 1536,
        "status": "ACTIVE"
      }
    ],
    "recordedAt": "2026-07-12T10:30:00Z"
  }
}
```

**Status Codes:**
| Code | Description |
|------|-------------|
| 200 | Statistics retrieved successfully |
| 401 | Unauthorized |

---

### Health Check

```
GET /api/v1/semantic/health
```

**Description:** Health check endpoint for the Semantic Intelligence Platform.

**Response (200):**
```json
{
  "success": true,
  "data": {
    "status": "UP",
    "components": {
      "postgresql": { "status": "UP", "latencyMs": 5 },
      "redis": { "status": "UP", "latencyMs": 2 },
      "kafka": { "status": "UP", "latencyMs": 3 },
      "openai": { "status": "UP", "latencyMs": 120 },
      "gemini": { "status": "UP", "latencyMs": 110 },
      "pgvector": { "status": "UP", "vectorCount": 15420 }
    },
    "uptime": "72h 15m 30s",
    "version": "1.0.0"
  }
}
```

**Response (503) — Degraded:**
```json
{
  "success": true,
  "data": {
    "status": "DEGRADED",
    "components": {
      "postgresql": { "status": "UP", "latencyMs": 5 },
      "redis": { "status": "UP", "latencyMs": 2 },
      "kafka": { "status": "DOWN", "latencyMs": 0 },
      "openai": { "status": "UP", "latencyMs": 120 },
      "gemini": { "status": "DOWN", "latencyMs": 0 }
    },
    "uptime": "72h 15m 30s",
    "version": "1.0.0"
  }
}
```

**Status Codes:**
| Code | Description |
|------|-------------|
| 200 | All components healthy |
| 503 | One or more components degraded/down |

---

## Rate Limiting

| Endpoint | Limit | Window |
|----------|-------|--------|
| `POST /api/v1/semantic/embed` | 60 requests | 1 minute |
| `POST /api/v1/semantic/search` | 100 requests | 1 minute |
| `POST /api/v1/semantic/similarity` | 200 requests | 1 minute |
| `GET /api/v1/semantic/index` | 30 requests | 1 minute |
| `POST /api/v1/semantic/index/rebuild` | 5 requests | 1 hour |
| `GET /api/v1/semantic/statistics` | 30 requests | 1 minute |
| `GET /api/v1/semantic/health` | 60 requests | 1 minute |

Rate limit exceeded response (429):
```json
{
  "type": "https://api.sporekart.com/errors/semantic/rate-limit-exceeded",
  "title": "Rate Limit Exceeded",
  "status": 429,
  "detail": "Too many requests. Please retry after 30 seconds.",
  "instance": "/api/v1/semantic/search",
  "correlationId": "d8e4f5a6-7b8c-9d0e-1f2a-3b4c5d6e7f8a",
  "timestamp": "2026-07-12T10:30:00Z"
}
```

---

## Error Responses (RFC 9457)

| HTTP Status | type | title | Common Causes |
|-------------|------|-------|---------------|
| 400 | `https://api.sporekart.com/errors/semantic/validation-error` | Validation Error | Empty query, invalid searchType, missing content |
| 401 | `https://api.sporekart.com/errors/semantic/unauthorized` | Unauthorized | Missing/invalid JWT token |
| 403 | `https://api.sporekart.com/errors/semantic/forbidden` | Forbidden | Insufficient role for index management |
| 404 | `https://api.sporekart.com/errors/semantic/not-found` | Not Found | Index not found |
| 409 | `https://api.sporekart.com/errors/semantic/conflict` | Conflict | Index already rebuilding |
| 429 | `https://api.sporekart.com/errors/semantic/rate-limit-exceeded` | Rate Limit Exceeded | Too many requests |
| 502 | `https://api.sporekart.com/errors/semantic/provider-unavailable` | Provider Unavailable | OpenAI/Gemini API down |
| 503 | `https://api.sporekart.com/errors/semantic/service-unavailable` | Service Unavailable | Core dependencies unavailable |
