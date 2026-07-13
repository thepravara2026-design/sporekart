package com.sporekart.ai.eventcatalog.api;

import com.sporekart.ai.eventcatalog.domain.EventCatalogEntry;
import java.util.List;

public interface EventCatalogService {

    EventCatalogEntry registerEvent(EventCatalogEntry entry);

    EventCatalogEntry updateEvent(String eventId, EventCatalogEntry entry);

    EventCatalogEntry getEvent(String eventId);

    List<EventCatalogEntry> listEvents();

    List<EventCatalogEntry> searchByModule(String module);

    List<EventCatalogEntry> searchByConsumer(String consumer);
}
