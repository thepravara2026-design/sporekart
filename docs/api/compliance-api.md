# Compliance API Reference

Base path: `/api/v1/compliance`

---

## POST /api/v1/compliance/validate

Validate a request against compliance rules.

### Request Body

```json
{
  "frameworkType": "INTERNAL_AI_GOVERNANCE",
  "scope": "MODULE",
  "module": "prompt",
  "resourceId": "prompt-123",
  "context": {
    "provider": "gemini",
    "model": "gemini-pro",
    "userId": "user-456",
    "roles": ["content_editor"],
    "environment": "production",
    "region": "EU"
  }
}
```

### Response (200 — Compliant)

```json
{
  "assessmentId": "asmt-abc-123",
  "status": "COMPLIANT",
  "framework": "INTERNAL_AI_GOVERNANCE",
  "riskLevel": "LOW",
  "rulesEvaluated": 5,
  "violations": [],
  "exceptions": [],
  "reportId": "rpt-def-456",
  "evaluatedAt": "2026-07-12T10:30:00Z"
}
```

### Response (200 — Non-compliant)

```json
{
  "assessmentId": "asmt-abc-123",
  "status": "NON_COMPLIANT",
  "framework": "INTERNAL_AI_GOVERNANCE",
  "riskLevel": "HIGH",
  "rulesEvaluated": 5,
  "violations": [
    {
      "ruleId": "rule-789",
      "ruleName": "provider-region-restriction",
      "severity": "ERROR",
      "message": "Provider region not in allowed regions for this module",
      "evidenceId": "evd-111"
    }
  ],
  "exceptions": [],
  "reportId": "rpt-def-456",
  "evaluatedAt": "2026-07-12T10:30:00Z"
}
```

### Response (422 — Validation Error)

```json
{
  "type": "about:blank",
  "title": "Unprocessable Entity",
  "status": 422,
  "detail": "Invalid framework type: UNKNOWN_FRAMEWORK",
  "instance": "/api/v1/compliance/validate",
  "errorCode": "CMP_422"
}
```

---

## GET /api/v1/compliance/frameworks

List all registered compliance frameworks.

### Query Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| includeInactive | boolean | No | Include inactive frameworks (default: false) |

### Response (200)

```json
{
  "frameworks": [
    {
      "id": "fw-gov-001",
      "type": "INTERNAL_AI_GOVERNANCE",
      "name": "Internal AI Governance",
      "version": "1.0",
      "status": "ACTIVE",
      "description": "Internal AI usage and governance policies"
    },
    {
      "id": "fw-rai-001",
      "type": "RESPONSIBLE_AI",
      "name": "Responsible AI Principles",
      "version": "1.0",
      "status": "ACTIVE",
      "description": "Fairness, transparency, accountability"
    }
  ],
  "total": 6
}
```

---

## GET /api/v1/compliance/rules

List compliance rules. Optionally filtered by framework.

### Query Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| frameworkId | string | No | Filter by framework ID |
| scope | string | No | Filter by compliance scope |
| status | string | No | Filter by rule status |

### Response (200)

```json
{
  "rules": [
    {
      "id": "rule-789",
      "frameworkId": "fw-gov-001",
      "name": "provider-region-restriction",
      "description": "Provider must be in allowed regions",
      "scope": "MODULE",
      "severity": "ERROR",
      "expression": "provider.region in allowedRegions",
      "controlType": "PREVENTIVE",
      "status": "ACTIVE",
      "priority": 10
    }
  ],
  "total": 12
}
```

---

## GET /api/v1/compliance/reports

List compliance reports.

### Query Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| assessmentId | string | No | Filter by assessment |
| frameworkId | string | No | Filter by framework |
| status | string | No | Filter by report status |
| from | string (ISO date) | No | Start date |
| to | string (ISO date) | No | End date |
| page | integer | No | Page number (default: 0) |
| size | integer | No | Page size (default: 20) |

### Response (200)

```json
{
  "reports": [
    {
      "id": "rpt-def-456",
      "assessmentId": "asmt-abc-123",
      "frameworkType": "INTERNAL_AI_GOVERNANCE",
      "status": "COMPLETED",
      "overallStatus": "COMPLIANT",
      "totalRules": 5,
      "passed": 5,
      "violations": 0,
      "generatedAt": "2026-07-12T10:30:00Z"
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 1
}
```

---

## GET /api/v1/compliance/violations

List compliance violations.

