package com.sporekart.operations.copilot.engine;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class InventoryEngineTest {

    @InjectMocks
    private InventoryEngine inventoryEngine;

    @Test
    void testGetInventoryStatus() {
        var response = inventoryEngine.getInventoryStatus("WH-MAIN", null);
        assertNotNull(response);
        assertTrue(response.totalSkuCount() > 0);
        assertTrue(response.totalInventoryValue() > 0);
    }

    @Test
    void testGetInventoryStatusWithCategory() {
        var response = inventoryEngine.getInventoryStatus("WH-MAIN", "Spawn");
        assertNotNull(response);
        assertTrue(response.totalSkuCount() > 0);
    }

    @Test
    void testGetAlerts() {
        var alerts = inventoryEngine.getAlerts("WH-MAIN");
        assertNotNull(alerts);
        assertTrue(alerts.totalAlerts() > 0);
    }

    @Test
    void testGetItemDetail() {
        var item = inventoryEngine.getItemDetail("MUSH-001");
        assertNotNull(item);
        assertEquals("MUSH-001", item.sku());
    }

    @Test
    void testGetLowStockItems() {
        var items = inventoryEngine.getLowStockItems("WH-MAIN");
        assertNotNull(items);
        assertFalse(items.isEmpty());
    }

    @Test
    void testCalculateInventoryRisk() {
        var risk = inventoryEngine.calculateInventoryRisk();
        assertNotNull(risk);
        assertTrue(risk.containsKey("overallRiskScore"));
    }

    @Test
    void testGenerateRestockSuggestions() {
        var suggestions = inventoryEngine.generateRestockSuggestions("WH-MAIN");
        assertNotNull(suggestions);
        assertFalse(suggestions.isEmpty());
    }

    @Test
    void testCalculateSafetyStock() {
        var safetyStock = inventoryEngine.calculateSafetyStock(50, 7, 95);
        assertTrue(safetyStock > 0);
    }

    @Test
    void testCalculateReorderPoint() {
        var reorderPoint = inventoryEngine.calculateReorderPoint(50, 7, 100);
        assertEquals(450, reorderPoint);
    }
}
