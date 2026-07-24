package com.sporekart.platform.util;

import java.util.List;

public record PaginationResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {
    public static <T> PaginationResponse<T> of(List<T> content, int page, int size, long totalElements) {
        int totalPages = size > 0 ? (int) Math.ceil((double) totalElements / size) : 0;
        return new PaginationResponse<>(
                content, page, size, totalElements,
                totalPages, page == 0, page >= totalPages - 1);
    }

    public static <T> PaginationResponse<T> empty() {
        return new PaginationResponse<>(List.of(), 0, 20, 0, 0, true, true);
    }
}
