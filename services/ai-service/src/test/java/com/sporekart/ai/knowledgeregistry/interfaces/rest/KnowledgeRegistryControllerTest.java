package com.sporekart.ai.knowledgeregistry.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.knowledgeregistry.api.KnowledgeHealthService;
import com.sporekart.ai.knowledgeregistry.api.KnowledgeRegistryService;
import com.sporekart.ai.knowledgeregistry.api.KnowledgeSyncService;
import com.sporekart.ai.knowledgeregistry.domain.KnowledgeSourceEntry;
import com.sporekart.ai.knowledgeregistry.domain.KnowledgeSourceType;
import com.sporekart.ai.knowledgeregistry.interfaces.rest.dto.KnowledgeSourceRequestDto;

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

@WebMvcTest(KnowledgeRegistryController.class)
class KnowledgeRegistryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private KnowledgeRegistryService registryService;

    @MockitoBean
    private KnowledgeSyncService syncService;

    @MockitoBean
    private KnowledgeHealthService healthService;

    @Test
    void registerReturns201() throws Exception {
        KnowledgeSourceEntry entry = new KnowledgeSourceEntry();
        entry.setSourceId("s1");
        entry.setSourceName("Docs");
        when(registryService.registerSource(any())).thenReturn(entry);

        KnowledgeSourceRequestDto request = new KnowledgeSourceRequestDto();
        request.setSourceName("Docs");
        request.setSourceType(KnowledgeSourceType.DOCUMENT);

        mockMvc.perform(post("/api/v1/knowledge-registry/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void listReturns200() throws Exception {
        KnowledgeSourceEntry entry = new KnowledgeSourceEntry();
        entry.setSourceId("s1");
        entry.setSourceName("Docs");
        when(registryService.listSources()).thenReturn(List.of(entry));

        mockMvc.perform(get("/api/v1/knowledge-registry/list"))
                .andExpect(status().isOk());
    }
}
