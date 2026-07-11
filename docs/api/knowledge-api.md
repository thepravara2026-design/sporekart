# Knowledge Platform API Reference

**Version:** 1.0.0
**Base URL:** `/api/v1/knowledge`
**Content-Type:** `application/json`

---

## Documents

### Create Document

```
POST /api/v1/knowledge/documents
```

**Request Body:**
```json
{
  "categoryId": "uuid",
  "title": "string (required)",
  "description": "string",
  "content": "string",
  "language": "string (default: en)",
  "author": "string",
  "source": "string",
  "visibility": "PUBLIC | INTERNAL | RESTRICTED | CONFIDENTIAL",
  "businessModule": "string",
  "region": "string",
  "tags": ["string"]
}
```

**Response (201):**
```json
{
  "success": true,
  "data": {
    "id": "uuid",
    "categoryId": "uuid",
    "categoryName": "string",
    "title": "string",
    "description": "string",
    "content": "string",
    "language": "string",
    "author": "string",
    "source": "string",
    "status": "DRAFT",
    "visibility": "INTERNAL",
    "businessModule": "string",
    "region": "string",
    "currentVersion": 1,
    "active": true,
    "createdBy": "uuid",
    "createdAt": "timestamp",
    "updatedBy": null,
    "updatedAt": null
  }
}
```

### List Documents

```
GET /api/v1/knowledge/documents
```

**Response (200):**
```json
{
  "success": true,
  "data": [DocumentResponse]
}
```

### Get Document

```
GET /api/v1/knowledge/documents/{id}
```

**Response (200):** Single DocumentResponse

**Error (404):**
```json
{
  "success": false,
  "errorCode": "KNOWLEDGE-404",
  "errorMessage": "Document not found: {id}"
}
```

### Update Document

```
PUT /api/v1/knowledge/documents/{id}
```

**Request Body:**
```json
{
  "categoryId": "uuid",
  "title": "string",
  "content": "string",
  "description": "string",
  "language": "string",
  "source": "string",
  "visibility": "string",
  "businessModule": "string",
  "region": "string"
}
```

All fields optional. Updating content creates a new version.

### Delete Document

```
DELETE /api/v1/knowledge/documents/{id}
```

**Response (200):** Soft deletes the document.

### Publish Document

```
POST /api/v1/knowledge/documents/{id}/publish
```

Changes status to PUBLISHED.

---

## Categories

### List Categories

```
GET /api/v1/knowledge/categories
```

**Response (200):**
```json
{
  "success": true,
  "data": [
    {
      "id": "uuid",
      "name": "FAQ",
      "description": null,
      "displayOrder": 3,
      "active": true
    }
  ]
}
```

15 default categories seeded at startup.

---

## Search

### Search Documents

```
GET /api/v1/knowledge/search?q={query}&category={categoryId}&businessModule={module}
```

All parameters optional.

---

## Retrieval

### Retrieve Context

```
POST /api/v1/knowledge/retrieve
```

**Request Body:**
```json
{
  "query": "how to grow tomatoes",
  "categories": ["Grower Manuals"],
  "language": "en",
  "visibility": "INTERNAL",
  "businessModule": "training",
  "maxChunks": 10
}
```

**Response (200):**
```json
{
  "success": true,
  "data": {
    "requestId": "uuid",
    "documents": [DocumentResponse],
    "chunks": [ChunkResponse],
    "citations": [CitationResponse]
  }
}
```

### Get Citations

```
GET /api/v1/knowledge/citations?requestId={requestId}
```

---

## Chunking

### Chunk Document

```
POST /api/v1/knowledge/documents/{id}/chunk?chunkSize=1000&overlap=100
```

### Get Chunks

```
GET /api/v1/knowledge/documents/{id}/chunks
```

---

## Error Codes

| Code | HTTP Status | Description |
|------|-------------|-------------|
| KNOWLEDGE-400 | 400 | Validation error |
| KNOWLEDGE-403 | 403 | Permission denied |
| KNOWLEDGE-404 | 404 | Resource not found |

---

## Response Envelope

All API responses use the standard `ResponseEnvelope`:

```json
{
  "success": true|false,
  "data": {...},
  "errorCode": "string",
  "errorMessage": "string",
  "timestamp": "2026-07-11T...",
  "correlationId": "string",
  "metadata": {...}
}
```
