package com.sporekart.ai.knowledge.interfaces.rest.dto;

import com.sporekart.ai.knowledge.infrastructure.persistence.KnowledgeDocumentEntity;

import java.time.OffsetDateTime;
import java.util.UUID;

public record DocumentResponse(
        UUID id,
        UUID categoryId,
        String categoryName,
        String title,
        String description,
        String content,
        String language,
        String author,
        String source,
        String status,
        String visibility,
        String businessModule,
        String region,
        int currentVersion,
        boolean active,
        UUID createdBy,
        OffsetDateTime createdAt,
        UUID updatedBy,
        OffsetDateTime updatedAt) {

    public static DocumentResponse from(KnowledgeDocumentEntity entity) {
        return new DocumentResponse(
                entity.getId(),
                entity.getCategory() != null ? entity.getCategory().getId() : null,
                entity.getCategory() != null ? entity.getCategory().getName() : null,
                entity.getTitle(),
                entity.getDescription(),
                entity.getContent(),
                entity.getLanguage(),
                entity.getAuthor(),
                entity.getSource(),
                entity.getStatus(),
                entity.getVisibility(),
                entity.getBusinessModule(),
                entity.getRegion(),
                entity.getCurrentVersion(),
                entity.isActive(),
                entity.getCreatedBy(),
                entity.getCreatedAt(),
                entity.getUpdatedBy(),
                entity.getUpdatedAt());
    }
}
