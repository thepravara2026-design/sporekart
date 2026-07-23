package com.sporekart.copilot.domain;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

public final class CopilotId {

    private final String id;
    private final String name;
    private final String version;
    private final OffsetDateTime createdAt;

    public CopilotId(String name, String version) {
        this(UUID.randomUUID().toString(), name, version, OffsetDateTime.now());
    }

    public CopilotId(String id, String name, String version, OffsetDateTime createdAt) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(version, "version must not be null");
        Objects.requireNonNull(createdAt, "createdAt must not be null");
        if (id.isBlank()) throw new IllegalArgumentException("id must not be blank");
        if (name.isBlank()) throw new IllegalArgumentException("name must not be blank");
        if (version.isBlank()) throw new IllegalArgumentException("version must not be blank");
        this.id = id;
        this.name = name;
        this.version = version;
        this.createdAt = createdAt;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getVersion() { return version; }
    public OffsetDateTime getCreatedAt() { return createdAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CopilotId copilotId)) return false;
        return id.equals(copilotId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "CopilotId{id='" + id + "', name='" + name + "', version='" + version + "'}";
    }
}
