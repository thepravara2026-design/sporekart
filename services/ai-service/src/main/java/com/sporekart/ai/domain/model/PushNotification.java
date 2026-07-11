package com.sporekart.ai.domain.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public class PushNotification {
    private UUID id;
    private UUID deviceId;
    private UUID userId;
    private String title;
    private String body;
    private String notificationType;
    private String actionUrl;
    private boolean isRead;
    private boolean isArchived;
    private OffsetDateTime sentAt;

    public PushNotification(UUID id, UUID deviceId, UUID userId, String title,
            String body, String notificationType) {
        this.id = id;
        this.deviceId = deviceId;
        this.userId = userId;
        this.title = title;
        this.body = body;
        this.notificationType = notificationType;
        this.isRead = false;
        this.isArchived = false;
        this.sentAt = OffsetDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public boolean isRead() {
        return isRead;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public OffsetDateTime getSentAt() {
        return sentAt;
    }

    public void markRead() {
        this.isRead = true;
    }

    public void archive() {
        this.isArchived = true;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }
}
