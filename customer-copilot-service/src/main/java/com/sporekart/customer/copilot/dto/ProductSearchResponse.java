package com.sporekart.customer.copilot.dto;

import com.sporekart.customer.copilot.domain.ProductItem;

import java.util.List;

public record ProductSearchResponse(
    List<ProductItem> products,
    int totalResults,
    int page,
    int size
) {
    public ProductSearchResponse {
        if (products == null) {
            products = List.of();
        }
        if (page < 0) {
            page = 0;
        }
        if (size <= 0) {
            size = 20;
        }
    }
}
