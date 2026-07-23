package com.sporekart.marketing.copilot.engine;

import com.sporekart.marketing.copilot.domain.MarketingContent;
import com.sporekart.marketing.copilot.domain.MarketingContent.ContentType;
import com.sporekart.marketing.copilot.domain.MarketingContent.ContentStatus;
import com.sporekart.marketing.copilot.dto.ContentGenerationRequest;
import com.sporekart.marketing.copilot.dto.ContentGenerationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ContentEngine {

    private static final Logger log = LoggerFactory.getLogger(ContentEngine.class);

    public ContentGenerationResponse generateContent(ContentGenerationRequest request) {
        log.info("Generating content for topic: {} of type: {}", request.topic(), request.contentType());
        var contentId = UUID.randomUUID().toString();
        var title = "[" + request.contentType() + "] " + request.topic();
        var body = generateBody(request);
        var status = ContentStatus.DRAFT;
        var seoRecs = List.of(
            "Include primary keyword in first 100 words",
            "Optimize meta description with " + (request.keywords() != null ? String.join(", ", request.keywords()) : "primary keywords"),
            "Add internal links to related content",
            "Use header tags (H1, H2, H3) for structure"
        );
        return new ContentGenerationResponse(contentId, title, body, request.contentType(), status.name(), seoRecs, LocalDateTime.now());
    }

    public MarketingContent createContentRecord(String title, String body, ContentType type, String audience, List<String> keywords, String locale) {
        log.debug("Creating content record: {}", title);
        return new MarketingContent(
            UUID.randomUUID().toString(), title, body, type, ContentStatus.DRAFT,
            audience, keywords, locale, null, "professional", LocalDateTime.now(),
            null, null, "system", null
        );
    }

    public List<String> suggestTopics(String audience, String segment) {
        log.info("Suggesting topics for audience: {} segment: {}", audience, segment);
        if ("home-growers".equalsIgnoreCase(audience)) {
            return List.of(
                "Beginner's Guide to Mushroom Cultivation",
                "Top 10 Mushroom Growing Kits for 2026",
                "Common Mistakes in Home Mushroom Farming"
            );
        } else if ("commercial-growers".equalsIgnoreCase(audience)) {
            return List.of(
                "Scaling Your Mushroom Farm: A Step-by-Step Guide",
                "Advanced Substrate Formulation Techniques",
                "ROI Analysis: Commercial Mushroom Production"
            );
        }
        return List.of("Mushroom Cultivation Trends 2026", "Exploring Exotic Mushroom Varieties");
    }

    public List<String> optimizeSEO(String content, List<String> keywords) {
        log.debug("Optimizing content for {} keywords", keywords != null ? keywords.size() : 0);
        return List.of(
            "Keyword density for '" + (keywords != null && !keywords.isEmpty() ? keywords.get(0) : "primary") + "' is optimal",
            "Add alt text to images",
            "Improve page load speed",
            "Add schema markup for articles"
        );
    }

    private String generateBody(ContentGenerationRequest request) {
        return "Generated content for " + request.topic() + " targeting " + (request.audience() != null ? request.audience() : "general audience")
            + ". This " + request.contentType() + " covers key aspects of " + request.topic()
            + " with a " + (request.tone() != null ? request.tone() : "professional") + " tone.";
    }

    public ContentGenerationResponse generateCampaignContent(String campaignId, String topic, ContentType contentType, String audience) {
        var request = new ContentGenerationRequest(contentType.name(), topic, audience, "professional", "en_IN", List.of(topic.toLowerCase().replace(" ", "-")), 500, campaignId, "professional");
        return generateContent(request);
    }
}
