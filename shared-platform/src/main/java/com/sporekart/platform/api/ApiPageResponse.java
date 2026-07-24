package com.sporekart.platform.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiPageResponse<T>(
        boolean success,
        String message,
        List<T> data,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last,
        Instant timestamp
) {
    public static <T> ApiPageResponse<T> of(List<T> data, int page, int size, long totalElements) {
        int totalPages = size > 0 ? (int) Math.ceil((double) totalElements / size) : 0;
        return new ApiPageResponse<>(
                true, "Success", data, page, size, totalElements,
                totalPages, page == 0, page >= totalPages - 1, Instant.now());
    }
}
