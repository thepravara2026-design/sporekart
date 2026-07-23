package com.sporekart.executive.copilot.engine;

import com.sporekart.executive.copilot.domain.StrategicRecommendation;
import com.sporekart.executive.copilot.dto.DecisionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StrategicDecisionEngineTest {

    private StrategicDecisionEngine engine;

    @BeforeEach
    void setUp() {
        engine = new StrategicDecisionEngine();
    }

    @Test
    void getRecommendation_expansion_shouldReturnValidDecision() {
        var request = new DecisionRequest("Expand to South India", "expansion", "growth", 2500000.0, "6 months");
        var response = engine.getRecommendation(request);
        assertNotNull(response);
        assertTrue(response.expectedROI() > 0);
        assertNotNull(response.recommendation());
        assertNotNull(response.supportingData());
    }

    @Test
    void getRecommendation_expansion_shouldHaveHighROI() {
        var request = new DecisionRequest("Expand to South India", "expansion", "growth", 2500000.0, "6 months");
        var response = engine.getRecommendation(request);
        assertEquals(185.0, response.expectedROI());
        assertEquals("MEDIUM", response.riskLevel());
    }

    @Test
    void getRecommendation_marketEntry_shouldReturnValidDecision() {
        var request = new DecisionRequest("Enter UAE market", "market_entry", "expansion", 5000000.0, "12 months");
        var response = engine.getRecommendation(request);
        assertNotNull(response);
        assertTrue(response.expectedROI() > 0);
        assertEquals("HIGH", response.riskLevel());
    }

    @Test
    void getRecommendation_pricing_shouldReturnValidDecision() {
        var request = new DecisionRequest("Optimize pricing", "pricing", "revenue", 0.0, "1 month");
        var response = engine.getRecommendation(request);
        assertNotNull(response);
        assertEquals("LOW", response.riskLevel());
        assertEquals(0.88, response.confidenceScore());
    }

    @Test
    void getRecommendation_investment_shouldReturnValidDecision() {
        var request = new DecisionRequest("Warehouse automation", "investment", "operations", 2500000.0, "4 months");
        var response = engine.getRecommendation(request);
        assertNotNull(response);
        assertEquals("MEDIUM", response.riskLevel());
        assertTrue(response.supportingData().size() >= 2);
    }

    @Test
    void getRecommendation_default_shouldReturnFallback() {
        var request = new DecisionRequest("Unknown intent", "unknown", "other", 0.0, null);
        var response = engine.getRecommendation(request);
        assertNotNull(response);
        assertEquals("LOW", response.riskLevel());
    }

    @Test
    void createRecommendation_shouldReturnValidRecommendation() {
        var rec = engine.createRecommendation("Test", "Description",
            StrategicRecommendation.RecommendationCategory.EXPANSION, 100.0, 80.0, 0.9);
        assertNotNull(rec);
        assertNotNull(rec.recId());
        assertEquals("Test", rec.title());
    }

    @Test
    void prioritizeRecommendations_shouldReturnSortedList() {
        var recs = List.of(
            engine.createRecommendation("A", "Desc", StrategicRecommendation.RecommendationCategory.EXPANSION, 100.0, 80.0, 0.5),
            engine.createRecommendation("B", "Desc", StrategicRecommendation.RecommendationCategory.INVESTMENT, 200.0, 90.0, 0.9)
        );
        var sorted = engine.prioritizeRecommendations(recs);
        assertEquals(2, sorted.size());
        assertEquals("B", sorted.get(0).title());
    }

    @Test
    void prioritizeRecommendations_nullList_shouldReturnEmpty() {
        var sorted = engine.prioritizeRecommendations(null);
        assertTrue(sorted.isEmpty());
    }

    @Test
    void simulateDecisionImpact_shouldReturnValidSimulation() {
        var result = engine.simulateDecisionImpact("Warehouse automation", 2500000.0);
        assertNotNull(result);
        assertEquals("Warehouse automation", result.get("decision"));
        assertEquals(2500000.0, result.get("investment"));
        assertTrue((Double) result.get("projectedRevenueIncrease") > 0);
    }

    @Test
    void simulateDecisionImpact_shouldHaveBreakevenMonths() {
        var result = engine.simulateDecisionImpact("Expansion", 1000000.0);
        assertEquals(14, result.get("breakevenMonths"));
    }
}
