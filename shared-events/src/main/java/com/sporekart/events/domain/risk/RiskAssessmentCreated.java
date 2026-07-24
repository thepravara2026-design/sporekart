package com.sporekart.events.domain.risk;

import com.sporekart.events.model.DomainEvent;

public class RiskAssessmentCreated extends DomainEvent {
    private final String assessmentId;
    private final String entityId;
    private final String riskLevel;

    private RiskAssessmentCreated(Builder builder) {
        super(builder);
        this.assessmentId = builder.assessmentId;
        this.entityId = builder.entityId;
        this.riskLevel = builder.riskLevel;
    }

    public String getAssessmentId() { return assessmentId; }
    public String getEntityId() { return entityId; }
    public String getRiskLevel() { return riskLevel; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends DomainEvent.Builder<Builder> {
        private String assessmentId;
        private String entityId;
        private String riskLevel;

        public Builder assessmentId(String assessmentId) { this.assessmentId = assessmentId; return this; }
        public Builder entityId(String entityId) { this.entityId = entityId; return this; }
        public Builder riskLevel(String riskLevel) { this.riskLevel = riskLevel; return this; }

        public RiskAssessmentCreated build() {
            return new RiskAssessmentCreated(this);
        }
    }
}
