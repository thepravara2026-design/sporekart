# Workflow Execution Engine

## Overview
The Workflow Execution Engine manages the lifecycle operations on workflow instances: start, pause, resume, cancel, retry, complete, fail, and archive.

## Operations
| Operation | Description | Valid States |
|---|---|---|
| start | Create and queue instance | CREATED |
| simulate | Create simulation instance | CREATED |
| pause | Suspend execution | RUNNING |
| resume | Continue execution | PAUSED |
| cancel | Abort execution | CREATED, QUEUED, PENDING, RUNNING, PAUSED, WAITING_APPROVAL, RETRYING |
| retry | Re-attempt execution | FAILED |
| complete | Mark as successful | RUNNING |
| fail | Mark as failed | PENDING, RUNNING, WAITING_APPROVAL, RETRYING |
| requestApproval | Enter approval gate | RUNNING |
| archive | Move to terminal state | COMPLETED, FAILED, CANCELLED |

## Retry Logic
- Configurable max retry attempts (default: 3)
- Retry count tracked per instance
- Throws IllegalStateException when max exceeded
- Backoff configuration available (default: 1000ms)

## Audit Trail
Every execution operation creates an audit entry with:
- Instance ID, action, actor, details, outcome
- Timestamp and metadata
