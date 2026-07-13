package com.sporekart.ai.assistant.infrastructure.persistence;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class AssistantRepositoryTest {

    @Autowired
    private AssistantRepository assistantRepository;

    @Autowired
    private AssistantSessionRepository sessionRepository;

    @Autowired
    private AssistantExecutionRepository executionRepository;

    @Autowired
    private AssistantFeedbackRepository feedbackRepository;

    @Autowired
    private AssistantIntentRepository intentRepository;

    @Autowired
    private AssistantProfileRepository profileRepository;

    @Autowired
    private AssistantMetricsRepository metricsRepository;

    @Autowired
    private AssistantTaskRepository taskRepository;

    @Test
    void shouldSaveAndFindAssistantById() {
        var entity = new AssistantEntity();
        entity.setName("Test Assistant");
        entity.setType("SUPPORT_COPILOT");
        entity.setStatus("ACTIVE");
        entity.setIsActive(true);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        entity.setIsDeleted(false);
        var saved = assistantRepository.save(entity);

        var found = assistantRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("Test Assistant", found.get().getName());
        assertFalse(found.get().getIsDeleted());
    }

    @Test
    void shouldFindByStatusAndIsDeletedFalse() {
        var entity = new AssistantEntity();
        entity.setName("Active Assistant");
        entity.setStatus("ACTIVE");
        entity.setIsActive(true);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        entity.setIsDeleted(false);
        assistantRepository.save(entity);

        var results = assistantRepository.findByStatusAndIsDeletedFalse("ACTIVE");

        assertFalse(results.isEmpty());
        assertEquals("ACTIVE", results.get(0).getStatus());
    }

    @Test
    void shouldFindByCreatedAtBetweenAndIsDeletedFalse() {
        var entity = new AssistantEntity();
        entity.setName("Dated Assistant");
        entity.setStatus("ACTIVE");
        entity.setIsActive(true);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        entity.setIsDeleted(false);
        assistantRepository.save(entity);

        var start = OffsetDateTime.now().minusHours(1);
        var end = OffsetDateTime.now().plusHours(1);
        var results = assistantRepository.findByCreatedAtBetweenAndIsDeletedFalse(start, end);

        assertFalse(results.isEmpty());
    }

    @Test
    void shouldSaveAndFindSessionByAssistantId() {
        var session = new AssistantSessionEntity();
        session.setAssistantId(UUID.randomUUID());
        session.setUserId(UUID.randomUUID());
        session.setStatus("ACTIVE");
        session.setCreatedAt(OffsetDateTime.now());
        session.setUpdatedAt(OffsetDateTime.now());
        session.setExpiresAt(OffsetDateTime.now().plusHours(24));
        session.setIsDeleted(false);
        sessionRepository.save(session);

        var results = sessionRepository.findByAssistantIdAndIsDeletedFalse(session.getAssistantId());

        assertFalse(results.isEmpty());
        assertEquals(session.getAssistantId(), results.get(0).getAssistantId());
    }

    @Test
    void shouldFindSessionByUserIdAndIsDeletedFalse() {
        var userId = UUID.randomUUID();
        var session = new AssistantSessionEntity();
        session.setAssistantId(UUID.randomUUID());
        session.setUserId(userId);
        session.setStatus("ACTIVE");
        session.setCreatedAt(OffsetDateTime.now());
        session.setUpdatedAt(OffsetDateTime.now());
        session.setExpiresAt(OffsetDateTime.now().plusHours(24));
        session.setIsDeleted(false);
        sessionRepository.save(session);

        var results = sessionRepository.findByUserIdAndIsDeletedFalse(userId);

        assertFalse(results.isEmpty());
        assertEquals(userId, results.get(0).getUserId());
    }

    @Test
    void shouldRespectSoftDeleteOnAssistant() {
        var entity = new AssistantEntity();
        entity.setName("Deleted Assistant");
        entity.setStatus("ACTIVE");
        entity.setIsActive(false);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        entity.setIsDeleted(true);
        var saved = assistantRepository.save(entity);

        var results = assistantRepository.findByStatusAndIsDeletedFalse("ACTIVE");
        var foundDeleted = results.stream().anyMatch(a -> a.getId().equals(saved.getId()));

        assertFalse(foundDeleted);
    }

    @Test
    void shouldSaveAndFindExecutionBySessionId() {
        var sessionId = UUID.randomUUID();
        var execution = new AssistantExecutionEntity();
        execution.setAssistantId(UUID.randomUUID());
        execution.setSessionId(sessionId);
        execution.setCopilotType("PRODUCT");
        execution.setAction("search");
        execution.setSuccess(true);
        execution.setLatencyMs(100L);
        execution.setCreatedAt(OffsetDateTime.now());
        execution.setCompletedAt(OffsetDateTime.now());
        execution.setIsDeleted(false);
        executionRepository.save(execution);

        var results = executionRepository.findBySessionIdAndIsDeletedFalse(sessionId);

        assertFalse(results.isEmpty());
        assertEquals(sessionId, results.get(0).getSessionId());
    }

    @Test
    void shouldSaveAndFindExecutionByAssistantId() {
        var assistantId = UUID.randomUUID();
        var execution = new AssistantExecutionEntity();
        execution.setAssistantId(assistantId);
        execution.setSessionId(UUID.randomUUID());
        execution.setCopilotType("SUPPORT");
        execution.setAction("help");
        execution.setSuccess(true);
        execution.setLatencyMs(50L);
        execution.setCreatedAt(OffsetDateTime.now());
        execution.setCompletedAt(OffsetDateTime.now());
        execution.setIsDeleted(false);
        executionRepository.save(execution);

        var results = executionRepository.findByAssistantIdAndIsDeletedFalse(assistantId);

        assertFalse(results.isEmpty());
        assertEquals(assistantId, results.get(0).getAssistantId());
    }

    @Test
    void shouldSaveAndFindFeedbackBySessionId() {
        var sessionId = UUID.randomUUID();
        var feedback = new AssistantFeedbackEntity();
        feedback.setSessionId(sessionId);
        feedback.setUserId(UUID.randomUUID());
        feedback.setRating(5);
        feedback.setHelpful(true);
        feedback.setCreatedAt(OffsetDateTime.now());
        feedback.setIsDeleted(false);
        feedbackRepository.save(feedback);

        var results = feedbackRepository.findBySessionIdAndIsDeletedFalse(sessionId);

        assertFalse(results.isEmpty());
        assertEquals(5, results.get(0).getRating().intValue());
    }

    @Test
    void shouldSaveAndFindFeedbackByUserId() {
        var userId = UUID.randomUUID();
        var feedback = new AssistantFeedbackEntity();
        feedback.setSessionId(UUID.randomUUID());
        feedback.setUserId(userId);
        feedback.setRating(4);
        feedback.setHelpful(true);
        feedback.setCreatedAt(OffsetDateTime.now());
        feedback.setIsDeleted(false);
        feedbackRepository.save(feedback);

        var results = feedbackRepository.findByUserIdAndIsDeletedFalse(userId);

        assertFalse(results.isEmpty());
        assertEquals(userId, results.get(0).getUserId());
    }

    @Test
    void shouldSaveAndFindIntentBySessionId() {
        var sessionId = UUID.randomUUID();
        var intent = new AssistantIntentEntity();
        intent.setSessionId(sessionId);
        intent.setUserInput("search product");
        intent.setResolvedIntent("product_search");
        intent.setConfidence(0.95);
        intent.setStatus("RESOLVED");
        intent.setPriority("HIGH");
        intent.setCreatedAt(OffsetDateTime.now());
        intent.setResolvedAt(OffsetDateTime.now());
        intent.setIsDeleted(false);
        intentRepository.save(intent);

        var results = intentRepository.findBySessionIdAndIsDeletedFalse(sessionId);

        assertFalse(results.isEmpty());
        assertEquals("product_search", results.get(0).getResolvedIntent());
    }

    @Test
    void shouldSaveAndFindProfileByAssistantId() {
        var assistantId = UUID.randomUUID();
        var profile = new AssistantProfileEntity();
        profile.setAssistantId(assistantId);
        profile.setDisplayName("Support Bot");
        profile.setIsActive(true);
        profile.setCreatedAt(OffsetDateTime.now());
        profile.setUpdatedAt(OffsetDateTime.now());
        profile.setIsDeleted(false);
        profileRepository.save(profile);

        var results = profileRepository.findByAssistantIdAndIsDeletedFalse(assistantId);

        assertFalse(results.isEmpty());
        assertEquals("Support Bot", results.get(0).getDisplayName());
    }

    @Test
    void shouldSaveAndFindMetricsByAssistantId() {
        var assistantId = UUID.randomUUID();
        var metrics = new AssistantMetricsEntity();
        metrics.setAssistantId(assistantId);
        metrics.setTotalRequests(100L);
        metrics.setSuccessfulRequests(95L);
        metrics.setAvgLatencyMs(150.0);
        metrics.setRecordedAt(OffsetDateTime.now());
        metrics.setIsDeleted(false);
        metricsRepository.save(metrics);

        var results = metricsRepository.findByAssistantIdAndIsDeletedFalse(assistantId);

        assertFalse(results.isEmpty());
        assertEquals(100L, results.get(0).getTotalRequests().longValue());
    }

    @Test
    void shouldSaveAndFindTaskBySessionId() {
        var sessionId = UUID.randomUUID();
        var task = new AssistantTaskEntity();
        task.setSessionId(sessionId);
        task.setName("QueryProducts");
        task.setStatus("PLANNING");
        task.setPriority(1);
        task.setTimeoutMs(5000L);
        task.setCreatedAt(OffsetDateTime.now());
        task.setIsDeleted(false);
        taskRepository.save(task);

        var results = taskRepository.findBySessionIdAndIsDeletedFalse(sessionId);

        assertFalse(results.isEmpty());
        assertEquals("QueryProducts", results.get(0).getName());
    }

    @Test
    void shouldFindTaskByStatusAndIsDeletedFalse() {
        var task = new AssistantTaskEntity();
        task.setSessionId(UUID.randomUUID());
        task.setName("TestTask");
        task.setStatus("COMPLETED");
        task.setPriority(1);
        task.setTimeoutMs(3000L);
        task.setCreatedAt(OffsetDateTime.now());
        task.setIsDeleted(false);
        taskRepository.save(task);

        var results = taskRepository.findByStatusAndIsDeletedFalse("COMPLETED");

        assertFalse(results.isEmpty());
        assertEquals("COMPLETED", results.get(0).getStatus());
    }
}
