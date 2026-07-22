package com.sporekart.ai.knowledge.domain;

import java.util.Objects;
import java.util.UUID;

public final class KnowledgeCollectionId {
    private final UUID value;

    private KnowledgeCollectionId(UUID value) {
        this.value = value;
    }

    public static KnowledgeCollectionId fromString(String s) {
        return new KnowledgeCollectionId(UUID.fromString(s));
    }

    public static KnowledgeCollectionId random() {
        return new KnowledgeCollectionId(UUID.randomUUID());
    }

    public UUID value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KnowledgeCollectionId that = (KnowledgeCollectionId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "KnowledgeCollectionId{value=" + value + '}';
    }
}
