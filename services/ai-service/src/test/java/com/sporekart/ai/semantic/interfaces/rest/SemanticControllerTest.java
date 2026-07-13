package com.sporekart.ai.semantic.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.semantic.application.*;
import com.sporekart.ai.semantic.domain.SearchType;
import com.sporekart.ai.semantic.domain.SemanticSearchResult;
import com.sporekart.ai.semantic.infrastructure.SemanticRedisCacheService;
import com.sporekart.ai.semantic.infrastructure.persistence.SemanticVectorIndexEntity;
import com.sporekart.ai.semantic.interfaces.rest.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SemanticControllerTest {

    @Mock private SemanticEmbeddingService embeddingService;
    @Mock private SemanticSearchServiceImpl searchService;
    @Mock private SemanticIndexService indexService;
    @Mock private SemanticEmbeddingBatchService batchService;
    @Mock private SemanticRedisCacheService cacheService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        SemanticController controller = new SemanticController(embeddingService, searchService, indexService, batchService, cacheService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testEmbedEndpoint() throws Exception {
        var entity = new com.sporekart.ai.semantic.infrastructure.persistence.SemanticEmbeddingEntity();
        entity.setId(UUID.randomUUID());
        entity.setStatus("PENDING");
        entity.setDimensions(1536);
        entity.setProvider("OPENAI");
        entity.setModel("text-embedding-ada-002");
        entity.setCreatedAt(OffsetDateTime.now());
        when(embeddingService.createEmbedding(anyString(), any(), anyString(), anyString(), anyInt(), anyString(), any()))
                .thenReturn(entity);

        mockMvc.perform(post("/api/v1/semantic/embed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"test\",\"provider\":\"OPENAI\",\"model\":\"text-embedding-ada-002\"}")
                        .header("X-User-Id", UUID.randomUUID().toString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.status").value("PENDING"));
    }

    @Test
    void testSearchEndpoint() throws Exception {
        List<SemanticSearchResult> results = List.of(
                new SemanticSearchResult("1", "content", 0.9, 1, Map.of())
        );
        when(searchService.search(anyString(), any(SearchType.class), anyMap(), anyInt(), anyDouble()))
                .thenReturn(results);

        mockMvc.perform(post("/api/v1/semantic/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"query\":\"test\",\"type\":\"SEMANTIC\",\"filters\":{},\"limit\":10,\"threshold\":0.7}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalCount").value(1));
    }

    @Test
    void testSimilarityEndpoint() throws Exception {
        when(searchService.similaritySearch(anyString(), anyInt(), anyDouble()))
                .thenReturn(List.of());

        mockMvc.perform(post("/api/v1/semantic/similarity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"embeddingId\":\"emb-1\",\"limit\":10,\"threshold\":0.7,\"algorithm\":\"COSINE\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testListIndexes() throws Exception {
        SemanticVectorIndexEntity idx = new SemanticVectorIndexEntity();
        idx.setId(UUID.randomUUID());
        idx.setName("test-idx");
        idx.setStatus("ACTIVE");
        idx.setVectorCount(100);
        idx.setDimensions(768);
        idx.setCreatedAt(OffsetDateTime.now());
        when(indexService.listIndexes()).thenReturn(List.of(idx));

        mockMvc.perform(get("/api/v1/semantic/index"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("test-idx"));
    }

    @Test
    void testRebuildIndex() throws Exception {
        SemanticVectorIndexEntity idx = new SemanticVectorIndexEntity();
        idx.setId(UUID.randomUUID());
        idx.setName("test-idx");
        idx.setStatus("REBUILDING");
        idx.setVectorCount(0);
        idx.setDimensions(768);
        idx.setCreatedAt(OffsetDateTime.now());
        when(indexService.getIndexByName("test-idx")).thenReturn(idx);

        mockMvc.perform(post("/api/v1/semantic/index/rebuild?name=test-idx"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("REBUILDING"));
    }

    @Test
    void testStatistics() throws Exception {
        when(indexService.getIndexStatistics("default")).thenReturn(Map.of("vector_count", 100.0));

        mockMvc.perform(get("/api/v1/semantic/statistics?indexName=default"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.statistics.vector_count").value(100.0));
    }

    @Test
    void testHealthEndpoint() throws Exception {
        when(embeddingService.listEmbeddings()).thenReturn(List.of());
        when(indexService.listIndexes()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/semantic/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("UP"));
    }

    @Test
    void testEmbedReturns400ForInvalid() throws Exception {
        mockMvc.perform(post("/api/v1/semantic/embed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRebuildIndexReturns404WhenNotFound() throws Exception {
        doThrow(new IndexException("Index not found: unknown")).when(indexService).rebuildIndex("unknown");
        SemanticController controller = new SemanticController(embeddingService, searchService, indexService, batchService, cacheService);
        MockMvc localMockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        localMockMvc.perform(post("/api/v1/semantic/index/rebuild?name=unknown"))
                .andExpect(status().isNotFound());
    }
}
