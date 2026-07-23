package com.sporekart.operations.copilot.domain;

import java.time.LocalDateTime;

public record Shipment(
    String shipmentId,
    String orderId,
    String courierId,
    String courierName,
    String trackingNumber,
    String originWarehouse,
    String destinationAddress,
    String destinationCity,
    String destinationState,
    String destinationPincode,
    double weight,
    ShipmentStatus status,
    LocalDateTime shippedAt,
    LocalDateTime estimatedDelivery,
    LocalDateTime deliveredAt,
    double shippingCost,
    String zone
) {
    public enum ShipmentStatus {
        LABEL_GENERATED, PICKED_UP, IN_TRANSIT, OUT_FOR_DELIVERY, DELIVERED, DELAYED, FAILED, RETURNED
    }
}
