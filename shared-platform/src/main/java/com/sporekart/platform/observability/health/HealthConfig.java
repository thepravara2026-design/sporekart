package com.sporekart.platform.observability.health;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.HealthContributor;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class HealthConfig {

    private static final Logger log = LoggerFactory.getLogger(HealthConfig.class);

    @Bean
    public Map<String, HealthContributor> healthContributors() {
        Map<String, HealthContributor> contributors = new LinkedHashMap<>();
        log.info("Health contributor registry initialized");
        return contributors;
    }
}
