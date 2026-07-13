package com.sporekart.ai.content.api;

import com.sporekart.ai.content.domain.*;
import java.util.Optional;
import java.util.UUID;

public interface ContentRecommendationService {
    ContentRecommendationResult getRecommendations(ContentRecommendationRequest request);
    ContentRecommendationResult getPersonalizedRecommendations(String contextId, UUID userId);
    Optional<ContentRecommendationResult> getRecommendationResult(UUID id);
}
