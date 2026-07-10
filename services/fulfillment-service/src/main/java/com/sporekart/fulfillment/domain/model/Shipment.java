package com.sporekart.fulfillment.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Shipment {
    private final String id;
    private final String orderId;
    private final String customerId;
    private final BigDecimal shippingCharge;
    private final ShipmentStatus status;
    private final List<ShipmentItem> items;
    private final String awb;
    private final Instant createdAt;
    private final Instant updatedAt;

    public Shipment(String id, String orderId, String customerId, BigDecimal shippingCharge, ShipmentStatus status,
            List<ShipmentItem> items, String awb, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.orderId = orderId;
        this.customerId = customerId;
        this.shippingCharge = shippingCharge;
        this.status = status;
        this.items = new ArrayList<>(items);
        this.awb = awb;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Shipment create(String orderId, String customerId, BigDecimal shippingCharge,
            List<ShipmentItem> items) {
        return new Shipment(UUID.randomUUID().toString(), orderId, customerId, shippingCharge, ShipmentStatus.CREATED,
                items, "AWB-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(), Instant.now(),
                Instant.now());
    }

    public Shipment cancel() {
        return new Shipment(id, orderId, customerId, shippingCharge, ShipmentStatus.CANCELLED, items, awb, createdAt,
                Instant.now());
    }

    public Shipment schedulePickup() {
        return new Shipment(id, orderId, customerId, shippingCharge, ShipmentStatus.PENDING_PICKUP, items, awb,
                createdAt, Instant.now());
    }

    public Shipment markInTransit() {
        return new Shipment(id, orderId, customerId, shippingCharge, ShipmentStatus.IN_TRANSIT, items, awb, createdAt,
                Instant.now());
    }

    public String getId() {
        return id;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public BigDecimal getShippingCharge() {
        return shippingCharge;
    }

    public ShipmentStatus getStatus() {
        return status;
    }

    public List<ShipmentItem> getItems() {
        return List.copyOf(items);
    }

    public String getAwb() {
        return awb;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
