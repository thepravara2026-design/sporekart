package com.sporekart.ai.assistant.interfaces.rest.dto;

import com.sporekart.ai.assistant.domain.*;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DtoTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void shouldRejectBlankChatRequestMessage() {
        var request = new ChatRequest("", UUID.randomUUID());
        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldAcceptValidChatRequest() {
        var request = new ChatRequest("search product", UUID.randomUUID());
        var violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldRejectBlankIntentRequestMessage() {
        var request = new IntentRequest("");
        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldAcceptValidIntentRequest() {
        var request = new IntentRequest("search product");
        var violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldRejectFeedbackRequestWithRatingBelowMin() {
        var request = new FeedbackRequest(UUID.randomUUID(), UUID.randomUUID(), 0, "bad", "general", false);
        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldRejectFeedbackRequestWithRatingAboveMax() {
        var request = new FeedbackRequest(UUID.randomUUID(), UUID.randomUUID(), 6, "great", "general", true);
        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldAcceptValidFeedbackRequest() {
        var request = new FeedbackRequest(UUID.randomUUID(), UUID.randomUUID(), 4, "good", "general", true);
        var violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldRejectBlankWorkflowRequestIntent() {
        var request = new WorkflowRequest("", UUID.randomUUID(), Map.of());
        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldAcceptValidWorkflowRequest() {
        var request = new WorkflowRequest("order_processing", UUID.randomUUID(), Map.of("key", "value"));
        var violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldMapChatResponseFromAssistantResponse() {
        var apiResponse = new com.sporekart.ai.assistant.api.AssistantResponse(
                UUID.randomUUID(), "processed", "product_search",
                0.95, List.of(), Map.of("copilot", "PRODUCT"), false, OffsetDateTime.now());

        var chatResponse = ChatResponse.from(apiResponse);

        assertEquals(apiResponse.sessionId(), chatResponse.sessionId());
        assertEquals(apiResponse.message(), chatResponse.message());
        assertEquals(apiResponse.intent(), chatResponse.intent());
        assertEquals(apiResponse.confidence(), chatResponse.confidence());
        assertEquals(apiResponse.tasks(), chatResponse.tasks());
        assertEquals(apiResponse.context(), chatResponse.context());
        assertEquals(apiResponse.requiresFollowUp(), chatResponse.requiresFollowUp());
    }

    @Test
    void shouldMapIntentResponseFromIntentResult() {
        var result = new IntentResult("product_search", 0.95, IntentPriority.HIGH,
                Map.of("key", "value"), List.of("search"), false);

        var intentResponse = IntentResponse.from(result);

        assertEquals(result.intent(), intentResponse.intent());
        assertEquals(result.confidence(), intentResponse.confidence());
        assertEquals(result.priority().name(), intentResponse.priority());
        assertEquals(result.entities(), intentResponse.entities());
        assertEquals(result.metadata(), intentResponse.metadata());
        assertEquals(result.isFallback(), intentResponse.isFallback());
    }

    @Test
    void shouldMapTaskResponseFromAssistantTask() {
        var task = new AssistantTask(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                "QueryProducts", "Search", TaskStatus.PLANNING, 1,
                Map.of("key", "value"), null, List.of(), 0, 3, 5000,
                null, OffsetDateTime.now(), null, null);

        var taskResponse = TaskResponse.from(task);

        assertEquals(task.id(), taskResponse.id());
        assertEquals(task.name(), taskResponse.name());
        assertEquals(task.status().name(), taskResponse.status());
        assertEquals(task.input(), taskResponse.input());
        assertEquals(task.createdAt(), taskResponse.createdAt());
    }

    @Test
    void shouldMapFeedbackResponseFromAssistantFeedback() {
        var feedback = new AssistantFeedback(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                5, "Great!", "general", Map.of(), true, OffsetDateTime.now());

        var feedbackResponse = FeedbackResponse.from(feedback);

        assertEquals(feedback.id(), feedbackResponse.id());
        assertEquals(feedback.rating(), feedbackResponse.rating());
        assertEquals("Feedback recorded successfully", feedbackResponse.message());
    }

    @Test
    void shouldMapAssistantResponseFromAssistantDomain() {
        var assistant = new Assistant(UUID.randomUUID(), "Support Bot", "Support assistant",
                AssistantType.SUPPORT_COPILOT, AssistantStatus.ACTIVE, Map.of(),
                true, UUID.randomUUID(), OffsetDateTime.now(), OffsetDateTime.now());

        var response = AssistantResponse.from(assistant);

        assertEquals(assistant.id(), response.id());
        assertEquals(assistant.name(), response.name());
        assertEquals(assistant.type().name(), response.type());
        assertEquals(assistant.status().name(), response.status());
        assertEquals(assistant.isActive(), response.active());
    }

    @Test
    void shouldMapStatisticsResponseFromAssistantMetrics() {
        var metrics = new AssistantMetrics(UUID.randomUUID(), UUID.randomUUID(), 100L, 95L, 5L,
                150.0, 0.95, 50L, 45L, 5L, OffsetDateTime.now());

        var statsResponse = StatisticsResponse.from(metrics);

        assertEquals(metrics.totalRequests(), statsResponse.totalRequests());
        assertEquals(metrics.successfulRequests(), statsResponse.successfulRequests());
        assertEquals(metrics.avgLatencyMs(), statsResponse.avgLatencyMs());
        assertEquals(metrics.intentAccuracy(), statsResponse.intentAccuracy());
        assertEquals(metrics.tasksCompleted(), statsResponse.tasksCompleted());
        assertEquals(metrics.recordedAt(), statsResponse.recordedAt());
    }
}
