package com.sporekart.bi.copilot.engine;

import com.sporekart.bi.copilot.domain.RevenueMetrics;
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
    void getRevenueSummary_shouldReturnMetricsForValidPeriod() {
        RevenueMetrics metrics = engine.getRevenueSummary("2025-06");
        assertNotNull(metrics);
        assertEquals("2025-06", metrics.period());
        assertTrue(metrics.totalRevenue() > 0);
        assertTrue(metrics.averageOrderValue() > 0);
    }

    @Test
    void getRevenueSummary_shouldReturnLatestForNullPeriod() {
        RevenueMetrics metrics = engine.getRevenueSummary(null);
        assertNotNull(metrics);
        assertTrue(metrics.totalRevenue() > 0);
    }

    @Test
    void getRevenueByProduct_shouldReturnFilteredByCategory() {
        Map<String, Double> byProduct = engine.getRevenueByProduct("2025-06", "Oyster");
        assertNotNull(byProduct);
        assertFalse(byProduct.isEmpty());
        assertTrue(byProduct.keySet().stream().anyMatch(k -> k.toLowerCase().contains("oyster")));
    }

    @Test
    void getRevenueByProduct_shouldReturnAllWhenCategoryNull() {
        Map<String, Double> byProduct = engine.getRevenueByProduct("2025-06", null);
        assertNotNull(byProduct);
        assertFalse(byProduct.isEmpty());
    }

    @Test
    void getRevenueByRegion_shouldReturnNonEmpty() {
        Map<String, Double> byRegion = engine.getRevenueByRegion("2025-06");
        assertNotNull(byRegion);
        assertEquals(5, byRegion.size());
    }

    @Test
    void getRevenueByRegion_shouldReturnEmptyForUnknownPeriod() {
        Map<String, Double> byRegion = engine.getRevenueByRegion("2099-01");
        assertTrue(byRegion.isEmpty());
    }

    @Test
    void getRevenueByChannel_shouldReturnFourChannels() {
        Map<String, Double> byChannel = engine.getRevenueByChannel("2025-06");
        assertNotNull(byChannel);
        assertEquals(4, byChannel.size());
    }

    @Test
    void getRevenueTrend_shouldReturnRequestedMonths() {
        List<TrendDataPoint> trend = engine.getRevenueTrend(6);
        assertEquals(6, trend.size());
    }

    @Test
    void getRevenueTrend_shouldNotExceedDataSize() {
        List<TrendDataPoint> trend = engine.getRevenueTrend(100);
        assertTrue(trend.size() <= 12);
        assertFalse(trend.isEmpty());
    }

    @Test
    void getAverageOrderValue_shouldReturnPositive() {
        double aov = engine.getAverageOrderValue("2025-06");
        assertTrue(aov > 0);
    }

    @Test
    void getAverageOrderValue_shouldReturnZeroForUnknownPeriod() {
        double aov = engine.getAverageOrderValue("2099-01");
        assertEquals(0, aov);
    }

    @Test
    void getTopProducts_shouldReturnSortedByRevenue() {
        List<Map<String, Object>> top = engine.getTopProducts(3, "2025-06");
        assertEquals(3, top.size());
        assertTrue((Double) top.get(0).get("revenue") >= (Double) top.get(1).get("revenue"));
    }

    @Test
    void getTopProducts_shouldHandleLimitGreaterThanProducts() {
        List<Map<String, Object>> top = engine.getTopProducts(50, "2025-06");
        assertFalse(top.isEmpty());
    }

    @Test
    void getRevenuePerCustomer_shouldReturnPositive() {
        double rpc = engine.getRevenuePerCustomer("2025-06");
        assertTrue(rpc > 0);
    }

    @Test
    void getGrowthRate_shouldReturnPositiveForLaterPeriod() {
        double growth = engine.getGrowthRate("2025-06", "2025-01");
        assertTrue(growth > 0);
    }

    @Test
    void getGrowthRate_shouldReturnZeroForUnknownPeriod() {
        double growth = engine.getGrowthRate("2099-01", "2025-01");
        assertEquals(0, growth);
    }
}
