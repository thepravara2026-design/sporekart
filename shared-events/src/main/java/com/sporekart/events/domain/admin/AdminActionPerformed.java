package com.sporekart.events.domain.admin;

import com.sporekart.events.model.DomainEvent;

public class AdminActionPerformed extends DomainEvent {
    private final String adminId;
    private final String action;
    private final String target;

    private AdminActionPerformed(Builder builder) {
        super(builder);
        this.adminId = builder.adminId;
        this.action = builder.action;
        this.target = builder.target;
    }

    public String getAdminId() { return adminId; }
    public String getAction() { return action; }
    public String getTarget() { return target; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends DomainEvent.Builder<Builder> {
        private String adminId;
        private String action;
        private String target;

        public Builder adminId(String adminId) { this.adminId = adminId; return this; }
        public Builder action(String action) { this.action = action; return this; }
        public Builder target(String target) { this.target = target; return this; }

        public AdminActionPerformed build() {
            return new AdminActionPerformed(this);
        }
    }
}
