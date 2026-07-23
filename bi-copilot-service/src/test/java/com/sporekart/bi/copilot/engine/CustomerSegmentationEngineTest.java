package com.sporekart.bi.copilot.engine;

import com.sporekart.bi.copilot.domain.CustomerSegment;
import com.sporekart.bi.copilot.domain.TrendDataPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CustomerSegmentationEngineTest {

    private CustomerSegmentationEngine engine;

    @BeforeEach
    void setUp() {
        engine = new CustomerSegmentationEngine();
    }

    @Test
    void segmentCustomers_shouldReturnSixSegments() {
        List<CustomerSegment> segments = engine.segmentCustomers();
        assertEquals(6, segments.size());
    }

    @Test
    void segmentCustomers_shouldHaveAllRequiredFields() {
        List<CustomerSegment> segments = engine.segmentCustomers();
        assertTrue(segments.stream().allMatch(s -> s.segmentId() != null));
        assertTrue(segments.stream().allMatch(s -> s.name() != null));
        assertTrue(segments.stream().allMatch(s -> s.characteristics() != null));
        assertTrue(segments.stream().allMatch(s -> s.recommendedStrategies() != null));
    }

    @Test
    void getSegmentById_shouldReturnMatchingSegment() {
        CustomerSegment segment = engine.getSegmentById("SEG_HIGH_VALUE");
        assertNotNull(segment);
        assertEquals("High-Value Growers", segment.name());
    }

    @Test
    void getSegmentById_shouldThrowForUnknownId() {
        assertThrows(IllegalArgumentException.class, () -> engine.getSegmentById("UNKNOWN"));
    }

    @Test
    void getSegmentRecommendations_shouldReturnStrategies() {
        List<String> recommendations = engine.getSegmentRecommendations("SEG_NEW_GROWER");
        assertFalse(recommendations.isEmpty());
    }

    @Test
    void getSegmentRecommendations_shouldThrowForUnknown() {
        assertThrows(IllegalArgumentException.class, () -> engine.getSegmentRecommendations("UNKNOWN"));
    }

    @Test
    void getHighValueSegments_shouldFilterByRevenue() {
        List<CustomerSegment> highValue = engine.getHighValueSegments();
        assertFalse(highValue.isEmpty());
        assertTrue(highValue.stream().allMatch(s -> s.averageRevenue() > 2000));
    }

    @Test
    void getHighValueSegments_shouldBeSortedDescending() {
        List<CustomerSegment> highValue = engine.getHighValueSegments();
        for (int i = 0; i < highValue.size() - 1; i++) {
            assertTrue(highValue.get(i).averageRevenue() >= highValue.get(i + 1).averageRevenue());
        }
    }

    @Test
    void getAtRiskSegments_shouldFilterByChurn() {
        List<CustomerSegment> atRisk = engine.getAtRiskSegments();
        assertFalse(atRisk.isEmpty());
        assertTrue(atRisk.stream().allMatch(s -> s.churnRate() > 10));
    }

    @Test
    void getAtRiskSegments_shouldIncludeAtRiskSegment() {
        List<CustomerSegment> atRisk = engine.getAtRiskSegments();
        assertTrue(atRisk.stream().anyMatch(s -> s.segmentId().equals("SEG_AT_RISK")));
    }

    @Test
    void getSegmentRevenueContribution_shouldSumTo100() {
        Map<String, Double> contribution = engine.getSegmentRevenueContribution();
        double sum = contribution.values().stream().mapToDouble(Double::doubleValue).sum();
        assertEquals(100.0, sum, 1.0);
    }

    @Test
    void analyzeSegmentTrends_shouldReturnPoints() {
        List<TrendDataPoint> trends = engine.analyzeSegmentTrends("SEG_HIGH_VALUE", 6);
        assertEquals(7, trends.size());
    }

    @Test
    void recommendMarketingStrategy_shouldReturnChannelsAndMessaging() {
        Map<String, Object> strategy = engine.recommendMarketingStrategy("SEG_COMMERCIAL");
        assertTrue(strategy.containsKey("channels"));
        assertTrue(strategy.containsKey("messaging"));
        assertTrue(strategy.containsKey("offers"));
    }

    @Test
    void recommendMarketingStrategy_shouldReturnDefaultForUnknown() {
        Map<String, Object> strategy = engine.recommendMarketingStrategy("UNKNOWN_SLUG");
        List<String> channels = (List<String>) strategy.get("channels");
        assertTrue(channels.contains("Email"));
    }
}
