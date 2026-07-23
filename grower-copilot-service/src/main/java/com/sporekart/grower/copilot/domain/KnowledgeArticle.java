package com.sporekart.grower.copilot.domain;

import java.util.List;

public record KnowledgeArticle(
    String articleId,
    String title,
    String content,
    String source,
    String type,
    List<String> tags,
    String citation,
    double relevanceScore,
    String url
) {}
