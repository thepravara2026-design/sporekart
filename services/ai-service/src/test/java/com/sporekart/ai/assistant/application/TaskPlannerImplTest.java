package com.sporekart.ai.assistant.application;

import com.sporekart.ai.assistant.domain.AssistantIntent;
import com.sporekart.ai.assistant.domain.AssistantTask;
import com.sporekart.ai.assistant.domain.IntentPriority;
import com.sporekart.ai.assistant.domain.IntentStatus;
import com.sporekart.ai.assistant.domain.TaskPlan;
import com.sporekart.ai.assistant.domain.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TaskPlannerImplTest {

    private TaskPlannerImpl planner;

    @BeforeEach
    void setUp() {
        planner = new TaskPlannerImpl();
    }

    private AssistantIntent createIntent(String resolvedIntent) {
        return new AssistantIntent(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "user input for " + resolvedIntent,
                resolvedIntent,
                0.9,
                IntentStatus.RESOLVED,
                IntentPriority.HIGH,
                Map.of(),
                List.of(),
                null,
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );
    }

    @Test
    void shouldPlanTasksForProductSearchIntent() {
        var intent = createIntent("product_search");
        var plan = planner.planTasks(intent, "search for product");

        assertNotNull(plan);
        assertFalse(plan.tasks().isEmpty());
        assertEquals("QueryProducts", plan.tasks().getFirst().name());
    }

    @Test
    void shouldPlanTasksForCustomerSupportIntent() {
        var intent = createIntent("customer_support");
        var plan = planner.planTasks(intent, "need help");

        assertNotNull(plan);
        assertFalse(plan.tasks().isEmpty());
        assertEquals("IdentifyIssue", plan.tasks().getFirst().name());
    }

    @Test
    void shouldPlanTasksForOrderStatusIntent() {
        var intent = createIntent("order_status");
        var plan = planner.planTasks(intent, "track order");

        assertEquals("FetchOrder", plan.tasks().getFirst().name());
        assertEquals(3, plan.tasks().size());
    }

    @Test
    void shouldReturnEmptyPlanForNullIntent() {
        var plan = planner.planTasks(null, "input");
        assertTrue(plan.tasks().isEmpty());
    }

    @Test
    void shouldCreateDefaultTaskForUnknownIntent() {
        var intent = createIntent("unknown_intent");
        var plan = planner.planTasks(intent, "some input");

        assertFalse(plan.tasks().isEmpty());
        assertEquals("DefaultTask", plan.tasks().getFirst().name());
    }

    @Test
    void shouldSetPlanningStatusOnAllTasks() {
        var intent = createIntent("inventory_check");
        var plan = planner.planTasks(intent, "check stock");

        plan.tasks().forEach(task -> assertEquals(TaskStatus.PLANNING, task.status()));
    }

    @Test
    void shouldBuildDependenciesBetweenTasks() {
        var intent = createIntent("pricing_info");
        var plan = planner.planTasks(intent, "what price");

        assertFalse(plan.dependencies().isEmpty());
        assertEquals(3, plan.tasks().size());
    }

    @Test
    void shouldCalculateTotalDuration() {
        var intent = createIntent("erp_sync");
        var plan = planner.planTasks(intent, "sync erp");

        assertTrue(plan.totalEstimatedDurationMs() > 0);
    }

    @Test
    void shouldExecuteTaskPlanAndReturnCompletedTasks() {
        var intent = createIntent("product_search");
        var plan = planner.planTasks(intent, "search");

        var executed = planner.executeTaskPlan(plan);

        assertFalse(executed.isEmpty());
        executed.forEach(task -> assertEquals(TaskStatus.COMPLETED, task.status()));
        assertNotNull(executed.getFirst().output());
    }

    @Test
    void shouldReturnEmptyListForNullPlan() {
        var executed = planner.executeTaskPlan(null);
        assertTrue(executed.isEmpty());
    }

    @Test
    void shouldCancelPendingTask() {
        var intent = createIntent("product_search");
        var plan = planner.planTasks(intent, "search");
        var taskId = plan.tasks().getFirst().id();

        planner.executeTaskPlan(plan);

        var otherIntent = createIntent("order_status");
        var otherPlan = planner.planTasks(otherIntent, "track");
        var otherTaskId = otherPlan.tasks().getFirst().id();

        var cancelled = planner.cancelTask(otherTaskId);
        assertTrue(cancelled);
    }

    @Test
    void shouldNotCancelCompletedTask() {
        var intent = createIntent("product_search");
        var plan = planner.planTasks(intent, "search");
        var taskId = plan.tasks().getFirst().id();

        planner.executeTaskPlan(plan);

        var result = planner.cancelTask(taskId);
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseForNonExistentTask() {
        var result = planner.cancelTask(UUID.randomUUID());
        assertFalse(result);
    }

    @Test
    void shouldReturnTaskHistoryForSession() {
        var intent = createIntent("product_search");
        var plan = planner.planTasks(intent, "search");
        planner.executeTaskPlan(plan);

        var history = planner.getTaskHistory(intent.sessionId());
        assertFalse(history.isEmpty());
        assertEquals(3, history.size());
    }

    @Test
    void shouldReturnEmptyHistoryWhenNoTasks() {
        var history = planner.getTaskHistory(UUID.randomUUID());
        assertTrue(history.isEmpty());
    }

    @Test
    void shouldAddTasksToStoreOnExecution() {
        var intent = createIntent("analytics_report");
        var plan = planner.planTasks(intent, "analytics");
        planner.executeTaskPlan(plan);

        var history = planner.getTaskHistory(intent.sessionId());
        assertFalse(history.isEmpty());
    }
}
