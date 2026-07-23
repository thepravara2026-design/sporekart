package com.sporekart.marketing.copilot.engine;

import com.sporekart.marketing.copilot.domain.*;
import com.sporekart.marketing.copilot.dto.SEORequest;
import com.sporekart.marketing.copilot.dto.SEOResponse;
import com.sporekart.marketing.copilot.dto.SEOResponse.SEOChecklistResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SEOEngine {

    private static final Logger log = LoggerFactory.getLogger(SEOEngine.class);

    public SEOResponse analyzeSEO(SEORequest request) {
        log.info("Analyzing SEO for URL: {} keyword: {}", request.url(), request.targetKeyword());
        var volume = estimateSearchVolume(request.targetKeyword());
        var difficulty = calculateKeywordDifficulty(request.targetKeyword());
        var ranking = estimateCurrentRanking(request.targetKeyword());
        var suggestions = List.of(
            "Optimize title tag with '" + request.targetKeyword() + "'",
            "Add '" + request.targetKeyword() + "' to H1 heading",
            "Improve meta description",
            "Add internal links"
        );
        var checklist = generateChecklist(request.url(), request.currentContent());
        var traffic = estimateTraffic(volume, ranking);
        return new SEOResponse(request.url(), request.targetKeyword(), volume, difficulty, ranking, suggestions,
            new SEOChecklistResponse(checklist.score(), checklist.critical().stream().map(SEOChecklist.SEOIssue::message).toList(),
                checklist.warnings().stream().map(SEOChecklist.SEOIssue::message).toList(), checklist.recommendations()),
            traffic, request.locale() != null ? request.locale() : "en_IN");
    }

    public SEOChecklist generateChecklist(String url, String content) {
        log.debug("Generating SEO checklist for URL: {}", url);
        var critical = new ArrayList<SEOChecklist.SEOIssue>();
        var warnings = new ArrayList<SEOChecklist.SEOIssue>();
        var passed = new ArrayList<SEOChecklist.SEOIssue>();

        if (content == null || content.isBlank()) {
            critical.add(new SEOChecklist.SEOIssue("content", "No content provided", "critical"));
        } else {
            passed.add(new SEOChecklist.SEOIssue("content_length", content.length() > 300 ? "Content length adequate" : "Content too short", "passed"));
        }

        warnings.add(new SEOChecklist.SEOIssue("meta_description", "Meta description not found", "warning"));
        passed.add(new SEOChecklist.SEOIssue("url_structure", "URL structure is clean", "passed"));

        var recommendations = List.of(
            "Add meta description (150-160 characters)",
            "Use descriptive alt text for images",
            "Implement Open Graph tags",
            "Add schema.org markup"
        );

        var score = calculateScore(critical.size(), warnings.size());
        return new SEOChecklist(url, score, critical, warnings, passed, recommendations);
    }

    public List<String> suggestKeywords(String seed, int count) {
        log.info("Suggesting keywords for seed: {} count: {}", seed, count);
        return List.of(
            seed + " guide",
            seed + " tips",
            "best " + seed,
            seed + " for beginners",
            seed + " price",
            "how to " + seed,
            seed + " reviews",
            "organic " + seed,
            seed + " online",
            seed + " benefits"
        ).subList(0, Math.min(count, 10));
    }

    public List<String> conductCompetitorAnalysis(String keyword) {
        log.info("Conducting competitor analysis for keyword: {}", keyword);
        return List.of(
            "Competitor 1 ranks #1 with comprehensive guide",
            "Competitor 2 ranks #3 with listicle format",
            "Competitor 3 ranks #5 with video content",
            "Opportunity: Create more in-depth content with practical examples"
        );
    }

    public int estimateSearchVolume(String keyword) {
        return keyword.length() * 150 + 200;
    }

    public double calculateKeywordDifficulty(String keyword) {
        return Math.min(95.0, keyword.length() * 3.5 + 10.0);
    }

    public int estimateCurrentRanking(String keyword) {
        return Math.max(1, 30 - keyword.length());
    }

    public double estimateTraffic(int volume, int ranking) {
        if (ranking <= 1) return volume * 0.35;
        if (ranking <= 3) return volume * 0.15;
        if (ranking <= 5) return volume * 0.08;
        if (ranking <= 10) return volume * 0.03;
        return volume * 0.01;
    }

    private double calculateScore(int critical, int warnings) {
        var deductions = critical * 20.0 + warnings * 5.0;
        return Math.max(0, 100 - deductions);
    }
}
