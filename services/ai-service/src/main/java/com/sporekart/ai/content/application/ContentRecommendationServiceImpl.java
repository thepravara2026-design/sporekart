package com.sporekart.ai.content.application;

import com.sporekart.ai.content.api.ContentRecommendationService;
import com.sporekart.ai.content.domain.ContentRecommendationRequest;
import com.sporekart.ai.content.domain.ContentRecommendationResult;
import com.sporekart.ai.content.domain.RecommendationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ContentRecommendationServiceImpl implements ContentRecommendationService {

    private static final Logger log = LoggerFactory.getLogger(ContentRecommendationServiceImpl.class);

    @Override
    public ContentRecommendationResult getRecommendations(ContentRecommendationRequest request) {
        log.info("Getting recommendations for request {}", request.id());
        return new ContentRecommendationResult(
                UUID.randomUUID(),
                request.id(),
                List.of(),
                request.recommendationType(),
                0,
                OffsetDateTime.now()
        );
    }

    @Override
    public ContentRecommendationResult getPersonalizedRecommendations(String contextId, UUID userId) {
        log.info("Getting personalized recommendations for user {} in context {}", userId, contextId);
        return new ContentRecommendationResult(
                UUID.randomUUID(),
                UUID.randomUUID(),
                List.of(),
                RecommendationType.PERSONALIZED,
                0,
                OffsetDateTime.now()
        );
    }

    @Override
    public Optional<ContentRecommendationResult> getRecommendationResult(UUID id) {
        log.debug("Fetching recommendation result {}", id);
        return Optional.empty();
    }
}
