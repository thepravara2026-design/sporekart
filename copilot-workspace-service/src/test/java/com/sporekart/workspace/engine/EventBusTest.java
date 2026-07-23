package com.sporekart.workspace.engine;

import com.sporekart.workspace.domain.WorkspaceEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EventBusTest {

    private EventBus eventBus;

    @BeforeEach
    void setUp() {
        eventBus = new EventBus();
    }

    @Test
    void publish_ShouldAddEvent() {
        WorkspaceEvent event = new WorkspaceEvent("evt-1", WorkspaceEvent.TYPE_CONVERSATION_STARTED,
                "copilot-1", "sess-1", "copilot-1", Map.of("msg", "hello"), null);
        eventBus.publish(event);

        List<WorkspaceEvent> events = eventBus.getEventsByType(WorkspaceEvent.TYPE_CONVERSATION_STARTED);
        assertThat(events).hasSize(1);
    }

    @Test
    void subscribe_ShouldReceivePublishedEvents() {
        StringBuilder received = new StringBuilder();
        Consumer<WorkspaceEvent> handler = event -> received.append(event.eventType());
        eventBus.subscribe(handler);

        WorkspaceEvent event = new WorkspaceEvent("evt-2", WorkspaceEvent.TYPE_TOOL_EXECUTED,
                "copilot-1", "sess-1", "copilot-1", Map.of(), null);
        eventBus.publish(event);

        assertThat(received.toString()).contains(WorkspaceEvent.TYPE_TOOL_EXECUTED);
    }

    @Test
    void unsubscribe_ShouldStopReceivingEvents() {
        StringBuilder received = new StringBuilder();
        Consumer<WorkspaceEvent> handler = event -> received.append(event.eventType());
        eventBus.subscribe(handler);
        eventBus.unsubscribe(handler);

        WorkspaceEvent event = new WorkspaceEvent("evt-3", WorkspaceEvent.TYPE_COPILOT_SWITCHED,
                "copilot-1", "sess-1", "copilot-1", Map.of(), null);
        eventBus.publish(event);

        assertThat(received.toString()).isEmpty();
    }

    @Test
    void getEventsByType_ShouldFilterByType() {
        eventBus.publish(new WorkspaceEvent("evt-4", WorkspaceEvent.TYPE_CONVERSATION_STARTED,
                "src", "sess-1", "copilot-1", Map.of(), null));
        eventBus.publish(new WorkspaceEvent("evt-5", WorkspaceEvent.TYPE_TOOL_EXECUTED,
                "src", "sess-1", "copilot-1", Map.of(), null));
        eventBus.publish(new WorkspaceEvent("evt-6", WorkspaceEvent.TYPE_CONVERSATION_STARTED,
                "src", "sess-2", "copilot-2", Map.of(), null));

        List<WorkspaceEvent> started = eventBus.getEventsByType(WorkspaceEvent.TYPE_CONVERSATION_STARTED);
        assertThat(started).hasSize(2);
    }

    @Test
    void getEventsBySession_ShouldFilterBySession() {
        eventBus.publish(new WorkspaceEvent("evt-7", WorkspaceEvent.TYPE_CONVERSATION_STARTED,
                "src", "sess-1", "copilot-1", Map.of(), null));
        eventBus.publish(new WorkspaceEvent("evt-8", WorkspaceEvent.TYPE_TOOL_EXECUTED,
                "src", "sess-1", "copilot-1", Map.of(), null));
        eventBus.publish(new WorkspaceEvent("evt-9", WorkspaceEvent.TYPE_CONVERSATION_STARTED,
                "src", "sess-2", "copilot-2", Map.of(), null));

        List<WorkspaceEvent> sessionEvents = eventBus.getEventsBySession("sess-1");
        assertThat(sessionEvents).hasSize(2);
    }

    @Test
    void getEventsByCopilot_ShouldFilterByCopilot() {
        eventBus.publish(new WorkspaceEvent("evt-10", WorkspaceEvent.TYPE_TOOL_EXECUTED,
                "src", "sess-1", "copilot-A", Map.of(), null));
        eventBus.publish(new WorkspaceEvent("evt-11", WorkspaceEvent.TYPE_CONVERSATION_STARTED,
                "src", "sess-1", "copilot-B", Map.of(), null));

        List<WorkspaceEvent> copilotEvents = eventBus.getEventsByCopilot("copilot-A");
        assertThat(copilotEvents).hasSize(1);
    }

    @Test
    void getRecentEvents_ShouldReturnLimitedResults() {
        for (int i = 0; i < 20; i++) {
            eventBus.publish(new WorkspaceEvent("evt-" + i, WorkspaceEvent.TYPE_WORKSPACE_CHANGED,
                    "src", "sess-1", "copilot-1", Map.of(), null));
        }

        List<WorkspaceEvent> recent = eventBus.getRecentEvents(5);
        assertThat(recent).hasSize(5);
    }

    @Test
    void clearEvents_ShouldRemoveAllEvents() {
        eventBus.publish(new WorkspaceEvent("evt-100", WorkspaceEvent.TYPE_SESSION_CLOSED,
                "src", "sess-1", "copilot-1", Map.of(), null));
        eventBus.clearEvents();

        List<WorkspaceEvent> events = eventBus.getEventsByType(WorkspaceEvent.TYPE_SESSION_CLOSED);
        assertThat(events).isEmpty();
    }
}
