package com.sporekart.marketing.copilot.engine;

import com.sporekart.marketing.copilot.domain.AEOGEOResult;
import com.sporekart.marketing.copilot.dto.AEOGEORequest;
import com.sporekart.marketing.copilot.dto.AEOGEOResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AEOGEOEngine {

    private static final Logger log = LoggerFactory.getLogger(AEOGEOEngine.class);

    public AEOGEOResponse analyzeAEOGEO(AEOGEORequest request) {
        log.info("Analyzing AEO/GEO for query: {}", request.query());
        var answerScore = calculateAnswerAppearanceScore(request.content(), request.query());
        var genSnippetScore = calculateGenerativeSnippetScore(request.content(), request.query());
        var snippetOpps = List.of(
            "What is " + request.query() + "?",
            "How to " + request.query().toLowerCase() + "?",
            request.query() + " benefits"
        );
        var peopleAlsoAsk = List.of(
            "Why is " + request.query() + " important?",
            "When to use " + request.query().toLowerCase() + "?",
            request.query() + " vs alternatives"
        );
        var optimized = generateOptimizedSnippet(request.query());
        var tips = List.of(
            "Structure content with clear Q&A format",
            "Use list and table formats for easy extraction",
            "Include FAQ schema markup",
            "Write concise, authoritative answers (40-50 words)",
            "Use natural language patterns matching voice search queries"
        );
        return new AEOGEOResponse(request.query(), answerScore, genSnippetScore, snippetOpps, peopleAlsoAsk,
            optimized, tips);
    }

    public AEOGEOResult analyzeQuery(String query, String content, String locale) {
        log.debug("Analyzing AEO/GEO for query: {} locale: {}", query, locale);
        var answerScore = calculateAnswerAppearanceScore(content, query);
        var genSnippetScore = calculateGenerativeSnippetScore(content, query);
        var snippetOpps = List.of("What is " + query + "?", "How to " + query.toLowerCase() + "?");
        var peopleAlsoAsk = List.of("Why is " + query + " important?");
        var schemaTypes = List.of("FAQPage", "HowTo", "Article", "Product");
        var optimized = generateOptimizedSnippet(query);
        var tips = List.of("Use clear Q&A format", "Add FAQ schema", "Write concise answers");
        return new AEOGEOResult(query, answerScore, genSnippetScore, snippetOpps, peopleAlsoAsk,
            schemaTypes, optimized, tips, locale != null ? locale : "en_IN");
    }

    public double calculateAnswerAppearanceScore(String content, String query) {
        if (content == null || content.isBlank()) return 0.0;
        var contentLower = content.toLowerCase();
        var queryTerms = query.toLowerCase().split(" ");
        long matchCount = 0;
        for (var term : queryTerms) {
            if (contentLower.contains(term)) matchCount++;
        }
        return Math.min(100.0, (double) matchCount / queryTerms.length * 100.0);
    }

    public double calculateGenerativeSnippetScore(String content, String query) {
        if (content == null || content.isBlank()) return 0.0;
        var hasLists = content.contains("1.") || content.contains("- ") || content.contains("* ");
        var hasTables = content.contains("|");
        var hasQA = content.contains("?") && content.contains("answer") || content.contains("Answer");
        var score = 30.0;
        if (hasLists) score += 25.0;
        if (hasTables) score += 20.0;
        if (hasQA) score += 25.0;
        return Math.min(100.0, score);
    }

    public String generateOptimizedSnippet(String query) {
        return query + " is a key aspect of modern mushroom cultivation that "
            + "involves careful planning and execution. To get started with "
            + query.toLowerCase() + ", you need to understand the fundamental "
            + "principles and best practices. Our comprehensive guide covers "
            + "everything from basics to advanced techniques.";
    }

    public List<String> getSchemaSuggestions(String contentType) {
        return switch (contentType.toLowerCase()) {
            case "recipe" -> List.of("Recipe", "HowTo", "NutritionInfo");
            case "product" -> List.of("Product", "Offer", "Review");
            case "article" -> List.of("Article", "NewsArticle", "BlogPosting");
            case "faq" -> List.of("FAQPage", "QAPage");
            case "video" -> List.of("VideoObject", "MediaObject");
            case "event" -> List.of("Event", "BusinessEvent");
            default -> List.of("Article", "WebPage");
        };
    }
}
