# Feature Flag Guide

## Overview

Feature flags control which AI capabilities are enabled at runtime. All flags are configured in `application.yml` under `sporekart.ai.features.*`.

## Flag Categories

### Platform Flags
| Flag | Description |
|------|-------------|
| ai-enabled | Master switch for all AI features |
| gateway-enabled | AI Gateway enabled |
| prompt-registry-enabled | Prompt management enabled |
| conversation-enabled | Conversation platform enabled |
| knowledge-enabled | Knowledge platform enabled |
| semantic-search-enabled | Semantic search enabled |
| embeddings-enabled | Vector embeddings enabled |
| copilots-enabled | Business copilots enabled |
| analytics-enabled | AI analytics enabled |
| monitoring-enabled | AI monitoring enabled |

### Provider Flags
| Flag | Description |
|------|-------------|
| provider-failover-enabled | Automatic provider failover |
| provider-gemini | Google Gemini enabled |
| provider-openai | OpenAI enabled |
| provider-claude | Anthropic Claude enabled |
| provider-azure-openai | Azure OpenAI enabled |
| provider-bedrock | AWS Bedrock enabled |
| provider-ollama | Ollama enabled |

### Capability Flags
| Flag | Description |
|------|-------------|
| rate-limiter-enabled | Rate limiting enabled |
| streaming-enabled | Streaming responses enabled |
| image-generation-enabled | Image generation enabled |
| audio-enabled | Audio processing enabled |
| vision-enabled | Vision/image analysis enabled |
| reasoning-models-enabled | Advanced reasoning models enabled |

## Adding a New Flag

1. Add to `FeatureFlagConfigurationProperties.java`
2. Add YAML entry under `sporekart.ai.features.*`
3. Add to `FeatureFlagRegistry` in documentation
4. Add to `configuration-platform.md` feature flag table

## Runtime Mutable Flags

These flags support runtime toggle without restart:
- provider-failover-enabled
- rate-limiter-enabled
- streaming-enabled

All other flags require application restart to take effect.
