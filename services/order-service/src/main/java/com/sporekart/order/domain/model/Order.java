package com.sporekart.order.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Order {
    private final String id;
    private final String customerId;
    private final BigDecimal amount;
    private final OrderStatus status;
    private final List<OrderItem> items;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final boolean deleted;

    public Order(String id, String customerId, BigDecimal amount, OrderStatus status, List<OrderItem> items,
            Instant createdAt, Instant updatedAt, boolean deleted) {
        this.id = id;
        this.customerId = customerId;
        this.amount = amount;
        this.status = status;
        this.items = new ArrayList<>(items);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deleted = deleted;
    }

    public static Order create(String customerId, BigDecimal amount, List<OrderItem> items) {
        return new Order(UUID.randomUUID().toString(), customerId, amount, OrderStatus.PENDING_PAYMENT,
                items, Instant.now(), Instant.now(), false);
    }

    public Order cancel() {
        return new Order(id, customerId, amount, OrderStatus.CANCELLED, items, createdAt, Instant.now(), deleted);
    }

    public Order confirm() {
        return new Order(id, customerId, amount, OrderStatus.ORDER_CONFIRMED, items, createdAt, Instant.now(), deleted);
    }

    public String getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public List<OrderItem> getItems() {
        return List.copyOf(items);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public boolean isDeleted() {
        return deleted;
    }
}
