# ADR-004: Redis Caching and Coordination

- **Status:** Accepted
- **Date:** 2026-07-12
- **Deciders:** Enterprise Architecture Review Board

## Problem

SporeKart performs expensive operations that are repeatedly requested: AI provider responses, embedding lookups, prompt compilations, capability discovery, and session state. Without a shared, low-latency cache the platform would re-execute costly model calls and re-query PostgreSQL unnecessarily, increasing cost and latency. The platform also needs distributed coordination primitives (locks, rate-limit counters, pub/sub) that a single JVM cannot provide once modules are extracted.

## Decision

We adopt Redis as the platform-wide cache and coordination layer. Redis backs the provider response cache, semantic embedding caches, prompt compilation results, capability-discovery results, and distributed locks for safe concurrent operations such as knowledge reindexing and usage-tracking aggregation. It also provides pub/sub signaling for cache invalidation across module instances. Caching is applied with explicit TTLs and namespace keys per module, and a cache-aside pattern is used so PostgreSQL remains the source of truth.

## Alternatives Considered

- **In-memory Caffeine cache** — Pros: zero infrastructure, very fast. Cons: not shared across instances, defeats extraction, no distributed coordination.
- **PostgreSQL materialized views** — Pros: consistent with DB. Cons: high latency for hot keys, no distributed locks.

## Trade-offs

- Shared, fast caching and coordination at the cost of an extra infrastructure dependency and cache-invalidation complexity.
- Reduced AI cost and latency at the cost of potential stale reads if invalidation fails.

## Consequences

- Positive: lower latency and AI spend, distributed coordination ready for extraction.
- Negative: cache coherence must be monitored; follow-up is a cache-invalidation test suite.

## Compliance

Enforced through cache abstraction configuration reviews, Redis key-naming conventions in the config-registry, and integration tests verifying invalidation behavior.
