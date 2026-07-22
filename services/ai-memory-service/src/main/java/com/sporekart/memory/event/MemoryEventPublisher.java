package com.sporekart.memory.event;

import com.sporekart.memory.persistence.MemoryEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MemoryEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(MemoryEventPublisher.class);

    private final ApplicationEventPublisher applicationEventPublisher;

    public MemoryEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publishMemoryCreated(MemoryEntity memory) {
        var event = new MemoryCreatedEvent(this, memory.getId(), memory.getOwnerId(),
                memory.getWorkspace(), memory.getMemoryType(), memory.getTitle());
        applicationEventPublisher.publishEvent(event);
        log.debug("Published MemoryCreatedEvent for memory {}", memory.getId());
    }

    public void publishMemoryUpdated(MemoryEntity memory) {
        var event = new MemoryUpdatedEvent(this, memory.getId(), memory.getOwnerId(),
                memory.getWorkspace(), memory.getMemoryType());
        applicationEventPublisher.publishEvent(event);
        log.debug("Published MemoryUpdatedEvent for memory {}", memory.getId());
    }

    public void publishMemoryDeleted(UUID memoryId) {
        var event = new MemoryDeletedEvent(this, memoryId);
        applicationEventPublisher.publishEvent(event);
        log.debug("Published MemoryDeletedEvent for memory {}", memoryId);
    }
}
