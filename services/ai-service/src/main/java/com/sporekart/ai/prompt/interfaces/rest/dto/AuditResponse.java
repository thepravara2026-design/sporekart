package com.sporekart.ai.prompt.interfaces.rest.dto;

import com.sporekart.ai.prompt.infrastructure.persistence.PromptAuditEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.OffsetDateTime;
import java.util.UUID;

@Schema(description = "Prompt audit entry response")
public record AuditResponse(
        UUID id,
        UUID templateId,
        UUID versionId,
        String action,
        String entityType,
        UUID entityId,
        String previousValue,
        String newValue,
        UUID changedBy,
        OffsetDateTime changedAt,
        String details) {

    public static AuditResponse from(PromptAuditEntity entity) {
        return new AuditResponse(entity.getId(), entity.getTemplateId(), entity.getVersionId(),
                entity.getAction(), entity.getEntityType(), entity.getEntityId(),
                entity.getPreviousValue(), entity.getNewValue(),
                entity.getChangedBy(), entity.getChangedAt(), entity.getDetails());
    }
}
