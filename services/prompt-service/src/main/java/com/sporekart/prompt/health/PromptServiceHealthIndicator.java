package com.sporekart.prompt.health;

import com.sporekart.prompt.repository.PromptTemplateRepository;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class PromptServiceHealthIndicator implements HealthIndicator {

    private final PromptTemplateRepository templateRepository;

    public PromptServiceHealthIndicator(PromptTemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    @Override
    public Health health() {
        try {
            var count = templateRepository.count();
            return Health.up()
                    .withDetail("templates", count)
                    .withDetail("service", "Prompt Management Platform")
                    .build();
        } catch (Exception e) {
            return Health.down()
                    .withDetail("error", e.getMessage())
                    .build();
        }
    }
}
