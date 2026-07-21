package com.sporekart.ai.runtime.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AgentExecutionRepository extends JpaRepository<AgentExecutionEntity, UUID> {

    List<AgentExecutionEntity> findByAgentIdOrderByStartedAtDesc(UUID agentId);

    List<AgentExecutionEntity> findByStatus(String status);

    long countByAgentId(UUID agentId);

    long countByStatus(String status);
}
