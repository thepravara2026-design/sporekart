package com.sporekart.events.domain.fulfillment;

import com.sporekart.events.model.DomainEvent;
import java.time.Instant;

public class ShipmentDelivered extends DomainEvent {
    private final String shipmentId;
    private final String orderId;
    private final Instant deliveredAt;

    private ShipmentDelivered(Builder builder) {
        super(builder);
        this.shipmentId = builder.shipmentId;
        this.orderId = builder.orderId;
        this.deliveredAt = builder.deliveredAt;
    }

    public String getShipmentId() { return shipmentId; }
    public String getOrderId() { return orderId; }
    public Instant getDeliveredAt() { return deliveredAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends DomainEvent.Builder<Builder> {
        private String shipmentId;
        private String orderId;
        private Instant deliveredAt;

        public Builder shipmentId(String shipmentId) { this.shipmentId = shipmentId; return this; }
        public Builder orderId(String orderId) { this.orderId = orderId; return this; }
        public Builder deliveredAt(Instant deliveredAt) { this.deliveredAt = deliveredAt; return this; }

        public ShipmentDelivered build() {
            return new ShipmentDelivered(this);
        }
    }
}
