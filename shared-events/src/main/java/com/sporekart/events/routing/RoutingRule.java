package com.sporekart.events.routing;

import com.sporekart.events.bus.EventFilter;

public class RoutingRule {
    private final String eventType;
    private final String subscriberName;
    private final EventFilter condition;

    public RoutingRule(String eventType, String subscriberName) {
        this(eventType, subscriberName, null);
    }

    public RoutingRule(String eventType, String subscriberName, EventFilter condition) {
        this.eventType = eventType;
        this.subscriberName = subscriberName;
        this.condition = condition;
    }

    public String getEventType() { return eventType; }
    public String getSubscriberName() { return subscriberName; }
    public EventFilter getCondition() { return condition; }
}
