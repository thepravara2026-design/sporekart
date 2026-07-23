package com.sporekart.copilot.service;

import com.sporekart.copilot.context.ContextAssembler;
import com.sporekart.copilot.context.CopilotContext;
import com.sporekart.copilot.context.PageContext;
import com.sporekart.copilot.context.UserContext;
import com.sporekart.copilot.core.CopilotEngine;
import com.sporekart.copilot.domain.*;
import com.sporekart.copilot.dto.ChatRequest;
import com.sporekart.copilot.dto.ChatResponse;
import com.sporekart.copilot.persona.PersonaEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CopilotOrchestrationServiceTest {

    @Mock
    private CopilotEngine copilotEngine;

    @Mock
    private CopilotRegistryService registryService;

    @Mock
    private CopilotSessionService sessionService;

    @Mock
    private PersonaEngine personaEngine;

    @Mock
    private ContextAssembler contextAssembler;

    private CopilotOrchestrationService service;

    @BeforeEach
    void setUp() {
        service = new CopilotOrchestrationService(
            copilotEngine, registryService, sessionService, personaEngine, contextAssembler);
    }

    @Test
    void processMessageShouldReturnChatResponse() {
        var user = UserContext.builder().userId("user1").userName("User1").email("").build();
        var page = PageContext.empty();
        var request = new ChatRequest(null, "Hello", null);
        var sessionId = new SessionId();

        var session = new CopilotSession(sessionId, CopilotType.CUSTOMER, user, CopilotStatus.ACTIVE,
            java.time.OffsetDateTime.now(), java.time.OffsetDateTime.now());

        when(sessionService.createSession(any(), any())).thenReturn(session);
        when(contextAssembler.assembleContext(any(), any(), any(), any()))
            .thenReturn(mock(CopilotContext.class));
        when(copilotEngine.processMessage(any(), any(), any(), any()))
            .thenReturn(CompletableFuture.completedFuture(
                CopilotResponse.of("Hello back", CopilotType.CUSTOMER)));

        ChatResponse response = service.processMessage(request, user, page);

        assertNotNull(response);
        assertEquals("Hello back", response.message());
        assertNotNull(response.sessionId());
    }

    @Test
    void processMessageShouldReuseExistingSession() {
        var user = UserContext.builder().userId("user1").userName("User1").email("").build();
        var page = PageContext.empty();
        var sessionId = new SessionId();
        var request = new ChatRequest(sessionId.toString(), "Hello", null);

        var session = new CopilotSession(sessionId, CopilotType.CUSTOMER, user, CopilotStatus.ACTIVE,
            java.time.OffsetDateTime.now(), java.time.OffsetDateTime.now());

        when(sessionService.getSession(any())).thenReturn(Optional.of(session));
        when(contextAssembler.assembleContext(any(), any(), any(), any()))
            .thenReturn(mock(CopilotContext.class));
        when(copilotEngine.processMessage(any(), any(), any(), any()))
            .thenReturn(CompletableFuture.completedFuture(
                CopilotResponse.of("Hi", CopilotType.CUSTOMER)));

        ChatResponse response = service.processMessage(request, user, page);

        assertNotNull(response);
        assertEquals(sessionId.toString(), response.sessionId());
        verify(sessionService, never()).createSession(any(), any());
    }

    @Test
    void processMessageShouldHandleEngineError() {
        var user = UserContext.builder().userId("user1").userName("User1").email("").build();
        var page = PageContext.empty();
        var request = new ChatRequest(null, "Hello", null);
        var sessionId = new SessionId();

        var session = new CopilotSession(sessionId, CopilotType.CUSTOMER, user, CopilotStatus.ACTIVE,
            java.time.OffsetDateTime.now(), java.time.OffsetDateTime.now());

        when(sessionService.createSession(any(), any())).thenReturn(session);
        when(contextAssembler.assembleContext(any(), any(), any(), any()))
            .thenReturn(mock(CopilotContext.class));
        when(copilotEngine.processMessage(any(), any(), any(), any()))
            .thenReturn(CompletableFuture.failedFuture(new RuntimeException("Engine failure")));

        assertThrows(java.util.concurrent.CompletionException.class, () -> {
            service.processMessage(request, user, page);
        });
    }
}
