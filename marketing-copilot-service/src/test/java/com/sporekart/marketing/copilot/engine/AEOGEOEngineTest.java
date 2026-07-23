package com.sporekart.marketing.copilot.engine;

import com.sporekart.marketing.copilot.dto.AEOGEORequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AEOGEOEngineTest {

    @InjectMocks
    private AEOGEOEngine aeogeoEngine;

    @Test
    void testAnalyzeAEOGEO() {
        var request = new AEOGEORequest("mushroom cultivation", "Mushroom cultivation is the practice of growing mushrooms.", "en_IN");
        var response = aeogeoEngine.analyzeAEOGEO(request);
        assertNotNull(response);
        assertEquals("mushroom cultivation", response.query());
        assertTrue(response.answerAppearanceScore() >= 0);
        assertTrue(response.generativeSnippetScore() >= 0);
        assertFalse(response.featuredSnippetOpportunities().isEmpty());
        assertFalse(response.peopleAlsoAsk().isEmpty());
        assertNotNull(response.optimizedSnippet());
        assertFalse(response.optimizationTips().isEmpty());
    }

    @Test
    void testCalculateAnswerAppearanceScoreFullMatch() {
        var score = aeogeoEngine.calculateAnswerAppearanceScore("Mushroom cultivation is fun", "mushroom cultivation");
        assertTrue(score > 50);
    }

    @Test
    void testCalculateAnswerAppearanceScoreNoMatch() {
        var score = aeogeoEngine.calculateAnswerAppearanceScore("Nothing about it here", "mushroom cultivation");
        assertEquals(0.0, score);
    }

    @Test
    void testCalculateGenerativeSnippetScoreWithLists() {
        var score = aeogeoEngine.calculateGenerativeSnippetScore("1. First item\n2. Second item\n- Third item", "test");
        assertTrue(score >= 55);
    }

    @Test
    void testCalculateGenerativeSnippetScorePlain() {
        var score = aeogeoEngine.calculateGenerativeSnippetScore("Just plain text without any structure", "test");
        assertEquals(30.0, score);
    }

    @Test
    void testGenerateOptimizedSnippet() {
        var snippet = aeogeoEngine.generateOptimizedSnippet("mushroom farming");
        assertNotNull(snippet);
        assertTrue(snippet.contains("mushroom farming"));
    }

    @Test
    void testGetSchemaSuggestions() {
        var schemas = aeogeoEngine.getSchemaSuggestions("article");
        assertTrue(schemas.contains("Article"));
    }

    @Test
    void testAnalyzeQuery() {
        var result = aeogeoEngine.analyzeQuery("how to grow mushrooms", "Content about growing mushrooms", "en_IN");
        assertNotNull(result);
        assertEquals("how to grow mushrooms", result.query());
        assertFalse(result.schemaTypes().isEmpty());
    }
}
