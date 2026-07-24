package com.sporekart.events.store;

import com.sporekart.events.model.Event;

import java.util.List;
import java.util.Map;

public interface EventStore {
    void storePublished(Event event);
    void storeConsumed(Event event, String subscriber);
    void storeFailed(Event event, String reason);
    Event getEvent(String eventId);
    List<Event> getEventsByType(String eventType);
    List<Event> getEventsByAggregate(String aggregateId);
    List<Event> getAllEvents();
    List<EventAuditRecord> getAuditTrail();
    List<EventAuditRecord> getAuditByEventId(String eventId);
    Map<String, Long> getEventTypeCounts();
    long getTotalPublished();
    long getTotalConsumed();
    long getTotalFailed();
    void clear();
}
