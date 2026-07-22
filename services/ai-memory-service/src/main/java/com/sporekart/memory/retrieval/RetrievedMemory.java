package com.sporekart.memory.retrieval;

import com.sporekart.memory.domain.MemoryType;
import com.sporekart.memory.domain.Priority;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record RetrievedMemory(
    UUID memoryId,
    String title,
    String content,
    String summary,
    MemoryType memoryType,
    double relevanceScore,
    String source,
    Priority priority,
    int importance,
    List<String> tags,
    UUID ownerId,
    String ownerType,
    String workspace,
    OffsetDateTime createdAt,
    OffsetDateTime lastAccessedAt,
    boolean isEphemeral,
    boolean hasEncryption
) {}
