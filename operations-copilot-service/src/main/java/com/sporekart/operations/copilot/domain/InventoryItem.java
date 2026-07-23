package com.sporekart.operations.copilot.domain;

import java.time.LocalDateTime;

public record InventoryItem(
    String sku,
    String productName,
    String category,
    int currentStock,
    int reservedStock,
    int availableStock,
    int reorderPoint,
    int safetyStock,
    int leadTimeDays,
    double unitCost,
    double inventoryValue,
    String warehouseId,
    String zoneId,
    String binId,
    LocalDateTime lastRestockedAt,
    LocalDateTime lastCountedAt,
    InventoryStatus status
) {
    public enum InventoryStatus {
        IN_STOCK, LOW_STOCK, CRITICAL, OUT_OF_STOCK, OVERSTOCKED, DISCONTINUED
    }
}
