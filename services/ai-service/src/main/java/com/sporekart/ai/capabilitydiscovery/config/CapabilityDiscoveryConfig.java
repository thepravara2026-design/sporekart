package com.sporekart.ai.capabilitydiscovery.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "sporekart.ai.capability-discovery")
public class CapabilityDiscoveryConfig {

    private static final Logger log = LoggerFactory.getLogger(CapabilityDiscoveryConfig.class);

    private boolean enabled = true;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        log.info("Capability Discovery module enabled flag set to: {}", enabled);
    }
}
