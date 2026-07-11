package com.sporekart.ai.gateway.config;

import com.sporekart.ai.core.api.AIContextResolver;
import com.sporekart.ai.core.api.ProviderResolver;
import com.sporekart.ai.core.api.RetryStrategy;
import com.sporekart.ai.core.api.TimeoutStrategy;
import com.sporekart.ai.core.application.featureflag.FeatureFlagService;
import com.sporekart.ai.gateway.api.RateLimiter;
import com.sporekart.ai.gateway.application.GatewayContextResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiGatewayConfig {

    @Bean
    RateLimiter rateLimiter() {
        return new com.sporekart.ai.gateway.infrastructure.InMemoryRateLimiter();
    }

    @Bean
    AIContextResolver contextResolver() {
        return new GatewayContextResolver();
    }

    @Bean
    ProviderResolver providerResolver(FeatureFlagService featureFlagService) {
        return new com.sporekart.ai.gateway.infrastructure.DefaultProviderResolver(featureFlagService);
    }

    @Bean
    RetryStrategy retryStrategy() {
        return new com.sporekart.ai.gateway.infrastructure.DefaultRetryStrategy();
    }

    @Bean
    TimeoutStrategy timeoutStrategy() {
        return new com.sporekart.ai.gateway.infrastructure.DefaultTimeoutStrategy();
    }
}
