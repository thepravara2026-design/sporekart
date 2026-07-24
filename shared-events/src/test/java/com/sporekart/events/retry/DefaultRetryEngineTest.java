package com.sporekart.events.retry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class DefaultRetryEngineTest {

    private DefaultRetryEngine engine;

    @BeforeEach
    void setUp() {
        engine = new DefaultRetryEngine(new RetryPolicy(3, Duration.ofMillis(10), Duration.ofMillis(100), 2.0));
    }

    @Test
    void testShouldRetryInitially() {
        assertTrue(engine.shouldRetry("event-1"));
    }

    @Test
    void testShouldNotRetryAfterMaxAttempts() {
        engine.recordFailure("event-1", "error 1");
        engine.recordFailure("event-1", "error 2");
        engine.recordFailure("event-1", "error 3");
        assertFalse(engine.shouldRetry("event-1"));
    }

    @Test
    void testRecordFailure() {
        engine.recordFailure("event-1", "test error");
        assertEquals(1, engine.getRetryCount("event-1"));
    }

    @Test
    void testMarkResolved() {
        engine.recordFailure("event-1", "error");
        engine.markResolved("event-1");
        assertFalse(engine.shouldRetry("event-1"));
    }

    @Test
    void testGetRecord() {
        engine.recordFailure("event-1", "error");
        assertTrue(engine.getRecord("event-1").isPresent());
        assertEquals(1, engine.getRecord("event-1").get().getAttemptCount());
    }

    @Test
    void testClear() {
        engine.recordFailure("event-1", "error");
        engine.clear();
        assertEquals(0, engine.getTotalRetries());
    }
}
