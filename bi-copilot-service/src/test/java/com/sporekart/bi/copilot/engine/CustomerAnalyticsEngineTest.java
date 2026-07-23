package com.sporekart.bi.copilot.engine;

import com.sporekart.bi.copilot.domain.CustomerAnalytics;
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
    void getCustomerSummaryReturnsValidAnalytics() {
        CustomerAnalytics result = engine.getCustomerSummary("current");
        assertNotNull(result);
        assertTrue(result.totalCustomers() >= 0);
        assertTrue(result.newCustomers() >= 0);
        assertTrue(result.returningCustomers() >= 0);
        assertTrue(result.retentionRate() >= 0);
    }

    @Test
    void getNewCustomersReturnsTrendData() {
        List<TrendDataPoint> result = engine.getNewCustomers(6);
        assertNotNull(result);
        assertEquals(6, result.size());
        result.forEach(p -> assertTrue(p.value() >= 0));
    }

    @Test
    void getReturningCustomersReturnsCount() {
        int count = engine.getReturningCustomers("current");
        assertTrue(count >= 0);
    }

    @Test
    void getRetentionRateReturnsPercentage() {
        double rate = engine.getRetentionRate("current");
        assertTrue(rate >= 0);
        assertTrue(rate <= 100);
    }

    @Test
    void getChurnRateReturnsPercentage() {
        double rate = engine.getChurnRate("current");
        assertTrue(rate >= 0);
        assertTrue(rate <= 100);
    }

    @Test
    void getCustomerLifetimeValueReturnsPositive() {
        double clv = engine.getCustomerLifetimeValue();
        assertTrue(clv > 0);
    }

    @Test
    void getTopCustomersReturnsOrderedList() {
        var top = engine.getTopCustomers(10);
        assertNotNull(top);
        assertEquals(10, top.size());
        for (int i = 1; i < top.size(); i++) {
            assertTrue(top.get(i - 1).totalSpent() >= top.get(i).totalSpent());
        }
    }

    @Test
    void getCustomerSegmentsReturnsAllSegments() {
        Map<String, Object> segments = engine.getCustomerSegments();
        assertNotNull(segments);
        assertTrue(segments.containsKey("Home Growers"));
        assertTrue(segments.containsKey("Commercial Farmers"));
    }

    @Test
    void getRevenueBySegmentReturnsSegmentRevenue() {
        Map<String, Double> result = engine.getRevenueBySegment("current");
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void getBuyingPatternsReturnsPatternDataForSegment() {
        Map<String, Object> patterns = engine.getBuyingPatterns("Home Growers");
        assertNotNull(patterns);
        assertEquals("Home Growers", patterns.get("segment"));
        assertTrue(patterns.containsKey("customerCount"));
        assertTrue(patterns.containsKey("avgOrderCount"));
    }

    @Test
    void getCustomerTrendReturnsDataPoints() {
        List<TrendDataPoint> trend = engine.getCustomerTrend(6);
        assertNotNull(trend);
        assertEquals(6, trend.size());
    }

    @Test
    void getRepeatPurchaseRateMatchesRetentionRate() {
        double repeat = engine.getRepeatPurchaseRate("current");
        double retention = engine.getRetentionRate("current");
        assertEquals(repeat, retention, 0.01);
    }
}
