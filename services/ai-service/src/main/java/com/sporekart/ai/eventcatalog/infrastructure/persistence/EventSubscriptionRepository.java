package com.sporekart.ai.eventcatalog.infrastructure.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventSubscriptionRepository extends JpaRepository<EventSubscriptionEntity, String> {

    List<EventSubscriptionEntity> findByConsumerName(String consumerName);

    List<EventSubscriptionEntity> findByEventId(String eventId);
}
