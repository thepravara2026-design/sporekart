package com.sporekart.ai.knowledge.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.knowledge.application.*;
import com.sporekart.ai.knowledge.infrastructure.KnowledgeKafkaEventPublisher;
import com.sporekart.ai.knowledge.infrastructure.KnowledgeRedisCacheService;
import com.sporekart.ai.knowledge.infrastructure.persistence.KnowledgeCategoryEntity;
import com.sporekart.ai.knowledge.infrastructure.persistence.KnowledgeDocumentEntity;
import com.sporekart.ai.knowledge.interfaces.rest.KnowledgeController;
import com.sporekart.ai.knowledge.interfaces.rest.dto.CreateDocumentRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class KnowledgeControllerTest {

    @Mock private KnowledgeDocumentService documentService;
    @Mock private KnowledgeChunkingService chunkingService;
    @Mock private KnowledgeMetadataService metadataService;
    @Mock private KnowledgeRetrievalService retrievalService;
    @Mock private KnowledgeSecurityService securityService;
    @Mock private KnowledgeRedisCacheService cacheService;
    @Mock private KnowledgeKafkaEventPublisher kafkaPublisher;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        KnowledgeController controller = new KnowledgeController(documentService, chunkingService,
                metadataService, retrievalService, securityService, cacheService, kafkaPublisher);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void shouldListDocuments() throws Exception {
        when(documentService.listDocuments()).thenReturn(List.of());
        mockMvc.perform(get("/api/v1/knowledge/documents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void shouldGetDocumentById() throws Exception {
        UUID id = UUID.randomUUID();
        KnowledgeDocumentEntity doc = new KnowledgeDocumentEntity();
        doc.setId(id);
        doc.setTitle("Test Doc");
        when(documentService.getDocument(id)).thenReturn(doc);
        mockMvc.perform(get("/api/v1/knowledge/documents/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void shouldReturn404WhenDocumentNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(documentService.getDocument(id)).thenThrow(new KnowledgeNotFoundException("not found"));
        mockMvc.perform(get("/api/v1/knowledge/documents/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateDocument() throws Exception {
        KnowledgeDocumentEntity doc = new KnowledgeDocumentEntity();
        doc.setId(UUID.randomUUID());
        doc.setTitle("New Doc");
        when(documentService.createDocument(any(), anyString(), any(), any(), any(), any(),
                any(), any(), any(), any(), any(), any())).thenReturn(doc);
        String json = objectMapper.writeValueAsString(new CreateDocumentRequest(
                UUID.randomUUID(), "New Doc", "desc", "content",
                "en", "author", "source", "INTERNAL", null, null, List.of()));
        mockMvc.perform(post("/api/v1/knowledge/documents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void shouldDeleteDocument() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(documentService).deleteDocument(any(), any());
        mockMvc.perform(delete("/api/v1/knowledge/documents/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void shouldListCategories() throws Exception {
        KnowledgeCategoryEntity cat = new KnowledgeCategoryEntity("FAQ", null, 0);
        cat.setId(UUID.randomUUID());
        when(metadataService.listCategories()).thenReturn(List.of(cat));
        mockMvc.perform(get("/api/v1/knowledge/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void shouldSearchDocuments() throws Exception {
        when(documentService.searchDocuments("test")).thenReturn(List.of());
        mockMvc.perform(get("/api/v1/knowledge/search").param("q", "test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void shouldHandleSecurityException() throws Exception {
        UUID id = UUID.randomUUID();
        when(documentService.getDocument(id)).thenThrow(new KnowledgeSecurityException("Access denied"));
        mockMvc.perform(get("/api/v1/knowledge/documents/" + id))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldHandleValidationException() throws Exception {
        UUID id = UUID.randomUUID();
        when(documentService.getDocument(id)).thenThrow(new KnowledgeValidationException("invalid"));
        mockMvc.perform(get("/api/v1/knowledge/documents/" + id))
                .andExpect(status().isBadRequest());
    }
}
