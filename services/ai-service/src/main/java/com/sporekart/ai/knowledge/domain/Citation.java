package com.sporekart.ai.knowledge.domain;

import java.time.Instant;

public record Citation(
    KnowledgeDocumentId documentId,
    KnowledgeChunkId chunkId,
    String source,
    String page,
    String section,
    String version,
    String workspaceId,
    double confidence,
    double relevanceScore,
    String excerpt,
    Instant retrievedAt
) {}
