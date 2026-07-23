package com.sporekart.marketing.copilot.engine;

import com.sporekart.marketing.copilot.dto.ContentGenerationRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ContentEngineTest {

    @InjectMocks
    private ContentEngine contentEngine;

    @Test
    void testGenerateContent() {
        var request = new ContentGenerationRequest("blog_article", "Mushroom Cultivation", "home-growers",
            "professional", "en_IN", List.of("mushroom", "cultivation", "guide"), 500, null, null);
        var response = contentEngine.generateContent(request);
        assertNotNull(response);
        assertNotNull(response.contentId());
        assertTrue(response.title().contains("Mushroom Cultivation"));
        assertEquals("blog_article", response.contentType());
        assertFalse(response.seoRecommendations().isEmpty());
    }

    @Test
    void testCreateContentRecord() {
        var content = contentEngine.createContentRecord("Test Title", "Test body", 
            com.sporekart.marketing.copilot.domain.MarketingContent.ContentType.BLOG_ARTICLE,
            "home-growers", List.of("keyword1"), "en_IN");
        assertNotNull(content);
        assertEquals("Test Title", content.title());
        assertEquals("home-growers", content.targetAudience());
    }

    @Test
    void testSuggestTopicsForHomeGrowers() {
        var topics = contentEngine.suggestTopics("home-growers", "beginners");
        assertEquals(3, topics.size());
        assertTrue(topics.get(0).contains("Beginner"));
    }

    @Test
    void testSuggestTopicsForCommercialGrowers() {
        var topics = contentEngine.suggestTopics("commercial-growers", "advanced");
        assertEquals(3, topics.size());
        assertTrue(topics.get(0).contains("Scaling"));
    }

    @Test
    void testSuggestTopicsDefault() {
        var topics = contentEngine.suggestTopics("enthusiasts", "all");
        assertEquals(2, topics.size());
    }

    @Test
    void testOptimizeSEO() {
        var recommendations = contentEngine.optimizeSEO("Some content about mushrooms", List.of("mushroom", "growing"));
        assertNotNull(recommendations);
        assertFalse(recommendations.isEmpty());
    }

    @Test
    void testGenerateCampaignContent() {
        var response = contentEngine.generateCampaignContent("camp-1", "Organic Mushrooms",
            com.sporekart.marketing.copilot.domain.MarketingContent.ContentType.LANDING_PAGE, "commercial-growers");
        assertNotNull(response);
        assertEquals("LANDING_PAGE", response.contentType());
    }
}
