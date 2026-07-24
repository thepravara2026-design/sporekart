package com.sporekart.admin.infrastructure.persistence;

import com.sporekart.admin.domain.model.SupportTicket;
import com.sporekart.admin.domain.model.SupportTicketStatus;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "admin_support_tickets")
public class SupportTicketEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false, length = 36)
    private String id;

    @Column(name = "subject", nullable = false, length = 255)
    private String subject;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "requester", nullable = false, length = 100)
    private String requester;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private SupportTicketStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected SupportTicketEntity() {}

    public SupportTicketEntity(String id, String subject, String description, String requester,
                               SupportTicketStatus status, Instant createdAt) {
        this.id = id;
        this.subject = subject;
        this.description = description;
        this.requester = requester;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static SupportTicketEntity fromDomain(SupportTicket ticket) {
        return new SupportTicketEntity(
            ticket.getId(), ticket.getSubject(), ticket.getDescription(),
            ticket.getRequester(), ticket.getStatus(), ticket.getCreatedAt());
    }

    public SupportTicket toDomain() {
        return new SupportTicket(id, subject, description, requester, status, createdAt);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getRequester() { return requester; }
    public void setRequester(String requester) { this.requester = requester; }
    public SupportTicketStatus getStatus() { return status; }
    public void setStatus(SupportTicketStatus status) { this.status = status; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
