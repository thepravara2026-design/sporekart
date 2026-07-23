package com.sporekart.customer.engine;

import com.sporekart.customer.copilot.domain.KnowledgeArticle;
import com.sporekart.customer.infrastructure.knowledge.KnowledgeClient;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class KnowledgeService {

    private final KnowledgeClient knowledgeClient;

    public KnowledgeService(KnowledgeClient knowledgeClient) {
        this.knowledgeClient = knowledgeClient;
    }

    public List<KnowledgeArticle> searchKnowledge(String query) {
        var results = knowledgeClient.searchKnowledge(query, null);
        return results.stream()
            .map(a -> new KnowledgeArticle(
                a.id(), a.title(), a.snippet(), a.content(),
                a.category(), a.source(), a.relevanceScore(), a.url()))
            .collect(Collectors.toList());
    }

    public List<KnowledgeArticle> searchKnowledge(String query, String category) {
        return knowledgeClient.searchKnowledge(query, category);
    }

    public List<KnowledgeArticle> getFAQs(String category) {
        return knowledgeClient.getFAQs(category);
    }

    public Map<String, Object> getGrowingAdvice(String mushroomType) {
        return knowledgeClient.getGrowingAdvice(mushroomType);
    }
}
