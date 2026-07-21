# Memory Platform

## Overview

The Memory Platform provides persistent, retrievable memory for AI agents and conversations. It supports multiple memory types with configurable consolidation policies and vector-based semantic retrieval.

## Capabilities

- **Short-term memory** — Session-scoped, ephemeral
- **Long-term memory** — Persistent across sessions
- **Episodic memory** — Event/experience-based
- **Semantic memory** — Fact/knowledge-based
- **Procedural memory** — How-to/process-based
- **Contextual memory** — Context-aware retrieval
- **Vector indexing** — Semantic similarity search
- **Consolidation** — TTL-based pruning and archiving
- **Importance scoring** — Prioritize critical memories

## Architecture

```mermaid
graph TB
    subgraph Memory["Memory Platform"]
        API["MemoryService API"]
        STORE["MemoryStore"]
        RETRIEVAL["MemoryRetrievalService"]
        INDEX["MemoryIndexService"]
        CONSOLIDATION["MemoryConsolidationService"]
        
        API --> STORE
        API --> RETRIEVAL
        API --> INDEX
        API --> CONSOLIDATION
    end

    subgraph Storage["Storage"]
        DB[("PostgreSQL<br/>memory_entries")]
        CACHE[("Redis Cache")]
        VECTOR[("Vector Store")]
    end

    subgraph Messaging["Messaging"]
        KAFKA["Kafka<br/>ai-memory-events"]
    end

    subgraph Monitor["Monitoring"]
        METRICS["Micrometer Metrics"]
    end

    STORE --> DB
    STORE --> CACHE
    INDEX --> VECTOR
    CONSOLIDATION --> KAFKA
    STORE --> METRICS
    RETRIEVAL --> CACHE
```

## API Endpoints

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/v1/ai/memory` | Store a memory entry |
| GET | `/api/v1/ai/memory/{id}` | Retrieve a memory entry |
| POST | `/api/v1/ai/memory/search` | Search memories |
| PUT | `/api/v1/ai/memory/{id}` | Update a memory entry |
| DELETE | `/api/v1/ai/memory/{id}` | Delete a memory entry |
| DELETE | `/api/v1/ai/memory/session/{sessionId}` | Delete by session |
| DELETE | `/api/v1/ai/memory/agent/{agentId}` | Delete by agent |
| GET | `/api/v1/ai/memory/stats` | Get memory statistics |
| GET | `/api/v1/ai/memory/summaries` | Get memory summaries |
| POST | `/api/v1/ai/memory/consolidate` | Trigger consolidation |
| POST | `/api/v1/ai/memory/prune` | Trigger pruning |

## Domain Model

```mermaid
classDiagram
    class MemoryEntry {
        UUID id
        String agentId
        String sessionId
        String userId
        String content
        MemoryType type
        MemoryImportance importance
        Map~String,String~ metadata
        Instant createdAt
        Instant lastAccessedAt
        Instant expiresAt
        int accessCount
        float[] embedding
    }

    class MemoryQuery {
        String agentId
        String sessionId
        String userId
        MemoryType type
        MemoryImportance minImportance
        Instant from
        Instant to
        String keywords
        int maxResults
        List~MemorySortOrder~ sortOrders
    }

    class MemoryType {
        <<enumeration>>
        SHORT_TERM
        LONG_TERM
        EPISODIC
        SEMANTIC
        PROCEDURAL
        WORKING
        CONTEXTUAL
    }

    class MemoryImportance {
        <<enumeration>>
        TRIVIAL
        LOW
        NORMAL
        HIGH
        CRITICAL
    }
```
