# AI Provider Abstraction Architecture

**Version:** 1.0.0
**Last Updated:** 2026-07-11
**Owner:** Enterprise AI Platform Engineering Team

---

## Overview

The AI Provider Abstraction Layer sits between the AI Gateway (Part 2) and external AI providers. It provides a uniform interface for all AI providers, enabling provider swapping through configuration only. Business modules never know which provider is being used.

---

## Architecture Diagram

```
Business Modules
       │
       ▼
┌──────────────────────────────────────────────────────────────────────┐
│                          AI Gateway (Part 2)                          │
│  GatewayPipeline → ProviderResolver → GatewayApplicationService      │
└──────────────────────────────────────────────────────────────────────┘
       │
       ▼
┌──────────────────────────────────────────────────────────────────────┐
│                    AI Provider Abstraction Layer (Part 3)             │
│                                                                      │
│  ┌──────────────────────────────────────────────────────────────┐   │
│  │                    Provider Registry                           │   │
│  │  Register → Discover → Enable/Disable → Version Tracking     │   │
│  └──────────────────────────────────────────────────────────────┘   │
│                                                                      │
│  ┌──────────────────────────────────────────────────────────────┐   │
│  │                    Provider Factory                            │   │
│  │  Resolve Config → Inject Dependencies → Validate → Return    │   │
│  └──────────────────────────────────────────────────────────────┘   │
│                                                                      │
│  ┌──────────────────────────────────────────────────────────────┐   │
│  │                    Provider Adapters                           │   │
│  │  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────────┐   │   │
│  │  │  Gemini  │ │  OpenAI  │ │  Claude  │ │   Azure      │   │   │
│  │  │ Adapter  │ │ Adapter  │ │ Adapter  │ │   OpenAI     │   │   │
│  │  └──────────┘ └──────────┘ └──────────┘ └──────────────┘   │   │
│  │  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────────┐   │   │
│  │  │ Bedrock  │ │  Ollama  │ │  Mistral │ │   Local LLM  │   │   │
│  │  │ Adapter  │ │ Adapter  │ │ Adapter  │ │   Adapter    │   │   │
│  │  └──────────┘ └──────────┘ └──────────┘ └──────────────┘   │   │
│  └──────────────────────────────────────────────────────────────┘   │
└──────────────────────────────────────────────────────────────────────┘
       │
       ▼
External AI Providers (Google, OpenAI, Anthropic, Azure, AWS, Ollama, Mistral, Local)
```

---

## Module Structure

```
com.sporekart.ai
├── core.api (13 provider interfaces)
│   ├── AIProvider.java
│   ├── ChatProvider.java
│   ├── EmbeddingProvider.java
│   ├── GenerationProvider.java
│   ├── VisionProvider.java
│   ├── ModerationProvider.java
│   ├── ProviderCapabilities.java
│   ├── ProviderHealth.java
│   ├── ProviderConfiguration.java
│   ├── ProviderHealthService.java
│   ├── ProviderSelector.java
│   ├── ProviderFailoverStrategy.java
│   └── ProviderValidator.java
│
├── provider.api (SPI — existing, extended)
│   ├── ProviderPort.java
│   ├── ProviderRegistry.java
│   ├── ModelDiscovery.java
│   └── ProviderHealthIndicator.java
│
├── provider.domain (7 files)
│   ├── Provider.java (existing)
│   ├── ProviderCapability.java (existing)
│   ├── ProviderModel.java (new)
│   ├── ProviderHealthRecord.java (new)
│   ├── ProviderConfigurationRecord.java (new)
│   └── ProviderCapabilityInfo.java (new)
│
├── provider.application (7 services)
│   ├── ProviderRegistryImpl.java
│   ├── ProviderFactoryImpl.java
│   ├── ProviderHealthServiceImpl.java
│   ├── ProviderConfigurationService.java
│   ├── ProviderSelectorImpl.java
│   ├── ProviderValidatorImpl.java
│   └── ProviderFailoverImpl.java
│
├── provider.infrastructure (10 files)
│   ├── GeminiAdapter.java
│   ├── OpenAIAdapter.java
│   ├── ClaudeAdapter.java
│   ├── AzureOpenAIAdapter.java
│   ├── BedrockAdapter.java
│   ├── OllamaAdapter.java
│   ├── MistralAdapter.java
│   ├── LocalLLMAdapter.java
│   ├── ProviderKafkaEventPublisher.java
│   └── ProviderRedisCacheService.java
│
├── provider.config
│   └── AiProviderConfig.java (registers all 8 adapters)
│
└── interfaces.rest
    └── ProviderController.java (6 endpoints)
```

---

## Design Patterns

| Pattern | Usage |
|---------|-------|
| **Adapter** | Each provider has an adapter implementing `ProviderPort` + `AIProvider` |
| **Factory** | `ProviderFactoryImpl` creates and returns provider instances |
| **Registry** | `ProviderRegistryImpl` maintains the provider catalog |
| **Strategy** | `ProviderSelectorImpl`, `ProviderFailoverImpl` implement selection/failover strategies |
| **Facade** | `ProviderController` serves as the REST facade |

---

## Cross-Cutting Concerns

### Observability
- Provider health tracked in `ProviderHealthServiceImpl` with consecutive failure counting
- Kafka events published on registration, enablement, health changes, switching, validation failures
- Micrometer counters and timers placeholders for provider-level metrics

### Security
- Provider validation via `ProviderValidatorImpl`
- Credential abstraction — no API keys in source code
- Feature flags gate every provider
- `is_secret` column in `ai_provider_configuration` for credential marking

### Caching (Redis)
- Registry, capabilities, models, health, configuration cached with TTLs ranging from 1 min to 10 min
- Full invalidation on provider changes

### Events (Kafka)
- Topic: `ai-provider-events` (3 partitions)
- 6 event types for provider lifecycle management

---

## Provider Selection Flow

```
Request arrives with module + optional preferredProvider
       │
       ▼
ProviderSelector.select(module, preferredProvider)
       │
       ├── preferredProvider specified and enabled? → return it
       ├── module preferences available? → return first enabled
       ├── any provider enabled? → return first
       └── fallback → return MOCK
       │
       ▼
ProviderFactoryImpl.createProvider(selected)
       │
       ▼
ProviderAdapter.generate(AiRequest)
```

## Failover Flow

```
Provider fails
       │
       ▼
ProviderFailoverStrategy.determineFailover(failedProvider, module)
       │
       ├── Find next provider in FAILOVER_ORDER
       ├── Exclude failed provider
       ├── Max 2 failover attempts
       └── 1000ms delay between attempts
       │
       ▼
ProviderSelectorImpl.selectFallback(module, failedProvider)
```

---

## Feature Flag Gating

Every provider is feature-flagged. The `ProviderSelectorImpl` checks the `FeatureFlagService` before selecting a provider:

| Flag | Controls |
|------|----------|
| `AI_PROVIDER_ENABLED` | Master provider toggle |
| `AI_PROVIDER_GEMINI` | Gemini availability |
| `AI_PROVIDER_OPENAI` | OpenAI availability |
| `AI_PROVIDER_CLAUDE` | Claude availability |
| `AI_PROVIDER_AZURE_OPENAI` | Azure OpenAI availability |
| `AI_PROVIDER_BEDROCK` | AWS Bedrock availability (default: off) |
| `AI_PROVIDER_OLLAMA` | Ollama availability (default: off) |
| `AI_PROVIDER_MISTRAL` | Mistral availability (default: off) |
| `AI_PROVIDER_LOCAL_LLM` | Local LLM availability (default: off) |
