package com.sporekart.events.bus;

import com.sporekart.events.model.Event;

@FunctionalInterface
public interface EventFilter {
    boolean accept(Event event);
}
