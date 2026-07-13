package com.sporekart.ai.assistant.interfaces.rest.dto;

import com.sporekart.ai.assistant.domain.AssistantTask;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public record TaskResponse(
        UUID id,
        String name,
        String status,
        Map<String, Object> input,
        Map<String, Object> output,
        OffsetDateTime createdAt
) {
    public static TaskResponse from(AssistantTask task) {
        return new TaskResponse(
                task.id(),
                task.name(),
                task.status().name(),
                task.input(),
                task.output(),
                task.createdAt()
        );
    }
}
