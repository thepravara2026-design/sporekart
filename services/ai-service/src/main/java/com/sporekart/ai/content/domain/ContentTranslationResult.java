package com.sporekart.ai.content.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ContentTranslationResult(
    UUID id,
    UUID requestId,
    String translatedText,
    String sourceLanguage,
    String targetLanguage,
    String detectedLanguage,
    double confidenceScore,
    OffsetDateTime generatedAt) {}
