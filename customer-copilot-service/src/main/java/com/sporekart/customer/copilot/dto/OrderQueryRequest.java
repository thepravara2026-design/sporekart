package com.sporekart.customer.copilot.dto;

public record OrderQueryRequest(
    String orderId,
    String customerId,
    String status
) {
    public OrderQueryRequest {
        if (orderId == null) {
            orderId = "";
        }
        if (customerId == null) {
            customerId = "";
        }
        if (status == null) {
            status = "";
        }
    }
}
