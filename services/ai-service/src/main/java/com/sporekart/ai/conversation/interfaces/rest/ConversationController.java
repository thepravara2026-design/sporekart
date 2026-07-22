package com.sporekart.ai.conversation.interfaces.rest;

import com.sporekart.ai.conversation.api.ConversationService;
import com.sporekart.ai.conversation.api.ContextWindowService;
import com.sporekart.ai.conversation.api.MemoryService;
import com.sporekart.ai.conversation.api.SessionService;
import com.sporekart.ai.conversation.api.SummarizerService;
import com.sporekart.ai.conversation.application.MessageEngine;
import com.sporekart.ai.conversation.application.MemoryRetrievalEngine;
import com.sporekart.ai.conversation.domain.Conversation;
import com.sporekart.ai.conversation.domain.ConversationId;
import com.sporekart.ai.conversation.domain.ConversationStatus;
import com.sporekart.ai.conversation.domain.MemoryEntry;
import com.sporekart.ai.conversation.domain.MemoryLayer;
import com.sporekart.ai.conversation.domain.Message;
import com.sporekart.ai.conversation.domain.MessageType;
import com.sporekart.ai.conversation.domain.Session;
import com.sporekart.ai.conversation.domain.SessionId;
import com.sporekart.ai.conversation.domain.Summary;
import com.sporekart.ai.conversation.domain.WorkspaceId;
import com.sporekart.ai.conversation.interfaces.rest.dto.AttachmentResponse;
import com.sporekart.ai.conversation.interfaces.rest.dto.CitationResponse;
import com.sporekart.ai.conversation.interfaces.rest.dto.ConversationResponse;
import com.sporekart.ai.conversation.interfaces.rest.dto.CreateConversationRequest;
import com.sporekart.ai.conversation.interfaces.rest.dto.CreateSessionRequest;
import com.sporekart.ai.conversation.interfaces.rest.dto.MemoryEntryResponse;
import com.sporekart.ai.conversation.interfaces.rest.dto.MemoryQueryRequest;
import com.sporekart.ai.conversation.interfaces.rest.dto.MessageRequest;
import com.sporekart.ai.conversation.interfaces.rest.dto.MessageResponse;
import com.sporekart.ai.conversation.interfaces.rest.dto.RestoreRequest;
import com.sporekart.ai.conversation.interfaces.rest.dto.RestoreResponse;
import com.sporekart.ai.conversation.interfaces.rest.dto.SessionResponse;
import com.sporekart.ai.conversation.interfaces.rest.dto.SummarizeResponse;
import com.sporekart.ai.conversation.interfaces.rest.dto.ToolCallResponse;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/ai")
@CrossOrigin(origins = "*")
public class ConversationController {

    private static final Logger log = LoggerFactory.getLogger(ConversationController.class);

    private final ConversationService conversationService;
    private final MessageEngine messageEngine;
    private final SummarizerService summarizerService;
    private final MemoryService memoryService;
    private final MemoryRetrievalEngine memoryRetrievalEngine;
    private final SessionService sessionService;
    private final ContextWindowService contextWindowService;

    public ConversationController(ConversationService conversationService,
                                  MessageEngine messageEngine,
                                  SummarizerService summarizerService,
                                  MemoryService memoryService,
                                  MemoryRetrievalEngine memoryRetrievalEngine,
                                  SessionService sessionService,
                                  ContextWindowService contextWindowService) {
        this.conversationService = conversationService;
        this.messageEngine = messageEngine;
        this.summarizerService = summarizerService;
        this.memoryService = memoryService;
        this.memoryRetrievalEngine = memoryRetrievalEngine;
        this.sessionService = sessionService;
        this.contextWindowService = contextWindowService;
    }

