package com.sporekart.notification.infrastructure.persistence;

import com.sporekart.notification.domain.model.NotificationChannel;
import com.sporekart.notification.domain.model.NotificationMessage;
import com.sporekart.notification.domain.model.NotificationStatus;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "notification_messages")
public class NotificationMessageEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false, length = 36)
    private String id;

    @Column(name = "recipient", nullable = false, length = 255)
    private String recipient;

    @Column(name = "subject", nullable = false, length = 255)
    private String subject;

    @Column(name = "body", columnDefinition = "TEXT")
    private String body;

    @Enumerated(EnumType.STRING)
    @Column(name = "channel", nullable = false, length = 30)
    private NotificationChannel channel;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private NotificationStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected NotificationMessageEntity() {}

    public NotificationMessageEntity(String id, String recipient, String subject, String body,
                                     NotificationChannel channel, NotificationStatus status, Instant createdAt) {
        this.id = id;
        this.recipient = recipient;
        this.subject = subject;
        this.body = body;
        this.channel = channel;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static NotificationMessageEntity fromDomain(NotificationMessage message) {
        return new NotificationMessageEntity(
            message.getId(), message.getRecipient(), message.getSubject(), message.getBody(),
            message.getChannel(), message.getStatus(), message.getCreatedAt());
    }

    public NotificationMessage toDomain() {
        return new NotificationMessage(id, recipient, subject, body, channel, status, createdAt);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) { this.recipient = recipient; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    public NotificationChannel getChannel() { return channel; }
    public void setChannel(NotificationChannel channel) { this.channel = channel; }
    public NotificationStatus getStatus() { return status; }
    public void setStatus(NotificationStatus status) { this.status = status; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
