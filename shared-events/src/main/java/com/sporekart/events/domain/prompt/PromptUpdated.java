package com.sporekart.events.domain.prompt;

import com.sporekart.events.model.DomainEvent;

public class PromptUpdated extends DomainEvent {
    private final String promptId;
    private final String newVersion;

    private PromptUpdated(Builder builder) {
        super(builder);
        this.promptId = builder.promptId;
        this.newVersion = builder.newVersion;
    }

    public String getPromptId() { return promptId; }
    public String getNewVersion() { return newVersion; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends DomainEvent.Builder<Builder> {
        private String promptId;
        private String newVersion;

        public Builder promptId(String promptId) { this.promptId = promptId; return this; }
        public Builder newVersion(String newVersion) { this.newVersion = newVersion; return this; }

        public PromptUpdated build() {
            return new PromptUpdated(this);
        }
    }
}
