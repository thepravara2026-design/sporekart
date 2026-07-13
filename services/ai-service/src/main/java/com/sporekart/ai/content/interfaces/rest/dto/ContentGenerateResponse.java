package com.sporekart.ai.content.interfaces.rest.dto;

import com.sporekart.ai.content.domain.ContentGenerationResponse;
import java.time.OffsetDateTime;
import java.util.UUID;

public record ContentGenerateResponse(
        UUID id,
        String requestId,
        String content,
        String contentType,
        String category,
        String tone,
        int tokenCount,
        double confidenceScore,
        boolean humanReviewRequired,
        String moderationStatus,
        OffsetDateTime generatedAt,
        long latencyMs,
        boolean success,
        String errorMessage
) {
    public static ContentGenerateResponse from(ContentGenerationResponse resp) {
        return new ContentGenerateResponse(
                resp.id(), resp.requestId(), resp.content(),
                resp.contentType().name(), resp.category().name(),
                resp.tone().name(), resp.tokenCount(),
                resp.confidenceScore(), resp.humanReviewRequired(),
                resp.moderationStatus().name(), resp.generatedAt(),
                resp.latencyMs(), resp.success(), resp.errorMessage()
        );
    }
}
