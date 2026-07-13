package com.sporekart.ai.content.interfaces.rest.dto;

import com.sporekart.ai.content.domain.ContentGenerationResponse;
import java.time.OffsetDateTime;
import java.util.UUID;

public record ContentHistoryResponse(
        UUID id,
        String requestId,
        String content,
        String contentType,
        String category,
        String tone,
        int tokenCount,
        long latencyMs,
        boolean success,
        OffsetDateTime generatedAt
) {
    public static ContentHistoryResponse from(ContentGenerationResponse resp) {
        return new ContentHistoryResponse(
                resp.id(), resp.requestId(), resp.content(),
                resp.contentType().name(), resp.category().name(),
                resp.tone().name(), resp.tokenCount(),
                resp.latencyMs(), resp.success(), resp.generatedAt()
        );
    }
}
