# AI Capability Discovery

**Version:** 1.0.0
**Last Updated:** 2026-07-12
**Owner:** Enterprise AI Platform Engineering Team
**Module:** `capability-discovery`

---

## Purpose

AI Capability Discovery is the runtime service that lets any consumer (gateway, business module, external client) ask *"what can the platform do right now, on which provider, with what dependencies?"* and get a truthful, current answer. It decouples callers from hardcoding provider or feature assumptions.

It aggregates the capability information held in the Provider Registry and the Global Config Registry into a single, queryable discovery surface.

---

## Capability Registry

- A `CapabilityRecord` describes one capability: `capabilityKey`, `displayName`, `category`, `supported` (`SUPPORTED`/`UNSUPPORTED`/`PARTIAL`), `providerTypes`, `notes`.
- Capabilities include: streaming, tool calling, embeddings, image (vision/generation), reasoning, moderation, and reserved future: audio in/out, structured-output, json-mode.
- The registry is built from the Provider Registry capability matrix plus the Global Config feature-flag state.

---

## Discovery API

REST endpoints under `/api/v1/ai/capabilities/*`:

- `GET /api/v1/ai/capabilities` — full capability matrix across providers
- `GET /api/v1/ai/capabilities/{key}` — detail for one capability
- `GET /api/v1/ai/capabilities/providers` — which providers support which capabilities
- `GET /api/v1/ai/capabilities/resolve?need=streaming&need=tool_calling` — resolve providers satisfying a need set
- `GET /api/v1/ai/capabilities/{providerId}` — capabilities of a single provider
- `GET /api/v1/ai/capabilities/availability` — real-time availability (health-aware)

All responses carry correlation IDs and follow RFC 9457 on error.

---

## Metadata

Each capability record carries:

- `category` — `CHAT`, `EMBEDDING`, `IMAGE`, `AUDIO`, `MODERATION`, `REASONING`, `TOOLING`
- `supportedLevel` — `SUPPORTED` / `PARTIAL` / `UNSUPPORTED`
- `constraints` — e.g., max tokens, rate notes
- `sinceVersion` — platform version that introduced support
- `deprecationNotice` — if applicable

---

## Supported Features

The discovery surface exposes the full feature set the platform can negotiate:

- Streaming responses
- Tool / function calling
- Embeddings (vector generation)
- Image understanding and generation
- Moderation
- Reasoning / extended-thinking
- Reserved: Audio in/out, structured output

---

## Dependencies

- A capability may depend on other platform modules (e.g., embeddings depend on the Semantic Platform + an embedding provider; image depends on a vision-capable provider).
- The registry exposes `dependsOn` edges so callers understand the full chain before invoking.
- Dependency edges feed the `system-integration.md` graph.

---

## Availability

- Availability is **health-aware**: a capability is reported `available` only if at least one healthy, active provider supports it and the relevant feature flag is enabled.
- The AI Gateway consults availability before routing to avoid dispatching unsupported or unhealthy requests.
- Availability is cached (short TTL) and refreshed on health/priority changes.

---

## Version

- Each capability records the platform `sinceVersion` and any `providerSdkVersion` constraints.
- Capability schema changes are versioned and emitted to the Event Catalog.

---

## Provider Compatibility

- The registry maps each capability to the set of `providerType`s that satisfy it (from Provider Registry capabilities).
- Compatibility is recalculated when providers register, deprecate, or change health/version.
- The Gateway's `ProviderResolver` uses this mapping for capability-aware selection and fallback.

---

## Future Feature Flags

- Feature flags (Global Config Registry) gate capability exposure without code changes.
- Reserved capabilities (audio, structured-output) are present in the registry as `UNSUPPORTED` and become `SUPPORTED` automatically when their feature flag is enabled and a compatible provider is registered.
- This allows progressive rollout of capabilities via configuration alone — no new business functionality in this sprint.

---

## Integration Points

- **AI Gateway** — uses availability + compatibility for routing.
- **Provider Registry** — source of capability matrix and provider compatibility.
- **Global Config Registry** — source of feature-flag gating.
- **Event Catalog** — emits `CapabilityChanged`, `CapabilityAvailable`, `CapabilityUnavailable`.
- **API Registry** — discovery endpoints auto-registered.

---

## Testing

- `CapabilityDiscoveryServiceTest` — matrix build, resolve-by-need, availability
- `CapabilityProviderCompatibilityTest` — provider-to-capability mapping
- `CapabilityControllerTest` — all discovery endpoints + RFC 9457 errors
- Architecture tests — capability-discovery boundary isolation
