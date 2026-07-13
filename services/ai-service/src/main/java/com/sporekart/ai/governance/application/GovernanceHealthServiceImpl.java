package com.sporekart.ai.governance.application;

import com.sporekart.ai.governance.api.GovernanceHealthService;
import com.sporekart.ai.governance.infrastructure.persistence.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GovernanceHealthServiceImpl implements GovernanceHealthService {

    private final GovernanceRepository governanceRepository;
    private final GovernanceConfigurationRepository configRepository;
    private final GovernanceAuditRepository auditRepository;

    @Override
    public Map<String, Object> checkHealth() {
        Map<String, Object> health = new LinkedHashMap<>();
        health.put("status", isOperational() ? "UP" : "DOWN");
        health.put("service", "governance");
        health.put("timestamp", System.currentTimeMillis());
        health.put("details", Map.of(
            "policies", getTotalPolicies(),
            "configurations", getTotalConfigurations(),
            "auditEntries", getTotalAuditEntries()
        ));
        return health;
    }

    @Override
    public Map<String, Object> getStatus() {
        Map<String, Object> status = new LinkedHashMap<>();
        status.put("operational", isOperational());
        status.put("mode", "DEVELOPMENT");
        status.put("uptime", System.currentTimeMillis());
        return status;
    }

    @Override
    public boolean isOperational() {
        try {
            governanceRepository.count();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Map<String, Object> getMetrics() {
        Map<String, Object> metrics = new LinkedHashMap<>();
        metrics.put("totalPolicies", getTotalPolicies());
        metrics.put("totalConfigurations", getTotalConfigurations());
        metrics.put("totalAuditEntries", getTotalAuditEntries());
        return metrics;
    }

    private long getTotalPolicies() {
        try { return governanceRepository.count(); } catch (Exception e) { return -1; }
    }

    private long getTotalConfigurations() {
        try { return configRepository.count(); } catch (Exception e) { return -1; }
    }

    private long getTotalAuditEntries() {
        try { return auditRepository.count(); } catch (Exception e) { return -1; }
    }
}
