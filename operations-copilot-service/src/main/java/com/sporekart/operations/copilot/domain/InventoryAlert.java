package com.sporekart.operations.copilot.domain;

import java.time.LocalDateTime;

public record InventoryAlert(
    String alertId,
    String sku,
    String productName,
    AlertType type,
    AlertSeverity severity,
    String message,
    int currentStock,
    int threshold,
    String warehouseId,
    LocalDateTime createdAt,
    boolean acknowledged
) {
    public enum AlertType {
        LOW_STOCK, CRITICAL_STOCK, OUT_OF_STOCK, OVERSTOCK, DEAD_STOCK,
        STOCK_AGING, INVENTORY_DISCREPANCY
    }
    public enum AlertSeverity {
        INFO, WARNING, CRITICAL, BLOCKER
    }
}
