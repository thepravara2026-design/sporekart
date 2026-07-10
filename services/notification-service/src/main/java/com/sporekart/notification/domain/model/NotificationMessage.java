package com.sporekart.notification.domain.model;

import java.time.Instant;
import java.util.UUID;

public class NotificationMessage {
    private final String id;
    private final String recipient;
    private final String subject;
    private final String body;
    private final NotificationChannel channel;
    private final NotificationStatus status;
    private final Instant createdAt;

    public NotificationMessage(String id, String recipient, String subject, String body, NotificationChannel channel,
            NotificationStatus status, Instant createdAt) {
        this.id = id;
        this.recipient = recipient;
        this.subject = subject;
        this.body = body;
        this.channel = channel;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static NotificationMessage create(String recipient, String subject, String body,
            NotificationChannel channel) {
        return new NotificationMessage(UUID.randomUUID().toString(), recipient, subject, body, channel,
                NotificationStatus.PENDING, Instant.now());
    }

    public NotificationMessage markSent() {
        return new NotificationMessage(id, recipient, subject, body, channel, NotificationStatus.SENT, createdAt);
    }

    public String getId() {
        return id;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public NotificationChannel getChannel() {
        return channel;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
