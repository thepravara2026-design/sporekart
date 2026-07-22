package com.sporekart.ai.conversation.domain;

import java.util.Objects;
import java.util.UUID;

public final class SessionId {

    private final UUID value;

    private SessionId(UUID value) {
        this.value = value;
    }

    public static SessionId fromString(String uuid) {
        return new SessionId(UUID.fromString(uuid));
    }

    public static SessionId random() {
        return new SessionId(UUID.randomUUID());
    }

    public UUID value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SessionId that)) return false;
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
