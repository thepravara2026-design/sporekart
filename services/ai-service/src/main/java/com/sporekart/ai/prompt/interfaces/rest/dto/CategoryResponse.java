package com.sporekart.ai.prompt.interfaces.rest.dto;

import com.sporekart.ai.prompt.infrastructure.persistence.PromptCategoryEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "Prompt category response")
public record CategoryResponse(
        UUID id,
        String name,
        String description,
        String icon,
        int displayOrder,
        boolean active) {

    public static CategoryResponse from(PromptCategoryEntity entity) {
        return new CategoryResponse(entity.getId(), entity.getName(), entity.getDescription(),
                entity.getIcon(), entity.getDisplayOrder(), entity.isActive());
    }
}
