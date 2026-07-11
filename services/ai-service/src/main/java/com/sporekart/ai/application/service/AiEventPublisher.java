package com.sporekart.ai.application.service;

public interface AiEventPublisher {
    void publish(String eventType, Object payload);
}
