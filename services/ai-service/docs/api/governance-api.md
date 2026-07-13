# Governance API Reference

## Overview

Base URL: `/api/v1/governance`

All endpoints require JWT bearer authentication. Responses use RFC 9457 Problem Details format for errors.

## Endpoints

### 1. List Policies

`GET /api/v1/governance/policies`

Query Parameters:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `type` | String | No | Filter by PolicyType |
| `status` | String | No | Filter by PolicyStatus |
| `severity` | String | No | Filter by PolicySeverity |
| `scope` | String | No | Filter by ConfigScope |
| `module` | String | No | Filter by module target |
| `page` | Integer | No | Page number (default 0) |
| `size` | Integer | No | Page size (default 20) |

**Request Example:**
```
GET /api/v1/governance/policies?type=RATE_LIMIT&status=ACTIVE&page=0&size=10
Authorization: Bearer <jwt-token>
```

**Response Example (200 OK):**
```json
{
  "content": [
    {
      "id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
      "name": "Global Rate Limit",
      "type": "RATE_LIMIT",
      "severity": "BLOCKING",
      "status": "ACTIVE",
      "rules": {
        "maxRequests": 1000,
        "windowSeconds": 60,
        "burstSize": 1200
      },
      "scope": "GLOBAL",
      "moduleTarget": null,
      "createdAt": "2026-07-12T10:00:00Z",
      "updatedAt": "2026-07-12T10:00:00Z",
      "version": 1
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 1,
  "totalPages": 1
}
```

**Error Response (400 Bad Request):**
```json
{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "Invalid filter parameter: type=INVALID_TYPE",
  "instance": "/api/v1/governance/policies",
  "correlationId": "req-abc-123"
}
```

---

### 2. Create Policy

`POST /api/v1/governance/policies`

**Request Body:**
```json
{
  "name": "Conversation Rate Limit",
  "type": "RATE_LIMIT",
  "severity": "BLOCKING",
  "rules": {
    "maxRequests": 100,
    "windowSeconds": 60,
    "burstSize": 150
  },
  "scope": "MODULE",
  "moduleTarget": "conversation"
}
```

**Response (201 Created):**
```json
{
  "id": "b2c3d4e5-f6a7-8901-bcde-f12345678901",
  "name": "Conversation Rate Limit",
  "type": "RATE_LIMIT",
  "severity": "BLOCKING",
  "status": "DRAFT",
  "rules": {
    "maxRequests": 100,
    "windowSeconds": 60,
    "burstSize": 150
  },
  "scope": "MODULE",
  "moduleTarget": "conversation",
  "createdAt": "2026-07-12T11:00:00Z",
  "updatedAt": "2026-07-12T11:00:00Z",
  "version": 1
}
```

**Error Response (422 Unprocessable Entity):**
```json
{
  "type": "about:blank",
  "title": "Validation Error",
  "status": 422,
  "detail": "Policy name must be unique within scope",
  "instance": "/api/v1/governance/policies",
  "correlationId": "req-def-456",
  "errors": [
    {
      "field": "name",
      "message": "A policy with name 'Conversation Rate Limit' already exists in scope MODULE"
    }
  ]
}
```

---

### 3. Update Policy

`PUT /api/v1/governance/policies/{id}`

**Request Body:**
```json
{
  "name": "Conversation Rate Limit v2",
  "severity": "CRITICAL",
  "rules": {
    "maxRequests": 200,
    "windowSeconds": 120,
    "burstSize": 250
  },
  "version": 1
}
```

**Response (200 OK):**
```json
{
  "id": "b2c3d4e5-f6a7-8901-bcde-f12345678901",
  "name": "Conversation Rate Limit v2",
  "type": "RATE_LIMIT",
  "severity": "CRITICAL",
  "status": "DRAFT",
  "rules": {
    "maxRequests": 200,
    "windowSeconds": 120,
    "burstSize": 250
  },
  "scope": "MODULE",
  "moduleTarget": "conversation",
  "createdAt": "2026-07-12T11:00:00Z",
  "updatedAt": "2026-07-12T11:30:00Z",
  "version": 2
}
```

**Error Response (409 Conflict):**
```json
{
  "type": "about:blank",
  "title": "Conflict",
  "status": 409,
  "detail": "Policy was modified by another user. Reload and retry.",
  "instance": "/api/v1/governance/policies/b2c3d4e5-f6a7-8901-bcde-f12345678901",
  "correlationId": "req-ghi-789"
}
```

---

### 4. Delete/Archive Policy

`DELETE /api/v1/governance/policies/{id}`

