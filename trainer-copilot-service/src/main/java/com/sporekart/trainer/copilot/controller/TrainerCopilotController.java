package com.sporekart.trainer.copilot.controller;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.sporekart.trainer.copilot.domain.PracticalGuide;
import com.sporekart.trainer.copilot.dto.AnalyticsSummaryResponse;
import com.sporekart.trainer.copilot.dto.AssessmentRequest;
import com.sporekart.trainer.copilot.dto.AssessmentResponse;
import com.sporekart.trainer.copilot.dto.BatchAnalyticsResponse;
import com.sporekart.trainer.copilot.dto.BatchListResponse;
import com.sporekart.trainer.copilot.dto.CertificationRequest;
import com.sporekart.trainer.copilot.dto.CertificationResponse;
import com.sporekart.trainer.copilot.dto.ChatRequest;
import com.sporekart.trainer.copilot.dto.ChatResponse;
import com.sporekart.trainer.copilot.dto.LessonRequest;
import com.sporekart.trainer.copilot.dto.LessonResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/copilot/trainer")
@Tag(name = "Trainer Copilot API", description = "AI-powered trainer copilot endpoints")
public class TrainerCopilotController {

    private static final Logger log = LoggerFactory.getLogger(TrainerCopilotController.class);

    private final TrainerCopilotOrchestrator orchestrator;

    public TrainerCopilotController(TrainerCopilotOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    @PostMapping("/chat")
    @Operation(summary = "Chat with the trainer copilot")
    public ResponseEntity<ChatResponse> chat(@Valid @RequestBody ChatRequest request) {
        log.info("Chat request: sessionId={}, messageLength={}", request.sessionId(),
                request.message() != null ? request.message().length() : 0);
        var response = orchestrator.chat(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "SSE streaming chat with the trainer copilot")
    public SseEmitter stream(@Valid @RequestBody ChatRequest request) {
        log.info("Stream request: sessionId={}, messageLength={}", request.sessionId(),
                request.message() != null ? request.message().length() : 0);
        SseEmitter emitter = new SseEmitter();
        orchestrator.streamChat(request, emitter);
        return emitter;
    }

    @GetMapping("/batches")
    @Operation(summary = "List all training batches")
    public ResponseEntity<BatchListResponse> listBatches() {
        log.info("List batches requested");
        var response = orchestrator.listBatches();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/batches/{batchId}")
    @Operation(summary = "Get batch analytics details")
    public ResponseEntity<BatchAnalyticsResponse> getBatchAnalytics(@PathVariable String batchId) {
        log.info("Batch analytics requested: batchId={}", batchId);
        var response = orchestrator.getBatchAnalytics(batchId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/lesson")
    @Operation(summary = "Generate a training lesson")
    public ResponseEntity<LessonResponse> generateLesson(@Valid @RequestBody LessonRequest request) {
        log.info("Lesson generation requested: topic={}, type={}", request.topic(), request.lessonType());
        var response = orchestrator.generateLesson(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/assessment")
    @Operation(summary = "Generate an assessment")
    public ResponseEntity<AssessmentResponse> generateAssessment(@Valid @RequestBody AssessmentRequest request) {
        log.info("Assessment generation requested: title={}, type={}", request.title(), request.type());
        var response = orchestrator.generateAssessment(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/analytics")
    @Operation(summary = "Get training analytics summary")
    public ResponseEntity<AnalyticsSummaryResponse> getAnalyticsSummary() {
        log.info("Analytics summary requested");
        var response = orchestrator.getAnalyticsSummary();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/certification")
    @Operation(summary = "Evaluate certification eligibility")
    public ResponseEntity<CertificationResponse> evaluateCertification(@Valid @RequestBody CertificationRequest request) {
        log.info("Certification evaluation requested: batchId={}, studentId={}", request.batchId(), request.studentId());
        var response = orchestrator.evaluateCertification(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cultivation/{category}")
    @Operation(summary = "Get practical cultivation guide by category")
    public ResponseEntity<PracticalGuide> getCultivationGuide(@PathVariable String category) {
        log.info("Cultivation guide requested: category={}", category);
        var response = orchestrator.getCultivationGuide(category);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/knowledge")
    @Operation(summary = "Search the training knowledge base")
    public ResponseEntity<List<Map<String, Object>>> searchKnowledge(@RequestParam String query) {
        log.info("Knowledge search requested: query={}", query);
        var response = orchestrator.searchKnowledge(query);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    @Operation(summary = "Get conversation history")
    public ResponseEntity<List<Map<String, Object>>> getConversationHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Conversation history requested: page={}, size={}", page, size);
        var response = orchestrator.getConversationHistory(page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    @Operation(summary = "Health check for trainer copilot service")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "trainer-copilot-service",
                "version", "1.0.0",
                "timestamp", OffsetDateTime.now().toString()
        ));
    }
}
