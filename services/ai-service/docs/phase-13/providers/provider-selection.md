# Provider Selection

## Selection Strategies

```mermaid
graph TB
    REQ[Provider Request]
    SEL[Provider Selector]
    S1[Priority Strategy]
    S2[Capability Strategy]
    S3[Availability Strategy]
    S4[Fallback Strategy]
    S5[Round Robin]
    S6[Weighted Strategy]
    DEC[Selected Provider]
    
    REQ --> SEL
    SEL --> S1
    SEL --> S2
    SEL --> S3
    SEL --> S4
    SEL --> S5
    SEL --> S6
    S1 --> DEC
    S2 --> DEC
    S3 --> DEC
    S4 --> DEC
    S5 --> DEC
    S6 --> DEC
```

| Strategy | Order | Description |
|----------|-------|-------------|
| Priority | 1 | Select by configured priority |
| Capability | 2 | Filter by required capabilities |
| Availability | 3 | Filter by health status |
| Weighted | 6 | Weighted random selection |
| Round Robin | 7 | Distribute across providers |
| Fallback | 10 | Ordered fallback chain |

## Selection Flow

```mermaid
sequenceDiagram
    participant GW as Gateway
    participant SEL as ProviderSelector
    participant S1 as Priority
    participant S2 as Capability
    participant S3 as Availability
    participant REG as Registry

    GW->>SEL: select(request)
    SEL->>REG: getActiveProviders()
    REG-->>SEL: provider list
    SEL->>S1: apply(providers, request)
    S1-->>SEL: ranked providers
    SEL->>S2: apply(providers, request)
    S2-->>SEL: filtered providers
    SEL->>S3: apply(providers, request)
    S3-->>SEL: available providers
    SEL-->>GW: selected provider
```

## Capability Model

```mermaid
classDiagram
    class ProviderCapability {
        CHAT
        COMPLETION
        STREAMING
        VISION
        EMBEDDINGS
        FUNCTION_CALLING
        JSON_OUTPUT
        LONG_CONTEXT
        REASONING
        TOOL_EXECUTION
        FINE_TUNING
        BATCH_API
        MODERATION
        MULTI_MODAL
    }
    class CapabilityProfile {
        +List~ProviderCapability~ capabilities
        +boolean hasCapability(ProviderCapability)
        +boolean hasAllCapabilities(List)
    }
    class CapabilityRegistry {
        <<interface>>
        +registerCapability(providerId, capability)
        +findProvidersByCapability(capability)
        +getProfile(providerId)
    }
    ProviderCapability --> CapabilityProfile
    CapabilityRegistry --> CapabilityProfile
```
