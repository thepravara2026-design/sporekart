package com.sporekart.events.registry;

import java.util.List;
import java.util.Optional;

public interface EventRegistry {
    void register(String eventType, String description, String domain);
    void register(String eventType, String description, String domain, int version);
    void register(String eventType, String description, String domain, int version, EventDescriptor descriptor);
    boolean isRegistered(String eventType);
    Optional<EventDescriptor> getDescriptor(String eventType);
    List<EventDescriptor> getAll();
    List<EventDescriptor> getByDomain(String domain);
    void deprecate(String eventType);
    List<EventDescriptor> search(String query);
    void validate(String eventType);
    int count();
}
