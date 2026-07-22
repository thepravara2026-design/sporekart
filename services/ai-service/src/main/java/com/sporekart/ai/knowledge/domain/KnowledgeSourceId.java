package com.sporekart.ai.knowledge.domain;

import java.util.Objects;
import java.util.UUID;

public final class KnowledgeSourceId {
    private final UUID value;

    private KnowledgeSourceId(UUID value) {
        this.value = value;
    }

    public static KnowledgeSourceId fromString(String s) {
        return new KnowledgeSourceId(UUID.fromString(s));
    }

    public static KnowledgeSourceId random() {
        return new KnowledgeSourceId(UUID.randomUUID());
    }

    public UUID value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KnowledgeSourceId that = (KnowledgeSourceId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "KnowledgeSourceId{value=" + value + '}';
    }
}
