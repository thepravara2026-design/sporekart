package com.sporekart.ai.rag.domain;

public record Chunk(
        String id,
        String documentId,
        int chunkIndex,
        String content,
        int tokenCount) {
}
