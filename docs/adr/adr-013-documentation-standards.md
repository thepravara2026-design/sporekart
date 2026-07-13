# ADR-013: Documentation Driven Development

- **Status:** Accepted
- **Date:** 2026-07-12
- **Deciders:** Enterprise Architecture Review Board

## Problem

SporeKart is a large, multi-module platform with many stakeholders (backend, frontend, mobile, governance, operations). Specifications, APIs, and architectural decisions documented after the fact become stale, causing integration mismatches, duplicated effort, and onboarding friction. The platform needs documentation to be a first-class, versioned artifact that evolves with the code.

## Decision

We adopt Documentation Driven Development (DDD-ish "docs first") as a standard: significant changes start with an ADR or spec in `docs/`, and API contracts are defined before implementation and registered in the api-registry. Architecture, module boundaries, and event catalogs are generated or kept in sync with the codebase. The frontend (React/Vite/TypeScript) and future Android/iOS clients consume OpenAPI and ADR docs as the contract source of truth. Documentation quality is part of the definition of done and reviewed in pull requests.

## Alternatives Considered

- **Code-first, docs later** — Pros: fast start. Cons: stale docs, integration errors, poor onboarding.
- **Separate wiki** — Pros: easy editing. Cons: diverges from code, not version-controlled with releases.

## Trade-offs

- Aligned teams and fewer integration bugs at the cost of upfront writing effort.
- Versioned docs at the cost of maintenance discipline.

## Consequences

- Positive: shared understanding, smoother frontend/mobile integration, durable knowledge.
- Negative: docs can lag if not enforced; follow-up is doc-freshness CI checks.

## Compliance

Enforced by PR templates requiring ADR/spec links, api-registry contract presence, and doc-lint checks in CI.
