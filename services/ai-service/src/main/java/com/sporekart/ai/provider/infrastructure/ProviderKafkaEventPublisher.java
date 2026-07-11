package com.sporekart.ai.provider.infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Profile("!test")
public class ProviderKafkaEventPublisher {
    private static final Logger log = LoggerFactory.getLogger(ProviderKafkaEventPublisher.class);
    private static final String TOPIC = "ai-provider-events";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ProviderKafkaEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishProviderRegistered(String providerName, String version) {
        Map<String, Object> event = buildEvent("ProviderRegistered", providerName);
        event.put("version", version);
        kafkaTemplate.send(TOPIC, providerName, event);
        log.debug("Published ProviderRegistered for {}", providerName);
    }

    public void publishProviderEnabled(String providerName) {
        Map<String, Object> event = buildEvent("ProviderEnabled", providerName);
        kafkaTemplate.send(TOPIC, providerName, event);
    }

    public void publishProviderDisabled(String providerName) {
        Map<String, Object> event = buildEvent("ProviderDisabled", providerName);
        kafkaTemplate.send(TOPIC, providerName, event);
    }

    public void publishProviderHealthChanged(String providerName, boolean healthy, String details) {
        Map<String, Object> event = buildEvent("ProviderHealthChanged", providerName);
        event.put("healthy", healthy);
        event.put("details", details);
        kafkaTemplate.send(TOPIC, providerName, event);
    }

    public void publishProviderSwitched(String fromProvider, String toProvider, String module) {
        Map<String, Object> event = buildEvent("ProviderSwitched", toProvider);
        event.put("fromProvider", fromProvider);
        event.put("module", module);
        kafkaTemplate.send(TOPIC, "switch", event);
    }

    public void publishProviderValidationFailed(String providerName, String reason) {
        Map<String, Object> event = buildEvent("ProviderValidationFailed", providerName);
        event.put("reason", reason);
        kafkaTemplate.send(TOPIC, providerName, event);
    }

    private Map<String, Object> buildEvent(String eventType, String providerName) {
        Map<String, Object> event = new LinkedHashMap<>();
        event.put("event", eventType);
        event.put("providerName", providerName);
        event.put("timestamp", OffsetDateTime.now().toString());
        return event;
    }
}
