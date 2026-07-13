package com.sporekart.ai.content.interfaces.rest.dto;

import com.sporekart.ai.content.domain.ContentClassificationResult;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record ContentClassifyResponse(
        UUID id,
        UUID requestId,
        Map<String, Double> classifications,
        String primaryCategory,
        double confidenceScore,
        List<String> keywords,
        OffsetDateTime generatedAt
) {
    public static ContentClassifyResponse from(ContentClassificationResult result) {
        return new ContentClassifyResponse(
                result.id(), result.requestId(), result.classifications(),
                result.primaryCategory(), result.confidenceScore(),
                result.keywords(), result.generatedAt()
        );
    }
}
