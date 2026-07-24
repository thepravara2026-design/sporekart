package com.sporekart.events.domain.fulfillment;

import com.sporekart.events.model.DomainEvent;

public class ShipmentCreated extends DomainEvent {
    private final String shipmentId;
    private final String orderId;

    private ShipmentCreated(Builder builder) {
        super(builder);
        this.shipmentId = builder.shipmentId;
        this.orderId = builder.orderId;
    }

    public String getShipmentId() { return shipmentId; }
    public String getOrderId() { return orderId; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends DomainEvent.Builder<Builder> {
        private String shipmentId;
        private String orderId;

        public Builder shipmentId(String shipmentId) { this.shipmentId = shipmentId; return this; }
        public Builder orderId(String orderId) { this.orderId = orderId; return this; }

        public ShipmentCreated build() {
            return new ShipmentCreated(this);
        }
    }
}
