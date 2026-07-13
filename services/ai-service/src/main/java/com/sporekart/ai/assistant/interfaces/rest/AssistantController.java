package com.sporekart.ai.assistant.interfaces.rest;

import com.sporekart.ai.assistant.api.AssistantOrchestrator;
import com.sporekart.ai.assistant.api.AssistantService;
import com.sporekart.ai.assistant.application.AssistantException;
import com.sporekart.ai.assistant.config.AssistantConfig;
import com.sporekart.ai.assistant.domain.AssistantFeedback;
import com.sporekart.ai.assistant.domain.AssistantIntent;
import com.sporekart.ai.assistant.domain.IntentPriority;
import com.sporekart.ai.assistant.domain.IntentStatus;
import com.sporekart.ai.assistant.infrastructure.kafka.AssistantKafkaEventPublisher;
import com.sporekart.ai.assistant.infrastructure.monitoring.AssistantMonitoringService;
import com.sporekart.ai.assistant.interfaces.rest.dto.AssistantListResponse;
import com.sporekart.ai.assistant.interfaces.rest.dto.AssistantResponse;
import com.sporekart.ai.assistant.interfaces.rest.dto.ChatRequest;
import com.sporekart.ai.assistant.interfaces.rest.dto.ChatResponse;
import com.sporekart.ai.assistant.interfaces.rest.dto.FeedbackRequest;
import com.sporekart.ai.assistant.interfaces.rest.dto.FeedbackResponse;
import com.sporekart.ai.assistant.interfaces.rest.dto.HistoryResponse;
import com.sporekart.ai.assistant.interfaces.rest.dto.IntentRequest;
import com.sporekart.ai.assistant.interfaces.rest.dto.IntentResponse;
import com.sporekart.ai.assistant.interfaces.rest.dto.StatisticsResponse;
import com.sporekart.ai.assistant.interfaces.rest.dto.TaskRequest;
import com.sporekart.ai.assistant.interfaces.rest.dto.TaskResponse;
import com.sporekart.ai.assistant.interfaces.rest.dto.WorkflowRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/assistants")
@Tag(name = "Assistant API", description = "AI assistant orchestration endpoints")
public class AssistantController {

    private static final Logger log = LoggerFactory.getLogger(AssistantController.class);

    private final AssistantOrchestrator orchestrator;
    private final AssistantService assistantService;
    private final AssistantConfig assistantConfig;
    private final AssistantMonitoringService monitoringService;
    private final AssistantKafkaEventPublisher kafkaPublisher;

    public AssistantController(AssistantOrchestrator orchestrator,
                                AssistantService assistantService,
                                AssistantConfig assistantConfig,
                                AssistantMonitoringService monitoringService,
                                AssistantKafkaEventPublisher kafkaPublisher) {
        this.orchestrator = orchestrator;
        this.assistantService = assistantService;
        this.assistantConfig = assistantConfig;
        this.monitoringService = monitoringService;
        this.kafkaPublisher = kafkaPublisher;
    }

    @PostMapping("/chat")
    @Operation(summary = "Send a chat message to an assistant")
    public ResponseEntity<ChatResponse> chat(@Valid @RequestBody ChatRequest request) {
        var response = orchestrator.chat(request.sessionId(), request.message());
        kafkaPublisher.publishResponseGenerated(request.sessionId());
        log.info("Chat response for session {}: intent={}", request.sessionId(), response.intent());
        return ResponseEntity.ok(ChatResponse.from(response));
    }

    @PostMapping("/intent")
    @Operation(summary = "Resolve intent from a message")
    public ResponseEntity<IntentResponse> resolveIntent(@Valid @RequestBody IntentRequest request) {
        var result = orchestrator.resolveIntent(request.message());
        kafkaPublisher.publishIntentResolved(UUID.randomUUID(), result.intent(), result.confidence());
        log.info("Resolved intent: {} (confidence={})", result.intent(), result.confidence());
        return ResponseEntity.ok(IntentResponse.from(result));
    }

