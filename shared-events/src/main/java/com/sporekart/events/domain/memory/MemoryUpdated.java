package com.sporekart.events.domain.memory;

import com.sporekart.events.model.DomainEvent;

public class MemoryUpdated extends DomainEvent {
    private final String memoryId;
    private final String entityType;
    private final String entityId;

    private MemoryUpdated(Builder builder) {
        super(builder);
        this.memoryId = builder.memoryId;
        this.entityType = builder.entityType;
        this.entityId = builder.entityId;
    }

    public String getMemoryId() { return memoryId; }
    public String getEntityType() { return entityType; }
    public String getEntityId() { return entityId; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends DomainEvent.Builder<Builder> {
        private String memoryId;
        private String entityType;
        private String entityId;

        public Builder memoryId(String memoryId) { this.memoryId = memoryId; return this; }
        public Builder entityType(String entityType) { this.entityType = entityType; return this; }
        public Builder entityId(String entityId) { this.entityId = entityId; return this; }

        public MemoryUpdated build() {
            return new MemoryUpdated(this);
        }
    }
}
