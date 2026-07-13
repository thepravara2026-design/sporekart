package com.sporekart.ai.content.interfaces.rest.dto;

import com.sporekart.ai.content.domain.ContentSEOResult;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record ContentSEOOutput(
        UUID id,
        UUID requestId,
        String title,
        String metaDescription,
        List<String> keywords,
        String slug,
        double readabilityScore,
        double seoScore,
        List<String> suggestions,
        OffsetDateTime generatedAt
) {
    public static ContentSEOOutput from(ContentSEOResult result) {
        return new ContentSEOOutput(
                result.id(), result.requestId(), result.title(),
                result.metaDescription(), result.keywords(),
                result.slug(), result.readabilityScore(),
                result.seoScore(), result.suggestions(),
                result.generatedAt()
        );
    }
}
