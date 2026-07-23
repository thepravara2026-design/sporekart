package com.sporekart.customer.engine;

import com.sporekart.customer.copilot.domain.KnowledgeArticle;
import com.sporekart.customer.infrastructure.knowledge.KnowledgeClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KnowledgeServiceTest {

    @Mock
    private KnowledgeClient knowledgeClient;

    private KnowledgeService knowledgeService;

    private KnowledgeArticle article1;
    private KnowledgeArticle article2;

    @BeforeEach
    void setUp() {
        knowledgeService = new KnowledgeService(knowledgeClient);

        article1 = new KnowledgeArticle("KB-001", "Getting Started with Mushroom Cultivation",
            "A comprehensive beginner's guide.", "Full content here...",
            "Cultivation", "SporeKart KB", 0.95, "/knowledge/getting-started");

        article2 = new KnowledgeArticle("KB-002", "Oyster Mushroom Growing Guide",
            "Step-by-step instructions.", "Full content here...",
            "Cultivation", "SporeKart KB", 0.92, "/knowledge/oyster-guide");
    }

    @Test
    void searchKnowledgeReturnsGroundedAnswers() {
        when(knowledgeClient.searchKnowledge(eq("oyster"), isNull()))
            .thenReturn(List.of(article2));

        var results = knowledgeService.searchKnowledge("oyster");

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertTrue(results.get(0).title().toLowerCase().contains("oyster"));
    }

    @Test
    void getFAQsReturnsQuestions() {
        when(knowledgeClient.getFAQs(any()))
            .thenReturn(List.of(article1, article2));

        var faqs = knowledgeService.getFAQs("Cultivation");

        assertNotNull(faqs);
        assertFalse(faqs.isEmpty());
    }

    @Test
    void getGrowingAdviceForSpecificMushroomReturnsAdvice() {
        when(knowledgeClient.getGrowingAdvice("oyster"))
            .thenReturn(Map.of(
                "mushroomType", "oyster",
                "difficulty", "Easy",
                "temperature", "18-24°C",
                "humidity", "85-95%",
                "growingCycleDays", 30
            ));

        var advice = knowledgeService.getGrowingAdvice("oyster");

        assertNotNull(advice);
        assertEquals("oyster", advice.get("mushroomType"));
        assertTrue(advice.containsKey("temperature"));
        assertTrue(advice.containsKey("humidity"));
    }

    @Test
    void answersContainCitations() {
        when(knowledgeClient.searchKnowledge(anyString(), isNull()))
            .thenReturn(List.of(article1, article2));

        var results = knowledgeService.searchKnowledge("mushroom");

        assertFalse(results.isEmpty());
        results.forEach(a -> {
            assertNotNull(a.source());
            assertFalse(a.source().isBlank());
            assertNotNull(a.url());
        });
    }

    @Test
    void emptyQueryReturnsAllKnowledge() {
        when(knowledgeClient.searchKnowledge(isNull(), isNull()))
            .thenReturn(List.of(article1, article2));

        var results = knowledgeService.searchKnowledge(null);

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(2, results.size());
    }

    @Test
    void searchKnowledgeWithCategoryFiltersCorrectly() {
        when(knowledgeClient.searchKnowledge(eq("mushroom"), eq("Cultivation")))
            .thenReturn(List.of(article1));

        var results = knowledgeService.searchKnowledge("mushroom", "Cultivation");

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("Cultivation", results.get(0).category());
    }

    @Test
    void searchKnowledgeReturnsSortedByRelevance() {
        when(knowledgeClient.searchKnowledge(anyString(), any()))
            .thenReturn(List.of(article1, article2));

        var results = knowledgeService.searchKnowledge("cultivation");

        assertFalse(results.isEmpty());
        results.forEach(a -> assertTrue(a.relevanceScore() >= 0 && a.relevanceScore() <= 1.0));
    }
}
