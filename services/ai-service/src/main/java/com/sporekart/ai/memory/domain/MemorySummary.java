package com.sporekart.ai.memory.domain;

import java.time.Instant;

public record MemorySummary(
    MemoryType type,
    long totalEntries,
    long activeEntries,
    Instant oldestEntry,
    Instant newestEntry,
    double averageImportance
) {}
