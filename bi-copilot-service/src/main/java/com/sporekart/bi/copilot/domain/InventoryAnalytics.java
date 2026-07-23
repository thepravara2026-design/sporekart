package com.sporekart.bi.copilot.domain;

import java.util.List;

public record InventoryAnalytics(
    String period,
    int totalStock,
    int lowStockItems,
    int deadStockItems,
    List<InventoryItem> fastMoving,
    List<InventoryItem> slowMoving,
    double turnoverRate,
    List<RestockingItem> restockingPriority,
    double inventoryRisk
) {
    public record InventoryItem(String productId, String name, int currentStock, int reorderLevel, int soldLastMonth, double turnoverDays) {}

    public record RestockingItem(String productId, String name, int currentStock, int recommendedOrder, String urgency) {}
}
