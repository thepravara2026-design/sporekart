package com.sporekart.events.model;

public class ApplicationEvent extends AbstractEvent {
    private final String applicationName;
    private final String action;

    private ApplicationEvent(Builder builder) {
        super(builder);
        this.applicationName = builder.applicationName;
        this.action = builder.action;
    }

    public String getApplicationName() { return applicationName; }
    public String getAction() { return action; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends AbstractEvent.Builder<Builder> {
        private String applicationName;
        private String action;

        public Builder applicationName(String applicationName) { this.applicationName = applicationName; return this; }
        public Builder action(String action) { this.action = action; return this; }

        public ApplicationEvent build() {
            return new ApplicationEvent(this);
        }
    }
}
