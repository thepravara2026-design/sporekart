package com.sporekart.events.bus;

import java.util.UUID;

public class Subscription {
    private final UUID id;
    private final String eventType;
    private final String subscriberName;
    private final EventHandler handler;
    private final EventFilter filter;

    public Subscription(String eventType, String subscriberName, EventHandler handler) {
        this(eventType, subscriberName, handler, null);
    }

    public Subscription(String eventType, String subscriberName, EventHandler handler, EventFilter filter) {
        this.id = UUID.randomUUID();
        this.eventType = eventType;
        this.subscriberName = subscriberName;
        this.handler = handler;
        this.filter = filter;
    }

    public UUID getId() { return id; }
    public String getEventType() { return eventType; }
    public String getSubscriberName() { return subscriberName; }
    public EventHandler getHandler() { return handler; }
    public EventFilter getFilter() { return filter; }

    public boolean matches(String eventType) {
        return this.eventType.equals(eventType) || this.eventType.equals("*");
    }
}
