# AI Provider Abstraction

**Version:** 1.0.0
**Last Updated:** 2026-07-11
**Status:** Implemented (Sprint 17 Part 3)

---

## Supported Providers

| Provider | Type | Status | Feature Flag |
|----------|------|--------|-------------|
| Gemini | Cloud (Google) | Stub adapter | `AI_PROVIDER_GEMINI` |
| OpenAI | Cloud (OpenAI) | Stub adapter | `AI_PROVIDER_OPENAI` |
| Claude | Cloud (Anthropic) | Stub adapter | `AI_PROVIDER_CLAUDE` |
| Azure OpenAI | Cloud (Microsoft) | Stub adapter | `AI_PROVIDER_AZURE_OPENAI` |
| AWS Bedrock | Cloud (Amazon) | Stub adapter (off by default) | `AI_PROVIDER_BEDROCK` |
| Ollama | Local | Stub adapter (off by default) | `AI_PROVIDER_OLLAMA` |
| Mistral | Cloud (Mistral) | Stub adapter (off by default) | `AI_PROVIDER_MISTRAL` |
| Local LLM | Local | Stub adapter (off by default) | `AI_PROVIDER_LOCAL_LLM` |

All adapters return stub responses. No real SDK calls are made.

---

## Provider Interface Hierarchy

```
AIProvider (core API)
  ├── generate(AiRequest) → AiResponse
  ├── supports(model) → boolean
  ├── getProviderName() → String
  ├── getCapabilities() → ProviderCapabilities
  └── isAvailable() → boolean

ChatProvider (core API)
  ├── chat(messages, model) → String
  ├── supportsStreaming() → boolean
  └── supportsFunctionCalling() → boolean

EmbeddingProvider (core API)
  ├── embed(text, model) → List<Float>
  ├── embedBatch(texts, model) → List<List<Float>>
  └── getEmbeddingDimension(model) → int

GenerationProvider (core API)
  ├── generate(prompt, model) → String
  └── generateWithConfig(prompt, model, GenerationConfig) → String

VisionProvider (core API)
  ├── analyzeImage(imageBytes, prompt, model) → String
  ├── analyzeImageUrl(imageUrl, prompt, model) → String
  └── extractTextFromImage(imageBytes, model) → List<String>

ModerationProvider (core API)
  ├── moderate(text, model) → ModerationResult
  └── isSafe(text, model) → boolean
```

---

## Provider Capabilities

Each provider exposes its capabilities via the `ProviderCapabilities` interface:

```java
interface ProviderCapabilities {
    String getProviderName();
    Set<String> supportedModels();
    int getMaxTokens(String model);
    boolean supportsStreaming(String model);
    boolean supportsEmbedding(String model);
    boolean supportsVision(String model);
    boolean supportsModeration(String model);
    boolean supportsFunctionCalling(String model);
    List<String> getCapabilities();
}
```

### Capability Matrix

| Capability | Gemini | OpenAI | Claude | Azure | Bedrock | Ollama | Mistral | Local |
|-----------|--------|--------|--------|-------|---------|--------|---------|-------|
| Text Generation | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
| Chat Completion | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
| Streaming | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✗ |
| Embeddings | ✓ | ✓ | ✗ | ✓ | ✗ | ✗ | ✓ | ✗ |
| Vision | ✓ | ✓ | ✓ | ✗ | ✗ | ✓ | ✗ | ✗ |
| Moderation | ✓ | ✓ | ✗ | ✗ | ✗ | ✗ | ✓ | ✗ |
| Function Calling | ✓ | ✓ | ✓ | ✓ | ✗ | ✗ | ✓ | ✗ |
| Code Generation | ✗ | ✓ | ✓ | ✗ | ✓ | ✗ | ✓ | ✗ |

---

## Configuration

Provider configuration is managed through `ProviderConfigurationService`:

```yaml
sporekart:
  ai:
    provider:
      providers:
        GEMINI:
          enabled: true
          endpoint: https://generativelanguage.googleapis.com
          model: gemini-pro
          max-tokens: 2048
          temperature: 0.7
```

Configuration can be reloaded at runtime via `reload()`.

---

## Health Monitoring

Provider health is tracked in `ProviderHealthServiceImpl`:

- Health status: healthy, degraded, unhealthy
- Latency tracking
- Consecutive failure counting
- Last failure timestamp
- Manual health marking via `markHealthChanged()`
- Health cached in Redis (1 min TTL)

---

## Security

- No API keys stored in source code
- `is_secret` column in `ai_provider_configuration` for credential marking
- Feature flags gate provider availability
- Provider validation before any operation
- Kafka audit events on provider changes
