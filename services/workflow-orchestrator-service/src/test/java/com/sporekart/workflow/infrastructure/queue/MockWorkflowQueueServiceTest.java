package com.sporekart.workflow.infrastructure.queue;

import com.sporekart.workflow.domain.model.WorkflowState;
import com.sporekart.workflow.domain.model.WorkflowType;
import com.sporekart.workflow.domain.repository.WorkflowRepositoryPort;
import com.sporekart.workflow.infrastructure.persistence.InMemoryWorkflowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MockWorkflowQueueServiceTest {
    private MockWorkflowQueueService queueService;
    private WorkflowRepositoryPort repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryWorkflowRepository();
        queueService = new MockWorkflowQueueService(repository);
    }

    @Test
    void shouldEnqueue() {
        var item = queueService.enqueue("inst-1", "Test", WorkflowType.ORDER, 5);
        assertNotNull(item);
        assertEquals(WorkflowState.QUEUED, item.state());
    }

    @Test
    void shouldDequeue() {
        queueService.enqueue("inst-1", "Test", WorkflowType.ORDER, 5);
        var item = queueService.dequeue();
        assertTrue(item.isPresent());
        assertEquals(WorkflowState.RUNNING, item.get().state());
    }

    @Test
    void shouldReturnEmptyWhenNoItems() {
        var item = queueService.dequeue();
        assertTrue(item.isEmpty());
    }

    @Test
    void shouldGetPendingItems() {
        queueService.enqueue("inst-1", "T1", WorkflowType.ORDER, 3);
        queueService.enqueue("inst-2", "T2", WorkflowType.ORDER, 8);
        var pending = queueService.getPendingItems();
        assertEquals(2, pending.size());
        assertTrue(pending.getFirst().priority() >= pending.getLast().priority());
    }

    @Test
    void shouldGetQueueMetrics() {
        queueService.enqueue("inst-1", "Test", WorkflowType.ORDER, 5);
        var metrics = queueService.getQueueMetrics();
        assertEquals(1, metrics.get("total"));
    }

    @Test
    void shouldClearQueue() {
        queueService.enqueue("inst-1", "Test", WorkflowType.ORDER, 5);
        queueService.clearQueue();
        assertTrue(repository.findAllQueueItems().isEmpty());
    }
}
