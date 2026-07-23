package com.sporekart.bi.copilot.engine;

import com.sporekart.bi.copilot.domain.BusinessInsight;
import com.sporekart.bi.copilot.domain.CrossCopilotMetric;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CrossCopilotIntelligenceEngineTest {

    private CrossCopilotIntelligenceEngine engine;

    @BeforeEach
    void setUp() {
        engine = new CrossCopilotIntelligenceEngine();
    }

    @Test
    void getCrossCopilotMetrics_shouldReturnSixteenMetrics() {
        List<CrossCopilotMetric> metrics = engine.getCrossCopilotMetrics();
        assertEquals(16, metrics.size());
    }

    @Test
    void getCrossCopilotMetrics_shouldCoverAllCopilots() {
        List<CrossCopilotMetric> metrics = engine.getCrossCopilotMetrics();
        long uniqueCopilots = metrics.stream().map(CrossCopilotMetric::copilotType).distinct().count();
        assertEquals(4, uniqueCopilots);
    }

    @Test
    void getCopilotPerformanceComparison_shouldReturnFourCopilots() {
        Map<String, Object> comparison = engine.getCopilotPerformanceComparison();
        assertEquals(4, comparison.size());
    }

    @Test
    void getCopilotPerformanceComparison_shouldHavePerformanceKeys() {
        Map<String, Object> comparison = engine.getCopilotPerformanceComparison();
        Map<String, Object> perf = (Map<String, Object>) comparison.values().iterator().next();
        assertTrue(perf.containsKey("activeSessions"));
        assertTrue(perf.containsKey("avgLatencyMs"));
        assertTrue(perf.containsKey("userSatisfaction"));
        assertTrue(perf.containsKey("revenueShare"));
    }

    @Test
    void getUnifiedBusinessHealth_shouldHaveOverallScore() {
        Map<String, Object> health = engine.getUnifiedBusinessHealth();
        assertTrue(health.containsKey("overallHealthScore"));
        assertTrue(health.containsKey("status"));
    }

    @Test
    void getUnifiedBusinessHealth_shouldHaveComponentScores() {
        Map<String, Object> health = engine.getUnifiedBusinessHealth();
        assertTrue(health.containsKey("componentScores"));
    }

    @Test
    void getRevenueContributionByCopilot_shouldReturnFourEntries() {
        Map<String, Double> contribution = engine.getRevenueContributionByCopilot();
        assertEquals(4, contribution.size());
    }

    @Test
    void getRevenueContributionByCopilot_shouldSumToApproximately100() {
        Map<String, Double> contribution = engine.getRevenueContributionByCopilot();
        double sum = contribution.values().stream().mapToDouble(Double::doubleValue).sum();
        assertEquals(100.0, sum, 1.0);
    }

    @Test
    void generateWeeklyDigest_shouldHaveSummaryAndMetrics() {
        Map<String, Object> digest = engine.generateWeeklyDigest();
        assertTrue(digest.containsKey("summary"));
        assertTrue(digest.containsKey("copilotMetrics"));
        assertTrue(digest.containsKey("topInsight"));
    }

    @Test
    void getCorrelationInsights_shouldReturnFourInsights() {
        List<BusinessInsight> insights = engine.getCorrelationInsights();
        assertEquals(4, insights.size());
    }

    @Test
    void getTopCombinedInsights_shouldLimitResults() {
        List<BusinessInsight> insights = engine.getTopCombinedInsights(2);
        assertEquals(2, insights.size());
    }
}
