package com.sporekart.ai.content.api;

import com.sporekart.ai.content.domain.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ContentGenerationService {
    ContentGenerationResponse generate(ContentGenerationRequest request);
    ContentGenerationResponse summarize(ContentSummaryRequest request);
    ContentGenerationResponse translate(ContentTranslationRequest request);
    ContentGenerationResponse classify(ContentClassificationRequest request);
    ContentGenerationResponse moderate(ContentModerationRequest request);
    ContentGenerationResponse generateSEO(ContentSEORequest request);
    Optional<ContentGenerationResponse> getGeneration(UUID id);
    List<ContentGenerationResponse> listHistory(UUID userId, int page, int size);
}
