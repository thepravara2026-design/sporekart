package com.sporekart.platform.observability.tracing;

import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.annotation.Observed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TracingConfig {

    private static final Logger log = LoggerFactory.getLogger(TracingConfig.class);

    @Bean
    @ConditionalOnProperty(name = "sporekart.tracing.enabled", havingValue = "true", matchIfMissing = true)
    public ObservationRegistry observationRegistry() {
        ObservationRegistry registry = ObservationRegistry.create();
        registry.observationConfig().observationHandler(context -> {
            log.trace("Observation: {} (context={})", context.getName(), context.getClass().getSimpleName());
            return true;
        });
        log.info("Observation registry initialized with default handler");
        return registry;
    }
}
