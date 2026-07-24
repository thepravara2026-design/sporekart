package com.sporekart.platform.observability;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;

@Configuration
public class ObservabilityConfig {

    private static final Logger log = LoggerFactory.getLogger(ObservabilityConfig.class);

    @Bean
    public ObservedAspect observedAspect(ObservationRegistry observationRegistry) {
        return new ObservedAspect(observationRegistry);
    }

    @Bean
    public ObservationRegistryCustomizer<ObservationRegistry> observationRegistryCustomizer() {
        return registry -> registry
            .observationConfig()
            .observationHandler(new MetricsObservationHandler());
    }

    @Bean
    public CompositeMeterRegistry compositeMeterRegistry() {
        return new CompositeMeterRegistry();
    }

    static class MetricsObservationHandler implements io.micrometer.observation.ObservationHandler<io.micrometer.observation.Observation.Context> {
        @Override
        public void onStart(io.micrometer.observation.Observation.Context context) {
            log.trace("Observation started: {}", context.getName());
        }

        @Override
        public void onStop(io.micrometer.observation.Observation.Context context) {
            log.trace("Observation stopped: {}", context.getName());
        }

        @Override
        public boolean supportsContext(io.micrometer.observation.Observation.Context context) {
            return true;
        }
    }
}
