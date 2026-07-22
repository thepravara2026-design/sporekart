package com.sporekart.memory.event;

import com.sporekart.memory.domain.MemoryType;

import java.util.UUID;

public record MemoryUpdatedEvent(
    Object source,
    UUID memoryId,
    UUID ownerId,
    String workspace,
    MemoryType memoryType
) {}
