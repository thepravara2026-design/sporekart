package com.sporekart.admin.engine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DecisionSupportEngineTest {

    private DecisionSupportEngine engine;

    @BeforeEach
    void setUp() {
        engine = new DecisionSupportEngine();
    }

    @Test
    void getReplenishmentRecommendationsShouldReturnSuggestions() {
        List<Map<String, Object>> recommendations = engine.getReplenishmentRecommendations();

        assertNotNull(recommendations);
        assertFalse(recommendations.isEmpty());
        for (Map<String, Object> rec : recommendations) {
            assertTrue(rec.containsKey("product"));
            assertTrue(rec.containsKey("recommendedQuantity"));
            assertTrue(rec.containsKey("priority"));
        }
    }

    @Test
    void getMarketingRecommendationsAreActionable() {
        List<Map<String, Object>> recommendations = engine.getMarketingRecommendations();

        assertNotNull(recommendations);
        assertFalse(recommendations.isEmpty());
        for (Map<String, Object> rec : recommendations) {
            assertTrue(rec.containsKey("campaign"));
            assertTrue(rec.containsKey("channel"));
            assertTrue(rec.containsKey("budget"));
            assertTrue(rec.containsKey("expectedROI"));
        }
    }

    @Test
    void getPricingRecommendationsHaveImpactRatings() {
        List<Map<String, Object>> recommendations = engine.getPricingRecommendations();

        assertNotNull(recommendations);
        assertFalse(recommendations.isEmpty());
        for (Map<String, Object> rec : recommendations) {
            assertTrue(rec.containsKey("product"));
            assertTrue(rec.containsKey("currentPrice"));
            assertTrue(rec.containsKey("suggestedPrice"));
            assertTrue(rec.containsKey("impact"));
            String impact = (String) rec.get("impact");
            assertTrue(impact.equals("HIGH") || impact.equals("MEDIUM") || impact.equals("LOW"));
        }
    }

    @Test
    void getOperationalImprovementsAreSpecific() {
        List<Map<String, Object>> improvements = engine.getOperationalImprovements();

        assertNotNull(improvements);
        assertFalse(improvements.isEmpty());
        for (Map<String, Object> imp : improvements) {
            assertTrue(imp.containsKey("area"));
            assertTrue(imp.containsKey("suggestion"));
            assertTrue(imp.containsKey("expectedBenefit"));
            assertNotNull(imp.get("suggestion"));
            assertFalse(((String) imp.get("suggestion")).isBlank());
        }
    }

    @Test
    void recommendationsHavePriorityScores() {
        List<Map<String, Object>> replenishments = engine.getReplenishmentRecommendations();

        for (Map<String, Object> rec : replenishments) {
            assertTrue(rec.containsKey("priority"));
            String priority = (String) rec.get("priority");
            assertTrue(priority.equals("HIGH") || priority.equals("MEDIUM") || priority.equals("LOW"));
        }

        List<Map<String, Object>> marketing = engine.getMarketingRecommendations();
        for (Map<String, Object> rec : marketing) {
            assertTrue(rec.containsKey("expectedROI"));
            assertTrue(((Number) rec.get("expectedROI")).doubleValue() >= 0);
        }
    }
}
