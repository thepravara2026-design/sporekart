package com.sporekart.marketing.copilot.engine;

import com.sporekart.marketing.copilot.dto.MarketingBriefRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StrategyEngineTest {

    @InjectMocks
    private StrategyEngine strategyEngine;

    @Test
    void testGenerateBrief() {
        var request = new MarketingBriefRequest("Increase brand awareness", "home-growers",
            "SporeKart is the best", List.of("email", "social"), 50000.0, "4 weeks");
        var response = strategyEngine.generateBrief(request);
        assertNotNull(response);
        assertNotNull(response.briefId());
        assertTrue(response.title().contains("Increase brand awareness"));
        assertFalse(response.deliverables().isEmpty());
        assertFalse(response.competitorSuggestions().isEmpty());
    }

    @Test
    void testGenerateBriefWithMinimalInput() {
        var request = new MarketingBriefRequest("Generate leads", "commercial-growers",
            null, null, 25000.0, null);
        var response = strategyEngine.generateBrief(request);
        assertNotNull(response);
        assertEquals("Generate leads", response.objective());
    }

    @Test
    void testGeneratePersonasForHomeGrowers() {
        var personas = strategyEngine.generatePersonas("home-growers");
        assertEquals(2, personas.size());
        assertEquals("Beginner Bob", personas.get(0).name());
        assertEquals("Hobbyist Helen", personas.get(1).name());
    }

    @Test
    void testGeneratePersonasForCommercialGrowers() {
        var personas = strategyEngine.generatePersonas("commercial-growers");
        assertEquals(1, personas.size());
        assertEquals("Farmer Frank", personas.get(0).name());
    }

    @Test
    void testGeneratePersonasForUnknown() {
        var personas = strategyEngine.generatePersonas("unknown");
        assertTrue(personas.isEmpty());
    }

    @Test
    void testAnalyzeCompetitors() {
        var analysis = strategyEngine.analyzeCompetitors("mushroom farming");
        assertEquals(5, analysis.size());
    }

    @Test
    void testCreateBrief() {
        var request = new MarketingBriefRequest("Test objective", "test audience", null, null, 0, null);
        var brief = strategyEngine.createBrief(request);
        assertNotNull(brief);
        assertNotNull(brief.id());
        assertEquals("Test objective", brief.objective());
    }
}
