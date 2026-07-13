package com.sporekart.ai.assistant.interfaces.rest.dto;

import com.sporekart.ai.assistant.domain.Assistant;
import java.util.UUID;

public record AssistantResponse(
        UUID id,
        String name,
        String type,
        String status,
        String description,
        boolean active
) {
    public static AssistantResponse from(Assistant assistant) {
        return new AssistantResponse(
                assistant.id(),
                assistant.name(),
                assistant.type().name(),
                assistant.status().name(),
                assistant.description(),
                assistant.isActive()
        );
    }
}
