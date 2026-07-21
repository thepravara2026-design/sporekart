package com.sporekart.ai.pipeline.observability;

import com.sporekart.ai.pipeline.PipelineContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PipelineAudit {
    private static final Logger log = LoggerFactory.getLogger(PipelineAudit.class);

    private final List<AuditEntry> auditLog = new ArrayList<>();

    public String recordRequestReceived(PipelineContext context) {
        var entry = new AuditEntry(
                UUID.randomUUID().toString(),
                context.pipelineId(),
                context.request().requestId(),
                "REQUEST_RECEIVED",
                context.request().module(),
                context.request().tenantId(),
                context.request().userId(),
                null,
                Instant.now(),
                "Request received from " + context.request().source());
        auditLog.add(entry);
        log.debug("Audit [{}]: Request received for module={}", entry.auditId(), context.request().module());
        return entry.auditId();
    }

    public String recordProviderSelection(PipelineContext context) {
        var entry = new AuditEntry(
                UUID.randomUUID().toString(),
                context.pipelineId(),
                context.request().requestId(),
                "PROVIDER_SELECTED",
                context.request().module(),
                context.request().tenantId(),
                context.request().userId(),
                null,
                Instant.now(),
                "Provider selected: " + context.selectedProvider()
                        + ", model: " + context.selectedModel());
        auditLog.add(entry);
        return entry.auditId();
    }

    public String recordExecutionStarted(PipelineContext context) {
        var entry = new AuditEntry(
                UUID.randomUUID().toString(),
                context.pipelineId(),
                context.request().requestId(),
                "EXECUTION_STARTED",
                context.request().module(),
                context.request().tenantId(),
                context.request().userId(),
                null,
                Instant.now(),
                "Execution started on " + context.selectedProvider());
        auditLog.add(entry);
        return entry.auditId();
    }

    public String recordExecutionCompleted(PipelineContext context) {
        var response = context.response();
        var details = response != null
                ? "Execution completed: success=" + response.success()
                + ", latency=" + context.elapsed().toMillis() + "ms"
                + ", tokens=" + (response.tokenUsage() != null ? response.tokenUsage().totalTokens() : 0)
                : "Execution completed with no response";
        var entry = new AuditEntry(
                UUID.randomUUID().toString(),
                context.pipelineId(),
                context.request().requestId(),
                response != null && response.success() ? "EXECUTION_COMPLETED" : "EXECUTION_FAILED",
                context.request().module(),
                context.request().tenantId(),
                context.request().userId(),
                null,
                Instant.now(),
                details);
        auditLog.add(entry);
        log.info("Audit [{}]: {}", entry.auditId(), details);
        return entry.auditId();
    }

    public String recordRetry(PipelineContext context, int attempt) {
        var entry = new AuditEntry(
                UUID.randomUUID().toString(),
                context.pipelineId(),
                context.request().requestId(),
                "RETRY",
                context.request().module(),
                context.request().tenantId(),
                context.request().userId(),
                null,
                Instant.now(),
                "Retry attempt " + attempt + " for provider " + context.selectedProvider());
        auditLog.add(entry);
        return entry.auditId();
    }

    public String recordFailure(PipelineContext context) {
        var entry = new AuditEntry(
                UUID.randomUUID().toString(),
                context.pipelineId(),
                context.request().requestId(),
                "FAILURE",
                context.request().module(),
                context.request().tenantId(),
                context.request().userId(),
                null,
                Instant.now(),
                "Pipeline failed: " + context.failureReason());
        auditLog.add(entry);
        log.warn("Audit [{}]: Pipeline failed - {}", entry.auditId(), context.failureReason());
        return entry.auditId();
    }

    public String recordTimeout(PipelineContext context) {
        var entry = new AuditEntry(
                UUID.randomUUID().toString(),
                context.pipelineId(),
                context.request().requestId(),
                "TIMEOUT",
                context.request().module(),
                context.request().tenantId(),
                context.request().userId(),
                null,
                Instant.now(),
                "Pipeline timed out after " + context.elapsed().toMillis() + "ms");
        auditLog.add(entry);
        log.warn("Audit [{}]: Pipeline timed out", entry.auditId());
        return entry.auditId();
    }

    public String recordPipelineCompleted(PipelineContext context) {
        var status = context.failed() ? "FAILED" : "COMPLETED";
        var entry = new AuditEntry(
                UUID.randomUUID().toString(),
                context.pipelineId(),
                context.request().requestId(),
                "PIPELINE_" + status,
                context.request().module(),
                context.request().tenantId(),
                context.request().userId(),
                null,
                Instant.now(),
                "Pipeline completed: status=" + status
                        + ", duration=" + context.elapsed().toMillis() + "ms"
                        + ", provider=" + context.selectedProvider());
        auditLog.add(entry);
        log.info("Audit [{}]: Pipeline {} - {}", entry.auditId(), status,
                context.elapsed().toMillis() + "ms");
        return entry.auditId();
    }

    public List<AuditEntry> getAuditLog() {
        return List.copyOf(auditLog);
    }

    public void clear() {
        auditLog.clear();
    }

    public record AuditEntry(
            String auditId,
            String pipelineId,
            String requestId,
            String action,
            String module,
            String tenantId,
            String userId,
            String auditReference,
            Instant timestamp,
            String details) {}
}
