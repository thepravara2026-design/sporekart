package com.sporekart.ai.knowledge.domain;

import java.util.Objects;
import java.util.UUID;

public final class KnowledgeVersionId {
    private final UUID value;

    private KnowledgeVersionId(UUID value) {
        this.value = value;
    }

    public static KnowledgeVersionId fromString(String s) {
        return new KnowledgeVersionId(UUID.fromString(s));
    }

    public static KnowledgeVersionId random() {
        return new KnowledgeVersionId(UUID.randomUUID());
    }

    public UUID value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KnowledgeVersionId that = (KnowledgeVersionId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "KnowledgeVersionId{value=" + value + '}';
    }
}
