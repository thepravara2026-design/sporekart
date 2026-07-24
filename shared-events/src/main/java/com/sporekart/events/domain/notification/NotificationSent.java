package com.sporekart.events.domain.notification;

import com.sporekart.events.model.DomainEvent;

public class NotificationSent extends DomainEvent {
    private final String notificationId;
    private final String recipient;
    private final String channel;

    private NotificationSent(Builder builder) {
        super(builder);
        this.notificationId = builder.notificationId;
        this.recipient = builder.recipient;
        this.channel = builder.channel;
    }

    public String getNotificationId() { return notificationId; }
    public String getRecipient() { return recipient; }
    public String getChannel() { return channel; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends DomainEvent.Builder<Builder> {
        private String notificationId;
        private String recipient;
        private String channel;

        public Builder notificationId(String notificationId) { this.notificationId = notificationId; return this; }
        public Builder recipient(String recipient) { this.recipient = recipient; return this; }
        public Builder channel(String channel) { this.channel = channel; return this; }

        public NotificationSent build() {
            return new NotificationSent(this);
        }
    }
}
