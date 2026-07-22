package com.sporekart.memory.consolidation;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ConsolidationResult(
    UUID consolidatedMemoryId,
    int sourcesCount,
    String consolidatedSummary,
    ConsolidationStrategy strategy,
    OffsetDateTime consolidatedAt
) {}
