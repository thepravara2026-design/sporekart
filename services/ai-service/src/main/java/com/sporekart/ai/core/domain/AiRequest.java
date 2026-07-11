package com.sporekart.ai.core.domain;

import java.util.Map;

public record AiRequest(
        String prompt,
        AiRole role,
        Map<String, Object> parameters,
        String conversationId,
        String module) {
    public AiRequest(String prompt) {
        this(prompt, AiRole.USER, Map.of(), null, null);
    }

    public AiRequest(String prompt, Map<String, Object> parameters) {
        this(prompt, AiRole.USER, parameters, null, null);
    }
}
