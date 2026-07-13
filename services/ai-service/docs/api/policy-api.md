# Policy Engine API Reference

Base path: `/api/v1/policies`

All endpoints require authentication (permitted in `SecurityConfig`). Error responses follow RFC 9457 Problem Details format via `PolicyErrorDto`.

## Endpoints

### 1. List Policies

```
GET /api/v1/policies
```

Query parameters:
| Name | Type | Required | Description |
|------|------|----------|-------------|
| module | string | No | Filter by module name |
| scope | string | No | Filter by scope (GLOBAL, MODULE, ROLE, etc.) |

Response `200 OK`:
```json
{
  "policies": [
    {
      "id": "uuid",
      "name": "string",
      "description": "string",
      "type": "GLOBAL",
      "status": "ACTIVE",
      "scope": "GLOBAL",
      "priority": 0,
      "module": "string",
      "active": true,
      "metadata": {}
    }
  ],
  "total": 1
}
```

### 2. Get Policy

```
GET /api/v1/policies/{id}
```

Path parameter: `id` — UUID of the policy

Response `200 OK`:
```json
{
  "id": "uuid",
  "name": "string",
  "description": "string",
  "type": "GLOBAL",
  "status": "ACTIVE",
  "scope": "GLOBAL",
  "priority": 0,
  "module": "string",
  "active": true,
  "metadata": {}
}
```

Response `404 Not Found`: When policy ID does not exist.

### 3. Create Policy

```
POST /api/v1/policies
```

Request body:
```json
{
  "name": "Content Moderation Policy",
  "description": "Blocks harmful content generation",
  "type": "MODULE",
  "scope": "MODULE",
  "priority": 100,
  "module": "content",
  "rules": {},
  "active": true
}
```

Response `201 Created`: Returns the created policy as `PolicyResponseDto`.

Creates policy with status DRAFT, severity INFO, and publishes `PolicyCreated` Kafka event.

### 4. Update Policy

```
PUT /api/v1/policies/{id}
```

Path parameter: `id` — UUID of the policy

Request body: Same as Create Policy

Response `200 OK`: Returns updated policy. Publishes `PolicyUpdated` Kafka event.

Response `404 Not Found`: When policy ID does not exist.

### 5. Delete Policy

```
DELETE /api/v1/policies/{id}
```

Response `204 No Content`: Archives the policy (soft-delete via `is_deleted` flag). Publishes `PolicyDeleted` Kafka event.

### 6. Evaluate Policy

```
POST /api/v1/policies/evaluate
```

Request body:
```json
{
  "module": "content",
  "action": "generate",
  "payload": {
    "contentType": "blog"
  },
  "context": {},
  "userId": "user-123",
  "roles": ["editor"]
}
```

Response `200 OK`:
```json
{
  "requestId": "uuid",
  "decision": "ALLOW",
  "violations": [],
  "totalTimeMs": 15,
  "passed": true
}
```

Response with violations:
```json
{
  "requestId": "uuid",
  "decision": "DENY",
  "violations": [
    {
      "ruleName": "block-harmful-content",
      "message": "Rule matched: block-harmful-content",
      "severity": "BLOCKING",
      "details": {
        "expression": "module == content && action == generate",
        "decision": "DENY"
      },
      "overridable": true
    }
  ],
  "totalTimeMs": 23,
  "passed": false
}
```

Publishes `PolicyEvaluated` Kafka event with requestId, decision, and evaluation time.

### 7. List Evaluations

```
GET /api/v1/policies/evaluations
```

Query parameters:
| Name | Type | Required | Description |
|------|------|----------|-------------|
| decision | string | No | Filter by decision (ALLOW, DENY, REVIEW, etc.) |

Response `200 OK`:
```json
{
  "evaluations": [
    {
      "requestId": "uuid",
      "decision": "ALLOW",
      "violations": [],
      "totalTimeMs": 15,
      "passed": true
    }
  ],
  "total": 1
}
```

### 8. List Violations

```
GET /api/v1/policies/violations
```

Response `200 OK`: Returns empty list (stub implementation).

```json
[]
```

### 9. Reload Policies

```
POST /api/v1/policies/reload
```

Response `200 OK`:
```json
{
  "success": true,
  "message": "Policies reloaded",
  "timestamp": 1712345678000
}
```

### 10. Health Check

```
GET /api/v1/policies/health
```

Response `200 OK`:
```json
{
  "status": "UP",
  "service": "policy-engine",
  "timestamp": 1712345678000,
  "details": {
    "metrics": {
      "evaluationCount": 42,
      "violationCount": 7,
      "cacheHits": 15,
      "cacheMisses": 3,
      "activations": 5,
      "deactivations": 2,
      "averageEvaluationTimeMs": 12.5
    }
  }
}
```

## Error Responses (RFC 9457)

```json
{
  "type": "about:blank",
  "title": "Internal Server Error",
  "status": 500,
  "detail": "Error message description",
  "extensions": {}
}
```

Error codes:
| HTTP Status | Code | Description |
|-------------|------|-------------|
| 400 | POL_400 | Bad request — invalid input |
| 404 | POL_404 | Resource not found |
| 500 | POL_500 | Evaluation or processing failure |

## DTOs

| DTO | Fields |
|-----|--------|
| `EvaluationRequestDto` | module, action, payload, context, userId, roles |
| `EvaluationResultDto` | requestId, decision, violations, totalTimeMs, passed |
| `EvaluationListDto` | evaluations, total |
| `PolicyRequestDto` | name, description, type, scope, priority, module, rules, active |
| `PolicyResponseDto` | id, name, description, type, status, scope, priority, module, active, metadata |
| `PolicyListDto` | policies, total |
| `PolicyHealthDto` | status, service, timestamp, details |
| `PolicyReloadDto` | success, message, timestamp |
| `PolicyErrorDto` | type, title, status, detail, extensions |
| `ViolationDto` | ruleName, message, severity, details, overridable |
