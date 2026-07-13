package com.sporekart.ai.workflow.application;

import com.sporekart.ai.workflow.api.WorkflowExecutionService;
import com.sporekart.ai.workflow.domain.WorkflowSchedule;
import com.sporekart.ai.workflow.infrastructure.persistence.WorkflowScheduleEntity;
import com.sporekart.ai.workflow.infrastructure.persistence.WorkflowScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkflowSchedulerServiceImplTest {

    @Mock
    private WorkflowScheduleRepository scheduleRepository;

    @Mock
    private WorkflowExecutionService executionService;

    private WorkflowSchedulerServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new WorkflowSchedulerServiceImpl(scheduleRepository, executionService);
    }

    @Test
    void shouldScheduleWorkflowWithValidCron() {
        var workflowId = UUID.randomUUID();
        when(scheduleRepository.findByWorkflowId(workflowId)).thenReturn(List.of());
        when(scheduleRepository.save(any())).thenAnswer(inv -> {
            var entity = inv.<WorkflowScheduleEntity>getArgument(0);
            entity.setId(UUID.randomUUID());
            return entity;
        });

        var schedule = service.scheduleWorkflow(workflowId, "0 0 * * * ?", "UTC");

        assertNotNull(schedule.id());
        assertEquals(workflowId, schedule.workflowId());
        assertEquals("0 0 * * * ?", schedule.cronExpression());
        assertTrue(schedule.isActive());
    }

    @Test
    void shouldThrowWhenSchedulingWithInvalidCron() {
        assertThrows(WorkflowException.class,
                () -> service.scheduleWorkflow(UUID.randomUUID(), "invalid-cron", "UTC"));
    }

    @Test
    void shouldCreateScheduleWithStartAt() {
        var workflowId = UUID.randomUUID();
        var startAt = OffsetDateTime.now();
        var endAt = OffsetDateTime.now().plusDays(30);
        when(scheduleRepository.save(any())).thenAnswer(inv -> {
            var entity = inv.<WorkflowScheduleEntity>getArgument(0);
            entity.setId(UUID.randomUUID());
            return entity;
        });

        var schedule = service.createSchedule(workflowId, "0 0 * * * ?", startAt, endAt, "UTC");

        assertNotNull(schedule.id());
        assertEquals(startAt, schedule.startAt());
        assertEquals(endAt, schedule.endAt());
        assertTrue(schedule.isActive());
    }

    @Test
    void shouldCreateScheduleWithoutStartAt() {
        var workflowId = UUID.randomUUID();
        when(scheduleRepository.save(any())).thenAnswer(inv -> {
            var entity = inv.<WorkflowScheduleEntity>getArgument(0);
            entity.setId(UUID.randomUUID());
            return entity;
        });

        var schedule = service.createSchedule(workflowId, "0 0 * * * ?", null, null, null);

        assertNotNull(schedule.id());
        assertNull(schedule.startAt());
        assertNull(schedule.endAt());
        assertEquals("UTC", schedule.timezone());
    }

    @Test
    void shouldThrowWhenCreatingScheduleWithInvalidCron() {
        assertThrows(WorkflowException.class,
                () -> service.createSchedule(UUID.randomUUID(), "bad", null, null, null));
    }

    @Test
    void shouldUnscheduleWorkflow() {
        var scheduleId = UUID.randomUUID();
        var entity = new WorkflowScheduleEntity();
        entity.setId(scheduleId);
        entity.setActive(true);
        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(entity));

        service.unscheduleWorkflow(scheduleId);

        assertFalse(entity.isActive());
        verify(scheduleRepository).save(entity);
    }

    @Test
    void shouldThrowWhenUnschedulingNonExistent() {
        var scheduleId = UUID.randomUUID();
        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.empty());

        assertThrows(WorkflowException.class, () -> service.unscheduleWorkflow(scheduleId));
    }

    @Test
    void shouldGetSchedule() {
        var workflowId = UUID.randomUUID();
        var entity = new WorkflowScheduleEntity();
        entity.setId(UUID.randomUUID());
        entity.setWorkflowId(workflowId);
        entity.setCronExpression("0 0 * * * ?");
        entity.setActive(true);
        entity.setCreatedAt(OffsetDateTime.now());
        when(scheduleRepository.findByWorkflowId(workflowId)).thenReturn(List.of(entity));

        var result = service.getSchedule(workflowId);

        assertTrue(result.isPresent());
        assertEquals(workflowId, result.get().workflowId());
    }

    @Test
    void shouldReturnEmptyWhenScheduleNotFound() {
        var workflowId = UUID.randomUUID();
        when(scheduleRepository.findByWorkflowId(workflowId)).thenReturn(List.of());

        var result = service.getSchedule(workflowId);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldListSchedules() {
        var workflowId = UUID.randomUUID();
        var entity = new WorkflowScheduleEntity();
        entity.setId(UUID.randomUUID());
        entity.setWorkflowId(workflowId);
        entity.setCronExpression("0 0 * * * ?");
        entity.setActive(true);
        entity.setCreatedAt(OffsetDateTime.now());
        when(scheduleRepository.findByWorkflowId(workflowId)).thenReturn(List.of(entity));

        var schedules = service.listSchedules(workflowId);

        assertEquals(1, schedules.size());
    }

    @Test
    void shouldReturnEmptyListWhenNoSchedules() {
        var workflowId = UUID.randomUUID();
        when(scheduleRepository.findByWorkflowId(workflowId)).thenReturn(List.of());

        var schedules = service.listSchedules(workflowId);

        assertTrue(schedules.isEmpty());
    }

    @Test
    void shouldPauseSchedule() {
        var scheduleId = UUID.randomUUID();
        var entity = new WorkflowScheduleEntity();
        entity.setId(scheduleId);
        entity.setActive(true);
        entity.setCreatedAt(OffsetDateTime.now());
        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(entity));
        when(scheduleRepository.save(any())).thenReturn(entity);

        var schedule = service.pauseSchedule(scheduleId);

        assertFalse(schedule.isActive());
    }

    @Test
    void shouldThrowWhenPausingNonExistentSchedule() {
        var scheduleId = UUID.randomUUID();
        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.empty());

        assertThrows(WorkflowException.class, () -> service.pauseSchedule(scheduleId));
    }

    @Test
    void shouldResumeSchedule() {
        var scheduleId = UUID.randomUUID();
        var entity = new WorkflowScheduleEntity();
        entity.setId(scheduleId);
        entity.setActive(false);
        entity.setCreatedAt(OffsetDateTime.now());
        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(entity));
        when(scheduleRepository.save(any())).thenReturn(entity);

        var schedule = service.resumeSchedule(scheduleId);

        assertTrue(schedule.isActive());
    }

    @Test
    void shouldThrowWhenResumingNonExistentSchedule() {
        var scheduleId = UUID.randomUUID();
        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.empty());

        assertThrows(WorkflowException.class, () -> service.resumeSchedule(scheduleId));
    }
}
