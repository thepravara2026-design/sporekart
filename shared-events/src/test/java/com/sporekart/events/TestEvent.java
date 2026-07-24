package com.sporekart.events;

import com.sporekart.events.model.*;

import java.time.Instant;
import java.util.UUID;

public class TestEvent implements Event {
    private final UUID eventId = UUID.randomUUID();
    private final String eventType;
    private final String aggregateId;
    private final String aggregateType = "test";
    private final String workspaceId = "ws-1";
    private final String correlationId = "corr-1";
    private final String causationId = "cause-1";
    private final Instant timestamp = Instant.now();
    private final int version = 1;
    private final EventPriority priority = EventPriority.NORMAL;
    private final EventMetadata metadata = new EventMetadata();
    private final String producer;

    public TestEvent(String eventType, String producer) {
        this.eventType = eventType;
        this.producer = producer;
        this.aggregateId = "agg-" + UUID.randomUUID().toString().substring(0, 8);
    }

    public TestEvent(String eventType) {
        this(eventType, "test-producer");
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
}
