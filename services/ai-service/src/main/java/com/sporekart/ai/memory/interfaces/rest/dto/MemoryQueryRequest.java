package com.sporekart.ai.memory.interfaces.rest.dto;

import java.time.Instant;
import java.util.List;

public record MemoryQueryRequest(
    String agentId,
    String sessionId,
    String userId,
    String type,
    Integer minImportance,
    Instant from,
    Instant to,
    String keywords,
    Integer maxResults,
    List<String> sortOrders
) {}
