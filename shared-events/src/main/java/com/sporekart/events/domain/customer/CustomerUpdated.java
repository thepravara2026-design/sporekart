package com.sporekart.events.domain.customer;

import com.sporekart.events.model.DomainEvent;

public class CustomerUpdated extends DomainEvent {
    private final String customerId;
    private final String updatedFields;

    private CustomerUpdated(Builder builder) {
        super(builder);
        this.customerId = builder.customerId;
        this.updatedFields = builder.updatedFields;
    }

    public String getCustomerId() { return customerId; }
    public String getUpdatedFields() { return updatedFields; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends DomainEvent.Builder<Builder> {
        private String customerId;
        private String updatedFields;

        public Builder customerId(String customerId) { this.customerId = customerId; return this; }
        public Builder updatedFields(String updatedFields) { this.updatedFields = updatedFields; return this; }

        public CustomerUpdated build() {
            return new CustomerUpdated(this);
        }
    }
}
