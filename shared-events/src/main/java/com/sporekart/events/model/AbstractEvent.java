package com.sporekart.events.model;

import java.time.Instant;
import java.util.UUID;

public abstract class AbstractEvent implements Event {
    private final UUID eventId;
    private final String eventType;
    private final String aggregateId;
    private final String aggregateType;
    private final String workspaceId;
    private final String correlationId;
    private final String causationId;
    private final Instant timestamp;
    private final int version;
    private final EventPriority priority;
    private final EventMetadata metadata;
    private final String producer;

    protected AbstractEvent(Builder<?> builder) {
        this.eventId = builder.eventId != null ? builder.eventId : UUID.randomUUID();
        this.eventType = builder.eventType;
        this.aggregateId = builder.aggregateId;
        this.aggregateType = builder.aggregateType;
        this.workspaceId = builder.workspaceId;
        this.correlationId = builder.correlationId;
        this.causationId = builder.causationId;
        this.timestamp = builder.timestamp != null ? builder.timestamp : Instant.now();
        this.version = builder.version;
        this.priority = builder.priority != null ? builder.priority : EventPriority.NORMAL;
        this.metadata = builder.metadata != null ? builder.metadata : EventMetadata.empty();
        this.producer = builder.producer;
    }

    @Override public UUID getEventId() { return eventId; }
    @Override public String getEventType() { return eventType; }
    @Override public String getAggregateId() { return aggregateId; }
    @Override public String getAggregateType() { return aggregateType; }
    @Override public String getWorkspaceId() { return workspaceId; }
    @Override public String getCorrelationId() { return correlationId; }
    @Override public String getCausationId() { return causationId; }
    @Override public Instant getTimestamp() { return timestamp; }
    @Override public int getVersion() { return version; }
    @Override public EventPriority getPriority() { return priority; }
    @Override public EventMetadata getMetadata() { return metadata; }
    @Override public String getProducer() { return producer; }

    @SuppressWarnings("unchecked")
    public static abstract class Builder<T extends Builder<T>> {
        private UUID eventId;
        private String eventType;
        private String aggregateId;
        private String aggregateType;
        private String workspaceId;
        private String correlationId;
        private String causationId;
        private Instant timestamp;
        private int version = 1;
        private EventPriority priority = EventPriority.NORMAL;
        private EventMetadata metadata;
        private String producer;

        public T eventId(UUID eventId) { this.eventId = eventId; return (T) this; }
        public T eventType(String eventType) { this.eventType = eventType; return (T) this; }
        public T aggregateId(String aggregateId) { this.aggregateId = aggregateId; return (T) this; }
        public T aggregateType(String aggregateType) { this.aggregateType = aggregateType; return (T) this; }
        public T workspaceId(String workspaceId) { this.workspaceId = workspaceId; return (T) this; }
        public T correlationId(String correlationId) { this.correlationId = correlationId; return (T) this; }
        public T causationId(String causationId) { this.causationId = causationId; return (T) this; }
        public T timestamp(Instant timestamp) { this.timestamp = timestamp; return (T) this; }
        public T version(int version) { this.version = version; return (T) this; }
        public T priority(EventPriority priority) { this.priority = priority; return (T) this; }
        public T metadata(EventMetadata metadata) { this.metadata = metadata; return (T) this; }
        public T producer(String producer) { this.producer = producer; return (T) this; }
        public T correlationIds(CorrelationIds ids) {
            this.correlationId = ids.getCorrelationId();
            this.causationId = ids.getCausationId();
            return (T) this;
        }
    }
}
