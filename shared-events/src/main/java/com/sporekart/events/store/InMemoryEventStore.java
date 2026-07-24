package com.sporekart.events.store;

import com.sporekart.events.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class InMemoryEventStore implements EventStore {
    private static final Logger log = LoggerFactory.getLogger(InMemoryEventStore.class);

    private final Map<String, Event> events = new ConcurrentHashMap<>();
    private final List<EventAuditRecord> auditTrail = new CopyOnWriteArrayList<>();
    private final AtomicLong publishedCount = new AtomicLong(0);
    private final AtomicLong consumedCount = new AtomicLong(0);
    private final AtomicLong failedCount = new AtomicLong(0);

    @Override
    public void storePublished(Event event) {
        events.put(event.getEventId().toString(), event);
        publishedCount.incrementAndGet();
        auditTrail.add(new EventAuditRecord(
                event.getEventId().toString(), event.getEventType(),
                "PUBLISH", event.getProducer(), "SUCCESS",
                Instant.now(), 0));
    }

    @Override
    public void storeConsumed(Event event, String subscriber) {
        consumedCount.incrementAndGet();
        auditTrail.add(new EventAuditRecord(
                event.getEventId().toString(), event.getEventType(),
                "CONSUME", subscriber, "SUCCESS",
                Instant.now(), 0));
    }

    @Override
    public void storeFailed(Event event, String reason) {
        failedCount.incrementAndGet();
        auditTrail.add(new EventAuditRecord(
                event.getEventId().toString(), event.getEventType(),
                "FAIL", reason, "FAILED",
                Instant.now(), 0));
    }

    @Override
    public Event getEvent(String eventId) {
        return events.get(eventId);
    }

    @Override
    public List<Event> getEventsByType(String eventType) {
        return events.values().stream()
                .filter(e -> e.getEventType().equals(eventType))
                .sorted(Comparator.comparing(Event::getTimestamp))
                .collect(Collectors.toList());
    }

    @Override
    public List<Event> getEventsByAggregate(String aggregateId) {
        return events.values().stream()
                .filter(e -> e.getAggregateId().equals(aggregateId))
                .sorted(Comparator.comparing(Event::getTimestamp))
                .collect(Collectors.toList());
    }

    @Override
    public List<Event> getAllEvents() {
        return List.copyOf(events.values());
    }

    @Override
    public List<EventAuditRecord> getAuditTrail() {
        return List.copyOf(auditTrail);
    }

    @Override
    public List<EventAuditRecord> getAuditByEventId(String eventId) {
        return auditTrail.stream()
                .filter(a -> a.getEventId().equals(eventId))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Long> getEventTypeCounts() {
        return events.values().stream()
                .collect(Collectors.groupingBy(
                        Event::getEventType, Collectors.counting()));
    }

    @Override
    public long getTotalPublished() { return publishedCount.get(); }
    @Override
    public long getTotalConsumed() { return consumedCount.get(); }
    @Override
    public long getTotalFailed() { return failedCount.get(); }

    @Override
    public void clear() {
        events.clear();
        auditTrail.clear();
        publishedCount.set(0);
        consumedCount.set(0);
        failedCount.set(0);
    }

    public Map<String, Long> getMetrics() {
        return Map.of(
                "published", publishedCount.get(),
                "consumed", consumedCount.get(),
                "failed", failedCount.get(),
                "stored", (long) events.size(),
                "auditEntries", (long) auditTrail.size()
        );
    }
}
