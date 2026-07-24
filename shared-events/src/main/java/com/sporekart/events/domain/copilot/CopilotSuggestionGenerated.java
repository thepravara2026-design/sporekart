package com.sporekart.events.domain.copilot;

import com.sporekart.events.model.DomainEvent;

public class CopilotSuggestionGenerated extends DomainEvent {
    private final String suggestionId;
    private final String copilotType;
    private final String suggestionType;

    private CopilotSuggestionGenerated(Builder builder) {
        super(builder);
        this.suggestionId = builder.suggestionId;
        this.copilotType = builder.copilotType;
        this.suggestionType = builder.suggestionType;
    }

    public String getSuggestionId() { return suggestionId; }
    public String getCopilotType() { return copilotType; }
    public String getSuggestionType() { return suggestionType; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends DomainEvent.Builder<Builder> {
        private String suggestionId;
        private String copilotType;
        private String suggestionType;

        public Builder suggestionId(String suggestionId) { this.suggestionId = suggestionId; return this; }
        public Builder copilotType(String copilotType) { this.copilotType = copilotType; return this; }
        public Builder suggestionType(String suggestionType) { this.suggestionType = suggestionType; return this; }

        public CopilotSuggestionGenerated build() {
            return new CopilotSuggestionGenerated(this);
        }
    }
}
