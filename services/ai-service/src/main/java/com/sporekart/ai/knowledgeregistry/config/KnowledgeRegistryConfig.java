package com.sporekart.ai.knowledgeregistry.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "sporekart.ai.knowledge-registry")
public class KnowledgeRegistryConfig {

    private static final Logger log = LoggerFactory.getLogger(KnowledgeRegistryConfig.class);

    private boolean enabled = true;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        log.info("Knowledge Registry module enabled flag set to: {}", enabled);
    }
}
