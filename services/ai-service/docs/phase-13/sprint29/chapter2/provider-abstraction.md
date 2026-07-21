# Sprint 29 — Chapter 2: AI Provider Abstraction Layer

## Architecture

```
AI Gateway
    │
    ▼
ProviderManager
    │
    ├── ProviderRegistry ─── CapabilityMatrix
    ├── ProviderFactory
    ├── HealthMonitor
    └── ProviderSelector
            │
    ┌───────┼───────────────┐
    ▼       ▼               ▼
OpenAI  Gemini  Claude  Ollama  Groq  Mistral  Bedrock  OpenRouter  TogetherAI  Azure
    │       │       │       │       │       │        │         │           │       │
    └───────┴───────┴───────┴───────┴───────┴────────┴─────────┴───────────┴───────┘
                                │
                            AIProvider
                            (Interface)
```

## Package Structure

```
com.sporekart.ai.provider
├── interfaces/
│   └── AIProvider.java                     Provider contract (9 generation methods, health, capabilities)
├── models/
│   ├── ProviderRequest.java                Immutable request record
│   ├── ProviderResponse.java               Immutable response with TokenUsage
│   ├── ProviderHealth.java                 Health status (HEALTHY/DEGRADED/UNHEALTHY/OFFLINE/UNKNOWN)
│   ├── ProviderCapabilities.java           Capability matrix model
│   ├── ProviderCostInfo.java               Cost per token with pricing tier
│   ├── ProviderRateLimits.java             Rate limit configuration
│   ├── ProviderMetrics.java                Performance metrics with success/failure tracking
│   ├── ProviderStatistics.java             Aggregated statistics
│   └── ProviderInfo.java                   Provider metadata record
├── configuration/
│   ├── ProviderConfig.java                 Per-provider configuration record
│   └── EnvironmentConfigProvider.java      Env-var based configuration (SPOREKART_AI_* prefix)
├── registry/
│   ├── ProviderRegistry.java               Registry interface (register, unregister, lookup, list)
│   └── ProviderRegistryImpl.java           Thread-safe ConcurrentHashMap implementation
├── factory/
│   └── ProviderFactory.java                Factory for provider instantiation
├── health/
│   └── HealthMonitor.java                  Scheduled health checks, heartbeat tracking, aggregate health
├── selectors/
│   ├── SelectionStrategy.java              Strategy interface
│   ├── SelectionContext.java                Selection context (module, preferred provider, hints)
│   ├── ProviderSelector.java               Selector with 7 built-in strategies + manual override
│   └── strategies (7 built-in):
│       ├── FirstAvailableStrategy          Pick first available provider
│       ├── LowestLatencyStrategy            Pick lowest latency provider
│       ├── CheapestStrategy                Pick cheapest provider
│       ├── PreferredStrategy               Pick preferred provider if available
│       ├── RandomStrategy                  Random selection
│       ├── WeightedStrategy                Weighted random selection
│       └── RoundRobinStrategy              Round-robin selection
├── capability/
│   └── CapabilityMatrix.java               Index-based capability lookup with model-level indexing
├── implementations/
│   ├── MockAIProvider.java                 Configurable mock implementation for all 10 providers
│   └── ProviderDefinitions.java            Provider registry definitions
└── (existing)
    ├── api/
    ├── domain/
    ├── application/
    ├── config/
    └── infrastructure/
```

## Provider Interface (`AIProvider`)

```java
public interface AIProvider {
    String providerId();
    String providerName();
    String providerVersion();

    // Generation methods
    ProviderResponse generateCompletion(ProviderRequest request);
    ProviderResponse generateChat(ProviderRequest request);
    ProviderResponse generateEmbeddings(ProviderRequest request);
    ProviderResponse generateStreaming(ProviderRequest request);
    CompletableFuture<ProviderResponse> generateCompletionAsync(ProviderRequest request);
    CompletableFuture<ProviderResponse> generateChatAsync(ProviderRequest request);

    // Health & capabilities
    ProviderHealth checkHealth();
    ProviderCapabilities getCapabilities();

    // Metadata
    List<String> supportedModels();
    List<String> supportedModalities();
    int maxTokens();
    ProviderCostInfo costInfo();
    ProviderRateLimits rateLimits();

    // Status
    boolean isAvailable();
    Optional<String> version();
}
```

## Provider Lifecycle

```
REGISTERED → VALIDATED → ACTIVE → DEGRADED → OFFLINE
                │            │         │          │
                └────────────┴─────────┴──────────┘
                            │
                        REMOVED
```

1. **Registration** — `ProviderRegistry.register(provider)` stores the provider
2. **Health Check** — `HealthMonitor` runs periodic checks, updates health status
3. **Selection** — `ProviderSelector.select()` picks provider based on strategy
4. **Execution** — Gateway calls generation methods on selected provider
5. **Failover** — On failure, `selectFallback()` picks next available provider
6. **Recovery** — HealthMonitor detects recovery, marks provider healthy again

