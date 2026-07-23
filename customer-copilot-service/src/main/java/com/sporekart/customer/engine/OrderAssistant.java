package com.sporekart.customer.engine;

import com.sporekart.customer.copilot.domain.CustomerOrder;
import com.sporekart.customer.infrastructure.order.OrderServiceClient;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class OrderAssistant {

    private final OrderServiceClient orderServiceClient;

    public OrderAssistant(OrderServiceClient orderServiceClient) {
        this.orderServiceClient = orderServiceClient;
    }

    public Optional<CustomerOrder> trackOrder(String orderId) {
        return orderServiceClient.getOrder(orderId);
    }

    public List<CustomerOrder> getOrderStatus(String status) {
        return orderServiceClient.getCustomerOrders("", status);
    }

    public List<CustomerOrder> getCustomerOrdersByStatus(String customerId, String status) {
        return orderServiceClient.getCustomerOrders(customerId, status);
    }

    public String getDeliveryETA(String orderId) {
        var orderOpt = orderServiceClient.getOrder(orderId);
        if (orderOpt.isEmpty()) return "Order not found";

        var order = orderOpt.get();
        if (order.estimatedDelivery() == null) return "Delivery estimate not available";

        var now = OffsetDateTime.now();
        var daysUntil = ChronoUnit.DAYS.between(now, order.estimatedDelivery());
        if (daysUntil < 0) return "Delivered";
        if (daysUntil == 0) return "Delivering today";
        return daysUntil + " days";
    }

    public Map<String, Object> getReturnGuidance() {
        var guidance = new LinkedHashMap<String, Object>();
        guidance.put("returnWindow", "30 days from delivery");
        guidance.put("condition", "Items must be unopened and in original packaging");
        guidance.put("steps", List.of(
            "Log into your account",
            "Go to Orders and select the item",
            "Click 'Return Item' and select a reason",
            "Print the return label",
            "Drop off the package at the nearest shipping center",
            "Refund will be processed within 5-7 business days"
        ));
        guidance.put("exclusions", List.of("Perishable items", "Custom products", "Digital downloads"));
        return guidance;
    }

    public Map<String, Object> getCancellationGuidance(String orderId) {
        var orderOpt = orderServiceClient.getOrder(orderId);
        if (orderOpt.isEmpty()) return Map.of("error", "Order not found");

        var order = orderOpt.get();
        var guidance = new LinkedHashMap<String, Object>();
        guidance.put("orderId", orderId);
        guidance.put("currentStatus", order.status());

        switch (order.status().toUpperCase()) {
            case "PENDING":
                guidance.put("cancellable", true);
                guidance.put("message", "Order can be cancelled immediately. Full refund will be processed.");
                break;
            case "PROCESSING":
                guidance.put("cancellable", true);
                guidance.put("message", "Order is being processed. Cancellation may incur a small fee.");
                break;
            case "SHIPPED":
                guidance.put("cancellable", false);
                guidance.put("message", "Order has been shipped. Please initiate a return after delivery.");
                break;
            case "DELIVERED":
                guidance.put("cancellable", false);
                guidance.put("message", "Order has been delivered. Please use the return process.");
                break;
            case "CANCELLED":
                guidance.put("cancellable", false);
                guidance.put("message", "This order has already been cancelled.");
                break;
            default:
                guidance.put("cancellable", false);
                guidance.put("message", "Please contact support for assistance.");
        }

        return guidance;
    }
}
