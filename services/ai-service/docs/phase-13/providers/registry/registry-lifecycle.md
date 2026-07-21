# Enterprise AI Provider Registry Lifecycle

## Registration Lifecycle

```mermaid
stateDiagram-v2
    [*] --> DRAFT
    DRAFT --> SUBMITTED
    SUBMITTED --> VALIDATING
    VALIDATING --> VALIDATED
    VALIDATED --> APPROVED
    VALIDATED --> REJECTED
    APPROVED --> REGISTERED
    REGISTERED --> [*]
    SUBMITTED --> CANCELLED
    DRAFT --> CANCELLED
```

## Activation Lifecycle

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
    INACTIVE --> REMOVED
```

## Status Transition

```mermaid
stateDiagram-v2
    [*] --> REGISTERED
    REGISTERED --> VALIDATED
    VALIDATED --> INITIALIZING
    INITIALIZING --> ACTIVE
    ACTIVE --> DEGRADED
    DEGRADED --> ACTIVE
    ACTIVE --> MAINTENANCE
    MAINTENANCE --> ACTIVE
    ACTIVE --> DEACTIVATED
    DEACTIVATED --> ACTIVE
    DEACTIVATED --> FAILED
    FAILED --> ACTIVE
    FAILED --> REMOVED
    DEACTIVATED --> REMOVED
    REMOVED --> [*]
```

## Lifecycle Events

- **Registration**: Provider enters the registry
- **Validation**: Provider metadata and capabilities validated
- **Activation**: Provider becomes available for use
- **Maintenance**: Provider temporarily unavailable
- **Deprecation**: Provider marked for removal
- **Removal**: Provider permanently removed from registry
