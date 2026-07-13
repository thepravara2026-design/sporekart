# Governance Automation Security

## Overview
The Governance Automation Platform implements a comprehensive security model covering RBAC, workflow authorization, scheduler authorization, and immutable audit trails.

## RBAC Roles
| Role | Permissions |
|------|-------------|
| AI_ADMINISTRATOR | Full access to all automation features |
| AI_AUTOMATION_MANAGER | Create, update, execute automation jobs and workflows |
| AI_AUTOMATION_OPERATOR | Execute jobs, view job status, manage schedules |
| AI_AUDITOR | Read-only access to automation audit trail |
| AI_VIEWER | View automation jobs, schedules, and execution history |

## Lifecycle Authorization
- **Transition Execution** — Requires `lifecycle:transition` permission
- **State View** — Requires `lifecycle:read` permission
- **History View** — Requires `lifecycle:history` permission
- **Force Delete** — Requires `lifecycle:force-delete` permission

## Workflow Authorization
- **Workflow Create** — Requires `workflow:create` permission
- **Workflow Execute** — Requires `workflow:execute` permission
- **Workflow Cancel** — Requires `workflow:cancel` permission
- **Workflow View** — Requires `workflow:read` permission
- **Workflow Override** — Requires `workflow:override` permission (admin only)

## Scheduler Authorization
- **Job Create** — Requires `scheduler:create` permission
- **Job Update** — Requires `scheduler:update` permission
- **Job Delete** — Requires `scheduler:delete` permission
- **Job Execute** — Requires `scheduler:execute` permission
- **Job View** — Requires `scheduler:read` permission

## Retry & Escalation Authorization
- **Retry Policy Manage** — Requires `automation:retry:manage` permission
- **Escalation Policy Manage** — Requires `automation:escalation:manage` permission
- **Expiration Policy Manage** — Requires `automation:expiration:manage` permission

## Immutable Audit Trail
- All automation operations are recorded in the audit trail
- Audit records are append-only — no UPDATE or DELETE allowed
- Enforced at DB level with BEFORE UPDATE/DELETE triggers
- Audit records include: actor, action, resource, timestamp, context, result
- Audit retention configurable via governance policies
- Audit query available for compliance and investigation

## Automation Security Controls
### Input Validation
- Job name validation (length, characters, injection prevention)
- Schedule configuration validation (cron expression parsing, timezone validation)
- Entity type validation against registered types
- Transition validation against state machine rules

### Execution Security
- Distributed lock prevention for duplicate execution
- Execution window enforcement
- Rate limiting for job execution
- Concurrent execution limits per job type
- Resource usage monitoring and limits

### Event Security
- Kafka event filtering and validation
- Event source authentication
- Event payload validation
- Dead-letter routing for invalid events

## Data Protection
- Job configuration stored with field-level security
- Sensitive data exclusion from audit records
- Retention policies for audit data
- Secure encryption for job credentials

## Error Codes (AutomationException)
| Code | HTTP Status | Description |
|------|-------------|-------------|
| AUT_400 | 400 | Invalid request / Validation failed |
| AUT_401 | 401 | Authentication required |
| AUT_403 | 403 | Insufficient permissions |
| AUT_404 | 404 | Resource not found |
| AUT_409 | 409 | Conflict (concurrent execution, invalid transition) |
| AUT_422 | 422 | Unprocessable entity |
| AUT_429 | 429 | Rate limit exceeded |
| AUT_500 | 500 | Internal automation error |
