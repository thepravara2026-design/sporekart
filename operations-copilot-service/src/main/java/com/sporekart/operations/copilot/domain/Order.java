package com.sporekart.operations.copilot.domain;

import java.time.LocalDateTime;
import java.util.List;

public record Order(
    String orderId,
    String customerId,
    List<OrderLineItem> lineItems,
    double totalAmount,
    double paidAmount,
    OrderStatus status,
    PaymentStatus paymentStatus,
    LocalDateTime createdAt,
    LocalDateTime processingAt,
    LocalDateTime packedAt,
    LocalDateTime shippedAt,
    LocalDateTime deliveredAt,
    String warehouseId,
    String shippingAddress,
    String courierId,
    String trackingNumber
) {
    public enum OrderStatus {
        PENDING, PROCESSING, PACKED, SHIPPED, DELIVERED, CANCELLED, RETURNED
    }
    public enum PaymentStatus {
        PENDING, PAID, FAILED, REFUNDED
    }
}
