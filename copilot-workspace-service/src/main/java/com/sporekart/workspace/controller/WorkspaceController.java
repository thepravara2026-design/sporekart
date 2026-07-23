package com.sporekart.workspace.controller;

import com.sporekart.workspace.dto.ChatRequest;
import com.sporekart.workspace.dto.ChatResponse;
import com.sporekart.workspace.dto.ContextResponse;
import com.sporekart.workspace.dto.CopilotListResponse;
import com.sporekart.workspace.dto.HandoffRequest;
import com.sporekart.workspace.dto.HandoffResponse;
import com.sporekart.workspace.dto.HistoryResponse;
import com.sporekart.workspace.dto.SwitchCopilotRequest;
import com.sporekart.workspace.dto.SwitchCopilotResponse;
import com.sporekart.workspace.dto.WorkspaceStatusResponse;
import com.sporekart.workspace.service.CopilotRegistryService;
import com.sporekart.workspace.service.WorkspaceOrchestrator;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/workspace")
public class WorkspaceController {

    private static final Logger log = LoggerFactory.getLogger(WorkspaceController.class);

    private final WorkspaceOrchestrator workspaceOrchestrator;
    private final CopilotRegistryService copilotRegistryService;

    public WorkspaceController(WorkspaceOrchestrator workspaceOrchestrator, CopilotRegistryService copilotRegistryService) {
        this.workspaceOrchestrator = workspaceOrchestrator;
        this.copilotRegistryService = copilotRegistryService;
    }

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(
            @Valid @RequestBody ChatRequest request,
            @RequestHeader Map<String, String> headers) {
        log.info("Chat request received: sessionId={}, preferredCopilot={}, workspaceId={}",
                request.sessionId(), request.preferredCopilot(), request.workspaceId());

        Map<String, Object> userContext = extractUserContext(headers);
        Map<String, Object> pageContext = Map.of(
                "url", request.pageUrl() != null ? request.pageUrl() : "",
                "title", request.pageTitle() != null ? request.pageTitle() : ""
        );

        ChatResponse response = workspaceOrchestrator.processMessage(request, userContext, pageContext);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/stream")
    public SseEmitter stream(
            @Valid @RequestBody ChatRequest request,
            @RequestHeader Map<String, String> headers) {
        log.info("Stream request received: sessionId={}, preferredCopilot={}",
                request.sessionId(), request.preferredCopilot());

        Map<String, Object> userContext = extractUserContext(headers);
        Map<String, Object> pageContext = Map.of(
                "url", request.pageUrl() != null ? request.pageUrl() : "",
                "title", request.pageTitle() != null ? request.pageTitle() : ""
        );

        return workspaceOrchestrator.processStreamMessage(request, userContext, pageContext);
    }

    @GetMapping("/copilots")
    public ResponseEntity<CopilotListResponse> getCopilots() {
        CopilotListResponse response = workspaceOrchestrator.discoverCopilots();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/switch")
    public ResponseEntity<SwitchCopilotResponse> switchCopilot(
            @Valid @RequestBody SwitchCopilotRequest request) {
        log.info("Switch copilot request: sessionId={}, targetCopilotId={}", request.sessionId(), request.targetCopilotId());
        SwitchCopilotResponse response = workspaceOrchestrator.switchCopilot(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/context")
    public ResponseEntity<ContextResponse> getContext(@RequestParam String sessionId) {
        ContextResponse response = workspaceOrchestrator.getContext(sessionId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/handoff")
    public ResponseEntity<HandoffResponse> handoff(@Valid @RequestBody HandoffRequest request) {
        log.info("Handoff request: sessionId={}, from={}, to={}", request.sessionId(), request.fromCopilotId(), request.toCopilotId());
        HandoffResponse response = workspaceOrchestrator.handoff(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    public ResponseEntity<HistoryResponse> getHistory(
            @RequestParam String sessionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        HistoryResponse response = workspaceOrchestrator.getHistory(sessionId, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status")
    public ResponseEntity<WorkspaceStatusResponse> getStatus(@RequestParam String workspaceId) {
        WorkspaceStatusResponse response = workspaceOrchestrator.getWorkspaceStatus(workspaceId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "copilot-workspace-service");
        health.put("timestamp", java.time.OffsetDateTime.now().toString());
        return ResponseEntity.ok(health);
    }

    private Map<String, Object> extractUserContext(Map<String, String> headers) {
        Map<String, Object> context = new HashMap<>();
        if (headers.containsKey("x-user-id")) {
            context.put("userId", headers.get("x-user-id"));
        }
        if (headers.containsKey("x-organization-id")) {
            context.put("organizationId", headers.get("x-organization-id"));
        }
        if (headers.containsKey("x-tenant-id")) {
            context.put("tenantId", headers.get("x-tenant-id"));
        }
        if (headers.containsKey("authorization")) {
            context.put("authToken", "present");
        }
        return context;
    }
}
