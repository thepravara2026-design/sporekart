package com.sporekart.events.model;

public class InfrastructureEvent extends AbstractEvent {
    private final String component;
    private final String infraAction;

    private InfrastructureEvent(Builder builder) {
        super(builder);
        this.component = builder.component;
        this.infraAction = builder.infraAction;
    }

    public String getComponent() { return component; }
    public String getInfraAction() { return infraAction; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends AbstractEvent.Builder<Builder> {
        private String component;
        private String infraAction;

        public Builder component(String component) { this.component = component; return this; }
        public Builder infraAction(String infraAction) { this.infraAction = infraAction; return this; }

        public InfrastructureEvent build() { return new InfrastructureEvent(this); }
    }
}
