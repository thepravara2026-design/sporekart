package com.sporekart.events.dlq;

import com.sporekart.events.model.Event;

import java.util.List;
import java.util.Optional;

public interface DeadLetterQueue {
    void sendToDeadLetter(Event event, String subscriberName, String failureReason);
    Optional<DeadLetterRecord> getRecord(String eventId);
    List<DeadLetterRecord> getAll();
    List<DeadLetterRecord> getByEventType(String eventType);
    List<DeadLetterRecord> getBySubscriber(String subscriberName);
    boolean replay(String eventId);
    int replayAll();
    int count();
    void clear();
}
