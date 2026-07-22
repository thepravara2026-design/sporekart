package com.sporekart.memory.summarization;

import com.sporekart.memory.domain.MemorySummaryType;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record SummaryResult(
    UUID summaryId,
    UUID memoryId,
    String summaryText,
    MemorySummaryType summaryType,
    int version,
    OffsetDateTime createdAt
) {}
