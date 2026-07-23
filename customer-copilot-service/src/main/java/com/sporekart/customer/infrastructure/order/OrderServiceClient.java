package com.sporekart.customer.infrastructure.order;

import com.sporekart.customer.copilot.domain.CustomerOrder;
import com.sporekart.customer.copilot.domain.ShoppingCartItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Component
public class OrderServiceClient {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceClient.class);

    private final Map<String, List<CustomerOrder>> customerOrders = new ConcurrentHashMap<>();
    private final Map<String, Map<String, Object>> orderMetadata = new ConcurrentHashMap<>();

    public OrderServiceClient() {
        log.info("Initializing OrderServiceClient with simulated order data");
        seedOrder("CUST-SEED-01", "ORD-2026-001", List.of(
            ShoppingCartItem.from("PROD-001", "Fresh Oyster Mushrooms", 2, 4.99),
            ShoppingCartItem.from("PROD-006", "Oyster Mushroom Growing Kit", 1, 29.99)
        ), "DELIVERED", 39.97, OffsetDateTime.now().minusDays(14),
            OffsetDateTime.now().minusDays(10), "/track/ORD-2026-001");
        seedOrder("CUST-SEED-01", "ORD-2026-002", List.of(
            ShoppingCartItem.from("PROD-007", "Shiitake Mushroom Log", 1, 39.99)
        ), "SHIPPED", 39.99, OffsetDateTime.now().minusDays(3),
            OffsetDateTime.now().plusDays(4), "/track/ORD-2026-002");
        seedOrder("CUST-SEED-02", "ORD-2026-003", List.of(
            ShoppingCartItem.from("PROD-016", "Mushroom Cultivation Book", 1, 24.99),
            ShoppingCartItem.from("PROD-012", "Digital Humidity Monitor", 1, 24.99)
        ), "PROCESSING", 49.98, OffsetDateTime.now().minusDays(1),
            OffsetDateTime.now().plusDays(7), "/track/ORD-2026-003");
        seedOrder("CUST-SEED-03", "ORD-2026-004", List.of(
            ShoppingCartItem.from("PROD-008", "Mushroom Garden Starter Kit", 2, 49.99)
        ), "PENDING", 99.98, OffsetDateTime.now().minusHours(6),
            OffsetDateTime.now().plusDays(5), "/track/ORD-2026-004");
        seedOrder("CUST-SEED-01", "ORD-2026-005", List.of(
            ShoppingCartItem.from("PROD-003", "Fresh Lion's Mane Mushrooms", 1, 12.99),
            ShoppingCartItem.from("PROD-017", "Reishi Mushroom Capsules", 2, 34.99)
        ), "CANCELLED", 82.97, OffsetDateTime.now().minusDays(7),
            null, "/track/ORD-2026-005");
    }

    private void seedOrder(String customerId, String orderId, List<ShoppingCartItem> items,
                           String status, double total, OffsetDateTime placed,
                           OffsetDateTime estimatedDelivery, String trackingUrl) {
        var order = new CustomerOrder(orderId, customerId, items, status, total, "USD",
            placed, estimatedDelivery, trackingUrl);
        customerOrders.computeIfAbsent(customerId, k -> new CopyOnWriteArrayList<>()).add(order);
        var meta = new HashMap<String, Object>();
        meta.put("paymentStatus", status.equals("DELIVERED") ? "PAID" : "PENDING");
        meta.put("paymentMethod", "Credit Card");
        meta.put("shippingAddress", "123 Mushroom Lane, Farmville");
        meta.put("carrier", status.equals("DELIVERED") ? "FastShip" : "Pending");
        meta.put("invoiceUrl", "/invoices/" + orderId + ".pdf");
        meta.put("returnEligible", !status.equals("CANCELLED") && !status.equals("DELIVERED"));
        meta.put("returnWindowDays", 30);
        meta.put("refundStatus", status.equals("CANCELLED") ? "PROCESSED" : "N/A");
        meta.put("cancellationAllowed", status.equals("PENDING") || status.equals("PROCESSING"));
        orderMetadata.put(orderId, meta);
    }

    public Optional<CustomerOrder> getOrder(String orderId) {
        log.debug("OrderServiceClient.getOrder called for orderId='{}'", orderId);
        return customerOrders.values().stream()
            .flatMap(List::stream)
            .filter(o -> o.orderId().equals(orderId))
            .findFirst();
    }

    public List<CustomerOrder> getCustomerOrders(String customerId, String status) {
        log.debug("OrderServiceClient.getCustomerOrders called for customerId='{}', status='{}'", customerId, status);
        var orders = customerOrders.getOrDefault(customerId, List.of());
        if (status == null || status.isBlank() || "ALL".equalsIgnoreCase(status)) {
            return orders;
        }
        return orders.stream()
            .filter(o -> o.status().equalsIgnoreCase(status))
            .toList();
    }

    public Map<String, Object> getOrderStatus(String orderId) {
        log.debug("OrderServiceClient.getOrderStatus called for orderId='{}'", orderId);
        var orderOpt = getOrder(orderId);
        if (orderOpt.isEmpty()) {
            return Map.of("error", "Order not found", "status", "UNKNOWN");
        }
        var order = orderOpt.get();
        var meta = orderMetadata.getOrDefault(orderId, Map.of());
        var result = new LinkedHashMap<String, Object>();
        result.put("orderId", order.orderId());
        result.put("status", order.status());
        result.put("placedAt", order.placedAt().toString());
        result.put("estimatedDelivery", order.estimatedDelivery() != null ? order.estimatedDelivery().toString() : "N/A");
        result.put("trackingUrl", order.trackingUrl());
        result.put("carrier", meta.getOrDefault("carrier", "Pending"));
        result.put("paymentStatus", meta.getOrDefault("paymentStatus", "UNKNOWN"));
        result.put("items", order.items().stream()
            .map(i -> Map.of("productId", i.productId(), "productName", i.productName(), "quantity", i.quantity()))
            .collect(Collectors.toList()));
        return result;
    }
}
