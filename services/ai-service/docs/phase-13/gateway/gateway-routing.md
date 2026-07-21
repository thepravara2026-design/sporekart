# AI Gateway Routing

## Provider Router Overview

The Provider Router selects which AI provider handles each request based on configurable strategies.

```mermaid
graph TB
    PL[Pipeline] --> RTR[Provider Router]
    RTR --> STR[Strategy Chain]
    STR --> FA[First Available]
    STR --> LO[Latency Optimized]
    STR --> CO[Cost Optimized]
    STR --> FB[Fallback]
    FA --> DEC[Routing Decision]
    LO --> DEC
    CO --> DEC
    FB --> DEC
    DEC --> PR[Provider]
```

## Routing Strategies

| Strategy | Order | Description |
|----------|-------|-------------|
| First Available | 1 | Use the first provider that matches |
| Latency Optimized | 2 | Route to lowest-latency provider |
| Cost Optimized | 3 | Route to cheapest provider |
| Fallback | 10 | Try fallback providers on failure |

## Strategy Chain

```mermaid
sequenceDiagram
    participant PL as Pipeline
    participant RTR as Router
    participant S1 as Strategy 1<br/>(First Available)
    participant S2 as Strategy 2<br/>(Latency)
    participant SN as Strategy N<br/>(Fallback)

    PL->>RTR: route(context)
    RTR->>S1: supports(context)?
    S1-->>RTR: yes
    RTR->>S1: resolve(context)
    S1-->>RTR: RoutingDecision
    RTR-->>PL: Provider resolved
    Note over RTR,SN: If no strategy supports,<br/>return unresolved
```

## Routing Decision

```mermaid
classDiagram
    class RoutingDecision {
        +String providerId
        +String model
        +String strategy
        +String reason
        +int priority
        +boolean fallback
        +boolean resolved
    }
    class ProviderRouterStrategy {
        <<interface>>
        +RoutingDecision resolve(PipelineContext)
        +String name()
        +int order()
        +boolean supports(PipelineContext)
    }
    RoutingDecision <.. ProviderRouterStrategy
```

## Provider Selection Criteria

- Model availability
- Region proximity
- Cost constraints
- Capability requirements (streaming, vision, audio)
- Tenant-specific routing rules
- Current provider health status
