package com.sporekart.ai.automation.infrastructure.persistence;

import com.sporekart.ai.automation.domain.ScheduledTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ScheduledTaskRepository extends JpaRepository<ScheduledTask, UUID> {
    List<ScheduledTask> findByActiveTrue();
}
