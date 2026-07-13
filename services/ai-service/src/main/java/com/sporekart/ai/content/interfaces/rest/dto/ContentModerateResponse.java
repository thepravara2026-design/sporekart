package com.sporekart.ai.content.interfaces.rest.dto;

import com.sporekart.ai.content.domain.ContentModerationResult;
import com.sporekart.ai.content.domain.ModerationStatus;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record ContentModerateResponse(
        UUID id,
        UUID requestId,
        ModerationStatus status,
        boolean containsPii,
        boolean containsProfanity,
        boolean isToxic,
        double confidenceScore,
        List<String> flags,
        boolean humanReviewRequired,
        UUID reviewedBy,
        OffsetDateTime reviewedAt
) {
    public static ContentModerateResponse from(ContentModerationResult result) {
        return new ContentModerateResponse(
                result.id(), result.requestId(), result.status(),
                result.containsPii(), result.containsProfanity(),
                result.isToxic(), result.confidenceScore(),
                result.flags(), result.humanReviewRequired(),
                result.reviewedBy(), result.reviewedAt()
        );
    }
}
