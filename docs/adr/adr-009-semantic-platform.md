# ADR-009: Semantic Intelligence and Embeddings

- **Status:** Accepted
- **Date:** 2026-07-12
- **Deciders:** Enterprise Architecture Review Board

## Problem

SporeKart needs semantic understanding for retrieval, similarity, clustering, and classification across knowledge, conversation, and content. Embedding models vary in dimensionality, quality, and cost, and embeddings must be produced consistently and cached efficiently. Hardcoding a single embedding provider inside each module would prevent model upgrades and create inconsistent vector spaces.

## Decision

We centralize semantic intelligence in the semantic module, exposed through the AI Gateway (ADR-005) and the pluggable provider framework (ADR-010). The semantic module abstracts embedding model selection, batching, normalization, and dimension handling, and integrates with the knowledge platform for vector storage and the search module for hybrid retrieval. Embeddings are cached in Redis (ADR-004) keyed by content hash and model version, and embedding jobs are coordinated via distributed locks to avoid duplication. Model changes are versioned in the provider-registry so vector spaces remain coherent.

## Alternatives Considered

- **Per-module embedding calls** — Pros: local. Cons: inconsistent spaces, duplicated logic, no caching.
- **Fixed single embedding model** — Pros: simple. Cons: no upgrade path, vendor lock-in.

## Trade-offs

- Consistent, upgradeable semantic layer at the cost of central coordination complexity.
- Cached embeddings at the cost of cache-key correctness maintenance.

## Consequences

- Positive: coherent vector space, swappable models, reused embeddings.
- Negative: semantic module is a dependency for many flows; follow-up is dimension-migration tooling.

## Compliance

Enforced by ArchUnit rules routing embeddings through the semantic module, Redis key-convention checks, and contract tests for embedding outputs.
