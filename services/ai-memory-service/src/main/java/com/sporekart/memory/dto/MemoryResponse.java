package com.sporekart.memory.dto;

import com.sporekart.memory.domain.MemoryType;
import com.sporekart.memory.domain.Priority;
import com.sporekart.memory.domain.Visibility;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record MemoryResponse(
    UUID id,
    String title,
    String content,
    String summary,
    MemoryType memoryType,
    String source,
    Priority priority,
    int importance,
    List<String> tags,
    UUID ownerId,
    String ownerType,
    String workspace,
    String department,
    Visibility visibility,
    UUID entityId,
    String entityType,
    UUID conversationId,
    boolean isEphemeral,
    OffsetDateTime expiresAt,
    boolean hasEncryption,
    Map<String, String> metadata,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt,
    OffsetDateTime lastAccessedAt,
    int accessCount
) {}
