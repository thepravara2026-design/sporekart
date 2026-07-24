package com.sporekart.events.domain.ai;

import com.sporekart.events.model.DomainEvent;

public class AIConversationCompleted extends DomainEvent {
    private final String conversationId;
    private final String summary;

    private AIConversationCompleted(Builder builder) {
        super(builder);
        this.conversationId = builder.conversationId;
        this.summary = builder.summary;
    }

    public String getConversationId() { return conversationId; }
    public String getSummary() { return summary; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends DomainEvent.Builder<Builder> {
        private String conversationId;
        private String summary;

        public Builder conversationId(String conversationId) { this.conversationId = conversationId; return this; }
        public Builder summary(String summary) { this.summary = summary; return this; }

        public AIConversationCompleted build() {
            return new AIConversationCompleted(this);
        }
    }
}
