package com.sporekart.copilot.controller;

import com.sporekart.copilot.capability.Capability;
import com.sporekart.copilot.context.PageContext;
import com.sporekart.copilot.context.UserContext;
import com.sporekart.copilot.domain.CopilotRegistration;
import com.sporekart.copilot.domain.CopilotSession;
import com.sporekart.copilot.domain.CopilotType;
import com.sporekart.copilot.domain.SessionId;
import com.sporekart.copilot.dto.*;
import com.sporekart.copilot.persona.Persona;
import com.sporekart.copilot.service.CopilotOrchestrationService;
import com.sporekart.copilot.service.CopilotRegistryService;
import com.sporekart.copilot.service.CopilotSessionService;
import com.sporekart.copilot.service.CopilotStreamingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/copilot")
public class CopilotChatController {

    private static final Logger log = LoggerFactory.getLogger(CopilotChatController.class);

    private final CopilotOrchestrationService orchestrationService;
    private final CopilotRegistryService registryService;
    private final CopilotSessionService sessionService;
    private final CopilotStreamingService streamingService;

    public CopilotChatController(
            CopilotOrchestrationService orchestrationService,
            CopilotRegistryService registryService,
            CopilotSessionService sessionService,
            CopilotStreamingService streamingService) {
        this.orchestrationService = orchestrationService;
        this.registryService = registryService;
        this.sessionService = sessionService;
        this.streamingService = streamingService;
    }

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@RequestBody @Valid ChatRequest request, HttpServletRequest httpRequest) {
        log.info("Chat request received: sessionId={}", request.sessionId());
        var user = resolveUser(httpRequest);
        var page = resolvePage(httpRequest);
        var response = orchestrationService.processMessage(request, user, page);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(@RequestBody @Valid StreamRequest request, HttpServletRequest httpRequest) {
        log.info("Stream request received: sessionId={}", request.sessionId());
        var user = resolveUser(httpRequest);
        var page = resolvePage(httpRequest);

        var sessionId = request.sessionId() != null
            ? SessionId.fromString(request.sessionId())
            : new SessionId();

        var emitter = streamingService.startStream(sessionId);

        var chatRequest = new ChatRequest(request.sessionId(), request.message(), request.context());
        try {
            var response = orchestrationService.processMessage(chatRequest, user, page);
            streamingService.completeStream(sessionId);
        } catch (Exception e) {
            log.error("Stream processing failed: {}", e.getMessage());
            streamingService.errorStream(sessionId, e);
        }

        return emitter;
    }

    @GetMapping("/list")
    public ResponseEntity<List<CopilotInfoResponse>> listCopilots() {
        var copilots = registryService.listCopilots().stream()
            .map(this::toInfoResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(copilots);
    }

    @GetMapping("/capabilities")
    public ResponseEntity<List<Capability>> getCapabilities() {
        return ResponseEntity.ok(registryService.getCapabilities());
    }

    @GetMapping("/personas")
    public ResponseEntity<List<Persona>> getPersonas() {
        return ResponseEntity.ok(registryService.getPersonas());
    }

    @PostMapping("/register")
    public ResponseEntity<CopilotRegistration> registerCopilot(@RequestBody @Valid RegisterCopilotRequest request) {
        log.info("Register copilot request: {}", request.name());
        var registration = registryService.registerCopilot(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(registration);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CopilotInfoResponse> getCopilot(@PathVariable String id) {
        var copilot = registryService.getCopilot(id);
        return ResponseEntity.ok(toInfoResponse(copilot));
    }

    @PutMapping("/{id}/enable")
    public ResponseEntity<Void> enableCopilot(@PathVariable String id) {
        registryService.enableCopilot(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/disable")
    public ResponseEntity<Void> disableCopilot(@PathVariable String id) {
        registryService.disableCopilot(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/health")
    public ResponseEntity<CopilotRegistration> getCopilotHealth(@PathVariable String id) {
        var health = registryService.getCopilotHealth(id);
        return ResponseEntity.ok(health);
    }

    @PostMapping("/session")
    public ResponseEntity<CopilotSession> createSession(@RequestBody @Valid CreateSessionRequest request) {
        log.info("Create session request for user: {}", request.userId());
        CopilotType type;
        try {
            type = CopilotType.valueOf(request.copilotType().toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
        var user = UserContext.builder()
            .userId(request.userId())
            .userName(request.userName())
            .roles(request.roles())
            .email("")
            .build();
        var session = sessionService.createSession(type, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(session);
    }

    @GetMapping("/session/{sessionId}")
    public ResponseEntity<CopilotSession> getSession(@PathVariable String sessionId) {
        var sid = SessionId.fromString(sessionId);
        return sessionService.getSession(sid)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/session/{sessionId}")
    public ResponseEntity<Void> closeSession(@PathVariable String sessionId) {
        var sid = SessionId.fromString(sessionId);
        sessionService.closeSession(sid);
        return ResponseEntity.noContent().build();
    }

    private UserContext resolveUser(HttpServletRequest request) {
        var userId = request.getHeader("X-User-Id");
        var userName = request.getHeader("X-User-Name");
        var rolesHeader = request.getHeader("X-User-Roles");
        var roles = rolesHeader != null ? List.of(rolesHeader.split(",")) : List.of();
        return UserContext.builder()
            .userId(userId != null ? userId : "anonymous")
            .userName(userName != null ? userName : "Anonymous")
            .roles(roles)
            .email("")
            .build();
    }

    private PageContext resolvePage(HttpServletRequest request) {
        var pageUrl = request.getHeader("X-Page-Url");
        var pageTitle = request.getHeader("X-Page-Title");
        return PageContext.builder()
            .pageUrl(pageUrl != null ? pageUrl : "unknown")
            .pageTitle(pageTitle != null ? pageTitle : "Unknown")
            .build();
    }

    private CopilotInfoResponse toInfoResponse(CopilotRegistration r) {
        return new CopilotInfoResponse(
            r.id(), r.name(), r.type().name(), r.status().name(),
            r.version(), r.capabilityIds(), r.registeredAt()
        );
    }
}
