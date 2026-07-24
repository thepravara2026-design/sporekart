package com.sporekart.events.bus;

import com.sporekart.events.model.Event;

@FunctionalInterface
public interface EventPublisher {
    void publish(Event event);
}
