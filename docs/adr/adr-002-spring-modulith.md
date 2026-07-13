# ADR-002: Spring Modulith Structured Modules

- **Status:** Accepted
- **Date:** 2026-07-12
- **Deciders:** Enterprise Architecture Review Board

## Problem

Within the chosen modular monolith, SporeKart requires an enforceable structure that keeps domain modules independent, prevents accidental coupling, and makes module boundaries explicit to both developers and tooling. Without a structural convention, teams naturally leak dependencies across modules, defeating the extraction strategy described in ADR-001 and turning the monolith into a tangled ball of mud.

## Decision

We standardize on Spring Modulith to express modules as first-class, package-based units with documented allowed dependencies, published domain events, and module-specific application and integration tests. Each module exposes a narrow public API package and an internal implementation package; everything else is encapsulated. Modules communicate through Spring Modulith's event publication registry and Kafka adapters rather than shared services. Module boundaries, dependencies, and the C4 component view are generated from the codebase to keep documentation in sync with reality.

## Alternatives Considered

- **Manual package conventions without tooling** — Pros: no new dependency. Cons: boundaries unenforced, drifts quickly, no test support.
- **OSGi / JPMS module system** — Pros: hard runtime isolation. Cons: heavyweight, poor Spring Boot integration, steep learning curve.

## Trade-offs

- Strong compile-time and test-time boundary enforcement at the cost of a learning curve for developers new to Spring Modulith.
- Generated documentation and tests improve maintainability at the cost of build-time configuration.

## Consequences

- Positive: enforced boundaries, automatic documentation, reliable module tests, clean extraction path.
- Negative: requires discipline to keep the public API minimal; follow-up is a module dependency lint check in CI.

## Compliance

Enforced by `Modulith` test harness verifying module structure, ArchUnit tests asserting allowed-dependency rules, and CI gating that fails builds on boundary violations.
