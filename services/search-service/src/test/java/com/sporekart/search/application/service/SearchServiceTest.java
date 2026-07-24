package com.sporekart.search.application.service;

import com.sporekart.search.application.dto.IndexDocumentRequest;
import com.sporekart.search.application.dto.SearchRequest;
import com.sporekart.search.application.dto.SearchResponse;
import com.sporekart.search.common.exception.DocumentNotFoundException;
import com.sporekart.search.domain.model.SearchDocument;
import com.sporekart.search.domain.model.SearchQuery;
import com.sporekart.search.domain.model.SearchResult;
import com.sporekart.search.domain.repository.SearchRepositoryPort;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SearchServiceTest {

    private final SearchRepositoryPort repositoryPort = Mockito.mock(SearchRepositoryPort.class);
    private final SearchService service = new SearchService(repositoryPort);

    @Test
    void indexDocumentPersistsAndReturnsDocument() {
        when(repositoryPort.index(any())).thenAnswer(invocation -> invocation.getArgument(0));
        IndexDocumentRequest request = new IndexDocumentRequest(
                "PRODUCT", "p1", "Mushroom", "Fresh", "content", List.of("organic"), Map.of());

        SearchDocument result = service.indexDocument(request);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("PRODUCT", result.getEntityType());
        assertEquals("Mushroom", result.getTitle());
        verify(repositoryPort).index(any());
    }

    @Test
    void searchWithBlankQueryReturnsEmpty() {
        SearchResponse response = service.search(
                new SearchRequest("", null, 0, 20, null, null));

        assertEquals(0, response.totalResults());
        assertEquals(0, response.documents().size());
    }

    @Test
    void searchWithValidQueryDelegatesToRepository() {
        SearchResult mockResult = new SearchResult(
                List.of(), 0, 0, 20, 0);
        when(repositoryPort.search(any())).thenReturn(mockResult);

        SearchResponse response = service.search(
                new SearchRequest("mushroom", null, 0, 20, null, null));

        assertNotNull(response);
        verify(repositoryPort).search(any(SearchQuery.class));
    }

    @Test
    void deleteDocumentThrowsWhenNotFound() {
        when(repositoryPort.findById("unknown")).thenReturn(Optional.empty());

        assertThrows(DocumentNotFoundException.class, () -> service.deleteDocument("unknown"));
    }

    @Test
    void deleteDocumentDeletesWhenFound() {
        SearchDocument doc = new SearchDocument("1", "PRODUCT", "p1", "Mushroom", "Fresh",
                null, List.of(), Map.of(), 0.0, Instant.now(), Instant.now());
        when(repositoryPort.findById("1")).thenReturn(Optional.of(doc));

        service.deleteDocument("1");

        verify(repositoryPort).deleteById("1");
    }

    @Test
    void getDocumentThrowsWhenNotFound() {
        when(repositoryPort.findById("unknown")).thenReturn(Optional.empty());

        assertThrows(DocumentNotFoundException.class, () -> service.getDocument("unknown"));
    }

    @Test
    void getDocumentReturnsWhenFound() {
        SearchDocument doc = new SearchDocument("1", "PRODUCT", "p1", "Mushroom", "Fresh",
                null, List.of(), Map.of(), 0.0, Instant.now(), Instant.now());
        when(repositoryPort.findById("1")).thenReturn(Optional.of(doc));

        SearchDocument result = service.getDocument("1");

        assertEquals("Mushroom", result.getTitle());
    }
}