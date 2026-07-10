package com.sporekart.admin.domain.model;

import java.time.Instant;
import java.util.UUID;

public class SupportTicket {
    private final String id;
    private final String subject;
    private final String description;
    private final String requester;
    private final SupportTicketStatus status;
    private final Instant createdAt;

    public SupportTicket(String id, String subject, String description, String requester,
            SupportTicketStatus status, Instant createdAt) {
        this.id = id;
        this.subject = subject;
        this.description = description;
        this.requester = requester;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static SupportTicket create(String subject, String description, String requester) {
        return new SupportTicket(UUID.randomUUID().toString(), subject, description, requester,
                SupportTicketStatus.OPEN, Instant.now());
    }

    public SupportTicket resolve() {
        return new SupportTicket(id, subject, description, requester, SupportTicketStatus.RESOLVED, createdAt);
    }

    public String getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public String getDescription() {
        return description;
    }

    public String getRequester() {
        return requester;
    }

    public SupportTicketStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
