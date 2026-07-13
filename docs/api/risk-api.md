# Risk API Reference

Base path: `/api/v1/risk`

---

## POST /api/v1/risk/assess

Initiate a risk assessment.

### Request Body

```json
{
  "module": "prompt",
  "providerId": "provider-gemini-1",
  "resourceType": "PROMPT_EXECUTION",
  "resourceId": "prompt-abc-123",
  "context": {
    "userId": "user-456",
    "roles": ["content_editor"],
    "environment": "production",
    "region": "EU",
    "workflowId": "wf-789"
  },
  "metadata": {
    "promptLength": 450,
    "knowledgeSourceCount": 3,
    "conversationTurnCount": 5
  }
}
```

### Response (201)

```json
{
  "assessmentId": "rsk-abc-123",
  "status": "PENDING",
  "riskLevel": null,
  "riskScore": null,
  "trustScore": null,
  "confidenceScore": null,
  "recommendation": null,
  "createdAt": "2026-07-12T10:30:00Z"
}
```

---

## GET /api/v1/risk/assessments

List risk assessments.

### Query Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| status | string | No | Filter by status (PENDING, IN_PROGRESS, COMPLETED, FAILED) |
| riskLevel | string | No | Filter by risk level (LOW, MEDIUM, HIGH, CRITICAL) |
| module | string | No | Filter by module |
| from | string (ISO date) | No | Start date |
| to | string (ISO date) | No | End date |
| page | integer | No | Page number (default: 0) |
| size | integer | No | Page size (default: 20) |

### Response (200)

```json
{
  "assessments": [
    {
      "assessmentId": "rsk-abc-123",
      "status": "COMPLETED",
      "riskLevel": "MEDIUM",
      "riskScore": 35,
      "trustScore": 72,
      "confidenceScore": 68,
      "recommendation": "REVIEW",
      "createdAt": "2026-07-12T10:30:00Z",
      "completedAt": "2026-07-12T10:30:02Z"
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 1
}
```

---

## GET /api/v1/risk/assessments/{id}

Get assessment details.

### Response (200)

```json
{
  "assessmentId": "rsk-abc-123",
  "status": "COMPLETED",
  "riskLevel": "MEDIUM",
  "riskScore": 35,
  "riskScoreBreakdown": {
    "PROVIDER": 20,
    "PROMPT": 10,
    "KNOWLEDGE": 5,
    "COMPLIANCE": 0,
    "PERFORMANCE": 0,
    "SECURITY": 0,
    "OPERATIONAL": 0
  },
  "trustScore": 72,
  "trustScoreBreakdown": {
    "PROVIDER_RELIABILITY": 85,
    "KNOWLEDGE_QUALITY": 70,
    "SEMANTIC_CONFIDENCE": 75,
    "PROMPT_VALIDATION": 80,
    "HISTORICAL_ACCURACY": 65,
    "POLICY_COMPLIANCE": 90,
    "WORKFLOW_SUCCESS": 60,
    "CONTEXT_COMPLETENESS": 70,
    "OUTPUT_VALIDATION": 55
  },
  "confidenceScore": 68,
  "confidenceScoreBreakdown": {
    "KNOWLEDGE_MATCH": 72,
    "SEMANTIC_SIMILARITY": 65,
    "PROMPT_QUALITY": 80,
    "CONVERSATION_CONTEXT": 55,
    "WORKFLOW_SUCCESS": 60,
    "PROVIDER_METADATA": 75
  },
  "recommendation": {
    "type": "REVIEW",
    "reason": "Medium risk with moderate trust and confidence",
    "actions": ["MONITOR", "REVIEW_OUTPUT"],
    "fallbackSuggestion": null
  },
  "factors": [
    {
      "category": "PROVIDER",
      "name": "provider-health",
      "score": 20,
      "weight": 0.20,
      "evidence": "Provider uptime 99.5%, error rate 0.5%"
    }
  ],
  "createdAt": "2026-07-12T10:30:00Z",
  "completedAt": "2026-07-12T10:30:02Z"
}
```

### Response (404)

