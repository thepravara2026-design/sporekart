# ADR-007: Prompt Platform with Versioning

- **Status:** Accepted
- **Date:** 2026-07-12
- **Deciders:** Enterprise Architecture Review Board

## Problem

Prompts are core intellectual assets in SporeKart, consumed by chat, content, assistant, and workflow modules. They change frequently, must be reproducible, require approval before production use, and must support A/B experimentation. Storing prompts as inline strings or unversioned database rows prevents rollback, auditing, and governance, and risks silent behavior changes in production AI behavior.

## Decision

We build a dedicated Prompt Platform around the prompt and prompt-registry modules. Every prompt is a versioned, immutable artifact with metadata (author, tags, intended model, governance status). New versions require a governance approval (ADR-006) before being marked active. The prompt-registry is the system of record and publishes version-change events to Kafka so dependent modules and the event-catalog stay informed. Prompts support templating, variable schemas, and safe compilation cached in Redis (ADR-004). The platform exposes CRUD, version history, diff, and rollback via the api-registry.

## Alternatives Considered

- **Plain text in code/config** — Pros: simple. Cons: no versioning, no governance, no rollback.
- **Generic document store** — Pros: flexible. Cons: lacks prompt-specific semantics (variables, model targeting, approval).

## Trade-offs

- Full lifecycle control and safety at the cost of building and maintaining a versioning system.
- Reproducibility at the cost of storage growth from immutable versions (mitigated by retention policy).

## Consequences

- Positive: safe prompt evolution, auditability, experimentation support.
- Negative: extra latency for version resolution; follow-up is a prompt-performance analytics integration.

## Compliance

Enforced by prompt-registry schema migrations (Flyway), governance approval tests, and API contract tests in the api-registry.
