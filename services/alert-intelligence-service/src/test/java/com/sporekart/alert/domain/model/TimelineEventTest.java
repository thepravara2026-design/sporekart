package com.sporekart.alert.domain.model;

import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class TimelineEventTest {

    @Test
    void createShouldReturnTimelineEventWithGivenValues() {
        var event = TimelineEvent.create(TimelineEventType.ALERT, "platform", "platform",
                "System update", "System was updated", AlertSeverity.INFO, "deploy-agent", Map.of("version", "2.0"));
        assertNotNull(event.id());
        assertEquals(TimelineEventType.ALERT, event.eventType());
        assertEquals("platform", event.category());
        assertEquals("platform", event.domain());
        assertEquals("System update", event.title());
        assertEquals("System was updated", event.description());
        assertEquals(AlertSeverity.INFO, event.severity());
        assertEquals("deploy-agent", event.source());
        assertEquals("2.0", event.metadata().get("version"));
        assertNotNull(event.timestamp());
    }
}
