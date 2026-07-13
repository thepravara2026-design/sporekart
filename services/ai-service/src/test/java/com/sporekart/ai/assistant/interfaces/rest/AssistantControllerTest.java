package com.sporekart.ai.assistant.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sporekart.ai.assistant.api.AssistantOrchestrator;
import com.sporekart.ai.assistant.api.AssistantService;
import com.sporekart.ai.assistant.application.AssistantException;
import com.sporekart.ai.assistant.config.AssistantConfig;
import com.sporekart.ai.assistant.domain.*;
import com.sporekart.ai.assistant.infrastructure.kafka.AssistantKafkaEventPublisher;
import com.sporekart.ai.assistant.infrastructure.monitoring.AssistantMonitoringService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AssistantControllerTest {

    @Mock private AssistantOrchestrator orchestrator;
    @Mock private AssistantService assistantService;
    @Mock private AssistantConfig assistantConfig;
    @Mock private AssistantMonitoringService monitoringService;
    @Mock private AssistantKafkaEventPublisher kafkaPublisher;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @ControllerAdvice
    static class TestExceptionHandler {
        @ExceptionHandler(AssistantException.class)
        @ResponseBody
        public ResponseEntity<String> handleAssistantException(AssistantException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

        @ExceptionHandler(IllegalArgumentException.class)
        @ResponseBody
        public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @BeforeEach
    void setUp() {
        var controller = new AssistantController(orchestrator, assistantService,
                assistantConfig, monitoringService, kafkaPublisher);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new TestExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void shouldChat() throws Exception {
        var sessionId = UUID.randomUUID();
        var response = new com.sporekart.ai.assistant.api.AssistantResponse(
                sessionId, "Product copilot processed", "product_search",
                0.95, List.of(), Map.of("copilot", "PRODUCT"), false, OffsetDateTime.now());
        when(orchestrator.chat(sessionId, "search product")).thenReturn(response);

        var request = Map.of("message", "search product", "sessionId", sessionId.toString());
        mockMvc.perform(post("/api/v1/assistants/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.intent").value("product_search"));
    }

    @Test
    void shouldReturn400WhenChattingWithBlankMessage() throws Exception {
        var request = Map.of("message", "", "sessionId", UUID.randomUUID().toString());
        mockMvc.perform(post("/api/v1/assistants/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldResolveIntent() throws Exception {
        var result = new IntentResult("product_search", 0.95, IntentPriority.HIGH,
                Map.of("matched_keywords", List.of("search")), List.of("search"), false);
        when(orchestrator.resolveIntent("search product")).thenReturn(result);

        var request = Map.of("message", "search product");
        mockMvc.perform(post("/api/v1/assistants/intent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.intent").value("product_search"))
                .andExpect(jsonPath("$.confidence").value(0.95));
    }

    @Test
    void shouldReturn400WhenResolvingIntentWithBlankMessage() throws Exception {
        var request = Map.of("message", "");
        mockMvc.perform(post("/api/v1/assistants/intent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldCreateTask() throws Exception {
        var taskId = UUID.randomUUID();
        var intentId = UUID.randomUUID();
        var sessionId = UUID.randomUUID();
        var task = new AssistantTask(taskId, sessionId, intentId,
                "QueryProducts", "Search", TaskStatus.PLANNING, 1,
                Map.of("key", "value"), null, List.of(), 0, 3, 5000,
                null, OffsetDateTime.now(), null, null);
        var taskPlan = new TaskPlan(UUID.randomUUID(), List.of(task), Map.of(), 5000, false);
        var intent = new AssistantIntent(intentId, sessionId, "desc",
                "QueryProducts", 0.0, IntentStatus.PENDING, IntentPriority.MEDIUM,
                Map.of("key", "value"), List.of(), null, OffsetDateTime.now(), null);
        when(orchestrator.createTaskPlan(any())).thenReturn(taskPlan);

        var request = Map.of(
                "sessionId", sessionId.toString(),
                "intentId", intentId.toString(),
                "name", "QueryProducts",
                "description", "Search product catalog",
                "input", Map.of("key", "value"));
        mockMvc.perform(post("/api/v1/assistants/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("QueryProducts"))
                .andExpect(jsonPath("$.status").value("PLANNING"));
    }

    @Test
    void shouldListAssistants() throws Exception {
        var assistant = new Assistant(UUID.randomUUID(), "Support Bot", "Support assistant",
                AssistantType.SUPPORT_COPILOT, AssistantStatus.ACTIVE, Map.of(),
                true, UUID.randomUUID(), OffsetDateTime.now(), OffsetDateTime.now());
        when(assistantService.findAll()).thenReturn(List.of(assistant));

        mockMvc.perform(get("/api/v1/assistants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1))
                .andExpect(jsonPath("$.assistants[0].name").value("Support Bot"));
    }

    @Test
    void shouldReturnEmptyListWhenNoAssistants() throws Exception {
        when(assistantService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/assistants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(0));
    }

    @Test
    void shouldGetAssistantById() throws Exception {
        var id = UUID.randomUUID();
        var assistant = new Assistant(id, "Support Bot", "Support",
                AssistantType.SUPPORT_COPILOT, AssistantStatus.ACTIVE, Map.of(),
                true, UUID.randomUUID(), OffsetDateTime.now(), OffsetDateTime.now());
        when(assistantService.findById(id)).thenReturn(Optional.of(assistant));

        mockMvc.perform(get("/api/v1/assistants/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Support Bot"));
    }

    @Test
    void shouldReturn404WhenAssistantNotFound() throws Exception {
        var id = UUID.randomUUID();
        when(assistantService.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/assistants/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetHistory() throws Exception {
        var userId = UUID.randomUUID();
        var session = new AssistantSession(UUID.randomUUID(), UUID.randomUUID(), userId,
                UUID.randomUUID(), Map.of(), AssistantStatus.ACTIVE,
                OffsetDateTime.now(), OffsetDateTime.now(), OffsetDateTime.now().plusHours(1));
        when(assistantService.getSessionHistory(userId)).thenReturn(List.of(session));

        mockMvc.perform(get("/api/v1/assistants/history")
                        .param("userId", userId.toString())
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1));
    }

    @Test
    void shouldGetStatistics() throws Exception {
        var assistantId = UUID.randomUUID();
        var metrics = new AssistantMetrics(UUID.randomUUID(), assistantId, 100L, 95L, 5L,
                150.0, 0.95, 50L, 45L, 5L, OffsetDateTime.now());
        when(assistantService.getStatistics(assistantId)).thenReturn(metrics);

        mockMvc.perform(get("/api/v1/assistants/statistics")
                        .param("assistantId", assistantId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalRequests").value(100));
    }

    @Test
    void shouldSubmitFeedback() throws Exception {
        var feedbackId = UUID.randomUUID();
        var feedback = new AssistantFeedback(feedbackId, UUID.randomUUID(), UUID.randomUUID(),
                5, "Great!", "general", Map.of(), true, OffsetDateTime.now());
        when(assistantService.recordFeedback(any())).thenReturn(feedback);

        var request = Map.of(
                "sessionId", UUID.randomUUID().toString(),
                "userId", UUID.randomUUID().toString(),
                "rating", 5,
                "comment", "Great!",
                "helpful", true);
        mockMvc.perform(post("/api/v1/assistants/feedback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rating").value(5));
    }

    @Test
    void shouldReturn400WhenFeedbackRatingOutOfRange() throws Exception {
        var request = Map.of(
                "sessionId", UUID.randomUUID().toString(),
                "userId", UUID.randomUUID().toString(),
                "rating", 0,
                "helpful", true);
        mockMvc.perform(post("/api/v1/assistants/feedback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldTriggerWorkflow() throws Exception {
        var sessionId = UUID.randomUUID();
        var request = Map.of(
                "intent", "order_processing",
                "sessionId", sessionId.toString());

        mockMvc.perform(post("/api/v1/assistants/workflow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted());
    }

    @Test
    void shouldReturn400WhenWorkflowIntentBlank() throws Exception {
        var request = Map.of("intent", "");
        mockMvc.perform(post("/api/v1/assistants/workflow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldHandleServiceException() throws Exception {
        var sessionId = UUID.randomUUID();
        when(orchestrator.chat(sessionId, "search"))
                .thenThrow(new AssistantException("Service error"));

        var request = Map.of("message", "search", "sessionId", sessionId.toString());
        mockMvc.perform(post("/api/v1/assistants/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
}
