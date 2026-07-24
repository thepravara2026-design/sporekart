package com.sporekart.events.store;

import com.sporekart.events.TestEvent;
import com.sporekart.events.model.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryEventStoreTest {

    private InMemoryEventStore store;

    @BeforeEach
    void setUp() {
        store = new InMemoryEventStore();
    }

    private TestEvent createTestEvent(String eventType, String aggregateId) {
        return new TestEvent(eventType);
    }

    @Test
    void testStorePublished() {
        Event event = createTestEvent("order.created", "order-1");
        store.storePublished(event);
        assertNotNull(store.getEvent(event.getEventId().toString()));
        assertEquals(1, store.getTotalPublished());
    }

    @Test
    void testGetEventsByType() {
        store.storePublished(createTestEvent("order.created", "order-1"));
        store.storePublished(createTestEvent("order.created", "order-2"));
        store.storePublished(createTestEvent("payment.succeeded", "pay-1"));

        assertEquals(2, store.getEventsByType("order.created").size());
        assertEquals(1, store.getEventsByType("payment.succeeded").size());
    }

    @Test
    void testAuditTrail() {
        Event event = createTestEvent("test.event", "agg-1");
        store.storePublished(event);
        store.storeConsumed(event, "subscriber-1");
        store.storeFailed(event, "error");

        assertEquals(3, store.getAuditTrail().size());
    }

    @Test
    void testEventTypeCounts() {
        store.storePublished(createTestEvent("type.a", "1"));
        store.storePublished(createTestEvent("type.a", "2"));
        store.storePublished(createTestEvent("type.b", "3"));

        var counts = store.getEventTypeCounts();
        assertEquals(2, counts.get("type.a"));
        assertEquals(1, counts.get("type.b"));
    }

    @Test
    void testClear() {
        store.storePublished(createTestEvent("test.event", "agg-1"));
        store.clear();
        assertEquals(0, store.getTotalPublished());
    }
}
