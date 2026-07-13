# ADR-014: API Standards — REST, RFC 9457, OpenAPI

- **Status:** Accepted
- **Date:** 2026-07-12
- **Deciders:** Enterprise Architecture Review Board

## Problem

SporeKart exposes APIs to the React/Vite/TypeScript frontend and must remain compatible with future Android and iOS native clients. Inconsistent endpoint design, error shapes, versioning, and missing machine-readable contracts cause client rework and brittle integrations. The platform needs a single, enforced API contract standard.

## Decision

We standardize on REST with resource-oriented URLs, JSON payloads, and semantic HTTP status codes. All error responses follow RFC 9457 (Problem Details for HTTP APIs) with a consistent `type`, `title`, `status`, `detail`, and extension members for error codes and correlation IDs. Every module publishes an OpenAPI 3 document registered in the api-registry, generated from code to prevent drift. APIs are versioned via URI path or header, and backward compatibility for mobile clients is a release gate. The capability-discovery registry advertises available endpoints and features.

## Alternatives Considered

- **GraphQL** — Pros: flexible queries. Cons: harder caching, governance, and mobile versioning discipline.
- **Custom error envelopes** — Pros: familiar. Cons: non-standard, breaks tooling and RFC compliance.

## Trade-offs

- Interoperable, tool-friendly APIs at the cost of REST constraints and OpenAPI upkeep.
- RFC 9457 consistency at the cost of migrating legacy error shapes.

## Consequences

- Positive: stable contracts, auto-generated client SDKs, mobile compatibility guarantee.
- Negative: must maintain OpenAPI accuracy; follow-up is client-codegen in CI.

## Compliance

Enforced by OpenAPI validation in CI, RFC 9457 response tests, and api-registry registration requirements for new endpoints.
