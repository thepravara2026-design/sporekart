package com.sporekart.ai.promptregistry.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.promptregistry.api.PromptOptimizationService;
import com.sporekart.ai.promptregistry.api.PromptRegistryService;
import com.sporekart.ai.promptregistry.api.PromptVersionService;
import com.sporekart.ai.promptregistry.domain.PromptRegistryEntry;
import com.sporekart.ai.promptregistry.domain.PromptStatus;
import com.sporekart.ai.promptregistry.interfaces.rest.dto.PromptRegistryRequestDto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PromptRegistryController.class)
class PromptRegistryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PromptRegistryService registryService;

    @MockitoBean
    private PromptOptimizationService optimizationService;

    @MockitoBean
    private PromptVersionService versionService;

    @Test
    void registerReturns200() throws Exception {
        PromptRegistryEntry entry = new PromptRegistryEntry();
        entry.setPromptId("p1");
        entry.setPromptName("Greeting");
        when(registryService.registerPrompt(any())).thenReturn(entry);

        PromptRegistryRequestDto request = new PromptRegistryRequestDto();
        request.setPromptId("p1");
        request.setPromptName("Greeting");
        request.setPromptText("Hello");
        request.setStatus(PromptStatus.DRAFT);

        mockMvc.perform(post("/api/v1/prompt-registry/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void listReturns200() throws Exception {
        PromptRegistryEntry entry = new PromptRegistryEntry();
        entry.setPromptId("p1");
        entry.setPromptName("Greeting");
        when(registryService.listPrompts()).thenReturn(List.of(entry));

        mockMvc.perform(get("/api/v1/prompt-registry/list"))
                .andExpect(status().isOk());
    }
}
