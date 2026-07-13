package com.sporekart.ai.conversation.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.conversation.api.ContextBuilder;
import com.sporekart.ai.conversation.api.MemoryManager;
import com.sporekart.ai.conversation.api.MessageService;
import com.sporekart.ai.conversation.api.SessionManager;
import com.sporekart.ai.conversation.application.ConversationSecurityService;
import com.sporekart.ai.conversation.domain.ConversationSession;
import com.sporekart.ai.conversation.domain.ConversationStatus;
import com.sporekart.ai.conversation.domain.MemoryEntry;
import com.sporekart.ai.conversation.domain.MemoryType;
import com.sporekart.ai.conversation.domain.MessageRole;
import com.sporekart.ai.conversation.domain.MessageStatus;
import com.sporekart.ai.conversation.domain.ConversationMessage;
import com.sporekart.ai.conversation.infrastructure.kafka.ConversationKafkaEventPublisher;
import com.sporekart.ai.conversation.infrastructure.monitoring.ConversationMonitoringService;
import com.sporekart.ai.conversation.interfaces.rest.dto.ConversationSessionRequest;
import com.sporekart.ai.conversation.interfaces.rest.dto.ConversationMessageRequest;
import com.sporekart.ai.conversation.interfaces.rest.dto.ConversationMemoryRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ConversationControllerTest {

    @Mock private SessionManager sessionManager;
    @Mock private MessageService messageService;
    @Mock private MemoryManager memoryManager;
    @Mock private ContextBuilder contextBuilder;
    @Mock private ConversationSecurityService securityService;
    @Mock private ConversationMonitoringService monitoringService;
    @Mock private ConversationKafkaEventPublisher kafkaPublisher;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        var controller = new ConversationController(sessionManager, messageService, memoryManager,
                contextBuilder, securityService, monitoringService, kafkaPublisher);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldCreateSession() throws Exception {
        var session = new ConversationSession(UUID.randomUUID(), "user-1", "Test", ConversationStatus.ACTIVE,
                null, OffsetDateTime.now(), OffsetDateTime.now(), null);
        when(securityService.checkRateLimit("user-1")).thenReturn(true);
        when(monitoringService.recordSessionLatency(any())).thenAnswer(inv -> {
            var supplier = inv.<java.util.function.Supplier<ConversationSession>>getArgument(0);
            return supplier.get();
        });
        when(sessionManager.createSession("user-1", "Test")).thenReturn(session);

        var request = new ConversationSessionRequest("user-1", "Test");
        mockMvc.perform(post("/api/v1/conversation/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value("user-1"))
                .andExpect(jsonPath("$.title").value("Test"));
    }

    @Test
    void shouldReturn429WhenRateLimited() throws Exception {
        when(securityService.checkRateLimit("user-1")).thenReturn(false);

        var request = new ConversationSessionRequest("user-1", "Test");
        mockMvc.perform(post("/api/v1/conversation/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isTooManyRequests());
    }

    @Test
    void shouldGetSession() throws Exception {
        var id = UUID.randomUUID();
        var session = new ConversationSession(id, "user-1", "Test", ConversationStatus.ACTIVE,
                null, OffsetDateTime.now(), OffsetDateTime.now(), null);
        when(monitoringService.recordSessionLatency(any())).thenAnswer(inv -> {
            var supplier = inv.<java.util.function.Supplier<Optional<ConversationSession>>>getArgument(0);
            return supplier.get();
        });
        when(sessionManager.getSession(id)).thenReturn(Optional.of(session));

        mockMvc.perform(get("/api/v1/conversation/sessions/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    void shouldGetUserSessions() throws Exception {
        when(monitoringService.recordSessionLatency(any())).thenAnswer(inv -> {
            var supplier = inv.<java.util.function.Supplier<List<ConversationSession>>>getArgument(0);
            return supplier.get();
        });
        when(sessionManager.getUserSessions("user-1")).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/conversation/sessions").param("userId", "user-1"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldSuspendSession() throws Exception {
        var id = UUID.randomUUID();
        var session = new ConversationSession(id, "user-1", "Test", ConversationStatus.ARCHIVED,
                null, OffsetDateTime.now(), OffsetDateTime.now(), null);
        when(monitoringService.recordSessionLatency(any())).thenAnswer(inv -> {
            var supplier = inv.<java.util.function.Supplier<ConversationSession>>getArgument(0);
            return supplier.get();
        });
        when(sessionManager.suspendSession(id)).thenReturn(session);

        mockMvc.perform(put("/api/v1/conversation/sessions/{id}/suspend", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ARCHIVED"));
    }

    @Test
    void shouldResumeSession() throws Exception {
        var id = UUID.randomUUID();
        var session = new ConversationSession(id, "user-1", "Test", ConversationStatus.ACTIVE,
                null, OffsetDateTime.now(), OffsetDateTime.now(), null);
        when(monitoringService.recordSessionLatency(any())).thenAnswer(inv -> {
            var supplier = inv.<java.util.function.Supplier<ConversationSession>>getArgument(0);
            return supplier.get();
        });
        when(sessionManager.resumeSession(id)).thenReturn(session);

        mockMvc.perform(put("/api/v1/conversation/sessions/{id}/resume", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void shouldCloseSession() throws Exception {
        var id = UUID.randomUUID();
        var session = new ConversationSession(id, "user-1", "Test", ConversationStatus.CLOSED,
                null, OffsetDateTime.now(), OffsetDateTime.now(), null);
        when(monitoringService.recordSessionLatency(any())).thenAnswer(inv -> {
            var supplier = inv.<java.util.function.Supplier<ConversationSession>>getArgument(0);
            return supplier.get();
        });
        when(sessionManager.closeSession(id)).thenReturn(session);

        mockMvc.perform(put("/api/v1/conversation/sessions/{id}/close", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CLOSED"));
    }

    @Test
    void shouldDeleteSession() throws Exception {
        var id = UUID.randomUUID();

        mockMvc.perform(delete("/api/v1/conversation/sessions/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldSendMessage() throws Exception {
        var sessionId = UUID.randomUUID();
        var msg = new ConversationMessage(UUID.randomUUID(), sessionId, MessageRole.USER, "Hello", null, MessageStatus.SENT, OffsetDateTime.now());
        when(securityService.validateMessage("Hello")).thenReturn(true);
        when(securityService.sanitizeMessage("Hello")).thenReturn("Hello");
        when(monitoringService.recordMessageLatency(any())).thenAnswer(inv -> {
            var supplier = inv.<java.util.function.Supplier<ConversationMessage>>getArgument(0);
            return supplier.get();
        });
        when(messageService.sendMessage(sessionId, MessageRole.USER, "Hello")).thenReturn(msg);

        var request = new ConversationMessageRequest("USER", "Hello");
        mockMvc.perform(post("/api/v1/conversation/sessions/{id}/messages", sessionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void shouldReturn400ForBlankContentMessage() throws Exception {
        var request = new ConversationMessageRequest("USER", "");
        mockMvc.perform(post("/api/v1/conversation/sessions/{id}/messages", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetSessionMessages() throws Exception {
        var sessionId = UUID.randomUUID();
        when(messageService.getSessionMessages(sessionId, 100, 0)).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/conversation/sessions/{id}/messages", sessionId))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetMessage() throws Exception {
        var id = UUID.randomUUID();
        var msg = new ConversationMessage(id, UUID.randomUUID(), MessageRole.USER, "Hello", null, MessageStatus.SENT, OffsetDateTime.now());
        when(messageService.getMessage(id)).thenReturn(Optional.of(msg));

        mockMvc.perform(get("/api/v1/conversation/messages/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteMessage() throws Exception {
        var id = UUID.randomUUID();

        mockMvc.perform(delete("/api/v1/conversation/messages/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldStoreMemory() throws Exception {
        var sessionId = UUID.randomUUID();
        var memory = new MemoryEntry(UUID.randomUUID(), sessionId, MemoryType.SHORT_TERM, "Summary", "kw", 0.8, OffsetDateTime.now(), null);
        when(memoryManager.storeMemory(sessionId, MemoryType.SHORT_TERM, "Summary", "kw", 0.8)).thenReturn(memory);

        var request = new ConversationMemoryRequest("SHORT_TERM", "Summary", "kw", 0.8);
        mockMvc.perform(post("/api/v1/conversation/sessions/{id}/memories", sessionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldGetSessionMemories() throws Exception {
        var sessionId = UUID.randomUUID();
        when(memoryManager.getSessionMemories(sessionId)).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/conversation/sessions/{id}/memories", sessionId))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteMemory() throws Exception {
        var id = UUID.randomUUID();

        mockMvc.perform(delete("/api/v1/conversation/memories/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldGetContext() throws Exception {
        var sessionId = UUID.randomUUID();
        when(contextBuilder.buildContext(sessionId)).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/conversation/sessions/{id}/context", sessionId))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetContextSources() throws Exception {
        var sessionId = UUID.randomUUID();
        when(contextBuilder.getContextSources(sessionId)).thenReturn(Map.of("knowledge", 1.0));

        mockMvc.perform(get("/api/v1/conversation/sessions/{id}/sources", sessionId))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRefreshContext() throws Exception {
        var sessionId = UUID.randomUUID();

        mockMvc.perform(post("/api/v1/conversation/sessions/{id}/context/refresh", sessionId))
                .andExpect(status().isOk());
    }

    @Test
    void shouldCheckHealth() throws Exception {
        var health = new ConversationMonitoringService.HealthStatus("UP", 0, true);
        when(monitoringService.checkHealth()).thenReturn(health);

        mockMvc.perform(get("/api/v1/conversation/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }
}
