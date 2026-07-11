package com.sporekart.ai.knowledge.interfaces.rest.dto;

import com.sporekart.ai.knowledge.infrastructure.persistence.KnowledgeCategoryEntity;

import java.util.UUID;

public record CategoryResponse(
        UUID id,
        String name,
        String description,
        int displayOrder,
        boolean active) {

    public static CategoryResponse from(KnowledgeCategoryEntity entity) {
        return new CategoryResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getDisplayOrder(),
                entity.isActive());
    }
}
