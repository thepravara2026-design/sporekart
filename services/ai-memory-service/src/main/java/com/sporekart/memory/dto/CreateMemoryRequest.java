package com.sporekart.memory.dto;

import com.sporekart.memory.domain.MemoryType;
import com.sporekart.memory.domain.Priority;
import com.sporekart.memory.domain.Visibility;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record CreateMemoryRequest(
    String title,
    String content,
    MemoryType memoryType,
    String source,
    Priority priority,
    Integer importance,
    List<String> tags,
    UUID ownerId,
    String ownerType,
    String workspace,
    String department,
    Visibility visibility,
    UUID entityId,
    String entityType,
    UUID conversationId,
    Boolean isEphemeral,
    Duration expiresIn,
    Boolean hasEncryption,
    Map<String, String> metadata
) {
    public record Duration(long amount, String unit) {}
}
