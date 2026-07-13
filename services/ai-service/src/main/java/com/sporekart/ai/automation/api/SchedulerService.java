package com.sporekart.ai.automation.api;

import com.sporekart.ai.automation.domain.*;
import java.util.List;
import java.util.UUID;

public interface SchedulerService {
    ScheduledTask createTask(ScheduledTask task);
    ScheduledTask updateTask(UUID id, ScheduledTask task);
    void deleteTask(UUID id);
    ScheduledTask getTask(UUID id);
    List<ScheduledTask> getAllTasks();
    List<ScheduledTask> getActiveTasks();
}
