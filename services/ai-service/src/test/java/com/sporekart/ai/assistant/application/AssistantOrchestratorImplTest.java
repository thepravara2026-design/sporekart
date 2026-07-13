package com.sporekart.ai.assistant.application;

import com.sporekart.ai.assistant.api.CopilotOrchestrator;
import com.sporekart.ai.assistant.api.IntentResolver;
import com.sporekart.ai.assistant.api.TaskPlanner;
import com.sporekart.ai.assistant.domain.AssistantExecution;
import com.sporekart.ai.assistant.domain.AssistantIntent;
import com.sporekart.ai.assistant.domain.AssistantTask;
import com.sporekart.ai.assistant.domain.CopilotType;
import com.sporekart.ai.assistant.domain.IntentPriority;
import com.sporekart.ai.assistant.domain.IntentResult;
import com.sporekart.ai.assistant.domain.IntentStatus;
import com.sporekart.ai.assistant.domain.TaskPlan;
import com.sporekart.ai.assistant.domain.TaskStatus;
import com.sporekart.ai.assistant.infrastructure.kafka.AssistantKafkaEventPublisher;
import com.sporekart.ai.assistant.infrastructure.monitoring.AssistantMonitoringService;
import com.sporekart.ai.assistant.infrastructure.persistence.AssistantExecutionEntity;
import com.sporekart.ai.assistant.infrastructure.persistence.AssistantExecutionRepository;
import com.sporekart.ai.assistant.infrastructure.persistence.AssistantMetricsRepository;
import com.sporekart.ai.assistant.infrastructure.persistence.AssistantSessionEntity;
import com.sporekart.ai.assistant.infrastructure.persistence.AssistantSessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssistantOrchestratorImplTest {

    @Mock private IntentResolver intentResolver;
    @Mock private TaskPlanner taskPlanner;
    @Mock private CopilotOrchestrator copilotOrchestrator;
    @Mock private AssistantSessionRepository sessionRepository;
    @Mock private AssistantExecutionRepository executionRepository;
    @Mock private AssistantMetricsRepository metricsRepository;
    @Mock private AssistantKafkaEventPublisher eventPublisher;
    @Mock private AssistantMonitoringService monitoringService;

    private AssistantOrchestratorImpl orchestrator;

    @BeforeEach
    void setUp() {
        orchestrator = new AssistantOrchestratorImpl(
                intentResolver, taskPlanner, copilotOrchestrator,
                sessionRepository, executionRepository, metricsRepository,
                eventPublisher, monitoringService);
    }

    private AssistantSessionEntity createSessionEntity(UUID sessionId) {
        var entity = new AssistantSessionEntity();
        entity.setId(sessionId);
        entity.setAssistantId(UUID.randomUUID());
        entity.setUserId(UUID.randomUUID());
        entity.setConversationId(UUID.randomUUID());
        entity.setStatus("ACTIVE");
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        entity.setExpiresAt(OffsetDateTime.now().plusHours(24));
        entity.setIsDeleted(false);
        return entity;
    }

    @Test
    void shouldChatAndReturnResponse() {
        var sessionId = UUID.randomUUID();
        var message = "search for product";
        var intentResult = new IntentResult("product_search", 0.95, IntentPriority.HIGH,
                Map.of(), List.of("search", "product"), false);
        var intent = new AssistantIntent(UUID.randomUUID(), sessionId, message,
                "product_search", 0.95, IntentStatus.RESOLVED, IntentPriority.HIGH,
                Map.of(), List.of(), null, OffsetDateTime.now(), OffsetDateTime.now());
        var taskPlan = new TaskPlan(UUID.randomUUID(), List.of(), Map.of(), 0, false);
        var sessionEntity = createSessionEntity(sessionId);
        var copilotResponse = new com.sporekart.ai.assistant.api.AssistantResponse(
                sessionId, "Product copilot processed", "product_search",
                0.95, List.of(), Map.of("copilot", "PRODUCT"), false, OffsetDateTime.now());

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(sessionEntity));
        when(intentResolver.resolveIntent(eq(message), anyList())).thenReturn(intentResult);
        when(intentResolver.classifyIntent(sessionId, message)).thenReturn(intent);
        when(taskPlanner.planTasks(intent, message)).thenReturn(taskPlan);
        when(copilotOrchestrator.handleRequest(any(), eq(message), eq(intent))).thenReturn(copilotResponse);
        when(taskPlanner.executeTaskPlan(taskPlan)).thenReturn(List.of());

        var response = orchestrator.chat(sessionId, message);

        assertNotNull(response);
        assertEquals(sessionId, response.sessionId());
        assertEquals("product_search", response.intent());
        assertEquals(0.95, response.confidence());

        verify(eventPublisher).publishAssistantInvoked(eq(sessionId), any());
        verify(eventPublisher).publishIntentResolved(sessionId, "product_search", 0.95);
        verify(eventPublisher).publishTaskPlanned(sessionId, 0);
        verify(eventPublisher).publishResponseGenerated(sessionId);
        verify(monitoringService).recordAssistantUsage(any());
    }

    @Test
    void shouldHandleChatErrorAndReturnErrorResponse() {
        var sessionId = UUID.randomUUID();
        var message = "search";

        when(sessionRepository.findById(sessionId)).thenThrow(new RuntimeException("DB error"));

        var response = orchestrator.chat(sessionId, message);

        assertNotNull(response);
        assertTrue(response.message().contains("error"));
        assertEquals("error", response.intent());
    }

    @Test
    void shouldCreateNewSessionWhenNotFound() {
        var sessionId = UUID.randomUUID();
        var message = "help";
        var intentResult = new IntentResult("customer_support", 0.8, IntentPriority.HIGH,
                Map.of(), List.of(), false);
        var intent = new AssistantIntent(UUID.randomUUID(), sessionId, message,
                "customer_support", 0.8, IntentStatus.RESOLVED, IntentPriority.HIGH,
                Map.of(), List.of(), null, OffsetDateTime.now(), OffsetDateTime.now());
        var taskPlan = new TaskPlan(UUID.randomUUID(), List.of(), Map.of(), 0, false);
        var copilotResponse = new com.sporekart.ai.assistant.api.AssistantResponse(
                sessionId, "Support copilot processed", "customer_support",
                0.8, List.of(), Map.of(), false, OffsetDateTime.now());

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());
        when(sessionRepository.save(any())).thenReturn(createSessionEntity(sessionId));
        when(intentResolver.resolveIntent(eq(message), anyList())).thenReturn(intentResult);
        when(intentResolver.classifyIntent(sessionId, message)).thenReturn(intent);
        when(taskPlanner.planTasks(intent, message)).thenReturn(taskPlan);
        when(copilotOrchestrator.handleRequest(any(), eq(message), eq(intent))).thenReturn(copilotResponse);
        when(taskPlanner.executeTaskPlan(taskPlan)).thenReturn(List.of());

        var response = orchestrator.chat(sessionId, message);

        assertNotNull(response);
        verify(sessionRepository).save(any());
    }

    @Test
    void shouldResolveIntent() {
        var message = "search product";
        var intentResult = new IntentResult("product_search", 0.9, IntentPriority.HIGH,
                Map.of(), List.of("search"), false);
        when(intentResolver.resolveIntent(eq(message), anyList())).thenReturn(intentResult);

        var result = orchestrator.resolveIntent(message);

        assertEquals("product_search", result.intent());
        assertEquals(0.9, result.confidence());
    }

    @Test
    void shouldCreateTaskPlan() {
        var intent = new AssistantIntent(UUID.randomUUID(), UUID.randomUUID(), "input",
                "product_search", 0.9, IntentStatus.RESOLVED, IntentPriority.HIGH,
                Map.of(), List.of(), null, OffsetDateTime.now(), OffsetDateTime.now());
        var taskPlan = new TaskPlan(UUID.randomUUID(), List.of(), Map.of(), 0, false);
        when(taskPlanner.planTasks(intent, "input")).thenReturn(taskPlan);

        var result = orchestrator.createTaskPlan(intent);

        assertNotNull(result);
        assertEquals(taskPlan.id(), result.id());
    }

    @Test
    void shouldGetStatistics() {
        var stats = orchestrator.getStatistics();

        assertNotNull(stats);
        assertEquals("operational", stats.get("status"));
        assertTrue(stats.containsKey("totalRequests"));
        assertTrue(stats.containsKey("activeSessions"));
    }

    @Test
    void shouldGetExecutionHistory() {
        var sessionId = UUID.randomUUID();
        var entity = new AssistantExecutionEntity();
        entity.setId(UUID.randomUUID());
        entity.setAssistantId(UUID.randomUUID());
        entity.setSessionId(sessionId);
        entity.setCopilotType("PRODUCT");
        entity.setAction("search");
        entity.setSuccess(true);
        entity.setLatencyMs(100L);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setCompletedAt(OffsetDateTime.now());
        entity.setIsDeleted(false);

        when(executionRepository.findBySessionIdAndIsDeletedFalse(sessionId)).thenReturn(List.of(entity));

        var history = orchestrator.getExecutionHistory(sessionId);

        assertFalse(history.isEmpty());
        assertEquals(1, history.size());
        assertEquals(CopilotType.PRODUCT, history.getFirst().copilotType());
    }

    @Test
    void shouldReturnEmptyExecutionHistoryWhenNoneExists() {
        var sessionId = UUID.randomUUID();
        when(executionRepository.findBySessionIdAndIsDeletedFalse(sessionId)).thenReturn(List.of());

        var history = orchestrator.getExecutionHistory(sessionId);

        assertTrue(history.isEmpty());
    }

    @Test
    void shouldHandleErrorInExecutionHistory() {
        var sessionId = UUID.randomUUID();
        when(executionRepository.findBySessionIdAndIsDeletedFalse(sessionId))
                .thenThrow(new RuntimeException("DB error"));

        assertThrows(RuntimeException.class, () -> orchestrator.getExecutionHistory(sessionId));
    }

    @Test
    void shouldIncludeTasksInResponse() {
        var sessionId = UUID.randomUUID();
        var message = "search";
        var intentResult = new IntentResult("product_search", 0.95, IntentPriority.HIGH,
                Map.of(), List.of(), false);
        var intent = new AssistantIntent(UUID.randomUUID(), sessionId, message,
                "product_search", 0.95, IntentStatus.RESOLVED, IntentPriority.HIGH,
                Map.of(), List.of(), null, OffsetDateTime.now(), OffsetDateTime.now());
        var task = new AssistantTask(UUID.randomUUID(), sessionId, intent.id(),
                "QueryProducts", "Search product catalog", TaskStatus.COMPLETED,
                1, Map.of(), Map.of("result", "done"), List.of(), 0, 3, 5000,
                null, OffsetDateTime.now(), OffsetDateTime.now(), OffsetDateTime.now());
        var taskPlan = new TaskPlan(UUID.randomUUID(), List.of(task), Map.of(), 5000, false);
        var sessionEntity = createSessionEntity(sessionId);
        var copilotResponse = new com.sporekart.ai.assistant.api.AssistantResponse(
                sessionId, "Product copilot processed", "product_search",
                0.95, List.of(task), Map.of(), false, OffsetDateTime.now());

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(sessionEntity));
        when(intentResolver.resolveIntent(eq(message), anyList())).thenReturn(intentResult);
        when(intentResolver.classifyIntent(sessionId, message)).thenReturn(intent);
        when(taskPlanner.planTasks(intent, message)).thenReturn(taskPlan);
        when(copilotOrchestrator.handleRequest(any(), eq(message), eq(intent))).thenReturn(copilotResponse);
        when(taskPlanner.executeTaskPlan(taskPlan)).thenReturn(List.of(task));

        var response = orchestrator.chat(sessionId, message);

        assertFalse(response.tasks().isEmpty());
    }
}
