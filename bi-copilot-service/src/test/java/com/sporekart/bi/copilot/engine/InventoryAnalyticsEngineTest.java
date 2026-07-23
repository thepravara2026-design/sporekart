package com.sporekart.bi.copilot.engine;

import com.sporekart.bi.copilot.domain.InventoryAnalytics;
import com.sporekart.bi.copilot.domain.InventoryAnalytics.InventoryItem;
import com.sporekart.bi.copilot.domain.InventoryAnalytics.RestockingItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InventoryAnalyticsEngineTest {

    private InventoryAnalyticsEngine engine;

    @BeforeEach
    void setUp() {
        engine = new InventoryAnalyticsEngine();
    }

    @Test
    void getInventorySummaryReturnsValidAnalytics() {
        InventoryAnalytics result = engine.getInventorySummary("current");
        assertNotNull(result);
        assertTrue(result.totalStock() >= 0);
        assertTrue(result.lowStockItems() >= 0);
        assertTrue(result.deadStockItems() >= 0);
        assertNotNull(result.fastMoving());
        assertNotNull(result.restockingPriority());
        assertTrue(result.inventoryRisk() >= 0);
    }

    @Test
    void getLowStockItemsReturnsItemsBelowReorderLevel() {
        List<InventoryItem> lowStock = engine.getLowStockItems();
        assertNotNull(lowStock);
        lowStock.forEach(item ->
                assertTrue(item.currentStock() <= item.reorderLevel())
        );
    }

    @Test
    void getDeadStockItemsReturnsItemsWithNoSales() {
        List<InventoryItem> deadStock = engine.getDeadStockItems();
        assertNotNull(deadStock);
    }

    @Test
    void getFastMovingItemsReturnsHighVelocityItems() {
        List<InventoryItem> fast = engine.getFastMovingItems(5);
        assertFalse(fast.isEmpty());
        assertEquals(5, fast.size());
    }

    @Test
    void getSlowMovingItemsReturnsLowVelocityItems() {
        List<InventoryItem> slow = engine.getSlowMovingItems(5);
        assertEquals(5, slow.size());
    }

    @Test
    void getRestockingPriorityReturnsPrioritizedItems() {
        List<RestockingItem> priority = engine.getRestockingPriority();
        assertNotNull(priority);
        assertFalse(priority.isEmpty());
    }

    @Test
    void getInventoryRiskReturnsRiskScore() {
        double risk = engine.getInventoryRisk();
        assertTrue(risk >= 0);
        assertTrue(risk <= 100);
    }

    @Test
    void getStockForecastReturnsProjectedStock() {
        var forecast = engine.getStockForecast("Fresh Oyster Mushroom", 4);
        assertNotNull(forecast);
        assertFalse(forecast.isEmpty());
        assertTrue(forecast.size() <= 5);
    }

    @Test
    void getStockForecastReturnsEmptyForUnknownProduct() {
        var forecast = engine.getStockForecast("UNKNOWN", 4);
        assertTrue(forecast.isEmpty());
    }

    @Test
    void getInventoryTurnoverReturnsRatio() {
        double turnover = engine.getInventoryTurnover("current");
        assertTrue(turnover >= 0);
    }

    @Test
    void getInventoryByCategoryReturnsCategorizedData() {
        Map<String, Object> byCat = engine.getInventoryByCategory();
        assertNotNull(byCat);
        assertFalse(byCat.isEmpty());
    }
}
