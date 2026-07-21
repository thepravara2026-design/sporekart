package com.sporekart.ai.providers.availability;

import java.time.Instant;
import java.util.Map;

public record AvailabilitySnapshot(
    String snapshotId,
    Instant timestamp,
    Map<String, Boolean> availabilityMap,
    int totalProviders,
    int availableCount,
    int unavailableCount,
    double overallAvailability
) {}
