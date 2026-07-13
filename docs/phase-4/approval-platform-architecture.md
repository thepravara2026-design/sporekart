# Approval Platform Architecture

## Multi-Layer Architecture

### Interfaces Layer
- **REST API**: 12 endpoints under `/api/v1/approvals` for CRUD and lifecycle operations.
- **WebSocket**: Real-time notifications for approval events (assignment, escalation, decision).

### Application Layer
- **Approval Engine**: Orchestrates the full lifecycle pipeline (submit, assign, review, decide, complete).
- **Approval Workflow Service**: Manages workflow definitions, status transitions, and validation.
- **Approval Assignment Service**: Handles reviewer resolution and assignment lifecycle.
- **Approval Decision Service**: Records and processes approve/reject decisions.
- **Approval Escalation Service**: Manages timed (SLA) and manual escalation chains with configurable levels.
- **Approval Delegation Service**: Handles temporary transfer of review authority between reviewers.
- **Approval Notification Service**: Publishes domain events and sends notifications.
- **Approval Metrics Service**: Tracks throughput, cycle time, bottleneck, and SLA compliance metrics.
- **Approval Audit Service**: Records immutable audit entries for every action.
- **Approval Monitoring Service**: Health checks and operational status reporting.

### Domain Layer
- **ApprovalRequest**, **ApprovalAssignment**, **ApprovalReviewer**, **ApprovalGroup**, **ApprovalWorkflow**, **ApprovalEscalation**, **ApprovalDelegation**, **ApprovalAudit**, **ApprovalComment**, **ApprovalHistoryEntry**, **ReviewerAvailability**, **ApprovalConfig**, **EscalationConfig**, **DelegationConfig**, **ApprovalMetrics**
- Enums: **ApprovalStatus** (PENDING, APPROVED, REJECTED, ESCALATED, CANCELLED, DELEGATED, EXPIRED), **ApprovalType**, **PriorityLevel** (LOW, MEDIUM, HIGH, CRITICAL), **ReviewerStatus**, **EscalationLevel**, **DelegationStatus**

### Infrastructure Layer
- **Persistence**: PostgreSQL via JPA/Hibernate with Flyway V23 migration (9 tables).
- **Redis**: 5 cache namespaces (workflows, reviewers, assignments, active-requests, delegation-mappings).
- **Kafka**: 10 event types published to the `approval-events` topic (REQUEST_CREATED, REQUEST_UPDATED, ASSIGNED, APPROVED, REJECTED, ESCALATED, DELEGATED, CANCELLED, EXPIRED, COMMENT_ADDED).
- **Monitoring**: Micrometer counters (requests created, approved, rejected, escalated, delegated, cancelled, expired), timer (cycle-time, review-time), gauge (pending-count).

## Core Flow

```
Submit -> Assign Reviewer -> Review -> Approve/Reject/Escalate
```

1. **Submit**: An approval request is created with payload, module, action, urgency, and SLA deadline.
2. **Assign Reviewer**: The assignment service resolves the appropriate reviewer using the configured strategy. If no reviewer is available, the request is escalated automatically.
3. **Review**: The assigned reviewer examines the request payload and supporting context.
4. **Decide**: The reviewer either approves (request proceeds), rejects (request denied with reason), or escalates (moved to higher authority).

## Reviewer Resolution Strategies

| Strategy | Description |
|----------|-------------|
| Role-based | Resolves reviewers by matching role names defined in the workflow |
| Department-based | Resolves reviewers belonging to the same department as the request creator |
| Group-based | Resolves reviewers from a named approval group |
| Round-robin | Distributes requests evenly across eligible reviewers in a pool |
| Priority | Routes high/critical urgency requests to higher-priority reviewers first |

## Escalation

- **Timed Escalation (SLA)**: If a request is not reviewed before its SLA deadline, the system automatically escalates to the next level.
- **Manual Escalation**: A reviewer may escalate a request to a higher authority with a reason.
- **Escalation Levels**: Configurable multi-level chains (e.g., reviewer -> manager -> director -> VP).
- Each level defines a new reviewer resolution strategy and SLA deadline.

## Delegation

- Reviewers may temporarily delegate their review authority to another eligible reviewer.
- Delegations have a start time, end time, and optional scope (specific requests or all).
- When a delegated reviewer acts, the audit records both the delegate and the original reviewer.

## Event-Driven Architecture

The `approval-events` Kafka topic carries 10 event types:

| Event | Trigger |
|-------|---------|
| REQUEST_CREATED | New approval request submitted |
| REQUEST_UPDATED | Request payload or metadata modified |
| ASSIGNED | Reviewer assigned to a request |
| APPROVED | Request approved |
| REJECTED | Request rejected |
| ESCALATED | Request escalated (timed or manual) |
| DELEGATED | Review authority delegated |
| CANCELLED | Request cancelled by submitter |
| EXPIRED | Request expired without decision |
| COMMENT_ADDED | Comment added to a request |
