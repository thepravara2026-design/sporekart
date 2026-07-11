package com.sporekart.ai.core.domain;

import java.time.OffsetDateTime;
import java.util.Map;

public record AiResponse(
        String content,
        AiRole role,
        String provider,
        String model,
        OffsetDateTime timestamp,
        Map<String, Object> metadata,
        boolean success,
        String errorMessage) {
    public AiResponse(String content) {
        this(content, AiRole.ASSISTANT, null, null, OffsetDateTime.now(), Map.of(), true, null);
    }

    public AiResponse(String content, String provider, String model) {
        this(content, AiRole.ASSISTANT, provider, model, OffsetDateTime.now(), Map.of(), true, null);
    }

    public static AiResponse failure(String errorMessage) {
        return new AiResponse(null, AiRole.ASSISTANT, null, null, OffsetDateTime.now(), Map.of(), false, errorMessage);
    }
}
