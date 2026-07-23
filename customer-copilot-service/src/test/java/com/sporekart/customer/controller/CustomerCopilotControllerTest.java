package com.sporekart.customer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.copilot.PageContext;
import com.sporekart.copilot.Suggestion;
import com.sporekart.copilot.UserContext;
import com.sporekart.customer.copilot.controller.CustomerCopilotController;
import com.sporekart.customer.copilot.domain.ConversationMessage;
import com.sporekart.customer.copilot.dto.*;
import com.sporekart.customer.copilot.service.CustomerCopilotOrchestrator;
import com.sporekart.customer.copilot.service.CustomerContextService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CustomerCopilotControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CustomerCopilotOrchestrator orchestrator;

    @Mock
    private CustomerContextService contextService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        var controller = new CustomerCopilotController(orchestrator);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void chatWithValidMessageReturnsOk() throws Exception {
        var sessionId = UUID.randomUUID().toString();
        var chatResponse = new ChatResponse(sessionId, "How can I help you?",
            List.of(Suggestion.of("Search", "search")), Map.of("key", "value"), false);

        when(orchestrator.processMessage(anyString(), anyString(), any(UserContext.class), any(PageContext.class)))
            .thenReturn(chatResponse);

        var request = new ChatRequest("Hello", null, null, null, null, null, null);

        mockMvc.perform(post("/api/v1/copilot/customer/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.sessionId").isNotEmpty())
            .andExpect(jsonPath("$.message").value("How can I help you?"))
            .andExpect(jsonPath("$.suggestions").isArray())
            .andExpect(jsonPath("$.context").isMap())
            .andExpect(jsonPath("$.streaming").isBoolean());
    }

    @Test
    void chatWithEmptyMessageReturnsBadRequest() throws Exception {
        var request = new ChatRequest("", null, null, null, null, null, null);

        mockMvc.perform(post("/api/v1/copilot/customer/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void recommendWithCustomerIdReturnsOk() throws Exception {
        var request = new RecommendRequest("CUST-001", "Electronics", "browsing", 5, List.of());

        mockMvc.perform(post("/api/v1/copilot/customer/recommend")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.recommendations").isArray())
            .andExpect(jsonPath("$.recommendationType").isString())
            .andExpect(jsonPath("$.explanation").isString());
    }

    @Test
    void recommendWithMissingCustomerIdReturnsBadRequest() throws Exception {
        var request = new RecommendRequest("", null, null, 5, null);

        mockMvc.perform(post("/api/v1/copilot/customer/recommend")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void searchProductsWithQueryReturnsOk() throws Exception {
        var request = new ProductSearchRequest("keyboard", null, null, null, null, 0, 10);

        mockMvc.perform(post("/api/v1/copilot/customer/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.products").isArray())
            .andExpect(jsonPath("$.totalResults").isNumber())
            .andExpect(jsonPath("$.page").isNumber())
            .andExpect(jsonPath("$.size").isNumber());
    }

    @Test
    void searchProductsWithCategoryFilterReturnsOk() throws Exception {
        var request = new ProductSearchRequest("", "Furniture", null, null, null, 0, 20);

        mockMvc.perform(post("/api/v1/copilot/customer/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.products").isArray());
    }

    @Test
    void getHistoryForSessionReturnsOk() throws Exception {
        var sessionId = UUID.randomUUID().toString();
        when(orchestrator.getConversationHistory(sessionId)).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/copilot/customer/history")
                .param("sessionId", sessionId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getHistoryWithoutSessionReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/v1/copilot/customer/history")
                .param("sessionId", ""))
            .andExpect(status().isBadRequest());
    }

    @Test
    void getContextWithSessionIdReturnsOk() throws Exception {
        var sessionId = UUID.randomUUID().toString();
        var contextResponse = new ContextResponse(sessionId, "/page",
            Map.of("userId", "u1"), Map.of("items", List.of()), Map.of("recent", List.of()));

        when(orchestrator.getContextService()).thenReturn(contextService);
        when(contextService.assembleContext(anyString(), any(PageContext.class), any(UserContext.class)))
            .thenReturn(contextResponse);

        mockMvc.perform(post("/api/v1/copilot/customer/context")
                .param("sessionId", sessionId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.sessionId").value(sessionId))
            .andExpect(jsonPath("$.userInfo").isMap())
            .andExpect(jsonPath("$.cartInfo").isMap())
            .andExpect(jsonPath("$.recentActivity").isMap());
    }

    @Test
    void healthReturnsUp() throws Exception {
        when(orchestrator.getConversationHistory(anyString())).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/copilot/customer/health"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("UP"))
            .andExpect(jsonPath("$.service").value("customer-copilot-service"))
            .andExpect(jsonPath("$.version").value("0.1.0"))
            .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }

    @Test
    void searchProductsWithPriceRangeReturnsFiltered() throws Exception {
        var request = new ProductSearchRequest("", "", 100.0, 500.0, null, 0, 20);

        mockMvc.perform(post("/api/v1/copilot/customer/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.products").isArray());
    }

    @Test
    void searchProductsWithNoResultsReturnsEmptyList() throws Exception {
        var request = new ProductSearchRequest("zzzznonexistent", null, null, null, null, 0, 20);

        mockMvc.perform(post("/api/v1/copilot/customer/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.products").isEmpty())
            .andExpect(jsonPath("$.totalResults").value(0));
    }
}
