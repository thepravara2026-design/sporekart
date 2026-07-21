# Enterprise AI Provider Circuit Breaker

## Overview

The Circuit Breaker framework provides automatic failure isolation and recovery for AI providers, preventing cascading failures across the platform.

## Circuit Breaker States

```mermaid
stateDiagram-v2
    [*] --> CLOSED
    CLOSED --> OPEN
    OPEN --> HALF_OPEN
    HALF_OPEN --> CLOSED
    HALF_OPEN --> OPEN
    OPEN --> CLOSED
```

## Circuit Breaker Flow

```mermaid
sequenceDiagram
    participant GW as Gateway
    participant CB as CircuitBreaker
    participant PR as Provider

    GW->>CB: allowRequest()
    CB-->>GW: true/false
    alt allowed
        GW->>PR: execute request
        PR-->>GW: success/failure
        GW->>CB: onSuccess()/onFailure()
    end
    CB->>CB: evaluate state transition
```

## State Transitions

| From | To | Condition |
|------|----|-----------|
| CLOSED | OPEN | Failure threshold exceeded |
| OPEN | HALF_OPEN | Timeout elapsed + cooldown |
| HALF_OPEN | CLOSED | Success threshold reached |
| HALF_OPEN | OPEN | Single failure during half-open |

## Configuration

| Parameter | Description |
|-----------|-------------|
| failureThreshold | Failures before opening circuit |
| successThreshold | Successes before closing circuit |
| timeout | Duration before attempting reset |
| halfOpenMaxDuration | Max time in half-open state |
| halfOpenMaxCalls | Max calls during half-open |
| automaticReset | Enable auto reset |
| cooldownPeriod | Cooldown between retries |
