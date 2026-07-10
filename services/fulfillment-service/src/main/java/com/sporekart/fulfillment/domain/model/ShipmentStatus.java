package com.sporekart.fulfillment.domain.model;

public enum ShipmentStatus {
    CREATED,
    PENDING_PICKUP,
    IN_TRANSIT,
    DELIVERED,
    FAILED,
    CANCELLED,
    RETURN_REQUESTED,
    RETURNED
}
