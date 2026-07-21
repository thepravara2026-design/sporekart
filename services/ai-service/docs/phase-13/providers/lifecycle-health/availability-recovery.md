# Enterprise AI Provider Availability & Recovery

## Overview

The Availability and Recovery frameworks ensure providers maintain service levels and can recover automatically from failures.

## Availability Architecture

```mermaid
graph TB
    subgraph Availability
        AM[AvailabilityManager]
        AS[AvailabilitySnapshot]
        AH[AvailabilityHistory]
        AMET[AvailabilityMetrics]
        AP[AvailabilityPolicy]
        AE[AvailabilityEvents]
    end

    subgraph Recovery
        RM[RecoveryManager]
        RP[RecoveryPolicy]
        RC[RecoveryContext]
        RT[RecoveryTrigger]
        RMET[RecoveryMetrics]
        RA[RecoveryAudit]
    end

    AM --> RM
    RM --> RT
    RT --> RM
    AH --> AMET
    RP --> RM
```

## Recovery Flow

```mermaid
sequenceDiagram
    participant MON as Monitor
    participant RT as RecoveryTrigger
    participant RM as RecoveryManager
    participant PR as Provider

    MON->>RT: onFailure(providerId)
    RT->>RM: attemptRecovery(providerId)
    RM->>RM: getStrategy(providerId)
    RM->>PR: execute recovery
    PR-->>RM: success/failure
    alt success
        RM->>MON: markAvailable(providerId)
    else failure
        RM->>RM: retry if retries available
    end
```

## Recovery Strategies

| Strategy | Description |
|----------|-------------|
| AUTOMATIC | System-initiated recovery |
| MANUAL | Human-initiated recovery |
| SCHEDULED | Time-based recovery |
| GRACEFUL | Controlled recovery with drain |
| EMERGENCY | Immediate forced recovery |

## SLA Management

- Track availability percentage per provider
- Calculate uptime/downtime windows
- Monitor SLA compliance
- Alert on SLA violations
