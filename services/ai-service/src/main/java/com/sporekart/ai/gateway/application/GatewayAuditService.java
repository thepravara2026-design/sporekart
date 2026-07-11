package com.sporekart.ai.gateway.application;

import com.sporekart.ai.gateway.domain.GatewayExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class GatewayAuditService {
    private static final Logger auditLog = LoggerFactory.getLogger("AUDIT");

    public void recordRequestReceived(GatewayExecutionContext context) {
        Map<String, Object> entry = new LinkedHashMap<>();
        entry.put("event", "AIRequestReceived");
        entry.put("executionId", context.executionId());
        entry.put("correlationId", context.correlationId().id());
        entry.put("module", context.module());
        entry.put("userId", context.userId());
        entry.put("timestamp", OffsetDateTime.now().toString());
        auditLog.info("AUDIT: {}", entry);
    }

    public void recordRequestCompleted(GatewayExecutionContext context, boolean success, long durationMs) {
        Map<String, Object> entry = new LinkedHashMap<>();
        entry.put("event", success ? "AIExecutionCompleted" : "AIExecutionFailed");
        entry.put("executionId", context.executionId());
        entry.put("correlationId", context.correlationId().id());
        entry.put("module", context.module());
        entry.put("success", success);
        entry.put("durationMs", durationMs);
        entry.put("timestamp", OffsetDateTime.now().toString());
        auditLog.info("AUDIT: {}", entry);
    }

    public void recordRequestRejected(GatewayExecutionContext context, String reason) {
        Map<String, Object> entry = new LinkedHashMap<>();
        entry.put("event", "AIRequestRejected");
        entry.put("executionId", context.executionId());
        entry.put("correlationId", context.correlationId().id());
        entry.put("reason", reason);
        entry.put("timestamp", OffsetDateTime.now().toString());
        auditLog.info("AUDIT: {}", entry);
    }
}
