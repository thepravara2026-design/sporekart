package com.sporekart.customer.service;

import com.sporekart.copilot.*;
import com.sporekart.customer.copilot.config.CustomerCopilotConfig;
import com.sporekart.customer.copilot.domain.ConversationMessage;
import com.sporekart.customer.copilot.domain.CustomerProfile;
import com.sporekart.customer.copilot.dto.ChatResponse;
import com.sporekart.customer.copilot.service.CustomerContextService;
import com.sporekart.customer.copilot.service.CustomerCopilotOrchestrator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerCopilotOrchestratorTest {

    @Mock
    private CopilotEngine copilotEngine;

    @Mock
    private CustomerContextService contextService;

    @Mock
    private CustomerCopilotConfig config;

    private CustomerCopilotOrchestrator orchestrator;

    @BeforeEach
    void setUp() {
        orchestrator = new CustomerCopilotOrchestrator(copilotEngine, contextService, config);

        when(config.getName()).thenReturn("Customer Copilot");
        when(config.getVersion()).thenReturn("0.1.0");
    }

    @Test
    void processMessageReturnsChatResponse() {
        var sessionId = "test-session";
        var userContext = UserContext.builder()
            .userId("user1").userName("Test").email("test@test.com")
            .roles(List.of("CUSTOMER")).workspaceId("default")
            .organizationId("org").tenantId("tenant").deviceType("web")
            .language("en").timeZone("UTC").build();
        var pageContext = PageContext.empty();

        var profile = new CustomerProfile("user1", "Test", "test@test.com",
            "en", Map.of(), OffsetDateTime.now());

        var copilotResponse = new CopilotResponse("Hello!", List.of(Suggestion.of("Search", "search")),
            Map.of("key", "val"), OffsetDateTime.now(), false);

        when(contextService.getOrCreateProfile(any(UserContext.class))).thenReturn(profile);
        when(copilotEngine.processMessage(any(SessionId.class), anyString(), any(UserContext.class), any(PageContext.class)))
            .thenReturn(CompletableFuture.completedFuture(copilotResponse));

        var response = orchestrator.processMessage(sessionId, "Hello", userContext, pageContext);

        assertNotNull(response);
        assertEquals(sessionId, response.sessionId());
        assertNotNull(response.message());
        assertFalse(response.message().isBlank());
    }

    @Test
    void processMessageCreatesSessionForNewCustomer() {
        var userContext = UserContext.builder()
            .userId("new-user").userName("New").email("new@test.com")
            .roles(List.of("CUSTOMER")).workspaceId("default")
            .organizationId("org").tenantId("tenant").deviceType("web")
            .language("en").timeZone("UTC").build();
        var pageContext = PageContext.empty();

        var profile = new CustomerProfile("new-user", "New", "new@test.com",
            "en", Map.of(), OffsetDateTime.now());

        var copilotResponse = new CopilotResponse("Welcome!", List.of(), Map.of(), OffsetDateTime.now(), false);

        when(contextService.getOrCreateProfile(any(UserContext.class))).thenReturn(profile);
        when(copilotEngine.processMessage(any(SessionId.class), anyString(), any(UserContext.class), any(PageContext.class)))
            .thenReturn(CompletableFuture.completedFuture(copilotResponse));

        var response = orchestrator.processMessage("", "Hi", userContext, pageContext);

        assertNotNull(response);
        assertNotNull(response.sessionId());
    }

    @Test
    void processMessageReturnsSuggestionsWithChatResponse() {
        var sessionId = "suggestions-test";
        var userContext = UserContext.builder()
            .userId("user2").userName("Test").email("test@test.com")
            .roles(List.of("CUSTOMER")).workspaceId("default")
            .organizationId("org").tenantId("tenant").deviceType("web")
            .language("en").timeZone("UTC").build();
        var pageContext = PageContext.empty();

        var profile = new CustomerProfile("user2", "Test", "test@test.com",
            "en", Map.of(), OffsetDateTime.now());

        var suggestions = List.of(
            Suggestion.of("Search Products", "search"),
            Suggestion.of("Track Order", "track")
        );

        var copilotResponse = new CopilotResponse("How can I help?", suggestions, Map.of(), OffsetDateTime.now(), false);

        when(contextService.getOrCreateProfile(any(UserContext.class))).thenReturn(profile);
        when(copilotEngine.processMessage(any(SessionId.class), anyString(), any(UserContext.class), any(PageContext.class)))
            .thenReturn(CompletableFuture.completedFuture(copilotResponse));

        var response = orchestrator.processMessage(sessionId, "Help", userContext, pageContext);

        assertNotNull(response);
        assertNotNull(response.suggestions());
        assertFalse(response.suggestions().isEmpty());
        assertEquals(2, response.suggestions().size());
    }

    @Test
    void processMessageHandlesMissingSessionGracefully() {
        var userContext = UserContext.builder()
            .userId("user3").userName("Test").email("test@test.com")
            .roles(List.of("CUSTOMER")).workspaceId("default")
            .organizationId("org").tenantId("tenant").deviceType("web")
            .language("en").timeZone("UTC").build();
        var pageContext = PageContext.empty();

        var profile = new CustomerProfile("user3", "Test", "test@test.com",
            "en", Map.of(), OffsetDateTime.now());

        var copilotResponse = new CopilotResponse("Session created!", List.of(), Map.of(), OffsetDateTime.now(), false);

        when(contextService.getOrCreateProfile(any(UserContext.class))).thenReturn(profile);
        when(copilotEngine.processMessage(any(SessionId.class), anyString(), any(UserContext.class), any(PageContext.class)))
            .thenReturn(CompletableFuture.completedFuture(copilotResponse));

        var result = orchestrator.processMessage("nonexistent-session", "Hello", userContext, pageContext);

        assertNotNull(result);
        assertNotNull(result.message());
    }

    @Test
    void processMessageIncludesContextInResponse() {
        var sessionId = "context-test";
        var userContext = UserContext.builder()
            .userId("user4").userName("Test").email("test@test.com")
            .roles(List.of("CUSTOMER")).workspaceId("default")
            .organizationId("org").tenantId("tenant").deviceType("web")
            .language("en").timeZone("UTC").build();
        var pageContext = PageContext.builder()
            .pageUrl("/products").pageTitle("Products").build();

        var profile = new CustomerProfile("user4", "Test", "test@test.com",
            "en", Map.of(), OffsetDateTime.now());

        var contextMap = Map.of("pageUrl", "/products", "intent", "browsing");
        var copilotResponse = new CopilotResponse("Sure!", List.of(), contextMap, OffsetDateTime.now(), false);

        when(contextService.getOrCreateProfile(any(UserContext.class))).thenReturn(profile);
        when(copilotEngine.processMessage(any(SessionId.class), anyString(), any(UserContext.class), any(PageContext.class)))
            .thenReturn(CompletableFuture.completedFuture(copilotResponse));

        var response = orchestrator.processMessage(sessionId, "Show me products", userContext, pageContext);

        assertNotNull(response);
        assertNotNull(response.context());
        assertFalse(response.context().isEmpty());
    }

    @Test
    void getConversationHistoryReturnsMessages() {
        var sessionId = "history-test";
        var messages = List.of(
            new ConversationMessage("m1", "user", "Hello", Map.of(), OffsetDateTime.now().toString()),
            new ConversationMessage("m2", "assistant", "Hi there", Map.of(), OffsetDateTime.now().toString())
        );

        when(contextService.getConversationHistory(sessionId)).thenReturn(messages);

        var history = orchestrator.getConversationHistory(sessionId);

        assertNotNull(history);
        assertEquals(2, history.size());
    }

    @Test
    void getConversationHistoryForEmptySessionReturnsEmpty() {
        when(contextService.getConversationHistory("empty")).thenReturn(List.of());

        var history = orchestrator.getConversationHistory("empty");

        assertNotNull(history);
        assertTrue(history.isEmpty());
    }

    @Test
    void processMessageHandlesEngineError() {
        var sessionId = "error-test";
        var userContext = UserContext.builder()
            .userId("user5").userName("Test").email("test@test.com")
            .roles(List.of("CUSTOMER")).workspaceId("default")
            .organizationId("org").tenantId("tenant").deviceType("web")
            .language("en").timeZone("UTC").build();
        var pageContext = PageContext.empty();

        var profile = new CustomerProfile("user5", "Test", "test@test.com",
            "en", Map.of(), OffsetDateTime.now());

        when(contextService.getOrCreateProfile(any(UserContext.class))).thenReturn(profile);
        when(copilotEngine.processMessage(any(SessionId.class), anyString(), any(UserContext.class), any(PageContext.class)))
            .thenReturn(CompletableFuture.failedFuture(new RuntimeException("Engine error")));

        var response = orchestrator.processMessage(sessionId, "Hello", userContext, pageContext);

        assertNotNull(response);
        assertNotNull(response.message());
        assertFalse(response.message().isBlank());
    }
}
