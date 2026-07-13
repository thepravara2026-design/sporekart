package com.sporekart.ai.conversation.interfaces.rest;

import com.sporekart.ai.conversation.api.ContextBuilder;
import com.sporekart.ai.conversation.api.MemoryManager;
import com.sporekart.ai.conversation.api.MessageService;
import com.sporekart.ai.conversation.api.SessionManager;
import com.sporekart.ai.conversation.application.ConversationException;
import com.sporekart.ai.conversation.application.ConversationSecurityService;
import com.sporekart.ai.conversation.domain.MemoryType;
import com.sporekart.ai.conversation.domain.MessageRole;
import com.sporekart.ai.conversation.infrastructure.kafka.ConversationKafkaEventPublisher;
import com.sporekart.ai.conversation.infrastructure.monitoring.ConversationMonitoringService;
import com.sporekart.ai.conversation.interfaces.rest.dto.ConversationMemoryRequest;
import com.sporekart.ai.conversation.interfaces.rest.dto.ConversationMemoryResponse;
import com.sporekart.ai.conversation.interfaces.rest.dto.ConversationMessageRequest;
import com.sporekart.ai.conversation.interfaces.rest.dto.ConversationMessageResponse;
import com.sporekart.ai.conversation.interfaces.rest.dto.ConversationSessionRequest;
import com.sporekart.ai.conversation.interfaces.rest.dto.ConversationSessionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/conversation")
@Tag(name = "Conversation API", description = "Enterprise AI conversation management endpoints")
public class ConversationController {

    private static final Logger log = LoggerFactory.getLogger(ConversationController.class);

    private final SessionManager sessionManager;
    private final MessageService messageService;
    private final MemoryManager memoryManager;
    private final ContextBuilder contextBuilder;
    private final ConversationSecurityService securityService;
    private final ConversationMonitoringService monitoringService;
    private final ConversationKafkaEventPublisher kafkaPublisher;

    public ConversationController(SessionManager sessionManager,
                                  MessageService messageService,
                                  MemoryManager memoryManager,
                                  ContextBuilder contextBuilder,
                                  ConversationSecurityService securityService,
                                  ConversationMonitoringService monitoringService,
                                  ConversationKafkaEventPublisher kafkaPublisher) {
        this.sessionManager = sessionManager;
        this.messageService = messageService;
        this.memoryManager = memoryManager;
        this.contextBuilder = contextBuilder;
        this.securityService = securityService;
        this.monitoringService = monitoringService;
        this.kafkaPublisher = kafkaPublisher;
    }

    @PostMapping("/sessions")
    @Operation(summary = "Create a new conversation session")
    public ResponseEntity<ConversationSessionResponse> createSession(@Valid @RequestBody ConversationSessionRequest request) {
        if (!securityService.checkRateLimit(request.userId())) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
        var session = monitoringService.recordSessionLatency(() ->
                sessionManager.createSession(request.userId(), request.title()));
        monitoringService.recordSessionCreated();
        kafkaPublisher.publishSessionCreated(session.id(), session.userId());
        return ResponseEntity.status(HttpStatus.CREATED).body(ConversationSessionResponse.from(session));
    }

    @GetMapping("/sessions/{sessionId}")
    @Operation(summary = "Get a conversation session by ID")
    public ResponseEntity<ConversationSessionResponse> getSession(@PathVariable UUID sessionId) {
        var session = monitoringService.recordSessionLatency(() ->
                sessionManager.getSession(sessionId)
                        .orElseThrow(() -> new ConversationException("Session not found: " + sessionId)));
        return ResponseEntity.ok(ConversationSessionResponse.from(session));
    }

