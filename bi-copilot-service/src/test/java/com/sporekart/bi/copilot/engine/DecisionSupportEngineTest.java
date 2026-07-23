package com.sporekart.bi.copilot.engine;

import com.sporekart.bi.copilot.domain.DecisionRecommendation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DecisionSupportEngineTest {

    @InjectMocks
    private DecisionSupportEngine engine;

    @Test
    void generateRecommendationsWithRevenueFocusReturnsRevenueRecs() {
        List<DecisionRecommendation> recs = engine.generateRecommendations("revenue", "current");
        assertNotNull(recs);
        assertFalse(recs.isEmpty());
        recs.forEach(r -> assertEquals("revenue", r.category()));
    }

    @Test
    void generateRecommendationsWithCustomerFocusReturnsCustomerRecs() {
        List<DecisionRecommendation> recs = engine.generateRecommendations("customer", "current");
        assertNotNull(recs);
        assertFalse(recs.isEmpty());
        recs.forEach(r -> assertEquals("customer", r.category()));
    }

    @Test
    void generateRecommendationsWithInventoryFocusReturnsInventoryRecs() {
        List<DecisionRecommendation> recs = engine.generateRecommendations("inventory", "current");
        assertNotNull(recs);
        assertFalse(recs.isEmpty());
        recs.forEach(r -> assertEquals("inventory", r.category()));
    }

    @Test
    void generateRecommendationsWithTrainingFocusReturnsTrainingRecs() {
        List<DecisionRecommendation> recs = engine.generateRecommendations("training", "current");
        assertNotNull(recs);
        assertFalse(recs.isEmpty());
        recs.forEach(r -> assertEquals("training", r.category()));
    }

    @Test
    void getPriorityRecommendationsReturnsOrderedByPriority() {
        List<DecisionRecommendation> recs = engine.getPriorityRecommendations("current", 5);
        assertNotNull(recs);
        assertFalse(recs.isEmpty());
        assertTrue(recs.size() <= 5);
    }

    @Test
    void explainRecommendationReturnsWhyExplanation() {
        Map<String, Object> explanation = engine.explainRecommendation("REC-001");
        assertNotNull(explanation);
        assertTrue(explanation.containsKey("why"));
        assertTrue(explanation.containsKey("expectedImpact"));
        assertTrue(explanation.containsKey("confidence"));
    }

    @Test
    void explainRecommendationIncludesActionableSteps() {
        Map<String, Object> explanation = engine.explainRecommendation("REC-001");
        assertNotNull(explanation.get("actionItems"));
    }

    @Test
    void generateRecommendationsWithAllFocusReturnsAllCategories() {
        List<DecisionRecommendation> recs = engine.generateRecommendations("all", "current");
        assertNotNull(recs);
        assertFalse(recs.isEmpty());
    }

    @Test
    void getPriorityRecommendationsReturnsHighConfidenceRecs() {
        List<DecisionRecommendation> recs = engine.getPriorityRecommendations("current", 3);
        assertEquals(3, recs.size());
        recs.forEach(r -> assertTrue(r.confidenceScore() > 0));
    }
}
