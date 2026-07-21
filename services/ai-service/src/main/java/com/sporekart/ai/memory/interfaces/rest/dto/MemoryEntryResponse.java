package com.sporekart.ai.memory.interfaces.rest.dto;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record MemoryEntryResponse(
    UUID id,
    String agentId,
    String sessionId,
    String userId,
    String content,
    String type,
    int importance,
    Map<String, String> metadata,
    Instant createdAt,
    Instant lastAccessedAt,
    Instant expiresAt,
    int accessCount
) {}
