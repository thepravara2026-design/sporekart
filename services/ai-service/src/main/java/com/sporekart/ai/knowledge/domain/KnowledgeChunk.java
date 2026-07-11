package com.sporekart.ai.knowledge.domain;

public record KnowledgeChunk(
        String id,
        String documentId,
        int chunkIndex,
        String content,
        int tokenCount,
        String parentChunkId) {
}
