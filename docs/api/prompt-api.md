# Prompt Management API

**Version:** 1.0.0
**Last Updated:** 2026-07-11
**Base Path:** `/api/v1/ai/prompts`

---

## Standard Response Envelope

Every response follows RFC 9457 Problem Details via `ResponseEnvelope<T>`:

```json
{
  "success": true | false,
  "data": { ... } | null,
  "errorCode": "AI-XXX" | null,
  "errorMessage": "string" | null,
  "timestamp": "2026-07-11T12:00:00Z",
  "correlationId": "uuid" | null,
  "metadata": {}
}
```

---

## Endpoints

### GET /api/v1/ai/prompts

List, search, or filter prompt templates.

**Query Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| `category` | UUID | Filter by category ID |
| `status` | String | Filter by status (DRAFT, PUBLISHED, etc.) |
| `q` | String | Search query (name/description) |

**Success Response (200):**
```json
{
  "success": true,
  "data": [
    {
      "id": "uuid",
      "categoryId": "uuid",
      "categoryName": "Customer Support",
      "name": "welcome-message",
      "description": "Welcome message for new customers",
      "templateText": "Welcome {{customerName}} to {{productName}}!",
      "status": "PUBLISHED",
      "currentVersion": 3,
      "tags": ["welcome", "onboarding"],
      "createdBy": "uuid",
      "createdAt": "2026-07-11T12:00:00Z",
      "updatedBy": "uuid",
      "updatedAt": "2026-07-11T12:30:00Z"
    }
  ]
}
```

---

### GET /api/v1/ai/prompts/{id}

Get a specific prompt template by ID.

**Success Response (200):** Same structure as above.

**Error Response (404):**
```json
{
  "success": false,
  "data": null,
  "errorCode": "AI-012",
  "errorMessage": "Template not found: {id}"
}
```

---

### POST /api/v1/ai/prompts

Create a new prompt template.

**Request Body:**
```json
{
  "categoryId": "uuid",
  "name": "welcome-message",
  "description": "Welcome message",
  "templateText": "Welcome {{customerName}}!",
  "variables": [
    {
      "name": "customerName",
      "type": "STRING",
      "required": true,
      "defaultValue": null,
      "description": "Customer display name",
      "validationRegex": null
    }
  ]
}
```

**Success Response (201):**
```json
{
  "success": true,
  "data": {
    "id": "uuid",
    "categoryId": "uuid",
    "categoryName": "Customer Support",
    "name": "welcome-message",
    "description": "Welcome message",
    "templateText": "Welcome {{customerName}}!",
    "status": "DRAFT",
    "currentVersion": 1
  }
}
```

**Error Response (400):**
```json
{
  "success": false,
  "data": null,
  "errorCode": "AI-006",
  "errorMessage": "Template already exists in this category: welcome-message"
}
```

---

### PUT /api/v1/ai/prompts/{id}

Update a prompt template.

**Request Body:**
```json
{
  "categoryId": "uuid",
  "name": "updated-name",
  "description": "Updated description",
  "templateText": "Updated template {{variable}}"
}
```

**Success Response (200):** Returns updated template.

---

### DELETE /api/v1/ai/prompts/{id}

Soft-delete a prompt template.

**Success Response (204):** No content.

---

### POST /api/v1/ai/prompts/{id}/publish

Publish a prompt template (must be APPROVED or DRAFT).

**Success Response (200):**
```json
{
  "success": true,
  "data": {
    "id": "uuid",
    "status": "PUBLISHED",
    "currentVersion": 2
  }
}
```

---

### POST /api/v1/ai/prompts/{id}/rollback?version=1

Rollback to a specific version. Creates a new version with the rolled-back content.

**Query Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `version` | int | Yes | Target version number |
| `rolledBackBy` | UUID | No | User performing rollback |

**Success Response (200):** Returns the new version.

---

### POST /api/v1/ai/prompts/render

Render a prompt template with variables.

**Request Body:**
```json
{
  "templateText": "Hello {{name}}, your {{role}} is confirmed!",
  "variables": {
    "name": "Alice",
    "role": "admin"
  }
}
```

**Success Response (200):**
```json
{
  "success": true,
  "data": {
    "renderedText": "Hello Alice, your admin is confirmed!",
    "success": true,
    "error": null
  }
}
```

**Error Response (400):**
```json
{
  "success": false,
  "data": {
    "renderedText": null,
    "success": false,
    "error": "Unresolved variable: {{role}}"
  }
}
```

---

### GET /api/v1/ai/prompts/categories

List all prompt categories.

**Success Response (200):**
```json
{
  "success": true,
  "data": [
    {
      "id": "uuid",
      "name": "Customer Support",
      "description": "Customer support assistant prompts",
      "icon": null,
      "displayOrder": 0,
      "active": true
    }
  ]
}
```

---

### GET /api/v1/ai/prompts/history

Get paginated audit history for all prompts.

**Query Parameters:**
| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `page` | int | 0 | Page number |
| `size` | int | 50 | Page size |

**Success Response (200):**
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "id": "uuid",
        "templateId": "uuid",
        "versionId": null,
        "action": "TEMPLATE_CREATED",
        "entityType": "TEMPLATE",
        "entityId": "uuid",
        "previousValue": null,
        "newValue": null,
        "changedBy": "uuid",
        "changedAt": "2026-07-11T12:00:00Z",
        "details": "Created template: welcome-message"
      }
    ],
    "pageable": { ... },
    "totalElements": 1
  }
}
```

---

### GET /api/v1/ai/prompts/{id}/versions

List all versions of a prompt template.

**Success Response (200):**
```json
{
  "success": true,
  "data": [
    {
      "id": "uuid",
      "templateId": "uuid",
      "versionNumber": 3,
      "templateText": "Welcome {{customerName}}!",
      "status": "PUBLISHED",
      "changeNotes": "Added localization support",
      "activationDate": "2026-07-11T12:00:00Z",
      "createdBy": "uuid",
      "createdAt": "2026-07-11T12:00:00Z",
      "approvedBy": "uuid",
      "approvedAt": "2026-07-11T12:05:00Z"
    }
  ]
}
```

---

### GET /api/v1/ai/prompts/{id}/history

Get audit history for a specific template.

---

### POST /api/v1/ai/prompts/{id}/submit

Submit a DRAFT template for approval.

**Success Response (200):** Template status changes to PENDING_APPROVAL.

---

### POST /api/v1/ai/prompts/{id}/approve

Approve a PENDING_APPROVAL template.

**Success Response (200):** Template status changes to APPROVED.

---

### POST /api/v1/ai/prompts/{id}/deprecate

Deprecate a PUBLISHED template.

**Success Response (200):** Template status changes to DEPRECATED.

---

### POST /api/v1/ai/prompts/import

Import prompts from a JSON export file.

**Request Body:** Full JSON export content.

**Success Response (200):**
```json
{
  "success": true,
  "data": {
    "imported": 5
  }
}
```

---

### GET /api/v1/ai/prompts/export

Export all prompts as JSON.

**Success Response (200):** Returns JSON content with `Content-Type: application/json`.

---

## Error Codes

| Code | HTTP Status | Description |
|------|-------------|-------------|
| AI-006 | 400 | Validation failure (duplicate name, missing fields, injection detected, payload too large) |
| AI-008 | 400 | Render error (unresolved variables, null template text) |
| AI-012 | 404 | Template, version, or category not found |

---

## OpenAPI

The API is documented via SpringDoc OpenAPI 3.0 at `/v3/api-docs` and Swagger UI at `/swagger-ui.html`. All endpoints are tagged under **Prompt Management**.
