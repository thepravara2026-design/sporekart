package com.sporekart.ai.conversation.domain;

import java.util.Objects;
import java.util.UUID;

public final class ConversationId {

    private final UUID value;

    private ConversationId(UUID value) {
        this.value = value;
    }

    public static ConversationId fromString(String uuid) {
        return new ConversationId(UUID.fromString(uuid));
    }

    public static ConversationId random() {
        return new ConversationId(UUID.randomUUID());
    }

    public UUID value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConversationId that)) return false;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
