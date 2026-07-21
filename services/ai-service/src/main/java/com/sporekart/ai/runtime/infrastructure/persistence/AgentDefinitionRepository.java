package com.sporekart.ai.runtime.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AgentDefinitionRepository extends JpaRepository<AgentDefinitionEntity, UUID> {

    List<AgentDefinitionEntity> findByType(String type);

    List<AgentDefinitionEntity> findByStatus(String status);

    List<AgentDefinitionEntity> findByTypeAndStatus(String type, String status);
}
