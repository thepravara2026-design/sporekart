package com.sporekart.ai.memory.domain;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public record MemoryQuery(
    Optional<String> agentId,
    Optional<String> sessionId,
    Optional<String> userId,
    Optional<MemoryType> type,
    Optional<MemoryImportance> minImportance,
    Optional<Instant> from,
    Optional<Instant> to,
    Optional<String> keywords,
    int maxResults,
    List<MemorySortOrder> sortOrders
) {}
