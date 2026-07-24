package com.sporekart.events.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DefaultEventRegistry implements EventRegistry {
    private static final Logger log = LoggerFactory.getLogger(DefaultEventRegistry.class);

    private final Map<String, EventDescriptor> registry = new ConcurrentHashMap<>();

    @Override
    public void register(String eventType, String description, String domain) {
        register(eventType, description, domain, 1);
    }

    @Override
    public void register(String eventType, String description, String domain, int version) {
        register(eventType, description, domain, version, null);
    }

    @Override
    public void register(String eventType, String description, String domain, int version, EventDescriptor descriptor) {
        EventDescriptor existing = registry.get(eventType);
        if (existing != null && existing.getVersion() >= version) {
            log.debug("Event type {} already registered at version {}", eventType, existing.getVersion());
            return;
        }
        EventDescriptor desc = descriptor != null ? descriptor :
                new EventDescriptor(eventType, description, domain, version, false, Instant.now(), Map.of());
        registry.put(eventType, desc);
        log.info("Registered event type {} (v{}) in domain {}", eventType, version, domain);
    }

    @Override
    public boolean isRegistered(String eventType) {
        return registry.containsKey(eventType);
    }

    @Override
    public Optional<EventDescriptor> getDescriptor(String eventType) {
        return Optional.ofNullable(registry.get(eventType));
    }

    @Override
    public List<EventDescriptor> getAll() {
        return List.copyOf(registry.values());
    }

    @Override
    public List<EventDescriptor> getByDomain(String domain) {
        return registry.values().stream()
                .filter(d -> d.getDomain().equalsIgnoreCase(domain))
                .collect(Collectors.toList());
    }

    @Override
    public void deprecate(String eventType) {
        EventDescriptor existing = registry.get(eventType);
        if (existing != null) {
            EventDescriptor deprecated = new EventDescriptor(
                    existing.getEventType(), existing.getDescription(),
                    existing.getDomain(), existing.getVersion(),
                    true, existing.getCreatedAt(), existing.getMetadata());
            registry.put(eventType, deprecated);
            log.info("Deprecated event type {}", eventType);
        }
    }

    @Override
    public List<EventDescriptor> search(String query) {
        String lower = query.toLowerCase();
        return registry.values().stream()
                .filter(d -> d.getEventType().toLowerCase().contains(lower)
                        || d.getDescription().toLowerCase().contains(lower)
                        || d.getDomain().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    @Override
    public void validate(String eventType) {
        EventDescriptor desc = registry.get(eventType);
        if (desc == null) {
            throw new IllegalArgumentException("Event type '" + eventType + "' is not registered");
        }
        if (desc.isDeprecated()) {
            throw new IllegalStateException("Event type '" + eventType + "' is deprecated");
        }
    }

    @Override
    public int count() {
        return registry.size();
    }
}
