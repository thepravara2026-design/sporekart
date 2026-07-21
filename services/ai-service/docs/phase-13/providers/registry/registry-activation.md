# Enterprise AI Provider Activation Workflow

## Overview

The Activation Workflow manages the lifecycle of provider activation from initial registration through active service to eventual removal.

## Activation Flow

```mermaid
sequenceDiagram
    participant User
    participant RAM as RegistrationManager
    participant VAL as Validator
    participant ACT as ActivationManager
    participant CAT as Catalog
    participant MON as Monitor

    User->>RAM: initiate registration
    RAM->>VAL: validate entry
    VAL-->>RAM: validation result
    RAM->>ACT: activate provider
    ACT->>CAT: add to catalog
    CAT-->>ACT: catalog entry
    ACT->>MON: start monitoring
    MON-->>ACT: monitoring active
    ACT-->>RAM: activation complete
    RAM-->>User: provider ready
```

## Activation States

```mermaid
stateDiagram-v2
    [*] --> INACTIVE
    INACTIVE --> REGISTERED
    REGISTERED --> VALIDATED
    VALIDATED --> READY
    READY --> ACTIVE
    ACTIVE --> MAINTENANCE
    MAINTENANCE --> ACTIVE
    ACTIVE --> DEPRECATED
    DEPRECATED --> REMOVED
    REMOVED --> [*]
```

## Activation Manager Responsibilities

- Activate providers for use
- Deactivate providers when needed
- Track activation state
- Support maintenance mode
- List active/inactive providers
