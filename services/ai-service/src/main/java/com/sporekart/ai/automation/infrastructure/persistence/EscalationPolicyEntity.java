package com.sporekart.ai.automation.infrastructure.persistence;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ai_escalation_policies")
public class EscalationPolicyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "max_levels")
    private int maxLevels;

    @Column(name = "escalation_delay_ms")
    private long escalationDelayMs;

    @Column(name = "escalation_target", length = 255)
    private String escalationTarget;

    @Column(name = "notification_channel", length = 100)
    private String notificationChannel;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public EscalationPolicyEntity() {}

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getMaxLevels() { return maxLevels; }
    public void setMaxLevels(int maxLevels) { this.maxLevels = maxLevels; }
    public long getEscalationDelayMs() { return escalationDelayMs; }
    public void setEscalationDelayMs(long escalationDelayMs) { this.escalationDelayMs = escalationDelayMs; }
    public String getEscalationTarget() { return escalationTarget; }
    public void setEscalationTarget(String escalationTarget) { this.escalationTarget = escalationTarget; }
    public String getNotificationChannel() { return notificationChannel; }
    public void setNotificationChannel(String notificationChannel) { this.notificationChannel = notificationChannel; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public LocalDateTime getDeletedAt() { return deletedAt; }
    public void setDeletedAt(LocalDateTime deletedAt) { this.deletedAt = deletedAt; }
}
