package com.sporekart.operations.copilot.dto;

import java.util.List;

public record InventoryStatusResponse(
    int totalSkuCount,
    int inStockCount,
    int lowStockCount,
    int criticalCount,
    int outOfStockCount,
    double totalInventoryValue,
    double inventoryTurnover,
    List<InventoryItemResponse> items
) {
    public record InventoryItemResponse(String sku, String productName, int currentStock, int reorderPoint, String status, String warehouseId) {}
}
