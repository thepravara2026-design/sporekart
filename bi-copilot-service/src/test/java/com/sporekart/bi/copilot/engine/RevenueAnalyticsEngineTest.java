package com.sporekart.bi.copilot.engine;

import com.sporekart.bi.copilot.domain.RevenueAnalytics;
import com.sporekart.bi.copilot.domain.TrendDataPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RevenueAnalyticsEngineTest {

    private RevenueAnalyticsEngine engine;

    @BeforeEach
    void setUp() {
        engine = new RevenueAnalyticsEngine();
    }

    @Test
    void getRevenueSummaryReturnsValidAnalytics() {
        RevenueAnalytics result = engine.getRevenueSummary("current");
        assertNotNull(result);
        assertNotNull(result.period());
        assertTrue(result.grossRevenue() > 0);
        assertTrue(result.netRevenue() > 0);
        assertTrue(result.averageOrderValue() > 0);
        assertFalse(result.byCategory().isEmpty());
        assertFalse(result.byProduct().isEmpty());
        assertFalse(result.byRegion().isEmpty());
        assertFalse(result.byCustomerSegment().isEmpty());
        assertFalse(result.byChannel().isEmpty());
        assertTrue(result.orderCount() > 0);
    }

    @Test
    void getRevenueByCategoryReturnsAllCategories() {
        Map<String, Object> result = engine.getRevenueByCategory("current");
        assertNotNull(result);
        assertTrue(result.containsKey("Mushroom Products"));
        assertTrue(result.containsKey("Training"));
        assertTrue(result.containsKey("Equipment"));
    }

    @Test
    void getRevenueByProductReturnsProductsForCategory() {
        Map<String, Double> result = engine.getRevenueByProduct("current", "Mushroom Products");
        assertNotNull(result);
        assertFalse(result.isEmpty());
        result.values().forEach(v -> assertTrue(v > 0));
    }

    @Test
    void getRevenueByProductReturnsAllWhenCategoryNull() {
        Map<String, Double> all = engine.getRevenueByProduct("current", null);
        Map<String, Double> filtered = engine.getRevenueByProduct("current", "Mushroom Products");
        assertTrue(all.size() >= filtered.size());
    }

    @Test
    void getRevenueByRegionReturnsRegionData() {
        Map<String, Object> result = engine.getRevenueByRegion("current");
        assertNotNull(result);
        assertTrue(result.containsKey("Maharashtra"));
    }

    @Test
    void getRevenueBySegmentReturnsSegmentMap() {
        Map<String, Double> result = engine.getRevenueBySegment("current");
        assertNotNull(result);
        assertTrue(result.containsKey("Home Growers"));
    }

    @Test
    void getRevenueByChannelReturnsChannelMap() {
        Map<String, Double> result = engine.getRevenueByChannel("current");
        assertNotNull(result);
        assertTrue(result.containsKey("Online Direct"));
    }

    @Test
    void getRevenueByTrainingReturnsTrainingMap() {
        Map<String, Double> result = engine.getRevenueByTraining("current");
        assertNotNull(result);
        assertTrue(result.containsKey("Mushroom Cultivation 101"));
    }

    @Test
    void getRevenueGrowthReturnsPercentage() {
        double growth = engine.getRevenueGrowth("current", "current");
        assertEquals(0.0, growth, 0.01);
    }

    @Test
    void getAverageOrderValueReturnsPositiveValue() {
        double aov = engine.getAverageOrderValue("current");
        assertTrue(aov > 0);
    }

    @Test
    void comparePeriodsReturnsComparisonData() {
        Map<String, Object> result = engine.comparePeriods("current", "current");
        assertNotNull(result);
        assertTrue(result.containsKey("period1"));
        assertTrue(result.containsKey("period2"));
        assertTrue(result.containsKey("revenue1"));
        assertTrue(result.containsKey("revenue2"));
        assertTrue(result.containsKey("growth"));
    }

    @Test
    void getRevenueTrendReturnsDataPoints() {
        List<TrendDataPoint> trend = engine.getRevenueTrend(6);
        assertNotNull(trend);
        assertEquals(6, trend.size());
        trend.forEach(p -> assertTrue(p.value() > 0));
    }

    @Test
    void getRefundAnalysisReturnsRefundData() {
        Map<String, Object> result = engine.getRefundAnalysis("current");
        assertNotNull(result);
        assertTrue((double) result.get("totalRefunds") >= 0);
        assertTrue((int) result.get("refundCount") >= 0);
        assertTrue(result.containsKey("byCategory"));
    }

    @Test
    void getRevenueSummaryBySpecificPeriod() {
        RevenueAnalytics result = engine.getRevenueSummary("current");
        assertNotNull(result);
        assertNotNull(result.period());
    }
}
