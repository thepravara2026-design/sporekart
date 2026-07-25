# Workflow SDK

## Overview
The Workflow SDK provides a programmatic interface for consuming workflow orchestration capabilities. Future automation modules MUST only consume this SDK.

## Components
| Class | Responsibility |
|---|---|
| WorkflowSDK (interface) | Full API contract |
| WorkflowClient | Primary implementation delegating to service |
| WorkflowRuntime | Workflow execution lifecycle |
| WorkflowRegistry | Definition management |
| WorkflowExecutor | Mock action execution |
| WorkflowSimulator | Simulation operations |
| WorkflowTelemetry | Metrics and health |
| WorkflowBuilder | Fluent builder for definitions |

## WorkflowBuilder Usage
```java
var def = new WorkflowBuilder("Order", "Process order", WorkflowType.ORDER, "Orders", "system")
    .addStep("Validate", "validation", "Validate input", 1)
    .addStep("Execute", "execution", "Execute action", 2, "Validate")
    .addConfig("timeout", 30)
    .build();
```

## WorkflowClient Capabilities
- List/Get/Generate workflow definitions
- Start, pause, resume, cancel, retry workflows
- Simulate, dry-run, rollback workflows
- Query audit trails, queue, approvals
- Access telemetry and health metrics
