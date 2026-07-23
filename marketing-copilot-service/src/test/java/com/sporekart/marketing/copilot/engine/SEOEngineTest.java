package com.sporekart.marketing.copilot.engine;

import com.sporekart.marketing.copilot.dto.SEORequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SEOEngineTest {

    @InjectMocks
    private SEOEngine seoEngine;

    @Test
    void testAnalyzeSEO() {
        var request = new SEORequest("https://sporekart.com/mushroom-guide", "mushroom cultivation", "en_IN", "article", null);
        var response = seoEngine.analyzeSEO(request);
        assertNotNull(response);
        assertEquals("mushroom cultivation", response.targetKeyword());
        assertTrue(response.searchVolume() > 0);
        assertTrue(response.keywordDifficulty() > 0);
        assertFalse(response.suggestions().isEmpty());
        assertNotNull(response.checklist());
    }

    @Test
    void testGenerateChecklistWithContent() {
        var checklist = seoEngine.generateChecklist("https://sporekart.com/test", "This is a sample content with enough length for testing purposes to ensure adequate content length validation.");
        assertNotNull(checklist);
        assertTrue(checklist.score() >= 0);
        assertNotNull(checklist.critical());
        assertNotNull(checklist.warnings());
    }

    @Test
    void testGenerateChecklistWithoutContent() {
        var checklist = seoEngine.generateChecklist("https://sporekart.com/test", "");
        assertNotNull(checklist);
        assertFalse(checklist.critical().isEmpty());
    }

    @Test
    void testSuggestKeywords() {
        var keywords = seoEngine.suggestKeywords("mushroom", 5);
        assertEquals(5, keywords.size());
        assertTrue(keywords.get(0).contains("mushroom"));
    }

    @Test
    void testConductCompetitorAnalysis() {
        var analysis = seoEngine.conductCompetitorAnalysis("mushroom cultivation");
        assertEquals(4, analysis.size());
    }

    @Test
    void testEstimateSearchVolume() {
        var volume = seoEngine.estimateSearchVolume("mushroom");
        assertTrue(volume > 0);
    }

    @Test
    void testCalculateKeywordDifficulty() {
        var difficulty = seoEngine.calculateKeywordDifficulty("organic mushroom farming");
        assertTrue(difficulty > 0);
    }

    @Test
    void testEstimateTraffic() {
        var traffic = seoEngine.estimateTraffic(1000, 1);
        assertTrue(traffic > 0);
    }
}
