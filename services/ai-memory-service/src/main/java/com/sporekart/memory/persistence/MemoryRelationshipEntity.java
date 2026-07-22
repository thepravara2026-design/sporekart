package com.sporekart.memory.persistence;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "ai_memory_relationships")
public class MemoryRelationshipEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "source_memory_id", nullable = false)
    private UUID sourceMemoryId;

    @Column(name = "target_memory_id", nullable = false)
    private UUID targetMemoryId;

    @Column(name = "relationship_type", nullable = false, length = 50)
    private String relationshipType;

    @Column(name = "strength")
    private double strength;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    public MemoryRelationshipEntity() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getSourceMemoryId() { return sourceMemoryId; }
    public void setSourceMemoryId(UUID sourceMemoryId) { this.sourceMemoryId = sourceMemoryId; }
    public UUID getTargetMemoryId() { return targetMemoryId; }
    public void setTargetMemoryId(UUID targetMemoryId) { this.targetMemoryId = targetMemoryId; }
    public String getRelationshipType() { return relationshipType; }
    public void setRelationshipType(String relationshipType) { this.relationshipType = relationshipType; }
    public double getStrength() { return strength; }
    public void setStrength(double strength) { this.strength = strength; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
