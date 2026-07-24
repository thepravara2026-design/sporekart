package com.sporekart.events.domain.training;

import com.sporekart.events.model.DomainEvent;

public class TrainingCompleted extends DomainEvent {
    private final String trainingId;
    private final String growerId;

    private TrainingCompleted(Builder builder) {
        super(builder);
        this.trainingId = builder.trainingId;
        this.growerId = builder.growerId;
    }

    public String getTrainingId() { return trainingId; }
    public String getGrowerId() { return growerId; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends DomainEvent.Builder<Builder> {
        private String trainingId;
        private String growerId;

        public Builder trainingId(String trainingId) { this.trainingId = trainingId; return this; }
        public Builder growerId(String growerId) { this.growerId = growerId; return this; }

        public TrainingCompleted build() {
            return new TrainingCompleted(this);
        }
    }
}
