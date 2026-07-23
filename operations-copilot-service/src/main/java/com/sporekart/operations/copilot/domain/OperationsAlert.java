package com.sporekart.operations.copilot.domain;

import java.time.LocalDateTime;

public record OperationsAlert(
    String alertId,
    String title,
    String message,
    AlertCategory category,
    AlertPriority priority,
    boolean actionable,
    String suggestedAction,
    String entityId,
    LocalDateTime createdAt,
    boolean acknowledged
) {
    public enum AlertCategory {
        INVENTORY, ORDER, WAREHOUSE, SHIPMENT, PROCUREMENT, SUPPLY_CHAIN, KPI, COST
    }
    public enum AlertPriority {
        LOW, MEDIUM, HIGH, URGENT
    }
}
