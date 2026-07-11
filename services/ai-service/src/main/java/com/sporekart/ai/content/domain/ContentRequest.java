package com.sporekart.ai.content.domain;

import com.sporekart.ai.core.domain.ContentType;
import java.util.Map;

public record ContentRequest(
        String prompt,
        ContentType contentType,
        String tone,
        int maxLength,
        String language,
        Map<String, Object> parameters) {
    public ContentRequest(String prompt, ContentType contentType) {
        this(prompt, contentType, "neutral", 500, "en", Map.of());
    }
}
