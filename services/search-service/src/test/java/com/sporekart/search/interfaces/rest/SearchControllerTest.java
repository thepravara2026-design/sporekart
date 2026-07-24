package com.sporekart.search.interfaces.rest;

import com.sporekart.search.application.dto.IndexDocumentRequest;
import com.sporekart.search.application.dto.SearchRequest;
import com.sporekart.search.application.service.SearchService;
import com.sporekart.search.common.exception.DocumentNotFoundException;
import com.sporekart.search.domain.model.SearchDocument;
import com.sporekart.search.domain.model.SearchResult;
import com.sporekart.search.infrastructure.persistence.InMemorySearchRepository;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SearchControllerTest {

    private final InMemorySearchRepository repository = new InMemorySearchRepository();
    private final SearchService searchService = new SearchService(repository);
    private final SearchController controller = new SearchController(searchService);

    @Test
    void indexDocumentReturnsCreated() {
        var request = new IndexDocumentRequest("PRODUCT", "p1", "Mushroom", "Fresh", "content",
                List.of("organic"), Map.of());

        ResponseEntity<SearchDocument> response = controller.indexDocument(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
    }

    @Test
    void searchReturnsResults() {
        var indexReq = new IndexDocumentRequest("PRODUCT", "p1", "Mushroom", "Fresh mushroom", "content",
                List.of("organic"), Map.of());
        controller.indexDocument(indexReq);

        var searchReq = new SearchRequest("mushroom", null, 0, 20, null, null);
        ResponseEntity<com.sporekart.search.application.dto.SearchResponse> response = controller.search(searchReq);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().totalResults());
    }

    @Test
    void searchWithBlankQueryReturnsEmpty() {
        var indexReq = new IndexDocumentRequest("PRODUCT", "p1", "Mushroom", "Fresh", "content",
                List.of(), Map.of());
        controller.indexDocument(indexReq);

        var searchReq = new SearchRequest("", null, 0, 20, null, null);
        ResponseEntity<com.sporekart.search.application.dto.SearchResponse> response = controller.search(searchReq);

        assertEquals(0, response.getBody().totalResults());
    }

    @Test
    void getDocumentReturnsDocument() {
        var indexReq = new IndexDocumentRequest("PRODUCT", "p1", "Mushroom", "Fresh", "content",
                List.of(), Map.of());
        SearchDocument indexed = controller.indexDocument(indexReq).getBody();

        ResponseEntity<SearchDocument> response = controller.getDocument(indexed.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Mushroom", response.getBody().getTitle());
    }

    @Test
    void getDocumentThrowsWhenNotFound() {
        assertThrows(DocumentNotFoundException.class, () -> controller.getDocument("unknown"));
    }

    @Test
    void deleteDocumentReturnsNoContent() {
        var indexReq = new IndexDocumentRequest("PRODUCT", "p1", "Mushroom", "Fresh", "content",
                List.of(), Map.of());
        SearchDocument indexed = controller.indexDocument(indexReq).getBody();

        ResponseEntity<Void> response = controller.deleteDocument(indexed.getId());

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void reindexEntityReplacesDocument() {
        var indexReq = new IndexDocumentRequest("PRODUCT", "p1", "Mushroom", "Fresh", "content",
                List.of(), Map.of());
        controller.indexDocument(indexReq);

        var reindexReq = new IndexDocumentRequest("PRODUCT", "p1", "Truffle", "Aroma", "content",
                List.of(), Map.of());
        ResponseEntity<SearchDocument> response = controller.reindexEntity("PRODUCT", "p1", reindexReq);

        assertEquals("Truffle", response.getBody().getTitle());
    }
}