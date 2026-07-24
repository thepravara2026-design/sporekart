package com.sporekart.events.registry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DefaultEventRegistryTest {

    private DefaultEventRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new DefaultEventRegistry();
    }

    @Test
    void testRegisterAndCheck() {
        registry.register("order.created", "Order created", "order");
        assertTrue(registry.isRegistered("order.created"));
    }

    @Test
    void testGetDescriptor() {
        registry.register("payment.succeeded", "Payment succeeded", "payment", 1);
        var desc = registry.getDescriptor("payment.succeeded");
        assertTrue(desc.isPresent());
        assertEquals("payment", desc.get().getDomain());
        assertEquals(1, desc.get().getVersion());
    }

    @Test
    void testGetByDomain() {
        registry.register("order.created", "Order created", "order");
        registry.register("order.paid", "Order paid", "order");
        registry.register("review.submitted", "Review submitted", "content");

        List<EventDescriptor> orderEvents = registry.getByDomain("order");
        assertEquals(2, orderEvents.size());
    }

    @Test
    void testDeprecate() {
        registry.register("old.event", "Old event", "legacy");
        assertFalse(registry.getDescriptor("old.event").get().isDeprecated());

        registry.deprecate("old.event");
        assertTrue(registry.getDescriptor("old.event").get().isDeprecated());
    }

    @Test
    void testValidateRegistered() {
        registry.register("valid.event", "Valid event", "test");
        assertDoesNotThrow(() -> registry.validate("valid.event"));
    }

    @Test
    void testValidateUnregistered() {
        assertThrows(IllegalArgumentException.class, () -> registry.validate("unknown.event"));
    }

    @Test
    void testCount() {
        assertEquals(0, registry.count());
        registry.register("event.one", "One", "test");
        registry.register("event.two", "Two", "test");
        assertEquals(2, registry.count());
    }

    @Test
    void testSearch() {
        registry.register("order.created", "Order created", "order");
        registry.register("user.registered", "User registered", "identity");
        List<EventDescriptor> results = registry.search("order");
        assertEquals(1, results.size());
    }
}
