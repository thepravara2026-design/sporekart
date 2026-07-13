package com.sporekart.ai.automation.infrastructure.persistence;

import com.sporekart.ai.automation.domain.AutomationJob;
import com.sporekart.ai.automation.domain.AutomationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AutomationJobRepository extends JpaRepository<AutomationJob, UUID> {
    List<AutomationJob> findByStatus(AutomationStatus status);
}
