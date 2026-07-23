package com.sporekart.marketing.copilot.engine;

import com.sporekart.marketing.copilot.domain.SocialMediaPost;
import com.sporekart.marketing.copilot.domain.SocialMediaPost.PostType;
import com.sporekart.marketing.copilot.dto.SocialMediaRequest;
import com.sporekart.marketing.copilot.dto.SocialMediaResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class SocialMediaEngine {

    private static final Logger log = LoggerFactory.getLogger(SocialMediaEngine.class);

    private static final Map<String, List<String>> BEST_TIMES = Map.of(
        "instagram", List.of("9:00 AM", "11:00 AM", "2:00 PM", "7:00 PM"),
        "facebook", List.of("8:00 AM", "10:00 AM", "1:00 PM", "3:00 PM"),
        "linkedin", List.of("7:00 AM", "9:00 AM", "12:00 PM", "5:00 PM"),
        "youtube", List.of("10:00 AM", "2:00 PM", "7:00 PM", "9:00 PM"),
        "twitter", List.of("8:00 AM", "11:00 AM", "1:00 PM", "6:00 PM"),
        "pinterest", List.of("8:00 PM", "9:00 PM", "10:00 PM", "11:00 PM"),
        "threads", List.of("7:00 AM", "12:00 PM", "6:00 PM", "9:00 PM")
    );

    public SocialMediaResponse generatePost(SocialMediaRequest request) {
        log.info("Generating social media post for platform: {} topic: {}", request.platform(), request.topic());
        var postId = UUID.randomUUID().toString();
        var content = generateContent(request);
        var hashtags = generateHashtags(request.topic(), request.hashtags());
        var postType = request.postType() != null ? request.postType() : "IMAGE";
        var cta = request.callToAction() != null ? request.callToAction() : defaultCTA(request.platform());
        var bestTimes = BEST_TIMES.getOrDefault(request.platform().toLowerCase(), List.of("10:00 AM", "2:00 PM"));
        return new SocialMediaResponse(postId, request.platform(), content, hashtags, postType, cta,
            LocalDateTime.now().plusHours(2), bestTimes);
    }

    public SocialMediaPost createPost(String platform, String topic, String campaignId) {
        log.debug("Creating social media post record for: {} on {}", topic, platform);
        return new SocialMediaPost(UUID.randomUUID().toString(), platform,
            "Check out our latest on " + topic + "! #SporeKart #Mushrooms",
            List.of("#" + topic.toLowerCase().replace(" ", ""), "#SporeKart", "#Mushrooms"),
            List.of(), "Learn More", PostType.IMAGE, LocalDateTime.now().plusDays(1),
            campaignId, "SCHEDULED");
    }

    public List<String> generateHashtags(String topic, List<String> customTags) {
        log.debug("Generating hashtags for topic: {}", topic);
        var tags = new ArrayList<String>();
        tags.add("#" + topic.toLowerCase().replace(" ", ""));
        tags.add("#SporeKart");
        tags.add("#MushroomFarming");
        tags.add("#SustainableAgriculture");
        tags.add("#OrganicFarming");
        if (customTags != null) {
            tags.addAll(customTags.stream().map(t -> t.startsWith("#") ? t : "#" + t).toList());
        }
        return tags.stream().distinct().limit(10).toList();
    }

    public Map<String, Object> analyzeBestTimes(String platform) {
        var times = BEST_TIMES.getOrDefault(platform.toLowerCase(), List.of("10:00 AM"));
        return Map.of("platform", platform, "bestTimes", times,
            "recommendation", "Post during peak engagement hours for maximum reach");
    }

    public List<String> generateContentCalendar(String campaignId, LocalDateTime startDate, int days) {
        log.info("Generating content calendar for campaign: {} covering {} days", campaignId, days);
        var calendar = new ArrayList<String>();
        for (int i = 0; i < days; i++) {
            var date = startDate.plusDays(i);
            calendar.add("Day " + (i + 1) + " (" + date.toLocalDate() + "): " + getContentTheme(i));
        }
        return calendar;
    }

    private String generateContent(SocialMediaRequest request) {
        var tone = request.tone() != null ? request.tone() : "professional";
        return "Discover the amazing world of " + request.topic() + " with SporeKart! "
            + "Our " + tone + " approach ensures you get the best quality "
            + "products and knowledge. " + (request.callToAction() != null ? request.callToAction() : "Shop now!")
            + " #SporeKart #" + request.topic().toLowerCase().replace(" ", "");
    }

    private String defaultCTA(String platform) {
        return switch (platform.toLowerCase()) {
            case "instagram" -> "Link in bio!";
            case "facebook" -> "Learn more on our website!";
            case "linkedin" -> "Read our full guide!";
            case "youtube" -> "Subscribe for more!";
            case "twitter" -> "Follow for updates!";
            default -> "Visit SporeKart today!";
        };
    }

    private String getContentTheme(int day) {
        var themes = List.of(
            "Educational post about mushroom varieties",
            "Behind-the-scenes at SporeKart farm",
            "Customer success story",
            "Product spotlight",
            "Tips and tricks for growers",
            "Industry news and trends",
            "Community highlight"
        );
        return themes.get(day % themes.size());
    }
}
