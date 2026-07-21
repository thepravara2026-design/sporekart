package com.sporekart.ai.prompt.domain.valueobject;

import java.util.Objects;

public record TemplateSection(
    SectionType type,
    String content,
    int order
) {
    public TemplateSection {
        Objects.requireNonNull(type, "SectionType must not be null");
        Objects.requireNonNull(content, "Section content must not be null");
        if (order < 0) throw new IllegalArgumentException("Order must be non-negative");
    }
}
