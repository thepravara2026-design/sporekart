package com.sporekart.ai.core.config;

import com.sporekart.ai.core.domain.AiModule;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "sporekart.ai.modules")
public class ModuleConfiguration {
    private Map<String, ModuleConfig> modules = new LinkedHashMap<>();

    public Map<String, ModuleConfig> getModules() { return modules; }
    public void setModules(Map<String, ModuleConfig> modules) { this.modules = modules; }

    public boolean isModuleEnabled(AiModule module) {
        ModuleConfig config = modules.get(module.name().toLowerCase());
        return config == null || config.isEnabled();
    }

    public static class ModuleConfig {
        private boolean enabled = true;
        private int rateLimit = 100;
        private String rateLimitDuration = "1m";

        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
        public int getRateLimit() { return rateLimit; }
        public void setRateLimit(int rateLimit) { this.rateLimit = rateLimit; }
        public String getRateLimitDuration() { return rateLimitDuration; }
        public void setRateLimitDuration(String rateLimitDuration) { this.rateLimitDuration = rateLimitDuration; }
    }
}