    @GetMapping("/sessions")
    @Operation(summary = "Get all sessions for a user")
    public ResponseEntity<List<ConversationSessionResponse>> getUserSessions(@RequestParam String userId) {
        var sessions = monitoringService.recordSessionLatency(() ->
                sessionManager.getUserSessions(userId));
        var response = sessions.stream().map(ConversationSessionResponse::from).toList();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/sessions/{sessionId}/suspend")
    @Operation(summary = "Suspend a conversation session")
    public ResponseEntity<ConversationSessionResponse> suspendSession(@PathVariable UUID sessionId) {
        var session = monitoringService.recordSessionLatency(() -> sessionManager.suspendSession(sessionId));
        return ResponseEntity.ok(ConversationSessionResponse.from(session));
    }

    @PutMapping("/sessions/{sessionId}/resume")
    @Operation(summary = "Resume a suspended conversation session")
    public ResponseEntity<ConversationSessionResponse> resumeSession(@PathVariable UUID sessionId) {
        var session = monitoringService.recordSessionLatency(() -> sessionManager.resumeSession(sessionId));
        return ResponseEntity.ok(ConversationSessionResponse.from(session));
    }

    @PutMapping("/sessions/{sessionId}/close")
    @Operation(summary = "Close a conversation session")
    public ResponseEntity<ConversationSessionResponse> closeSession(@PathVariable UUID sessionId) {
        var session = monitoringService.recordSessionLatency(() -> sessionManager.closeSession(sessionId));
        monitoringService.recordSessionClosed();
        kafkaPublisher.publishSessionClosed(session.id(), session.userId());
        return ResponseEntity.ok(ConversationSessionResponse.from(session));
    }

    @DeleteMapping("/sessions/{sessionId}")
    @Operation(summary = "Delete a conversation session")
    public ResponseEntity<Void> deleteSession(@PathVariable UUID sessionId) {
        sessionManager.deleteSession(sessionId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/sessions/{sessionId}/messages")
    @Operation(summary = "Send a message in a conversation session")
    public ResponseEntity<ConversationMessageResponse> sendMessage(@PathVariable UUID sessionId,
                                                                   @Valid @RequestBody ConversationMessageRequest request) {
        if (!securityService.validateMessage(request.content())) {
            return ResponseEntity.badRequest().build();
        }
        var sanitized = securityService.sanitizeMessage(request.content());
        var role = MessageRole.valueOf(request.role().toUpperCase());
        var message = monitoringService.recordMessageLatency(() ->
                messageService.sendMessage(sessionId, role, sanitized));
        monitoringService.recordMessageSent(request.role());
        kafkaPublisher.publishMessageSent(message.id(), sessionId, request.role());
        return ResponseEntity.status(HttpStatus.CREATED).body(ConversationMessageResponse.from(message));
    }

    @GetMapping("/sessions/{sessionId}/messages")
    @Operation(summary = "Get all messages in a session")
    public ResponseEntity<List<ConversationMessageResponse>> getSessionMessages(@PathVariable UUID sessionId,
                                                                                @RequestParam(defaultValue = "0") int offset,
                                                                                @RequestParam(defaultValue = "100") int limit) {
        var messages = messageService.getSessionMessages(sessionId, limit, offset);
        var response = messages.stream().map(ConversationMessageResponse::from).toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/messages/{messageId}")
    @Operation(summary = "Get a message by ID")
    public ResponseEntity<ConversationMessageResponse> getMessage(@PathVariable UUID messageId) {
        var message = messageService.getMessage(messageId)
                .orElseThrow(() -> new ConversationException("Message not found: " + messageId));
        return ResponseEntity.ok(ConversationMessageResponse.from(message));
    }

    @DeleteMapping("/messages/{messageId}")
    @Operation(summary = "Delete a message")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
        messageService.deleteMessage(messageId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/sessions/{sessionId}/memories")
    @Operation(summary = "Store a memory for a session")
    public ResponseEntity<ConversationMemoryResponse> storeMemory(@PathVariable UUID sessionId,
                                                                  @Valid @RequestBody ConversationMemoryRequest request) {
        var memoryType = MemoryType.valueOf(request.memoryType().toUpperCase());
        var memory = memoryManager.storeMemory(sessionId, memoryType, request.summary(), request.keywords(), request.relevanceScore());
        kafkaPublisher.publishMemoryStored(memory.id(), sessionId, request.memoryType());
        return ResponseEntity.status(HttpStatus.CREATED).body(ConversationMemoryResponse.from(memory));
    }

    @GetMapping("/sessions/{sessionId}/memories")
    @Operation(summary = "Get all memories for a session")
    public ResponseEntity<List<ConversationMemoryResponse>> getSessionMemories(@PathVariable UUID sessionId) {
        var memories = memoryManager.getSessionMemories(sessionId);
        var response = memories.stream().map(ConversationMemoryResponse::from).toList();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/memories/{memoryId}")
    @Operation(summary = "Delete a memory")
    public ResponseEntity<Void> deleteMemory(@PathVariable UUID memoryId) {
        memoryManager.deleteMemory(memoryId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/sessions/{sessionId}/context")
    @Operation(summary = "Get context for a session")
    public ResponseEntity<List<Map<String, Object>>> getContext(@PathVariable UUID sessionId) {
        var context = contextBuilder.buildContext(sessionId);
        var response = context.stream()
                .map(e -> Map.<String, Object>of("source", e.source(), "content", e.content(), "weight", e.weight()))
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/sessions/{sessionId}/sources")
    @Operation(summary = "Get context sources for a session")
    public ResponseEntity<Map<String, Double>> getContextSources(@PathVariable UUID sessionId) {
        return ResponseEntity.ok(contextBuilder.getContextSources(sessionId));
    }

    @PostMapping("/sessions/{sessionId}/context/refresh")
    @Operation(summary = "Refresh context for a session")
    public ResponseEntity<Void> refreshContext(@PathVariable UUID sessionId) {
        contextBuilder.refreshContext(sessionId);
        kafkaPublisher.publishContextRefreshed(sessionId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/health")
    @Operation(summary = "Health check for conversation module")
    public ResponseEntity<ConversationMonitoringService.HealthStatus> health() {
        return ResponseEntity.ok(monitoringService.checkHealth());
    }
}
