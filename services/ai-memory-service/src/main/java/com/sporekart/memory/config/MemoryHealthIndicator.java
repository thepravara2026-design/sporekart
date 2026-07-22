package com.sporekart.memory.config;

import com.sporekart.memory.repository.MemoryRepository;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component("memoryHealthIndicator")
public class MemoryHealthIndicator implements HealthIndicator {

    private final MemoryRepository memoryRepository;

    public MemoryHealthIndicator(MemoryRepository memoryRepository) {
        this.memoryRepository = memoryRepository;
    }

    @Override
    public Health health() {
        try {
            var count = memoryRepository.count();
            return Health.up()
                    .withDetail("totalMemories", count)
                    .withDetail("status", "available")
                    .build();
        } catch (Exception e) {
            return Health.down()
                    .withDetail("error", e.getMessage())
                    .build();
        }
    }
}
