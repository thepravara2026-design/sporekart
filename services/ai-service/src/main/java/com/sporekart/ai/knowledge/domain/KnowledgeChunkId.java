package com.sporekart.ai.knowledge.domain;

import java.util.Objects;
import java.util.UUID;

public final class KnowledgeChunkId {
    private final UUID value;

    private KnowledgeChunkId(UUID value) {
        this.value = value;
    }

    public static KnowledgeChunkId fromString(String s) {
        return new KnowledgeChunkId(UUID.fromString(s));
    }

    public static KnowledgeChunkId random() {
        return new KnowledgeChunkId(UUID.randomUUID());
    }

    public UUID value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KnowledgeChunkId that = (KnowledgeChunkId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "KnowledgeChunkId{value=" + value + '}';
    }
}
