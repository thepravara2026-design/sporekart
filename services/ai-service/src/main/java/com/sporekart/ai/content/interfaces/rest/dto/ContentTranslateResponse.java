package com.sporekart.ai.content.interfaces.rest.dto;

import com.sporekart.ai.content.domain.ContentTranslationResult;
import java.time.OffsetDateTime;
import java.util.UUID;

public record ContentTranslateResponse(
        UUID id,
        UUID requestId,
        String translatedText,
        String sourceLanguage,
        String targetLanguage,
        String detectedLanguage,
        double confidenceScore,
        OffsetDateTime generatedAt
) {
    public static ContentTranslateResponse from(ContentTranslationResult result) {
        return new ContentTranslateResponse(
                result.id(), result.requestId(), result.translatedText(),
                result.sourceLanguage(), result.targetLanguage(),
                result.detectedLanguage(), result.confidenceScore(),
                result.generatedAt()
        );
    }
}
