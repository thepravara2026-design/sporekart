package com.sporekart.memory.dto;

import com.sporekart.memory.domain.MemoryType;
import com.sporekart.memory.domain.Priority;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

public record UpdateMemoryRequest(
    String title,
    String content,
    List<String> tags,
    Priority priority,
    Integer importance,
    MemoryType memoryType,
    String source,
    Map<String, String> metadata
) {}
