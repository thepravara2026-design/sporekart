package com.sporekart.ai.eventcatalog.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.sporekart.ai.eventcatalog.domain.EventCatalogEntry;
import com.sporekart.ai.eventcatalog.domain.EventRetryStrategy;
import com.sporekart.ai.eventcatalog.infrastructure.persistence.EventCatalogEntity;
import com.sporekart.ai.eventcatalog.infrastructure.persistence.EventCatalogRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class EventCatalogServiceTest {

    @Mock
    private EventCatalogRepository repository;

    @InjectMocks
    private EventCatalogServiceImpl service;

    @Test
    void registerEventPersistsEntry() {
        EventCatalogEntry entry = new EventCatalogEntry();
        entry.setEventId("e1");
        entry.setEventName("OrderCreated");
        entry.setModule("order");
        entry.setProducer("order-service");
        entry.setRetryStrategy(EventRetryStrategy.LINEAR);
        entry.setEventVersion(1);

        when(repository.save(any(EventCatalogEntity.class))).thenAnswer(i -> i.getArgument(0));

        EventCatalogEntry saved = service.registerEvent(entry);

        assertNotNull(saved);
        assertEquals("OrderCreated", saved.getEventName());
    }

    @Test
    void searchByModuleReturnsMatchingEvents() {
        EventCatalogEntity entity = new EventCatalogEntity();
        entity.setModule("order");
        entity.setEventName("OrderCreated");
        when(repository.findByModule("order")).thenReturn(List.of(entity));

        List<EventCatalogEntry> result = service.searchByModule("order");

        assertEquals(1, result.size());
        assertEquals("order", result.get(0).getModule());
    }

    @Test
    void searchByConsumerReturnsSubscribedEvents() {
        EventCatalogEntity entity = new EventCatalogEntity();
        entity.setEventName("OrderCreated");
        entity.setConsumers(List.of("billing"));
        when(repository.findByConsumersContaining("billing")).thenReturn(List.of(entity));

        List<EventCatalogEntry> result = service.searchByConsumer("billing");

        assertEquals(1, result.size());
        assertEquals("billing", result.get(0).getConsumers().get(0));
    }
}