Query Parameters:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `permanent` | Boolean | No | If false (default), archives the policy |

**Response (200 OK):**
```json
{
  "id": "b2c3d4e5-f6a7-8901-bcde-f12345678901",
  "status": "ARCHIVED",
  "message": "Policy archived successfully"
}
```

**Error Response (403 Forbidden):**
```json
{
  "type": "about:blank",
  "title": "Forbidden",
  "status": 403,
  "detail": "Only ADMINISTRATOR role can permanently delete policies",
  "instance": "/api/v1/governance/policies/b2c3d4e5-f6a7-8901-bcde-f12345678901",
  "correlationId": "req-jkl-012"
}
```

---

### 5. Query Audit Trail

`GET /api/v1/governance/audit`

Query Parameters:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `eventType` | String | No | Filter by AuditEventType |
| `actorId` | String | No | Filter by actor |
| `resource` | String | No | Filter by resource pattern |
| `from` | DateTime | No | Start of time range (ISO 8601) |
| `to` | DateTime | No | End of time range (ISO 8601) |
| `page` | Integer | No | Page number (default 0) |
| `size` | Integer | No | Page size (default 50, max 200) |

**Request Example:**
```
GET /api/v1/governance/audit?eventType=POLICY_ACTIVATED&from=2026-07-12T00:00:00Z&to=2026-07-12T23:59:59Z
Authorization: Bearer <jwt-token>
```

**Response (200 OK):**
```json
{
  "content": [
    {
      "id": "c3d4e5f6-a7b8-9012-cdef-123456789012",
      "eventType": "POLICY_ACTIVATED",
      "actorId": "user-admin-001",
      "actorRole": "ADMINISTRATOR",
      "resource": "governance:policy:a1b2c3d4-e5f6-7890-abcd-ef1234567890",
      "action": "ACTIVATE",
      "outcome": "SUCCESS",
      "details": "Policy 'Global Rate Limit' activated by administrator",
      "timestamp": "2026-07-12T10:30:00Z",
      "correlationId": "corr-admin-activate-001",
      "sourceIp": "192.168.1.100",
      "userAgent": "Mozilla/5.0 ..."
    }
  ],
  "page": 0,
  "size": 50,
  "totalElements": 1,
  "totalPages": 1
}
```

---

### 6. Export Audit Records

`POST /api/v1/governance/audit/export`

**Request Body:**
```json
{
  "eventTypes": ["POLICY_CREATED", "POLICY_ACTIVATED", "CONFIG_UPDATED"],
  "actorId": "user-admin-001",
  "from": "2026-07-01T00:00:00Z",
  "to": "2026-07-12T23:59:59Z",
  "format": "CSV"
}
```

**Response (200 OK):**
Content-Type: `text/csv`
Content-Disposition: `attachment; filename="audit-export-2026-07-12.csv"`

```csv
id,eventType,actorId,actorRole,resource,action,outcome,details,timestamp,correlationId,sourceIp
c3d4e5f6...,POLICY_ACTIVATED,user-admin-001,ADMINISTRATOR,governance:policy:...,ACTIVATE,SUCCESS,Policy 'Global Rate Limit' activated,2026-07-12T10:30:00Z,corr-...,192.168.1.100
```

---

### 7. Run Compliance Check

`GET /api/v1/governance/compliance`

Query Parameters:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `policyId` | UUID | No | Check specific policy |
| `module` | String | No | Check specific module |
| `run` | Boolean | No | If true, triggers new compliance check |

**Response (200 OK):**
```json
{
  "id": "d4e5f6a7-b8c9-0123-defa-234567890123",
  "policyId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "status": "COMPLIANT",
  "violations": [],
  "checkedAt": "2026-07-12T12:00:00Z",
  "module": "conversation",
  "details": "All active policies are compliant for module 'conversation'"
}
```

---

### 8. List Configuration

`GET /api/v1/governance/config`

Query Parameters:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `scope` | String | No | Filter by ConfigScope |
| `module` | String | No | Filter by module |
| `key` | String | No | Filter by key pattern |

**Response (200 OK):**
```json
{
  "content": [
    {
      "id": "e5f6a7b8-c9d0-1234-efab-345678901234",
      "scope": "MODULE",
      "module": "conversation",
      "key": "max.session.duration.minutes",
      "value": "60",
      "dataType": "INTEGER",
      "description": "Maximum conversation session duration in minutes",
      "tags": ["session", "timeout"],
      "encrypted": false,
      "version": 1
    }
  ]
}
```

---

### 9. Create Configuration Entry

`POST /api/v1/governance/config`

