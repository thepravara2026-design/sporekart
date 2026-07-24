package com.sporekart.events.model;

import java.time.Instant;
import java.util.UUID;

public interface Event {
    UUID getEventId();
    String getEventType();
    String getAggregateId();
    String getAggregateType();
    String getWorkspaceId();
    String getCorrelationId();
    String getCausationId();
    Instant getTimestamp();
    int getVersion();
    EventPriority getPriority();
    EventMetadata getMetadata();
    String getProducer();
}
