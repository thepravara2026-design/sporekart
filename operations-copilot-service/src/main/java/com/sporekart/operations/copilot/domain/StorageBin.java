package com.sporekart.operations.copilot.domain;

public record StorageBin(
    String binId,
    String zoneId,
    double capacity,
    double usedCapacity,
    String currentSku,
    int itemCount,
    double utilizationPct
) {}
