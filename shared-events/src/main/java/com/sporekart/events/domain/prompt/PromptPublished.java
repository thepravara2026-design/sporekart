package com.sporekart.events.domain.prompt;

import com.sporekart.events.model.DomainEvent;

public class PromptPublished extends DomainEvent {
    private final String promptId;
    private final String version;

    private PromptPublished(Builder builder) {
        super(builder);
        this.promptId = builder.promptId;
        this.version = builder.version;
    }

    public String getPromptId() { return promptId; }
    public String getPromptVersion() { return version; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends DomainEvent.Builder<Builder> {
        private String promptId;
        private String version;

        public Builder promptId(String promptId) { this.promptId = promptId; return this; }
        public Builder version(String version) { this.version = version; return this; }

        public PromptPublished build() {
            return new PromptPublished(this);
        }
    }
}
