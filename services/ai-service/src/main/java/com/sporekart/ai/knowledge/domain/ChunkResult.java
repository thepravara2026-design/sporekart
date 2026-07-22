package com.sporekart.ai.knowledge.domain;

public record ChunkResult(
    KnowledgeChunkId chunkId,
    String content,
    int sequence,
    int tokens,
    String heading,
    String section
) {}
