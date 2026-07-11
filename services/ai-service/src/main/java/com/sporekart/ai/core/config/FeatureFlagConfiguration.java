package com.sporekart.ai.core.config;

import com.sporekart.ai.core.application.featureflag.AiFeatureFlagProperties;
import com.sporekart.ai.core.application.featureflag.FeatureFlagName;
import com.sporekart.ai.core.application.featureflag.FeatureFlagService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeatureFlagConfiguration {

    @Bean
    FeatureFlagService featureFlagService(AiFeatureFlagProperties properties) {
        return new FeatureFlagService(properties);
    }
}
