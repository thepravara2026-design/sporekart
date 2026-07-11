package com.sporekart.ai.prompt.interfaces.rest.dto;

import com.sporekart.ai.prompt.infrastructure.persistence.PromptVersionEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.OffsetDateTime;
import java.util.UUID;

@Schema(description = "Prompt version response")
public record VersionResponse(
        UUID id,
        UUID templateId,
        int versionNumber,
        String templateText,
        String status,
        String changeNotes,
        OffsetDateTime activationDate,
        UUID createdBy,
        OffsetDateTime createdAt,
        UUID approvedBy,
        OffsetDateTime approvedAt) {

    public static VersionResponse from(PromptVersionEntity entity) {
        return new VersionResponse(
                entity.getId(), entity.getTemplate().getId(),
                entity.getVersionNumber(), entity.getTemplateText(),
                entity.getStatus(), entity.getChangeNotes(),
                entity.getActivationDate(), entity.getCreatedBy(), entity.getCreatedAt(),
                entity.getApprovedBy(), entity.getApprovedAt());
    }
}
