package com.sporekart.ai.configregistry.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "sporekart.ai.config-registry.enabled", havingValue = "true", matchIfMissing = true)
public class ConfigRegistryConfig {
}
