package com.sporekart.order.application.service;

import com.sporekart.order.domain.model.Order;
import com.sporekart.order.domain.model.OrderItem;
import com.sporekart.order.domain.model.OrderStatus;
import com.sporekart.order.infrastructure.persistence.InMemoryOrderRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrderServiceTest {
    @Test
    void createAndCancelOrder() {
        OrderService service = new OrderService(new InMemoryOrderRepository());
        Order order = service.create("customer-1", new BigDecimal("199.99"),
                List.of(new OrderItem("product-1", 2, new BigDecimal("99.99"))));

        assertNotNull(order);
        assertEquals(OrderStatus.PENDING_PAYMENT, order.getStatus());

        Order cancelled = service.cancel(order.getId());
        assertEquals(OrderStatus.CANCELLED, cancelled.getStatus());
    }
}
