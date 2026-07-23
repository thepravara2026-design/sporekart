package com.sporekart.operations.copilot.domain;

import java.util.List;

public record WarehouseZone(
    String zoneId,
    String zoneType,
    double capacity,
    double usedCapacity,
    int bins,
    int usedBins,
    List<StorageBin> binsDetail
) {}
