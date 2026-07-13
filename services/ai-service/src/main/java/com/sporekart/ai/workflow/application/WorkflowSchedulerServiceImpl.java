package com.sporekart.ai.workflow.application;

import com.sporekart.ai.workflow.api.WorkflowExecutionService;
import com.sporekart.ai.workflow.api.WorkflowSchedulerService;
import com.sporekart.ai.workflow.domain.WorkflowSchedule;
import com.sporekart.ai.workflow.infrastructure.persistence.WorkflowScheduleEntity;
import com.sporekart.ai.workflow.infrastructure.persistence.WorkflowScheduleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class WorkflowSchedulerServiceImpl implements WorkflowSchedulerService {

    private static final Logger log = LoggerFactory.getLogger(WorkflowSchedulerServiceImpl.class);

    private final WorkflowScheduleRepository scheduleRepository;
    private final WorkflowExecutionService executionService;

    public WorkflowSchedulerServiceImpl(WorkflowScheduleRepository scheduleRepository,
                                        WorkflowExecutionService executionService) {
        this.scheduleRepository = scheduleRepository;
        this.executionService = executionService;
    }

    @Override
    @Transactional
    public WorkflowSchedule scheduleWorkflow(UUID workflowId, String cronExpression, String timezone) {
        if (!CronExpression.isValidExpression(cronExpression)) {
            throw new WorkflowException("Invalid cron expression: " + cronExpression);
        }

        var existing = scheduleRepository.findByWorkflowId(workflowId);
        existing.forEach(scheduleRepository::delete);

        var tz = timezone != null ? ZoneId.of(timezone) : ZoneId.of("UTC");
        var cron = CronExpression.parse(cronExpression);
        var nextExecution = cron.next(OffsetDateTime.now().toInstant().atZone(tz));

        var entity = new WorkflowScheduleEntity();
        entity.setWorkflowId(workflowId);
        entity.setCronExpression(cronExpression);
        entity.setTimezone(timezone != null ? timezone : "UTC");
        entity.setActive(true);
        entity.setStartAt(OffsetDateTime.now());
        entity.setNextExecutionAt(nextExecution != null
                ? OffsetDateTime.ofInstant(nextExecution.toInstant(), tz)
                : null);

        var saved = scheduleRepository.save(entity);
        log.info("Scheduled workflow {} with cron {} in timezone {}", workflowId, cronExpression, entity.getTimezone());
        return toDomain(saved);
    }

    @Override
    @Transactional
    public void unscheduleWorkflow(UUID scheduleId) {
        var entity = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new WorkflowException("Schedule not found: " + scheduleId));
        entity.setActive(false);
        scheduleRepository.save(entity);
        log.info("Unscheduled workflow schedule {}", scheduleId);
    }

    @Override
    @Transactional
    public WorkflowSchedule createSchedule(UUID workflowId, String cronExpression,
                                           OffsetDateTime startAt, OffsetDateTime endAt, String timezone) {
        if (!CronExpression.isValidExpression(cronExpression)) {
            throw new WorkflowException("Invalid cron expression: " + cronExpression);
        }
        var tz = timezone != null ? ZoneId.of(timezone) : ZoneId.of("UTC");
        var entity = new WorkflowScheduleEntity();
        entity.setWorkflowId(workflowId);
        entity.setCronExpression(cronExpression);
        entity.setStartAt(startAt);
        entity.setEndAt(endAt);
        entity.setTimezone(timezone != null ? timezone : "UTC");
        entity.setActive(true);
        entity.setCreatedAt(OffsetDateTime.now());
        var saved = scheduleRepository.save(entity);
        log.info("Created schedule for workflow {} with cron {}", workflowId, cronExpression);
        return toDomain(saved);
    }

    @Override
    public List<WorkflowSchedule> listSchedules(UUID workflowId) {
        return scheduleRepository.findByWorkflowId(workflowId).stream().map(this::toDomain).toList();
    }

    @Override
    @Transactional
    public WorkflowSchedule pauseSchedule(UUID scheduleId) {
        var entity = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new WorkflowException("Schedule not found: " + scheduleId));
        entity.setActive(false);
        var saved = scheduleRepository.save(entity);
        log.info("Paused schedule {}", scheduleId);
        return toDomain(saved);
    }

    @Override
    @Transactional
    public WorkflowSchedule resumeSchedule(UUID scheduleId) {
        var entity = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new WorkflowException("Schedule not found: " + scheduleId));
        entity.setActive(true);
        var saved = scheduleRepository.save(entity);
        log.info("Resumed schedule {}", scheduleId);
        return toDomain(saved);
    }

    @Override
    public Optional<WorkflowSchedule> getSchedule(UUID workflowId) {
        var schedules = scheduleRepository.findByWorkflowId(workflowId);
        return schedules.isEmpty() ? Optional.empty() : Optional.of(toDomain(schedules.get(0)));
    }

    @Override
    @Transactional
    public void processScheduledExecutions() {
        var schedules = scheduleRepository.findByIsActiveTrue();
        var now = OffsetDateTime.now();

        for (var schedule : schedules) {
            if (schedule.getNextExecutionAt() != null && !now.isBefore(schedule.getNextExecutionAt())) {
                log.info("Processing scheduled execution for workflow {}", schedule.getWorkflowId());
                try {
                    executionService.executeWorkflow(schedule.getWorkflowId(), "scheduled", null);

                    var cron = CronExpression.parse(schedule.getCronExpression());
                    var tz = schedule.getTimezone() != null ? ZoneId.of(schedule.getTimezone()) : ZoneId.of("UTC");
                    var nextExecution = cron.next(now.toInstant().atZone(tz));
                    schedule.setLastExecutedAt(now);
                    schedule.setNextExecutionAt(nextExecution != null
                            ? OffsetDateTime.ofInstant(nextExecution.toInstant(), tz)
                            : null);
                    scheduleRepository.save(schedule);
                } catch (Exception e) {
                    log.error("Failed to execute scheduled workflow {}: {}", schedule.getWorkflowId(), e.getMessage());
                }
            }
        }
    }

    private WorkflowSchedule toDomain(WorkflowScheduleEntity entity) {
        return new WorkflowSchedule(
                entity.getId(),
                entity.getWorkflowId(),
                entity.getCronExpression(),
                entity.getStartAt(),
                entity.getEndAt(),
                entity.isActive(),
                entity.getTimezone(),
                entity.getLastExecutedAt(),
                entity.getNextExecutionAt()
        );
    }
}
