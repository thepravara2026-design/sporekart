# Sprint 17 — Part 8: Enterprise AI Workflow & Intelligent Automation Platform

**Date:** 2026-07-12
**Module:** ai-service
**Lead:** Enterprise AI Platform Engineering Team

---

## Objective

Build the Enterprise AI Workflow & Intelligent Automation Platform — workflow definitions, execution engine, step dispatcher, state machine, scheduling, REST APIs, Flyway V17 migration, Redis caching, Kafka events, health monitoring, and comprehensive tests. No workflow builder UI, no drag-and-drop designer, no business AI assistants, no mobile apps. Provider SDK calls are excluded — the engine remains provider-agnostic.

Business modules MUST NOT communicate directly with AI providers; workflow execution flow: Workflow API → Workflow Engine → Step Dispatcher → Workflow Steps → AI Gateway → Provider Framework.

Future Android/iOS must consume same APIs without backend redesign.

---

## Deliverables

### Workflow Platform Modules

| Module | Package | Purpose |
|--------|---------|---------|
| workflow-core | `com.sporekart.ai.workflow.domain` | Domain records, enums |
| workflow-api | `com.sporekart.ai.workflow.api` | Port interfaces (WorkflowService, WorkflowExecutionService, WorkflowSchedulerService, WorkflowEngine, WorkflowStepDispatcher) |
| workflow-application | `com.sporekart.ai.workflow.application` | Application services, state machine, step dispatcher |
| workflow-infrastructure | `com.sporekart.ai.workflow.infrastructure` | JPA, Redis, Kafka, monitoring |
| workflow-interfaces | `com.sporekart.ai.workflow.interfaces.rest` | REST controller + DTOs |

---

### Domain Model

| Record/Enum | Fields | Purpose |
|-------------|--------|---------|
| `WorkflowStatus` | DRAFT, ACTIVE, DEACTIVATED, ARCHIVED | Definition lifecycle states |
| `WorkflowExecutionStatus` | PENDING, RUNNING, PAUSED, COMPLETED, FAILED, CANCELLED, TIMED_OUT | Execution lifecycle states |
| `WorkflowTriggerType` | MANUAL, SCHEDULED, EVENT, WEBHOOK, API | Trigger types |
| `WorkflowStepType` | TASK, CONDITION, SUB_WORKFLOW, NOTIFICATION, TRANSFORMATION, APPROVAL, WAIT, LOOP, CUSTOM | Step types |
| `WorkflowStepStatus` | PENDING, IN_PROGRESS, COMPLETED, FAILED, SKIPPED | Step execution status |
| `WorkflowConditionType` | IF_ELSE, SWITCH, COMPARE, REGEX, SCRIPT, RULE | Condition types |
| `WorkflowDefinition` | id, name, description, category, status, version, triggerType, triggerConfig, metadata, isTemplate, createdBy, createdAt, updatedAt | Core workflow definition |
| `WorkflowVersion` | id, workflowId, version, definitionJson, isActive, createdAt | Versioning record |
| `WorkflowStep` | id, workflowId, name, type, orderIndex, config, metadata, isOptional, timeoutMs, maxRetries | Step definition |
| `WorkflowCondition` | id, workflowId, stepId, conditionType, expression, description | Conditional branching |
| `WorkflowAction` | id, workflowId, stepId, actionType, config, onSuccess, onFailure | Action handlers |
| `WorkflowExecution` | id, workflowId, workflowVersion, status, triggerType, triggerData, startedBy, startedAt, completedAt, errorMessage, retryCount | Execution tracking |
| `WorkflowExecutionState` | id, executionId, workflowId, currentStep, context, variables, status, createdAt, updatedAt | State machine state |
| `WorkflowSchedule` | id, workflowId, cronExpression, startAt, endAt, isActive, timezone, lastExecutedAt, nextExecutionAt | Scheduled execution |

---

### API Endpoints (18 endpoints)

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/v1/workflows` | Create a new workflow definition |
| GET | `/api/v1/workflows/{workflowId}` | Get a workflow definition by ID |
| GET | `/api/v1/workflows` | List all workflow definitions |
| PUT | `/api/v1/workflows/{workflowId}` | Update a workflow definition |
| DELETE | `/api/v1/workflows/{workflowId}` | Delete a workflow definition |
| POST | `/api/v1/workflows/{workflowId}/publish` | Publish workflow (DRAFT → ACTIVE) |
| POST | `/api/v1/workflows/{workflowId}/deactivate` | Deactivate workflow (ACTIVE → INACTIVE) |
| POST | `/api/v1/workflows/{workflowId}/clone` | Clone a workflow definition |
| POST | `/api/v1/workflows/{workflowId}/steps` | Add a step to a workflow |
| GET | `/api/v1/workflows/{workflowId}/steps` | Get steps for a workflow |
| DELETE | `/api/v1/workflows/{workflowId}/steps/{stepId}` | Remove a step from a workflow |
| POST | `/api/v1/workflows/execute` | Execute a workflow |
| GET | `/api/v1/workflows/executions/{executionId}` | Get execution status |
| GET | `/api/v1/workflows/executions` | List executions for a workflow |
| POST | `/api/v1/workflows/executions/{executionId}/cancel` | Cancel an execution |
| GET | `/api/v1/workflows/executions/{executionId}/state` | Get execution state |
| POST | `/api/v1/workflows/schedules` | Create a schedule |
| GET | `/api/v1/workflows/schedules` | List schedules for a workflow |
| PUT | `/api/v1/workflows/schedules/{scheduleId}/pause` | Pause a schedule |
| PUT | `/api/v1/workflows/schedules/{scheduleId}/resume` | Resume a schedule |
| GET | `/api/v1/workflows/health` | Workflow module health check |

---

### State Machine (WorkflowStateMachine)

Valid transitions for `WorkflowExecutionStatus`:

| From | To | Valid |
|------|----|-------|
| PENDING | RUNNING | ✅ |
| PENDING | CANCELLED | ✅ |
| RUNNING | PAUSED | ✅ |
| RUNNING | COMPLETED | ✅ |
| RUNNING | FAILED | ✅ |
| PAUSED | RUNNING | ✅ |
| RUNNING | TIMED_OUT | ✅ |
| PENDING | COMPLETED | ❌ |
| COMPLETED | RUNNING | ❌ |
| CANCELLED | RUNNING | ❌ |
| FAILED | RUNNING | ❌ |
| PAUSED | COMPLETED | ❌ |

---

### Flyway Migration V17

| Table | Description |
|-------|-------------|
| `ai_workflows` | Workflow definitions |
| `ai_workflow_versions` | Version snapshots |
| `ai_workflow_steps` | Workflow step definitions |
| `ai_workflow_conditions` | Conditional branching rules |
| `ai_workflow_actions` | Action handler configuration |
| `ai_workflow_executions` | Execution tracking |
| `ai_workflow_execution_history` | Execution history log |
| `ai_workflow_execution_state` | State machine state |
| `ai_workflow_schedules` | Scheduled execution configs |

All tables use UUID primary keys, `TIMESTAMP` for datetimes, `TEXT` for large content, H2-compatible syntax.

---

### File Count

| Layer | Files |
|-------|-------|
| Domain (enums + records) | 14 |
| API (interfaces) | 5 |
| Application (services + state machine) | 8 |
| Infrastructure (JPA entities + repos + Kafka + Redis + monitoring) | 16 |
| Interfaces (controller + DTOs) | 10 |
| Config (Flyway + config updates) | 6 |
| Tests | 12 test classes ~168 tests |
| **Total** | **~71 source files, ~168 tests** |
