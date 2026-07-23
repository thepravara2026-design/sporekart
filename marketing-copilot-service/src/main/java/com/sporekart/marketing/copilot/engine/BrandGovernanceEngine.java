package com.sporekart.marketing.copilot.engine;

import com.sporekart.marketing.copilot.domain.BrandCheckResult;
import com.sporekart.marketing.copilot.domain.BrandCheckResult.BrandViolation;
import com.sporekart.marketing.copilot.dto.BrandCheckRequest;
import com.sporekart.marketing.copilot.dto.BrandCheckResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BrandGovernanceEngine {

    private static final Logger log = LoggerFactory.getLogger(BrandGovernanceEngine.class);

    private static final List<String> BANNED_TERMS = List.of("competitor", "cheap", "low-quality", "knockoff");
    private static final List<String> REQUIRED_PHRASES = List.of("SporeKart", "quality", "expertise");
    private static final List<String> BRAND_KEYWORDS = List.of("SporeKart", "sporekart", "mushroom", "cultivation",
        "quality", "sustainable", "organic", "expert", "innovative");

    public BrandCheckResponse checkContent(BrandCheckRequest request) {
        log.info("Checking brand consistency for content type: {}", request.contentType());
        var contentLower = request.content().toLowerCase();
        var violations = new ArrayList<String>();
        var suggestions = new ArrayList<String>();

        int brandWordCount = 0;
        for (var keyword : BRAND_KEYWORDS) {
            int count = countOccurrences(contentLower, keyword.toLowerCase());
            brandWordCount += count;
            if (count == 0 && !keyword.equals("sporekart")) {
                violations.add("Missing brand keyword: '" + keyword + "'");
            }
        }

        int bannedCount = 0;
        for (var banned : BANNED_TERMS) {
            if (contentLower.contains(banned)) {
                violations.add("Contains banned term: '" + banned + "'");
                bannedCount++;
            }
        }

        int requiredCount = 0;
        for (var required : REQUIRED_PHRASES) {
            if (contentLower.contains(required.toLowerCase())) {
                requiredCount++;
            }
        }
        if (requiredCount < REQUIRED_PHRASES.size()) {
            suggestions.add("Add more brand-aligned phrases like 'SporeKart quality' and 'expertise'");
        }

        if (brandWordCount < 3) {
            suggestions.add("Increase brand keyword density for better brand reinforcement");
        }

        var consistencyScore = calculateScore(bannedCount, brandWordCount, contentLower.length());
        var approved = consistencyScore >= 70.0;

        if (!approved) {
            suggestions.add("Review and revise content to better align with SporeKart brand guidelines");
        }

        return new BrandCheckResponse(Math.round(consistencyScore * 10.0) / 10.0, violations, suggestions, approved);
    }

    public BrandCheckResult performAudit(String contentId, String content, String brandVoice) {
        log.debug("Performing brand audit for content: {}", contentId);
        var contentLower = content.toLowerCase();
        var violations = new ArrayList<BrandViolation>();
        var suggestions = new ArrayList<String>();

        for (var keyword : BRAND_KEYWORDS) {
            if (!contentLower.contains(keyword.toLowerCase())) {
                violations.add(new BrandViolation("brand_keyword", "Include '" + keyword + "'", keyword, "medium"));
            }
        }
        for (var banned : BANNED_TERMS) {
            if (contentLower.contains(banned)) {
                violations.add(new BrandViolation("banned_term", "Remove '" + banned + "'", banned, "critical"));
            }
        }

        var score = calculateScore(violations.size(), BRAND_KEYWORDS.size(), contentLower.length());
        var approved = score >= 70.0;
        if (!approved) {
            suggestions.add("Align content with SporeKart brand voice: " + (brandVoice != null ? brandVoice : "professional"));
        }

        return new BrandCheckResult(contentId, content.substring(0, Math.min(50, content.length())),
            Math.round(score * 10.0) / 10.0, violations, suggestions, approved);
    }

    public List<String> getBrandGuidelines() {
        return List.of(
            "Always capitalize 'SporeKart' with 'S' and 'K'",
            "Maintain professional yet approachable tone",
            "Use data-driven claims where possible",
            "Emphasize quality, sustainability, and expertise",
            "Avoid negative comparisons with competitors",
            "Include clear call-to-action in all marketing content",
            "Use SporeKart brand colors: #2D5A27, #8BC34A, #FFFFFF",
            "Keep messaging consistent across all channels"
        );
    }

    public List<String> enforceConsistency(String content, String channel) {
        log.debug("Enforcing brand consistency for channel: {}", channel);
        var suggestions = new ArrayList<String>();
        var lower = content.toLowerCase();
        if (!lower.contains("sporekart")) {
            suggestions.add("Add 'SporeKart' brand mention");
        }
        if (channel.equals("social") && content.length() > 280) {
            suggestions.add("Shorten content for social media platform");
        }
        if (channel.equals("email") && !lower.contains("unsubscribe")) {
            suggestions.add("Add unsubscribe link for email compliance");
        }
        return suggestions;
    }

    private double calculateScore(int bannedTerms, int brandWordCount, int contentLength) {
        if (contentLength == 0) return 0.0;
        var score = 100.0;
        score -= bannedTerms * 25.0;
        if (brandWordCount < 3) score -= 15.0;
        if (contentLength < 50) score -= 10.0;
        return Math.max(0, Math.min(100, score));
    }

    private int countOccurrences(String text, String term) {
        int count = 0;
        int idx = 0;
        while ((idx = text.indexOf(term, idx)) != -1) {
            count++;
            idx += term.length();
        }
        return count;
    }
}
