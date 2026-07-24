package com.sporekart.events.serializer;

import com.sporekart.events.TestEvent;
import com.sporekart.events.model.Event;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventSerializerTest {

    private final EventSerializer serializer = new EventSerializer();

    @Test
    void testSerialize() {
        Event event = new TestEvent("test.event");
        String json = serializer.serialize(event);
        assertNotNull(json);
        assertTrue(json.contains("test.event"));
        assertTrue(json.contains(event.getAggregateId()));
    }

    @Test
    void testToJson() {
        Event event = new TestEvent("test.event");
        String json = serializer.toJson(event);
        assertNotNull(json);
        assertTrue(json.contains("\"eventType\""));
    }
}
