package com.sporekart.ai.eventcatalog.domain;

public enum EventDeliveryStatus {
    PENDING,
    DELIVERED,
    FAILED,
    RETRYING,
    DLQ
}
