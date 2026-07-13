# Lifecycle Orchestration Architecture

## Overview
The Lifecycle Orchestration system manages the complete lifecycle of governance entities through a configurable state machine. It provides state transitions, transition validation, history tracking, and automation rules for state changes.

## State Machine

### States (10)
```
CREATED → ACTIVE → PENDING_REVIEW → APPROVED → DEPLOYED → SUSPENDED
                                                        → EXPIRED
                                                        → ARCHIVED
                                                        → ROLLED_BACK
                                                        → DELETED
```

### Allowed Transitions
| From | To | Condition |
|------|-----|-----------|
| CREATED | ACTIVE | Auto-transition on creation |
| ACTIVE | PENDING_REVIEW | Manual or rule-based trigger |
| PENDING_REVIEW | APPROVED | Approval granted |
| PENDING_REVIEW | ACTIVE | Approval rejected (revert) |
| APPROVED | DEPLOYED | Auto or manual deploy |
| DEPLOYED | ACTIVE | Rollback to active |
| DEPLOYED | SUSPENDED | Suspension trigger |
| DEPLOYED | EXPIRED | Time-based expiration |
| DEPLOYED | ARCHIVED | Manual or retention policy |
| DEPLOYED | ROLLED_BACK | Rollback execution |
| DEPLOYED | DELETED | Manual deletion with force |
| SUSPENDED | ACTIVE | Unsuspension trigger |
| SUSPENDED | DELETED | Manual deletion |
| EXPIRED | ARCHIVED | Auto-archive on expiration |
| ARCHIVED | DELETED | Permanent deletion |
| ANY | DELETED | Force delete (requires authorization) |

## Managed Entity Types (8)
1. **Policies** — Governance policy lifecycle from creation to archival
2. **Decisions** — Decision record lifecycle with review and deployment
3. **Approvals** — Approval request lifecycle with escalation support
4. **Compliance Assessments** — Assessment lifecycle from planning to closure
5. **Risk Assessments** — Risk evaluation lifecycle with monitoring
6. **Reports** — Report generation lifecycle with scheduling
7. **Configuration Versions** — Configuration version lifecycle with rollout
8. **Feature Flags** — Feature flag lifecycle from development to retirement

## Transition Validation
- State machine rules enforce valid transitions
- Pre-condition checks before transition execution
- Authorization verification for state changes
- Dependency validation (ensure dependent entities allow transition)
- Business rule evaluation before transition
- Concurrent transition prevention via distributed locks

## Transition History
- Complete transition audit trail
- Timestamp, actor, source state, target state, reason
- Associated metadata and context
- Transition duration tracking
- Failure recording with error details
- Queryable by entity type, entity ID, date range

## Automation Rules
- **Time-based** — Schedule transitions at specific times/dates
- **Event-based** — Trigger transitions on Kafka events
- **Condition-based** — Evaluate conditions before transition
- **Policy-based** — Enforce governance policy rules on transitions
- **Approval-based** — Require approval for specific transitions
- **Escalation-based** — Automatic escalation on transition failure

## Integration
- **LifecycleManager** — Core API for state management
- **AutomationEngine** — Orchestrates automated transitions
- **ExpirationManager** — Handles time-based expirations
- **WorkflowOrchestrator** — Executes workflows triggered by transitions