**Request Body:**
```json
{
  "scope": "MODULE",
  "module": "conversation",
  "key": "max.session.duration.minutes",
  "value": "60",
  "dataType": "INTEGER",
  "description": "Maximum conversation session duration in minutes",
  "tags": ["session", "timeout"],
  "encrypted": false
}
```

**Response (201 Created):**
Standard config entry response as above.

---

### 10. Import Configuration

`POST /api/v1/governance/config/import`

Content-Type: `application/json`

**Request Body:**
```json
{
  "format": "JSON",
  "overwriteExisting": true,
  "entries": [
    {
      "scope": "MODULE",
      "module": "conversation",
      "key": "max.session.duration.minutes",
      "value": "60",
      "dataType": "INTEGER",
      "description": "Maximum conversation session duration in minutes"
    },
    {
      "scope": "MODULE",
      "module": "prompt",
      "key": "default.template.cache.ttl",
      "value": "1800",
      "dataType": "INTEGER",
      "description": "Default template cache TTL in seconds"
    }
  ]
}
```

**Response (200 OK):**
```json
{
  "imported": 2,
  "skipped": 0,
  "failed": 0,
  "errors": []
}
```

---

### 11. Export Configuration

`GET /api/v1/governance/config/export`

Query Parameters:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `scope` | String | No | Filter by scope |
| `module` | String | No | Filter by module |
| `format` | String | No | JSON (default) or YAML |

**Response (200 OK):**
Content-Disposition: `attachment; filename="config-export-2026-07-12.json"`
```json
{
  "exportedAt": "2026-07-12T12:00:00Z",
  "entries": [...]
}
```

---

### 12. View Usage Quotas

`GET /api/v1/governance/quotas`

Query Parameters:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `module` | String | No | Filter by module |
| `userId` | String | No | Filter by user |
| `period` | String | No | DAILY, WEEKLY, MONTHLY |

**Response (200 OK):**
```json
{
  "content": [
    {
      "id": "f6a7b8c9-d0e1-2345-fabc-456789012345",
      "module": "conversation",
      "userId": "user-003",
      "operation": "AI_CHAT",
      "periodStart": "2026-07-12T00:00:00Z",
      "periodEnd": "2026-07-13T00:00:00Z",
      "count": 45,
      "limit": 100
    }
  ]
}
```

---

### 13. Reset Usage Quotas

`POST /api/v1/governance/quotas/reset`

**Request Body:**
```json
{
  "module": "conversation",
  "userId": "user-003",
  "operation": "AI_CHAT",
  "period": "DAILY"
}
```

**Response (200 OK):**
```json
{
  "reset": 1,
  "message": "Usage quotas reset successfully for conversation/user-003/AI_CHAT (DAILY)"
}
```

---

### 14. List Roles

`GET /api/v1/governance/roles`

**Response (200 OK):**
```json
{
  "content": [
    {
      "id": "a7b8c9d0-e1f2-3456-abcd-567890123456",
      "name": "AI_ADMINISTRATOR",
      "description": "Full access to all AI governance operations",
      "permissions": ["POLICY:*", "CONFIG:*", "AUDIT:*", "QUOTA:*", "ROLE:*", "COMPLIANCE:*"],
      "hierarchyLevel": 100
    },
    {
      "id": "b8c9d0e1-f2a3-4567-bcde-678901234567",
      "name": "AI_OPERATOR",
      "description": "Can manage policies and view audit",
      "permissions": ["POLICY:CREATE", "POLICY:UPDATE", "POLICY:ACTIVATE", "POLICY:DEACTIVATE", "AUDIT:READ"],
      "hierarchyLevel": 70
    },
    {
      "id": "c9d0e1f2-a3b4-5678-cdef-789012345678",
      "name": "AI_VIEWER",
      "description": "Read-only access to governance data",
      "permissions": ["POLICY:READ", "AUDIT:READ", "CONFIG:READ", "QUOTA:READ", "COMPLIANCE:READ"],
      "hierarchyLevel": 30
    }
  ]
}
```

---

### 15. Create/Update Role

`POST /api/v1/governance/roles`

**Request Body:**
```json
{
  "name": "AI_COMPLIANCE_OFFICER",
  "description": "Can view policies and run compliance checks",
  "permissions": ["POLICY:READ", "COMPLIANCE:READ", "COMPLIANCE:RUN", "AUDIT:READ"],
  "hierarchyLevel": 50
}
```

**Response (201 Created):**
Standard role definition response.

---

### 16. Assign Role to User

`POST /api/v1/governance/roles/{roleId}/assign`

