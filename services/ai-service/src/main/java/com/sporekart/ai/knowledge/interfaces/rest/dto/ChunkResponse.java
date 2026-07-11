package com.sporekart.ai.knowledge.interfaces.rest.dto;

import com.sporekart.ai.knowledge.infrastructure.persistence.KnowledgeChunkEntity;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ChunkResponse(
        UUID id,
        UUID documentId,
        int chunkIndex,
        String content,
        int tokenCount,
        int charCount,
        String chunkSizeStrategy,
        UUID parentChunkId,
        OffsetDateTime createdAt) {

    public static ChunkResponse from(KnowledgeChunkEntity entity) {
        return new ChunkResponse(
                entity.getId(),
                entity.getDocument().getId(),
                entity.getChunkIndex(),
                entity.getContent(),
                entity.getTokenCount(),
                entity.getCharCount(),
                entity.getChunkSizeStrategy(),
                entity.getParentChunk() != null ? entity.getParentChunk().getId() : null,
                entity.getCreatedAt());
    }
}