## Configuration

All provider configuration comes from environment variables:

```
SPOREKART_AI_OPENAI_API_KEY=sk-...
SPOREKART_AI_OPENAI_ENDPOINT=https://api.openai.com
SPOREKART_AI_OPENAI_TIMEOUT_MS=30000
SPOREKART_AI_OPENAI_MAX_RETRIES=3
SPOREKART_AI_OPENAI_ENABLED=true
SPOREKART_AI_OPENAI_RPM=500
SPOREKART_AI_OPENAI_TPM=100000
SPOREKART_AI_OPENAI_ORG_ID=org-...
SPOREKART_AI_OPENAI_PROJECT_ID=proj-...
SPOREKART_AI_OPENAI_DEPLOYMENT_NAME=gpt-4
SPOREKART_AI_OPENAI_MODEL_OVERRIDE=gpt-4-turbo
```

Pattern: `SPOREKART_AI_{PROVIDER_TYPE}_{KEY}`

System properties with dots are also supported: `sporekart.ai.openai.api-key`

## Selection Strategies

| Strategy | Description | Use Case |
|----------|-------------|----------|
| First Available | Pick first healthy provider | Default, simple routing |
| Lowest Latency | Pick provider with lowest response time | Real-time applications |
| Cheapest | Pick provider with lowest cost | Batch processing, cost-sensitive |
| Preferred | Try preferred first, fallback to first available | User preference |
| Random | Random selection | Load distribution, A/B testing |
| Weighted | Weighted random (by inverse cost) | Cost-aware load balancing |
| Round Robin | Sequential rotation | Even load distribution |
| Manual Override | Explicit provider selection | Admin override |

## Capability Matrix

The `CapabilityMatrix` builds an index of all providers and their capabilities:

```
streaming       → {openai-1, gemini-1, claude-1, ...}
vision          → {gemini-1, openai-1}
embeddings      → {openai-1, gemini-1, ollama-1}
function-calling → {openai-1, gemini-1, claude-1}
reasoning       → {claude-1}
model:gpt-4     → {openai-1, openrouter-1}
model:gemini-pro → {gemini-1}
```

Dynamic discovery: `findProvidersWithCapability()`, `findBestProviderFor()`

## Provider Definitions

10 standard providers configured with realistic defaults:

| Provider | Models | Modalities | Cost Tier | Max Tokens |
|----------|--------|------------|-----------|------------|
| OpenAI | gpt-4, gpt-4-turbo, gpt-3.5-turbo, text-embedding-3 | text, chat, embeddings, vision, audio | HIGH | 8192 |
| Gemini | gemini-pro, gemini-pro-vision, gemini-ultra | text, chat, vision, embeddings | MEDIUM | 8192 |
| Claude | claude-3-opus, claude-3-sonnet, claude-3-haiku | text, chat, reasoning, long-context | HIGH | 100000 |
| Azure OpenAI | gpt-4, gpt-4-turbo, gpt-3.5-turbo | text, chat, embeddings, vision | HIGH | 8192 |
| Ollama | llama3, mistral, codellama | text, chat, embeddings | FREE | 4096 |
| Groq | mixtral-8x7b, llama3-70b, gemma-7b | text, chat | LOW | 4096 |
| Mistral | mistral-large, mistral-medium, mistral-small | text, chat | MEDIUM | 4096 |
| OpenRouter | gpt-4, claude-3-opus, gemini-pro, mixtral | text, chat | CUSTOM | 4096 |
| Bedrock | anthropic.claude-v2, amazon.titan-text | text, chat | CUSTOM | 4096 |
| Together AI | mixtral-8x7b, llama3-70b | text, chat | CUSTOM | 4096 |

## Extension Guide

Adding a new provider requires:

1. **Implement `AIProvider`** — All generation methods, health check, capabilities
2. **Register with `ProviderRegistry`** — `registry.register(new MyProvider())`
3. **Add environment config** — `SPOREKART_AI_MYPROVIDER_API_KEY=...`
4. **(Optional) Add capability mappings** — CapabilityMatrix auto-indexes

```java
public class MyCustomProvider implements AIProvider {
    // Implement all interface methods
}
```

## Testing

| Test Suite | Tests | Coverage |
|------------|-------|----------|
| ProviderRegistryTest | 14 | Registry operations, lookup, health tracking |
| ProviderFactoryTest | 7 | Factory creation, configuration |
| ProviderSelectorTest | 12 | All 7 strategies, fallback, custom strategies |
| HealthMonitorTest | 10 | Health checks, aggregation, failure tracking |
| CapabilityMatrixTest | 12 | Index, lookup, model indexing |
| MockProviderTest | 20 | All provider types, generation, health, cost |
| EnvironmentConfigProviderTest | 5 | Env var loading, defaults |
| **Total** | **80** | **95%+** |
