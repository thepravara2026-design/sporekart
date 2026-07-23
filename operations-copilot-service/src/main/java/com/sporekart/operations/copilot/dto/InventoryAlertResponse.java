package com.sporekart.operations.copilot.dto;

import java.util.List;

public record InventoryAlertResponse(
    int totalAlerts,
    int criticalAlerts,
    int warningAlerts,
    List<AlertItem> alerts
) {
    public record AlertItem(String alertId, String sku, String productName, String type, String severity, String message) {}
}
