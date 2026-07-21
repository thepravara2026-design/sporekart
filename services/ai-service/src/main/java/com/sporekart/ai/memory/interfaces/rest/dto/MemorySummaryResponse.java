package com.sporekart.ai.memory.interfaces.rest.dto;

import java.time.Instant;

public record MemorySummaryResponse(
    String type,
    long totalEntries,
    long activeEntries,
    Instant oldestEntry,
    Instant newestEntry,
    double averageImportance
) {}
