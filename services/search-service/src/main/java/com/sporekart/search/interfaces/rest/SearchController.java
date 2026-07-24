package com.sporekart.search.interfaces.rest;

import com.sporekart.search.application.dto.IndexDocumentRequest;
import com.sporekart.search.application.dto.SearchRequest;
import com.sporekart.search.application.dto.SearchResponse;
import com.sporekart.search.application.service.SearchService;
import com.sporekart.search.domain.model.SearchDocument;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping("/index")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SearchDocument> indexDocument(@Valid @RequestBody IndexDocumentRequest request) {
        SearchDocument document = searchService.indexDocument(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(document);
    }

    @PostMapping("/query")
    public ResponseEntity<SearchResponse> search(@Valid @RequestBody SearchRequest request) {
        return ResponseEntity.ok(searchService.search(request));
    }

    @GetMapping("/documents/{id}")
    public ResponseEntity<SearchDocument> getDocument(@PathVariable String id) {
        return ResponseEntity.ok(searchService.getDocument(id));
    }

    @DeleteMapping("/documents/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDocument(@PathVariable String id) {
        searchService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/reindex/{entityType}/{entityId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SearchDocument> reindexEntity(@PathVariable String entityType,
            @PathVariable String entityId, @Valid @RequestBody IndexDocumentRequest request) {
        SearchDocument document = searchService.reindexEntity(entityType, entityId, request);
        return ResponseEntity.ok(document);
    }
}