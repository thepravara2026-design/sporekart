package com.sporekart.events.bus;

import com.sporekart.events.model.Event;

@FunctionalInterface
public interface EventHandler {
    void handle(Event event);
}
