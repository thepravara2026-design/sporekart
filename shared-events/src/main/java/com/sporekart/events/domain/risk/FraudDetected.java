package com.sporekart.events.domain.risk;

import com.sporekart.events.model.DomainEvent;

public class FraudDetected extends DomainEvent {
    private final String caseId;
    private final String entityId;
    private final String severity;

    private FraudDetected(Builder builder) {
        super(builder);
        this.caseId = builder.caseId;
        this.entityId = builder.entityId;
        this.severity = builder.severity;
    }

    public String getCaseId() { return caseId; }
    public String getEntityId() { return entityId; }
    public String getSeverity() { return severity; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends DomainEvent.Builder<Builder> {
        private String caseId;
        private String entityId;
        private String severity;

        public Builder caseId(String caseId) { this.caseId = caseId; return this; }
        public Builder entityId(String entityId) { this.entityId = entityId; return this; }
        public Builder severity(String severity) { this.severity = severity; return this; }

        public FraudDetected build() {
            return new FraudDetected(this);
        }
    }
}
