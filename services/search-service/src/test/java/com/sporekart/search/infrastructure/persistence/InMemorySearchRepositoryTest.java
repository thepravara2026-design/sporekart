package com.sporekart.search.infrastructure.persistence;

import com.sporekart.search.domain.model.SearchDocument;
import com.sporekart.search.domain.model.SearchQuery;
import com.sporekart.search.domain.model.SearchResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemorySearchRepositoryTest {

    private InMemorySearchRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemorySearchRepository();
    }

    @Test
    void indexAndFindByIdRoundTrip() {
        SearchDocument doc = createDocument("1", "PRODUCT", "p1", "Mushroom", "Fresh mushroom");
        SearchDocument saved = repository.index(doc);

        assertTrue(repository.findById("1").isPresent());
        assertEquals("Mushroom", repository.findById("1").orElseThrow().getTitle());
    }

    @Test
    void bulkIndexStoresAllDocuments() {
        List<SearchDocument> docs = List.of(
                createDocument("1", "PRODUCT", "p1", "Mushroom", "Fresh"),
                createDocument("2", "PRODUCT", "p2", "Truffle", "Aroma"));
        repository.bulkIndex(docs);

        assertTrue(repository.findById("1").isPresent());
        assertTrue(repository.findById("2").isPresent());
    }

    @Test
    void searchMatchesByTitle() {
        repository.index(createDocument("1", "PRODUCT", "p1", "Mushroom", "Fresh"));
        repository.index(createDocument("2", "PRODUCT", "p2", "Truffle Oil", "Premium oil"));

        SearchResult result = repository.search(
                new SearchQuery("mushroom", null, 0, 20, "score", "desc"));

        assertEquals(1, result.totalResults());
        assertEquals("Mushroom", result.documents().get(0).getTitle());
    }

    @Test
    void searchMatchesByDescription() {
        repository.index(createDocument("1", "PRODUCT", "p1", "Mushroom", "Fresh forest mushroom"));
        repository.index(createDocument("2", "PRODUCT", "p2", "Truffle", "Aromatic truffle oil"));

        SearchResult result = repository.search(
                new SearchQuery("forest", null, 0, 20, "score", "desc"));

        assertEquals(1, result.totalResults());
    }

    @Test
    void searchMatchesByContent() {
        SearchDocument doc = new SearchDocument("1", "PRODUCT", "p1", "Mushroom", "Fresh",
                "Wild forest mushroom from himalayas", List.of("organic"), Map.of(), 0.0,
                Instant.now(), Instant.now());
        repository.index(doc);

        SearchResult result = repository.search(
                new SearchQuery("himalayas", null, 0, 20, "score", "desc"));

        assertEquals(1, result.totalResults());
    }

    @Test
    void searchMatchesByTags() {
        SearchDocument doc = new SearchDocument("1", "PRODUCT", "p1", "Mushroom", "Fresh",
                "content", List.of("organic", "vegan"), Map.of(), 0.0,
                Instant.now(), Instant.now());
        repository.index(doc);

        SearchResult result = repository.search(
                new SearchQuery("vegan", null, 0, 20, "score", "desc"));

        assertEquals(1, result.totalResults());
    }

    @Test
    void searchWithFilters() {
        repository.index(createDocument("1", "PRODUCT", "p1", "Mushroom", "Fresh"));
        repository.index(createDocument("2", "CATEGORY", "c1", "Vegetables", "Green"));

        SearchResult result = repository.search(
                new SearchQuery("fresh", Map.of("entityType", "PRODUCT"), 0, 20, "score", "desc"));

        assertEquals(1, result.totalResults());
        assertEquals("PRODUCT", result.documents().get(0).getEntityType());
    }

    @Test
    void emptyQueryReturnsNoResults() {
        repository.index(createDocument("1", "PRODUCT", "p1", "Mushroom", "Fresh"));

        SearchResult result = repository.search(
                new SearchQuery("", null, 0, 20, "score", "desc"));

        assertEquals(0, result.totalResults());
    }

    @Test
    void resultsOrderedByScoreDescending() {
        SearchDocument lowScore = new SearchDocument("1", "PRODUCT", "p1", "Mushroom",
                "Something about mushroom", "content", List.of(), Map.of(), 0.0,
                Instant.now(), Instant.now());
        SearchDocument highScore = new SearchDocument("2", "PRODUCT", "p2", "Mushroom Special",
                "Description", "content with mushroom", List.of("mushroom"), Map.of(), 0.0,
                Instant.now(), Instant.now());
        repository.index(lowScore);
        repository.index(highScore);

        SearchResult result = repository.search(
                new SearchQuery("mushroom", null, 0, 20, "score", "desc"));

        assertTrue(result.documents().get(0).getScore() >= result.documents().get(1).getScore());
    }

    @Test
    void paginationWorks() {
        for (int i = 0; i < 10; i++) {
            repository.index(createDocument("doc" + i, "PRODUCT", "p" + i, "Item " + i, "Description"));
        }

        SearchResult page1 = repository.search(
                new SearchQuery("item", null, 0, 3, "score", "desc"));
        assertEquals(3, page1.documents().size());
        assertEquals(10, page1.totalResults());
        assertEquals(4, page1.totalPages());

        SearchResult page2 = repository.search(
                new SearchQuery("item", null, 1, 3, "score", "desc"));
        assertEquals(3, page2.documents().size());
    }

    @Test
    void deleteByIdRemovesDocument() {
        repository.index(createDocument("1", "PRODUCT", "p1", "Mushroom", "Fresh"));
        assertTrue(repository.findById("1").isPresent());

        repository.deleteById("1");

        assertFalse(repository.findById("1").isPresent());
    }

    @Test
    void deleteByEntityRemovesDocuments() {
        repository.index(createDocument("1", "PRODUCT", "p1", "Mushroom", "Fresh"));
        repository.index(createDocument("2", "PRODUCT", "p1", "Truffle", "Aroma"));

        repository.deleteByEntity("PRODUCT", "p1");

        assertFalse(repository.findById("1").isPresent());
        assertFalse(repository.findById("2").isPresent());
    }

    @Test
    void clearIndexRemovesAll() {
        repository.index(createDocument("1", "PRODUCT", "p1", "Mushroom", "Fresh"));
        repository.index(createDocument("2", "CATEGORY", "c1", "Veggies", "Green"));

        repository.clearIndex();

        assertFalse(repository.findById("1").isPresent());
        assertFalse(repository.findById("2").isPresent());
    }

    private SearchDocument createDocument(String id, String entityType, String entityId, String title,
            String description) {
        return new SearchDocument(id, entityType, entityId, title, description, null, List.of(), Map.of(), 0.0,
                Instant.now(), Instant.now());
    }
}