```json
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "Assessment rsk-abc-123 not found",
  "instance": "/api/v1/risk/assessments/rsk-abc-123",
  "errorCode": "RSK_404"
}
```

---

## POST /api/v1/risk/assessments/{id}/factors

Add risk factors to an assessment.

### Request Body

```json
{
  "factors": [
    {
      "category": "PROVIDER",
      "name": "provider-health",
      "score": 20,
      "weight": 0.20,
      "evidence": "Provider uptime 99.5%, error rate 0.5%"
    },
    {
      "category": "PROMPT",
      "name": "prompt-validation",
      "score": 10,
      "weight": 0.15,
      "evidence": "All variables resolved, no injection patterns"
    }
  ]
}
```

### Response (200)

```json
{
  "assessmentId": "rsk-abc-123",
  "factorCount": 2,
  "status": "IN_PROGRESS"
}
```

---

## GET /api/v1/risk/assessments/{id}/factors

List factors for an assessment.

### Response (200)

```json
{
  "factors": [
    {
      "id": "fct-111",
      "category": "PROVIDER",
      "name": "provider-health",
      "score": 20,
      "weight": 0.20,
      "evidence": "Provider uptime 99.5%, error rate 0.5%",
      "createdAt": "2026-07-12T10:30:00Z"
    }
  ],
  "total": 1
}
```

---

## POST /api/v1/risk/assess/{id}/trust

Evaluate trust for an assessment.

### Request Body

```json
{
  "factors": ["PROVIDER_RELIABILITY", "KNOWLEDGE_QUALITY", "SEMANTIC_CONFIDENCE"],
  "context": {
    "providerUptime": 99.95,
    "providerErrorRate": 0.02,
    "citationCount": 3,
    "verifiedCitations": 3
  }
}
```

### Response (200)

```json
{
  "assessmentId": "rsk-abc-123",
  "trustScore": 72,
  "factors": [
    {
      "factor": "PROVIDER_RELIABILITY",
      "score": 85,
      "weight": 0.15,
      "reason": "Provider uptime 99.95% (24h), error rate 0.02%"
    },
    {
      "factor": "KNOWLEDGE_QUALITY",
      "score": 70,
      "weight": 0.15,
      "reason": "3 of 3 citations verified, source authority: HIGH"
    },
    {
      "factor": "SEMANTIC_CONFIDENCE",
      "score": 75,
      "weight": 0.12,
      "reason": "Cosine similarity 0.89 with query centroid"
    }
  ],
  "evaluatedAt": "2026-07-12T10:30:00Z"
}
```

---

## GET /api/v1/risk/assess/{id}/trust

Get trust evaluation.

### Response (200)

```json
{
  "assessmentId": "rsk-abc-123",
  "trustScore": 72,
  "trustLevel": "HIGH",
  "factors": [
    {
      "factor": "PROVIDER_RELIABILITY",
      "score": 85,
      "weight": 0.15,
      "reason": "Provider uptime 99.95% (24h), error rate 0.02%"
    },
    {
      "factor": "KNOWLEDGE_QUALITY",
      "score": 70,
      "weight": 0.15,
      "reason": "3 of 3 citations verified, source authority: HIGH"
    },
    {
      "factor": "SEMANTIC_CONFIDENCE",
      "score": 75,
      "weight": 0.12,
      "reason": "Cosine similarity 0.89 with query centroid"
    },
    {
      "factor": "PROMPT_VALIDATION",
      "score": 80,
      "weight": 0.12,
      "reason": "No injection patterns detected, all variables resolved"
    },
    {
      "factor": "HISTORICAL_ACCURACY",
      "score": 65,
      "weight": 0.12,
      "reason": "87% accuracy on 150 similar queries"
    },
    {
      "factor": "POLICY_COMPLIANCE",
      "score": 90,
      "weight": 0.10,
      "reason": "All 5 active policies satisfied"
    },
    {
      "factor": "WORKFLOW_SUCCESS",
      "score": 60,
      "weight": 0.08,
      "reason": "94% completion rate across 50 prior workflows"
    },
    {
      "factor": "CONTEXT_COMPLETENESS",
      "score": 70,
      "weight": 0.08,
      "reason": "All 7 required context fields populated"
    },
    {
      "factor": "OUTPUT_VALIDATION",
      "score": 55,
      "weight": 0.08,
      "reason": "Response format validated, length within limits"
    }
  ],
  "evaluatedAt": "2026-07-12T10:30:00Z"
}
```

