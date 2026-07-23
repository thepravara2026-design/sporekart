package com.sporekart.admin.service;

import com.sporekart.admin.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AdminCopilotOrchestratorTest {

    private AdminCopilotOrchestrator orchestrator;

    @BeforeEach
    void setUp() {
        AdminCopilotRegistrationService registrationService = new AdminCopilotRegistrationService();
        AdminContextService contextService = new AdminContextService();
        orchestrator = new AdminCopilotOrchestrator(registrationService, contextService);
    }

    @Test
    void processMessageReturnsChatResponse() throws ExecutionException, InterruptedException {
        ChatRequest request = new ChatRequest("Show me the dashboard", null, null, null, null, null);

        ChatResponse response = orchestrator.processMessage(request).get();

        assertNotNull(response);
        assertNotNull(response.sessionId());
        assertNotNull(response.message());
        assertFalse(response.message().isBlank());
    }

    @Test
    void createsSessionForAdminUser() throws ExecutionException, InterruptedException {
        ChatRequest request = new ChatRequest("Hello", null, null, null, null, null);

        ChatResponse response = orchestrator.processMessage(request).get();

        assertNotNull(response.sessionId());
        assertFalse(response.sessionId().isBlank());
    }

    @Test
    void returnsDashboardDataInResponse() {
        DashboardResponse dashboard = orchestrator.getDashboard();

        assertNotNull(dashboard);
        assertNotNull(dashboard.dashboard());
        assertNotNull(dashboard.timestamp());
    }

    @Test
    void returnsSuggestionsWithQueries() throws ExecutionException, InterruptedException {
        ChatRequest request = new ChatRequest("What are my sales numbers?", null, null, null, null, null);

        ChatResponse response = orchestrator.processMessage(request).get();

        assertNotNull(response.suggestions());
    }

    @Test
    void handlesAnalyticsQueries() throws ExecutionException, InterruptedException {
        ChatRequest request = new ChatRequest("Show me revenue analytics for last month", null, null, null, null, null);

        ChatResponse response = orchestrator.processMessage(request).get();

        assertNotNull(response);
        assertNotNull(response.message());
    }

    @Test
    void includesContextInResponses() throws ExecutionException, InterruptedException {
        ChatRequest request = new ChatRequest("What is my top selling product?", null, null, null, null, null);

        ChatResponse response = orchestrator.processMessage(request).get();

        assertNotNull(response.context());
    }
}
