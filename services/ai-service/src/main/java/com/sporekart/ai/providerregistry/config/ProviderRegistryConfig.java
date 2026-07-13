package com.sporekart.ai.providerregistry.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@Configuration
@EnableConfigurationProperties(ProviderRegistryProperties.class)
public class ProviderRegistryConfig {

    @Bean
    @ConditionalOnProperty(name = "sporekart.ai.provider-registry.enabled", havingValue = "true", matchIfMissing = true)
    public ProviderRegistryFeature providerRegistryFeature(ProviderRegistryProperties properties) {
        return new ProviderRegistryFeature(properties.isEnabled());
    }

    public static class ProviderRegistryFeature {
        private final boolean enabled;

        public ProviderRegistryFeature(boolean enabled) {
            this.enabled = enabled;
        }

        public boolean isEnabled() {
            return enabled;
        }
    }
}
