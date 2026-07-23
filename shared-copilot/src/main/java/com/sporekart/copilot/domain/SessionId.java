package com.sporekart.copilot.domain;

import java.util.Objects;
import java.util.UUID;

public final class SessionId {

    private final UUID value;

    public SessionId() {
        this(UUID.randomUUID());
    }

    public SessionId(UUID value) {
        Objects.requireNonNull(value, "value must not be null");
        this.value = value;
    }

    public static SessionId fromString(String uuid) {
        return new SessionId(UUID.fromString(uuid));
    }

    public UUID getValue() { return value; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SessionId sessionId)) return false;
        return value.equals(sessionId.value);
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
