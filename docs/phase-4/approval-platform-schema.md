# Approval Platform Database Schema

Migration: `V23__sprint18_approval_platform.sql`

## Table: approval_workflows

| Column | Type | Description |
|--------|------|-------------|
| id | VARCHAR(36) | Primary key (UUID) |
| module | VARCHAR(100) | Module name (e.g., order, content, policy) |
| name | VARCHAR(255) | Human-readable workflow name |
| allowed_transitions | TEXT | JSON array of valid status transitions |
| created_at | TIMESTAMP | Creation timestamp |
| updated_at | TIMESTAMP | Last update timestamp |

## Table: approval_requests

| Column | Type | Description |
|--------|------|-------------|
| id | VARCHAR(36) | Primary key (UUID) |
| workflow_id | VARCHAR(36) | FK to approval_workflows |
| module | VARCHAR(100) | Module originating the request |
| action | VARCHAR(100) | Action requiring approval (e.g., REFUND, PUBLISH) |
| status | VARCHAR(20) | PENDING, APPROVED, REJECTED, ESCALATED, CANCELLED, DELEGATED, EXPIRED |
| payload | TEXT | JSON request payload |
| reviewer_id | VARCHAR(36) | FK to approval_reviewers (current assignee) |
| group_id | VARCHAR(36) | FK to approval_groups |
| urgency | VARCHAR(20) | LOW, MEDIUM, HIGH, CRITICAL |
| sla_deadline | TIMESTAMP | SLA deadline for review completion |
| created_at | TIMESTAMP | Creation timestamp |
| updated_at | TIMESTAMP | Last update timestamp |

## Table: approval_assignments

| Column | Type | Description |
|--------|------|-------------|
| id | VARCHAR(36) | Primary key (UUID) |
| request_id | VARCHAR(36) | FK to approval_requests |
| reviewer_id | VARCHAR(36) | FK to approval_reviewers |
| status | VARCHAR(20) | ACTIVE, COMPLETED, DELEGATED, EXPIRED |
| assigned_at | TIMESTAMP | Assignment timestamp |
| completed_at | TIMESTAMP | Completion timestamp (nullable) |

## Table: approval_reviewers

| Column | Type | Description |
|--------|------|-------------|
| id | VARCHAR(36) | Primary key (UUID) |
| user_id | VARCHAR(36) | System user ID |
| name | VARCHAR(255) | Reviewer display name |
| email | VARCHAR(255) | Reviewer email address |
| roles | TEXT | JSON array of role names |
| department | VARCHAR(100) | Department name |
| groups | TEXT | JSON array of group IDs |
| priority | INTEGER | Priority ranking (lower = higher priority) |
| status | VARCHAR(20) | AVAILABLE, BUSY, AWAY, INACTIVE |
| max_pending | INTEGER | Maximum concurrent pending assignments |
| created_at | TIMESTAMP | Creation timestamp |

## Table: approval_groups

| Column | Type | Description |
|--------|------|-------------|
| id | VARCHAR(36) | Primary key (UUID) |
| name | VARCHAR(255) | Group display name |
| members | TEXT | JSON array of reviewer IDs |
| created_at | TIMESTAMP | Creation timestamp |

## Table: approval_escalations

| Column | Type | Description |
|--------|------|-------------|
| id | VARCHAR(36) | Primary key (UUID) |
| request_id | VARCHAR(36) | FK to approval_requests |
| escalated_by | VARCHAR(36) | FK to approval_reviewers (who triggered escalation) |
| escalated_to | VARCHAR(36) | FK to approval_reviewers (target reviewer) |
| level | INTEGER | Escalation level (1-based) |
| reason | TEXT | Escalation reason |
| status | VARCHAR(20) | PENDING, RESOLVED, EXPIRED |
| resolved_at | TIMESTAMP | Resolution timestamp (nullable) |
| created_at | TIMESTAMP | Creation timestamp |

## Table: approval_history

| Column | Type | Description |
|--------|------|-------------|
| id | VARCHAR(36) | Primary key (UUID) |
| request_id | VARCHAR(36) | FK to approval_requests |
| decision | VARCHAR(20) | APPROVED, REJECTED, ESCALATED, DELEGATED, CANCELLED |
| comment | TEXT | Decision comment (nullable) |
| reviewer_id | VARCHAR(36) | FK to approval_reviewers |
| timestamp | TIMESTAMP | Event timestamp |

## Table: approval_audit

| Column | Type | Description |
|--------|------|-------------|
| id | VARCHAR(36) | Primary key (UUID) |
| request_id | VARCHAR(36) | FK to approval_requests |
| action | VARCHAR(50) | Audit action (CREATED, ASSIGNED, APPROVED, etc.) |
| reviewer_id | VARCHAR(36) | FK to approval_reviewers (nullable) |
| details | TEXT | JSON audit details |
| timestamp | TIMESTAMP | Audit event timestamp |

## Table: approval_comments

| Column | Type | Description |
|--------|------|-------------|
| id | VARCHAR(36) | Primary key (UUID) |
| history_id | VARCHAR(36) | FK to approval_history |
| author_id | VARCHAR(36) | FK to approval_reviewers |
| content | TEXT | Comment body |
| timestamp | TIMESTAMP | Comment timestamp |

## Indexes

- `idx_approval_requests_status` on approval_requests(status)
- `idx_approval_requests_module` on approval_requests(module)
- `idx_approval_requests_reviewer` on approval_requests(reviewer_id)
- `idx_approval_requests_sla` on approval_requests(sla_deadline)
- `idx_approval_assignments_reviewer` on approval_assignments(reviewer_id)
- `idx_approval_assignments_request` on approval_assignments(request_id)
- `idx_approval_escalations_request` on approval_escalations(request_id)
- `idx_approval_history_request` on approval_history(request_id)
- `idx_approval_audit_request` on approval_audit(request_id)
- `idx_approval_audit_timestamp` on approval_audit(timestamp)
- `idx_approval_comments_history` on approval_comments(history_id)

## Relationships

```
approval_workflows 1---* approval_requests
approval_requests 1---* approval_assignments
approval_requests 1---* approval_escalations
approval_requests 1---* approval_history
approval_requests 1---* approval_audit
approval_reviewers 1---* approval_assignments
approval_reviewers 1---* approval_escalations (escalated_by, escalated_to)
approval_reviewers 1---* approval_history
approval_groups 1---* approval_requests
approval_history 1---* approval_comments
```
