package com.sporekart.ai.memory.domain;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record MemoryEntry(
    UUID id,
    String agentId,
    String sessionId,
    String userId,
    String content,
    MemoryType type,
    MemoryImportance importance,
    Map<String, String> metadata,
    Instant createdAt,
    Instant lastAccessedAt,
    Instant expiresAt,
    int accessCount
) {}