**Request Body:**
```json
{
  "userId": "user-003",
  "scope": "MODULE",
  "scopeValue": "conversation"
}
```

**Response (200 OK):**
```json
{
  "userId": "user-003",
  "role": "AI_OPERATOR",
  "scope": "MODULE",
  "scopeValue": "conversation",
  "assignedAt": "2026-07-12T13:00:00Z"
}
```

## OpenAPI Specification

```yaml
openapi: 3.0.3
info:
  title: AI Governance API
  description: Enterprise AI Governance Platform REST API
  version: 1.0.0
servers:
  - url: /api/v1/governance
paths:
  /policies:
    get:
      summary: List policies
      parameters:
        - name: type
          in: query
          schema: { type: string }
        - name: status
          in: query
          schema: { type: string }
        - name: severity
          in: query
          schema: { type: string }
        - name: scope
          in: query
          schema: { type: string }
        - name: module
          in: query
          schema: { type: string }
      responses:
        '200':
          description: Paginated policy list
    post:
      summary: Create policy
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/PolicyRequest'
      responses:
        '201':
          description: Policy created
  /policies/{id}:
    put:
      summary: Update policy
      parameters:
        - name: id
          in: path
          required: true
          schema: { type: string, format: uuid }
      responses:
        '200':
          description: Policy updated
    delete:
      summary: Archive/delete policy
      parameters:
        - name: id
          in: path
          required: true
          schema: { type: string, format: uuid }
      responses:
        '200':
          description: Policy archived
  /audit:
    get:
      summary: Query audit trail
      parameters:
        - name: eventType
          in: query
          schema: { type: string }
        - name: actorId
          in: query
          schema: { type: string }
        - name: from
          in: query
          schema: { type: string, format: date-time }
        - name: to
          in: query
          schema: { type: string, format: date-time }
      responses:
        '200':
          description: Paginated audit records
  /compliance:
    get:
      summary: Run or view compliance check
      responses:
        '200':
          description: Compliance report
  /config:
    get:
      summary: List configuration entries
      responses:
        '200':
          description: Configuration list
    post:
      summary: Create/update configuration entry
      responses:
        '201':
          description: Configuration entry created
  /config/import:
    post:
      summary: Import configuration bundle
      responses:
        '200':
          description: Import result
  /config/export:
    get:
      summary: Export configuration bundle
      responses:
        '200':
          description: Configuration export
  /quotas:
    get:
      summary: View usage quotas
      responses:
        '200':
          description: Usage quota list
  /quotas/reset:
    post:
      summary: Reset usage quotas
      responses:
        '200':
          description: Quota reset result
  /roles:
    get:
      summary: List roles
      responses:
        '200':
          description: Role list
    post:
      summary: Create/update role
      responses:
        '201':
          description: Role created
  /roles/{roleId}/assign:
    post:
      summary: Assign role to user
      parameters:
        - name: roleId
          in: path
          required: true
          schema: { type: string, format: uuid }
      responses:
        '200':
          description: Role assigned
components:
  schemas:
    PolicyRequest:
      type: object
      properties:
        name: { type: string }
        type: { type: string }
        severity: { type: string }
        rules: { type: object }
        scope: { type: string }
        moduleTarget: { type: string }
      required: [name, type, severity, scope]
    PolicyResponse:
      type: object
      properties:
        id: { type: string, format: uuid }
        name: { type: string }
        type: { type: string }
        severity: { type: string }
        status: { type: string }
        rules: { type: object }
        scope: { type: string }
        moduleTarget: { type: string }
        createdAt: { type: string, format: date-time }
        updatedAt: { type: string, format: date-time }
        version: { type: integer }
    ConfigEntryRequest:
      type: object
      properties:
        scope: { type: string }
        module: { type: string }
        key: { type: string }
        value: { type: string }
        dataType: { type: string }
        description: { type: string }
        tags: { type: array, items: { type: string } }
        encrypted: { type: boolean }
      required: [scope, key, value, dataType]
    AuditRecordResponse:
      type: object
      properties:
        id: { type: string, format: uuid }
        eventType: { type: string }
        actorId: { type: string }
        actorRole: { type: string }
        resource: { type: string }
        action: { type: string }
        outcome: { type: string }
        details: { type: string }
        timestamp: { type: string, format: date-time }
        correlationId: { type: string }
        sourceIp: { type: string }
        userAgent: { type: string }
    ProblemDetails:
      type: object
      properties:
        type: { type: string, format: uri }
        title: { type: string }
        status: { type: integer }
        detail: { type: string }
        instance: { type: string }
        correlationId: { type: string }
        errors: { type: array, items: { type: object } }
```
