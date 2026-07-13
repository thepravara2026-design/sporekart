package com.sporekart.ai.approval.infrastructure.redis;

import com.sporekart.ai.approval.domain.ApprovalAssignment;
import com.sporekart.ai.approval.domain.ApprovalRequest;
import com.sporekart.ai.approval.domain.ApprovalWorkflow;
import com.sporekart.ai.approval.domain.DomainConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApprovalRedisCacheService {

    private static final String PENDING_PREFIX = "approval:pending:";
    private static final String ASSIGNMENT_PREFIX = "approval:assignment:";
    private static final String CONFIG_PREFIX = "approval:config:";
    private static final String WORKFLOW_PREFIX = "approval:workflow:";
    private static final String STATISTICS_PREFIX = "approval:statistics:";

    private static final long PENDING_TTL = 60;
    private static final long ASSIGNMENT_TTL = 120;
    private static final long CONFIG_TTL = 300;
    private static final long WORKFLOW_TTL = 300;
    private static final long STATISTICS_TTL = 120;

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public void cachePending(String key, ApprovalRequest request) {
        set(PENDING_PREFIX + key, request, PENDING_TTL);
    }

    public ApprovalRequest getPending(String key) {
        return get(PENDING_PREFIX + key, ApprovalRequest.class);
    }

    public void evictPending(String key) {
        redisTemplate.delete(PENDING_PREFIX + key);
    }

    public void cacheAssignment(String key, ApprovalAssignment assignment) {
        set(ASSIGNMENT_PREFIX + key, assignment, ASSIGNMENT_TTL);
    }

    public ApprovalAssignment getAssignment(String key) {
        return get(ASSIGNMENT_PREFIX + key, ApprovalAssignment.class);
    }

    public void cacheConfig(String key, DomainConfig config) {
        set(CONFIG_PREFIX + key, config, CONFIG_TTL);
    }

    public DomainConfig getConfig(String key) {
        return get(CONFIG_PREFIX + key, DomainConfig.class);
    }

    public void evictConfig(String key) {
        redisTemplate.delete(CONFIG_PREFIX + key);
    }

    public void cacheWorkflow(String key, ApprovalWorkflow workflow) {
        set(WORKFLOW_PREFIX + key, workflow, WORKFLOW_TTL);
    }

    public ApprovalWorkflow getWorkflow(String key) {
        return get(WORKFLOW_PREFIX + key, ApprovalWorkflow.class);
    }

    public void cacheStatistics(String key, Map<String, Object> statistics) {
        set(STATISTICS_PREFIX + key, statistics, STATISTICS_TTL);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getStatistics(String key) {
        return get(STATISTICS_PREFIX + key, Map.class);
    }

    public void invalidateAll() {
        redisTemplate.delete(redisTemplate.keys("approval:*"));
        log.info("All approval cache entries invalidated");
    }

    private <T> void set(String key, T value, long ttlSeconds) {
        try {
            redisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(value), ttlSeconds, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Failed to cache value for key: {}", key, e);
        }
    }

    private <T> T get(String key, Class<T> type) {
        try {
            var value = redisTemplate.opsForValue().get(key);
            if (value == null) return null;
            return objectMapper.readValue(value, type);
        } catch (Exception e) {
            log.error("Failed to retrieve cached value for key: {}", key, e);
            return null;
        }
    }
}
