package com.sporekart.ai.eventcatalog.api;

import com.sporekart.ai.eventcatalog.domain.EventCatalogEntry;
import com.sporekart.ai.eventcatalog.domain.EventSubscription;
import java.util.List;

public interface EventSubscriptionService {

    EventSubscription subscribe(String eventId, String consumerName, String consumerGroup);

    void unsubscribe(String subscriptionId);

    List<EventSubscription> listSubscriptions();

    List<EventCatalogEntry> getSubscribedEvents(String consumer);
}
