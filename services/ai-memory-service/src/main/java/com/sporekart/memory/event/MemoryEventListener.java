package com.sporekart.memory.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MemoryEventListener {

    private static final Logger log = LoggerFactory.getLogger(MemoryEventListener.class);

    @EventListener
    public void handleMemoryCreated(MemoryCreatedEvent event) {
        log.info("Memory created: id={}, title={}, type={}, workspace={}",
                event.memoryId(), event.title(), event.memoryType(), event.workspace());
    }

    @EventListener
    public void handleMemoryUpdated(MemoryUpdatedEvent event) {
        log.info("Memory updated: id={}, workspace={}", event.memoryId(), event.workspace());
    }

    @EventListener
    public void handleMemoryDeleted(MemoryDeletedEvent event) {
        log.info("Memory deleted: id={}", event.memoryId());
    }
}
