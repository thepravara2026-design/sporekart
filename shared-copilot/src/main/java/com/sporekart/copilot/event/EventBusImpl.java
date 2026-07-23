package com.sporekart.copilot.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventBusImpl implements EventBus {

    private static final Logger log = LoggerFactory.getLogger(EventBusImpl.class);

    private final ConcurrentMap<String, List<EventHandler>> subscribers = new ConcurrentHashMap<>();

    public EventBusImpl() {
        log.debug("EventBusImpl initialized");
    }

    @Override
    public void publish(CopilotEvent event) {
        log.debug("Publishing event: type={}, id={}", event.type(), event.id());

        List<EventHandler> handlers = subscribers.get(event.type());
        if (handlers != null) {
            for (EventHandler handler : handlers) {
                try {
                    handler.handle(event);
                } catch (Exception e) {
                    log.error("Error handling event type={}: {}", event.type(), e.getMessage(), e);
                }
            }
        }

        List<EventHandler> wildcardHandlers = subscribers.get("*");
        if (wildcardHandlers != null) {
            for (EventHandler handler : wildcardHandlers) {
                try {
                    handler.handle(event);
                } catch (Exception e) {
                    log.error("Error in wildcard handler for event type={}: {}", event.type(), e.getMessage(), e);
                }
            }
        }
    }

    @Override
    public void subscribe(String eventType, EventHandler handler) {
        subscribers.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>()).add(handler);
        log.debug("Subscribed handler for event type: {}", eventType);
    }

    @Override
    public void unsubscribe(String eventType, EventHandler handler) {
        List<EventHandler> handlers = subscribers.get(eventType);
        if (handlers != null) {
            handlers.remove(handler);
            log.debug("Unsubscribed handler for event type: {}", eventType);
        }
    }
}
