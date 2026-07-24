package com.sporekart.events.domain.ai;

import com.sporekart.events.model.DomainEvent;

public class AIActionExecuted extends DomainEvent {
    private final String actionId;
    private final String actionType;
    private final String result;

    private AIActionExecuted(Builder builder) {
        super(builder);
        this.actionId = builder.actionId;
        this.actionType = builder.actionType;
        this.result = builder.result;
    }

    public String getActionId() { return actionId; }
    public String getActionType() { return actionType; }
    public String getResult() { return result; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends DomainEvent.Builder<Builder> {
        private String actionId;
        private String actionType;
        private String result;

        public Builder actionId(String actionId) { this.actionId = actionId; return this; }
        public Builder actionType(String actionType) { this.actionType = actionType; return this; }
        public Builder result(String result) { this.result = result; return this; }

        public AIActionExecuted build() {
            return new AIActionExecuted(this);
        }
    }
}
