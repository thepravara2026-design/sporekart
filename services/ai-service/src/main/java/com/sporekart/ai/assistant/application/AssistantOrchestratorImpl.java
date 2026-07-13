package com.sporekart.ai.assistant.application;

import com.sporekart.ai.assistant.api.AssistantOrchestrator;
import com.sporekart.ai.assistant.api.AssistantResponse;
import com.sporekart.ai.assistant.domain.AssistantExecution;
import com.sporekart.ai.assistant.domain.AssistantIntent;
import com.sporekart.ai.assistant.domain.IntentResult;
import com.sporekart.ai.assistant.domain.TaskPlan;
import com.sporekart.ai.assistant.infrastructure.kafka.AssistantKafkaEventPublisher;
import com.sporekart.ai.assistant.infrastructure.monitoring.AssistantMonitoringService;
import com.sporekart.ai.assistant.infrastructure.persistence.AssistantExecutionEntity;
import com.sporekart.ai.assistant.infrastructure.persistence.AssistantExecutionRepository;
import com.sporekart.ai.assistant.infrastructure.persistence.AssistantMetricsRepository;
import com.sporekart.ai.assistant.infrastructure.persistence.AssistantSessionEntity;
import com.sporekart.ai.assistant.infrastructure.persistence.AssistantSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class AssistantOrchestratorImpl implements AssistantOrchestrator {

    private static final Logger log = LoggerFactory.getLogger(AssistantOrchestratorImpl.class);

    private static final List<String> DEFAULT_INTENTS = List.of(
            "product_search", "customer_support", "order_status", "inventory_check",
            "pricing_info", "training_request", "grower_advisory", "marketplace_listing",
            "erp_sync", "analytics_report", "account_admin", "notification_pref"
    );

    private final com.sporekart.ai.assistant.api.IntentResolver intentResolver;
    private final com.sporekart.ai.assistant.api.TaskPlanner taskPlanner;
    private final com.sporekart.ai.assistant.api.CopilotOrchestrator copilotOrchestrator;
    private final AssistantSessionRepository sessionRepository;
    private final AssistantExecutionRepository executionRepository;
    private final AssistantMetricsRepository metricsRepository;
    private final AssistantKafkaEventPublisher eventPublisher;
    private final AssistantMonitoringService monitoringService;

    public AssistantOrchestratorImpl(
            com.sporekart.ai.assistant.api.IntentResolver intentResolver,
            com.sporekart.ai.assistant.api.TaskPlanner taskPlanner,
            com.sporekart.ai.assistant.api.CopilotOrchestrator copilotOrchestrator,
            AssistantSessionRepository sessionRepository,
            AssistantExecutionRepository executionRepository,
            AssistantMetricsRepository metricsRepository,
            AssistantKafkaEventPublisher eventPublisher,
            AssistantMonitoringService monitoringService) {
        this.intentResolver = intentResolver;
        this.taskPlanner = taskPlanner;
        this.copilotOrchestrator = copilotOrchestrator;
        this.sessionRepository = sessionRepository;
        this.executionRepository = executionRepository;
        this.metricsRepository = metricsRepository;
        this.eventPublisher = eventPublisher;
        this.monitoringService = monitoringService;
    }

    @Override
    public AssistantResponse chat(UUID sessionId, String message) {
        long startTime = System.currentTimeMillis();

        try {
            log.info("Processing chat for session {}: '{}'", sessionId, message);

            eventPublisher.publishAssistantInvoked(sessionId, UUID.randomUUID());

            var sessionEntity = findOrCreateSession(sessionId);
            var session = new com.sporekart.ai.assistant.domain.AssistantSession(
                    sessionEntity.getId(), sessionEntity.getAssistantId(),
                    sessionEntity.getUserId(), sessionEntity.getConversationId(),
                    Map.of(), com.sporekart.ai.assistant.domain.AssistantStatus.ACTIVE,
                    sessionEntity.getCreatedAt(), sessionEntity.getUpdatedAt(),
                    sessionEntity.getExpiresAt());

            IntentResult intentResult = intentResolver.resolveIntent(message, DEFAULT_INTENTS);
            AssistantIntent intent = intentResolver.classifyIntent(sessionId, message);

            eventPublisher.publishIntentResolved(sessionId, intentResult.intent(), intentResult.confidence());

            TaskPlan taskPlan = taskPlanner.planTasks(intent, message);

            eventPublisher.publishTaskPlanned(sessionId, taskPlan.tasks().size());

            AssistantResponse copilotResponse = copilotOrchestrator.handleRequest(session, message, intent);

            List<com.sporekart.ai.assistant.domain.AssistantTask> executedTasks = taskPlanner.executeTaskPlan(taskPlan);

            Map<String, Object> context = new HashMap<>(copilotResponse.context());
            context.put("intentConfidence", intentResult.confidence());
            context.put("tasksPlanned", taskPlan.tasks().size());
            context.put("tasksExecuted", executedTasks.size());

            AssistantResponse response = new AssistantResponse(
                    sessionId,
                    copilotResponse.message(),
                    intentResult.intent(),
                    intentResult.confidence(),
                    executedTasks,
                    context,
                    copilotResponse.requiresFollowUp(),
                    OffsetDateTime.now()
            );

            long latency = System.currentTimeMillis() - startTime;
            eventPublisher.publishResponseGenerated(sessionId);
            monitoringService.recordResponseLatency(latency);
            monitoringService.recordAssistantUsage(session.assistantId());

            log.info("Chat processed for session {} with intent '{}' ({}ms)", sessionId, intentResult.intent(), latency);
            return response;

        } catch (Exception e) {
            long latency = System.currentTimeMillis() - startTime;
            log.error("Error processing chat for session {}: {}", sessionId, e.getMessage(), e);

            monitoringService.recordResponseLatency(latency);

            return new AssistantResponse(
                    sessionId,
                    "I encountered an error processing your request. Please try again.",
                    "error",
                    0.0,
                    Collections.emptyList(),
                    Map.of("error", e.getMessage()),
                    false,
                    OffsetDateTime.now()
            );
        }
    }

    @Override
    public IntentResult resolveIntent(String message) {
        return intentResolver.resolveIntent(message, DEFAULT_INTENTS);
    }

    @Override
    public TaskPlan createTaskPlan(AssistantIntent intent) {
        return taskPlanner.planTasks(intent, intent.userInput());
    }

    @Override
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRequests", 0);
        stats.put("activeSessions", 0);
        stats.put("avgLatencyMs", 0.0);
        stats.put("intentAccuracy", 0.0);
        stats.put("status", "operational");
        return stats;
    }

    @Override
    public List<AssistantExecution> getExecutionHistory(UUID sessionId) {
        return executionRepository.findBySessionIdAndIsDeletedFalse(sessionId).stream()
                .map(this::toExecution)
                .toList();
    }

    private AssistantSessionEntity findOrCreateSession(UUID sessionId) {
        return sessionRepository.findById(sessionId)
                .orElseGet(() -> {
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
                    return sessionRepository.save(entity);
                });
    }

    private AssistantExecution toExecution(AssistantExecutionEntity entity) {
        return new AssistantExecution(
                entity.getId(),
                entity.getAssistantId(),
                entity.getSessionId(),
                entity.getTaskId(),
                entity.getCopilotType() != null ? com.sporekart.ai.assistant.domain.CopilotType.valueOf(entity.getCopilotType()) : null,
                entity.getAction(),
                Map.of(),
                Map.of(),
                Boolean.TRUE.equals(entity.getSuccess()),
                entity.getLatencyMs(),
                entity.getErrorMessage(),
                entity.getCreatedAt(),
                entity.getCompletedAt()
        );
    }
}
