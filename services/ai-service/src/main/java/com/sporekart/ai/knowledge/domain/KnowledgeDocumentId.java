package com.sporekart.ai.knowledge.domain;

import java.util.Objects;
import java.util.UUID;

public final class KnowledgeDocumentId {
    private final UUID value;

    private KnowledgeDocumentId(UUID value) {
        this.value = value;
    }

    public static KnowledgeDocumentId fromString(String s) {
        return new KnowledgeDocumentId(UUID.fromString(s));
    }

    public static KnowledgeDocumentId random() {
        return new KnowledgeDocumentId(UUID.randomUUID());
    }

    public UUID value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KnowledgeDocumentId that = (KnowledgeDocumentId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "KnowledgeDocumentId{value=" + value + '}';
    }
}
