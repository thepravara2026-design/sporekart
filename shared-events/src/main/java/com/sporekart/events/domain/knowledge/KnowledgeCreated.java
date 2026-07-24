package com.sporekart.events.domain.knowledge;

import com.sporekart.events.model.DomainEvent;

public class KnowledgeCreated extends DomainEvent {
    private final String knowledgeId;
    private final String title;

    private KnowledgeCreated(Builder builder) {
        super(builder);
        this.knowledgeId = builder.knowledgeId;
        this.title = builder.title;
    }

    public String getKnowledgeId() { return knowledgeId; }
    public String getTitle() { return title; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends DomainEvent.Builder<Builder> {
        private String knowledgeId;
        private String title;

        public Builder knowledgeId(String knowledgeId) { this.knowledgeId = knowledgeId; return this; }
        public Builder title(String title) { this.title = title; return this; }

        public KnowledgeCreated build() {
            return new KnowledgeCreated(this);
        }
    }
}
