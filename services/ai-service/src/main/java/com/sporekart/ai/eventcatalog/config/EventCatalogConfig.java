package com.sporekart.ai.eventcatalog.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "sporekart.ai.event-catalog.enabled", havingValue = "true", matchIfMissing = true)
public class EventCatalogConfig {
}
