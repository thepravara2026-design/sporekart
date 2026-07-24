package com.sporekart.support.infrastructure.persistence;

import com.sporekart.support.domain.model.*;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "support_tickets")
public class SupportTicketEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false, length = 36)
    private String id;

    @Column(name = "customer_id", nullable = false, length = 100)
    private String customerId;

    @Column(name = "subject", nullable = false, length = 255)
    private String subject;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 30)
    private SupportCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false, length = 30)
    private SupportPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private TicketStatus status;

    @Column(name = "assigned_to", length = 100)
    private String assignedTo;

    @Column(name = "resolution", columnDefinition = "TEXT")
    private String resolution;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "resolved_at")
    private Instant resolvedAt;

    protected SupportTicketEntity() {}

    public SupportTicketEntity(String id, String customerId, String subject, String description,
                               SupportCategory category, SupportPriority priority, TicketStatus status,
                               String assignedTo, String resolution, Instant createdAt,
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

    public static SupportTicketEntity fromDomain(SupportTicket ticket) {
        return new SupportTicketEntity(
            ticket.getId(), ticket.getCustomerId(), ticket.getSubject(), ticket.getDescription(),
            ticket.getCategory(), ticket.getPriority(), ticket.getStatus(),
            ticket.getAssignedTo(), ticket.getResolution(),
            ticket.getCreatedAt(), ticket.getUpdatedAt(), ticket.getResolvedAt());
    }

    public SupportTicket toDomain() {
        return new SupportTicket(id, customerId, subject, description, category, priority,
            status, assignedTo, resolution, createdAt, updatedAt, resolvedAt);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public SupportCategory getCategory() { return category; }
    public void setCategory(SupportCategory category) { this.category = category; }
    public SupportPriority getPriority() { return priority; }
    public void setPriority(SupportPriority priority) { this.priority = priority; }
    public TicketStatus getStatus() { return status; }
    public void setStatus(TicketStatus status) { this.status = status; }
    public String getAssignedTo() { return assignedTo; }
    public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }
    public String getResolution() { return resolution; }
    public void setResolution(String resolution) { this.resolution = resolution; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public Instant getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(Instant resolvedAt) { this.resolvedAt = resolvedAt; }
}
