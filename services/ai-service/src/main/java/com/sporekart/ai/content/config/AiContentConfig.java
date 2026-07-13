package com.sporekart.ai.content.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ai.features.content")
public class AiContentConfig {

    private boolean enabled = true;
    private Generation generation = new Generation();
    private Cache cache = new Cache();

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public Generation getGeneration() { return generation; }
    public void setGeneration(Generation generation) { this.generation = generation; }
    public Cache getCache() { return cache; }
    public void setCache(Cache cache) { this.cache = cache; }

    public static class Generation {
        private int maxLength = 2000;
        private String defaultTone = "neutral";

        public int getMaxLength() { return maxLength; }
        public void setMaxLength(int maxLength) { this.maxLength = maxLength; }
        public String getDefaultTone() { return defaultTone; }
        public void setDefaultTone(String defaultTone) { this.defaultTone = defaultTone; }
    }

    public static class Cache {
        private int ttlMinutes = 15;

        public int getTtlMinutes() { return ttlMinutes; }
        public void setTtlMinutes(int ttlMinutes) { this.ttlMinutes = ttlMinutes; }
    }
}
