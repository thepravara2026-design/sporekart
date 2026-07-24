package com.sporekart.events.registry;

import java.time.Instant;
import java.util.Map;

public class EventDescriptor {
    private final String eventType;
    private final String description;
    private final String domain;
    private final int version;
    private final boolean deprecated;
    private final Instant createdAt;
    private final Map<String, String> metadata;

    public EventDescriptor(String eventType, String description, String domain,
                           int version, boolean deprecated, Instant createdAt,
                           Map<String, String> metadata) {
        this.eventType = eventType;
        this.description = description;
        this.domain = domain;
        this.version = version;
        this.deprecated = deprecated;
        this.createdAt = createdAt;
        this.metadata = metadata;
    }

    public String getEventType() { return eventType; }
    public String getDescription() { return description; }
    public String getDomain() { return domain; }
    public int getVersion() { return version; }
    public boolean isDeprecated() { return deprecated; }
    public Instant getCreatedAt() { return createdAt; }
    public Map<String, String> getMetadata() { return metadata; }
}
