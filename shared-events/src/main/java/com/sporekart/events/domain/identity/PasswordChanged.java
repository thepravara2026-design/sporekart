package com.sporekart.events.domain.identity;

import com.sporekart.events.model.DomainEvent;

public class PasswordChanged extends DomainEvent {
    private final String userId;

    private PasswordChanged(Builder builder) {
        super(builder);
        this.userId = builder.userId;
    }

    public String getUserId() { return userId; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends DomainEvent.Builder<Builder> {
        private String userId;

        public Builder userId(String userId) { this.userId = userId; return this; }

        public PasswordChanged build() {
            return new PasswordChanged(this);
        }
    }
}
