package com.sporekart.ai.conversation.interfaces.rest.dto;

import java.time.Instant;
import java.util.Map;

public class MemoryEntryResponse {
    private final String id;
    private final String layer;
    private final String key;
    private final String value;
    private final Map<String, String> metadata;
    private final Instant createdAt;
    private final Instant expiresAt;

    public MemoryEntryResponse(String id, String layer, String key, String value,
                               Map<String, String> metadata, Instant createdAt, Instant expiresAt) {
        this.id = id;
        this.layer = layer;
        this.key = key;
        this.value = value;
        this.metadata = metadata;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public String getId() {
        return id;
    }

    public String getLayer() {
        return layer;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }
}
