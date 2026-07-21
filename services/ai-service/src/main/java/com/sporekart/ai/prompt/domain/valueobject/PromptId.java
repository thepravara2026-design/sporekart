package com.sporekart.ai.prompt.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

public record PromptId(String value) {
    public PromptId {
        Objects.requireNonNull(value, "PromptId must not be null");
        if (value.isBlank()) throw new IllegalArgumentException("PromptId must not be blank");
    }

    public static PromptId generate() {
        return new PromptId(UUID.randomUUID().toString());
    }

    public static PromptId from(String value) {
        return new PromptId(value);
    }
}
