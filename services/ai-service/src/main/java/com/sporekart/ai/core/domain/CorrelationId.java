package com.sporekart.ai.core.domain;

import java.util.UUID;

public record CorrelationId(String id) {
    public CorrelationId() {
        this(UUID.randomUUID().toString());
    }

    public static CorrelationId generate() {
        return new CorrelationId(UUID.randomUUID().toString());
    }

    public static CorrelationId fromString(String id) {
        return new CorrelationId(id);
    }

    @Override
    public String toString() {
        return id;
    }
}
