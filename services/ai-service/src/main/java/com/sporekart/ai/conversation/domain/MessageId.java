package com.sporekart.ai.conversation.domain;

import java.util.Objects;
import java.util.UUID;

public final class MessageId {

    private final UUID value;

    private MessageId(UUID value) {
        this.value = value;
    }

    public static MessageId fromString(String uuid) {
        return new MessageId(UUID.fromString(uuid));
    }

    public static MessageId random() {
        return new MessageId(UUID.randomUUID());
    }

    public UUID value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageId that)) return false;
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
