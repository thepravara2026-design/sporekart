package com.sporekart.workspace.engine;

import com.sporekart.workspace.domain.WorkspaceEvent;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Component
public class EventBus {

    private static final Logger log = LoggerFactory.getLogger(EventBus.class);
    private static final int MAX_EVENTS = 10000;

    public static final String CONVERSATION_STARTED = WorkspaceEvent.TYPE_CONVERSATION_STARTED;
    public static final String CONVERSATION_FINISHED = WorkspaceEvent.TYPE_CONVERSATION_FINISHED;
    public static final String TOOL_EXECUTED = WorkspaceEvent.TYPE_TOOL_EXECUTED;
    public static final String KNOWLEDGE_RETRIEVED = WorkspaceEvent.TYPE_KNOWLEDGE_RETRIEVED;
    public static final String PROMPT_EXECUTED = WorkspaceEvent.TYPE_PROMPT_EXECUTED;
    public static final String COPILOT_SWITCHED = WorkspaceEvent.TYPE_COPILOT_SWITCHED;
    public static final String WORKSPACE_CHANGED = WorkspaceEvent.TYPE_WORKSPACE_CHANGED;
    public static final String SESSION_CLOSED = WorkspaceEvent.TYPE_SESSION_CLOSED;
    public static final String HANDOFF_INITIATED = WorkspaceEvent.TYPE_HANDOFF_INITIATED;
    public static final String HANDOFF_COMPLETED = WorkspaceEvent.TYPE_HANDOFF_COMPLETED;
    public static final String COLLABORATION_STARTED = WorkspaceEvent.TYPE_COLLABORATION_STARTED;
    public static final String COLLABORATION_COMPLETED = WorkspaceEvent.TYPE_COLLABORATION_COMPLETED;
    public static final String ROUTING_DECISION = "ROUTING_DECISION";

    @FunctionalInterface
    public interface EventHandler {
        void handleEvent(WorkspaceEvent event);
    }

    private final ConcurrentLinkedQueue<WorkspaceEvent> eventStore = new ConcurrentLinkedQueue<>();
    private final ConcurrentHashMap<String, EventHandler> subscriptions = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, List<String>> typeIndex = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, List<String>> sessionIndex = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, List<String>> copilotIndex = new ConcurrentHashMap<>();
    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    public void publish(WorkspaceEvent event) {
        String eventId = event.eventId() != null ? event.eventId() : UUID.randomUUID().toString();
        WorkspaceEvent enriched = new WorkspaceEvent(
            eventId,
            event.eventType(),
            event.source(),
            event.sessionId(),
            event.copilotId(),
            event.payload(),
            event.timestamp() != null ? event.timestamp() : OffsetDateTime.now()
        );

        eventStore.add(enriched);
        if (eventStore.size() > MAX_EVENTS) {
            eventStore.poll();
        }

        indexEvent(enriched);

        for (Map.Entry<String, EventHandler> entry : subscriptions.entrySet()) {
            executor.submit(() -> {
                try {
                    entry.getValue().handleEvent(enriched);
                } catch (Exception e) {
                    log.error("EventHandler {} failed for event {}", entry.getKey(), enriched.eventId(), e);
                }
            });
        }
    }

    public String subscribe(String eventType, EventHandler handler) {
        String subscriptionId = UUID.randomUUID().toString();
        subscriptions.put(subscriptionId, handler);
        typeIndex.computeIfAbsent(eventType, k -> Collections.synchronizedList(new ArrayList<>())).add(subscriptionId);
        return subscriptionId;
    }

    public void unsubscribe(String subscriptionId) {
        subscriptions.remove(subscriptionId);
        for (List<String> subs : typeIndex.values()) {
            subs.remove(subscriptionId);
        }
    }

    public List<WorkspaceEvent> getEventsByType(String eventType, int limit) {
        return eventStore.stream()
            .filter(e -> e.eventType().equals(eventType))
            .limit(limit)
            .collect(Collectors.toList());
    }

    public List<WorkspaceEvent> getEventsBySession(String sessionId, int limit) {
        return eventStore.stream()
            .filter(e -> sessionId.equals(e.sessionId()))
            .limit(limit)
            .collect(Collectors.toList());
    }

    public List<WorkspaceEvent> getEventsByCopilot(String copilotId, int limit) {
        return eventStore.stream()
            .filter(e -> copilotId.equals(e.copilotId()))
            .limit(limit)
            .collect(Collectors.toList());
    }

    public List<WorkspaceEvent> getRecentEvents(int limit) {
        List<WorkspaceEvent> all = new ArrayList<>(eventStore);
        int size = all.size();
        if (size <= limit) {
            return all;
        }
        return all.subList(size - limit, size);
    }

    public void clearEvents() {
        eventStore.clear();
        typeIndex.clear();
        sessionIndex.clear();
        copilotIndex.clear();
    }

    private void indexEvent(WorkspaceEvent event) {
        if (event.eventType() != null) {
            typeIndex.computeIfAbsent(event.eventType(), k -> Collections.synchronizedList(new ArrayList<>()));
        }
        if (event.sessionId() != null) {
            sessionIndex.computeIfAbsent(event.sessionId(), k -> Collections.synchronizedList(new ArrayList<>()));
        }
        if (event.copilotId() != null) {
            copilotIndex.computeIfAbsent(event.copilotId(), k -> Collections.synchronizedList(new ArrayList<>()));
        }
    }
}