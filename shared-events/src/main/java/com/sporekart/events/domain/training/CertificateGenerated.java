package com.sporekart.events.domain.training;

import com.sporekart.events.model.DomainEvent;

public class CertificateGenerated extends DomainEvent {
    private final String certificateId;
    private final String trainingId;
    private final String growerId;

    private CertificateGenerated(Builder builder) {
        super(builder);
        this.certificateId = builder.certificateId;
        this.trainingId = builder.trainingId;
        this.growerId = builder.growerId;
    }

    public String getCertificateId() { return certificateId; }
    public String getTrainingId() { return trainingId; }
    public String getGrowerId() { return growerId; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends DomainEvent.Builder<Builder> {
        private String certificateId;
        private String trainingId;
        private String growerId;

        public Builder certificateId(String certificateId) { this.certificateId = certificateId; return this; }
        public Builder trainingId(String trainingId) { this.trainingId = trainingId; return this; }
        public Builder growerId(String growerId) { this.growerId = growerId; return this; }

        public CertificateGenerated build() {
            return new CertificateGenerated(this);
        }
    }
}
