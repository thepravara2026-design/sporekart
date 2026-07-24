package com.sporekart.events.domain.customer;

import com.sporekart.events.model.DomainEvent;

public class CustomerRegistered extends DomainEvent {
    private final String customerId;
    private final String email;

    private CustomerRegistered(Builder builder) {
        super(builder);
        this.customerId = builder.customerId;
        this.email = builder.email;
    }

    public String getCustomerId() { return customerId; }
    public String getEmail() { return email; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends DomainEvent.Builder<Builder> {
        private String customerId;
        private String email;

        public Builder customerId(String customerId) { this.customerId = customerId; return this; }
        public Builder email(String email) { this.email = email; return this; }

        public CustomerRegistered build() {
            return new CustomerRegistered(this);
        }
    }
}
