package com.sporekart.marketing.copilot.domain;

import java.time.LocalDateTime;
import java.util.List;

public record SocialMediaPost(
    String id,
    String platform,
    String content,
    List<String> hashtags,
    List<String> mediaUrls,
    String callToAction,
    PostType postType,
    LocalDateTime scheduledAt,
    String campaignId,
    String status
) {
    public enum PostType {
        IMAGE, VIDEO, CAROUSEL, STORY, REEL, TEXT_ONLY, POLL, LIVE
    }
}
