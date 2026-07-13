package com.sporekart.ai.assistant.application;

import com.sporekart.ai.assistant.api.AssistantResponse;
import com.sporekart.ai.assistant.api.CopilotOrchestrator;
import com.sporekart.ai.assistant.domain.AssistantExecution;
import com.sporekart.ai.assistant.domain.AssistantIntent;
import com.sporekart.ai.assistant.domain.AssistantSession;
import com.sporekart.ai.assistant.domain.CopilotType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CopilotOrchestratorImpl implements CopilotOrchestrator {

    private static final Logger log = LoggerFactory.getLogger(CopilotOrchestratorImpl.class);

    private final Map<String, CopilotType> intentCopilotMap;

    public CopilotOrchestratorImpl() {
        intentCopilotMap = new ConcurrentHashMap<>();
        intentCopilotMap.put("product_search", CopilotType.PRODUCT);
        intentCopilotMap.put("customer_support", CopilotType.SUPPORT);
        intentCopilotMap.put("order_status", CopilotType.ORDER);
        intentCopilotMap.put("inventory_check", CopilotType.INVENTORY);
        intentCopilotMap.put("pricing_info", CopilotType.PRODUCT);
        intentCopilotMap.put("training_request", CopilotType.TRAINING);
        intentCopilotMap.put("grower_advisory", CopilotType.GROWER);
        intentCopilotMap.put("marketplace_listing", CopilotType.MARKETPLACE);
        intentCopilotMap.put("erp_sync", CopilotType.ERP);
        intentCopilotMap.put("analytics_report", CopilotType.ANALYTICS);
        intentCopilotMap.put("account_admin", CopilotType.ADMINISTRATION);
        intentCopilotMap.put("notification_pref", CopilotType.NOTIFICATION);
    }

    @Override
    public AssistantResponse handleRequest(AssistantSession session, String userInput, AssistantIntent intent) {
        if (session == null || intent == null) {
            log.warn("Cannot handle request with null session or intent");
            return buildErrorResponse(session, "Invalid request: missing session or intent");
        }

        CopilotType copilotType = resolveCopilot(intent.resolvedIntent());
        String responseMessage = invokeCopilotStub(copilotType, userInput);

        log.info("Copilot {} handled request for session {}", copilotType, session.id());

        return new AssistantResponse(
                session.id(),
                responseMessage,
                intent.resolvedIntent(),
                intent.confidence(),
                Collections.emptyList(),
                Map.of("copilot", copilotType.name(), "sessionId", session.id().toString()),
                false,
                OffsetDateTime.now()
        );
    }

    @Override
    public CopilotType resolveCopilot(String intent) {
        return intentCopilotMap.getOrDefault(intent, CopilotType.SUPPORT);
    }

    @Override
    public AssistantExecution executeAction(CopilotType copilot, String action, Map<String, Object> params) {
        String response = invokeCopilotStub(copilot, action);
        long latency = (long) (Math.random() * 1000) + 100;

        return new AssistantExecution(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                null,
                copilot,
                action,
                params,
                Map.of("response", response, "latencyMs", latency),
                true,
                latency,
                null,
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );
    }

    private String invokeCopilotStub(CopilotType copilot, String input) {
        log.debug("Invoking copilot {} with input '{}'", copilot, input);
        return switch (copilot) {
            case CUSTOMER -> handleCustomerCopilot(input);
            case PRODUCT -> handleProductCopilot(input);
            case TRAINING -> handleTrainingCopilot(input);
            case GROWER -> handleGrowerCopilot(input);
            case MARKETPLACE -> handleMarketplaceCopilot(input);
            case ERP -> handleErpCopilot(input);
            case INVENTORY -> handleInventoryCopilot(input);
            case ORDER -> handleOrderCopilot(input);
            case ANALYTICS -> handleAnalyticsCopilot(input);
            case SUPPORT -> handleSupportCopilot(input);
            case ADMINISTRATION -> handleAdministrationCopilot(input);
            case NOTIFICATION -> handleNotificationCopilot(input);
        };
    }

    private String handleCustomerCopilot(String input) {
        return "Customer copilot processed: " + input;
    }

    private String handleProductCopilot(String input) {
        return "Product copilot processed: " + input;
    }

    private String handleTrainingCopilot(String input) {
        return "Training copilot processed: " + input;
    }

    private String handleGrowerCopilot(String input) {
        return "Grower copilot processed: " + input;
    }

    private String handleMarketplaceCopilot(String input) {
        return "Marketplace copilot processed: " + input;
    }

    private String handleErpCopilot(String input) {
        return "ERP copilot processed: " + input;
    }

    private String handleInventoryCopilot(String input) {
        return "Inventory copilot processed: " + input;
    }

    private String handleOrderCopilot(String input) {
        return "Order copilot processed: " + input;
    }

    private String handleAnalyticsCopilot(String input) {
        return "Analytics copilot processed: " + input;
    }

    private String handleSupportCopilot(String input) {
        return "Support copilot processed: " + input;
    }

    private String handleAdministrationCopilot(String input) {
        return "Administration copilot processed: " + input;
    }

    private String handleNotificationCopilot(String input) {
        return "Notification copilot processed: " + input;
    }

    private AssistantResponse buildErrorResponse(AssistantSession session, String error) {
        UUID sessionId = session != null ? session.id() : UUID.randomUUID();
        return new AssistantResponse(
                sessionId,
                error,
                "error",
                0.0,
                Collections.emptyList(),
                Map.of("error", true),
                false,
                OffsetDateTime.now()
        );
    }
}
