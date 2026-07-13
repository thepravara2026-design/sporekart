package com.sporekart.ai.assistant.interfaces.rest.dto;

import com.sporekart.ai.assistant.domain.AssistantTask;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record ChatResponse(
        UUID sessionId,
        String message,
        String intent,
        double confidence,
        List<AssistantTask> tasks,
        Map<String, Object> context,
        boolean requiresFollowUp,
        OffsetDateTime timestamp
) {
    public static ChatResponse from(com.sporekart.ai.assistant.api.AssistantResponse response) {
        return new ChatResponse(
                response.sessionId(),
                response.message(),
                response.intent(),
                response.confidence(),
                response.tasks(),
                response.context(),
                response.requiresFollowUp(),
                response.timestamp()
        );
    }
}
