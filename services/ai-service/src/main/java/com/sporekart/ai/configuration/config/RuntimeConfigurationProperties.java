package com.sporekart.ai.configuration.config;

import com.sporekart.ai.configuration.domain.RuntimeConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "sporekart.ai.modules.runtime")
public record RuntimeConfigurationProperties(
    RuntimeConfiguration runtime
) {
    public RuntimeConfigurationProperties() {
        this(RuntimeConfiguration.defaults());
    }
}
