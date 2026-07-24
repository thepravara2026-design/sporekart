package com.sporekart.events.routing;

import com.sporekart.events.bus.Subscription;
import com.sporekart.events.model.Event;

import java.util.List;

public interface EventRouter {
    List<Subscription> route(Event event, List<Subscription> subscriptions);
    void addRule(RoutingRule rule);
    List<RoutingRule> getRules();
}
