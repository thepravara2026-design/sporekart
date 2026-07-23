package com.sporekart.copilot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.copilot.capability.Capability;
import com.sporekart.copilot.context.UserContext;
import com.sporekart.copilot.domain.*;
import com.sporekart.copilot.dto.*;
import com.sporekart.copilot.persona.DefaultPersonas;
import com.sporekart.copilot.persona.Persona;
import com.sporekart.copilot.service.CopilotOrchestrationService;
import com.sporekart.copilot.service.CopilotRegistryService;
import com.sporekart.copilot.service.CopilotSessionService;
import com.sporekart.copilot.service.CopilotStreamingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CopilotChatController.class)
class CopilotChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CopilotOrchestrationService orchestrationService;

    @MockitoBean
    private CopilotRegistryService registryService;

    @MockitoBean
    private CopilotSessionService sessionService;

    @MockitoBean
    private CopilotStreamingService streamingService;

    private CopilotRegistration testRegistration;
    private CopilotSession testSession;

    @BeforeEach
    void setUp() {
        testRegistration = new CopilotRegistration(
            "test-1", "TestCopilot", CopilotType.CUSTOMER, "1.0.0",
            "Test copilot", DefaultPersonas.customerPersona(),
            List.of("cap1"), CopilotStatus.ACTIVE, Map.of(), OffsetDateTime.now()
        );

        var user = UserContext.builder().userId("user1").userName("User1").email("").build();
        testSession = new CopilotSession(
            new SessionId(), CopilotType.CUSTOMER, user, CopilotStatus.ACTIVE,
            OffsetDateTime.now(), OffsetDateTime.now()
        );
    }

    @Test
    void chatShouldReturnOk() throws Exception {
        var request = new ChatRequest(null, "Hello", null);
        var response = new ChatResponse("sess-1", "Hi there", List.of(), Map.of(), false);

        when(orchestrationService.processMessage(any(), any(), any())).thenReturn(response);

        mockMvc.perform(post("/api/copilot/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Hi there"));
    }

    @Test
    void chatShouldReturn400WhenMessageBlank() throws Exception {
        var request = new ChatRequest(null, "", null);

        mockMvc.perform(post("/api/copilot/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void listCopilotsShouldReturnOk() throws Exception {
        when(registryService.listCopilots()).thenReturn(List.of(testRegistration));

        mockMvc.perform(get("/api/copilot/list"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("TestCopilot"));
    }

    @Test
    void getCapabilitiesShouldReturnOk() throws Exception {
        var cap = new Capability("cap1", "Cap1", "Test capability", new CopilotType[]{CopilotType.CUSTOMER}, null, null);
        when(registryService.getCapabilities()).thenReturn(List.of(cap));

        mockMvc.perform(get("/api/copilot/capabilities"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value("cap1"));
    }

    @Test
    void getPersonasShouldReturnOk() throws Exception {
        when(registryService.getPersonas()).thenReturn(DefaultPersonas.all());

        mockMvc.perform(get("/api/copilot/personas"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value("customer-support"));
    }

    @Test
    void registerCopilotShouldReturnCreated() throws Exception {
        var request = new RegisterCopilotRequest("NewCopilot", "CUSTOMER", "1.0.0", "desc", null, List.of("cap1"));
        when(registryService.registerCopilot(any())).thenReturn(testRegistration);

        mockMvc.perform(post("/api/copilot/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("TestCopilot"));
    }

    @Test
    void getCopilotShouldReturnOk() throws Exception {
        when(registryService.getCopilot("test-1")).thenReturn(testRegistration);

        mockMvc.perform(get("/api/copilot/test-1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("TestCopilot"));
    }

    @Test
    void enableCopilotShouldReturnOk() throws Exception {
        mockMvc.perform(put("/api/copilot/test-1/enable"))
            .andExpect(status().isOk());
    }

    @Test
    void disableCopilotShouldReturnOk() throws Exception {
        mockMvc.perform(put("/api/copilot/test-1/disable"))
            .andExpect(status().isOk());
    }

    @Test
    void createSessionShouldReturnCreated() throws Exception {
        var request = new CreateSessionRequest("CUSTOMER", "user1", "User1", List.of("role1"));
        when(sessionService.createSession(any(), any())).thenReturn(testSession);

        mockMvc.perform(post("/api/copilot/session")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated());
    }

    @Test
    void getSessionShouldReturnOk() throws Exception {
        var sid = testSession.id();
        when(sessionService.getSession(any())).thenReturn(Optional.of(testSession));

        mockMvc.perform(get("/api/copilot/session/" + sid))
            .andExpect(status().isOk());
    }

    @Test
    void getSessionShouldReturn404WhenNotFound() throws Exception {
        when(sessionService.getSession(any())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/copilot/session/" + new SessionId()))
            .andExpect(status().isNotFound());
    }

    @Test
    void closeSessionShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/copilot/session/" + new SessionId()))
            .andExpect(status().isNoContent());
    }

    @Test
    void getCopilotHealthShouldReturnOk() throws Exception {
        when(registryService.getCopilotHealth("test-1")).thenReturn(testRegistration);

        mockMvc.perform(get("/api/copilot/test-1/health"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(CopilotStatus.ACTIVE.name()));
    }
}
