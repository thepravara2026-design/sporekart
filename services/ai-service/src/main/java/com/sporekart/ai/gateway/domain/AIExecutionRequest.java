package com.sporekart.ai.gateway.domain;

import com.sporekart.ai.core.domain.AiRole;
import java.util.Map;

public record AIExecutionRequest(
        String prompt,
        AiRole role,
        String module,
        String preferredProvider,
        String userId,
        Map<String, Object> parameters,
        Map<String, Object> metadata) {
    public AIExecutionRequest(String prompt) {
        this(prompt, AiRole.USER, null, null, null, Map.of(), Map.of());
    }
}
