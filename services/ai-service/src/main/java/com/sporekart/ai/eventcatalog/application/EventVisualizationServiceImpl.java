package com.sporekart.ai.eventcatalog.application;

import com.sporekart.ai.eventcatalog.api.EventVisualizationService;
import com.sporekart.ai.eventcatalog.domain.EventCatalogEntry;
import com.sporekart.ai.eventcatalog.infrastructure.persistence.EventCatalogEntity;
import com.sporekart.ai.eventcatalog.infrastructure.persistence.EventCatalogRepository;
import com.sporekart.ai.eventcatalog.infrastructure.persistence.EventSubscriptionRepository;
import com.sporekart.ai.eventcatalog.interfaces.rest.dto.EventDependencyDto;
import com.sporekart.ai.eventcatalog.interfaces.rest.dto.EventFlowDto;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@ConditionalOnProperty(name = "sporekart.ai.event-catalog.enabled", havingValue = "true", matchIfMissing = true)
public class EventVisualizationServiceImpl implements EventVisualizationService {

    private final EventCatalogRepository catalogRepository;
    private final EventSubscriptionRepository subscriptionRepository;

    public EventVisualizationServiceImpl(EventCatalogRepository catalogRepository,
                                         EventSubscriptionRepository subscriptionRepository) {
        this.catalogRepository = catalogRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public EventFlowDto getEventFlow() {
        List<EventCatalogEntry> events = catalogRepository.findAll().stream()
                .map(e -> e.toDomain())
                .toList();

        List<EventFlowDto.Node> nodes = new ArrayList<>();
        List<EventFlowDto.Edge> edges = new ArrayList<>();

        for (EventCatalogEntry event : events) {
            nodes.add(new EventFlowDto.Node(event.getEventId(), event.getEventName(), "EVENT"));
            if (event.getProducer() != null) {
                nodes.add(new EventFlowDto.Node(event.getProducer(), event.getProducer(), "PRODUCER"));
                edges.add(new EventFlowDto.Edge(event.getProducer(), event.getEventId(), "produces"));
            }
            if (event.getConsumers() != null) {
                for (String consumer : event.getConsumers()) {
                    nodes.add(new EventFlowDto.Node(consumer, consumer, "CONSUMER"));
                    edges.add(new EventFlowDto.Edge(event.getEventId(), consumer, "consumes"));
                }
            }
        }

        EventFlowDto flow = new EventFlowDto();
        flow.setNodes(nodes);
        flow.setEdges(edges);
        return flow;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventDependencyDto> getEventDependencies() {
        List<EventCatalogEntry> events = catalogRepository.findAll().stream()
                .map(e -> e.toDomain())
                .toList();

        List<EventDependencyDto> dependencies = new ArrayList<>();
        for (EventCatalogEntry event : events) {
            EventDependencyDto dependency = new EventDependencyDto();
            dependency.setEventId(event.getEventId());
            dependency.setEventName(event.getEventName());
            dependency.setProducer(event.getProducer());
            dependency.setConsumers(event.getConsumers() != null ? event.getConsumers() : new ArrayList<>());
            dependencies.add(dependency);
        }
        return dependencies;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, List<String>> getModuleEventMap() {
        return catalogRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        e -> e.getModule() == null ? "UNKNOWN" : e.getModule(),
                        Collectors.mapping(EventCatalogEntity::getEventName, Collectors.toList())
                ));
    }
}
