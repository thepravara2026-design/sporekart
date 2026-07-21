package com.sporekart.ai.runtime.infrastructure.redis;

import org.springframework.stereotype.Component;

@Component
public class AgentRuntimeRedisCacheService {

    public void cacheAgentDefinition(String key, String value) {
    }

    public String getCachedAgentDefinition(String key) {
        return null;
    }

    public void evictAgentDefinition(String key) {
    }

    public void cacheExecutionResult(String key, String value) {
    }

    public String getCachedExecutionResult(String key) {
        return null;
    }

    public void clearCache() {
    }
}
