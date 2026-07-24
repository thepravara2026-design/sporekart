package com.sporekart.events.security;

import com.sporekart.events.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class EventSecurity {
    private static final Logger log = LoggerFactory.getLogger(EventSecurity.class);

    private boolean securityEnabled = false;
    private final Set<String> allowedPublishers = ConcurrentHashMap.newKeySet();
    private final Map<String, Set<String>> subscriberAccess = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> workspaceIsolation = new ConcurrentHashMap<>();

    public void allowPublisher(String producer) {
        securityEnabled = true;
        allowedPublishers.add(producer);
    }

    public void denyPublisher(String producer) {
        allowedPublishers.remove(producer);
    }

    public void grantSubscriberAccess(String subscriber, String eventType) {
        securityEnabled = true;
        subscriberAccess.computeIfAbsent(subscriber, k -> ConcurrentHashMap.newKeySet()).add(eventType);
    }

    public void isolateWorkspace(String workspaceId, String... allowedProducers) {
        securityEnabled = true;
        workspaceIsolation.put(workspaceId, Set.of(allowedProducers));
    }

    public boolean validatePublisher(Event event) {
        if (!securityEnabled) return true;
        return allowedPublishers.contains(event.getProducer());
    }

    public boolean validateConsumer(String subscriber, Event event) {
        Set<String> allowed = subscriberAccess.get(subscriber);
        if (allowed == null || allowed.isEmpty()) return true;
        return allowed.contains(event.getEventType()) || allowed.contains("*");
    }

    public boolean validateWorkspace(Event event) {
        Set<String> allowed = workspaceIsolation.get(event.getWorkspaceId());
        if (allowed == null) return true;
        return allowed.contains(event.getProducer());
    }
}
