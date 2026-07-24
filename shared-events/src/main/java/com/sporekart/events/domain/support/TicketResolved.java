package com.sporekart.events.domain.support;

import com.sporekart.events.model.DomainEvent;

public class TicketResolved extends DomainEvent {
    private final String ticketId;
    private final String resolvedBy;

    private TicketResolved(Builder builder) {
        super(builder);
        this.ticketId = builder.ticketId;
        this.resolvedBy = builder.resolvedBy;
    }

    public String getTicketId() { return ticketId; }
    public String getResolvedBy() { return resolvedBy; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends DomainEvent.Builder<Builder> {
        private String ticketId;
        private String resolvedBy;

        public Builder ticketId(String ticketId) { this.ticketId = ticketId; return this; }
        public Builder resolvedBy(String resolvedBy) { this.resolvedBy = resolvedBy; return this; }

        public TicketResolved build() {
            return new TicketResolved(this);
        }
    }
}
