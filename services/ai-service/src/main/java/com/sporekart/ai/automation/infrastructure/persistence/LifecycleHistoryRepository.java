package com.sporekart.ai.automation.infrastructure.persistence;

import com.sporekart.ai.automation.domain.LifecycleState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LifecycleHistoryRepository extends JpaRepository<LifecycleState, UUID> {
    List<LifecycleState> findByEntityIdAndEntityTypeOrderByEnteredAtAsc(UUID entityId, String entityType);
}
