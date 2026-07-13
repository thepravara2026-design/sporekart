package com.sporekart.ai.admin.application;

import com.sporekart.ai.admin.api.AdministrationMetricsService;
import com.sporekart.ai.admin.domain.AdminOperationType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
public class AdministrationMetricsServiceImpl implements AdministrationMetricsService {

    private final AtomicLong totalOperations = new AtomicLong(0);
    private final AtomicLong configurationChanges = new AtomicLong(0);
    private final AtomicLong rollbacks = new AtomicLong(0);
    private final AtomicLong featureFlagChanges = new AtomicLong(0);
    private final Map<String, AtomicLong> operationCounters = new ConcurrentHashMap<>();

    @Override
    public long getTotalOperations() {
        return totalOperations.get();
    }

    @Override
    public long getConfigurationChanges() {
        return configurationChanges.get();
    }

    @Override
    public long getRollbackCount() {
        return rollbacks.get();
    }

    @Override
    public long getFeatureFlagChanges() {
        return featureFlagChanges.get();
    }

    @Override
    public Map<String, Long> getOperationDistribution() {
        var breakdown = new HashMap<String, Long>();
        for (var entry : operationCounters.entrySet()) {
            breakdown.put(entry.getKey(), entry.getValue().get());
        }
        return breakdown;
    }

    @Override
    public Map<String, Object> getStatistics() {
        var stats = new HashMap<String, Object>();
        stats.put("totalOperations", totalOperations.get());
        stats.put("configurationChanges", configurationChanges.get());
        stats.put("rollbacks", rollbacks.get());
        stats.put("featureFlagChanges", featureFlagChanges.get());
        stats.put("operationsByType", getOperationDistribution());
        stats.put("status", "operational");
        return stats;
    }

    public void recordOperation(AdminOperationType type) {
        totalOperations.incrementAndGet();
        operationCounters.computeIfAbsent(type.name(), k -> new AtomicLong(0)).incrementAndGet();
    }

    public void recordConfigurationChange() {
        configurationChanges.incrementAndGet();
    }

    public void recordRollback() {
        rollbacks.incrementAndGet();
    }

    public void recordFeatureFlagChange() {
        featureFlagChanges.incrementAndGet();
    }
}
