package com.sporekart.events.model;

public class NotificationEvent extends AbstractEvent {
    private final String notificationType;
    private final String recipientId;
    private final String channel;

    private NotificationEvent(Builder builder) {
        super(builder);
        this.notificationType = builder.notificationType;
        this.recipientId = builder.recipientId;
        this.channel = builder.channel;
    }

    public String getNotificationType() { return notificationType; }
    public String getRecipientId() { return recipientId; }
    public String getChannel() { return channel; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends AbstractEvent.Builder<Builder> {
        private String notificationType;
        private String recipientId;
        private String channel;

        public Builder notificationType(String notificationType) { this.notificationType = notificationType; return this; }
        public Builder recipientId(String recipientId) { this.recipientId = recipientId; return this; }
        public Builder channel(String channel) { this.channel = channel; return this; }

        public NotificationEvent build() { return new NotificationEvent(this); }
    }
}
