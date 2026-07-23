package com.sporekart.bi.copilot.engine;

import com.sporekart.bi.copilot.domain.CustomerAnalytics;
import com.sporekart.bi.copilot.domain.CustomerSegment;
import com.sporekart.bi.copilot.domain.TrendDataPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CustomerAnalyticsEngineTest {

    private CustomerAnalyticsEngine engine;

    @BeforeEach
    void setUp() {
        engine = new CustomerAnalyticsEngine();
    }

    @Test
    void getCustomerSummary_shouldReturnAnalyticsForValidPeriod() {
        CustomerAnalytics analytics = engine.getCustomerSummary("2025-06");
        assertNotNull(analytics);
        assertEquals("2025-06", analytics.period());
        assertTrue(analytics.totalCustomers() > 0);
    }

    @Test
    void getCustomerSummary_shouldReturnLatestForNullPeriod() {
        CustomerAnalytics analytics = engine.getCustomerSummary(null);
        assertNotNull(analytics);
        assertTrue(analytics.totalCustomers() > 0);
    }

    @Test
    void getCustomerSegments_shouldReturnFourSegments() {
        List<CustomerSegment> segments = engine.getCustomerSegments();
        assertEquals(4, segments.size());
    }

    @Test
    void getCustomerSegments_shouldHaveNamesAndCounts() {
        List<CustomerSegment> segments = engine.getCustomerSegments();
        assertTrue(segments.stream().allMatch(s -> s.name() != null && !s.name().isBlank()));
        assertTrue(segments.stream().allMatch(s -> s.customerCount() >= 0));
    }

    @Test
    void getChurnAnalysis_shouldReturnTopLevelKeys() {
        Map<String, Object> analysis = engine.getChurnAnalysis("2025-06");
        assertNotNull(analysis);
        assertTrue(analysis.containsKey("churnRate"));
        assertTrue(analysis.containsKey("churnedCustomers"));
        assertTrue(analysis.containsKey("retentionRate"));
        assertTrue(analysis.containsKey("churnBySegment"));
        assertTrue(analysis.containsKey("topChurnReasons"));
    }

    @Test
    void getChurnAnalysis_shouldReturnEmptyForUnknownPeriod() {
        Map<String, Object> analysis = engine.getChurnAnalysis("2099-01");
        assertTrue(analysis.isEmpty());
    }

    @Test
    void getCustomerLifetimeValue_shouldReturnPositive() {
        double clv = engine.getCustomerLifetimeValue("2025-06");
        assertTrue(clv > 0);
    }

    @Test
    void getCustomerLifetimeValue_shouldReturnZeroForUnknownPeriod() {
        double clv = engine.getCustomerLifetimeValue("2099-01");
        assertEquals(0, clv);
    }

    @Test
    void getRetentionRate_shouldReturnBetweenZeroAndHundred() {
        double rate = engine.getRetentionRate("2025-06");
        assertTrue(rate > 0 && rate <= 100);
    }

    @Test
    void getRetentionRate_shouldReturnZeroForUnknownPeriod() {
        double rate = engine.getRetentionRate("2099-01");
        assertEquals(0, rate);
    }

    @Test
    void getCustomerSatisfactionTrend_shouldReturnRequestedCount() {
        List<TrendDataPoint> trend = engine.getCustomerSatisfactionTrend(5);
        assertEquals(5, trend.size());
    }

    @Test
    void getCustomerSatisfactionTrend_shouldHaveSatisfactionMetric() {
        List<TrendDataPoint> trend = engine.getCustomerSatisfactionTrend(3);
        assertTrue(trend.stream().allMatch(t -> "satisfaction".equals(t.metric())));
    }

    @Test
    void getActiveCustomerCount_shouldReturnPositive() {
        int count = engine.getActiveCustomerCount();
        assertTrue(count > 0);
    }

    @Test
    void getCustomerAcquisitionTrend_shouldReturnRequestedCount() {
        List<TrendDataPoint> trend = engine.getCustomerAcquisitionTrend(4);
        assertEquals(4, trend.size());
        assertTrue(trend.stream().allMatch(t -> "acquisition".equals(t.metric())));
    }

    @Test
    void getCustomerSegmentation_shouldReturnSegments() {
        List<CustomerSegment> segments = engine.getCustomerSegmentation();
        assertFalse(segments.isEmpty());
    }
}
