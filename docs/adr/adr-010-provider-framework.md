# ADR-010: Pluggable Provider Framework

- **Status:** Accepted
- **Date:** 2026-07-12
- **Deciders:** Enterprise Architecture Review Board

## Problem

SporeKart must integrate with an evolving set of AI providers (LLM, embedding, reranking, speech, image) without rewriting business logic each time a provider is added, changed, or deprecated. Hard dependencies on any single vendor create lock-in and block cost or quality optimization. The platform also needs a single place to register, configure, and discover provider capabilities.

## Decision

We build a provider framework in the provider and provider-registry modules that defines stable interfaces for each capability type, with adapter implementations per vendor. Providers self-register their capabilities, models, limits, and pricing into the provider-registry, which the AI Gateway (ADR-005) and capability-discovery consult for routing and fallback. Adding a provider means adding an adapter and a registry entry, with no changes to consuming modules. Provider health, quotas, and cost are tracked and surfaced via usage-tracking and monitoring.

## Alternatives Considered

- **Direct SDK integration per use case** — Pros: fastest initially. Cons: lock-in, duplicated config, no abstraction.
- **Generic HTTP facade** — Pros: simple. Cons: cannot express provider-specific features (streaming, function calling).

## Trade-offs

- Vendor independence and extensibility at the cost of building and maintaining adapters.
- Clean routing at the cost of an abstraction that must handle provider-specific semantics.

## Consequences

- Positive: easy provider onboarding, A/B model testing, no lock-in.
- Negative: adapter bugs affect many flows; follow-up is a provider-certification test suite.

## Compliance

Enforced by the provider-registry rejecting unregistered providers, adapter contract tests, and ArchUnit rules limiting provider SDK usage to the provider module.
