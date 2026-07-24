package com.sporekart.support.domain.model;

import java.time.Instant;
import java.util.UUID;

public class SupportTicket {
    private final String id;
    private final String customerId;
    private final String subject;
    private final String description;
    private final SupportCategory category;
    private final SupportPriority priority;
    private TicketStatus status;
    private String assignedTo;
    private String resolution;
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant resolvedAt;

    public SupportTicket(String id, String customerId, String subject, String description, SupportCategory category,
            SupportPriority priority, TicketStatus status, String assignedTo, String resolution, Instant createdAt,
            Instant updatedAt, Instant resolvedAt) {
        this.id = id;
        this.customerId = customerId;
        this.subject = subject;
        this.description = description;
        this.category = category;
        this.priority = priority;
        this.status = status;
        this.assignedTo = assignedTo;
        this.resolution = resolution;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.resolvedAt = resolvedAt;
    }

    public static SupportTicket create(String customerId, String subject, String description, SupportCategory category,
            SupportPriority priority) {
        return new SupportTicket(UUID.randomUUID().toString(), customerId, subject, description, category, priority,
                TicketStatus.OPEN, null, null, Instant.now(), Instant.now(), null);
    }

    public void assignTo(String agentId) {
        if (status != TicketStatus.OPEN) {
            throw new IllegalStateException("Only OPEN tickets can be assigned");
        }
        this.assignedTo = agentId;
        this.updatedAt = Instant.now();
    }

    public void updateStatus(TicketStatus newStatus) {
        if (status == TicketStatus.RESOLVED && newStatus == TicketStatus.CLOSED) {
            this.status = newStatus;
            this.updatedAt = Instant.now();
            return;
        }
        if (newStatus == TicketStatus.OPEN && status != TicketStatus.OPEN) {
            throw new IllegalStateException("Cannot revert to OPEN from " + status);
        }
        if (newStatus == TicketStatus.CLOSED && status != TicketStatus.RESOLVED) {
            throw new IllegalStateException("Ticket must be RESOLVED before CLOSED");
        }
        this.status = newStatus;
        this.updatedAt = Instant.now();
    }

    public void resolve(String resolution) {
        if (resolution == null || resolution.isBlank()) {
            throw new IllegalArgumentException("Resolution is required");
        }
        this.status = TicketStatus.RESOLVED;
        this.resolution = resolution;
        this.resolvedAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void addResolution(String resolution) {
        if (resolution == null || resolution.isBlank()) {
            throw new IllegalArgumentException("Resolution is required");
        }
        this.resolution = resolution;
        this.updatedAt = Instant.now();
    }

    public String getId() { return id; }
    public String getCustomerId() { return customerId; }
    public String getSubject() { return subject; }
    public String getDescription() { return description; }
    public SupportCategory getCategory() { return category; }
    public SupportPriority getPriority() { return priority; }
    public TicketStatus getStatus() { return status; }
    public String getAssignedTo() { return assignedTo; }
    public String getResolution() { return resolution; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public Instant getResolvedAt() { return resolvedAt; }
}