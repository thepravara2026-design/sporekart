package com.sporekart.events.domain.support;

import com.sporekart.events.model.DomainEvent;

public class TicketCreated extends DomainEvent {
    private final String ticketId;
    private final String customerId;
    private final String category;

    private TicketCreated(Builder builder) {
        super(builder);
        this.ticketId = builder.ticketId;
        this.customerId = builder.customerId;
        this.category = builder.category;
    }

    public String getTicketId() { return ticketId; }
    public String getCustomerId() { return customerId; }
    public String getCategory() { return category; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends DomainEvent.Builder<Builder> {
        private String ticketId;
        private String customerId;
        private String category;

        public Builder ticketId(String ticketId) { this.ticketId = ticketId; return this; }
        public Builder customerId(String customerId) { this.customerId = customerId; return this; }
        public Builder category(String category) { this.category = category; return this; }

        public TicketCreated build() {
            return new TicketCreated(this);
        }
    }
}
