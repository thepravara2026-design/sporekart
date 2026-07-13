package com.sporekart.ai.eventcatalog.interfaces.rest;

import com.sporekart.ai.eventcatalog.api.EventCatalogService;
import com.sporekart.ai.eventcatalog.api.EventSubscriptionService;
import com.sporekart.ai.eventcatalog.api.EventVisualizationService;
import com.sporekart.ai.eventcatalog.domain.EventCatalogEntry;
import com.sporekart.ai.eventcatalog.domain.EventSubscription;
import com.sporekart.ai.eventcatalog.interfaces.rest.dto.EventCatalogRequestDto;
import com.sporekart.ai.eventcatalog.interfaces.rest.dto.EventCatalogResponseDto;
import com.sporekart.ai.eventcatalog.interfaces.rest.dto.EventDependencyDto;
import com.sporekart.ai.eventcatalog.interfaces.rest.dto.EventFlowDto;
import com.sporekart.ai.eventcatalog.interfaces.rest.dto.EventSubscriptionRequestDto;
import com.sporekart.ai.eventcatalog.interfaces.rest.dto.EventSubscriptionResponseDto;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/event-catalog")
public class EventCatalogController {

    private final EventCatalogService catalogService;
    private final EventSubscriptionService subscriptionService;
    private final EventVisualizationService visualizationService;

    public EventCatalogController(EventCatalogService catalogService,
                                  EventSubscriptionService subscriptionService,
                                  EventVisualizationService visualizationService) {
        this.catalogService = catalogService;
        this.subscriptionService = subscriptionService;
        this.visualizationService = visualizationService;
    }

    @PostMapping("/register")
    public ResponseEntity<EventCatalogResponseDto> register(@RequestBody EventCatalogRequestDto dto) {
        EventCatalogEntry entry = toEntry(dto);
        EventCatalogEntry saved = catalogService.registerEvent(entry);
        return ResponseEntity.ok(toResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventCatalogResponseDto> update(@PathVariable("id") String id,
                                                          @RequestBody EventCatalogRequestDto dto) {
        EventCatalogEntry entry = toEntry(dto);
        EventCatalogEntry updated = catalogService.updateEvent(id, entry);
        return ResponseEntity.ok(toResponse(updated));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventCatalogResponseDto> getEvent(@PathVariable("id") String id) {
        return ResponseEntity.ok(toResponse(catalogService.getEvent(id)));
    }

    @GetMapping("/list")
    public ResponseEntity<List<EventCatalogResponseDto>> listEvents() {
        return ResponseEntity.ok(catalogService.listEvents().stream().map(this::toResponse).toList());
    }

    @GetMapping("/module/{module}")
    public ResponseEntity<List<EventCatalogResponseDto>> searchByModule(@PathVariable("module") String module) {
        return ResponseEntity.ok(catalogService.searchByModule(module).stream().map(this::toResponse).toList());
    }

    @GetMapping("/consumer/{consumer}")
    public ResponseEntity<List<EventCatalogResponseDto>> searchByConsumer(@PathVariable("consumer") String consumer) {
        return ResponseEntity.ok(catalogService.searchByConsumer(consumer).stream().map(this::toResponse).toList());
    }

    @PostMapping("/subscribe")
    public ResponseEntity<EventSubscriptionResponseDto> subscribe(@RequestBody EventSubscriptionRequestDto dto) {
        EventSubscription subscription = subscriptionService.subscribe(
                dto.getEventId(), dto.getConsumerName(), dto.getConsumerGroup());
        return ResponseEntity.ok(toResponse(subscription));
    }

    @PostMapping("/unsubscribe")
    public ResponseEntity<Void> unsubscribe(@RequestParam("subscriptionId") String subscriptionId) {
        subscriptionService.unsubscribe(subscriptionId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/subscriptions")
    public ResponseEntity<List<EventSubscriptionResponseDto>> listSubscriptions() {
        return ResponseEntity.ok(subscriptionService.listSubscriptions().stream().map(this::toResponse).toList());
    }

    @GetMapping("/subscriptions/{consumer}")
    public ResponseEntity<List<EventCatalogResponseDto>> subscribedEvents(@PathVariable("consumer") String consumer) {
        return ResponseEntity.ok(subscriptionService.getSubscribedEvents(consumer).stream()
                .map(this::toResponse).toList());
    }

    @GetMapping("/flow")
    public ResponseEntity<EventFlowDto> getFlow() {
        return ResponseEntity.ok(visualizationService.getEventFlow());
    }

    @GetMapping("/dependencies")
    public ResponseEntity<List<EventDependencyDto>> getDependencies() {
        return ResponseEntity.ok(visualizationService.getEventDependencies());
    }

    @GetMapping("/module-map")
    public ResponseEntity<Map<String, List<String>>> getModuleMap() {
        return ResponseEntity.ok(visualizationService.getModuleEventMap());
    }

    private EventCatalogEntry toEntry(EventCatalogRequestDto dto) {
        EventCatalogEntry entry = new EventCatalogEntry();
        entry.setEventId(dto.getEventId());
        entry.setEventName(dto.getEventName());
        entry.setEventVersion(dto.getEventVersion());
        entry.setModule(dto.getModule());
        entry.setProducer(dto.getProducer());
        entry.setConsumers(dto.getConsumers());
        entry.setPayloadSchema(dto.getPayloadSchema());
        entry.setRetentionDays(dto.getRetentionDays());
        entry.setRetryStrategy(dto.getRetryStrategy());
        entry.setMaxRetries(dto.getMaxRetries());
        entry.setDlqEnabled(dto.isDlqEnabled());
        entry.setDlqTopic(dto.getDlqTopic());
        entry.setDescription(dto.getDescription());
        entry.setDocumentation(dto.getDocumentation());
        return entry;
    }

    private EventCatalogResponseDto toResponse(EventCatalogEntry entry) {
        EventCatalogResponseDto dto = new EventCatalogResponseDto();
        dto.setEventId(entry.getEventId());
        dto.setEventName(entry.getEventName());
        dto.setEventVersion(entry.getEventVersion());
        dto.setModule(entry.getModule());
        dto.setProducer(entry.getProducer());
        dto.setConsumers(entry.getConsumers());
        dto.setPayloadSchema(entry.getPayloadSchema());
        dto.setRetentionDays(entry.getRetentionDays());
        dto.setRetryStrategy(entry.getRetryStrategy());
        dto.setMaxRetries(entry.getMaxRetries());
        dto.setDlqEnabled(entry.isDlqEnabled());
        dto.setDlqTopic(entry.getDlqTopic());
        dto.setDescription(entry.getDescription());
        dto.setDocumentation(entry.getDocumentation());
        dto.setCreatedAt(entry.getCreatedAt());
        dto.setUpdatedAt(entry.getUpdatedAt());
        return dto;
    }

    private EventSubscriptionResponseDto toResponse(EventSubscription subscription) {
        EventSubscriptionResponseDto dto = new EventSubscriptionResponseDto();
        dto.setSubscriptionId(subscription.getSubscriptionId());
        dto.setEventId(subscription.getEventId());
        dto.setConsumerName(subscription.getConsumerName());
        dto.setConsumerGroup(subscription.getConsumerGroup());
        dto.setEnabled(subscription.isEnabled());
        return dto;
    }
}
