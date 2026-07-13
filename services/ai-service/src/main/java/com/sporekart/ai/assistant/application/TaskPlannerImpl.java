package com.sporekart.ai.assistant.application;

import com.sporekart.ai.assistant.api.TaskPlanner;
import com.sporekart.ai.assistant.domain.AssistantIntent;
import com.sporekart.ai.assistant.domain.AssistantTask;
import com.sporekart.ai.assistant.domain.TaskPlan;
import com.sporekart.ai.assistant.domain.TaskStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class TaskPlannerImpl implements TaskPlanner {

    private static final Logger log = LoggerFactory.getLogger(TaskPlannerImpl.class);

    private final Map<UUID, List<AssistantTask>> taskStore = new ConcurrentHashMap<>();

    private static final Map<String, List<TaskTemplate>> INTENT_TASK_MAP = new HashMap<>();

    record TaskTemplate(String name, String description, int priority, long timeoutMs) {}

    static {
        INTENT_TASK_MAP.put("product_search", List.of(
                new TaskTemplate("QueryProducts", "Search product catalog", 1, 5000),
                new TaskTemplate("FilterResults", "Apply search filters", 2, 3000),
                new TaskTemplate("GetProductDetail", "Retrieve product details", 3, 3000)
        ));
        INTENT_TASK_MAP.put("customer_support", List.of(
                new TaskTemplate("IdentifyIssue", "Identify customer issue", 1, 5000),
                new TaskTemplate("FindSolution", "Find solution for issue", 2, 5000),
                new TaskTemplate("EscalateIfNeeded", "Escalate to support team", 3, 3000)
        ));
        INTENT_TASK_MAP.put("order_status", List.of(
                new TaskTemplate("FetchOrder", "Fetch order details", 1, 3000),
                new TaskTemplate("TrackDelivery", "Track delivery status", 2, 3000),
                new TaskTemplate("EstimateDelivery", "Estimate delivery time", 3, 2000)
        ));
        INTENT_TASK_MAP.put("inventory_check", List.of(
                new TaskTemplate("CheckStock", "Check stock levels", 1, 3000),
                new TaskTemplate("FindWarehouse", "Locate nearest warehouse", 2, 3000),
                new TaskTemplate("EstimateAvailability", "Estimate availability date", 3, 2000)
        ));
        INTENT_TASK_MAP.put("pricing_info", List.of(
                new TaskTemplate("GetBasePrice", "Retrieve base price", 1, 2000),
                new TaskTemplate("CalculateDiscount", "Apply applicable discounts", 2, 3000),
                new TaskTemplate("GetFinalPrice", "Compute final price", 3, 2000)
        ));
        INTENT_TASK_MAP.put("training_request", List.of(
                new TaskTemplate("FindCourses", "Find relevant training courses", 1, 4000),
                new TaskTemplate("CheckPrerequisites", "Check course prerequisites", 2, 2000),
                new TaskTemplate("EnrollLearner", "Enroll user in course", 3, 3000)
        ));
        INTENT_TASK_MAP.put("grower_advisory", List.of(
                new TaskTemplate("AnalyzeSoil", "Analyze soil conditions", 1, 5000),
                new TaskTemplate("RecommendCrop", "Recommend suitable crops", 2, 5000),
                new TaskTemplate("GenerateAdvisory", "Generate grower advisory report", 3, 4000)
        ));
        INTENT_TASK_MAP.put("marketplace_listing", List.of(
                new TaskTemplate("ValidateListing", "Validate listing details", 1, 3000),
                new TaskTemplate("CheckCompliance", "Check marketplace compliance", 2, 3000),
                new TaskTemplate("PublishListing", "Publish to marketplace", 3, 5000)
        ));
        INTENT_TASK_MAP.put("erp_sync", List.of(
                new TaskTemplate("ValidateConnection", "Validate ERP connection", 1, 5000),
                new TaskTemplate("MapData", "Map data fields for sync", 2, 5000),
                new TaskTemplate("ExecuteSync", "Execute data synchronization", 3, 10000)
        ));
        INTENT_TASK_MAP.put("analytics_report", List.of(
                new TaskTemplate("QueryData", "Query analytics data sources", 1, 5000),
                new TaskTemplate("GenerateReport", "Generate analytics report", 2, 5000),
                new TaskTemplate("FormatOutput", "Format report output", 3, 3000)
        ));
        INTENT_TASK_MAP.put("account_admin", List.of(
                new TaskTemplate("VerifyIdentity", "Verify user identity", 1, 3000),
                new TaskTemplate("UpdateProfile", "Update user profile settings", 2, 3000),
                new TaskTemplate("ApplyChanges", "Apply configuration changes", 3, 2000)
        ));
        INTENT_TASK_MAP.put("notification_pref", List.of(
                new TaskTemplate("GetCurrentPrefs", "Get current notification preferences", 1, 2000),
                new TaskTemplate("ValidatePrefs", "Validate preference changes", 2, 2000),
                new TaskTemplate("UpdatePrefs", "Update notification preferences", 3, 2000)
        ));
    }

    @Override
    public TaskPlan planTasks(AssistantIntent intent, String userInput) {
        if (intent == null) {
            log.warn("Cannot plan tasks for null intent");
            return createEmptyTaskPlan();
        }

        String resolvedIntent = intent.resolvedIntent();
        List<TaskTemplate> templates = INTENT_TASK_MAP.getOrDefault(resolvedIntent,
                List.of(new TaskTemplate("DefaultTask", "Process " + resolvedIntent, 1, 5000)));

        List<AssistantTask> tasks = new ArrayList<>();
        List<String> previousTaskIds = new ArrayList<>();
        Map<String, List<String>> dependencies = new HashMap<>();
        int totalDuration = 0;

        for (TaskTemplate template : templates) {
            UUID taskId = UUID.randomUUID();
            Map<String, Object> taskInput = new HashMap<>();
            taskInput.put("intent", resolvedIntent);
            taskInput.put("userInput", userInput);
            taskInput.put("taskName", template.name());

            AssistantTask task = new AssistantTask(
                    taskId,
                    intent.sessionId(),
                    intent.id(),
                    template.name(),
                    template.description(),
                    TaskStatus.PLANNING,
                    template.priority(),
                    taskInput,
                    null,
                    new ArrayList<>(previousTaskIds),
                    0,
                    3,
                    template.timeoutMs(),
                    null,
                    OffsetDateTime.now(),
                    null,
                    null
            );

            tasks.add(task);
            dependencies.put(taskId.toString(), new ArrayList<>(previousTaskIds));
            previousTaskIds.add(taskId.toString());
            totalDuration += template.timeoutMs();
        }

        taskStore.put(intent.sessionId(), new ArrayList<>(tasks));

        log.debug("Planned {} tasks for intent '{}'", tasks.size(), resolvedIntent);
        return new TaskPlan(UUID.randomUUID(), tasks, dependencies, totalDuration, false);
    }

    @Override
    public List<AssistantTask> executeTaskPlan(TaskPlan plan) {
        if (plan == null || plan.tasks() == null) {
            log.warn("Cannot execute null task plan");
            return List.of();
        }

        List<AssistantTask> executedTasks = new ArrayList<>();
        for (AssistantTask task : plan.tasks()) {
            AssistantTask executed = new AssistantTask(
                    task.id(),
                    task.sessionId(),
                    task.intentId(),
                    task.name(),
                    task.description(),
                    TaskStatus.COMPLETED,
                    task.priority(),
                    task.input(),
                    Map.of("result", "Simulated execution of " + task.name()),
                    task.dependencies(),
                    task.retryCount(),
                    task.maxRetries(),
                    task.timeoutMs(),
                    null,
                    task.createdAt(),
                    OffsetDateTime.now(),
                    OffsetDateTime.now()
            );

            executedTasks.add(executed);
        }

        if (!plan.tasks().isEmpty()) {
            UUID sessionId = plan.tasks().getFirst().sessionId();
            taskStore.put(sessionId, new ArrayList<>(executedTasks));
        }

        log.debug("Executed {} tasks from plan {}", executedTasks.size(), plan.id());
        return executedTasks;
    }

    @Override
    public boolean cancelTask(UUID taskId) {
        for (Map.Entry<UUID, List<AssistantTask>> entry : taskStore.entrySet()) {
            List<AssistantTask> tasks = entry.getValue();
            for (int i = 0; i < tasks.size(); i++) {
                if (tasks.get(i).id().equals(taskId)) {
                    AssistantTask current = tasks.get(i);
                    if (current.status() == TaskStatus.COMPLETED || current.status() == TaskStatus.CANCELLED) {
                        log.warn("Cannot cancel task {} in status {}", taskId, current.status());
                        return false;
                    }
                    AssistantTask cancelled = new AssistantTask(
                            current.id(), current.sessionId(), current.intentId(),
                            current.name(), current.description(), TaskStatus.CANCELLED,
                            current.priority(), current.input(), current.output(),
                            current.dependencies(), current.retryCount(), current.maxRetries(),
                            current.timeoutMs(), "Cancelled by user",
                            current.createdAt(), current.startedAt(), OffsetDateTime.now()
                    );
                    tasks.set(i, cancelled);
                    log.info("Task {} cancelled", taskId);
                    return true;
                }
            }
        }
        log.warn("Task {} not found for cancellation", taskId);
        return false;
    }

    @Override
    public List<AssistantTask> getTaskHistory(UUID sessionId) {
        List<AssistantTask> tasks = taskStore.getOrDefault(sessionId, List.of());
        log.debug("Returning {} tasks for session {}", tasks.size(), sessionId);
        return tasks.stream()
                .sorted((a, b) -> a.createdAt().compareTo(b.createdAt()))
                .collect(Collectors.toList());
    }

    private TaskPlan createEmptyTaskPlan() {
        return new TaskPlan(UUID.randomUUID(), List.of(), Map.of(), 0, false);
    }
}
