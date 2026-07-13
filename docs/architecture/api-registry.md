# API Registry

**Version:** 1.0.0
**Last Updated:** 2026-07-12
**Owner:** Enterprise AI Platform Engineering Team
**Module:** `api-registry`

---

## Purpose

The API Registry is the auto-discovered, centralized catalog of every REST API the SporeKart Enterprise AI Platform exposes. It makes the platform's external surface introspectable: what exists, who owns it, how it is secured, what depends on it, and whether it is healthy or deprecated.

It is the runtime counterpart to the OpenAPI specification and the static API docs, kept in sync automatically.

---

## Auto-Registered REST APIs

- Every `@RestController` endpoint is registered on startup via a Spring listener that scans `interfaces/rest` packages.
- Registration captures path, HTTP method, content types, and declared `@Operation`/OpenAPI metadata.
- Manual or dynamically routed endpoints are also registered via explicit metadata where scanning is insufficient.
- Registration is idempotent and refreshes on restart.

---

## OpenAPI

- The registry links each endpoint to its OpenAPI operation id and schema.
- The OpenAPI document (`OpenApiConfig`) is the canonical schema; the registry is the discovery index.
- Contract tests verify the registry matches the published OpenAPI spec (no orphan or missing endpoints).

---

## Ownership & Module

- Each API entry records its owning **module** (e.g., `provider-registry`, `policy-engine`) and **owner** team.
- Ownership drives the runbook routing and the architecture review sign-off.

---

## Auth & Authorization

- Each endpoint records its authentication requirement (always OAuth2/JWT at the gateway) and the RBAC roles permitted (`SecurityConfig` permitted paths + method-level roles).
- The registry surfaces the effective authorization model so reviewers can confirm least-privilege.

---

## Deprecation

- Endpoints can be marked `DEPRECATED` with a `sunsetAt` date and `replacement` path.
- Deprecated endpoints remain registered (visible) but flagged; they are excluded from "active surface" reports.
- Removal follows the deprecation lifecycle and is recorded in the Event Catalog.

---

## Version

- APIs follow the URL versioning convention (`/api/v1/...`) per `docs/standards/api-versioning.md` and semantic versioning per `docs/standards/semantic-versioning.md`.
- The registry records the API version and any per-endpoint version overrides.

---

## Consumers & Dependencies

- **Consumers** — internal modules and external clients known to call the endpoint (declared via the Event Catalog / module integration docs).
- **Dependencies** — downstream services the endpoint calls (e.g., a prompt endpoint depends on the Prompt Platform, Knowledge Platform, Semantic Platform, AI Gateway, Provider Framework).
- Dependency edges feed the `system-integration.md` graph (96+ REST integration points).

---

## Health

- Each API/module exposes a health indicator (Spring Boot Actuator + custom `HealthIndicator`).
- The registry aggregates health status per module and exposes `/api/v1/registry/health` summarizing all modules.
- Unhealthy modules are flagged for the runbook/alerting pipeline.

---

## Integration Points

- **Event Catalog** — async events referenced by API docs.
- **Provider/Prompt/Knowledge/Usage/Config/Capability Registries** — their REST surfaces are auto-registered here.
- **Governance Administration** — module enable/disable reflected in registry availability.
- **API standards** — versioning, response format, error handling, correlation tracing enforced and validated.

---

## Testing

- `ApiRegistryServiceTest` — auto-registration, ownership, deprecation
- `ApiRegistryContractTest` — registry vs OpenAPI parity
- `ApiRegistryControllerTest` — discovery/health endpoints + RFC 9457 errors
- Architecture tests — every controller endpoint is registered
