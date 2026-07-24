package com.sporekart.events.routing;

import com.sporekart.events.TestEvent;
import com.sporekart.events.bus.EventHandler;
import com.sporekart.events.bus.Subscription;
import com.sporekart.events.model.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DefaultEventRouterTest {

    private DefaultEventRouter router;
    private List<Subscription> subscriptions;

    @BeforeEach
    void setUp() {
        router = new DefaultEventRouter();
        EventHandler handler = event -> {};

        subscriptions = List.of(
                new Subscription("order.created", "order-handler", handler),
                new Subscription("payment.succeeded", "payment-handler", handler),
                new Subscription("*", "wildcard-handler", handler)
        );
    }

    @Test
    void testRouteExactMatch() {
        Event event = new TestEvent("order.created");
        List<Subscription> matched = router.route(event, subscriptions);
        assertEquals(2, matched.size());
    }

    @Test
    void testRouteWildcard() {
        Event event = new TestEvent("unknown.event");
        List<Subscription> matched = router.route(event, subscriptions);
        assertEquals(1, matched.size());
        assertEquals("wildcard-handler", matched.get(0).getSubscriberName());
    }

    @Test
    void testNoMatch() {
        List<Subscription> emptySubs = List.of();
        Event event = new TestEvent("any.event");
        List<Subscription> matched = router.route(event, emptySubs);
        assertTrue(matched.isEmpty());
    }

    @Test
    void testAddRule() {
        router.addRule(new RoutingRule("custom.event", "custom-handler"));
        assertEquals(1, router.getRules().size());
    }
}
