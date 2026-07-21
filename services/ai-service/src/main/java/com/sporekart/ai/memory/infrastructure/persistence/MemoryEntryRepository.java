package com.sporekart.ai.memory.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MemoryEntryRepository extends JpaRepository<MemoryEntryEntity, UUID> {

    List<MemoryEntryEntity> findBySessionId(String sessionId);

    List<MemoryEntryEntity> findByAgentId(String agentId);

    List<MemoryEntryEntity> findByUserId(String userId);

    List<MemoryEntryEntity> findByAgentIdAndType(String agentId, String type);

    long countByType(String type);

    void deleteBySessionId(String sessionId);

    void deleteByAgentId(String agentId);
}
