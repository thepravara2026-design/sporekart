package com.sporekart.ai.promptregistry.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "sporekart.ai.prompt-registry")
public class PromptRegistryConfig {

    private boolean enabled = true;
    private Registry registry = new Registry();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Registry getRegistry() {
        return registry;
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }

    public static class Registry {
        private int maxVersionsToRetain = 100;
        private boolean autoApprove = false;

        public int getMaxVersionsToRetain() {
            return maxVersionsToRetain;
        }

        public void setMaxVersionsToRetain(int maxVersionsToRetain) {
            this.maxVersionsToRetain = maxVersionsToRetain;
        }

        public boolean isAutoApprove() {
            return autoApprove;
        }

        public void setAutoApprove(boolean autoApprove) {
            this.autoApprove = autoApprove;
        }
    }
}
