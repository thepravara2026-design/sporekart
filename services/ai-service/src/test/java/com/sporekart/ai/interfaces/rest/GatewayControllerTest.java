package com.sporekart.ai.interfaces.rest;

import com.sporekart.ai.gateway.application.GatewayApplicationService;
import com.sporekart.ai.gateway.application.GatewayResponseBuilder;
import com.sporekart.ai.gateway.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GatewayControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnHealth() throws Exception {
        mockMvc.perform(get("/api/v1/ai/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("UP"));
    }

    @Test
    void shouldReturnStatus() throws Exception {
        mockMvc.perform(get("/api/v1/ai/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.active").isBoolean());
    }

    @Test
    void shouldReturnFeatures() throws Exception {
        mockMvc.perform(get("/api/v1/ai/features"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void shouldExecuteValidRequest() throws Exception {
        mockMvc.perform(post("/api/v1/ai/execute")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"prompt\":\"What is my order status?\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").exists())
                .andExpect(jsonPath("$.data.status").value("COMPLETED"));
    }

    @Test
    void shouldValidateValidRequest() throws Exception {
        mockMvc.perform(post("/api/v1/ai/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"prompt\":\"Hello\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void shouldRejectBlankPromptOnValidate() throws Exception {
        mockMvc.perform(post("/api/v1/ai/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"prompt\":\"\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("FAILED"));
    }

    @Test
    void shouldRejectBlankPromptOnExecute() throws Exception {
        mockMvc.perform(post("/api/v1/ai/execute")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"prompt\":\"\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("FAILED"));
    }
}
