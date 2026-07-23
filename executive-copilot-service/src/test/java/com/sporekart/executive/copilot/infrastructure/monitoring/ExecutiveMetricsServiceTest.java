package com.sporekart.executive.copilot.infrastructure.monitoring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExecutiveMetricsServiceTest {

    private ExecutiveMetricsService metricsService;

    @BeforeEach
    void setUp() {
        metricsService = new ExecutiveMetricsService();
        metricsService.reset();
    }

    @Test
    void recordQueryLatency_shouldUpdateMetrics() {
        metricsService.recordQueryLatency(100);
        metricsService.recordQueryLatency(200);
        assertEquals(150.0, metricsService.getAverageQueryLatencyMs());
    }

    @Test
    void recordQueryLatency_zeroCount_shouldReturnZero() {
        assertEquals(0.0, metricsService.getAverageQueryLatencyMs());
    }

    @Test
    void recordForecastExecution_shouldUpdateMetrics() {
        metricsService.recordForecastExecution(500);
        assertEquals(500.0, metricsService.getAverageForecastExecutionMs());
    }

    @Test
    void recordReportGeneration_shouldUpdateMetrics() {
        metricsService.recordReportGeneration(1000);
        assertEquals(1000.0, metricsService.getAverageReportGenerationMs());
    }

    @Test
    void recordRecommendationAccuracy_shouldUpdateMetrics() {
        metricsService.recordRecommendationAccuracy(true);
        metricsService.recordRecommendationAccuracy(false);
        metricsService.recordRecommendationAccuracy(true);
        assertEquals(66.67, metricsService.getRecommendationAccuracy(), 0.01);
    }

    @Test
    void recordRecommendationAccuracy_noData_shouldReturnZero() {
        assertEquals(0.0, metricsService.getRecommendationAccuracy());
    }

    @Test
    void recordPromptUsage_shouldIncrement() {
        metricsService.recordPromptUsage();
        metricsService.recordPromptUsage();
        assertEquals(2, metricsService.getPromptUsageCount());
    }

    @Test
    void recordTokenCost_shouldAccumulate() {
        metricsService.recordTokenCost(1000);
        metricsService.recordTokenCost(500);
        assertEquals(1500, metricsService.getTotalTokenCost());
    }

    @Test
    void recordDashboardUsage_shouldTrackTypes() {
        metricsService.recordDashboardUsage("executive");
        metricsService.recordDashboardUsage("today");
        metricsService.recordDashboardUsage("executive");

        var usage = metricsService.getDashboardUsage();
        assertEquals(2, usage.get("executive"));
        assertEquals(1, usage.get("today"));
    }

    @Test
    void getAllMetrics_shouldReturnAllCategories() {
        metricsService.recordQueryLatency(100);
        metricsService.recordDashboardUsage("executive");

        var all = metricsService.getAllMetrics();
        assertTrue(all.containsKey("queryLatency"));
        assertTrue(all.containsKey("forecastExecution"));
        assertTrue(all.containsKey("reportGeneration"));
        assertTrue(all.containsKey("recommendationAccuracy"));
        assertTrue(all.containsKey("promptUsage"));
        assertTrue(all.containsKey("tokenCost"));
        assertTrue(all.containsKey("dashboardUsage"));
    }

    @Test
    void getAllMetrics_shouldContainSubMetrics() {
        metricsService.recordQueryLatency(100);
        var queryLatency = (java.util.Map<String, Object>) metricsService.getAllMetrics().get("queryLatency");
        assertEquals(100.0, queryLatency.get("avgMs"));
        assertEquals(1, queryLatency.get("total"));
    }

    @Test
    void reset_shouldClearAllMetrics() {
        metricsService.recordQueryLatency(100);
        metricsService.recordPromptUsage();
        metricsService.reset();

        assertEquals(0.0, metricsService.getAverageQueryLatencyMs());
        assertEquals(0, metricsService.getPromptUsageCount());
        assertTrue(metricsService.getDashboardUsage().isEmpty());
    }
}
