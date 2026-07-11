package com.sporekart.ai.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaAiEventPublisher implements AiEventPublisher {
    private static final Logger log = LoggerFactory.getLogger(KafkaAiEventPublisher.class);
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaAiEventPublisher(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(String eventType, Object payload) {
        try {
            String message;
            if (payload instanceof String s) {
                message = s;
            } else {
                message = new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(payload);
            }
            kafkaTemplate.send("erp-integration-events", eventType, message);
        } catch (Exception e) {
            log.warn("Failed to publish event {}: {}", eventType, e.getMessage());
        }
    }
}
