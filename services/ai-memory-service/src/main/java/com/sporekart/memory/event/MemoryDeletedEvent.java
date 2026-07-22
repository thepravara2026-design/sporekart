package com.sporekart.memory.event;

import java.util.UUID;

public record MemoryDeletedEvent(
    Object source,
    UUID memoryId
) {}
