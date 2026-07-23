package com.sporekart.operations.copilot.domain;

import java.util.List;

public record Warehouse(
    String warehouseId,
    String name,
    String location,
    String region,
    double totalCapacity,
    double usedCapacity,
    double availableCapacity,
    int totalBins,
    int usedBins,
    List<WarehouseZone> zones,
    WarehouseHealth health
) {
    public enum WarehouseHealth {
        HEALTHY, WARNING, CRITICAL
    }
}
