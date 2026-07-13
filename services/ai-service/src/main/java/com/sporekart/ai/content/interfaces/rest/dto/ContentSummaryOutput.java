package com.sporekart.ai.content.interfaces.rest.dto;

import com.sporekart.ai.content.domain.ContentSummaryResult;
import java.time.OffsetDateTime;
import java.util.UUID;

public record ContentSummaryOutput(
        UUID id,
        UUID requestId,
        String summary,
        int originalLength,
        int summaryLength,
        double compressionRatio,
        String language,
        OffsetDateTime generatedAt
) {
    public static ContentSummaryOutput from(ContentSummaryResult result) {
        return new ContentSummaryOutput(
                result.id(), result.requestId(), result.summary(),
                result.originalLength(), result.summaryLength(),
                result.compressionRatio(), result.language(),
                result.generatedAt()
        );
    }
}
