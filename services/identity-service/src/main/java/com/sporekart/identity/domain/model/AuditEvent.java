package com.sporekart.identity.domain.model;

import java.time.Instant;

public class AuditEvent {
    private final Long id;
    private final String eventType;
    private final String actorId;
    private final String details;
    private final String ipAddress;
    private final Instant createdAt;

    public AuditEvent(Long id, String eventType, String actorId,
                      String details, String ipAddress, Instant createdAt) {
        this.id = id;
        this.eventType = eventType;
        this.actorId = actorId;
        this.details = details;
        this.ipAddress = ipAddress;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getEventType() { return eventType; }
    public String getActorId() { return actorId; }
    public String getDetails() { return details; }
    public String getIpAddress() { return ipAddress; }
    public Instant getCreatedAt() { return createdAt; }
}
