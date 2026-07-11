package com.sporekart.ai.prompt.interfaces.rest.dto;

import com.sporekart.ai.prompt.infrastructure.persistence.PromptCategoryEntity;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptTemplateEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.OffsetDateTime;
import java.util.UUID;

@Schema(description = "Prompt template response")
public record PromptResponse(
        UUID id,
        UUID categoryId,
        String categoryName,
        String name,
        String description,
        String templateText,
        String status,
        int currentVersion,
        String[] tags,
        UUID createdBy,
        OffsetDateTime createdAt,
        UUID updatedBy,
        OffsetDateTime updatedAt) {

    public static PromptResponse from(PromptTemplateEntity entity) {
        PromptCategoryEntity cat = entity.getCategory();
        return new PromptResponse(
                entity.getId(), cat.getId(), cat.getName(),
                entity.getName(), entity.getDescription(), entity.getTemplateText(),
                entity.getStatus(), entity.getCurrentVersion(), entity.getTags(),
                entity.getCreatedBy(), entity.getCreatedAt(),
                entity.getUpdatedBy(), entity.getUpdatedAt());
    }
}
