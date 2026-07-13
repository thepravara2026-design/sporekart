package com.sporekart.ai.automation.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.sporekart.ai.automation.api.AutomationAuditService;
import com.sporekart.ai.automation.domain.*;
import com.sporekart.ai.automation.infrastructure.persistence.ScheduledTaskRepository;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SchedulerServiceImplTest {

    @Mock
    private ScheduledTaskRepository taskRepository;
    @Mock
    private AutomationAuditService auditService;

    private SchedulerServiceImpl scheduler;

    @BeforeEach
    void setUp() {
        scheduler = new SchedulerServiceImpl(taskRepository, auditService);
    }

    @Test
    void createTaskShouldSaveAndAudit() {
        var task = new ScheduledTask(null, "test-task", JobType.HEALTH_CHECK,
            ScheduleFrequency.DAILY, null, Map.of(), true, null, null, null, null);
        var savedTask = new ScheduledTask(UUID.randomUUID(), "test-task", JobType.HEALTH_CHECK,
            ScheduleFrequency.DAILY, null, Map.of(), true, null, null, Instant.now(), Instant.now());

        when(taskRepository.save(any())).thenReturn(savedTask);

        var result = scheduler.createTask(task);

        assertEquals(savedTask.id(), result.id());
        assertEquals("test-task", result.name());
        verify(auditService).recordAudit(eq("SCHEDULED_TASK_CREATED"), any(), any(), any(), any(), any());
    }

    @Test
    void updateTaskShouldUpdateAndAudit() {
        var id = UUID.randomUUID();
        var existing = new ScheduledTask(id, "old", JobType.HEALTH_CHECK,
            ScheduleFrequency.DAILY, null, Map.of(), true, null, null, Instant.now(), Instant.now());
        var update = new ScheduledTask(id, "new-name", JobType.CONFIG_SYNC,
            ScheduleFrequency.HOURLY, null, Map.of(), false, null, null, null, null);

        when(taskRepository.findById(id)).thenReturn(Optional.of(existing));
        when(taskRepository.save(any())).thenReturn(update);

        var result = scheduler.updateTask(id, update);

        assertEquals("new-name", result.name());
        assertEquals(JobType.CONFIG_SYNC, result.jobType());
        assertEquals(ScheduleFrequency.HOURLY, result.frequency());
        assertFalse(result.active());
        verify(auditService).recordAudit(eq("SCHEDULED_TASK_UPDATED"), any(), any(), any(), any(), any());
    }

    @Test
    void updateTaskShouldThrowWhenNotFound() {
        var id = UUID.randomUUID();
        var task = new ScheduledTask(id, "test", JobType.HEALTH_CHECK,
            ScheduleFrequency.ONCE, null, Map.of(), true, null, null, null, null);

        when(taskRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> scheduler.updateTask(id, task));
    }

    @Test
    void deleteTaskShouldDeleteAndAudit() {
        var id = UUID.randomUUID();
        scheduler.deleteTask(id);
        verify(taskRepository).deleteById(id);
        verify(auditService).recordAudit(eq("SCHEDULED_TASK_DELETED"), any(), any(), any(), any(), any());
    }

    @Test
    void getTaskShouldReturnTask() {
        var id = UUID.randomUUID();
        var task = new ScheduledTask(id, "test", JobType.HEALTH_CHECK,
            ScheduleFrequency.DAILY, null, Map.of(), true, null, null, Instant.now(), Instant.now());

        when(taskRepository.findById(id)).thenReturn(Optional.of(task));

        var result = scheduler.getTask(id);

        assertEquals(task, result);
    }

    @Test
    void getTaskShouldReturnNullWhenNotFound() {
        when(taskRepository.findById(any())).thenReturn(Optional.empty());
        assertNull(scheduler.getTask(UUID.randomUUID()));
    }

    @Test
    void getAllTasksShouldReturnAll() {
        var tasks = List.of(
            new ScheduledTask(UUID.randomUUID(), "t1", JobType.HEALTH_CHECK,
                ScheduleFrequency.DAILY, null, Map.of(), true, null, null, Instant.now(), Instant.now())
        );

        when(taskRepository.findAll()).thenReturn(tasks);

        var result = scheduler.getAllTasks();

        assertEquals(tasks, result);
    }

    @Test
    void getActiveTasksShouldReturnActiveOnly() {
        var tasks = List.of(
            new ScheduledTask(UUID.randomUUID(), "t1", JobType.HEALTH_CHECK,
                ScheduleFrequency.DAILY, null, Map.of(), true, null, null, Instant.now(), Instant.now())
        );

        when(taskRepository.findByActiveTrue()).thenReturn(tasks);

        var result = scheduler.getActiveTasks();

        assertEquals(tasks, result);
    }
}
