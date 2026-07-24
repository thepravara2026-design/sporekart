package com.sporekart.events.domain.copilot;

import com.sporekart.events.model.DomainEvent;

public class CopilotActionExecuted extends DomainEvent {
    private final String actionId;
    private final String copilotType;
    private final String action;

    private CopilotActionExecuted(Builder builder) {
        super(builder);
        this.actionId = builder.actionId;
        this.copilotType = builder.copilotType;
        this.action = builder.action;
    }

    public String getActionId() { return actionId; }
    public String getCopilotType() { return copilotType; }
    public String getAction() { return action; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends DomainEvent.Builder<Builder> {
        private String actionId;
        private String copilotType;
        private String action;

        public Builder actionId(String actionId) { this.actionId = actionId; return this; }
        public Builder copilotType(String copilotType) { this.copilotType = copilotType; return this; }
        public Builder action(String action) { this.action = action; return this; }

        public CopilotActionExecuted build() {
            return new CopilotActionExecuted(this);
        }
    }
}
