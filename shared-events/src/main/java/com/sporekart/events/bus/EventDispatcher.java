package com.sporekart.events.bus;

import com.sporekart.events.model.Event;

@FunctionalInterface
public interface EventDispatcher {
    void dispatch(Event event);
}
