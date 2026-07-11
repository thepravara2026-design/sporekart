package com.sporekart.ai.knowledge.interfaces.rest.dto;

import com.sporekart.ai.knowledge.infrastructure.persistence.KnowledgeCitationEntity;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public record CitationResponse(
        UUID id,
        UUID documentId,
        String documentTitle,
        UUID retrievalRequestId,
        List<String> chunkIds,
        List<String> excerpts,
        Double relevanceScore,
        String retrievalContext) {

    public static CitationResponse from(KnowledgeCitationEntity entity) {
        return new CitationResponse(
                entity.getId(),
                entity.getDocument().getId(),
                entity.getDocument().getTitle(),
                entity.getRetrievalRequestId(),
                entity.getChunkIds() != null ? Arrays.asList(entity.getChunkIds()) : List.of(),
                entity.getExcerpts() != null ? Arrays.asList(entity.getExcerpts()) : List.of(),
                entity.getRelevanceScore(),
                entity.getRetrievalContext());
    }
}
