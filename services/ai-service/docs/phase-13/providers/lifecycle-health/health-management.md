# Enterprise AI Provider Health Management

## Overview

The Health Management platform provides comprehensive health monitoring for every AI provider, including readiness, liveness, diagnostics, and health aggregation.

## Health Architecture

```mermaid
graph TB
    subgraph Health_Management
        HM[HealthManager]
        HC[HealthController]
        HA[HealthAggregator]
        HR[HealthReporter]
        HV[HealthValidator]
    end

    subgraph Health_Checkers
        CHK[HealthChecker]
        COMP[CompositeHealthChecker]
        RCHK[ReadinessChecker]
        LCHK[LivenessChecker]
    end

    subgraph Health_Data
        HS[HealthSnapshot]
        HH[HealthHistory]
        HP[HealthPolicy]
        HCONF[HealthConfiguration]
    end

    HM --> CHK
    HM --> RCHK
    HM --> LCHK
    CHK --> COMP
    HM --> HA
    HA --> HR
    HV --> HM
    HS --> HM
    HH --> HM
    HP --> HM
    HCONF --> HM
```

## Health Check Flow

```mermaid
sequenceDiagram
    participant GW as Gateway
    participant HM as HealthManager
    participant CHK as HealthChecker
    participant RCHK as ReadinessChecker
    participant LCHK as LivenessChecker

    GW->>HM: check(providerId)
    HM->>CHK: check(providerId)
    CHK-->>HM: HealthResponse
    HM->>RCHK: checkReadiness(providerId)
    RCHK-->>HM: ReadinessState
    HM->>LCHK: checkLiveness(providerId)
    LCHK-->>HM: LivenessState
    HM-->>GW: aggregated health result
```

## Health Model

Every provider exposes:

| Metric | Description |
|--------|-------------|
| Overall Health | Composite health status |
| Readiness | Provider ready to serve |
| Liveness | Provider process alive |
| Availability | Provider accessible |
| Latency | Response time |
| Failure Count | Total failures |
| Success Count | Total successes |
| Timeout Count | Total timeouts |
| Recovery Count | Total recoveries |
| Circuit Status | Circuit breaker state |
| Heartbeat Status | Heartbeat state |
| Maintenance Status | Maintenance mode |
| Resource Status | Resource utilization |
