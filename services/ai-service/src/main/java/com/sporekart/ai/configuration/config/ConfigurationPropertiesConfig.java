package com.sporekart.ai.configuration.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
    AiConfigurationProperties.class,
    FeatureFlagConfigurationProperties.class,
    RuntimeConfigurationProperties.class,
    CacheConfigurationProperties.class,
    ObservabilityConfigurationProperties.class
})
public class ConfigurationPropertiesConfig {
}
