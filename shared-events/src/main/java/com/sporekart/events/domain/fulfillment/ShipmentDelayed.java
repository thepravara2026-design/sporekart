package com.sporekart.events.domain.fulfillment;

import com.sporekart.events.model.DomainEvent;

public class ShipmentDelayed extends DomainEvent {
    private final String shipmentId;
    private final String orderId;
    private final String reason;

    private ShipmentDelayed(Builder builder) {
        super(builder);
        this.shipmentId = builder.shipmentId;
        this.orderId = builder.orderId;
        this.reason = builder.reason;
    }

    public String getShipmentId() { return shipmentId; }
    public String getOrderId() { return orderId; }
    public String getReason() { return reason; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends DomainEvent.Builder<Builder> {
        private String shipmentId;
        private String orderId;
        private String reason;

        public Builder shipmentId(String shipmentId) { this.shipmentId = shipmentId; return this; }
        public Builder orderId(String orderId) { this.orderId = orderId; return this; }
        public Builder reason(String reason) { this.reason = reason; return this; }

        public ShipmentDelayed build() {
            return new ShipmentDelayed(this);
        }
    }
}
