package com.sporekart.ai.content.domain;

import com.sporekart.ai.core.domain.ContentType;
import java.time.OffsetDateTime;

public record ContentResult(
        String id,
        String content,
        ContentType contentType,
        int tokenCount,
        OffsetDateTime generatedAt,
        boolean success,
        String errorMessage) {
    public ContentResult(String content, ContentType contentType) {
        this(null, content, contentType, 0, OffsetDateTime.now(), true, null);
    }

    public static ContentResult failure(String errorMessage) {
        return new ContentResult(null, null, null, 0, OffsetDateTime.now(), false, errorMessage);
    }
}
