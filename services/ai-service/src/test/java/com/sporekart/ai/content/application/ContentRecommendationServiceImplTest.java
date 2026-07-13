package com.sporekart.ai.content.application;

import com.sporekart.ai.content.domain.ContentRecommendationRequest;
import com.sporekart.ai.content.domain.RecommendationType;
import com.sporekart.ai.core.domain.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ContentRecommendationServiceImplTest {

    private ContentRecommendationServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ContentRecommendationServiceImpl();
    }

    @Test
    void shouldGetRecommendations() {
        var request = new ContentRecommendationRequest(UUID.randomUUID(), "ctx1", UUID.randomUUID(),
                RecommendationType.PERSONALIZED, ContentType.TEXT, 10, Map.of("lang", "en"));

        var result = service.getRecommendations(request);

        assertNotNull(result);
        assertNotNull(result.id());
        assertEquals(request.id(), result.requestId());
        assertEquals(RecommendationType.PERSONALIZED, result.type());
        assertTrue(result.recommendations().isEmpty());
        assertEquals(0, result.totalResults());
        assertNotNull(result.generatedAt());
    }

    @Test
    void shouldGetRecommendationsWithTrendingType() {
        var request = new ContentRecommendationRequest(UUID.randomUUID(), "ctx2", UUID.randomUUID(),
                RecommendationType.TRENDING, ContentType.TEXT, 5, null);

        var result = service.getRecommendations(request);

        assertNotNull(result);
        assertEquals(RecommendationType.TRENDING, result.type());
        assertTrue(result.recommendations().isEmpty());
    }

    @Test
    void shouldGetRecommendationsWithNullFilters() {
        var request = new ContentRecommendationRequest(UUID.randomUUID(), "ctx3", UUID.randomUUID(),
                RecommendationType.SIMILAR, null, 20, null);

        var result = service.getRecommendations(request);

        assertNotNull(result);
        assertEquals(RecommendationType.SIMILAR, result.type());
    }

    @Test
    void shouldGetPersonalizedRecommendations() {
        var contextId = "user-session-1";
        var userId = UUID.randomUUID();

        var result = service.getPersonalizedRecommendations(contextId, userId);

        assertNotNull(result);
        assertNotNull(result.id());
        assertEquals(RecommendationType.PERSONALIZED, result.type());
        assertTrue(result.recommendations().isEmpty());
    }

    @Test
    void shouldGetRecommendationResultReturnEmpty() {
        var id = UUID.randomUUID();

        var result = service.getRecommendationResult(id);

        assertTrue(result.isEmpty());
    }
}
