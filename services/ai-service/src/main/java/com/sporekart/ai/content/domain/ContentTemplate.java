package com.sporekart.ai.content.domain;

import com.sporekart.ai.core.domain.ContentType;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record ContentTemplate(
    UUID id,
    String name,
    String description,
    ContentCategory category,
    ContentType contentType,
    String templateContent,
    List<String> variables,
    boolean isActive,
    int version,
    UUID createdBy,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt) {}
