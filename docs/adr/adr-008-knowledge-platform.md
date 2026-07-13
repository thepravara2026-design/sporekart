# ADR-008: Knowledge Platform and RAG

- **Status:** Accepted
- **Date:** 2026-07-12
- **Deciders:** Enterprise Architecture Review Board

## Problem

SporeKart must ground AI responses in enterprise knowledge to reduce hallucination and provide cited, trustworthy answers. Knowledge arrives from many sources, requires ingestion, chunking, embedding, storage, retrieval, and governance before it can be used for retrieval-augmented generation (RAG). Without a dedicated platform, RAG logic would be duplicated and knowledge quality would be unmanaged.

## Decision

We build a Knowledge Platform spanning the knowledge, knowledge-registry, rag, and semantic modules. Ingestion is a governed pipeline (ADR-006) that validates, chunks, and embeds documents, storing chunks and vectors with provenance. The knowledge-registry tracks datasets, sources, and freshness. The rag module performs hybrid retrieval (keyword via search plus vector via semantic) and reranking through the AI Gateway (ADR-005), returning cited context to chat and assistant. Embeddings are cached in Redis and refreshed on knowledge-update events from Kafka.

## Alternatives Considered

- **Vendor RAG-as-a-service** — Pros: fast start. Cons: data residency concerns, less control, harder governance.
- **Pure vector search only** — Pros: simple. Cons: weaker recall than hybrid, no keyword fallback.

## Trade-offs

- High answer quality and citations at the cost of pipeline complexity and embedding compute.
- Hybrid retrieval at the cost of maintaining two indexes (keyword + vector).

## Consequences

- Positive: grounded, cited, governed answers; reusable knowledge across modules.
- Negative: ingestion latency and cost; follow-up is automated freshness and quality monitoring.

## Compliance

Enforced by knowledge-registry schema tests, governed-ingestion integration tests, and citation-requirement checks in the rag module.
