package com.sporekart.ai.assistant.application;

import com.sporekart.ai.assistant.domain.AssistantIntent;
import com.sporekart.ai.assistant.domain.AssistantSession;
import com.sporekart.ai.assistant.domain.AssistantStatus;
import com.sporekart.ai.assistant.domain.CopilotType;
import com.sporekart.ai.assistant.domain.IntentPriority;
import com.sporekart.ai.assistant.domain.IntentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CopilotOrchestratorImplTest {

    private CopilotOrchestratorImpl orchestrator;

    @BeforeEach
    void setUp() {
        orchestrator = new CopilotOrchestratorImpl();
    }

    private AssistantSession createSession() {
        return new AssistantSession(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                Map.of(),
                AssistantStatus.ACTIVE,
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                OffsetDateTime.now().plusHours(1)
        );
    }

    private AssistantIntent createIntent(String resolvedIntent) {
        return new AssistantIntent(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "input",
                resolvedIntent,
                0.9,
                IntentStatus.RESOLVED,
                IntentPriority.HIGH,
                Map.of(),
                List.of(),
                null,
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );
    }

    @Test
    void shouldResolveCopilotForProductSearch() {
        var copilot = orchestrator.resolveCopilot("product_search");
        assertEquals(CopilotType.PRODUCT, copilot);
    }

    @Test
    void shouldResolveCopilotForCustomerSupport() {
        var copilot = orchestrator.resolveCopilot("customer_support");
        assertEquals(CopilotType.SUPPORT, copilot);
    }

    @Test
    void shouldResolveCopilotForOrderStatus() {
        var copilot = orchestrator.resolveCopilot("order_status");
        assertEquals(CopilotType.ORDER, copilot);
    }

    @Test
    void shouldResolveCopilotForAnalytics() {
        var copilot = orchestrator.resolveCopilot("analytics_report");
        assertEquals(CopilotType.ANALYTICS, copilot);
    }

    @Test
    void shouldResolveDefaultCopilotForUnknownIntent() {
        var copilot = orchestrator.resolveCopilot("unknown_intent");
        assertEquals(CopilotType.SUPPORT, copilot);
    }

    @Test
    void shouldHandleRequestForProductCopilot() {
        var session = createSession();
        var intent = createIntent("product_search");
        var response = orchestrator.handleRequest(session, "search for product", intent);

        assertNotNull(response);
        assertTrue(response.message().contains("Product copilot processed"));
        assertEquals("product_search", response.intent());
        assertEquals(session.id(), response.sessionId());
    }

    @Test
    void shouldHandleRequestForSupportCopilot() {
        var session = createSession();
        var intent = createIntent("customer_support");
        var response = orchestrator.handleRequest(session, "need help", intent);

        assertTrue(response.message().contains("Support copilot processed"));
    }

    @Test
    void shouldHandleRequestForOrderCopilot() {
        var session = createSession();
        var intent = createIntent("order_status");
        var response = orchestrator.handleRequest(session, "track order", intent);

        assertTrue(response.message().contains("Order copilot processed"));
    }

    @Test
    void shouldHandleRequestForInventoryCopilot() {
        var session = createSession();
        var intent = createIntent("inventory_check");
        var response = orchestrator.handleRequest(session, "check stock", intent);

        assertTrue(response.message().contains("Inventory copilot processed"));
    }

    @Test
    void shouldHandleRequestForTrainingCopilot() {
        var session = createSession();
        var intent = createIntent("training_request");
        var response = orchestrator.handleRequest(session, "learn", intent);

        assertTrue(response.message().contains("Training copilot processed"));
    }

    @Test
    void shouldReturnErrorResponseForNullSession() {
        var intent = createIntent("product_search");
        var response = orchestrator.handleRequest(null, "input", intent);

        assertTrue(response.message().contains("Invalid request"));
    }

    @Test
    void shouldReturnErrorResponseForNullIntent() {
        var session = createSession();
        var response = orchestrator.handleRequest(session, "input", null);

        assertTrue(response.message().contains("Invalid request"));
    }

    @Test
    void shouldExecuteActionAndReturnExecution() {
        var execution = orchestrator.executeAction(CopilotType.PRODUCT, "search", Map.of("q", "product"));

        assertNotNull(execution);
        assertTrue(execution.success());
        assertEquals(CopilotType.PRODUCT, execution.copilotType());
        assertEquals("search", execution.action());
    }

    @Test
    void shouldReturnCopilotResponseInContext() {
        var session = createSession();
        var intent = createIntent("pricing_info");
        var response = orchestrator.handleRequest(session, "price", intent);

        assertTrue(response.context().containsKey("copilot"));
        assertEquals(CopilotType.PRODUCT.name(), response.context().get("copilot"));
    }
}
