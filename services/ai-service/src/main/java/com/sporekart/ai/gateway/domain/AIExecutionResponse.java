package com.sporekart.ai.gateway.domain;

import com.sporekart.ai.core.domain.AiRole;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

public record AIExecutionResponse(
        String requestId,
        String correlationId,
        String content,
        AiRole role,
        String provider,
        String model,
        OffsetDateTime timestamp,
        long executionTimeMs,
        String status,
        List<String> warnings,
        Map<String, Object> metadata,
        AIErrorDetail error) {
    public boolean isSuccess() { return error == null; }

    public static AIExecutionResponse success(String requestId, String correlationId, String content,
            String provider, String model, long executionTimeMs) {
        return new AIExecutionResponse(requestId, correlationId, content, AiRole.ASSISTANT,
                provider, model, OffsetDateTime.now(), executionTimeMs, "COMPLETED",
                List.of(), Map.of(), null);
    }

    public static AIExecutionResponse failure(String requestId, String correlationId,
            String errorCode, String errorMessage, long executionTimeMs) {
        return new AIExecutionResponse(requestId, correlationId, null, null,
                null, null, OffsetDateTime.now(), executionTimeMs, "FAILED",
                List.of(), Map.of(), new AIErrorDetail(errorCode, errorMessage));
    }
}
