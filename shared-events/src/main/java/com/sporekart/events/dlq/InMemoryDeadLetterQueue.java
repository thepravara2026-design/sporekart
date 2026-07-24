package com.sporekart.events.dlq;

import com.sporekart.events.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class InMemoryDeadLetterQueue implements DeadLetterQueue {
    private static final Logger log = LoggerFactory.getLogger(InMemoryDeadLetterQueue.class);

    private final List<DeadLetterRecord> records = new CopyOnWriteArrayList<>();
    private final Map<String, Event> originalEvents = new ConcurrentHashMap<>();

    @Override
    public void sendToDeadLetter(Event event, String subscriberName, String failureReason) {
        DeadLetterRecord record = new DeadLetterRecord(
                event.getEventId().toString(),
                event.getEventType(),
                subscriberName,
                failureReason,
                java.time.Instant.now());
        records.add(record);
        originalEvents.put(event.getEventId().toString(), event);
        log.warn("Event {} sent to DLQ: subscriber={}, reason={}",
                event.getEventId(), subscriberName, failureReason);
    }

    @Override
    public Optional<DeadLetterRecord> getRecord(String eventId) {
        return records.stream()
                .filter(r -> r.getEventId().equals(eventId))
                .findFirst();
    }

    @Override
    public List<DeadLetterRecord> getAll() {
        return List.copyOf(records);
    }

    @Override
    public List<DeadLetterRecord> getByEventType(String eventType) {
        return records.stream()
                .filter(r -> r.getEventType().equals(eventType))
                .collect(Collectors.toList());
    }

    @Override
    public List<DeadLetterRecord> getBySubscriber(String subscriberName) {
        return records.stream()
                .filter(r -> r.getSubscriberName().equals(subscriberName))
                .collect(Collectors.toList());
    }

    @Override
    public boolean replay(String eventId) {
        Optional<DeadLetterRecord> opt = getRecord(eventId);
        if (opt.isEmpty()) return false;

        DeadLetterRecord record = opt.get();
        int index = records.indexOf(record);
        if (index >= 0) {
            records.set(index, record.withReplayed());
            log.info("Replayed event {} from DLQ", eventId);
            return true;
        }
        return false;
    }

    @Override
    public int replayAll() {
        int count = 0;
        for (int i = 0; i < records.size(); i++) {
            records.set(i, records.get(i).withReplayed());
            count++;
        }
        log.info("Replayed {} events from DLQ", count);
        return count;
    }

    @Override
    public int count() {
        return records.size();
    }

    @Override
    public void clear() {
        records.clear();
        originalEvents.clear();
    }
}
