package com.sporekart.operations.copilot.engine;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderOperationsEngineTest {

    @InjectMocks
    private OrderOperationsEngine orderOperationsEngine;

    @Test
    void testAnalyzeOrderQueue() {
        var response = orderOperationsEngine.analyzeOrderQueue("WH-MAIN", null);
        assertNotNull(response);
        assertNotNull(response.orderSummary());
        assertTrue(response.orderSummary().containsKey("pending"));
        assertFalse(response.bottlenecks().isEmpty());
        assertFalse(response.recommendations().isEmpty());
        assertTrue(response.fulfillmentRate() > 0);
    }

    @Test
    void testGetOrderDetail() {
        var order = orderOperationsEngine.getOrderDetail("ORD-001");
        assertNotNull(order);
        assertEquals("ORD-001", order.orderId());
        assertNotNull(order.lineItems());
        assertFalse(order.lineItems().isEmpty());
    }

    @Test
    void testGetFulfillmentRecommendations() {
        var recs = orderOperationsEngine.getFulfillmentRecommendations("WH-MAIN");
        assertFalse(recs.isEmpty());
    }

    @Test
    void testDetectBottlenecks() {
        var bottlenecks = orderOperationsEngine.detectBottlenecks("WH-MAIN");
        assertNotNull(bottlenecks);
        assertTrue(bottlenecks.containsKey("packingBottleneck"));
    }

    @Test
    void testGeneratePriorityQueue() {
        var queue = orderOperationsEngine.generatePriorityQueue(List.of("ORD-1", "ORD-2", "ORD-3"));
        assertEquals(5, queue.size());
        assertTrue(queue.get(0).contains("Priority 1"));
    }

    @Test
    void testGeneratePriorityQueueWithNull() {
        var queue = orderOperationsEngine.generatePriorityQueue(null);
        assertFalse(queue.isEmpty());
    }
}
