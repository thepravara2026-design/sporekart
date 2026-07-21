package com.sporekart.ai.memory.infrastructure.kafka;

import org.springframework.stereotype.Component;

@Component
public class MemoryKafkaEventPublisher {

    public void publishMemoryCreated(String memoryId) {
    }

    public void publishMemoryUpdated(String memoryId) {
    }

    public void publishMemoryDeleted(String memoryId) {
    }

    public void publishMemoryConsolidated(String policy) {
    }

    public void publishMemoryPruned(int entriesRemoved) {
    }
}
