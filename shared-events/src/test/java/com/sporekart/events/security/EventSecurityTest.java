package com.sporekart.events.security;

import com.sporekart.events.TestEvent;
import com.sporekart.events.model.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventSecurityTest {

    private EventSecurity security;

    @BeforeEach
    void setUp() {
        security = new EventSecurity();
    }

    @Test
    void testAllowPublisher() {
        security.allowPublisher("trusted-service");
        Event event = new TestEvent("test.event", "trusted-service");
        assertTrue(security.validatePublisher(event));
    }

    @Test
    void testDenyPublisher() {
        security.allowPublisher("trusted-service");
        security.denyPublisher("trusted-service");
        Event event = new TestEvent("test.event", "trusted-service");
        assertFalse(security.validatePublisher(event));
    }

    @Test
    void testEmptyAllowedPublishers() {
        Event event = new TestEvent("test.event", "any-service");
        assertTrue(security.validatePublisher(event));
    }

    @Test
    void testConsumerAccess() {
        security.grantSubscriberAccess("order-handler", "order.created");
        Event orderEvent = new TestEvent("order.created", "order-service");
        assertTrue(security.validateConsumer("order-handler", orderEvent));
    }

    @Test
    void testConsumerWildcardAccess() {
        security.grantSubscriberAccess("wildcard-handler", "*");
        Event event = new TestEvent("test.event", "any-service");
        assertTrue(security.validateConsumer("wildcard-handler", event));
    }

    @Test
    void testWorkspaceIsolation() {
        security.isolateWorkspace("workspace-a", "service-1");
        Event allowedEvent = new TestEvent("test.event", "service-1");
        assertTrue(security.validateWorkspace(allowedEvent));
    }
}