---

## POST /api/v1/risk/assess/{id}/confidence

Calculate confidence for an assessment.

### Request Body

```json
{
  "factors": ["KNOWLEDGE_MATCH", "SEMANTIC_SIMILARITY"],
  "context": {
    "knowledgeRelevanceScore": 0.85,
    "embeddingSimilarity": 0.78,
    "promptCompletionRate": 1.0
  }
}
```

### Response (200)

```json
{
  "assessmentId": "rsk-abc-123",
  "confidenceScore": 68,
  "factors": [
    {
      "factor": "KNOWLEDGE_MATCH",
      "score": 72,
      "weight": 0.25,
      "explanation": "Retrieved 3 relevant documents with avg relevance 0.85"
    },
    {
      "factor": "SEMANTIC_SIMILARITY",
      "score": 65,
      "weight": 0.20,
      "explanation": "Embedding cosine similarity 0.78 with query centroid"
    }
  ],
  "evaluatedAt": "2026-07-12T10:30:00Z"
}
```

---

## GET /api/v1/risk/assess/{id}/confidence

Get confidence score.

### Response (200)

```json
{
  "assessmentId": "rsk-abc-123",
  "confidenceScore": 68,
  "confidenceLevel": "MEDIUM",
  "factors": [
    {
      "factor": "KNOWLEDGE_MATCH",
      "score": 72,
      "weight": 0.25,
      "explanation": "Retrieved 3 relevant documents with avg relevance 0.85"
    },
    {
      "factor": "SEMANTIC_SIMILARITY",
      "score": 65,
      "weight": 0.20,
      "explanation": "Embedding cosine similarity 0.78 with query centroid"
    },
    {
      "factor": "PROMPT_QUALITY",
      "score": 80,
      "weight": 0.18,
      "explanation": "Prompt template fully matched, all variables resolved"
    },
    {
      "factor": "CONVERSATION_CONTEXT",
      "score": 55,
      "weight": 0.15,
      "explanation": "5 prior turns with 60% topic alignment"
    },
    {
      "factor": "WORKFLOW_SUCCESS",
      "score": 60,
      "weight": 0.12,
      "explanation": "Similar workflows completed with 88% success rate"
    },
    {
      "factor": "PROVIDER_METADATA",
      "score": 75,
      "weight": 0.10,
      "explanation": "Provider capabilities match request requirements"
    }
  ],
  "evaluatedAt": "2026-07-12T10:30:00Z"
}
```

---

## GET /api/v1/risk/health

Health check endpoint.

### Response (200)

```json
{
  "status": "UP",
  "activeAssessments": 3,
  "pendingAssessments": 1,
  "averageRiskScore": 35,
  "averageTrustScore": 72,
  "averageConfidenceScore": 68,
  "cacheHitRate": 0.85,
  "lastEvaluationTimeMs": 180,
  "version": "1.0.0"
}
```

### Response (503)

```json
{
  "status": "DOWN",
  "activeAssessments": 0,
  "errors": ["Risk engine not initialized"],
  "version": "1.0.0"
}
```

## Error Response Format (RFC 9457)

All risk API errors use the RFC 9457 Problem Details format:

```json
{
  "type": "about:blank",
  "title": "string",
  "status": 400,
  "detail": "string",
  "instance": "/api/v1/risk/assess",
  "errorCode": "RSK_4xx"
}
```

| Error Code | HTTP Status | Description |
|------------|-------------|-------------|
| RSK_400 | 400 | Invalid request parameters |
| RSK_401 | 401 | Missing or invalid authentication |
| RSK_403 | 403 | Insufficient permissions |
| RSK_404 | 404 | Resource not found |
| RSK_409 | 409 | Conflict (invalid state transition) |
| RSK_422 | 422 | Risk evaluation failure |
| RSK_429 | 429 | Rate limit exceeded |
| RSK_500 | 500 | Internal risk engine error |
