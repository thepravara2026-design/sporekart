package com.sporekart.customer.copilot.dto;

public record ProductSearchRequest(
    String query,
    String category,
    Double minPrice,
    Double maxPrice,
    String sortBy,
    int page,
    int size
) {
    public ProductSearchRequest {
        if (query == null) {
            query = "";
        }
        if (category == null) {
            category = "";
        }
        if (sortBy == null || sortBy.isBlank()) {
            sortBy = "relevance";
        }
        if (page < 0) {
            page = 0;
        }
        if (size <= 0) {
            size = 20;
        }
    }
}
