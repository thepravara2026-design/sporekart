package com.sporekart.operations.copilot.engine;

import com.sporekart.operations.copilot.domain.DecisionRecommendation;
import com.sporekart.operations.copilot.domain.DecisionRecommendation.DecisionCategory;
import com.sporekart.operations.copilot.dto.DecisionSupportRequest;
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
    private DecisionSupportEngine decisionSupportEngine;

    @Test
    void testGetRecommendationIncreaseProcurement() {
        var request = new DecisionSupportRequest("Increase purchase of MUSH-001", "increase_procurement", "INVENTORY", "MUSH-001");
        var response = decisionSupportEngine.getRecommendation(request);
        assertNotNull(response);
        assertNotNull(response.recommendation());
        assertTrue(response.confidenceScore() > 0);
        assertFalse(response.supportingData().isEmpty());
    }

    @Test
    void testGetRecommendationReduceProcurement() {
        var request = new DecisionSupportRequest("Reduce buying", "reduce_procurement", "INVENTORY", null);
        var response = decisionSupportEngine.getRecommendation(request);
        assertNotNull(response);
        assertTrue(response.recommendation().contains("Reduce"));
    }

    @Test
    void testGetRecommendationTransfer() {
        var request = new DecisionSupportRequest("Transfer stock", "transfer_inventory", "WAREHOUSE", null);
        var response = decisionSupportEngine.getRecommendation(request);
        assertNotNull(response);
        assertTrue(response.recommendation().contains("Transfer"));
    }

    @Test
    void testGetRecommendationPromotions() {
        var request = new DecisionSupportRequest("Launch promotion", "launch_promotions", "PRICING", null);
        var response = decisionSupportEngine.getRecommendation(request);
        assertNotNull(response);
        assertTrue(response.recommendation().contains("discount"));
    }

    @Test
    void testGetRecommendationDefault() {
        var request = new DecisionSupportRequest("Unknown action", "unknown", null, null);
        var response = decisionSupportEngine.getRecommendation(request);
        assertNotNull(response);
        assertTrue(response.recommendation().contains("Maintain"));
    }

    @Test
    void testCreateRecommendation() {
        var rec = decisionSupportEngine.createRecommendation("Test Rec", "Description",
            DecisionCategory.INVENTORY, 80.0, 50000.0, 0.9);
        assertNotNull(rec);
        assertEquals("Test Rec", rec.title());
        assertEquals(DecisionCategory.INVENTORY, rec.category());
    }

    @Test
    void testPrioritizeRecommendations() {
        var recs = List.of(
            new DecisionRecommendation("1", "Low", "Desc", DecisionCategory.PROCESS, 50.0, 0.0, 0.5, List.of(), "LOW", "3m"),
            new DecisionRecommendation("2", "High", "Desc", DecisionCategory.INVENTORY, 90.0, 0.0, 0.9, List.of(), "LOW", "1m")
        );
        var prioritized = decisionSupportEngine.prioritizeRecommendations(recs);
        assertEquals("High", prioritized.get(0).title());
    }

    @Test
    void testPrioritizeRecommendationsNull() {
        var prioritized = decisionSupportEngine.prioritizeRecommendations(null);
        assertTrue(prioritized.isEmpty());
    }

    @Test
    void testSimulateDecision() {
        var result = decisionSupportEngine.simulateDecision("Increase procurement", Map.of("amount", 1000));
        assertNotNull(result);
        assertTrue(result.containsKey("expectedOutcome"));
        assertTrue(result.containsKey("netBenefit"));
    }
}
