package com.sporekart.ai.content.domain;

import com.sporekart.ai.core.domain.ContentType;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public record ContentGenerationRequest(
    UUID id,
    String requestId,
    String prompt,
    ContentType contentType,
    ContentCategory category,
    ContentTone tone,
    String targetLanguage,
    int maxLength,
    Map<String, Object> parameters,
    UUID templateId,
    UUID createdBy) {}
