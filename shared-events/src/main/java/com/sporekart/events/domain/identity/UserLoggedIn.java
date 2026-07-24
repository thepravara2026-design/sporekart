package com.sporekart.events.domain.identity;

import com.sporekart.events.model.DomainEvent;

public class UserLoggedIn extends DomainEvent {
    private final String userId;
    private final String sessionId;

    private UserLoggedIn(Builder builder) {
        super(builder);
        this.userId = builder.userId;
        this.sessionId = builder.sessionId;
    }

    public String getUserId() { return userId; }
    public String getSessionId() { return sessionId; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends DomainEvent.Builder<Builder> {
        private String userId;
        private String sessionId;

        public Builder userId(String userId) { this.userId = userId; return this; }
        public Builder sessionId(String sessionId) { this.sessionId = sessionId; return this; }

        public UserLoggedIn build() {
            return new UserLoggedIn(this);
        }
    }
}