    @PostMapping("/task")
    @Operation(summary = "Create a new assistant task")
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest request) {
        var intent = new AssistantIntent(
                request.intentId() != null ? request.intentId() : UUID.randomUUID(),
                request.sessionId(),
                request.description() != null ? request.description() : "",
                request.name() != null ? request.name() : "",
                0.0,
                IntentStatus.PENDING,
                IntentPriority.MEDIUM,
                request.input() != null ? request.input() : Map.of(),
                List.of(),
                null,
                OffsetDateTime.now(),
                null
        );
        var taskPlan = orchestrator.createTaskPlan(intent);
        var task = taskPlan.tasks().isEmpty() ? null : taskPlan.tasks().getFirst();
        kafkaPublisher.publishTaskStarted(task != null ? task.id() : null, request.name());
        log.info("Created task: {} for session {}", request.name(), request.sessionId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(task != null ? TaskResponse.from(task) : null);
    }

    @GetMapping
    @Operation(summary = "List all available assistants")
    public ResponseEntity<AssistantListResponse> listAssistants() {
        var assistants = assistantService.findAll();
        var responses = assistants.stream()
                .map(AssistantResponse::from)
                .toList();
        log.info("Listed {} assistants", responses.size());
        return ResponseEntity.ok(new AssistantListResponse(responses, responses.size()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an assistant by ID")
    public ResponseEntity<AssistantResponse> getAssistant(@PathVariable UUID id) {
        var assistant = assistantService.findById(id)
                .orElseThrow(() -> new AssistantException("Assistant not found: " + id));
        log.info("Retrieved assistant: {}", id);
        return ResponseEntity.ok(AssistantResponse.from(assistant));
    }

    @GetMapping("/history")
    @Operation(summary = "Get chat history for a user")
    public ResponseEntity<HistoryResponse> getHistory(
            @RequestParam UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        var sessions = assistantService.getSessionHistory(userId);
        var history = sessions.stream()
                .map(s -> new ChatResponse(
                        s.id(), "", "", 0.0, Collections.emptyList(),
                        s.context(), false, s.createdAt()))
                .toList();
        log.info("Retrieved history for user {}: {} entries", userId, history.size());
        return ResponseEntity.ok(new HistoryResponse(history, history.size(), page, size));
    }

    @GetMapping("/statistics")
    @Operation(summary = "Get assistant usage statistics")
    public ResponseEntity<StatisticsResponse> getStatistics(@RequestParam UUID assistantId) {
        var metrics = assistantService.getStatistics(assistantId);
        log.info("Retrieved statistics for assistant {}", assistantId);
        return ResponseEntity.ok(StatisticsResponse.from(metrics));
    }

    @PostMapping("/feedback")
    @Operation(summary = "Submit assistant feedback")
    public ResponseEntity<FeedbackResponse> submitFeedback(@Valid @RequestBody FeedbackRequest request) {
        var feedback = new AssistantFeedback(
                null,
                request.sessionId(),
                request.userId(),
                request.rating(),
                request.comment(),
                request.category(),
                Map.of(),
                request.helpful(),
                OffsetDateTime.now()
        );
        var saved = assistantService.recordFeedback(feedback);
        kafkaPublisher.publishFeedbackReceived(saved.id(), saved.rating());
        monitoringService.recordFeedback(saved.rating());
        log.info("Feedback recorded: {} rating={}", saved.id(), saved.rating());
        return ResponseEntity.status(HttpStatus.CREATED).body(FeedbackResponse.from(saved));
    }

    @PostMapping("/workflow")
    @Operation(summary = "Trigger an assistant workflow")
    public ResponseEntity<Void> triggerWorkflow(@Valid @RequestBody WorkflowRequest request) {
        orchestrator.resolveIntent(request.intent());
        kafkaPublisher.publishWorkflowTriggered(request.sessionId(), request.intent());
        monitoringService.recordWorkflowInvocation(request.intent());
        log.info("Workflow triggered: intent={}, session={}", request.intent(), request.sessionId());
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
