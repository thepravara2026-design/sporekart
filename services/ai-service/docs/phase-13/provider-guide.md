# Provider Configuration Guide

## Supported Providers

| Provider | Config Class | Default Model | Status |
|----------|-------------|---------------|--------|
| OpenAI | `OpenAiConfig` | gpt-4o | Configured |
| Google Gemini | `GeminiConfig` | gemini-2.0-flash | Configured |
| Anthropic Claude | `ClaudeConfig` | claude-sonnet-4 | Configured |
| Azure OpenAI | `AzureOpenAiConfig` | gpt-4o | Configured |
| AWS Bedrock | `BedrockConfig` | claude-3.5-sonnet | Configured |
| Ollama | `OllamaConfig` | llama3 | Configured |
| OpenRouter | `OpenRouterConfig` | openrouter/auto | Configured |

## Provider Configuration Structure

```yaml
sporekart:
  ai:
    provider:
      default-provider: openai
      failover-enabled: true
      provider-order: openai, gemini, claude, azure, bedrock, ollama, openrouter
      providers:
        GEMINI:
          enabled: true
          endpoint: https://generativelanguage.googleapis.com
          model: gemini-pro
          max-tokens: 2048
          temperature: 0.7
```

## Provider Failover

When a provider fails, the system automatically fails over to the next provider in the `provider-order` list. Failover is enabled by default and can be disabled with `sporekart.ai.provider.failover-enabled: false`.

## Adding a New Provider

1. Create config record in `configuration/model/provider/`
2. Add to `ProviderConfigurationProperties.java`
3. Add YAML configuration under `sporekart.ai.provider.providers`
4. Add to provider order list
5. Add to provider matrix in documentation
