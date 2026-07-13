package com.sporekart.ai.automation.application;

import com.sporekart.ai.automation.api.ExpirationManager;
import com.sporekart.ai.automation.domain.ExpirationPolicy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class ExpirationManagerImpl implements ExpirationManager {

    private final ConcurrentHashMap<UUID, ExpirationPolicy> policies = new ConcurrentHashMap<>();

    @Override
    public List<ExpirationPolicy> findExpiredEntities() {
        var now = Instant.now();
        var expired = new ArrayList<ExpirationPolicy>();
        for (var policy : policies.values()) {
            if (policy.enabled() && policy.ttlMs() > 0) {
                var age = Duration.between(policy.createdAt(), now).toMillis();
                if (age >= policy.ttlMs()) {
                    expired.add(policy);
                }
            }
        }
        log.info("Found {} expired entities", expired.size());
        return expired;
    }

    @Override
    public void applyExpiration(UUID entityId, String entityType) {
        log.info("Applying expiration for {} {}: action={}", entityType, entityId, "ARCHIVE");
    }

    @Override
    public List<ExpirationPolicy> getExpirationPolicies() {
        return List.copyOf(policies.values());
    }
}
