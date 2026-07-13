package com.sporekart.ai.eventcatalog.application;

import com.sporekart.ai.eventcatalog.api.EventSubscriptionService;
import com.sporekart.ai.eventcatalog.domain.EventCatalogEntry;
import com.sporekart.ai.eventcatalog.domain.EventSubscription;
import com.sporekart.ai.eventcatalog.infrastructure.persistence.EventCatalogRepository;
import com.sporekart.ai.eventcatalog.infrastructure.persistence.EventSubscriptionEntity;
import com.sporekart.ai.eventcatalog.infrastructure.persistence.EventSubscriptionRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@ConditionalOnProperty(name = "sporekart.ai.event-catalog.enabled", havingValue = "true", matchIfMissing = true)
public class EventSubscriptionServiceImpl implements EventSubscriptionService {

    private final EventSubscriptionRepository subscriptionRepository;
    private final EventCatalogRepository catalogRepository;

    public EventSubscriptionServiceImpl(EventSubscriptionRepository subscriptionRepository,
                                        EventCatalogRepository catalogRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.catalogRepository = catalogRepository;
    }

    @Override
    @Transactional
    public EventSubscription subscribe(String eventId, String consumerName, String consumerGroup) {
        EventSubscription subscription = new EventSubscription();
        subscription.setSubscriptionId(UUID.randomUUID().toString());
        subscription.setEventId(eventId);
        subscription.setConsumerName(consumerName);
        subscription.setConsumerGroup(consumerGroup);
        subscription.setEnabled(true);
        return subscriptionRepository.save(new EventSubscriptionEntity(subscription)).toDomain();
    }

    @Override
    @Transactional
    public void unsubscribe(String subscriptionId) {
        subscriptionRepository.deleteById(subscriptionId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventSubscription> listSubscriptions() {
        return subscriptionRepository.findAll().stream()
                .map(EventSubscriptionEntity::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventCatalogEntry> getSubscribedEvents(String consumer) {
        return subscriptionRepository.findByConsumerName(consumer).stream()
                .map(s -> catalogRepository.findById(s.getEventId()))
                .filter(Optional::isPresent)
                .map(o -> o.get().toDomain())
                .toList();
    }
}
