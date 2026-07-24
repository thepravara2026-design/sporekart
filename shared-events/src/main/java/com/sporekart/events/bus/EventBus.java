package com.sporekart.events.bus;

import com.sporekart.events.dlq.DeadLetterRecord;
import com.sporekart.events.model.Event;
import com.sporekart.events.model.EventPriority;

import java.util.List;

public interface EventBus extends EventPublisher {
    Subscription subscribe(String eventType, String subscriberName, EventHandler handler);
    Subscription subscribe(String eventType, String subscriberName, EventHandler handler, EventFilter filter);
    void unsubscribe(Subscription subscription);
    void publish(Event event);
    void publishAll(List<Event> events);
    void dispatch(Event event);
    boolean retry(String eventId);
    void deadLetter(String eventId, String reason);
    List<Event> replay(String eventType, int limit);
    List<DeadLetterRecord> getDeadLetterQueue();
    void clear();
    int getQueueDepth();
    int getQueueDepth(EventPriority priority);
}
