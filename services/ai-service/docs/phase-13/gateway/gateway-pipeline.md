# AI Gateway Pipeline

## Pipeline Stages

The pipeline consists of 11 ordered stages that process each request.

```mermaid
graph LR
    V[Validation] --> AUTH[Authentication]
    AUTH --> AZ[Authorization]
    AZ --> Q[Quota Check]
    Q --> RL[Rate Limiter]
    RL --> PS[Provider Selection]
    PS --> R[Routing]
    R --> E[Execution]
    E --> PP[Post-Processing]
    PP --> AD[Audit]
    AD --> M[Metrics]
```

## Stage Details

| Stage | Order | Responsibility |
|-------|-------|----------------|
| VALIDATION | 1 | Validate request structure and required fields |
| AUTHENTICATION | 2 | Verify client identity |
| AUTHORIZATION | 3 | Check access permissions |
| QUOTA_CHECK | 4 | Verify token/resource quota |
| RATE_LIMITER | 5 | Enforce rate limits |
| PROVIDER_SELECTION | 6 | Select target AI provider |
| ROUTING | 7 | Route request to provider |
| EXECUTION | 8 | Execute provider API call |
| POST_PROCESSING | 9 | Transform/normalize response |
| AUDIT | 10 | Record audit trail |
| METRICS | 11 | Collect performance metrics |

## Pipeline Context

Each request creates a `PipelineContext` that flows through all stages:

```mermaid
classDiagram
    class PipelineContext {
        +String pipelineId
        +GatewayRequest request
        +GatewayResponse response
        +GatewayExecutionContext executionContext
        +PipelineStage currentStage
        +Map~String, Object~ attributes
        +Instant startedAt
        +Instant completedAt
        +boolean failed
        +String failureReason
    }
    class PipelineStage {
        +int order()
        +String description()
    }
    class PipelineInterceptor {
        +boolean before(PipelineContext)
        +void after(PipelineContext)
        +void onError(PipelineContext, Throwable)
    }
    PipelineContext --> PipelineStage
    PipelineContext --> PipelineInterceptor
```

## Stage Handlers

Each stage has a dedicated handler class:

- `ValidationStage` - Validates request
- `AuthenticationStage` - Verifies identity
- `AuthorizationStage` - Checks permissions
- `QuotaStage` - Verifies quota
- `RateLimiterStage` - Enforces rate limits
- `ProviderSelectionStage` - Selects provider
- `RoutingStage` - Routes to provider
- `ExecutionStage` - Executes provider call
- `PostProcessingStage` - Processes response
- `AuditStage` - Records audit
- `MetricsStage` - Collects metrics

## Interceptor Chain

```mermaid
sequenceDiagram
    participant EX as PipelineExecutor
    participant I1 as Interceptor 1
    participant I2 as Interceptor 2
    participant SH as StageHandler

    EX->>I1: before()
    I1-->>EX: continue=true
    EX->>I2: before()
    I2-->>EX: continue=true
    EX->>SH: handle()
    SH-->>EX: done
    EX->>I2: after()
    EX->>I1: after()
```

## Execution Flow

```mermaid
flowchart TD
    START([Start]) --> REQ[Receive Request]
    REQ --> CTX[Create PipelineContext]
    CTX --> LOOP{For each stage}
    LOOP -->|Yes| B{before interceptors<br/>all pass?}
    B -->|Yes| HANDLE[Execute stage handler]
    B -->|No| FAIL[Fail pipeline]
    HANDLE --> AFTER[after interceptors]
    AFTER --> NEXT[Next stage]
    NEXT --> LOOP
    LOOP -->|No| COMPLETE[Complete pipeline]
    COMPLETE --> RESP[Return response]
    FAIL --> ERR[Handle error]
    ERR --> RESP
```