### Query Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| assessmentId | string | No | Filter by assessment |
| severity | string | No | Filter by severity (INFO, WARNING, ERROR, CRITICAL) |
| resolved | boolean | No | Filter by resolution status |

### Response (200)

```json
{
  "violations": [
    {
      "id": "vio-111",
      "assessmentId": "asmt-abc-123",
      "ruleId": "rule-789",
      "ruleName": "provider-region-restriction",
      "severity": "ERROR",
      "message": "Provider region not in allowed regions",
      "evidenceId": "evd-111",
      "exceptionId": null,
      "resolved": false,
      "detectedAt": "2026-07-12T10:30:00Z"
    }
  ],
  "total": 1
}
```

---

## GET /api/v1/compliance/exceptions

List compliance exceptions.

### Query Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| status | string | No | Filter by status (REQUESTED, APPROVED, REJECTED, EXPIRED, REVOKED) |
| ruleId | string | No | Filter by rule |

### Response (200)

```json
{
  "exceptions": [
    {
      "id": "exc-111",
      "ruleId": "rule-789",
      "reason": "Legacy integration requires temporary exception",
      "status": "REQUESTED",
      "requestedBy": "user-456",
      "approvedBy": null,
      "expiresAt": "2026-07-19T10:30:00Z",
      "requestedAt": "2026-07-12T10:30:00Z"
    }
  ],
  "total": 1
}
```

---

## POST /api/v1/compliance/exceptions

Create a new exception request.

### Request Body

```json
{
  "ruleId": "rule-789",
  "reason": "Legacy integration requires temporary exception",
  "scope": "MODULE",
  "moduleId": "prompt",
  "expiresAt": "2026-07-19T10:30:00Z",
  "metadata": {
    "ticketRef": "INC-12345",
    "approvedBy": "compliance-officer@example.com"
  }
}
```

### Response (201)

```json
{
  "id": "exc-111",
  "ruleId": "rule-789",
  "status": "REQUESTED",
  "requestedBy": "user-456",
  "requestedAt": "2026-07-12T10:30:00Z",
  "expiresAt": "2026-07-19T10:30:00Z"
}
```

### Response (400)

```json
{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "Rule rule-789 does not exist",
  "errorCode": "CMP_400"
}
```

---

## GET /api/v1/compliance/statistics

Get compliance metrics and statistics.

### Response (200)

```json
{
  "totalValidations": 1250,
  "compliantCount": 1120,
  "nonCompliantCount": 130,
  "complianceRate": 0.896,
  "violationsBySeverity": {
    "INFO": 45,
    "WARNING": 60,
    "ERROR": 20,
    "CRITICAL": 5
  },
  "averageEvaluationTimeMs": 245,
  "topViolatedRules": [
    {"ruleId": "rule-789", "count": 15},
    {"ruleId": "rule-555", "count": 10}
  ],
  "periodStart": "2026-07-12T00:00:00Z",
  "periodEnd": "2026-07-12T23:59:59Z"
}
```

---

## GET /api/v1/compliance/health

Health check endpoint.

### Response (200)

```json
{
  "status": "UP",
  "frameworkCount": 6,
  "activeRuleCount": 12,
  "pendingAssessments": 3,
  "cacheHitRate": 0.85,
  "lastEvaluationTimeMs": 180,
  "version": "1.0.0"
}
```

### Response (503 — Unhealthy)

```json
{
  "status": "DOWN",
  "frameworkCount": 6,
  "activeRuleCount": 0,
  "errors": ["No active compliance rules loaded"],
  "version": "1.0.0"
}
```

## Error Response Format (RFC 9457)

All compliance API errors use the RFC 9457 Problem Details format:

```json
{
  "type": "about:blank",
  "title": "string",
  "status": 400,
  "detail": "string",
  "instance": "/api/v1/compliance/validate",
  "errorCode": "CMP_4xx"
}
```

| Error Code | HTTP Status | Description |
|------------|-------------|-------------|
| CMP_400 | 400 | Invalid request parameters |
| CMP_401 | 401 | Missing or invalid authentication |
| CMP_403 | 403 | Insufficient permissions |
| CMP_404 | 404 | Resource not found |
| CMP_409 | 409 | Conflict (invalid state transition) |
| CMP_422 | 422 | Validation rule failure |
| CMP_429 | 429 | Rate limit exceeded |
| CMP_500 | 500 | Internal compliance engine error |
