# Workflow Definition Engine

## Overview
The Workflow Definition Engine generates and manages reusable workflow definitions. Each definition consists of a trigger, validation steps, business rules, decision points, mock execution, audit, and completion.

## Step Types
| Type | Description |
|---|---|
| validation | Input validation, eligibility checks |
| decision | Business rule evaluation, scoring |
| execution | Mock action execution |
| approval | Manual approval gate |
| completion | Finalization, notification |

## Step Model
| Field | Type | Description |
|---|---|---|
| name | String | Step name |
| type | String | Step type (validation/execution/etc.) |
| description | String | Purpose |
| dependsOn | List<String> | Prerequisite step names |
| config | Map | Step-specific configuration |
| order | int | Execution order |

## Conditional Branching
Definitions support:
- **Sequential tasks**: Steps execute in order with dependency tracking
- **Parallel tasks**: Steps with no dependencies can execute concurrently
- **Conditional paths**: Decision engine determines execution path
- **Retry policies**: Configurable retry count per instance
- **Timeout policies**: Configurable via workflow config
- **Manual approval**: Approval gates pause execution
- **Rollback simulation**: Simulated rollback on failure
