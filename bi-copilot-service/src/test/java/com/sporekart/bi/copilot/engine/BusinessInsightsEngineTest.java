package com.sporekart.bi.copilot.engine;

import com.sporekart.bi.copilot.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BusinessInsightsEngineTest {

    private BusinessInsightsEngine engine;

    @BeforeEach
    void setUp() {
        engine = new BusinessInsightsEngine();
    }

    @Test
    void generateRevenueInsights_shouldReturnInsightsForHighGrowth() {
        RevenueMetrics metrics = new RevenueMetrics("m1", "2025-06", "2025-06-01", "2025-06-30",
                500000, 1000, 500, 400,
                Map.of("Product A", 250000.0), Map.of("Mushroom", 500000.0),
                Map.of("North", 300000.0, "South", 200000.0), Map.of("Online", 300000.0, "Retail", 200000.0),
                15.0, 434783.0, null);
        List<BusinessInsight> insights = engine.generateRevenueInsights(metrics);
        assertFalse(insights.isEmpty());
    }

    @Test
    void generateRevenueInsights_shouldDetectDecline() {
        RevenueMetrics metrics = new RevenueMetrics("m2", "2025-06", "2025-06-01", "2025-06-30",
                400000, 1000, 400, 300,
                Map.of("Product A", 200000.0), Map.of("Mushroom", 400000.0),
                Map.of("North", 200000.0, "South", 200000.0), Map.of("Online", 200000.0, "Retail", 200000.0),
                -5.0, 421053.0, null);
        List<BusinessInsight> insights = engine.generateRevenueInsights(metrics);
        assertTrue(insights.stream().anyMatch(i -> i.severity().equals("critical")));
    }

    @Test
    void generateCustomerInsights_shouldReturnHighChurnInsight() {
        CustomerAnalytics analytics = new CustomerAnalytics("c1", "2025-06", 1000, 50, 120,
                12.0, 5000, 800, 88.0,
                Map.of("New Growers", 400), Map.of("New Growers", 200000.0),
                4.2, 880, Map.of("North", 300));
        List<BusinessInsight> insights = engine.generateCustomerInsights(analytics);
        assertTrue(insights.stream().anyMatch(i -> "customer".equals(i.category())));
    }

    @Test
    void generateCustomerInsights_shouldNotReturnInsightForLowChurn() {
        CustomerAnalytics analytics = new CustomerAnalytics("c2", "2025-06", 1000, 50, 20,
                2.0, 5000, 800, 98.0,
                Map.of("New Growers", 400), Map.of("New Growers", 200000.0),
                4.5, 980, Map.of("North", 300));
        List<BusinessInsight> insights = engine.generateCustomerInsights(analytics);
        assertTrue(insights.isEmpty());
    }

    @Test
    void generateTrainingInsights_shouldReturnLowCompletionInsight() {
        TrainingAnalytics analytics = new TrainingAnalytics("t1", "2025-06", 100, 5, 3, 2,
                85.0, 72.0, 40, 15,
                65.0, Map.of("Module1", 80.0), Map.of("Course1", 100),
                200000, 150000, 25.0);
        List<BusinessInsight> insights = engine.generateTrainingInsights(analytics);
        assertFalse(insights.isEmpty());
    }

    @Test
    void generateCrossDomainInsights_shouldReturnCrossDomainOnly() {
        List<BusinessInsight> insights = engine.generateCrossDomainInsights();
        assertTrue(insights.stream().allMatch(i -> "cross-domain".equals(i.category())));
    }

    @Test
    void getActionableInsights_shouldOnlyReturnActionable() {
        List<BusinessInsight> insights = engine.getActionableInsights();
        assertTrue(insights.stream().allMatch(BusinessInsight::actionable));
    }

    @Test
    void generateDailyBriefing_shouldHaveRequiredKeys() {
        Map<String, Object> briefing = engine.generateDailyBriefing();
        assertTrue(briefing.containsKey("briefingDate"));
        assertTrue(briefing.containsKey("totalInsights"));
        assertTrue(briefing.containsKey("topInsights"));
        assertTrue(briefing.containsKey("priority"));
        assertTrue(briefing.containsKey("recommendedActions"));
    }

    @Test
    void getTopInsights_shouldLimitResults() {
        List<BusinessInsight> insights = engine.getTopInsights(3);
        assertEquals(3, insights.size());
    }

    @Test
    void getTopInsights_shouldBeSortedByConfidence() {
        List<BusinessInsight> insights = engine.getTopInsights(25);
        for (int i = 0; i < insights.size() - 1; i++) {
            assertTrue(insights.get(i).confidenceScore() >= insights.get(i + 1).confidenceScore());
        }
    }

    @Test
    void generateInsights_shouldFilterByCategory() {
        List<BusinessInsight> insights = engine.generateInsights("revenue", "2025-06");
        assertTrue(insights.stream().allMatch(i -> "revenue".equals(i.category())));
    }
}
