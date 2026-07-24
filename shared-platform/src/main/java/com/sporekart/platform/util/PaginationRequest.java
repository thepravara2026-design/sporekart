package com.sporekart.platform.util;

public record PaginationRequest(int page, int size, String sort, String direction) {
    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_SIZE = 20;
    public static final int MAX_SIZE = 1000;

    public PaginationRequest {
        if (page < 0) page = DEFAULT_PAGE;
        if (size <= 0 || size > MAX_SIZE) size = DEFAULT_SIZE;
        if (sort == null) sort = "";
        if (direction == null || (!direction.equalsIgnoreCase("asc") && !direction.equalsIgnoreCase("desc"))) {
            direction = "asc";
        }
    }

    public int getOffset() {
        return page * size;
    }

    public boolean isAscending() {
        return "asc".equalsIgnoreCase(direction);
    }

    public static PaginationRequest of(int page, int size) {
        return new PaginationRequest(page, size, null, null);
    }

    public static PaginationRequest defaultRequest() {
        return new PaginationRequest(DEFAULT_PAGE, DEFAULT_SIZE, null, null);
    }
}
