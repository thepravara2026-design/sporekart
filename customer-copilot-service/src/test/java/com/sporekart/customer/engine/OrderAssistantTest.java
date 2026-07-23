package com.sporekart.customer.engine;

import com.sporekart.customer.copilot.domain.CustomerOrder;
import com.sporekart.customer.copilot.domain.ShoppingCartItem;
import com.sporekart.customer.infrastructure.order.OrderServiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderAssistantTest {

    @Mock
    private OrderServiceClient orderServiceClient;

    private OrderAssistant orderAssistant;

    private CustomerOrder order1;
    private CustomerOrder order2;

    @BeforeEach
    void setUp() {
        orderAssistant = new OrderAssistant(orderServiceClient);

        order1 = new CustomerOrder("ORD-001", "CUST-001",
            List.of(ShoppingCartItem.from("P1", "Product 1", 2, 10.0)),
            "DELIVERED", 20.0, "USD",
            OffsetDateTime.now().minusDays(10), OffsetDateTime.now().minusDays(3), "/track/ORD-001");

        order2 = new CustomerOrder("ORD-002", "CUST-001",
            List.of(ShoppingCartItem.from("P2", "Product 2", 1, 25.0)),
            "PROCESSING", 25.0, "USD",
            OffsetDateTime.now().minusDays(1), OffsetDateTime.now().plusDays(5), "/track/ORD-002");
    }

    @Test
    void trackOrderWithValidIdReturnsOrder() {
        when(orderServiceClient.getOrder("ORD-001")).thenReturn(Optional.of(order1));

        var result = orderAssistant.trackOrder("ORD-001");

        assertTrue(result.isPresent());
        assertEquals("ORD-001", result.get().orderId());
        assertEquals("DELIVERED", result.get().status());
    }

    @Test
    void trackOrderWithInvalidIdReturnsEmpty() {
        when(orderServiceClient.getOrder("INVALID")).thenReturn(Optional.empty());

        var result = orderAssistant.trackOrder("INVALID");

        assertTrue(result.isEmpty());
    }

    @Test
    void getOrderStatusByStatusReturnsFilteredOrders() {
        when(orderServiceClient.getCustomerOrders(anyString(), anyString()))
            .thenReturn(List.of(order1));

        var results = orderAssistant.getOrderStatus("DELIVERED");

        assertNotNull(results);
        assertFalse(results.isEmpty());
        results.forEach(o -> assertEquals("DELIVERED", o.status()));
    }

    @Test
    void getDeliveryETAReturnsReasonableEstimate() {
        when(orderServiceClient.getOrder("ORD-002")).thenReturn(Optional.of(order2));

        var eta = orderAssistant.getDeliveryETA("ORD-002");

        assertNotNull(eta);
        assertFalse(eta.isBlank());
        assertTrue(eta.contains("days") || eta.equals("Delivering today"));
    }

    @Test
    void getDeliveryETAForDeliveredOrderReturnsDelivered() {
        when(orderServiceClient.getOrder("ORD-001")).thenReturn(Optional.of(order1));

        var eta = orderAssistant.getDeliveryETA("ORD-001");

        assertEquals("Delivered", eta);
    }

    @Test
    void getDeliveryETAForInvalidOrderReturnsNotFound() {
        when(orderServiceClient.getOrder("INVALID")).thenReturn(Optional.empty());

        var eta = orderAssistant.getDeliveryETA("INVALID");

        assertEquals("Order not found", eta);
    }

    @Test
    void getReturnGuidanceContainsSteps() {
        var guidance = orderAssistant.getReturnGuidance();

        assertNotNull(guidance);
        assertTrue(guidance.containsKey("returnWindow"));
        assertTrue(guidance.containsKey("steps"));
        assertTrue(guidance.containsKey("condition"));

        var steps = (List<String>) guidance.get("steps");
        assertNotNull(steps);
        assertFalse(steps.isEmpty());
    }

    @Test
    void getCancellationGuidanceForPendingOrderAllowsCancellation() {
        var pendingOrder = new CustomerOrder("ORD-003", "CUST-001",
            List.of(), "PENDING", 10.0, "USD",
            OffsetDateTime.now(), OffsetDateTime.now().plusDays(5), "/track/ORD-003");
        when(orderServiceClient.getOrder("ORD-003")).thenReturn(Optional.of(pendingOrder));

        var guidance = orderAssistant.getCancellationGuidance("ORD-003");

        assertNotNull(guidance);
        assertEquals(true, guidance.get("cancellable"));
        assertEquals("PENDING", guidance.get("currentStatus"));
    }

    @Test
    void getCancellationGuidanceForShippedOrderDoesNotAllowCancellation() {
        when(orderServiceClient.getOrder("ORD-001")).thenReturn(Optional.of(order1));

        var guidance = orderAssistant.getCancellationGuidance("ORD-001");

        assertNotNull(guidance);
        assertEquals(false, guidance.get("cancellable"));
    }

    @Test
    void getCancellationGuidanceForInvalidOrderReturnsError() {
        when(orderServiceClient.getOrder("INVALID")).thenReturn(Optional.empty());

        var guidance = orderAssistant.getCancellationGuidance("INVALID");

        assertNotNull(guidance);
        assertTrue(guidance.containsKey("error"));
    }
}
