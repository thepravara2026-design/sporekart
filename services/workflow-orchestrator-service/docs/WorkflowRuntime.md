# Workflow Runtime

## Overview
The Workflow Runtime manages the lifecycle of workflow instances from creation through completion. It validates state transitions, manages context, and maintains execution history.

## Lifecycle
```
CREATED → QUEUED → PENDING → RUNNING ⇄ PAUSED
                                       ⇄ WAITING_APPROVAL
                                       → COMPLETED
                                       → FAILED → RETRYING → RUNNING
                                       → CANCELLED
COMPLETED → ARCHIVED
FAILED → ARCHIVED
CANCELLED → ARCHIVED
```

## State Machine Rules
| From | To | Condition |
|---|---|---|
| CREATED | QUEUED | Auto on start |
| CREATED | CANCELLED | User cancellation |
| QUEUED | PENDING | Queue processing |
| QUEUED | CANCELLED | Pre-execution cancel |
| PENDING | RUNNING | Execution start |
| PENDING | CANCELLED | Pre-run cancel |
| PENDING | FAILED | Setup failure |
| RUNNING | PAUSED | User pause |
| RUNNING | WAITING_APPROVAL | Approval gate |
| RUNNING | COMPLETED | Success |
| RUNNING | FAILED | Error |
| RUNNING | RETRYING | Retry triggered |
| RUNNING | CANCELLED | Force cancel |
| PAUSED | RUNNING | Resume |
| PAUSED | CANCELLED | Cancel while paused |
| WAITING_APPROVAL | RUNNING | Approved |
| WAITING_APPROVAL | CANCELLED | Rejected |
| WAITING_APPROVAL | FAILED | Approval failure |
| RETRYING | RUNNING | Retry execute |
| RETRYING | FAILED | Retries exhausted |
| RETRYING | CANCELLED | Cancel during retry |

## Execution Context
Each workflow instance carries:
- `context`: Input parameters and runtime data
- `result`: Output from completed execution
- `transitions`: Ordered list of state transitions with timestamps
- `traceId`: Unique trace for distributed correlation
