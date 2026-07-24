package com.sporekart.events.routing;

import com.sporekart.events.bus.Subscription;
import com.sporekart.events.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class DefaultEventRouter implements EventRouter {
    private static final Logger log = LoggerFactory.getLogger(DefaultEventRouter.class);

    private final List<RoutingRule> rules = new CopyOnWriteArrayList<>();

    @Override
    public List<Subscription> route(Event event, List<Subscription> subscriptions) {
        String eventType = event.getEventType();

        List<Subscription> matched = subscriptions.stream()
                .filter(sub -> sub.matches(eventType))
                .collect(Collectors.toList());

        for (RoutingRule rule : rules) {
            if (rule.getEventType().equals(eventType) || rule.getEventType().equals("*")) {
                boolean alreadySubscribed = matched.stream()
                        .anyMatch(s -> s.getSubscriberName().equals(rule.getSubscriberName()));
                if (!alreadySubscribed) {
                    log.debug("Routing rule matched: {} -> {}", eventType, rule.getSubscriberName());
                }
            }
        }

        return matched;
    }

    @Override
    public void addRule(RoutingRule rule) {
        rules.add(rule);
        log.debug("Added routing rule: {} -> {}", rule.getEventType(), rule.getSubscriberName());
    }

    @Override
    public List<RoutingRule> getRules() {
        return List.copyOf(rules);
    }
}
