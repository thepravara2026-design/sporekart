package com.sporekart.ai.domain.model;

public class SearchResult {
    private final String title;
    private final String snippet;
    private final double score;

    public SearchResult(String title, String snippet, double score) {
        this.title = title;
        this.snippet = snippet;
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public String getSnippet() {
        return snippet;
    }

    public double getScore() {
        return score;
    }
}
