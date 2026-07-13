# Approval Platform API

Base URL: `/api/v1/approvals`

## POST /api/v1/approvals

Submit a new approval request.

**Request Body:**
```json
{
  "workflowId": "wf-123",
  "module": "order",
  "action": "REFUND",
  "payload": {
    "orderId": "ORD-456",
    "amount": 250.00,
    "reason": "Customer requested refund due to damaged goods"
  },
  "urgency": "HIGH",
  "slaDeadline": "2026-07-13T18:00:00Z"
}
```

**Response (201 Created):**
```json
{
  "id": "apr-789",
  "workflowId": "wf-123",
  "module": "order",
  "action": "REFUND",
  "status": "PENDING",
  "urgency": "HIGH",
  "slaDeadline": "2026-07-13T18:00:00Z",
  "createdAt": "2026-07-12T10:30:00Z",
  "updatedAt": "2026-07-12T10:30:00Z"
}
```

## GET /api/v1/approvals

List approvals with optional filters.

**Query Parameters:**
- `status` — Filter by status (PENDING, APPROVED, REJECTED, etc.)
- `module` — Filter by module name
- `action` — Filter by action name
- `urgency` — Filter by priority level
- `reviewerId` — Filter by assigned reviewer
- `createdAfter` — Filter by creation date (ISO 8601)
- `createdBefore` — Filter by creation date (ISO 8601)
- `page` — Page number (default 0)
- `size` — Page size (default 20)

**Response (200 OK):**
```json
{
  "content": [
    {
      "id": "apr-789",
      "workflowId": "wf-123",
      "module": "order",
      "action": "REFUND",
      "status": "PENDING",
      "urgency": "HIGH",
      "slaDeadline": "2026-07-13T18:00:00Z",
      "reviewerId": "usr-456",
      "createdAt": "2026-07-12T10:30:00Z"
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 1,
  "totalPages": 1
}
```

## GET /api/v1/approvals/{id}

Get approval request details.

**Response (200 OK):**
```json
{
  "id": "apr-789",
  "workflowId": "wf-123",
  "module": "order",
  "action": "REFUND",
  "status": "PENDING",
  "payload": {
    "orderId": "ORD-456",
    "amount": 250.00,
    "reason": "Customer requested refund due to damaged goods"
  },
  "urgency": "HIGH",
  "slaDeadline": "2026-07-13T18:00:00Z",
  "reviewerId": "usr-456",
  "assignments": [
    {
      "id": "asn-111",
      "reviewerId": "usr-456",
      "status": "ACTIVE",
      "assignedAt": "2026-07-12T10:31:00Z"
    }
  ],
  "createdAt": "2026-07-12T10:30:00Z",
  "updatedAt": "2026-07-12T10:31:00Z"
}
```

**Response (404 Not Found):**
```json
{
  "error": "APPROVAL_NOT_FOUND",
  "message": "Approval request apr-999 not found"
}
```

## POST /api/v1/approvals/{id}/approve

Approve a pending approval request.

**Request Body (optional):**
```json
{
  "comment": "Refund approved. Amount within policy limits."
}
```

**Response (200 OK):**
```json
{
  "id": "apr-789",
  "status": "APPROVED",
  "decision": {
    "reviewerId": "usr-456",
    "decision": "APPROVED",
    "comment": "Refund approved. Amount within policy limits.",
    "timestamp": "2026-07-12T11:00:00Z"
  }
}
```

**Response (409 Conflict):**
```json
{
  "error": "INVALID_STATE",
  "message": "Cannot approve a request in CANCELLED state"
}
```

## POST /api/v1/approvals/{id}/reject

Reject a pending approval request.

**Request Body:**
```json
{
  "comment": "Refund amount exceeds approval threshold for this level."
}
```

**Response (200 OK):**
```json
{
  "id": "apr-789",
  "status": "REJECTED",
  "decision": {
    "reviewerId": "usr-456",
    "decision": "REJECTED",
    "comment": "Refund amount exceeds approval threshold for this level.",
    "timestamp": "2026-07-12T11:05:00Z"
  }
}
```

