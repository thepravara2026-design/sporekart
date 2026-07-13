# ADR-005: AI Gateway Central Abstraction

- **Status:** Accepted
- **Date:** 2026-07-12
- **Deciders:** Enterprise Architecture Review Board

## Problem

SporeKart integrates with multiple AI providers (LLM, embedding, reranking, speech) whose APIs, auth models, rate limits, and capabilities differ widely. Calling provider APIs directly from business modules would scatter provider logic, make it impossible to swap or add providers, and prevent centralized concerns such as cost control, fallback, and observability from being applied uniformly.

## Decision

We establish the gateway module as the single entry point for all outbound AI communication, sitting atop the pluggable provider framework (ADR-010). The AI Gateway abstracts provider differences behind stable internal interfaces, centralizes request/response normalization, retries with backoff, circuit breaking, token accounting, and routing/fallback between providers. Business modules (chat, content, rag, semantic, assistant) never call providers directly; they call the gateway, which resolves the appropriate provider via the provider-registry. The gateway emits events for usage-tracking and exposes its capabilities through capability-discovery.

## Alternatives Considered

- **Direct provider calls per module** — Pros: fastest to prototype. Cons: duplicated logic, no central control, vendor lock-in.
- **External API gateway (e.g., cloud LLM proxy)** — Pros: offloads concerns. Cons: less control, harder to integrate with internal registries and cost tracking.

## Trade-offs

- Centralized control and swap-ability at the cost of a single abstraction layer to maintain.
- Consistent cross-cutting behavior at the cost of gateway as a potential bottleneck (mitigated by stateless horizontal scaling).

## Consequences

- Positive: provider independence, uniform cost/observability, easy addition of new models.
- Negative: gateway must evolve carefully; follow-up is provider-adapter contract tests.

## Compliance

Enforced by ArchUnit rules forbidding provider SDK imports outside gateway/provider modules, and by the provider-registry validating every gateway route.
