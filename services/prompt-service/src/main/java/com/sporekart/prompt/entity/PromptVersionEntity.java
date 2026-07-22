package com.sporekart.prompt.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "prompt_versions")
public class PromptVersionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "template_id", nullable = false)
    private UUID templateId;

    @Column(name = "version", nullable = false)
    private Integer version;

    @Column(name = "prompt_body", columnDefinition = "TEXT", nullable = false)
    private String promptBody;

    @Column(name = "system_prompt", columnDefinition = "TEXT")
    private String systemPrompt;

    @Column(name = "developer_prompt", columnDefinition = "TEXT")
    private String developerPrompt;

    @Column(name = "user_prompt", columnDefinition = "TEXT")
    private String userPrompt;

    @Column(name = "few_shot_examples", columnDefinition = "TEXT")
    private String fewShotExamples;

    @Column(name = "conversation_instructions", columnDefinition = "TEXT")
    private String conversationInstructions;

    @Column(name = "safety_constraints", columnDefinition = "TEXT")
    private String safetyConstraints;

    @Column(name = "provider_metadata", columnDefinition = "TEXT")
    private String providerMetadata;

    @Column(name = "variables_json", columnDefinition = "TEXT")
    private String variablesJson;

    @Column(name = "provider_constraints", length = 500)
    private String providerConstraints;

    @Column(name = "temperature")
    private BigDecimal temperature;

    @Column(name = "top_p")
    private BigDecimal topP;

    @Column(name = "max_tokens")
    private Integer maxTokens;

    @Column(name = "is_published")
    private boolean isPublished;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "created_by", nullable = false)
    private UUID createdBy;

    @Column(name = "change_notes", columnDefinition = "TEXT")
    private String changeNotes;

    public PromptVersionEntity() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getTemplateId() { return templateId; }
    public void setTemplateId(UUID templateId) { this.templateId = templateId; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getPromptBody() { return promptBody; }
    public void setPromptBody(String promptBody) { this.promptBody = promptBody; }
    public String getSystemPrompt() { return systemPrompt; }
    public void setSystemPrompt(String systemPrompt) { this.systemPrompt = systemPrompt; }
    public String getDeveloperPrompt() { return developerPrompt; }
    public void setDeveloperPrompt(String developerPrompt) { this.developerPrompt = developerPrompt; }
    public String getUserPrompt() { return userPrompt; }
    public void setUserPrompt(String userPrompt) { this.userPrompt = userPrompt; }
    public String getFewShotExamples() { return fewShotExamples; }
    public void setFewShotExamples(String fewShotExamples) { this.fewShotExamples = fewShotExamples; }
    public String getConversationInstructions() { return conversationInstructions; }
    public void setConversationInstructions(String conversationInstructions) { this.conversationInstructions = conversationInstructions; }
    public String getSafetyConstraints() { return safetyConstraints; }
    public void setSafetyConstraints(String safetyConstraints) { this.safetyConstraints = safetyConstraints; }
    public String getProviderMetadata() { return providerMetadata; }
    public void setProviderMetadata(String providerMetadata) { this.providerMetadata = providerMetadata; }
    public String getVariablesJson() { return variablesJson; }
    public void setVariablesJson(String variablesJson) { this.variablesJson = variablesJson; }
    public String getProviderConstraints() { return providerConstraints; }
    public void setProviderConstraints(String providerConstraints) { this.providerConstraints = providerConstraints; }
    public BigDecimal getTemperature() { return temperature; }
    public void setTemperature(BigDecimal temperature) { this.temperature = temperature; }
    public BigDecimal getTopP() { return topP; }
    public void setTopP(BigDecimal topP) { this.topP = topP; }
    public Integer getMaxTokens() { return maxTokens; }
    public void setMaxTokens(Integer maxTokens) { this.maxTokens = maxTokens; }
    public boolean isPublished() { return isPublished; }
    public void setPublished(boolean published) { isPublished = published; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public UUID getCreatedBy() { return createdBy; }
    public void setCreatedBy(UUID createdBy) { this.createdBy = createdBy; }
    public String getChangeNotes() { return changeNotes; }
    public void setChangeNotes(String changeNotes) { this.changeNotes = changeNotes; }
}
