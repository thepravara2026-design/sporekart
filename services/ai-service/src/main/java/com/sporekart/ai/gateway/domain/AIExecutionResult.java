package com.sporekart.ai.gateway.domain;

import com.sporekart.ai.core.domain.AiResponse;
import java.util.Map;

public record AIExecutionResult(
        AiResponse response,
        String provider,
        String model,
        long durationMs,
        Map<String, Object> metadata) {
}
