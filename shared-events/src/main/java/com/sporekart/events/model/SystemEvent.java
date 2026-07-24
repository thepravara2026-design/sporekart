package com.sporekart.events.model;

public class SystemEvent extends AbstractEvent {
    private final String systemName;
    private final String systemAction;

    private SystemEvent(Builder builder) {
        super(builder);
        this.systemName = builder.systemName;
        this.systemAction = builder.systemAction;
    }

    public String getSystemName() { return systemName; }
    public String getSystemAction() { return systemAction; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends AbstractEvent.Builder<Builder> {
        private String systemName;
        private String systemAction;

        public Builder systemName(String systemName) { this.systemName = systemName; return this; }
        public Builder systemAction(String systemAction) { this.systemAction = systemAction; return this; }

        public SystemEvent build() { return new SystemEvent(this); }
    }
}
