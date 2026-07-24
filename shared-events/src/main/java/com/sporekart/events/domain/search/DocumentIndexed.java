package com.sporekart.events.domain.search;

import com.sporekart.events.model.DomainEvent;

public class DocumentIndexed extends DomainEvent {
    private final String documentId;
    private final String entityType;

    private DocumentIndexed(Builder builder) {
        super(builder);
        this.documentId = builder.documentId;
        this.entityType = builder.entityType;
    }

    public String getDocumentId() { return documentId; }
    public String getEntityType() { return entityType; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends DomainEvent.Builder<Builder> {
        private String documentId;
        private String entityType;

        public Builder documentId(String documentId) { this.documentId = documentId; return this; }
        public Builder entityType(String entityType) { this.entityType = entityType; return this; }

        public DocumentIndexed build() {
            return new DocumentIndexed(this);
        }
    }
}
