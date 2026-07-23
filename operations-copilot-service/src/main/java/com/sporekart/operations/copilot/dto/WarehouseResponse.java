package com.sporekart.operations.copilot.dto;

import java.util.List;
import java.util.Map;

public record WarehouseResponse(
    String warehouseId,
    String health,
    Map<String, Object> capacity,
    double efficiency,
    List<String> recommendations,
    List<ZoneResponse> zones
) {
    public record ZoneResponse(String zoneId, String type, double utilization, int itemCount) {}
}
