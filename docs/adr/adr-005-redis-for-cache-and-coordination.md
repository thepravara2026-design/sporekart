# ADR-005: Use Redis for cache and short-lived coordination state

- Status: Accepted
- Date: 2026-07-10
- Owners: Enterprise Architecture Review Board

## Context
The platform requires fast access to transient state and short-lived coordination primitives.

## Decision
Use Redis for caching, session-related state, and lightweight coordination where required.

## Alternatives considered
- Database-backed caching only
- In-memory-only local caches
- No cache layer for transient state

## Trade-offs
Redis improves performance and coordination, but introduces a distributed dependency that requires explicit invalidation and failure handling.

## Consequences
The platform gains improved read performance and better support for high-throughput workflows.

## Approval
Reviewed and accepted by the ARB.