## POST /api/v1/approvals/{id}/delegate

Delegate review authority to another reviewer.

**Request Body:**
```json
{
  "delegateTo": "usr-789",
  "reason": "Out of office until July 15",
  "expiresAt": "2026-07-15T23:59:59Z"
}
```

**Response (200 OK):**
```json
{
  "id": "apr-789",
  "status": "DELEGATED",
  "delegation": {
    "fromReviewer": "usr-456",
    "toReviewer": "usr-789",
    "reason": "Out of office until July 15",
    "expiresAt": "2026-07-15T23:59:59Z",
    "timestamp": "2026-07-12T12:00:00Z"
  }
}
```

## POST /api/v1/approvals/{id}/escalate

Escalate a request to a higher authority.

**Request Body:**
```json
{
  "reason": "Request requires director-level approval",
  "level": 2
}
```

**Response (200 OK):**
```json
{
  "id": "apr-789",
  "status": "ESCALATED",
  "escalation": {
    "escalatedBy": "usr-456",
    "level": 2,
    "reason": "Request requires director-level approval",
    "status": "PENDING",
    "timestamp": "2026-07-12T12:30:00Z"
  }
}
```

## POST /api/v1/approvals/{id}/cancel

Cancel a pending approval request (submitter only).

**Request Body (optional):**
```json
{
  "reason": "Refund processed via alternative channel"
}
```

**Response (200 OK):**
```json
{
  "id": "apr-789",
  "status": "CANCELLED",
  "cancelledAt": "2026-07-12T13:00:00Z"
}
```

## GET /api/v1/approvals/pending

Get pending approvals for the current authenticated user.

**Query Parameters:**
- `page` — Page number (default 0)
- `size` — Page size (default 20)

**Response (200 OK):**
```json
{
  "content": [
    {
      "id": "apr-790",
      "module": "order",
      "action": "REFUND",
      "urgency": "MEDIUM",
      "slaDeadline": "2026-07-14T18:00:00Z",
      "submittedBy": "usr-123",
      "createdAt": "2026-07-12T14:00:00Z"
    }
  ],
  "totalElements": 1,
  "page": 0,
  "size": 20
}
```

## GET /api/v1/approvals/history

Get approval history for a specific request.

**Query Parameters:**
- `requestId` — Approval request ID (required)
- `page` — Page number (default 0)
- `size` — Page size (default 50)

**Response (200 OK):**
```json
{
  "requestId": "apr-789",
  "history": [
    {
      "action": "CREATED",
      "reviewerId": null,
      "timestamp": "2026-07-12T10:30:00Z"
    },
    {
      "action": "ASSIGNED",
      "reviewerId": "usr-456",
      "timestamp": "2026-07-12T10:31:00Z"
    },
    {
      "action": "APPROVED",
      "reviewerId": "usr-456",
      "comment": "Refund approved.",
      "timestamp": "2026-07-12T11:00:00Z"
    }
  ]
}
```

## GET /api/v1/approvals/statistics

Get approval metrics for dashboard and monitoring.

**Query Parameters:**
- `startDate` — Start of date range (ISO 8601)
- `endDate` — End of date range (ISO 8601)
- `module` — Filter by module (optional)

**Response (200 OK):**
```json
{
  "totalRequests": 150,
  "approved": 120,
  "rejected": 15,
  "escalated": 5,
  "cancelled": 8,
  "expired": 2,
  "pending": 10,
  "averageCycleTimeMs": 3600000,
  "averageReviewTimeMs": 1800000,
  "slaComplianceRate": 0.93,
  "periodStart": "2026-07-01T00:00:00Z",
  "periodEnd": "2026-07-12T23:59:59Z"
}
```

## GET /api/v1/approvals/health

Health check endpoint.

**Response (200 OK):**
```json
{
  "status": "UP",
  "timestamp": "2026-07-12T15:00:00Z",
  "components": {
    "database": "UP",
    "redis": "UP",
    "kafka": "UP"
  }
}
```
