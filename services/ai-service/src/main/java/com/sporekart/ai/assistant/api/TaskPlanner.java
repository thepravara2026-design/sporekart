package com.sporekart.ai.assistant.api;

import com.sporekart.ai.assistant.domain.AssistantIntent;
import com.sporekart.ai.assistant.domain.AssistantTask;
import com.sporekart.ai.assistant.domain.TaskPlan;

import java.util.List;
import java.util.UUID;

public interface TaskPlanner {
    TaskPlan planTasks(AssistantIntent intent, String userInput);
    List<AssistantTask> executeTaskPlan(TaskPlan plan);
    boolean cancelTask(UUID taskId);
    List<AssistantTask> getTaskHistory(UUID sessionId);
}
