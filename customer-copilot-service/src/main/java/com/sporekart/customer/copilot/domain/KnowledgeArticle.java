package com.sporekart.customer.copilot.domain;

public record KnowledgeArticle(
    String id,
    String title,
    String snippet,
    String content,
    String category,
    String source,
    double relevanceScore,
    String url
) {
    public KnowledgeArticle {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id must not be blank");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title must not be blank");
        }
        if (snippet == null) {
            snippet = "";
        }
        if (content == null) {
            content = "";
        }
        if (category == null || category.isBlank()) {
            category = "General";
        }
        if (source == null) {
            source = "SporeKart KB";
        }
        if (url == null) {
            url = "";
        }
    }
}