    @PostMapping("/conversations")
    public ResponseEntity<ConversationResponse> createConversation(
            @Valid @RequestBody CreateConversationRequest request) {
        var sessionId = SessionId.fromString(request.getSessionId());
        var workspaceId = request.getWorkspaceId() != null
                ? WorkspaceId.fromString(request.getWorkspaceId()) : null;
        var conv = conversationService.createConversation(
                request.getTitle(), sessionId, workspaceId, null);
        log.info("Created conversation: {}", conv.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(toConversationResponse(conv));
    }

    @GetMapping("/conversations")
    public ResponseEntity<List<ConversationResponse>> listConversations(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String workspaceId,
            @RequestParam(required = false) String status) {
        var wsId = workspaceId != null ? WorkspaceId.fromString(workspaceId) : null;
        var convStatus = status != null ? ConversationStatus.valueOf(status.toUpperCase()) : null;
        var convs = conversationService.listConversations(userId, wsId, convStatus);
        var response = convs.stream().map(this::toConversationResponse).toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/conversations/{id}")
    public ResponseEntity<ConversationResponse> getConversation(@PathVariable String id) {
        var convId = ConversationId.fromString(id);
        var conv = conversationService.getConversation(convId);
        return ResponseEntity.ok(toConversationResponse(conv));
    }

    @DeleteMapping("/conversations/{id}")
    public ResponseEntity<Void> deleteConversation(@PathVariable String id) {
        var convId = ConversationId.fromString(id);
        conversationService.deleteConversation(convId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/conversations/{id}/messages")
    public ResponseEntity<MessageResponse> addMessage(
            @PathVariable String id,
            @Valid @RequestBody MessageRequest request) {
        var convId = ConversationId.fromString(id);
        var messageType = MessageType.valueOf(request.getType().toUpperCase());
        var msg = messageEngine.createMessage(convId, messageType, request.getContent(),
                request.getRole(), request.getMetadata());
        conversationService.addMessage(convId, msg);
        log.info("Added message {} to conversation {}", msg.getId(), id);
        return ResponseEntity.status(HttpStatus.CREATED).body(toMessageResponse(msg));
    }

    @GetMapping("/conversations/{id}/messages")
    public ResponseEntity<List<MessageResponse>> getMessages(
            @PathVariable String id,
            @RequestParam(required = false) String after) {
        var convId = ConversationId.fromString(id);
        List<Message> messages;
        if (after != null) {
            var afterInstant = Instant.parse(after);
            messages = messageEngine.getConversationMessagesAfter(convId, afterInstant);
        } else {
            messages = messageEngine.getConversationMessages(convId);
        }
        var response = messages.stream().map(this::toMessageResponse).toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/conversations/{id}/summarize")
    public ResponseEntity<SummarizeResponse> summarizeConversation(@PathVariable String id) {
        var convId = ConversationId.fromString(id);
        var messages = messageEngine.getConversationMessages(convId);
        var summary = summarizerService.summarize(convId, messages);
        log.info("Summarized conversation {} with strategy {}", id, summary.getCompressionStrategy());
        return ResponseEntity.ok(toSummarizeResponse(summary));
    }

    @PostMapping("/conversations/{id}/restore")
    public ResponseEntity<RestoreResponse> restoreConversation(
            @PathVariable String id,
            @Valid @RequestBody RestoreRequest request) {
        var convId = ConversationId.fromString(id);
        var summaries = summarizerService.getSummaries(convId);
        var summary = summaries.stream()
                .filter(s -> s.getId().equals(request.getSummaryId()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Summary not found: " + request.getSummaryId()));
        var recentMessages = request.getRecentMessages() != null
                ? request.getRecentMessages().stream()
                    .map(r -> messageEngine.createMessage(convId,
                            r.getType() != null ? MessageType.valueOf(r.getType().toUpperCase()) : MessageType.USER,
                            r.getContent(), r.getRole(), r.getMetadata()))
                    .toList()
                : List.<Message>of();
        var restored = summarizerService.restoreFromSummary(summary, recentMessages);
        return ResponseEntity.ok(new RestoreResponse(restored,
                summary.getOriginalTokenCount() != null ? summary.getOriginalTokenCount() : 0,
                recentMessages.size()));
    }

    @PostMapping("/conversations/{id}/close")
    public ResponseEntity<ConversationResponse> closeConversation(@PathVariable String id) {
        var convId = ConversationId.fromString(id);
        var conv = conversationService.closeConversation(convId);
        log.info("Closed conversation: {}", id);
        return ResponseEntity.ok(toConversationResponse(conv));
    }

    @PostMapping("/conversations/{id}/archive")
    public ResponseEntity<ConversationResponse> archiveConversation(@PathVariable String id) {
        var convId = ConversationId.fromString(id);
        var conv = conversationService.archiveConversation(convId);
        log.info("Archived conversation: {}", id);
        return ResponseEntity.ok(toConversationResponse(conv));
    }

    @PostMapping("/conversations/{id}/restore-from-archive")
    public ResponseEntity<ConversationResponse> restoreFromArchive(@PathVariable String id) {
        var convId = ConversationId.fromString(id);
        var conv = conversationService.restoreConversation(convId);
        log.info("Restored conversation from archive: {}", id);
        return ResponseEntity.ok(toConversationResponse(conv));
    }

    @GetMapping("/memory")
    public ResponseEntity<List<MemoryEntryResponse>> getMemory(
            @RequestParam String layer,
            @RequestParam(required = false) String key) {
        var memoryLayer = MemoryLayer.valueOf(layer.toUpperCase());
        List<MemoryEntry> entries;
        if (key != null) {
            var opt = memoryService.retrieve(key, memoryLayer);
            entries = opt.map(List::of).orElse(List.of());
        } else {
            entries = memoryService.retrieveByLayer(memoryLayer);
        }
        var response = entries.stream().map(this::toMemoryEntryResponse).toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/memory/query")
    public ResponseEntity<List<MemoryEntryResponse>> queryMemory(
            @Valid @RequestBody MemoryQueryRequest request) {
        var memoryLayer = request.getLayer() != null
                ? MemoryLayer.valueOf(request.getLayer().toUpperCase()) : null;
        List<MemoryEntry> entries;
        if (memoryLayer != null && request.getQuery() != null) {
            entries = memoryRetrievalEngine.searchMemory(request.getQuery(), memoryLayer);
        } else if (memoryLayer != null) {
            entries = memoryService.retrieveByLayer(memoryLayer);
        } else {
            entries = List.of();
        }
        if (request.getLimit() != null && request.getLimit() > 0) {
            entries = entries.stream().limit(request.getLimit()).toList();
        }
        var response = entries.stream().map(this::toMemoryEntryResponse).toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/memory/store")
    public ResponseEntity<Void> storeMemory(
            @RequestParam String layer,
            @RequestParam String key,
            @RequestParam String value,
            @RequestParam(required = false) Long ttlSeconds,
            @RequestParam(required = false) String workspaceId) {
        var memoryLayer = MemoryLayer.valueOf(layer.toUpperCase());
        var metadata = workspaceId != null
                ? Map.of("workspaceId", workspaceId)
                : Map.<String, String>of();
        if (ttlSeconds != null && ttlSeconds > 0) {
            memoryService.store(memoryLayer, key, value, metadata, Duration.ofSeconds(ttlSeconds));
        } else {
            memoryService.store(memoryLayer, key, value, metadata);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/memory/clear")
    public ResponseEntity<Void> clearMemory(
            @RequestParam String layer,
            @RequestParam(required = false) String workspaceId) {
        var memoryLayer = MemoryLayer.valueOf(layer.toUpperCase());
        if (workspaceId != null) {
            memoryService.clear(MemoryLayer.valueOf(layer.toUpperCase()));
        } else {
            memoryService.clear(memoryLayer);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/sessions")
    public ResponseEntity<List<SessionResponse>> listSessions(
            @RequestParam(required = false) String userId) {
        var sessions = sessionService.listSessions(userId);
        var response = sessions.stream().map(this::toSessionResponse).toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/sessions")
    public ResponseEntity<SessionResponse> createSession(
            @Valid @RequestBody CreateSessionRequest request) {
        var workspaceId = request.getWorkspaceId() != null
                ? WorkspaceId.fromString(request.getWorkspaceId()) : null;
        var idleTimeout = request.getIdleTimeoutMinutes() != null
                ? Duration.ofMinutes(request.getIdleTimeoutMinutes()) : null;
        var session = sessionService.createSession(request.getUserId(), workspaceId, idleTimeout);
        log.info("Created session: {}", session.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(toSessionResponse(session));
    }

    @DeleteMapping("/sessions/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable String id) {
        var sessionId = SessionId.fromString(id);
        sessionService.closeSession(sessionId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/sessions/{id}/activity")
    public ResponseEntity<SessionResponse> recordActivity(@PathVariable String id) {
        var sessionId = SessionId.fromString(id);
        var session = sessionService.recordActivity(sessionId);
        return ResponseEntity.ok(toSessionResponse(session));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Not Found", "message", ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Bad Request", "message", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleInternal(Exception ex) {
        log.error("Unexpected error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Internal Server Error", "message", ex.getMessage()));
    }

    private ConversationResponse toConversationResponse(Conversation conv) {
        return new ConversationResponse(
                conv.getId().toString(),
                conv.getTitle(),
                conv.getStatus().name(),
                conv.getSessionId() != null ? conv.getSessionId().toString() : null,
                conv.getWorkspaceId() != null ? conv.getWorkspaceId().toString() : null,
                conv.getUserId(),
                conv.getParticipantIds(),
                conv.getMetadata(),
                conv.getCreatedAt(),
                conv.getUpdatedAt(),
                conv.getClosedAt(),
                conv.getMessageCount(),
                conv.getTotalTokenUsage());
    }

    private MessageResponse toMessageResponse(Message msg) {
        return new MessageResponse(
                msg.getId().toString(),
                msg.getConversationId().toString(),
                msg.getType().name(),
                msg.getContent(),
                msg.getRole(),
                msg.getMetadata(),
                msg.getCitations() != null
                        ? msg.getCitations().stream()
                                .map(c -> new CitationResponse(c.sourceId(), c.sourceType(), c.snippet(), c.relevanceScore()))
                                .toList()
                        : null,
                msg.getAttachments() != null
                        ? msg.getAttachments().stream()
                                .map(a -> new AttachmentResponse(a.name(), a.type(), a.url(), a.size()))
                                .toList()
                        : null,
                msg.getToolCall() != null
                        ? new ToolCallResponse(msg.getToolCall().toolName(), msg.getToolCall().arguments(),
                                msg.getToolCall().result(), msg.getToolCall().status())
                        : null,
                msg.getTokenCount(),
                msg.getTokenUsage(),
                msg.getProviderUsed(),
                msg.getLatencyMs(),
                msg.getStatus(),
                msg.getCreatedAt());
    }

    private SessionResponse toSessionResponse(Session session) {
        return new SessionResponse(
                session.getId().toString(),
                session.getUserId(),
                session.getWorkspaceId() != null ? session.getWorkspaceId().toString() : null,
                session.getStatus(),
                session.getMetadata(),
                session.getConversationIds() != null
                        ? session.getConversationIds().stream().map(Object::toString).toList()
                        : null,
                session.getCreatedAt(),
                session.getLastActivityAt(),
                session.getExpiresAt());
    }

    private SummarizeResponse toSummarizeResponse(Summary summary) {
        return new SummarizeResponse(
                summary.getId(),
                summary.getConversationId().toString(),
                summary.getSummary(),
                summary.getCompressionStrategy(),
                summary.getOriginalTokenCount(),
                summary.getCompressedTokenCount(),
                summary.getCompressionRatio(),
                summary.getCreatedAt());
    }

    private MemoryEntryResponse toMemoryEntryResponse(MemoryEntry entry) {
        return new MemoryEntryResponse(
                entry.id(),
                entry.layer().name(),
                entry.key(),
                entry.value(),
                entry.metadata(),
                entry.createdAt(),
                entry.expiresAt());
    }
}
