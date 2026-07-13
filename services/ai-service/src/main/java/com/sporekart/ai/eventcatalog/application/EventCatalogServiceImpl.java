package com.sporekart.ai.eventcatalog.application;

import com.sporekart.ai.eventcatalog.api.EventCatalogService;
import com.sporekart.ai.eventcatalog.domain.EventCatalogEntry;
import com.sporekart.ai.eventcatalog.infrastructure.persistence.EventCatalogEntity;
import com.sporekart.ai.eventcatalog.infrastructure.persistence.EventCatalogRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@ConditionalOnProperty(name = "sporekart.ai.event-catalog.enabled", havingValue = "true", matchIfMissing = true)
public class EventCatalogServiceImpl implements EventCatalogService {

    private final EventCatalogRepository repository;

    public EventCatalogServiceImpl(EventCatalogRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public EventCatalogEntry registerEvent(EventCatalogEntry entry) {
        Instant now = Instant.now();
        entry.setCreatedAt(now);
        entry.setUpdatedAt(now);
        EventCatalogEntity entity = new EventCatalogEntity(entry);
        return repository.save(entity).toDomain();
    }

    @Override
    @Transactional
    public EventCatalogEntry updateEvent(String eventId, EventCatalogEntry entry) {
        Optional<EventCatalogEntity> existing = repository.findById(eventId);
        if (existing.isEmpty()) {
            throw new IllegalArgumentException("Event not found: " + eventId);
        }
        EventCatalogEntity entity = existing.get();
        entity.setEventName(entry.getEventName());
        entity.setEventVersion(entry.getEventVersion());
        entity.setModule(entry.getModule());
        entity.setProducer(entry.getProducer());
        entity.setConsumers(entry.getConsumers());
        entity.setPayloadSchema(entry.getPayloadSchema());
        entity.setRetentionDays(entry.getRetentionDays());
        entity.setRetryStrategy(entry.getRetryStrategy());
        entity.setMaxRetries(entry.getMaxRetries());
        entity.setDlqEnabled(entry.isDlqEnabled());
        entity.setDlqTopic(entry.getDlqTopic());
        entity.setDescription(entry.getDescription());
        entity.setDocumentation(entry.getDocumentation());
        entity.setUpdatedAt(Instant.now());
        return repository.save(entity).toDomain();
    }

    @Override
    @Transactional(readOnly = true)
    public EventCatalogEntry getEvent(String eventId) {
        return repository.findById(eventId)
                .map(EventCatalogEntity::toDomain)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + eventId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventCatalogEntry> listEvents() {
        return repository.findAll().stream()
                .map(EventCatalogEntity::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventCatalogEntry> searchByModule(String module) {
        return repository.findByModule(module).stream()
                .map(EventCatalogEntity::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventCatalogEntry> searchByConsumer(String consumer) {
        return repository.findByConsumersContaining(consumer).stream()
                .map(EventCatalogEntity::toDomain)
                .toList();
    }
}
