# Agent Runtime

## Overview

The Agent Runtime provides execution infrastructure for AI agents. It supports agent registration, scheduling, execution with tool integration, and full lifecycle management.

## Capabilities

- **Agent Registration** — Define agent types, tools, prompts
- **Execution** — Synchronous and asynchronous execution
- **Scheduling** — Cron-based execution schedules
- **Tool Registry** — Register and invoke tools
- **Orchestration** — Multi-step workflow execution
- **Memory Integration** — Context-aware execution
- **Lifecycle Management** — Activate, pause, archive, delete
- **Monitoring** — Execution metrics and tracing

## Architecture

```mermaid
graph TB
    subgraph Runtime["Agent Runtime"]
        SERVICE["AgentRuntimeService"]
        EXECUTOR["AgentExecutor"]
        SCHEDULER["AgentScheduler"]
        TOOLS["AgentToolRegistry"]
        ORCH["AgentOrchestrator"]
        LIFECYCLE["AgentLifecycleManager"]
        
        SERVICE --> EXECUTOR
        SERVICE --> SCHEDULER
        SERVICE --> TOOLS
        SERVICE --> ORCH
        SERVICE --> LIFECYCLE
        ORCH --> EXECUTOR
        ORCH --> TOOLS
    end

    subgraph Storage["Storage"]
        DB[("PostgreSQL<br/>agent_definitions<br/>agent_executions")]
        CACHE[("Redis Cache")]
    end

    subgraph Messaging["Messaging"]
        KAFKA["Kafka<br/>ai-runtime-events"]
    end

    subgraph Integrations["Integrations"]
        MEMORY["Memory Platform"]
        GATEWAY["AI Gateway"]
        KNOWLEDGE["Knowledge Platform"]
    end

    EXECUTOR --> DB
    EXECUTOR --> CACHE
    SCHEDULER --> KAFKA
    ORCH --> MEMORY
    ORCH --> GATEWAY
    ORCH --> KNOWLEDGE
    LIFECYCLE --> DB
```

## API Endpoints

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/v1/ai/runtime/agents` | Register a new agent |
| GET | `/api/v1/ai/runtime/agents/{id}` | Get agent definition |
| GET | `/api/v1/ai/runtime/agents` | List agents (filterable) |
| PUT | `/api/v1/ai/runtime/agents/{id}` | Update agent |
| DELETE | `/api/v1/ai/runtime/agents/{id}` | Delete agent |
| POST | `/api/v1/ai/runtime/agents/{id}/execute` | Execute agent (async) |
| POST | `/api/v1/ai/runtime/agents/{id}/execute-sync` | Execute agent (sync) |
| GET | `/api/v1/ai/runtime/executions/{id}` | Get execution result |
| GET | `/api/v1/ai/runtime/agents/{id}/executions` | Get execution history |
| POST | `/api/v1/ai/runtime/executions/{id}/cancel` | Cancel execution |
| PUT | `/api/v1/ai/runtime/agents/{id}/status` | Update agent status |
| GET | `/api/v1/ai/runtime/metrics` | Get runtime metrics |
| GET | `/api/v1/ai/runtime/health` | Health check |
| POST | `/api/v1/ai/runtime/schedules` | Create schedule |
| GET | `/api/v1/ai/runtime/schedules/{agentId}` | Get schedule |
| DELETE | `/api/v1/ai/runtime/schedules/{agentId}` | Delete schedule |
| POST | `/api/v1/ai/runtime/schedules/{agentId}/trigger` | Trigger now |

## Domain Model

```mermaid
classDiagram
    class AgentDefinition {
        UUID id
        String name
        String description
        AgentType type
        AgentStatus status
        String systemPrompt
        List~String~ tools
        Map~String,String~ configuration
        int maxIterations
        int maxTokens
        double temperature
        Instant createdAt
        Instant updatedAt
        String version
    }

    class AgentExecution {
        UUID id
        UUID agentId
        String sessionId
        String userId
        ExecutionStatus status
        String input
        String output
        Map~String,String~ metadata
        int iterationsUsed
        int tokensUsed
        Instant startedAt
        Instant completedAt
        String errorMessage
    }

    class AgentType {
        <<enumeration>>
        CHAT
        TASK
        WORKFLOW
        COPILOT
        AUTOMATION
        ANALYST
        CUSTOM
    }

    class AgentStatus {
        <<enumeration>>
        DRAFT
        ACTIVE
        PAUSED
        DISABLED
        ARCHIVED
        FAILED
    }

    class ExecutionStatus {
        <<enumeration>>
        PENDING
        RUNNING
        COMPLETED
        FAILED
        TIMEOUT
        CANCELLED
        PAUSED
    }
```
