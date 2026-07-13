package com.sporekart.ai.assistant.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "ai_assistant_intents")
public class AssistantIntentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "session_id", nullable = false)
    private UUID sessionId;

    @Column(name = "user_input", columnDefinition = "TEXT")
    private String userInput;

    @Column(name = "resolved_intent", length = 100)
    private String resolvedIntent;

    @Column(name = "confidence")
    private Double confidence;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "priority", length = 20)
    private String priority;

    @Column(name = "metadata", columnDefinition = "TEXT")
    private String metadata;

    @Column(name = "entities", columnDefinition = "TEXT")
    private String entities;

    @Column(name = "fallback_intent", length = 100)
    private String fallbackIntent;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "resolved_at")
    private OffsetDateTime resolvedAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    public AssistantIntentEntity() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getSessionId() { return sessionId; }
    public void setSessionId(UUID sessionId) { this.sessionId = sessionId; }
    public String getUserInput() { return userInput; }
    public void setUserInput(String userInput) { this.userInput = userInput; }
    public String getResolvedIntent() { return resolvedIntent; }
    public void setResolvedIntent(String resolvedIntent) { this.resolvedIntent = resolvedIntent; }
    public Double getConfidence() { return confidence; }
    public void setConfidence(Double confidence) { this.confidence = confidence; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public String getMetadata() { return metadata; }
    public void setMetadata(String metadata) { this.metadata = metadata; }
    public String getEntities() { return entities; }
    public void setEntities(String entities) { this.entities = entities; }
    public String getFallbackIntent() { return fallbackIntent; }
    public void setFallbackIntent(String fallbackIntent) { this.fallbackIntent = fallbackIntent; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public OffsetDateTime getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(OffsetDateTime resolvedAt) { this.resolvedAt = resolvedAt; }
    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }
}
