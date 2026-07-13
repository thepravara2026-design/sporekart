# Enterprise AI Provider Registry

**Version:** 1.0.0
**Last Updated:** 2026-07-12
**Owner:** Enterprise AI Platform Engineering Team
**Module:** `provider-registry`

---

## Purpose

The Provider Registry is the authoritative, runtime-queryable catalog of every AI provider integrated with the SporeKart Enterprise AI Platform. It answers: which providers exist, what they can do, how healthy they are, which one should be used first, what to do when one fails, and how to retire a provider safely.

It sits above the Provider Abstraction Layer (Part 3) and below the AI Gateway. Business modules and the Gateway consult the registry to resolve the optimal provider for a request without hardcoding provider knowledge.

---

## Provider Metadata

Each provider entry carries:

- `providerId` — stable UUID
- `providerName` — human-readable (e.g., `Gemini`, `OpenAI`)
- `providerType` — enum (`GEMINI`, `OPENAI`, `CLAUDE`, `AZURE_OPENAI`, `BEDROCK`, `OLLAMA`, `MISTRAL`, `LOCAL_LLM`, `MOCK`)
- `description` — operational notes
- `contact` / `owner` — responsible team
- `endpoint` — base URL (masked in responses)
- `createdAt` / `updatedAt` — audit timestamps
- `tags` — operational classification

---

## Provider Priority & Status

- **Priority** — integer ordering used by the selection algorithm (lower number = higher preference). The Gateway's `ProviderResolver` walks the priority-ordered list respecting module preference and feature flags.
- **Status** — enum: `ACTIVE`, `INACTIVE`, `MAINTENANCE`, `DEPRECATED`, `DECOMMISSIONED`. Only `ACTIVE` providers are eligible for selection. `MAINTENANCE` providers are excluded from new requests but retained for in-flight traffic.

---

## Supported Models

Each provider declares a list of models:

- `modelId` — provider-specific identifier (e.g., `gpt-4o`, `claude-3-5-sonnet`)
- `displayName`
- `family` — enum (`CHAT`, `EMBEDDING`, `IMAGE`, `MODERATION`, `REASONING`)
- `contextWindowTokens`
- `maxOutputTokens`
- `costPerInputToken` / `costPerOutputToken` — informational only (see Usage Tracking); never used for billing
- `supportedFeatures` — link to Capability Discovery

---

## Capabilities

Capabilities describe what a provider/model can do. They are surfaced both here and via Capability Discovery.

- **Context windows** — max input tokens per request
- **Token limits** — max output tokens
- **Streaming** — `SUPPORTED` / `UNSUPPORTED`
- **Tool calling** — `SUPPORTED` / `UNSUPPORTED` (function/tool invocation)
- **Embeddings** — `SUPPORTED` / `UNSUPPORTED` (vector generation)
- **Image** — `SUPPORTED` / `UNSUPPORTED` (vision / image generation)
- **Future: Audio** — reserved capability flag (`AUDIO_IN`, `AUDIO_OUT`) for speech-to-text / text-to-speech, default `UNSUPPORTED`

The registry stores a `ProviderCapabilityInfo` record mapping each capability to a support level, enabling the Gateway to reject requests a provider cannot satisfy before invocation.

---

## Provider Health

- `ProviderHealthRecord` tracks `status` (`HEALTHY`, `DEGRADED`, `UNHEALTHY`, `UNKNOWN`), `lastCheckedAt`, `latencyMs`, `errorRate`, `message`.
- Health is evaluated by `ProviderHealthServiceImpl` (periodic probe) and on-demand via the discovery API.
- Unhealthy providers are temporarily removed from the eligible set by the selection algorithm (circuit-breaker style), independent of their declared `Status`.

---

## Provider Version

- `version` — semantic version of the adapter/integration contract.
- `sdkVersion` — external SDK version the adapter targets.
- Version changes are recorded in the provider's history and emitted as a Kafka event (`ProviderVersionChanged`) for the Event Catalog.

---

## Fallback Chain

- The registry defines an ordered **fallback chain** per module/use-case: `primaryProvider → secondaryProvider → tertiaryProvider`.
- On provider failure (exception, timeout, unhealthy), `ProviderFailoverImpl` advances to the next eligible provider.
- Fallback respects priority, feature flags, and capability requirements (a provider lacking the required capability is skipped).
- The chain is declarative in configuration and overridable per request context.

---

## Deprecation

- A provider is moved to `DEPRECATED` before `DECOMMISSIONED`.
- Deprecation records a `deprecatedAt` date, `sunsetAt` date, and `replacementProviderId`.
- While deprecated, the provider is excluded from new selection but its metadata remains queryable for audit and migration planning.
- Consumers are warned via the discovery API (`deprecationNotice`).

---

## Lifecycle

```
REGISTERED → ACTIVE → MAINTENANCE → ACTIVE
                  │
                  ├─→ DEPRECATED → DECOMMISSIONED
                  └─→ INACTIVE → ACTIVE
```

Transitions are validated, audit-logged, and versioned. Only `ACTIVE` providers participate in selection.

---

## Discovery / Metadata APIs

REST endpoints under `/api/v1/ai/providers/*` (existing Part 3 surface extended by the registry):

- `GET /api/v1/ai/providers` — list all providers with status, priority, health
- `GET /api/v1/ai/providers/{id}` — full metadata, models, capabilities
- `GET /api/v1/ai/providers/{id}/health` — current health record
- `GET /api/v1/ai/providers/{id}/models` — supported models
- `GET /api/v1/ai/providers/capabilities` — capability matrix across providers
- `GET /api/v1/ai/providers/select?module=&feature=` — resolve optimal provider (priority + fallback)
- `POST /api/v1/ai/providers/{id}/health/refresh` — trigger health probe
- `GET /api/v1/ai/providers/{id}/fallback` — view fallback chain

All responses are RFC 9457-compliant on error and carry correlation IDs.

---

## Integration Points

- **AI Gateway** — uses priority, capabilities, health, and fallback chain during `ProviderResolver`.
- **Capability Discovery** — consumes the capability matrix.
- **Event Catalog** — subscribes to `ProviderRegistered`, `ProviderDeprecated`, `ProviderVersionChanged`, `ProviderHealthChanged`.
- **Usage Tracking** — tags every usage record with the resolved `providerId` and `modelId`.
- **API Registry** — the discovery endpoints are auto-registered.

---

## Testing

- `ProviderRegistryImplTest` — registration, priority ordering, status filtering
- `ProviderFailoverImplTest` — fallback chain advancement
- `ProviderHealthServiceImplTest` — health evaluation and exclusion
- `ProviderSelectorImplTest` — capability-aware selection
- `ProviderControllerTest` — all endpoints + RFC 9457 errors
- Architecture tests — registry stays within `provider-registry` boundaries
