# Governance Automation API Reference

Base URL: `/api/v1`

## Governance Lifecycle Endpoints

### `POST /api/v1/governance/lifecycle/transition`
Execute a lifecycle state transition.

**Request Body:**
```json
{
  "entityType": "POLICY",
  "entityId": "uuid",
  "targetState": "APPROVED",
  "reason": "Approval granted after review",
  "metadata": {}
}
```

**Response (200):**
```json
{
  "transitionId": "uuid",
  "entityType": "POLICY",
  "entityId": "uuid",
  "fromState": "PENDING_REVIEW",
  "toState": "APPROVED",
  "status": "COMPLETED",
  "timestamp": "2026-07-12T10:00:00Z",
  "actor": "user-uuid"
}
```

**Errors:** `400` Invalid transition, `404` Entity not found, `409` Concurrent transition

### `GET /api/v1/governance/lifecycle/state/{entityType}/{entityId}`
Get current lifecycle state for an entity.

**Response (200):**
```json
{
  "entityType": "POLICY",
  "entityId": "uuid",
  "currentState": "APPROVED",
  "previousState": "PENDING_REVIEW",
  "enteredAt": "2026-07-12T10:00:00Z",
  "updatedAt": "2026-07-12T10:00:00Z"
}
```

### `GET /api/v1/governance/lifecycle/history/{entityType}/{entityId}`
Get lifecycle transition history for an entity.

**Response (200):**
```json
{
  "entityType": "POLICY",
  "entityId": "uuid",
  "transitions": [
    {
      "fromState": "CREATED",
      "toState": "ACTIVE",
      "timestamp": "2026-07-12T08:00:00Z",
      "actor": "system",
      "reason": "Auto-activation"
    },
    {
      "fromState": "ACTIVE",
      "toState": "PENDING_REVIEW",
      "timestamp": "2026-07-12T09:00:00Z",
      "actor": "user-uuid",
      "reason": "Submitted for review"
    }
  ]
}
```

## Automation Endpoints

### `POST /api/v1/automation/jobs`
Create a new automation job.

**Request Body:**
```json
{
  "name": "Weekly Policy Refresh",
  "type": "RECURRING",
  "jobClass": "com.sporekart.governance.automation.job.PolicyRefreshJob",
  "schedule": {
    "frequency": "WEEKLY",
    "interval": 1,
    "dayOfWeek": "MONDAY",
    "time": "02:00:00",
    "timezone": "UTC"
  },
  "config": {},
  "enabled": true,
  "retryPolicyId": "uuid",
  "escalationPolicyId": "uuid"
}
```

**Response (201):**
```json
{
  "jobId": "uuid",
  "name": "Weekly Policy Refresh",
  "type": "RECURRING",
  "status": "ACTIVE",
  "createdAt": "2026-07-12T10:00:00Z"
}
```

### `GET /api/v1/automation/jobs`
List all automation jobs.

**Query Parameters:** `page`, `size`, `type`, `status`, `search`

**Response (200):**
```json
{
  "jobs": [
    {
      "jobId": "uuid",
      "name": "Weekly Policy Refresh",
      "type": "RECURRING",
      "status": "ACTIVE",
      "lastRun": "2026-07-12T02:00:00Z",
      "nextRun": "2026-07-19T02:00:00Z"
    }
  ],
  "page": 0,
  "size": 20,
  "total": 1
}
```

### `GET /api/v1/automation/jobs/{jobId}`
Get automation job details.

**Response (200):**
```json
{
  "jobId": "uuid",
  "name": "Weekly Policy Refresh",
  "type": "RECURRING",
  "status": "ACTIVE",
  "schedule": {
    "frequency": "WEEKLY",
    "dayOfWeek": "MONDAY",
    "time": "02:00:00",
    "timezone": "UTC"
  },
  "lastRun": "2026-07-12T02:00:00Z",
  "lastRunStatus": "COMPLETED",
  "nextRun": "2026-07-19T02:00:00Z",
  "executionCount": 10,
  "failureCount": 1,
  "enabled": true,
  "createdAt": "2026-07-10T10:00:00Z"
}
```

### `PUT /api/v1/automation/jobs/{jobId}`
Update an existing automation job.

**Request Body:**
```json
{
  "name": "Updated Policy Refresh",
  "schedule": {
    "frequency": "DAILY",
    "time": "03:00:00"
  },
  "enabled": true
}
```

**Response (200):**
```json
{
  "jobId": "uuid",
  "name": "Updated Policy Refresh",
  "updatedAt": "2026-07-12T11:00:00Z"
}
```

### `DELETE /api/v1/automation/jobs/{jobId}`
Delete an automation job.

**Response (204):** No content

### `POST /api/v1/automation/jobs/{jobId}/execute`
Execute a job immediately.

**Response (202):**
```json
{
  "executionId": "uuid",
  "jobId": "uuid",
  "status": "RUNNING",
  "startedAt": "2026-07-12T11:00:00Z"
}
```

### `GET /api/v1/automation/scheduled-tasks`
List all scheduled tasks with upcoming execution times.

**Query Parameters:** `page`, `size`, `status`, `jobId`

**Response (200):**
```json
{
  "tasks": [
    {
      "taskId": "uuid",
      "jobId": "uuid",
      "jobName": "Weekly Policy Refresh",
      "scheduledAt": "2026-07-19T02:00:00Z",
      "status": "SCHEDULED",
      "executionWindow": {
        "start": "2026-07-19T02:00:00Z",
        "end": "2026-07-19T03:00:00Z"
      }
    }
  ],
  "page": 0,
  "size": 20,
  "total": 5
}
```

## Error Format (RFC 9457)
```json
{
  "type": "urn:sporekart:automation:error",
  "title": "Job Not Found",
  "status": 404,
  "detail": "Automation job with ID {jobId} not found",
  "instance": "/api/v1/automation/jobs/{jobId}"
}
```

## Error Codes
| Code | HTTP Status | Description |
|------|-------------|-------------|
| AUT_400 | 400 | Invalid request / Invalid transition |
| AUT_404 | 404 | Job / Entity not found |
| AUT_409 | 409 | Concurrent transition / Job already running |
| AUT_422 | 422 | Unprocessable entity / Invalid schedule |
| AUT_500 | 500 | Internal automation error |
