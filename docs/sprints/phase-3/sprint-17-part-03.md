# Sprint 17 — Part 3: Enterprise AI Provider Abstraction Layer

**Date:** 2026-07-11
**Module:** ai-service
**Lead:** Enterprise AI Platform Engineering Team

---

## Objective

Build the Enterprise AI Provider Framework — the abstraction layer that sits between the AI Gateway (Part 2) and external AI providers. Business modules must never know which AI provider is being used. Providers are swappable through configuration only.

---

## Deliverables

### Core Interfaces (13 in `core/api`)

| Interface | Purpose |
|-----------|---------|
| `AIProvider` | Main provider interface — `generate`, `supports`, `getProviderName`, `getCapabilities`, `isAvailable` |
| `ChatProvider` | Chat completion — `chat`, `supportsStreaming`, `supportsFunctionCalling` |
| `EmbeddingProvider` | Text embeddings — `embed`, `embedBatch`, `getEmbeddingDimension` |
| `GenerationProvider` | Text generation with `GenerationConfig` record (temperature, maxTokens, topP, topK, penalties) |
| `VisionProvider` | Image analysis — `analyzeImage`, `analyzeImageUrl`, `extractTextFromImage` |
| `ModerationProvider` | Content moderation — `moderate`, `isSafe`, `ModerationResult` record |
| `ProviderCapabilities` | Capability query — `supportedModels`, `supportsStreaming`, `supportsEmbedding`, etc. |
| `ProviderHealth` | Health status record — `healthy`, `degraded`, `latencyMs`, `lastChecked` |
| `ProviderConfiguration` | Config access — `getProperty`, `getAllProperties`, `isProviderEnabled`, `setProperty`, `reload` |
| `ProviderHealthService` | Health management — `checkHealth`, `checkAllProviders`, `isProviderHealthy`, `markHealthChanged` |
| `ProviderSelector` | Provider selection — `select`, `selectFallback` |
| `ProviderFailoverStrategy` | Failover — `determineFailover`, `getMaxFailoverAttempts`, `getFailoverDelayMs` |
| `ProviderValidator` | Validation — `validateProvider`, `validateModel`, `validateRequest` |

### Provider Domain Models (4 new in `provider/domain`)

| Model | Fields |
|-------|--------|
| `ProviderModel` | id, providerType, name, maxTokens, supportsStreaming, supportsEmbedding, supportsVision, supportsFunctionCalling, costPerInputToken, costPerOutputToken |
| `ProviderHealthRecord` | providerType, healthy, degraded, latencyMs, lastChecked, lastFailure, consecutiveFailures, details |
| `ProviderConfigurationRecord` | providerType, environment, enabled, priority, maxRetries, timeoutMs, properties |
| `ProviderCapabilityInfo` | providerType, supportedModels, defaultMaxTokens, streamingSupported, embeddingSupported, visionSupported, moderationSupported, functionCallingSupported |

### Application Services (7 in `provider/application`)

| Service | Responsibility |
|---------|----------------|
| `ProviderRegistryImpl` | Register/unregister providers, find by type, list all, find AIProvider instances |
| `ProviderFactoryImpl` | Create provider instances by AiProviderType or string |
| `ProviderHealthServiceImpl` | Track health status, record checks, mark health changes |
| `ProviderConfigurationService` | Manage per-provider config properties, enable/disable via feature flags |
| `ProviderSelectorImpl` | Select provider by module preference, fallback logic |
| `ProviderValidatorImpl` | Validate provider, model, and request |
| `ProviderFailoverImpl` | Failover ordering, max attempts, delay |

### Provider Adapters (8 in `provider/infrastructure`)

| Adapter | Supported Models |
|---------|-----------------|
| `GeminiAdapter` | gemini-pro, gemini-pro-vision, gemini-ultra |
| `OpenAIAdapter` | gpt-4, gpt-4-turbo, gpt-3.5-turbo, text-embedding-3 |
| `ClaudeAdapter` | claude-3-opus, claude-3-sonnet, claude-3-haiku |
| `AzureOpenAIAdapter` | gpt-4, gpt-4-turbo, gpt-3.5-turbo, text-embedding-ada-002 |
| `BedrockAdapter` | anthropic.claude-v2, anthropic.claude-instant-v1 |
| `OllamaAdapter` | llama3, llama3:70b, mistral, mixtral |
| `MistralAdapter` | mistral-large-latest, mistral-medium-latest, mistral-small-latest |
| `LocalLLMAdapter` | local-model, local-chat |

Each adapter implements both `ProviderPort` (SPI) and `AIProvider` (core API). Only `GeminiAdapter` contains stub integration methods.

### Feature Flags (8 new, 22 total)

