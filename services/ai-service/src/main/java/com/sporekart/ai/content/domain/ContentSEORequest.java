package com.sporekart.ai.content.domain;

import com.sporekart.ai.core.domain.ContentType;
import java.util.UUID;

public record ContentSEORequest(
    UUID id,
    String content,
    String targetKeyword,
    String targetAudience,
    ContentType contentType,
    String language) {}
