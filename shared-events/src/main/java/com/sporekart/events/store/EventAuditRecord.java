package com.sporekart.events.store;

import java.time.Instant;

public class EventAuditRecord {
    private final String eventId;
    private final String eventType;
    private final String action;
    private final String subscriber;
    private final String status;
    private final Instant timestamp;
    private final long durationMs;

    public EventAuditRecord(String eventId, String eventType, String action,
                            String subscriber, String status, Instant timestamp, long durationMs) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.action = action;
        this.subscriber = subscriber;
        this.status = status;
        this.timestamp = timestamp;
        this.durationMs = durationMs;
    }

    public String getEventId() { return eventId; }
    public String getEventType() { return eventType; }
    public String getAction() { return action; }
    public String getSubscriber() { return subscriber; }
    public String getStatus() { return status; }
    public Instant getTimestamp() { return timestamp; }
    public long getDurationMs() { return durationMs; }
}
