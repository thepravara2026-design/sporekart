package com.sporekart.events.dlq;

import com.sporekart.events.TestEvent;
import com.sporekart.events.model.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryDeadLetterQueueTest {

    private InMemoryDeadLetterQueue dlq;
    private Event testEvent;

    @BeforeEach
    void setUp() {
        dlq = new InMemoryDeadLetterQueue();
        testEvent = createTestEvent("test.event");
    }

    private TestEvent createTestEvent(String eventType) {
        return new TestEvent(eventType);
    }

    @Test
    void testSendToDeadLetter() {
        dlq.sendToDeadLetter(testEvent, "subscriber-1", "Handler failure");
        assertEquals(1, dlq.count());
    }

    @Test
    void testGetRecord() {
        dlq.sendToDeadLetter(testEvent, "subscriber-1", "error");
        var record = dlq.getRecord(testEvent.getEventId().toString());
        assertTrue(record.isPresent());
        assertEquals("test.event", record.get().getEventType());
        assertEquals("subscriber-1", record.get().getSubscriberName());
        assertEquals("error", record.get().getFailureReason());
    }

    @Test
    void testGetByEventType() {
        dlq.sendToDeadLetter(createTestEvent("type.a"), "sub-1", "err");
        dlq.sendToDeadLetter(createTestEvent("type.b"), "sub-1", "err");
        dlq.sendToDeadLetter(createTestEvent("type.a"), "sub-2", "err");

        assertEquals(2, dlq.getByEventType("type.a").size());
        assertEquals(1, dlq.getByEventType("type.b").size());
    }

    @Test
    void testReplay() {
        dlq.sendToDeadLetter(testEvent, "sub-1", "error");
        assertTrue(dlq.replay(testEvent.getEventId().toString()));

        var record = dlq.getRecord(testEvent.getEventId().toString()).get();
        assertEquals(1, record.getReplayCount());
        assertNotNull(record.getLastReplayedAt());
    }

    @Test
    void testReplayAll() {
        dlq.sendToDeadLetter(createTestEvent("event.1"), "sub-1", "err");
        dlq.sendToDeadLetter(createTestEvent("event.2"), "sub-1", "err");
        assertEquals(2, dlq.replayAll());
    }

    @Test
    void testClear() {
        dlq.sendToDeadLetter(testEvent, "sub-1", "error");
        dlq.clear();
        assertEquals(0, dlq.count());
    }
}
