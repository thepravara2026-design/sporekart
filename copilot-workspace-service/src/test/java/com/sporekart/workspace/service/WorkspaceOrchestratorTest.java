package com.sporekart.workspace.service;

import com.sporekart.workspace.domain.CopilotInfo;
import com.sporekart.workspace.domain.RoutedMessage;
import com.sporekart.workspace.dto.*;
import com.sporekart.workspace.engine.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkspaceOrchestratorTest {

    @Mock
    private CopilotRouterEngine routerEngine;

    @Mock
    private CopilotRegistryService registryService;

    @Mock
    private WorkspaceSessionManager sessionManager;

    @Mock
    private ContextBroker contextBroker;

    @Mock
    private CollaborationEngine collaborationEngine;

    @Mock
    private HandoffEngine handoffEngine;

    private WorkspaceOrchestrator orchestrator;

    @BeforeEach
    void setUp() {
        orchestrator = new WorkspaceOrchestrator(routerEngine, registryService, sessionManager,
                contextBroker, collaborationEngine, handoffEngine);
    }

    @Test
    void processMessage_ShouldRouteMessage() {
        ChatRequest request = new ChatRequest("show my order", "sess-1", "ws-1", null, null, null, false);
        when(routerEngine.routeMessage(anyString(), any())).thenReturn(
                new RoutedMessage("msg-1", "show my order", CopilotInfo.TYPE_CUSTOMER, 0.95,
                        "matched order intent", null, false, List.of()));

        ChatResponse response = orchestrator.processMessage(request);
        assertNotNull(response);
        assertEquals(CopilotInfo.TYPE_CUSTOMER, response.copilotId());
    }

    @Test
    void processMessage_ShouldUsePreferredCopilot() {
        ChatRequest request = new ChatRequest("hello", "sess-1", "ws-1", CopilotInfo.TYPE_ADMIN, null, null, false);

        ChatResponse response = orchestrator.processMessage(request);
        assertEquals(CopilotInfo.TYPE_ADMIN, response.copilotId());
    }

    @Test
    void processMessage_ShouldEnableCollaboration() {
        ChatRequest request = new ChatRequest("complex query", "sess-1", "ws-1", null, null, null, true);
        when(routerEngine.routeMessage(anyString(), any())).thenReturn(
                new RoutedMessage("msg-2", "complex query", CopilotInfo.TYPE_ADMIN, 0.8,
                        "matched", null, false, List.of("copilot-2", "copilot-3")));

        ChatResponse response = orchestrator.processMessage(request);
        assertNotNull(response);
    }

    @Test
    void switchCopilot_ShouldSwitchActiveCopilot() {
        SwitchCopilotRequest request = new SwitchCopilotRequest("sess-1", "copilot-2", "user request", true);
        SwitchCopilotResponse response = orchestrator.switchCopilot(request);
        assertNotNull(response);
        assertEquals("sess-1", response.sessionId());
    }

    @Test
    void handoff_ShouldInitiateHandoff() {
        com.sporekart.workspace.dto.HandoffRequest request =
                new com.sporekart.workspace.dto.HandoffRequest("sess-1", "copilot-1", "copilot-2", "needs help", "context");
        when(handoffEngine.initiateHandoff(anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(new com.sporekart.workspace.domain.HandoffRequest(
                        "handoff-1", "sess-1", "copilot-1", "copilot-2", "needs help",
                        "context", "", "PENDING", null));

        HandoffResponse response = orchestrator.handoff(request);
        assertNotNull(response);
        assertEquals("handoff-1", response.handoffId());
    }

    @Test
    void getContext_ShouldReturnContext() {
        ContextResponse response = orchestrator.getContext("sess-1");
        assertNotNull(response);
        assertEquals("sess-1", response.sessionId());
    }

    @Test
    void getHistory_ShouldReturnHistory() {
        HistoryResponse response = orchestrator.getHistory("sess-1", 0, 20);
        assertNotNull(response);
    }

    @Test
    void getWorkspaceStatus_ShouldReturnStatus() {
        WorkspaceStatusResponse response = orchestrator.getWorkspaceStatus("ws-1");
        assertNotNull(response);
        assertEquals("ws-1", response.workspaceId());
    }

    @Test
    void discoverCopilots_ShouldReturnAvailableCopilots() {
        List<CopilotInfo> copilots = List.of(
                new CopilotInfo("c1", "Customer", CopilotInfo.TYPE_CUSTOMER, "1.0", null,
                        true, CopilotInfo.STATUS_ACTIVE, List.of(), List.of(), null, 0, 0, null));
        when(registryService.getAllCopilots()).thenReturn(copilots);

        List<CopilotInfo> result = orchestrator.discoverCopilots("ws-1");
        assertThat(result).hasSize(1);
    }
}
