package com.sporekart.workflow.infrastructure.queue;

import com.sporekart.workflow.domain.model.*;
import com.sporekart.workflow.domain.repository.WorkflowRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MockWorkflowQueueService {
    private final WorkflowRepositoryPort repository;
    private final Random random = new Random();

    public MockWorkflowQueueService(WorkflowRepositoryPort repository) {
        this.repository = repository;
    }

    public WorkflowQueue enqueue(String instanceId, String workflowName, WorkflowType type, int priority) {
        var item = WorkflowQueue.create(instanceId, workflowName, type, priority, "STANDARD", Map.of());
        var saved = repository.saveQueueItem(item);

        repository.saveAudit(WorkflowAudit.create(instanceId, "QUEUED", "system",
            "Workflow " + workflowName + " queued with priority " + priority, "SUCCESS",
            Map.of("queueId", saved.id(), "priority", priority)));

        return saved;
    }

    public Optional<WorkflowQueue> dequeue() {
        var items = repository.findAllQueueItems().stream()
            .filter(q -> q.state() == WorkflowState.QUEUED)
            .sorted(Comparator.comparingInt(WorkflowQueue::priority).reversed()
                .thenComparing(WorkflowQueue::queuedAt))
            .toList();

        if (items.isEmpty()) return Optional.empty();

        var item = items.getFirst();
        var processed = item.withProcessed();
        repository.saveQueueItem(processed);
        return Optional.of(processed);
    }

    public List<WorkflowQueue> getQueueByType(String queueType) {
        return repository.findQueueByType(queueType);
    }

    public List<WorkflowQueue> getQueueByState(WorkflowState state) {
        return repository.findQueueByState(state);
    }

    public List<WorkflowQueue> getPendingItems() {
        return repository.findQueueByState(WorkflowState.QUEUED).stream()
            .sorted(Comparator.comparingInt(WorkflowQueue::priority).reversed())
            .collect(Collectors.toUnmodifiableList());
    }

    public Map<String, Object> getQueueMetrics() {
        var items = repository.findAllQueueItems();
        return Map.of(
            "total", items.size(),
            "queued", items.stream().filter(q -> q.state() == WorkflowState.QUEUED).count(),
            "processing", items.stream().filter(q -> q.state() == WorkflowState.RUNNING).count(),
            "priorityDistribution", Map.of(
                "high", items.stream().filter(q -> q.priority() >= 8).count(),
                "medium", items.stream().filter(q -> q.priority() >= 4 && q.priority() < 8).count(),
                "low", items.stream().filter(q -> q.priority() < 4).count()
            )
        );
    }

    public void clearQueue() {
        repository.clearQueue("STANDARD");
    }
}
