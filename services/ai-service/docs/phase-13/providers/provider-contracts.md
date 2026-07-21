# Provider Contracts

## Contract Types

| Contract | Purpose |
|----------|---------|
| ChatContract | Multi-turn chat completions |
| CompletionContract | Single-turn text completion |
| StreamingContract | Streaming response support |
| EmbeddingContract | Text vector embeddings |
| VisionContract | Image analysis |
| ImageGenerationContract | Image generation |
| AudioContract | Speech/transcription |
| ReasoningContract | Reasoning model invocation |
| ToolCallingContract | Tool/function execution |
| StructuredOutputContract | Structured JSON output |
| FunctionCallingContract | Function definition execution |
| JSONModeContract | Strict JSON mode |

## Exception Hierarchy

```mermaid
graph BT
    PE[ProviderException] --> RE[RuntimeException]
    PUE[ProviderUnavailableException] --> PE
    UCE[UnsupportedCapabilityException] --> PE
    PCE[ProviderConfigurationException] --> PE
    PAE[ProviderAuthenticationException] --> PE
    PRLE[ProviderRateLimitException] --> PE
    PTE[ProviderTimeoutException] --> PE
    PHE[ProviderHealthException] --> PE
    PDE[ProviderDiscoveryException] --> PE
    PVE[ProviderValidationException] --> PE
    PSE[ProviderSelectionException] --> PE
```

## Provider Metadata

| Field | Type | Description |
|-------|------|-------------|
| providerId | String | Unique identifier |
| providerName | String | Display name |
| type | ProviderType | Enum (OPENAI, GEMINI, etc.) |
| version | String | API version |
| supportedModels | List~String~ | Available models |
| regions | List~String~ | Deployment regions |
| contextWindow | long | Max context tokens |
| rateLimitPerMinute | int | RPM limit |
| streamingSupported | boolean | Streaming capability |

## Monitoring Architecture

```mermaid
graph TB
    PRO[Provider] --> MC[Metrics Collector]
    PRO --> HM[Health Monitor]
    PRO --> PT[Performance Tracker]
    PRO --> CT[Cost Tracker]
    PRO --> AT[Availability Tracker]
    MC --> DB[(Metrics Store)]
    HM --> DB
    PT --> DB
    CT --> DB
    AT --> DB
    DB --> DASH[Dashboard]
```
