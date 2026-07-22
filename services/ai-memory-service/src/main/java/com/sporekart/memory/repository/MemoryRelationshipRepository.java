package com.sporekart.memory.repository;

import com.sporekart.memory.persistence.MemoryRelationshipEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MemoryRelationshipRepository extends JpaRepository<MemoryRelationshipEntity, UUID> {
    List<MemoryRelationshipEntity> findBySourceMemoryId(UUID sourceMemoryId);
    List<MemoryRelationshipEntity> findByTargetMemoryId(UUID targetMemoryId);
    List<MemoryRelationshipEntity> findByRelationshipType(String relationshipType);
    void deleteBySourceMemoryIdOrTargetMemoryId(UUID sourceId, UUID targetId);
}
