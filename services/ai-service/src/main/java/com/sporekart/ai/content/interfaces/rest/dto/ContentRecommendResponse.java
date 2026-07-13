package com.sporekart.ai.content.interfaces.rest.dto;

import com.sporekart.ai.content.domain.ContentRecommendationResult;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record ContentRecommendResponse(
        UUID id,
        UUID requestId,
        List<Map<String, Object>> recommendations,
        String type,
        int totalResults,
        OffsetDateTime generatedAt
) {
    public static ContentRecommendResponse from(ContentRecommendationResult result) {
        return new ContentRecommendResponse(
                result.id(), result.requestId(), result.recommendations(),
                result.type().name(), result.totalResults(),
                result.generatedAt()
        );
    }
}
