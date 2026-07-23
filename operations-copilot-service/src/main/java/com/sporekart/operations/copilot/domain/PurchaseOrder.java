package com.sporekart.operations.copilot.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record PurchaseOrder(
    String poId,
    String vendorId,
    String vendorName,
    List<PurchaseLineItem> lineItems,
    double totalCost,
    POStatus status,
    LocalDate orderDate,
    LocalDate expectedDelivery,
    LocalDateTime actualDelivery,
    String warehouseId,
    String notes
) {
    public enum POStatus {
        DRAFT, PENDING_APPROVAL, APPROVED, ORDERED, PARTIALLY_RECEIVED, RECEIVED, CANCELLED
    }
}
