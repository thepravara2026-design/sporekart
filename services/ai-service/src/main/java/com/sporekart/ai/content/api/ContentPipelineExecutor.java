package com.sporekart.ai.content.api;

import com.sporekart.ai.content.domain.*;

public interface ContentPipelineExecutor {
    ContentGenerationResponse executeGenerationPipeline(ContentGenerationRequest request);
    ContentSummaryResult executeSummaryPipeline(ContentSummaryRequest request);
    ContentTranslationResult executeTranslationPipeline(ContentTranslationRequest request);
    ContentClassificationResult executeClassificationPipeline(ContentClassificationRequest request);
    ContentModerationResult executeModerationPipeline(ContentModerationRequest request);
    ContentSEOResult executeSEOPipeline(ContentSEORequest request);
    ContentRecommendationResult executeRecommendationPipeline(ContentRecommendationRequest request);
}
