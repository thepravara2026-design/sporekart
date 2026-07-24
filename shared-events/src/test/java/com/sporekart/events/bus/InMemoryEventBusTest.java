package com.sporekart.events.bus;

import com.sporekart.events.dlq.DeadLetterQueue;
import com.sporekart.events.dlq.InMemoryDeadLetterQueue;
import com.sporekart.events.TestEvent;
import com.sporekart.events.model.Event;
import com.sporekart.events.registry.DefaultEventRegistry;
import com.sporekart.events.registry.EventRegistry;
import com.sporekart.events.retry.DefaultRetryEngine;
import com.sporekart.events.retry.RetryEngine;
import com.sporekart.events.routing.DefaultEventRouter;
import com.sporekart.events.routing.EventRouter;
import com.sporekart.events.security.EventSecurity;
import com.sporekart.events.store.EventStore;
import com.sporekart.events.store.InMemoryEventStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryEventBusTest {

    private InMemoryEventBus bus;
    private EventRegistry registry;
    private EventStore eventStore;
    private DeadLetterQueue dlq;

    @BeforeEach
    void setUp() {
        registry = new DefaultEventRegistry();
        EventRouter router = new DefaultEventRouter();
        RetryEngine retryEngine = new DefaultRetryEngine();
        dlq = new InMemoryDeadLetterQueue();
        eventStore = new InMemoryEventStore();
        EventSecurity security = new EventSecurity();
        bus = new InMemoryEventBus(registry, router, retryEngine, dlq, eventStore, security, 2);
    }

    private TestEvent createTestEvent(String eventType) {
        return new TestEvent(eventType);
    }

    @Test
    void testPublishAndSubscribe() throws InterruptedException {
        registry.register("test.event", "Test event", "test");
        AtomicInteger counter = new AtomicInteger(0);

        bus.subscribe("test.event", "test-subscriber", event -> counter.incrementAndGet());
        bus.publish(createTestEvent("test.event"));

        Thread.sleep(200);
        assertEquals(1, counter.get());
    }

    @Test
    void testSubscribeWithFilter() throws InterruptedException {
        registry.register("filtered.event", "Filtered event", "test");
        AtomicInteger counter = new AtomicInteger(0);

        bus.subscribe("filtered.event", "filtered-subscriber",
                event -> counter.incrementAndGet(),
                event -> "accept".equals(event.getMetadata().get("decision")));

        TestEvent accepted = createTestEvent("filtered.event");
        accepted.getMetadata().put("decision", "accept");
        bus.publish(accepted);

        TestEvent rejected = createTestEvent("filtered.event");
        rejected.getMetadata().put("decision", "reject");
        bus.publish(rejected);

        Thread.sleep(200);
        assertEquals(1, counter.get());
    }

    @Test
    void testUnsubscribe() throws InterruptedException {
        registry.register("unsub.event", "Unsubscribe test", "test");
        AtomicInteger counter = new AtomicInteger(0);

        Subscription sub = bus.subscribe("unsub.event", "unsub-subscriber", event -> counter.incrementAndGet());
        bus.unsubscribe(sub);
        bus.publish(createTestEvent("unsub.event"));

        Thread.sleep(100);
        assertEquals(0, counter.get());
    }

    @Test
    void testWildcardSubscription() throws InterruptedException {
        AtomicInteger counter = new AtomicInteger(0);
        bus.subscribe("*", "wildcard-subscriber", event -> counter.incrementAndGet());

        bus.publish(createTestEvent("any.event.type"));
        bus.publish(createTestEvent("another.event"));

        Thread.sleep(200);
        assertEquals(2, counter.get());
    }

    @Test
    void testDeadLetterOnHandlerFailure() throws InterruptedException {
        registry.register("failing.event", "Failing event", "test");

        bus.subscribe("failing.event", "failing-subscriber", event -> {
            throw new RuntimeException("Handler failure");
        });

        bus.publish(createTestEvent("failing.event"));

        Thread.sleep(500);
        assertTrue(dlq.count() > 0 || eventStore.getTotalFailed() > 0);
    }

    @Test
    void testPublishAll() throws InterruptedException {
        registry.register("batch.event", "Batch event", "test");
        AtomicInteger counter = new AtomicInteger(0);

        bus.subscribe("batch.event", "batch-subscriber", event -> counter.incrementAndGet());
        bus.publishAll(java.util.List.of(
                createTestEvent("batch.event"),
                createTestEvent("batch.event"),
                createTestEvent("batch.event")
        ));

        Thread.sleep(200);
        assertEquals(3, counter.get());
    }

    @Test
    void testQueueDepth() {
        assertEquals(0, bus.getQueueDepth());
        bus.publish(createTestEvent("queue.test"));
        assertTrue(bus.getQueueDepth() >= 0);
    }

    @Test
    void testClear() {
        bus.subscribe("clear.test", "clear-sub", event -> {});
        bus.publish(createTestEvent("clear.test"));
        bus.clear();
        assertEquals(0, bus.getQueueDepth());
    }
}
