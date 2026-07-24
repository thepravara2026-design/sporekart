package com.sporekart.events.model;

public class AIEvent extends AbstractEvent {
    private final String aiActionType;
    private final String modelId;
    private final String promptId;

    private AIEvent(Builder builder) {
        super(builder);
        this.aiActionType = builder.aiActionType;
        this.modelId = builder.modelId;
        this.promptId = builder.promptId;
    }

    public String getAiActionType() { return aiActionType; }
    public String getModelId() { return modelId; }
    public String getPromptId() { return promptId; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends AbstractEvent.Builder<Builder> {
        private String aiActionType;
        private String modelId;
        private String promptId;

        public Builder aiActionType(String aiActionType) { this.aiActionType = aiActionType; return this; }
        public Builder modelId(String modelId) { this.modelId = modelId; return this; }
        public Builder promptId(String promptId) { this.promptId = promptId; return this; }

        public AIEvent build() { return new AIEvent(this); }
    }
}