| Flag | Default | Purpose |
|------|---------|---------|
| `AI_PROVIDER_ENABLED` | true | Master provider toggle |
| `AI_PROVIDER_GEMINI` | true | Enable Gemini |
| `AI_PROVIDER_OPENAI` | true | Enable OpenAI |
| `AI_PROVIDER_CLAUDE` | true | Enable Claude |
| `AI_PROVIDER_AZURE_OPENAI` | true | Enable Azure OpenAI |
| `AI_PROVIDER_BEDROCK` | false | Enable AWS Bedrock |
| `AI_PROVIDER_OLLAMA` | false | Enable Ollama |
| `AI_PROVIDER_MISTRAL` | false | Enable Mistral |
| `AI_PROVIDER_LOCAL_LLM` | false | Enable Local LLM |

### REST APIs

| Method | Path | Purpose |
|--------|------|---------|
| `GET` | `/api/v1/ai/providers` | List all registered providers |
| `GET` | `/api/v1/ai/providers/{id}` | Get provider details by type |
| `GET` | `/api/v1/ai/providers/capabilities` | List capabilities of all providers |
| `GET` | `/api/v1/ai/providers/health` | Health status of all providers |
| `POST` | `/api/v1/ai/providers/switch` | Switch active provider for a module |
| `POST` | `/api/v1/ai/providers/validate` | Validate a provider configuration |

### Kafka Events

Topic `ai-provider-events` (3 partitions): 6 event types — `ProviderRegistered`, `ProviderEnabled`, `ProviderDisabled`, `ProviderHealthChanged`, `ProviderSwitched`, `ProviderValidationFailed`

### Redis Caching Strategy

| Prefix | TTL | Purpose |
|--------|-----|---------|
| `prov:registry:*` | 10 min | Provider registry entries |
| `prov:capabilities:*` | 10 min | Provider capabilities |
| `prov:models:*` | 5 min | Supported model lists |
| `prov:health:*` | 1 min | Health status |
| `prov:config:*` | 10 min | Configuration properties |

### Flyway Migrations

`V12__sprint17_ai_provider_abstraction.sql` — 5 tables:

| Table | Purpose |
|-------|---------|
| `ai_provider_registry` | Provider registration, enablement, priority, endpoint, JSON config |
| `ai_provider_models` | Per-provider model list with capabilities and cost |
| `ai_provider_configuration` | KV configuration with environment scoping and secret flag |
| `ai_provider_capabilities` | Provider capability assignments |
| `ai_provider_health` | Health tracking with latency, failures, degradation |

### Tests (8 test classes, 49 tests)

| Test Class | Tests | Type |
|-----------|-------|------|
| `ProviderRegistryImplTest` | 6 | Unit |
| `ProviderFactoryImplTest` | 4 | Unit |
| `ProviderValidatorImplTest` | 8 | Unit |
| `ProviderFailoverImplTest` | 4 | Unit |
| `ProviderSelectorImplTest` | 3 | Unit |
| `ProviderHealthServiceImplTest` | 6 | Unit |
| `ProviderConfigurationServiceTest` | 6 | Unit |
| `AdapterTest` | 12 | Unit |

---

## Architecture Rules

1. **No provider leakage** — Business modules never see provider SDKs
2. **Swappable via config** — Providers switched through `application.yml` or feature flags
3. **Gateway remains single entry** — All requests still flow through `GatewayPipeline`
4. **Adapter pattern** — Each provider implements `ProviderPort` + `AIProvider` interfaces
5. **All adapters are stubs** — No real SDK calls until Part 4 approval

---

## Configuration Changes

### `application.yml`

```yaml
sporekart:
  ai:
    features:
      provider-azure-openai: true
      provider-bedrock: false
      provider-ollama: false
      provider-mistral: false
      provider-local-llm: false
```

### `SecurityConfig.java`

Permitted paths added for `/api/v1/ai/providers`, `/api/v1/ai/providers/*`, `/api/v1/ai/providers/capabilities`, `/api/v1/ai/providers/health`

### `KafkaConfig.java`

`aiProviderEventsTopic()` bean added (3 partitions, 1 replica)

---

## Risks

- All adapters are stubs — no real provider SDK calls implemented
- `Bedrock`, `Ollama`, `Mistral`, `LocalLLM` are feature-flagged off by default
- `ProviderSelectorImpl` uses `null` for `FeatureFlagService` in test — needs cleanup when real flags injected
- No secret management — API keys stored in config only (not encrypted)

---

## Technical Debt

- Provider configuration uses in-memory map — should be DB-backed
- Health checks are manual (`markHealthChanged`) — no automated polling
- Adapters return hardcoded stub responses — no real provider integration
- No circuit breaker or bulkhead patterns yet
- Provider capabilities are hardcoded — should be loaded from DB/registry

---

## Readiness for Sprint 17 Part 4

Part 4 can begin implementation of:
- Provider SDK integration (Gemini, OpenAI, Claude SDK calls)
- Real provider health checks with circuit breaker
- Secret/key management with encryption
- Provider-specific error mapping
- Provider-specific `ChatProvider`, `EmbeddingProvider` implementations
