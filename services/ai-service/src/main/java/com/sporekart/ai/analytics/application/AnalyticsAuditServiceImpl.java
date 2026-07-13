package com.sporekart.ai.analytics.application;

import com.sporekart.ai.analytics.api.AnalyticsAuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsAuditServiceImpl implements AnalyticsAuditService {

    private final Map<UUID, List<Map<String, Object>>> auditStore = new ConcurrentHashMap<>();
    private final List<Map<String, Object>> allAuditLogs = new ArrayList<>();

    @Override
    public void recordAudit(String action, String entityType, UUID entityId, UUID performedBy,
                            Map<String, Object> details, String ipAddress) {
        var record = new LinkedHashMap<String, Object>();
        record.put("id", UUID.randomUUID().toString());
        record.put("action", action);
        record.put("entityType", entityType);
        record.put("entityId", entityId.toString());
        record.put("performedBy", performedBy != null ? performedBy.toString() : "system");
        record.put("details", details);
        record.put("ipAddress", ipAddress);
        record.put("timestamp", Instant.now().toString());

        auditStore.computeIfAbsent(entityId, k -> new ArrayList<>()).add(record);
        allAuditLogs.add(record);
        log.debug("Audit: {} {} {} by {}", action, entityType, entityId, performedBy);
    }

    @Override
    public List<Object> getAuditLogs(UUID entityId) {
        var logs = auditStore.get(entityId);
        if (logs == null) {
            return List.of();
        }
        return new ArrayList<>(logs);
    }

    @Override
    public List<Object> getAuditLogsByDateRange(Instant from, Instant to) {
        return allAuditLogs.stream()
                .filter(record -> {
                    var ts = Instant.parse((String) record.get("timestamp"));
                    return !ts.isBefore(from) && !ts.isAfter(to);
                })
                .collect(Collectors.toList());
    }
}
