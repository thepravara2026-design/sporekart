package com.sporekart.workspace.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.workspace.dto.ChatRequest;
import com.sporekart.workspace.dto.HandoffRequest;
import com.sporekart.workspace.dto.SwitchCopilotRequest;
import com.sporekart.workspace.service.WorkspaceOrchestrator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WorkspaceController.class)
class WorkspaceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private WorkspaceOrchestrator orchestrator;

    @Test
    void chat_ShouldReturn200() throws Exception {
        ChatRequest request = new ChatRequest("hello", "sess-1", "ws-1", null, null, null, false);
        when(orchestrator.processMessage(any())).thenReturn(null);

        mockMvc.perform(post("/api/v1/workspace/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void stream_ShouldReturnSseEmitter() throws Exception {
        ChatRequest request = new ChatRequest("hello", "sess-1", "ws-1", null, null, null, true);
        when(orchestrator.processMessage(any())).thenReturn(null);

        mockMvc.perform(post("/api/v1/workspace/stream")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void getCopilots_ShouldReturn200() throws Exception {
        mockMvc.perform(get("/api/v1/workspace/copilots")
                        .param("workspaceId", "ws-1"))
                .andExpect(status().isOk());
    }

    @Test
    void switchCopilot_ShouldReturn200() throws Exception {
        SwitchCopilotRequest request = new SwitchCopilotRequest("sess-1", "copilot-2", "user request", true);

        mockMvc.perform(post("/api/v1/workspace/switch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void getContext_ShouldReturn200() throws Exception {
        mockMvc.perform(get("/api/v1/workspace/context")
                        .param("sessionId", "sess-1"))
                .andExpect(status().isOk());
    }

    @Test
    void handoff_ShouldReturn200() throws Exception {
        HandoffRequest request = new HandoffRequest("sess-1", "copilot-1", "copilot-2", "needs expertise", "context summary");

        mockMvc.perform(post("/api/v1/workspace/handoff")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void getHistory_ShouldReturn200() throws Exception {
        mockMvc.perform(get("/api/v1/workspace/history")
                        .param("sessionId", "sess-1")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk());
    }

    @Test
    void getStatus_ShouldReturn200() throws Exception {
        mockMvc.perform(get("/api/v1/workspace/status")
                        .param("workspaceId", "ws-1"))
                .andExpect(status().isOk());
    }

    @Test
    void health_ShouldReturn200() throws Exception {
        mockMvc.perform(get("/api/v1/workspace/health"))
                .andExpect(status().isOk());
    }
}
