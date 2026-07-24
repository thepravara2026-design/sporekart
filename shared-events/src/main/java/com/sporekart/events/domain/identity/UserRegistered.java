package com.sporekart.events.domain.identity;

import com.sporekart.events.model.DomainEvent;

public class UserRegistered extends DomainEvent {
    private final String userId;
    private final String email;

    private UserRegistered(Builder builder) {
        super(builder);
        this.userId = builder.userId;
        this.email = builder.email;
    }

    public String getUserId() { return userId; }
    public String getEmail() { return email; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends DomainEvent.Builder<Builder> {
        private String userId;
        private String email;

        public Builder userId(String userId) { this.userId = userId; return this; }
        public Builder email(String email) { this.email = email; return this; }

        public UserRegistered build() {
            return new UserRegistered(this);
        }
    }
}
