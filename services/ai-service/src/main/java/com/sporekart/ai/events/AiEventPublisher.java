package com.sporekart.ai.events;

public interface AiEventPublisher {
    void publish(AiDomainEvent event);
    void publish(IntegrationEvent event);
    void publishToTopic(String topic, String key, String payload);
}
