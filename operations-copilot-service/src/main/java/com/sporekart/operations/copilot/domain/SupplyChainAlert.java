package com.sporekart.operations.copilot.domain;

import java.time.LocalDateTime;
import java.util.List;

public record SupplyChainAlert(
    String alertId,
    String title,
    String description,
    AlertCategory category,
    AlertSeverity severity,
    String affectedEntity,
    double impactScore,
    List<String> recommendedActions,
    LocalDateTime detectedAt,
    boolean resolved
) {
    public enum AlertCategory {
        SUPPLY_DELAY, VENDOR_RISK, PROCUREMENT_DELAY, INVENTORY_SHORTAGE,
        WAREHOUSE_CONGESTION, SHIPPING_DELAY, OPERATIONAL_FAILURE, CAPACITY_ISSUE
    }
    public enum AlertSeverity {
        LOW, MEDIUM, HIGH, CRITICAL
    }
}
