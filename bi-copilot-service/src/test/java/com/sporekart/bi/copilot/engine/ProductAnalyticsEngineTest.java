package com.sporekart.bi.copilot.engine;

import com.sporekart.bi.copilot.domain.ProductAnalytics;
import com.sporekart.bi.copilot.domain.ProductAnalytics.ProductPerformance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ProductAnalyticsEngineTest {

    private ProductAnalyticsEngine engine;

    @BeforeEach
    void setUp() {
        engine = new ProductAnalyticsEngine();
    }

    @Test
    void getProductSummaryReturnsValidAnalytics() {
        ProductAnalytics result = engine.getProductSummary("current");
        assertNotNull(result);
        assertNotNull(result.topProducts());
        assertNotNull(result.worstProducts());
        assertNotNull(result.fastMovers());
        assertNotNull(result.slowMovers());
        assertFalse(result.categoryPerformance().isEmpty());
        assertTrue(result.conversionRate() > 0);
    }

    @Test
    void getTopProductsReturnsCorrectCount() {
        List<ProductPerformance> top = engine.getTopProducts(5, "current");
        assertEquals(5, top.size());
        for (int i = 1; i < top.size(); i++) {
            assertTrue(top.get(i - 1).revenue() >= top.get(i).revenue());
        }
    }

    @Test
    void getWorstProductsReturnsCorrectCount() {
        List<ProductPerformance> worst = engine.getWorstProducts(5, "current");
        assertEquals(5, worst.size());
    }

    @Test
    void getFastMoversReturnsPositiveGrowthProducts() {
        List<ProductPerformance> fast = engine.getFastMovers(5);
        assertFalse(fast.isEmpty());
        assertEquals(5, fast.size());
        fast.forEach(p -> assertTrue(p.growth() > 5));
    }

    @Test
    void getSlowMoversReturnsSlowOrNoMovementProducts() {
        List<ProductPerformance> slow = engine.getSlowMovers(5);
        assertEquals(5, slow.size());
    }

    @Test
    void getCategoryPerformanceReturnsAllCategories() {
        Map<String, Object> perf = engine.getCategoryPerformance("current");
        assertNotNull(perf);
        assertTrue(perf.containsKey("Mushroom Products"));
        assertTrue(perf.containsKey("Training"));
    }

    @Test
    void getConversionRateReturnsPositiveDouble() {
        double rate = engine.getConversionRate("current");
        assertTrue(rate > 0);
    }

    @Test
    void getSearchTrendsReturnsTrendingProducts() {
        List<String> trends = engine.getSearchTrends(5);
        assertNotNull(trends);
        assertFalse(trends.isEmpty());
    }

    @Test
    void getWishlistTrendsReturnsWishlistedProducts() {
        List<String> trends = engine.getWishlistTrends(5);
        assertNotNull(trends);
        assertFalse(trends.isEmpty());
    }

    @Test
    void getProductProfitabilityReturnsProfitData() {
        Map<String, Object> profit = engine.getProductProfitability("current");
        assertNotNull(profit);
        assertFalse(profit.isEmpty());
        profit.values().forEach(v -> {
            Map<String, Object> pm = (Map<String, Object>) v;
            assertTrue(pm.containsKey("revenue"));
            assertTrue(pm.containsKey("margin"));
        });
    }

    @Test
    void getProductGrowthReturnsTrendForProduct() {
        var trend = engine.getProductGrowth("Fresh Oyster Mushroom", 6);
        assertNotNull(trend);
        assertEquals(6, trend.size());
    }
}
