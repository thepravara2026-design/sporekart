package com.sporekart.memory.event;

import com.sporekart.memory.domain.MemoryType;

import java.util.UUID;

public record MemoryCreatedEvent(
    Object source,
    UUID memoryId,
    UUID ownerId,
    String workspace,
    MemoryType memoryType,
    String title
) {}
