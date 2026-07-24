package com.sporekart.events.domain.knowledge;

import com.sporekart.events.model.DomainEvent;

public class KnowledgeUpdated extends DomainEvent {
    private final String knowledgeId;
    private final String updatedFields;

    private KnowledgeUpdated(Builder builder) {
        super(builder);
        this.knowledgeId = builder.knowledgeId;
        this.updatedFields = builder.updatedFields;
    }

    public String getKnowledgeId() { return knowledgeId; }
    public String getUpdatedFields() { return updatedFields; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends DomainEvent.Builder<Builder> {
        private String knowledgeId;
        private String updatedFields;

        public Builder knowledgeId(String knowledgeId) { this.knowledgeId = knowledgeId; return this; }
        public Builder updatedFields(String updatedFields) { this.updatedFields = updatedFields; return this; }

        public KnowledgeUpdated build() {
            return new KnowledgeUpdated(this);
        }
    }
}
