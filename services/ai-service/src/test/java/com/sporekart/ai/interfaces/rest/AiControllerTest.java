package com.sporekart.ai.interfaces.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldListProviders() throws Exception {
        mockMvc.perform(get("/ai/providers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("GEMINI"));
    }

    @Test
    void shouldCreatePrompt() throws Exception {
        mockMvc.perform(post("/ai/prompts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"category\":\"support\",\"name\":\"reply\",\"template\":\"hello\",\"version\":\"v1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("reply"));
    }

    @Test
    void shouldUploadKnowledgeDocument() throws Exception {
        mockMvc.perform(post("/ai/knowledge")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"FAQ\",\"category\":\"support\",\"content\":\"hello\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("FAQ"));
    }

    @Test
    void shouldHandleChatRequest() throws Exception {
        mockMvc.perform(post("/ai/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"prompt\":\"How do I return my order?\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").exists());
    }

    @Test
    void shouldSearchKnowledge() throws Exception {
        mockMvc.perform(post("/ai/knowledge")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Returns\",\"category\":\"support\",\"content\":\"Returns are easy\"}"));

        mockMvc.perform(post("/ai/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"query\":\"returns\",\"category\":\"support\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Returns"));
    }

    @Test
    void shouldRegisterVendor() throws Exception {
        mockMvc.perform(post("/vendors/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Vendor One\",\"gstNumber\":\"GST123\",\"bankAccount\":\"BANK001\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Vendor One"));
    }

    @Test
    void shouldRegisterDealer() throws Exception {
        mockMvc.perform(post("/dealers/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Dealer One\",\"gstNumber\":\"GST123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Dealer One"));
    }
}
