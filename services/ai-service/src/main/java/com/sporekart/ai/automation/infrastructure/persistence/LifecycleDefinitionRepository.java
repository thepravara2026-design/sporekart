package com.sporekart.ai.automation.infrastructure.persistence;

import com.sporekart.ai.automation.domain.LifecycleDefinition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LifecycleDefinitionRepository extends JpaRepository<LifecycleDefinition, UUID> {
}
