package com.sporekart.ai.workflow.interfaces.rest.dto;

import com.sporekart.ai.workflow.domain.WorkflowDefinition;
import com.sporekart.ai.workflow.domain.WorkflowTriggerType;

import java.time.OffsetDateTime;
import java.util.UUID;

public record WorkflowDefinitionResponse(
        UUID id,
        String name,
        String description,
        String category,
        String status,
        String version,
        WorkflowTriggerType triggerType,
        String triggerConfig,
        UUID createdBy,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
    public static WorkflowDefinitionResponse from(WorkflowDefinition def) {
        return new WorkflowDefinitionResponse(
                def.id(), def.name(), def.description(), def.category(),
                def.status().name(), def.version(), def.triggerType(),
                def.triggerConfig(), def.createdBy(),
                def.createdAt(), def.updatedAt()
        );
    }
}
