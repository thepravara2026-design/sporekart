package com.sporekart.ai.automation.application;

import com.sporekart.ai.automation.api.AutomationAuditService;
import com.sporekart.ai.automation.api.SchedulerService;
import com.sporekart.ai.automation.domain.ScheduledTask;
import com.sporekart.ai.automation.infrastructure.persistence.ScheduledTaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerServiceImpl implements SchedulerService {

    private final ScheduledTaskRepository taskRepository;
    private final AutomationAuditService auditService;

    @Override
    public ScheduledTask createTask(ScheduledTask task) {
        var now = Instant.now();
        var toSave = new ScheduledTask(
            task.id() != null ? task.id() : UUID.randomUUID(),
            task.name(), task.jobType(), task.frequency(),
            task.cronExpression(), task.params(), task.active(),
            task.lastRunAt(), task.nextRunAt(), now, now
        );
        var saved = taskRepository.save(toSave);
        auditService.recordAudit("SCHEDULED_TASK_CREATED", "ScheduledTask", saved.id(), null,
            Map.of("name", saved.name(), "frequency", saved.frequency().toString()), null);
        log.info("Created scheduled task: {} with frequency {}", saved.name(), saved.frequency());
        return saved;
    }

    @Override
    public ScheduledTask updateTask(UUID id, ScheduledTask task) {
        var existing = taskRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Scheduled task not found: " + id));
        var now = Instant.now();
        var updated = new ScheduledTask(
            id, task.name(), task.jobType(), task.frequency(),
            task.cronExpression(), task.params(), task.active(),
            task.lastRunAt(), task.nextRunAt(), existing.createdAt(), now
        );
        var saved = taskRepository.save(updated);
        auditService.recordAudit("SCHEDULED_TASK_UPDATED", "ScheduledTask", id, null,
            Map.of("name", saved.name()), null);
        log.info("Updated scheduled task: {}", id);
        return saved;
    }

    @Override
    public void deleteTask(UUID id) {
        taskRepository.deleteById(id);
        auditService.recordAudit("SCHEDULED_TASK_DELETED", "ScheduledTask", id, null,
            Map.of("taskId", id.toString()), null);
        log.info("Deleted scheduled task: {}", id);
    }

    @Override
    public ScheduledTask getTask(UUID id) {
        return taskRepository.findById(id).orElse(null);
    }

    @Override
    public List<ScheduledTask> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public List<ScheduledTask> getActiveTasks() {
        return taskRepository.findByActiveTrue();
    }
}
