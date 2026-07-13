package com.sporekart.ai.risk.application;

import static org.junit.jupiter.api.Assertions.*;
import com.sporekart.ai.risk.domain.RecommendationType;
import com.sporekart.ai.risk.domain.RiskLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RiskMetricsServiceImplTest {

    private RiskMetricsServiceImpl metricsService;

    @BeforeEach
    void setUp() {
        metricsService = new RiskMetricsServiceImpl();
    }

    @Test
    void initialMetricsShouldBeZero() {
        assertEquals(0, metricsService.getTotalAssessments());
        assertEquals(0.0, metricsService.getAverageRiskScore());
        assertTrue(metricsService.getRiskDistribution().isEmpty());
        assertTrue(metricsService.getRecommendationCounts().isEmpty());
    }

    @Test
    void recordAssessmentShouldIncrementCount() {
        metricsService.recordAssessment(RiskLevel.HIGH);
        assertEquals(1, metricsService.getTotalAssessments());
    }

    @Test
    void recordAssessmentShouldUpdateDistribution() {
        metricsService.recordAssessment(RiskLevel.LOW);
        metricsService.recordAssessment(RiskLevel.HIGH);
        metricsService.recordAssessment(RiskLevel.HIGH);

        var distribution = metricsService.getRiskDistribution();
        assertEquals(1L, distribution.get("LOW"));
        assertEquals(2L, distribution.get("HIGH"));
    }

    @Test
    void recordRecommendationShouldUpdateCounts() {
        metricsService.recordRecommendation(RecommendationType.PROCEED);
        metricsService.recordRecommendation(RecommendationType.BLOCK_EXECUTION);
        metricsService.recordRecommendation(RecommendationType.PROCEED);

        var counts = metricsService.getRecommendationCounts();
        assertEquals(2L, counts.get("PROCEED"));
        assertEquals(1L, counts.get("BLOCK_EXECUTION"));
    }

    @Test
    void recordTrustScoreShouldUpdateTrends() {
        metricsService.recordTrustScore(80.0);
        metricsService.recordTrustScore(90.0);

        var trends = metricsService.getTrustTrends();
        assertTrue(trends.containsKey("average"));
        assertTrue(trends.containsKey("count"));
        assertEquals(2.0, trends.get("count"));
    }

    @Test
    void recordConfidenceShouldUpdateTrends() {
        metricsService.recordConfidence(70.0);
        metricsService.recordConfidence(80.0);
        metricsService.recordConfidence(90.0);

        var trends = metricsService.getConfidenceTrends();
        assertEquals(3.0, trends.get("count"));
    }

    @Test
    void recordLatencyShouldAccumulate() {
        metricsService.recordLatency(100);
        metricsService.recordLatency(200);
        metricsService.recordLatency(300);

        assertTrue(metricsService.getStatistics().containsKey("averageLatencyMs"));
    }

    @Test
    void getStatisticsShouldReturnAllValues() {
        metricsService.recordAssessment(RiskLevel.LOW);
        metricsService.recordAssessment(RiskLevel.CRITICAL);
        metricsService.recordRecommendation(RecommendationType.PROCEED);
        metricsService.recordTrustScore(75.0);
        metricsService.recordConfidence(65.0);
        metricsService.recordLatency(150);

        var stats = metricsService.getStatistics();

        assertTrue(stats.containsKey("totalAssessments"));
        assertTrue(stats.containsKey("averageRiskScore"));
        assertTrue(stats.containsKey("riskDistribution"));
        assertTrue(stats.containsKey("trustTrends"));
        assertTrue(stats.containsKey("confidenceTrends"));
        assertTrue(stats.containsKey("recommendationCounts"));
        assertTrue(stats.containsKey("averageLatencyMs"));
    }

    @Test
    void recordMultipleAssessmentsShouldAggregateCorrectly() {
        metricsService.recordAssessment(RiskLevel.LOW);
        metricsService.recordAssessment(RiskLevel.MEDIUM);
        metricsService.recordAssessment(RiskLevel.HIGH);
        metricsService.recordAssessment(RiskLevel.CRITICAL);
        metricsService.recordAssessment(RiskLevel.CRITICAL);

        assertEquals(5, metricsService.getTotalAssessments());

        var distribution = metricsService.getRiskDistribution();
        assertEquals(1L, distribution.get("LOW"));
        assertEquals(1L, distribution.get("MEDIUM"));
        assertEquals(1L, distribution.get("HIGH"));
        assertEquals(2L, distribution.get("CRITICAL"));
    }

    @Test
    void getTrustTrendsShouldReturnAverage() {
        metricsService.recordTrustScore(80.0);
        metricsService.recordTrustScore(90.0);

        var trends = metricsService.getTrustTrends();
        assertEquals(85.0, trends.get("average"), 0.01);
    }

    @Test
    void getConfidenceTrendsShouldReturnAverage() {
        metricsService.recordConfidence(60.0);
        metricsService.recordConfidence(80.0);

        var trends = metricsService.getConfidenceTrends();
        assertEquals(70.0, trends.get("average"), 0.01);
    }
}
