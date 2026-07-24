package com.sporekart.events.domain.ai;

import com.sporekart.events.model.DomainEvent;

public class AIConversationStarted extends DomainEvent {
    private final String conversationId;
    private final String userId;
    private final String copilotType;

    private AIConversationStarted(Builder builder) {
        super(builder);
        this.conversationId = builder.conversationId;
        this.userId = builder.userId;
        this.copilotType = builder.copilotType;
    }

    public String getConversationId() { return conversationId; }
    public String getUserId() { return userId; }
    public String getCopilotType() { return copilotType; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends DomainEvent.Builder<Builder> {
        private String conversationId;
        private String userId;
        private String copilotType;

        public Builder conversationId(String conversationId) { this.conversationId = conversationId; return this; }
        public Builder userId(String userId) { this.userId = userId; return this; }
        public Builder copilotType(String copilotType) { this.copilotType = copilotType; return this; }

        public AIConversationStarted build() {
            return new AIConversationStarted(this);
        }
    }
}
