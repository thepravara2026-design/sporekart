package com.sporekart.ai.prompt.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "ai_prompt_execution_log")
public class PromptExecutionLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "template_id")
    private UUID templateId;

    @Column(name = "version_number")
    private Integer versionNumber;

    @Column(name = "prompt_text", nullable = false, columnDefinition = "TEXT")
    private String promptText;

    @Column(name = "rendered_text", columnDefinition = "TEXT")
    private String renderedText;

    @Column(columnDefinition = "JSONB")
    private String variables;

    @Column(name = "duration_ms")
    private Long durationMs;

    @Column(nullable = false)
    private boolean success = true;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "executed_by")
    private UUID executedBy;

    @Column(length = 100)
    private String source;

    @Column(name = "correlation_id", length = 255)
    private String correlationId;

    @Column(name = "executed_at", nullable = false)
    private OffsetDateTime executedAt;

    public PromptExecutionLogEntity() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getTemplateId() { return templateId; }
    public void setTemplateId(UUID templateId) { this.templateId = templateId; }
    public Integer getVersionNumber() { return versionNumber; }
    public void setVersionNumber(Integer versionNumber) { this.versionNumber = versionNumber; }
    public String getPromptText() { return promptText; }
    public void setPromptText(String promptText) { this.promptText = promptText; }
    public String getRenderedText() { return renderedText; }
    public void setRenderedText(String renderedText) { this.renderedText = renderedText; }
    public String getVariables() { return variables; }
    public void setVariables(String variables) { this.variables = variables; }
    public Long getDurationMs() { return durationMs; }
    public void setDurationMs(Long durationMs) { this.durationMs = durationMs; }
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public UUID getExecutedBy() { return executedBy; }
    public void setExecutedBy(UUID executedBy) { this.executedBy = executedBy; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public String getCorrelationId() { return correlationId; }
    public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }
    public OffsetDateTime getExecutedAt() { return executedAt; }
    public void setExecutedAt(OffsetDateTime executedAt) { this.executedAt = executedAt; }
}
