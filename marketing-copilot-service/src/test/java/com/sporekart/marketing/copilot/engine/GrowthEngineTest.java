package com.sporekart.marketing.copilot.engine;

import com.sporekart.marketing.copilot.domain.GrowthRecommendation;
import com.sporekart.marketing.copilot.domain.GrowthRecommendation.GrowthArea;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GrowthEngineTest {

    @InjectMocks
    private GrowthEngine growthEngine;

    @Test
    void testGenerateRecommendations() {
        var response = growthEngine.generateRecommendations("home-growers", "beginners");
        assertNotNull(response);
        assertFalse(response.recommendations().isEmpty());
        assertEquals(5, response.recommendations().size());
        assertNotNull(response.summary());
    }

    @Test
    void testCreateCustomRecommendation() {
        var rec = growthEngine.createCustomRecommendation("Test Growth", "Description",
            GrowthArea.CUSTOMER_ACQUISITION, 80.0, 0.9, List.of("Action 1", "Action 2"));
        assertNotNull(rec);
        assertEquals("Test Growth", rec.title());
        assertEquals(GrowthArea.CUSTOMER_ACQUISITION, rec.area());
    }

    @Test
    void testPrioritizeRecommendations() {
        var recs = List.of(
            new GrowthRecommendation("1", "Low", "Desc", GrowthArea.CONTENT_STRATEGY, 50.0, 0.5, "3m", List.of(), ""),
            new GrowthRecommendation("2", "High", "Desc", GrowthArea.CUSTOMER_ACQUISITION, 90.0, 0.9, "3m", List.of(), "")
        );
        var prioritized = growthEngine.prioritizeRecommendations(recs);
        assertEquals("High", prioritized.get(0).title());
        assertEquals("Low", prioritized.get(1).title());
    }

    @Test
    void testPrioritizeRecommendationsEmpty() {
        var prioritized = growthEngine.prioritizeRecommendations(List.of());
        assertTrue(prioritized.isEmpty());
    }

    @Test
    void testPrioritizeRecommendationsNull() {
        var prioritized = growthEngine.prioritizeRecommendations(null);
        assertTrue(prioritized.isEmpty());
    }

    @Test
    void testCalculateProjectedROI() {
        var roi = growthEngine.calculateProjectedROI(10000.0, 50000.0);
        assertEquals(400.0, roi, 0.01);
    }

    @Test
    void testCalculateProjectedROIWithZeroInvestment() {
        var roi = growthEngine.calculateProjectedROI(0.0, 50000.0);
        assertEquals(0.0, roi, 0.01);
    }

    @Test
    void testSuggestExperimentsForAcquisition() {
        var experiments = growthEngine.suggestExperiments("CUSTOMER_ACQUISITION");
        assertFalse(experiments.isEmpty());
        assertTrue(experiments.get(0).contains("A/B test"));
    }

    @Test
    void testSuggestExperimentsForRetention() {
        var experiments = growthEngine.suggestExperiments("CUSTOMER_RETENTION");
        assertFalse(experiments.isEmpty());
    }

    @Test
    void testSuggestExperimentsDefault() {
        var experiments = growthEngine.suggestExperiments("UNKNOWN");
        assertEquals(2, experiments.size());
    }
}
