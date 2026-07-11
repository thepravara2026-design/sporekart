package com.sporekart.ai.knowledge.infrastructure.persistence;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "knowledge_access_log")
public class KnowledgeAccessLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "document_id")
    private KnowledgeDocumentEntity document;

    @Column(nullable = false, length = 100)
    private String action;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "user_role", length = 50)
    private String userRole;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(nullable = false)
    private OffsetDateTime timestamp;

    @Column(name = "duration_ms")
    private Long durationMs;

    @Column(columnDefinition = "TEXT")
    private String details;

    public KnowledgeAccessLogEntity() {}

    public KnowledgeAccessLogEntity(KnowledgeDocumentEntity document, String action) {
        this.document = document;
        this.action = action;
        this.timestamp = OffsetDateTime.now();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public KnowledgeDocumentEntity getDocument() { return document; }
    public void setDocument(KnowledgeDocumentEntity document) { this.document = document; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public String getUserRole() { return userRole; }
    public void setUserRole(String userRole) { this.userRole = userRole; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public OffsetDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(OffsetDateTime timestamp) { this.timestamp = timestamp; }
    public Long getDurationMs() { return durationMs; }
    public void setDurationMs(Long durationMs) { this.durationMs = durationMs; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
}
